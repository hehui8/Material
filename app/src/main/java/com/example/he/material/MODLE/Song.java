package com.example.he.material.MODLE;

import java.io.Serializable;

public class Song implements Serializable {
	private int id;
    private String artist;
    private String songName;
    private String album;
    private String path;
	private String lrc;
	private String picpath;
	public Song(){
	    super();
    }
    public  Song(int id, String artist,String songName,String album,String path){
	    this.id=id;
	    this.artist=artist;
	    this.songName=songName;
	    this.album=album;
	    this.path=path;
    }
    public  Song(int id, String artist,String songName,String path){
        this.id=id;
        this.artist=artist;
        this.songName=songName;
        this.path=path;
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLrc() {
		return lrc;
	}

	public void setLrc(String lrc) {
		this.lrc = lrc;
	}


    
    
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}


    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }
}
