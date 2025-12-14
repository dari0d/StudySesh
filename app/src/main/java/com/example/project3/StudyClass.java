package com.example.project3;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "classes",
        indices = {
                @Index(value = {"name"}, unique = true),
                @Index(value = {"code"}, unique = true)
        }
)
public class StudyClass {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String name;

    @NonNull
    public String code;

    public String startTime;
    public String endTime;

    public String weekdays;
}