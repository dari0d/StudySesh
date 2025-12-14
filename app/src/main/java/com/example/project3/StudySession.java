package com.example.project3;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "sessions",
        foreignKeys = @ForeignKey(
                entity = StudyClass.class,
                parentColumns = "id",
                childColumns = "classId",
                onDelete = ForeignKey.SET_NULL
        )
)
public class StudySession {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @Nullable
    public Long classId;

    public String name;
    public String startTime;
    public String endTime;
    public String weekdays;
    public boolean recurring;
    public String code;

    public int focusScore;
    public int phonePickups;
    public int seshStreakDays;
    public int concentrationMinutes;
}
