package com.example.djshichaoren.googleocrtest2.database;

import android.content.Context;

import com.example.djshichaoren.googleocrtest2.database.dao.NewWordDao;
import com.example.djshichaoren.googleocrtest2.database.dao.SubtitleDao;
import com.example.djshichaoren.googleocrtest2.database.dao.SubtitleSentenceDao;
import com.example.djshichaoren.googleocrtest2.database.dao.WordSceneDao;
import com.example.djshichaoren.googleocrtest2.database.entity.NewWordEntity;
import com.example.djshichaoren.googleocrtest2.database.entity.SubtitleEntity;
import com.example.djshichaoren.googleocrtest2.database.entity.SubtitleSentenceEntity;
import com.example.djshichaoren.googleocrtest2.database.entity.WordSceneEntity;

import java.util.List;

public class SubtitleDatabaseUtil {

    // 单词表
    public static void updateNewWordEntity(Context context, NewWordEntity newWordEntity){
        SubtitleDatabase subtitleDatabase = SubtitleDatabase.getInstance(context);
        NewWordDao newWordDao = subtitleDatabase.getNewWordDao();

        newWordDao.updateNewWord(newWordEntity);
    }

    public static NewWordEntity insertNewWordEntity(Context context, String word){
        SubtitleDatabase subtitleDatabase = SubtitleDatabase.getInstance(context);
        NewWordDao newWordDao = subtitleDatabase.getNewWordDao();

        NewWordEntity newWordEntity = new NewWordEntity(word, true);
        long id = newWordDao.insertNewWord(newWordEntity);
        newWordEntity.id = (int)id;
        return newWordEntity;
    }

    public static NewWordEntity getNewWordEntity(Context context, String word){
        SubtitleDatabase subtitleDatabase = SubtitleDatabase.getInstance(context);
        NewWordDao newWordDao = subtitleDatabase.getNewWordDao();

        List<NewWordEntity> newWordEntityList = newWordDao.queryNewWordWithWordString(word);
        if(newWordEntityList != null && newWordEntityList.size() > 0){
            return newWordEntityList.get(0);
        }

        return null;
    }

    // 字幕表
    public static SubtitleEntity getSubtitleEntity(Context context, String name){
        SubtitleDatabase subtitleDatabase = SubtitleDatabase.getInstance(context);
        SubtitleDao subtitleDao = subtitleDatabase.getSubtitleDao();

        List<SubtitleEntity> subtitleEntityList = subtitleDao.querySubtitleByName(name);
        if(subtitleEntityList != null && subtitleEntityList.size() > 0){
            return subtitleEntityList.get(0);
        }

        return null;
    }

    public static SubtitleEntity insertSubtitleEntity(Context context, String name){
        SubtitleDatabase subtitleDatabase = SubtitleDatabase.getInstance(context);
        SubtitleDao subtitleDao = subtitleDatabase.getSubtitleDao();

        SubtitleEntity subtitleEntity = new SubtitleEntity(name);
        long id = subtitleDao.insertSubtitle(subtitleEntity);
        subtitleEntity.id = (int)id;
        return subtitleEntity;
    }

    // 句子表
    public static SubtitleSentenceEntity getSubtitleSentenceEntity(Context context, int subtitleId, int sentenceId){
        SubtitleDatabase subtitleDatabase = SubtitleDatabase.getInstance(context);
        SubtitleSentenceDao subtitleSentenceDao = subtitleDatabase.getSubtitleSentenceDao();

        List<SubtitleSentenceEntity> subtitleSentenceEntityList = subtitleSentenceDao.querySubtitleSentenceWidthSubtitleId(subtitleId, sentenceId);
        if(subtitleSentenceEntityList != null && subtitleSentenceEntityList.size() > 0){
            return subtitleSentenceEntityList.get(0);
        }

        return null;
    }

    public static SubtitleSentenceEntity insertSubtitleSentenceEntity(Context context, int subtitleId, int sentenceId, long startTime, long endTime, String sentenceEnglish, String sentenceChinese){
        SubtitleDatabase subtitleDatabase = SubtitleDatabase.getInstance(context);
        SubtitleSentenceDao subtitleSentenceDao = subtitleDatabase.getSubtitleSentenceDao();

        SubtitleSentenceEntity subtitleSentenceEntity = new SubtitleSentenceEntity(subtitleId, sentenceId, startTime, endTime, sentenceEnglish, sentenceChinese);
        long id = subtitleSentenceDao.insertSubtitleSentence(subtitleSentenceEntity);
        subtitleSentenceEntity.id = (int)id;
        return subtitleSentenceEntity;
    }

    // 生词情景表
    public static void updateWordSceneEntity(Context context, WordSceneEntity wordSceneEntity){
        SubtitleDatabase subtitleDatabase = SubtitleDatabase.getInstance(context);
        WordSceneDao wordSceneDao = subtitleDatabase.getWordSceneDao();

        wordSceneDao.updateWordScene(wordSceneEntity);
    }

    public static WordSceneEntity insertWordSceneEntity(Context context, int subtitleSentenceId, int newWordId, String wordTranslation, boolean isNew){
        SubtitleDatabase subtitleDatabase = SubtitleDatabase.getInstance(context);
        WordSceneDao wordSceneDao = subtitleDatabase.getWordSceneDao();

        WordSceneEntity wordSceneEntity = new WordSceneEntity(subtitleSentenceId, newWordId, wordTranslation, isNew);
        wordSceneDao.insertNewWord(wordSceneEntity);
        return wordSceneEntity;
    }

    public static WordSceneEntity getWordSceneEntity(Context context, int subtitleSentenceId, int newWordId){
        SubtitleDatabase subtitleDatabase = SubtitleDatabase.getInstance(context);
        WordSceneDao wordSceneDao = subtitleDatabase.getWordSceneDao();

        List<WordSceneEntity> wordSceneEntityList = wordSceneDao.queryWordSceneWithSubtitleSentenceIdAndNewWordId(subtitleSentenceId, newWordId);
        if(wordSceneEntityList != null && wordSceneEntityList.size() > 0){
            return wordSceneEntityList.get(0);
        }

        return null;
    }


}
