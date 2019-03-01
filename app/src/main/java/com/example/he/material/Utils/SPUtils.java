package com.example.he.material.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.he.material.MODLE.User;
import com.google.gson.Gson;

import java.util.Map;

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

    public void addLoveSongSP(String key, String values) {
        setSP("LOVE");
        if (sp != null) {
            editor = sp.edit();
            if (editor != null) {
                editor.putString(key, values);
                editor.apply();
            }
        }
    }

    public boolean getSingleLoveSong(String key) {
        setSP("LOVE");
        if (sp != null) {
           String str= sp.getString(key,null);
            if(!TextUtils.isEmpty(str)){
                return  true;
            }else{
                return false;
            }
        }
        return false;
    }

    public Map<String, ?> getLoveListSP(String key) {
        setSP("LOVE");
        Map<String, ?> map;
        if (sp != null) {
            map = sp.getAll();
            if (map != null) {
                return map;
            }
        }
        return null;
    }

    public void removeLoveListSP(String key) {
        setSP("LOVE");
        if (sp != null) {
            editor = sp.edit();
            if (editor != null) {
                editor.remove(key);
                editor.apply();
            }
        }
    }
}
