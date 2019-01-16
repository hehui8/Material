package com.example.he.material.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.he.material.R;

/**
 * project: Material
 * author : Android研发部_姓名
 * date : 2019/1/15
 * time : 11:40
 * email : 企业邮箱
 * note : 说明
 */
public class LaunchActivity  extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_launch);
        Thread myThread =new Thread(){
            @Override
            public void run() {
               try{
                   sleep(2000);
                   Intent itent=new Intent(getApplicationContext(),MainActivity.class);
                   startActivity(itent);
                   finish();
               }catch (Exception e){
                   e.printStackTrace();
               }
            }
        };
        myThread.start();
    }
}
