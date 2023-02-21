package com.example.djshichaoren.googleocrtest2.config;

import android.os.Environment;

import java.io.File;

/**
 * 类描述：
 * 修改人：DJSHICHAOREN
 * 修改时间：2019/8/2 18:40
 * 修改备注：
 */
public class Config {
    public static String BASE_URL = "";

    // String url = "http://dict-co.iciba.com/api/dictionary.php?w=" + word + "&type=json&key=9AA9FA4923AC16CED1583C26CF284C3F";
    public static final File BASE_DIR = Environment.getExternalStorageDirectory();
    public static final String BASE_FOLDER_NAME = BASE_DIR + "/" + "看电影记单词";
}
