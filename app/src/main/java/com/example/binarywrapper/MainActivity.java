package com.example.binarywrapper;

import com.example.binarywrapper.DumpFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import android.content.Context;
import java.io.FileOutputStream;
import java.io.IOException;



public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "MainActivity";
    public static final String LOG_FILE = "binwrapper_log.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String command = "ls -l /sdcard";
        Date d = new Date();

        File dir = getApplicationContext().getFilesDir();
        Log.d(LOG_TAG, "Start writing");
        DumpFile dump = new DumpFile();
        dump.writeToFile(getApplicationContext(), LOG_FILE, d.toString() + "\n");
        dump.appendToFile(getApplicationContext(), LOG_FILE, command + "\n");
        dump.appendToFile(getApplicationContext(), LOG_FILE, runCommand(command) + "\n");
        Log.d(LOG_TAG, "Read data : " + dump.readFromFile(getApplicationContext(), LOG_FILE));
    }

    public String runCommand(String command) {
        StringBuilder output = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
                output.append("\n");
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output.toString();
    }

}