package com.example.he.material.MODLE.GEDAN;

public class Root {
    private String result;

    private String code;

    private Data data;

    public void setResult(String result){
        this.result = result;
    }
    public String getResult(){
        return this.result;
    }
    public void setCode(String ErrCode){
        this.code = ErrCode;
    }
    public String getCode(){
        return this.code;
    }
    public void setData(Data Data){
        this.data = Data;
    }
    public Data getData(){
        return this.data;
    }
}
