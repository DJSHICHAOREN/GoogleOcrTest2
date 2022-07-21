package com.example.djshichaoren.googleocrtest2.database.dao;

import com.example.djshichaoren.googleocrtest2.database.entity.SubtitleEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface SubtitleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertSubtitle(SubtitleEntity subtitleEntity);

    @Query("SELECT * FROM SubtitleEntity WHERE name = :name")
    List<SubtitleEntity> querySubtitleByName(String name);

    @Query("SELECT * FROM SubtitleEntity")
    List<SubtitleEntity> queryAllSubtitle();

}
