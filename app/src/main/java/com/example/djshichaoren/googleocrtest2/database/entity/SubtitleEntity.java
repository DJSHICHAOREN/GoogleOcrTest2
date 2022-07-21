package com.example.djshichaoren.googleocrtest2.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class SubtitleEntity implements Serializable {

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
