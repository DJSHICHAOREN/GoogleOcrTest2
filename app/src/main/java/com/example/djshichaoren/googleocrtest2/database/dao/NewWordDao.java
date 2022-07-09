package com.example.djshichaoren.googleocrtest2.database.dao;

import com.example.djshichaoren.googleocrtest2.database.entity.NewWordEntity;
import com.example.djshichaoren.googleocrtest2.database.entity.SubtitleEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface NewWordDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertNewWord(NewWordEntity newWordEntity);


    @Query("SELECT * FROM NewWordEntity WHERE word = :word")
    List<NewWordEntity> queryNewWordWithWordString(String word);

    @Update
    int updateNewWord(NewWordEntity newWordEntity);
}
