package com.example.he.material.Utils;

import android.app.Activity;
import android.content.Context;
import android.text.StaticLayout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * project: Material
 * author : Android研发部_姓名
 * date : 2019/1/10
 * time : 13:15
 * email : 企业邮箱
 * note : 说明
 */
public class DataUtils {
     static List<String> recentDataList = new ArrayList<>();

    /**
     * 将搜索框的文本保存到searchRecent数组中
     */
    public static void putData(String searchkey) {
        String mMatchString;
        Collections.reverse(recentDataList);
        if (recentDataList != null && searchkey != null) {
            for (int i = 0; i < recentDataList.size(); i++) {
                mMatchString = recentDataList.get(i);
                if (mMatchString.equals(searchkey)) {
                    recentDataList.remove(i);
                }
            }
            if (recentDataList.size() < 10 && recentDataList.size() >= 0) {
                recentDataList.add(searchkey);
            } else if (recentDataList.size() == 10) {
                recentDataList.remove(0);
                recentDataList.add(searchkey);
            }
        }
        Collections.reverse(recentDataList);
    }

    public static List<String> getData() {

        if (recentDataList != null) {
            Collections.reverse(recentDataList);
            return recentDataList;
        }
        return null;
    }

    public static void clearDataList() {
        if (recentDataList != null) {
            recentDataList.clear();
        }

    }

    public static void hideSoftKeyboard(Context context, List<View> viewList) {
        if (viewList == null) return;

        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);

        for (View v : viewList) {
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    public static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            view.requestFocus();
            imm.showSoftInput(view, 0);
        }
    }

    public static void hideKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
    public static void  toggleSoftInput(View view){
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0,0);
        }
    }

}
