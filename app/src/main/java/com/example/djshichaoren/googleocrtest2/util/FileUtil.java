package com.example.djshichaoren.googleocrtest2.util;

import android.content.Context;
import android.util.Log;

import com.example.djshichaoren.googleocrtest2.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileUtil {

    public void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static String readFromFile(Context context, String fileName) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(fileName);

            ret = readFromInputStream(inputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static String readFromResource(Context context, int resourceId) {

        String ret = "";
        InputStream inputStream = context.getResources().openRawResource(resourceId);
        ret = readFromInputStream(inputStream);

        return ret;
    }


    public static String readFromInputStream(InputStream inputStream){
        String ret = "";

        try {
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString).append("\n");
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
}
