package com.example.djshichaoren.googleocrtest2.database;


import android.content.Context;

import com.example.djshichaoren.googleocrtest2.database.dao.NewWordDao;
import com.example.djshichaoren.googleocrtest2.database.dao.SubtitleDao;
import com.example.djshichaoren.googleocrtest2.database.dao.SubtitleSentenceDao;
import com.example.djshichaoren.googleocrtest2.database.dao.WordSceneDao;
import com.example.djshichaoren.googleocrtest2.database.entity.NewWordEntity;
import com.example.djshichaoren.googleocrtest2.database.entity.SubtitleEntity;
import com.example.djshichaoren.googleocrtest2.database.entity.SubtitleSentenceEntity;
import com.example.djshichaoren.googleocrtest2.database.entity.WordSceneEntity;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(
        entities = {SubtitleEntity.class, WordSceneEntity.class, SubtitleSentenceEntity.class},
        version = 1,
        exportSchema = false
)
public abstract class SubtitleDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "subtitle_database";

    private static volatile SubtitleDatabase mInstance;

    private static Migration migration = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE WordSceneEntity ADD UNIQUE INDEX subtitle_id");
        }
    };

    public static SubtitleDatabase getInstance(Context context){
        if(mInstance == null){
            synchronized (SubtitleDatabase.class){
                if(mInstance == null){
                    mInstance = Room.databaseBuilder(context.getApplicationContext(), SubtitleDatabase.class, DATABASE_NAME)
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .addMigrations(migration).build();
                }
            }
        }

        return mInstance;
    }

    public abstract SubtitleDao getSubtitleDao();

    public abstract SubtitleSentenceDao getSubtitleSentenceDao();

    public abstract WordSceneDao getWordSceneDao();
}
