package com.example.he.material.Controler;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.he.material.DataBase;
import com.example.he.material.MODLE.Music;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DataBaseControler {
    private  String TAG="database";
    private String  name;
    private String imagepath;
    private  String path;
    private  String singer;
    private  int id;
    private  int size;
    private  DataBase mDataBase;
    private SQLiteDatabase mdb;
    private Cursor mCursor;
    private  Music music;
    private List<Music> query_list =new ArrayList<>();
    private int imageId;

    //数据库插入
    public void insert(DataBase mdatabase,int id,String name,String singer,int imageId,String path){

         mdb=mdatabase.getWritableDatabase();
        if(mdb.isOpen()){
            ContentValues values=new ContentValues();
            values.put("id",id);
            values.put("name",name);
            values.put("singer",singer);
            values.put("imageid",imageId);
            values.put("path",path);
            mdb.insert("music",null,values);
            //mdb.execSQL("insert into music(id,name,imageId,path) values(id,name,imagepath,path)");
            Log.i(TAG, "id:"+id+"name"+name+"singer"+singer+"imageid"+imageId+"path"+path);
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

    public Music query(DataBase mdatabase,String mname){

        mdb=mdatabase.getWritableDatabase();
        if(mdb.isOpen()){
            mCursor=mdb.rawQuery("select * from music where name= ?;",new String[]{mname+""});
            if(mCursor!=null&&mCursor.moveToFirst())
            {
                id=mCursor.getInt(0);
                name=mCursor.getString(1);
                singer=mCursor.getString(2);
                imageId=mCursor.getInt(3);
                path=mCursor.getString(4);

                mdb.close();
                Log.i(TAG, "id:"+id+"name"+name+"singer"+singer+"path"+path);
                return new Music(id,name,singer,path,imageId);
            }
            mdb.close();
        }
        return null;
    }
    public List<Music> query(DataBase mdatabase){

        mdb=mdatabase.getWritableDatabase();
        if(mdb.isOpen()){
            mCursor=mdb.rawQuery("select * from music",null);
            while(mCursor.moveToNext()){
                id=mCursor.getInt(mCursor.getColumnIndex("id"));
                name=mCursor.getString(mCursor.getColumnIndex("name"));
                singer=mCursor.getString(mCursor.getColumnIndex("singer"));
                imageId=mCursor.getInt(mCursor.getColumnIndex("imageid"));
                path=mCursor.getString(mCursor.getColumnIndex("path"));
                Log.i(TAG,id+"  id here " +name+"  "+singer+"   "+path+"   ");
                mdb.close();

                music=new Music(id,name,singer,path,imageId);
                query_list.add(music);

            }
            mdb.close();
        }
        return query_list;
    }

}
