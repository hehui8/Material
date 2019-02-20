package com.example.he.material.MODLE.GEDAN;

import java.util.List;

public class Data {

    private long songListId;
    private String songListName;
    private String songListPic;
    private int songListCount;
    private int songListPlayCount;
    private String songListDescription;
    private String coverImgUrl;
    private Creator creator;
    private List<songs> songs;


    public long getSongListId() {
        return songListId;
    }

    public void setSongListId(long songListId) {
        this.songListId = songListId;
    }

    public String getSongListName() {
        return songListName;
    }

    public void setSongListName(String songListName) {
        this.songListName = songListName;
    }

    public String getSongListPic() {
        return songListPic;
    }

    public void setSongListPic(String songListPic) {
        this.songListPic = songListPic;
    }

    public int getSongListCount() {
        return songListCount;
    }

    public void setSongListCount(int songListCount) {
        this.songListCount = songListCount;
    }

    public int getSongListPlayCount() {
        return songListPlayCount;
    }

    public void setSongListPlayCount(int songListPlayCount) {
        this.songListPlayCount = songListPlayCount;
    }

    public String getSongListDescription() {
        return songListDescription;
    }

    public void setSongListDescription(String songListDescription) {
        this.songListDescription = songListDescription;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public List<songs> getSongs() {
        return songs;
    }

    public void setSongs(List<com.example.he.material.MODLE.GEDAN.songs> songs) {
        this.songs = songs;
    }




}
