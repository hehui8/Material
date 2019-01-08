package com.example.he.material.MODLE.GEDAN;

public class Creator {
    private int userId;

    private String nickname;

    private String signature;

    private String avatarUrl;

    private String backgroundUrl;

    public void setUserId(int userId){
        this.userId = userId;
    }
    public int getUserId(){
        return this.userId;
    }
    public void setNickname(String nickname){
        this.nickname = nickname;
    }
    public String getNickname(){
        return this.nickname;
    }
    public void setSignature(String signature){
        this.signature = signature;
    }
    public String getSignature(){
        return this.signature;
    }
    public void setAvatarUrl(String avatarUrl){
        this.avatarUrl = avatarUrl;
    }
    public String getAvatarUrl(){
        return this.avatarUrl;
    }
    public void setBackgroundUrl(String backgroundUrl){
        this.backgroundUrl = backgroundUrl;
    }
    public String getBackgroundUrl(){
        return this.backgroundUrl;
    }

}
