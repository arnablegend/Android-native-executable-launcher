package com.example.binarywrapper;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

class DumpFile {
    public void writeToFile(Context context, String fileName, String content) {
        FileOutputStream outputStream = null;
        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void appendToFile(Context context, String fileName, String content) {
        FileOutputStream outputStream = null;
        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_APPEND);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String readFromFile(Context context, String fileName) {
        StringBuilder content = new StringBuilder();

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(context.openFileInput(fileName));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
                content.append("\n");
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }
}