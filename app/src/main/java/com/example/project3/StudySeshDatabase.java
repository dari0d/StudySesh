package com.example.project3;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
        entities = {StudyClass.class, StudySession.class},
        version = 2,
        exportSchema = false
)
public abstract class StudySeshDatabase extends RoomDatabase {

    private static volatile StudySeshDatabase INSTANCE;

    public abstract StudyDao studyDao();

    public static StudySeshDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (StudySeshDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    StudySeshDatabase.class,
                                    "study_sesh_db")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();

                }
            }
        }
        return INSTANCE;
    }
}