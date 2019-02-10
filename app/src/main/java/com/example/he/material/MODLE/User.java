package com.example.he.material.MODLE;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private int loginState;
    private int type;
    private String username;
    private String password;
    private int state;
    private String name;

    public int getLoginState() {
        return loginState;
    }

    public void setLoginState(int loginState) {
        this.loginState = loginState;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    //构造
    public User() {
        super();
    }

    public User(String username, String password) {
        this.password = password;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSonglist() {
        return songlist;
    }

    public void setSonglist(long songlist) {
        this.songlist = songlist;
    }

    private long songlist;

}
