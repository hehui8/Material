package com.example.he.material.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

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

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launch);

        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
        }
        SPUtils spUtils = new SPUtils(this);
        String getFromSP = spUtils.getSP("lastUser");
        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1500);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
        if (getFromSP != null && !getFromSP.isEmpty()) {
            Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
            Gson gson = new Gson();
            User user = new User();
            user = gson.fromJson(getFromSP, User.class);
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(LaunchActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        Preferences preferences=new Preferences(this);
        User user = preferences.read("LASTUSER");
        //上次登录的用户的用户名
        lastName=user.getUsername();

    }

}
