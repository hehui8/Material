package com.example.he.material.MODLE;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private int state;
    private String name;
    //构造
    public User(){
        super();
    }
    public User(String username,String password){
        this.password=password;
        this.username =username;
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


}
