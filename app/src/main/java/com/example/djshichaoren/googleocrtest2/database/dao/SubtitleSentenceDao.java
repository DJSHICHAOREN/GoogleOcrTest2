package com.example.djshichaoren.googleocrtest2.database.dao;


import com.example.djshichaoren.googleocrtest2.database.entity.NewWordEntity;
import com.example.djshichaoren.googleocrtest2.database.entity.SubtitleSentenceEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface SubtitleSentenceDao {

    @Query("SELECT * FROM SubtitleSentenceEntity WHERE subtitle_id = :subtitleId AND  sentence_id = :sentenceId")
    List<SubtitleSentenceEntity> querySubtitleSentenceWidthSubtitleId(int subtitleId, int sentenceId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertSubtitleSentence(SubtitleSentenceEntity subtitleSentenceEntity);

}
