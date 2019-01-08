package com.example.he.material.MODLE.GEDAN;

import java.io.Serializable;

public class Songs implements Serializable {
    private int id;

    private String title;

    private String author;

    private int author_id;

    private String author_pic;

    private String pic;

    private String url;

    private String lrc;

    private int time;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setAuthor(String author){
        this.author = author;
    }
    public String getAuthor(){
        return this.author;
    }
    public void setAuthor_id(int author_id){
        this.author_id = author_id;
    }
    public int getAuthor_id(){
        return this.author_id;
    }
    public void setAuthor_pic(String author_pic){
        this.author_pic = author_pic;
    }
    public String getAuthor_pic(){
        return this.author_pic;
    }
    public void setPic(String pic){
        this.pic = pic;
    }
    public String getPic(){
        return this.pic;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(){
        return this.url;
    }
    public void setLrc(String lrc){
        this.lrc = lrc;
    }
    public String getLrc(){
        return this.lrc;
    }
    public void setTime(int time){
        this.time = time;
    }
    public int getTime(){
        return this.time;
    }

}
