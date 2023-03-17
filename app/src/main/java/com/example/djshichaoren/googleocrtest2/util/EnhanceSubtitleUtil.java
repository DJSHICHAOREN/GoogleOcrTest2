package com.example.djshichaoren.googleocrtest2.util;

import android.util.Log;

import com.example.djshichaoren.googleocrtest2.subtitle_api.subtitle.srt.SRTLine;
import com.example.djshichaoren.googleocrtest2.subtitle_api.subtitle.srt.SRTSub;
import com.example.djshichaoren.googleocrtest2.util.text.StringCleaner;

import java.util.ArrayList;
import java.util.List;

public class EnhanceSubtitleUtil {
    private SRTSub mAssistSubtitle;

    private List<List<Integer>> mCandidateListList = new ArrayList<>();
    private int mNowCurrentListIndicator = 0;
    private int mCandidateListListCount = 3;
    private int mCurrentMatchedIndex = -1; // -1:没有match x:当前匹配的index
    private int mLastMatchedSubtitleIndex = -1;
    public EnhanceSubtitleUtil() {
        for (int i=0; i < mCandidateListListCount; i++) {
            mCandidateListList.add(new ArrayList<>());
        }
    }

    public String enhance(String content) {
        if (mAssistSubtitle == null) return content;

        int result = tryToMatchContent(content);
        if (result != -1) {
            SRTLine srtLine = mAssistSubtitle.getSrtLine(result);
            return srtLine.getEnglishString();
        }

        return content;
    }

    public int tryToMatchContent(String content) {
        mNowCurrentListIndicator = mNowCurrentListIndicator % mCandidateListList.size();
        List<Integer> candidateList = mCandidateListList.get(mNowCurrentListIndicator);

        if (mLastMatchedSubtitleIndex != -1) {
            // 先从最近找到的开始查询
            for (int i=mLastMatchedSubtitleIndex; i < mLastMatchedSubtitleIndex + 3; i++) {
                if (i > mAssistSubtitle.getLength()){
                    break;
                }
                SRTLine srtLine = mAssistSubtitle.getSrtLine(i);
                if (isContentSimilarToSubtitleSimplified(content, srtLine.getEnglishWordList())) {
                    mLastMatchedSubtitleIndex = i;
                    return i;
                }
            }
            mLastMatchedSubtitleIndex = -1;

//            for (int i=mLastMatchedSubtitleIndex; i < mLastMatchedSubtitleIndex + 3; i++) {
//                if (i > mAssistSubtitle.getLength()){
//                    break;
//                }
//                SRTLine srtLine = mAssistSubtitle.getSrtLine(i);
//                if (isContentSimilarToSubtitleSimplified(content, srtLine.getEnglishWordList())) {
//                    mLastMatchedSubtitleIndex = i;
//                    return i;
//                }
//            }
//
//            for (int i = mLastMatchedSubtitleIndex; i > 0 ; i--) {
//                SRTLine srtLine = mAssistSubtitle.getSrtLine(i);
//                if (isContentSimilarToSubtitle(content, srtLine.getEnglishWordList())) {
//                    mLastMatchedSubtitleIndex = i;
//                    return i;
//                }
//            }
        }
        else {
            for (int i=0; i < mAssistSubtitle.getLength(); i++) {
                SRTLine srtLine = mAssistSubtitle.getSrtLine(i);
                if (isContentSimilarToSubtitle(content, srtLine.getEnglishWordList())) {
                    candidateList.add(i);
                    mLastMatchedSubtitleIndex = i;
                    return i;
                }
            }
        }



        return -1;
    }

    public void setAssistSubtitle(SRTSub assistSubtitle) {
        mAssistSubtitle = assistSubtitle;
    }

