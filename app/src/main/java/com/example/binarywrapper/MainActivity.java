package com.example.binarywrapper;

import com.example.binarywrapper.DumpFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;




public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "MainActivity";
    public static final String LOG_FILE = "binwrapper_log.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create Log File
        Date d = new Date();
        DumpFile dump = new DumpFile();
        dump.writeToFile(getApplicationContext(), LOG_FILE, d.toString() + "\n");

        // Check lib folder. This is the path where packaged .so files
        // will be dumped into the device. The same path where executables
        // will also be kept as .so files
        String command = "ls -l " + getApplicationInfo().nativeLibraryDir;
        dump.appendToFile(getApplicationContext(), LOG_FILE, command + "\n");
        dump.appendToFile(getApplicationContext(), LOG_FILE, runCommand(command) + "\n");

        // Execute binary. This app will not run out of the box. Values need
        // to be updated with exact values.
        // #CHANGEME
        List<String> commands = Arrays.asList("./binary.so"
            ,"arg1"
            ,"arg2");
        String exec_command = Arrays.toString(commands.toArray());
        dump.appendToFile(getApplicationContext(), LOG_FILE, exec_command + "\n");
        dump.appendToFile(getApplicationContext(), LOG_FILE, command + "\n");
        Log.d(LOG_TAG, "Read data : " + dump.readFromFile(getApplicationContext(), LOG_FILE));
    }

    private String runCommand(List<String> commands){
        StringBuilder output = new StringBuilder();
        try{
            ProcessBuilder builder = new ProcessBuilder();
            // Change to execute directory
            builder.directory(new File(getApplicationInfo().nativeLibraryDir));
            Map<String, String> env_kv = builder.environment();
            env_kv.put("LD_LIBRARY_PATH", "/system/vendor/lib64"); // #CHANGEME
            builder.command(commands);
            builder.redirectErrorStream(true);

            Log.d(LOG_TAG, "Starting process");
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            Log.d(LOG_TAG, "Capturing output");
            while((line = reader.readLine()) != null){
                output.append(line + "\n");
            }
            reader.close();
            process.waitFor();
            Log.d(LOG_TAG, "Process finished");
        } catch (IOException e) {
            Log.e(LOG_TAG, "IO Exception", e);
        } catch (InterruptedException e) {
            Log.e(LOG_TAG, "Interrupted Exception", e);
        }
        return output.toString();
    }
    private String runCommand(String command) {
        StringBuilder output = new StringBuilder();
        try {
            Log.d(LOG_TAG, "Exec process");
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            Log.d(LOG_TAG, "Capturing output");
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
                output.append("\n");
            }

            reader.close();
            process.waitFor();
            Log.d(LOG_TAG, "Process finished");
        } catch (IOException e) {
            Log.e(LOG_TAG, "IO Exception", e);
        } catch (InterruptedException e) {
            Log.e(LOG_TAG, "Interrupted Exception", e);
        }

        return output.toString();
    }

    // Helper function to copy Asset files
    private void copyAsset(String filename){
        try {
            Log.d(LOG_TAG, "Copying asset : " + filename);
            InputStream in_file = getAssets().open(filename);
            File targetFile = new File(getApplicationContext().getFilesDir().getPath() + "/" + filename);

            java.nio.file.Files.copy(in_file, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            in_file.close();
        } catch (IOException e) {
            Log.e(LOG_TAG, "IO Exception", e);
        }
    }
}