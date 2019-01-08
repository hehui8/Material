package com.example.he.material;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBase  extends SQLiteOpenHelper{
    private  String TAG="database";
    private  int version=1;


    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql ="create table music (id INTEGER PRIMARY KEY ,name varchar(20),singer varchar(40),imageid INTEGER,path varchar(400))";
        Log.i(TAG,"create database");
        db.execSQL("DROP TABLE IF EXISTS music");
        db.execSQL(sql);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG,"Update database");
    }
}
