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


    @Query("SELECT * FROM WordSceneEntity WHERE subtitle_id = :subtitleId AND subtitle_sentence_id = :subtitleSentenceId AND word = :word")
    List<WordSceneEntity> queryWordSceneWithSubtitleIdAndSubtitleSentenceIdAndWord(int subtitleId, int subtitleSentenceId, String word);

    @Query("SELECT * FROM WordSceneEntity WHERE subtitle_id = :subtitleId AND is_new = :isNew ORDER BY subtitle_sentence_position")
    List<WordSceneEntity> queryWordSceneWithSubtitleId(int subtitleId, boolean isNew);

    @Update
    int updateWordScene(WordSceneEntity wordSceneEntity);

}
