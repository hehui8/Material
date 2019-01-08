package com.example.he.material;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.he.material.Activity.LoginActivity;
import com.example.he.material.Activity.MainActivity;
import com.example.he.material.MODLE.GEDAN.Root;
import com.example.he.material.MODLE.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OKHTTP {
    public static  OkHttpClient client ;

    public void  sendRequest(final Handler handler){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(client ==null){
                    client =new OkHttpClient.Builder().build();
                }
                try{

                    client =new OkHttpClient.Builder().build();
                    String json="{\"TransCode\":\"020111\",\"OpenId\":\"Test\",\"Body\":{\"SongListId\":\"2429050789\"}}";
                    RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"),json);
                    Request request = new Request.Builder()
                            //.url("http://118.25.27.150:8080/Music/Servlet_1")
                            //.url("http://192.168.49.2:8080/Music/Servlet_1")
                            .url("https://api.hibai.cn/api/index/index")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String str = response.body().string();
                    Log.i("ResponseToString",str);

                }catch (Exception e){
                    e.printStackTrace();
                }

                Message message =new Message();
                message.what =1;
                handler.sendMessage(message);
            }
        }).start();
    }

    }


