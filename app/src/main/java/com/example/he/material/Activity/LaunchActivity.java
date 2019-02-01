package com.example.he.material.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.he.material.MODLE.User;
import com.example.he.material.R;
import com.example.he.material.Utils.AndroidWorkaround;
import com.example.he.material.Utils.SPUtils;
import com.google.gson.Gson;
import com.example.he.material.Utils.Preferences;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
/**
 * project: Material
 * author : Android研发部_姓名
 * date : 2019/1/15
 * time : 11:40
 * email : 企业邮箱
 * note : 说明
 */
public class LaunchActivity extends Activity {

    private String lastName=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_launch);

        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
        }
        SPUtils spUtils = new SPUtils(this);
        String getFromSP = spUtils.getSP("lastUser");
        if (getFromSP != null && !getFromSP.isEmpty()) {
            Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
            Gson gson = new Gson();
            User user = new User();
            user = gson.fromJson(getFromSP, User.class);
            intent.putExtra("user", user);
            startActivity(intent);
        } else {
            Intent intent = new Intent(LaunchActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        Preferences preferences=new Preferences(this);
        User user = preferences.read("LASTUSER");
        //上次登录的用户的用户名
        lastName=user.getUsername();

        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);

                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();

        //启动对用户之前是否登陆做检查

        OkHttpRequestAsynctask mOkAsynctask=new OkHttpRequestAsynctask();
        mOkAsynctask.execute(user);


    }

    public class OkHttpRequestAsynctask extends AsyncTask<User, Integer, User> {
        @Override
        protected User doInBackground(User... users) {
            User user = new User();
            Preferences preferences=new Preferences(getBaseContext());
            user= preferences.read(lastName,null);
            Log.d("lautologin","s"+lastName+"   "+user.getUsername()+"  "+user.getPassword());

            Gson gson = new Gson();
            // System.out.print(users.getClass());
            String json = gson.toJson(user);
            try {
                OkHttpClient mclient = new OkHttpClient.Builder()
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .writeTimeout(5, TimeUnit.SECONDS)
                        .readTimeout(5, TimeUnit.SECONDS)
                        .build();

                RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);
                Request request = new Request.Builder()
                        //.url("http://172.21.187.216:8080/Music/Servlet_1")
                        //服务器url
                        .url("http://106.15.89.25:8080/TestMusic/LoginServlet")
                        .post(requestBody)
                        .build();
                Response response = mclient.newCall(request).execute();
                if (response != null) {
                    String str = response.body().string();
                    Log.d("login_success", "登陆成功 " + str);
                    if (str.equals("false")) {
                        user = null;
                        return user;
                    }
                    user = gson.fromJson(str, User.class);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return user;
        }

        /*
         *
         *
         **/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(User user) {
            if(user==null){
                Intent intent =new Intent(LaunchActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                Intent intent =new Intent(LaunchActivity.this,MainActivity.class);
                intent.putExtra("from","launchActivity");
                startActivity(intent);
                finish();
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

    }
}
