package com.example.djshichaoren.googleocrtest2.http.bean;

import java.util.List;

/**
 * 类描述：
 * 修改人：DJSHICHAOREN
 * 修改时间：2019/8/2 20:47
 * 修改备注：
 */
public class JinshanTranslation extends BaseBean {
    private String word_name;

//    private Exchange exchange;

    private List<Symbol> symbols;


    public List<Symbol> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<Symbol> symbols) {
        this.symbols = symbols;
    }

//    public Exchange getExchange() {
//        return exchange;
//    }
//
//    public void setExchange(Exchange exchange) {
//        this.exchange = exchange;
//    }

    public String getWord_name() {
        return word_name;
    }

    public void setWord_name(String word_name) {
        this.word_name = word_name;
    }

//    public class Exchange{
//        public String word_pl;
//        public String word_third;
//        public String word_past;
//        public String word_done;
//        public String word_ing;
//        public String word_er;
//        public String word_est;
//
//        public String getWord_pl() {
//            return word_pl;
//        }
//
//        public void setWord_pl(String word_pl) {
//            this.word_pl = word_pl;
//        }
//
//        public String getWord_past() {
//            return word_past;
//        }
//
//        public void setWord_past(String word_past) {
//            this.word_past = word_past;
//        }
//
//        public String getWord_done() {
//            return word_done;
//        }
//
//        public void setWord_done(String word_done) {
//            this.word_done = word_done;
//        }
//
//        public String getWord_ing() {
//            return word_ing;
//        }
//
//        public void setWord_ing(String word_ing) {
//            this.word_ing = word_ing;
//        }
//
//        public String getWord_er() {
//            return word_er;
//        }
//
//        public void setWord_er(String word_er) {
//            this.word_er = word_er;
//        }
//
//        public String getWord_est() {
//            return word_est;
//        }
//
//        public void setWord_est(String word_est) {
//            this.word_est = word_est;
//        }
//
//
//
//        public String getWord_third() {
//            return word_third;
//        }
//
//        public void setWord_third(String word_third) {
//            this.word_third = word_third;
//        }
//    }

    public class Symbol{
        public String ph_en;
        public String ph_am;
        public String ph_other;
        public String ph_en_mp3;
        public String ph_am_mp3;
        public String ph_tts_mp3;
        public List<Part> parts;

        public String getPh_en() {
            return ph_en;
        }

        public void setPh_en(String ph_en) {
            this.ph_en = ph_en;
        }

        public String getPh_am() {
            return ph_am;
        }

        public void setPh_am(String ph_am) {
            this.ph_am = ph_am;
        }

        public String getPh_other() {
            return ph_other;
        }

        public void setPh_other(String ph_other) {
            this.ph_other = ph_other;
        }

        public String getPh_en_mp3() {
            return ph_en_mp3;
        }

        public void setPh_en_mp3(String ph_en_mp3) {
            this.ph_en_mp3 = ph_en_mp3;
        }

        public String getPh_am_mp3() {
            return ph_am_mp3;
        }

        public void setPh_am_mp3(String ph_am_mp3) {
            this.ph_am_mp3 = ph_am_mp3;
        }

        public String getPh_tts_mp3() {
            return ph_tts_mp3;
        }

        public void setPh_tts_mp3(String ph_tts_mp3) {
            this.ph_tts_mp3 = ph_tts_mp3;
        }

        public List<Part> getParts() {
            return parts;
        }

        public void setParts(List<Part> parts) {
            this.parts = parts;
        }

        public class Part{
            public String part;
            public List<String> means;

            public String getPart() {
                return part;
            }

            public void setPart(String part) {
                this.part = part;
            }

            public List<String> getMeans() {
                return means;
            }

            public void setMeans(List<String> means) {
                this.means = means;
            }

        }
    }
}
