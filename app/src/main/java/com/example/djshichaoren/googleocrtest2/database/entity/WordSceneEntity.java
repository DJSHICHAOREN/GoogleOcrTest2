package com.example.djshichaoren.googleocrtest2.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = { @ForeignKey(entity = SubtitleSentenceEntity.class, parentColumns = "id", childColumns = "subtitle_sentence_id", onDelete = ForeignKey.CASCADE)}
        , indices = {@Index(value = {"subtitle_sentence_id"})})
public class WordSceneEntity implements Comparable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "word")
    public String word;

    @ColumnInfo(name = "subtitle_sentence_id")
    public int subtitleSentenceId;

    @ColumnInfo(name = "subtitle_id")
    public int subtitleId;

    @ColumnInfo(name = "word_translation")
    public String wordTranslation;

    @ColumnInfo(name = "subtitle_sentence_position")
    public int subtitleSentencePosition;



    @ColumnInfo(name = "is_new")
    public boolean isNew;

    public WordSceneEntity(){

    }

    @Ignore
    public WordSceneEntity(int subtitleSentenceId, int subtitleId, String wordTranslation, boolean isNew, int subtitleSentencePosition, String word) {
        this.subtitleSentenceId = subtitleSentenceId;
        this.wordTranslation = wordTranslation;
        this.isNew = isNew;
        this.subtitleId = subtitleId;
        this.subtitleSentencePosition = subtitleSentencePosition;
        this.word = word;
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof WordSceneEntity){
            WordSceneEntity wordSceneEntity = (WordSceneEntity)o;
            return this.subtitleSentenceId - wordSceneEntity.subtitleSentenceId;
        }
        return 0;
    }
}
