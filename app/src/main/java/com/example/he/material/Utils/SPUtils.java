package com.example.he.material.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.he.material.MODLE.User;
import com.google.gson.Gson;

public class SPUtils {
    private Context mContext;
    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    public void setSP(String key) {
        if (mContext != null) {
            sp = mContext.getSharedPreferences(key, Context.MODE_PRIVATE);
        }
    }

    public SPUtils(Context context) {
        this.mContext = context;
    }

    public void addSP(String key, String values) {
        setSP("user");
        editor = sp.edit();
        if (editor != null) {
            editor.putString(key, values);
            editor.apply();
        }
    }

    public void addSP(String key, User user) {
        setSP("user");
        editor = sp.edit();
        if (editor != null) {
            Gson gson = new Gson();
            if (user != null) {
                String values = gson.toJson(user);
                editor.putString(key, values);
                editor.apply();
            }
        }
    }

    public String getSP(String key) {
        setSP("user");
        if (sp != null) {
            if (sp.getString(key, null) != null) {
                return sp.getString(key, null);
            }
        }
        return null;
    }

}
