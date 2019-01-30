package com.example.he.material.Controler;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.he.material.DataBase;
import com.example.he.material.MODLE.Song;

import java.util.ArrayList;
import java.util.List;

public class DataBaseControler {
    private  String TAG="database";
    private String  name;
    private String imagepath;
    private  String path;
    private  String singer;
    private  int id;
    private  DataBase mDataBase;
    private SQLiteDatabase mdb;
    private Cursor mCursor;
    private  Song music;
    private List<Song> query_list =new ArrayList<>();

    //数据库插入
    public void insert(DataBase mdatabase,int id,String name,String singer,String path){

         mdb=mdatabase.getWritableDatabase();
        if(mdb.isOpen()){
            ContentValues values=new ContentValues();
            values.put("id",id);
            values.put("name",name);
            values.put("singer",singer);
            values.put("path",path);
            mdb.insert("music",null,values);
            //mdb.execSQL("insert into music(id,name,imageId,path) values(id,name,imagepath,path)");
             mdb.close();
        }
    }
//数据库删除
    public void delete(DataBase mdatabase,int id){
        mdb=mdatabase.getWritableDatabase();
        if(mdb.isOpen()) {
            mdb.execSQL("delete from music where id = ?;", new Integer[]{id});
            mdb.close();
        }
    }

    //数据库查询(根据歌曲名)

    public Song query(DataBase mdatabase, String mname){

        mdb=mdatabase.getWritableDatabase();
        if(mdb.isOpen()){
            mCursor=mdb.rawQuery("select * from music where name= ?;",new String[]{mname+""});
            if(mCursor!=null&&mCursor.moveToFirst())
            {
                Song song =new Song();
                song.setId(mCursor.getInt(0));
                song.setSongName(mCursor.getString(1));
                song.setArtist(mCursor.getString(2));
                song.setPath(mCursor.getString(3));
                mdb.close();
                return song;
            }
            mdb.close();
        }
        return null;
    }
    public List<Song> query(DataBase mdatabase){

        mdb=mdatabase.getWritableDatabase();
        if(mdb.isOpen()){
            mCursor=mdb.rawQuery("select * from music",null);
            while(mCursor.moveToNext()){
                id=mCursor.getInt(mCursor.getColumnIndex("id"));
                name=mCursor.getString(mCursor.getColumnIndex("name"));
                singer=mCursor.getString(mCursor.getColumnIndex("singer"));
                path=mCursor.getString(mCursor.getColumnIndex("path"));
                Log.i(TAG,id+"  id here " +name+"  "+singer+"   "+path+"   ");
                mdb.close();

                music=new Song(id,name,singer,path);
                query_list.add(music);

            }
            mdb.close();
        }
        return query_list;
    }

}
