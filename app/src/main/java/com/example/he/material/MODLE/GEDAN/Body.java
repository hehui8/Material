package com.example.he.material.MODLE.GEDAN;

import java.util.List;

public class Body {
    private int id;

    private String name;

    private String coverImgUrl;

    private Creator creator;

    private List<Songs> songs ;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setCoverImgUrl(String coverImgUrl){
        this.coverImgUrl = coverImgUrl;
    }
    public String getCoverImgUrl(){
        return this.coverImgUrl;
    }
    public void setCreator(Creator creator){
        this.creator = creator;
    }
    public Creator getCreator(){
        return this.creator;
    }
    public void setSongs(List<Songs> songs){
        this.songs = songs;
    }
    public List<Songs> getSongs(){
        return this.songs;
    }

}
