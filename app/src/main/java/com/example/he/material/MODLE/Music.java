package com.example.he.material.MODLE;

import java.io.Serializable;

public class Music implements Serializable {
    private int id;
    private String  name;
    private String imagepath;
    private int imageId;
    private  int AlbumId;
    private  String path;
    private String  singer;
     private int size;

    public Music() {
        super();
    }

    public Music(int id, String name, String singer, String path, int imageId ) {
        this.id=id;
        this.name=name;
        this.singer=singer;
        this.imageId=imageId;
        this.path=path;
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imageId) {
        this.imagepath = imageId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getAlbumId() {
        return AlbumId;
    }

    public void setAlbumId(int albumId) {
        AlbumId = albumId;
    }
}
