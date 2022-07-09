package com.example.djshichaoren.googleocrtest2.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(primaryKeys={"subtitle_id", "word_scene_id"}
        , foreignKeys = { @ForeignKey(entity = SubtitleEntity.class, parentColumns = "id", childColumns = "subtitle_id", onDelete = ForeignKey.CASCADE),
                            @ForeignKey(entity = NewWordEntity.class, parentColumns = "id", childColumns = "word_scene_id", onDelete = ForeignKey.CASCADE)}
        , indices = {@Index(value = {"subtitle_id", "word_scene_id"}, unique = true)})
public class WordSceneEntity {

    @ColumnInfo(name = "subtitle_id")
    public int subtitleId;

    @ColumnInfo(name = "word_scene_id")
    public int wordSceneId;

    @ColumnInfo(name = "word_translation")
    public String wordTranslation;

    @ColumnInfo(name = "sentence_english")
    public String sentenceEnglish;

    @ColumnInfo(name = "sentence_chinese")
    public String sentenceChinese;
}
