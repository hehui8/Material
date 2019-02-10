package com.example.he.material.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.he.material.MODLE.User;
import com.example.he.material.OKHTTP;
import com.example.he.material.R;
import com.example.he.material.Utils.SPUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends Activity {

    private EditText mUserEd;
    private EditText mNameEd;
    private EditText mPassEd;
    private Button mRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mPassEd = findViewById(R.id.ed_password);
        mUserEd = findViewById(R.id.ed_username);
        mNameEd = findViewById(R.id.ed_name);
        mRegister = findViewById(R.id.register_button);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPassEd != null && mUserEd != null) {
                    String username = mUserEd.getText().toString();
                    String password = mPassEd.getText().toString();
                    String name = mNameEd.getText().toString();
                    if (!username.isEmpty() && !password.isEmpty() && !name.isEmpty()) {
                        registerRequest(username, password, name);
                    }
                }
            }
        });

    }

    public void registerRequest(String username, String password, String name) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
        //构造表单数据（通过json）
        final User user = new User();
        if (name != null && password != null) {
            user.setPassword(password);
            user.setUsername(username);
            user.setName(name);
        }
        Gson gson = new Gson();
        String json = gson.toJson(user);
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; " +
                "charset=utf-8"), json);
        final Request request = new Request.Builder()
                //.url("http://118.25.27.150:8080/Music/Servlet_1")
                .url("http://10.1.14.15:8080/TestMusic/RegisterServlet")
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterActivity.this, "请稍后再试", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String type = response.body().string();
                Log.d("register", type);
                if (type.equals("3"))
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SPUtils spUtils = new SPUtils(RegisterActivity.this);
                            spUtils.addSP(user.getName(), user.getPassword());
                            spUtils.addSP("lastUser", user);
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            intent.putExtra("user", user);
                            startActivity(intent);
                            finish();
                        }
                    });
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "账户或者用户名已存在", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
