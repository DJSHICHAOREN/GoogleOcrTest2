package com.example.djshichaoren.googleocrtest2.http.bean;

import java.util.List;

public class SubtitleSearchResult extends BaseBean{
    public int status;

    public OutSubEntity sub;

    public static class OutSubEntity{

        public List<SubEntity> subs;

        public String action;
        public String keyword;
        public String result;
    }

    public static class SubEntity{
        public String native_name;
        public String videoname;
        public int revision;
        public String subtype;
        public String upload_time;
        public int vote_score;
        public int id;
        public String release_site;
        public OutLangEntity lang;
    }

    public static class OutLangEntity{
        public LangEntity langlist;
        public String desc;
    }

    public static class LangEntity{
        public String langdou;
        public String langkor;
    }
}
