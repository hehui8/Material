package com.example.he.material.MODLE;

import java.util.List;

public class JsonRootBean {

    private String result;
    private int code;
    private List<Data> data;

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public List<Data> getData() {
        return data;
    }

}