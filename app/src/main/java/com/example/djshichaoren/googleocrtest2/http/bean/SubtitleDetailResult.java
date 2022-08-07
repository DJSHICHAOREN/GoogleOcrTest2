package com.example.djshichaoren.googleocrtest2.http.bean;

import java.util.List;

public class SubtitleDetailResult extends BaseBean{
    public int status;

    public OutSubEntity sub;
    public static class OutSubEntity{
        public List<SubEntity> subs;
        public String result;
        public String action;
    }

    public static class SubEntity{
        public List<FileEntity> filelist;
    }

    public static class FileEntity{
        public String url;
        public String f;
        public String s;
    }


}