    private boolean isContentSimilarToSubtitle(String content, List<String> subtitleTextList) {
        if (content == null || subtitleTextList == null) return false;

        content = StringCleaner.cleanSentenceString(content);
        content = StringCleaner.removeStringBlank(content);
        content = StringCleaner.replace_l_to_i(content);

        StringBuilder subtitleStringBuilder = new StringBuilder();
        for (int i=0; i < subtitleTextList.size(); i++) {
            subtitleStringBuilder.append(subtitleTextList.get(i));
        }
        String subtitleString = StringCleaner.cleanSentenceString(subtitleStringBuilder.toString());
        subtitleString = StringCleaner.removeStringBlank(subtitleString);
//        boolean isTheAnimSentence = false;
//        if (subtitleString.indexOf("thisuvsuit") != -1) {
//            Log.d("lwd", "hello");
//            isTheAnimSentence = true;
//        }

        int notFoundWord = 0;
        int contentRedundant = 0;
        for (int i=0; i < subtitleTextList.size(); i++) {
            String subtitleWord = StringCleaner.cleanSentenceString(subtitleTextList.get(i));
            subtitleWord = StringCleaner.removeStringBlank(subtitleWord);
            subtitleWord = StringCleaner.replace_l_to_i(subtitleWord);

            int wordIndex = content.indexOf(subtitleWord);
            if (wordIndex != -1) {

                if (wordIndex != 0) {
                    contentRedundant += wordIndex;
                }

                // 去除已经匹配到的单词及其前面全部的字符
                if (wordIndex + subtitleWord.length() < content.length()) {
                    content = content.substring(wordIndex + subtitleWord.length());
                }
            } else {
                notFoundWord++;
//                Log.d("lwd", "notFound:" + subtitleWord);
            }
        }
        contentRedundant += content.length();

//        if (isTheAnimSentence) {
//            Log.d("lwd", "notFoundWord:" + notFoundWord + " contentRedundant:" + contentRedundant);
//        }

        String msg = "content:" + content + " subtitleString:" + subtitleString
                + " notFoundWord:" + notFoundWord + " contentRedundant:" + contentRedundant;
        if (((subtitleTextList.size() <= 3 && notFoundWord == 0)
                || (3 < subtitleTextList.size() && subtitleTextList.size() <= 5 && notFoundWord < 2)
                || (subtitleTextList.size() > 5 && notFoundWord < 3)) && contentRedundant < 12 ) {
            Log.d("lwd", "success:" + msg);

            return true;
        }
        else {
            Log.d("lwd", "failed:" + msg);
        }

        return false;

    }

    private boolean isContentSimilarToSubtitleSimplified(String content, List<String> subtitleTextList) {
        if (content == null || subtitleTextList == null) return false;

        content = StringCleaner.cleanSentenceString(content);
        content = StringCleaner.removeStringBlank(content);
        content = StringCleaner.replace_l_to_i(content);

        StringBuilder subtitleStringBuilder = new StringBuilder();
        for (int i=0; i < subtitleTextList.size(); i++) {
            subtitleStringBuilder.append(subtitleTextList.get(i));
        }
        String subtitleString = StringCleaner.cleanSentenceString(subtitleStringBuilder.toString());
        subtitleString = StringCleaner.removeStringBlank(subtitleString);

        boolean isTheAnimSentence = false;
        if (subtitleString.indexOf("backwheniwas") != -1) {
            Log.d("lwd", "hello");
            isTheAnimSentence = true;
        }

        int foundWord = 0;
        for (int i=0; i < subtitleTextList.size(); i++) {
            String subtitleWord = StringCleaner.cleanSentenceString(subtitleTextList.get(i));
            subtitleWord = StringCleaner.removeStringBlank(subtitleWord);
            subtitleWord = StringCleaner.replace_l_to_i(subtitleWord);


            int wordIndex = content.indexOf(subtitleWord);
            if (wordIndex != -1) {
                foundWord++;

            } else {
            }
        }

//        if (isTheAnimSentence) {
//            Log.d("lwd", "foundWord:" + foundWord);
//        }:

        String msg = "content:" + content + " subtitleString:" + subtitleString
                + " foundWord:" + foundWord + " subtitleTextList.size():" + subtitleTextList.size()
                + " 1:" + (foundWord > 0) + " 2:" + (subtitleTextList.size() < 5 && foundWord > 1)
                + " 3:" + ((float)foundWord / subtitleTextList.size() >= 0.5);
        if (foundWord > 0
                &&
                ( (subtitleTextList.size() < 5 && foundWord > 1) ||(foundWord / subtitleTextList.size() >= 0.5))  ) {
            Log.d("lwd", "Simplified success :" + msg + " ");
            return true;
        }
        else {
            Log.d("lwd", "Simplified failed :" + msg);
        }

        return false;

    }
}
