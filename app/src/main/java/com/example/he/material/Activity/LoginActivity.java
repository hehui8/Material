package com.example.he.material.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.he.material.MODLE.User;
import com.example.he.material.R;
import com.example.he.material.Utils.AndroidWorkaround;
import com.example.he.material.Utils.SPUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    String username;
    String password;
    private User user = null;

    private EditText mLogin_password;
    private EditText mLogin_username;
    private CircleImageView mLogin_view;
    private Button mLogin_register;
    private Button mLogin_login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
        }


        /*
         * 初始化控件
         * */
        final EditText mLogin_password = findViewById(R.id.password_edit);
        final EditText mLogin_username = findViewById(R.id.username_edit);
        Button mLogin_login = findViewById(R.id.login_button);
        Button mLogin_register = findViewById(R.id.register_button);
        CircleImageView mLogin_view = findViewById(R.id.login_view);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

//        获取文本框入的账号信息

        mLogin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = mLogin_username.getText().toString();
                password = mLogin_password.getText().toString();

                if (!username.isEmpty() && !password.isEmpty()) {

                    if (username == null || password == null) {

                        Toast.makeText(LoginActivity.this, "请输入账号或密码", Toast.LENGTH_LONG).show();
                    } else {

                        User user = new User(username, password);
                        //声明一个asynctask对象并实例化
                        OkHttpRequestAsynctask1 okHttpRequest = new OkHttpRequestAsynctask1();
                        //调用execute方法
                        okHttpRequest.execute(user);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "请输入账号或密码", Toast.LENGTH_LONG).show();
                }

            }
        });

        mLogin_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);


                Log.d("Test1", "here");

            }
        });
    }

    class OkHttpRequestAsynctask1 extends AsyncTask<User, Integer, User> {
        @Override
        protected User doInBackground(User... users) {
            User user = new User();
            user.setUsername(users[0].getUsername());
            user.setPassword(users[0].getPassword());
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
                        .url("http://10.1.14.15:8080/TestMusic/LoginServlet")
                        /*  .url("http://192.168.55.15:8080/TestMusic/LoginServlet")*/
                        .post(requestBody)
                        .build();
                Response response = mclient.newCall(request).execute();
                if (response != null) {
                    String str = response.body().string();
                    Log.d("login_success", "登陆成功 " + str);
                    if (str.equals("false")) {
                        return null;
                    }
                    if (!str.isEmpty()) {
                        user = gson.fromJson(str, User.class);
                        user.setUsername(users[0].getUsername());
                        user.setPassword(users[0].getPassword());
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(LoginActivity.this, "网络异常，请检查网络", Toast.LENGTH_SHORT).show();
            }
            return user;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(User user) {
            if (user != null) {
                Toast.makeText(LoginActivity.this, "欢迎登陆  " + user.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("user", user);
                SPUtils spUtils = new SPUtils(LoginActivity.this);
                Log.d("hess", "onCreate: " + user.getUsername());
                spUtils.addSP(user.getName(), user.getPassword());
                spUtils.addSP("lastUser", user);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "账号或密码错误  ", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }


    }


}


