package com.example.he.material.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.he.material.MODLE.User;

/**
 * project: Material
 * author : Android研发部_姓名
 * date : 2019/2/1
 * time : 14:17
 * email : 企业邮箱
 * note : 说明
 */
public class Preferences {
    private Context context;
    private  SharedPreferences preferences;
    private SharedPreferences.Editor editor;



    public Preferences(Context context) {
        this.context = context;
        preferences =  context.getSharedPreferences("login", Context.MODE_PRIVATE);
    }


    public void save(String name, String pass) {
        //保存文件名字为"shared",保存形式为Context.MODE_PRIVATE即该数据只能被本应用读取
        editor = preferences.edit();
        editor.putString("name", pass);
        editor.commit();//提交数据
    }

    public void save(String name) {
        //保存文件名字为"shared",保存形式为Context.MODE_PRIVATE即该数据只能被本应用读取
        editor = preferences.edit();
        editor.putString("LASTUSER", name);
        editor.commit();//提交数据
    }


    public User read(String key, String values) {
        String pass;
        User user = new User();
        pass = preferences.getString(key, "");
        user.setName(key);
        if (!pass.isEmpty()) {
            user.setPassword(pass);
            user.setUsername(key);
        }
        return user;
    }
    public User read(String key) {
        String lastname;
        String pass;
        User user = new User();
        lastname = preferences.getString(key, "");
        pass=preferences.getString(lastname,null);
        if (!lastname.isEmpty()) {
            user.setUsername(lastname);
            user.setPassword(pass);
        }
        return user;
    }

}
