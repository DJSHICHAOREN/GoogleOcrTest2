package com.example.djshichaoren.googleocrtest2.database.dao;

import com.example.djshichaoren.googleocrtest2.database.entity.SubtitleEntity;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

@Dao
public interface SubtitleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertSubtitle(SubtitleEntity subtitleEntity);

}
