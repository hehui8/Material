package com.example.he.material.MODLE;

public class Song {
	private int id;
    private String artist;
    private String songName;
    private String album;
    private String path;
	private String lrc;
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



}
