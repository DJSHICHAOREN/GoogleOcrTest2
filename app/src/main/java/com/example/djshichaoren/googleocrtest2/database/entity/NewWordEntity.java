package com.example.djshichaoren.googleocrtest2.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class NewWordEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "word")
    public String word;

    @ColumnInfo(name = "is_new")
    public boolean isNew;

    public NewWordEntity(){

    }

    @Ignore
    public NewWordEntity(String word, boolean isNew) {
        this.word = word;
        this.isNew = isNew;
    }
}
