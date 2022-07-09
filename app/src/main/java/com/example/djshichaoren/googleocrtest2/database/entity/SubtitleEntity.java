package com.example.djshichaoren.googleocrtest2.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class SubtitleEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;


    @ColumnInfo(name = "name")
    public String name;

    public SubtitleEntity(){

    }

    @Ignore
    public SubtitleEntity(String name) {
        this.name = name;
    }
}
