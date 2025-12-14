package com.example.project3;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StudyDao {

    @Insert
    long insertClass(StudyClass studyClass);

    @Update
    void updateClass(StudyClass studyClass);

    @Delete
    void deleteClass(StudyClass studyClass);

    @Query("SELECT * FROM classes ORDER BY name")
    List<StudyClass> getAllClasses();

    @Query("SELECT * FROM classes WHERE name = :name LIMIT 1")
    StudyClass findClassByName(String name);

    @Query("SELECT * FROM classes WHERE code = :code LIMIT 1")
    StudyClass findClassByCode(String code);


    @Insert
    long insertSession(StudySession session);

    @Update
    void updateSession(StudySession session);

    @Delete
    void deleteSession(StudySession session);

    @Query("SELECT * FROM sessions ORDER BY name")
    List<StudySession> getAllSessions();

    @Query("SELECT * FROM sessions WHERE name = :name LIMIT 1")
    StudySession findSessionByName(String name);

    @Query("SELECT * FROM sessions WHERE code = :code LIMIT 1")
    StudySession findSessionByCode(String code);
}