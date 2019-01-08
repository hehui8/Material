package com.example.he.material.MODLE.GEDAN;

public class Root {
    private int ResultCode;

    private String ErrCode;

    private Body Body;

    public void setResultCode(int ResultCode){
        this.ResultCode = ResultCode;
    }
    public int getResultCode(){
        return this.ResultCode;
    }
    public void setErrCode(String ErrCode){
        this.ErrCode = ErrCode;
    }
    public String getErrCode(){
        return this.ErrCode;
    }
    public void setBody(Body Body){
        this.Body = Body;
    }
    public Body getBody(){
        return this.Body;
    }
}
