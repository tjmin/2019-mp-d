//작성자: 이원구
// for database (2 of 3)
// DataBase Class, an abstract class to create database

package com.example.myapplication.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.Script;


@Database(entities = Script.class, version = 1,exportSchema = false )
public abstract class ScriptsDB extends RoomDatabase {

    public abstract ScriptsDao scriptsDao();

    public static final String DATABASE_NAME = "scriptsDb";
    private static ScriptsDB instance;

    public static ScriptsDB getInstance(Context context){
        if (instance == null)
            instance = Room.databaseBuilder(context, ScriptsDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        return instance;
    }
}