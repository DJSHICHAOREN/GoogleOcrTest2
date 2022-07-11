package com.example.djshichaoren.googleocrtest2.database.dao;


import com.example.djshichaoren.googleocrtest2.database.entity.WordSceneEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface WordSceneDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertNewWord(WordSceneEntity wordSceneEntity);


    @Query("SELECT * FROM WordSceneEntity WHERE subtitle_sentence_id = :subtitleSentenceId AND new_word_id = :newWordId")
    List<WordSceneEntity> queryWordSceneWithSubtitleSentenceIdAndNewWordId(int subtitleSentenceId, int newWordId);

    @Update
    int updateWordScene(WordSceneEntity wordSceneEntity);

}
