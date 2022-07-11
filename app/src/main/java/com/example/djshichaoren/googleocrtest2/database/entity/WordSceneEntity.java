package com.example.djshichaoren.googleocrtest2.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(primaryKeys={"subtitle_sentence_id", "new_word_id"}
        , foreignKeys = { @ForeignKey(entity = SubtitleSentenceEntity.class, parentColumns = "id", childColumns = "subtitle_sentence_id", onDelete = ForeignKey.CASCADE),
                            @ForeignKey(entity = NewWordEntity.class, parentColumns = "id", childColumns = "new_word_id", onDelete = ForeignKey.CASCADE)}
        , indices = {@Index(value = {"subtitle_sentence_id", "new_word_id"}, unique = true)})
public class WordSceneEntity {

    @ColumnInfo(name = "subtitle_sentence_id")
    public int subtitleSentenceId;

    @ColumnInfo(name = "new_word_id")
    public int newWordId;

    @ColumnInfo(name = "word_translation")
    public String wordTranslation;

    @ColumnInfo(name = "is_new")
    public boolean isNew;

    public WordSceneEntity(){

    }

    @Ignore
    public WordSceneEntity(int subtitleSentenceId, int newWordId, String wordTranslation, boolean isNew) {
        this.subtitleSentenceId = subtitleSentenceId;
        this.newWordId = newWordId;
        this.wordTranslation = wordTranslation;
        this.isNew = isNew;
    }
}
