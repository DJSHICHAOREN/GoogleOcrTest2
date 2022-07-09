package com.example.djshichaoren.googleocrtest2.database;

import android.content.Context;

import com.example.djshichaoren.googleocrtest2.database.dao.NewWordDao;
import com.example.djshichaoren.googleocrtest2.database.entity.NewWordEntity;

import java.util.List;

public class SubtitleDatabaseUtil {

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
}
