package com.example.djshichaoren.googleocrtest2.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class SubtitleSentenceEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "subtitle_id")
    public int subtitleId;

    @ColumnInfo(name = "sentence_id")
    public int sentenceId;

    @ColumnInfo(name = "start_time")
    public long startTime;

    @ColumnInfo(name = "end_time")
    public long endTime;

    @ColumnInfo(name = "sentence_english")
    public String sentenceEnglish;

    @ColumnInfo(name = "sentence_chinese")
    public String sentenceChinese;

    public SubtitleSentenceEntity(){

    }

    @Ignore
    public SubtitleSentenceEntity(int subtitleId, int sentenceId, long startTime, long endTime, String sentenceEnglish, String sentenceChinese) {
        this.subtitleId = subtitleId;
        this.sentenceId = sentenceId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sentenceEnglish = sentenceEnglish;
        this.sentenceChinese = sentenceChinese;
    }
}
