package com.example.djshichaoren.googleocrtest2.util;

import static com.example.djshichaoren.googleocrtest2.config.Config.BASE_FOLDER_NAME;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

    public static void writeTheInputStreamToLocalFile(InputStream inputStream, String outputFileName, Context context){
        File file = createFile(outputFileName, context);
        if (file == null) return;
        try{
            long size = 0;
            BufferedInputStream in = new BufferedInputStream(inputStream);
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            int len = -1;
            byte[] b = new byte[1024];
            while((len = in.read(b)) != -1){
                out.write(b, 0, len);
                size += len;
            }
            in.close();
            out.close();
        }
        catch (Exception exception){

        }

    }

    public static boolean createDirectory(String directoryPath){
        // 创建文件夹
        File dir = new File(directoryPath);
        if (!dir.exists()) {
            boolean res = dir.mkdirs();
            return res;
        }
        else{
            return true;
        }
    }

    public static File createFile(String fileName, Context context){
        try {
            String directoryPath = context.getApplicationContext().getFilesDir().getAbsolutePath() + "/" + "subtitles";
            if(createDirectory(directoryPath)){

                File file = new File(directoryPath, fileName);
                file.createNewFile();
                return file;
            }
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        return null;

    }

    public static File getSubtitleFile(Context context, String fileName){
        String directoryPath = context.getApplicationContext().getFilesDir().getAbsolutePath() + "/" + "subtitles";
        File dir = new File(directoryPath);
        if(dir.exists()){
            return new File(directoryPath, fileName);
        }
        return null;
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
