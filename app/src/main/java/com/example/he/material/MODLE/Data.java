package com.example.he.material.MODLE;

import java.io.Serializable;

/**
 * project: Material
 * author : Android研发部_姓名
 * date : 2019/2/19
 * time : 9:46
 * email : 企业邮箱
 * note : 说明
 */
public class Data  implements Serializable {

    private long id;
    private String title;
    private String creator;
    private String description;
    private String coverImgUrl;
    private int songNum;
    private int playCount;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreator() {
        return creator;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setSongNum(int songNum) {
        this.songNum = songNum;
    }

    public int getSongNum() {
        return songNum;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public int getPlayCount() {
        return playCount;
    }
}
