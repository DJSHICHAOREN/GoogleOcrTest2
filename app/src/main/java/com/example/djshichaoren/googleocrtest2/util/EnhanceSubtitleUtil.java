package com.example.djshichaoren.googleocrtest2.util;

import android.util.Log;

import com.example.djshichaoren.googleocrtest2.subtitle_api.subtitle.srt.SRTLine;
import com.example.djshichaoren.googleocrtest2.subtitle_api.subtitle.srt.SRTSub;
import com.example.djshichaoren.googleocrtest2.util.text.StringCleaner;

import java.util.ArrayList;
import java.util.Arrays;
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

        if (mCurrentMatchedIndex != -1) {
            mCurrentMatchedIndex++;
            if (mCurrentMatchedIndex >= mAssistSubtitle.getLength()) return content;
            SRTLine srtLine = mAssistSubtitle.getSrtLine(mCurrentMatchedIndex);
            if (isContentSimilarToSubtitle(content, srtLine.getEnglishWordList())) {
                return srtLine.getEnglishString();
            }
            else {
                mCurrentMatchedIndex = -1;
                return content;
            }
        }
        else {
            int result = tryToMatchContent(content);
            if (result != -1) {
                SRTLine srtLine = mAssistSubtitle.getSrtLine(result);
//                mCurrentMatchedIndex = result;
                return srtLine.getEnglishString();
            }

        }

        return content;
    }

    public int tryToMatchContent(String content) {
        mNowCurrentListIndicator = mNowCurrentListIndicator % mCandidateListList.size();
        List<Integer> candidateList = mCandidateListList.get(mNowCurrentListIndicator);

        if (mLastMatchedSubtitleIndex != -1) {
            for (int i=mLastMatchedSubtitleIndex; i < mAssistSubtitle.getLength(); i++) {
                SRTLine srtLine = mAssistSubtitle.getSrtLine(i);
                if (isContentSimilarToSubtitle2(content, srtLine.getEnglishWordList())) {
                    mLastMatchedSubtitleIndex = i;
                    return i;
                }
            }

            for (int i = mLastMatchedSubtitleIndex; i > 0 ; i--) {
                SRTLine srtLine = mAssistSubtitle.getSrtLine(i);
                if (isContentSimilarToSubtitle2(content, srtLine.getEnglishWordList())) {
                    mLastMatchedSubtitleIndex = i;
                    return i;
                }
            }
        }
        else {
            for (int i=0; i < mAssistSubtitle.getLength(); i++) {
                SRTLine srtLine = mAssistSubtitle.getSrtLine(i);
                if (isContentSimilarToSubtitle2(content, srtLine.getEnglishWordList())) {
                    candidateList.add(i);
                    mLastMatchedSubtitleIndex = i;
                    return i;
                }
            }
        }

        mLastMatchedSubtitleIndex = -1;


        // 倒着查询，看是否有连着三个都匹配上
//        boolean matched = false;
//        int matchedValue = -1;
//        for (int i=0; i < candidateList.size(); i++) {
//            int value = c bandidateList.get(i);
//
//            for (int j=1; j < mCandidateListList.size(); j++) {
//                if (mCandidateListList.get((i - j + mCandidateListListCount)%mCandidateListListCount).contains(value - j)){
//                    matched = true;
//                    matchedValue = value;
//                }
//            }
//        }
//
//        if (matched) {
//            return mNowCurrentListIndicator;
//        }

//        return matchedValue;
        return -1;
    }

    public void setAssistSubtitle(SRTSub assistSubtitle) {
        mAssistSubtitle = assistSubtitle;
    }

    private boolean isContentSimilarToSubtitle(String content, List<String> subtitleTextList) {
        if (content == null || subtitleTextList == null) return false;

        String newContent = content.toLowerCase();
        int containWordCount = 0;
        for (int i=0; i < subtitleTextList.size(); i++) {
            String word = subtitleTextList.get(i);
            if (newContent.contains(word.toLowerCase())) {
                containWordCount++;
            }
        }

        if (containWordCount > 5) {
            Log.d("lwd", "bigger than 5");
        }
        if ( (subtitleTextList.size() <=3 &&  subtitleTextList.size() == containWordCount)
                || (subtitleTextList.size() > 3 && subtitleTextList.size() - containWordCount < 2) ) {

            Log.d("lwd", "isContentSimilarToSubtitle one similar content：" + newContent);
            return true;
        }
        else {
            Log.d("lwd", "failed");
        }
        return false;
    }

    private boolean isContentSimilarToSubtitle2(String content, List<String> subtitleTextList) {
        if (content == null || subtitleTextList == null) return false;

        content = StringCleaner.cleanSentenceString(content);
        content = StringCleaner.removeStringBlank(content);

        StringBuilder subtitleStringBuilder = new StringBuilder();
        for (int i=0; i < subtitleTextList.size(); i++) {
            subtitleStringBuilder.append(subtitleTextList.get(i));
        }
        String subtitleString = StringCleaner.cleanSentenceString(subtitleStringBuilder.toString());
        subtitleString = StringCleaner.removeStringBlank(subtitleString);
        boolean isTheAnimSentence = false;
        if (subtitleString.indexOf("ifthebibletellsyou") != -1) {
            Log.d("lwd", "hello");
            isTheAnimSentence = true;
        }

        int notFoundWord = 0;
        int contentRedundant = 0;
        for (int i=0; i < subtitleTextList.size(); i++) {
            String subtitleWord = StringCleaner.cleanSentenceString(subtitleTextList.get(i));
            subtitleWord = StringCleaner.removeStringBlank(subtitleWord);

            int wordIndex = content.indexOf(subtitleWord);
            if (wordIndex != -1) {

                if (wordIndex != 0) {
                    contentRedundant += wordIndex;
                }

                if (wordIndex + subtitleWord.length() < content.length()) {
                    content = content.substring(wordIndex + subtitleWord.length());
                }
            } else {
                notFoundWord++;
//                Log.d("lwd", "notFound:" + subtitleWord);
            }
        }
        contentRedundant += content.length();

        if (isTheAnimSentence) {
            Log.d("lwd", "notFoundWord:" + notFoundWord + " contentRedundant:" + contentRedundant);
        }

        if (((subtitleTextList.size() <= 3 && notFoundWord == 0)
                || (3 < subtitleTextList.size() && subtitleTextList.size() <= 5 && notFoundWord < 2)
                || (subtitleTextList.size() > 5 && notFoundWord < 3)) && contentRedundant < 12 ) {
            return true;
        }

        return false;

    }
}
