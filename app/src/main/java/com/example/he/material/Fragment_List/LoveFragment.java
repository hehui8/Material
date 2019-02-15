package com.example.he.material.Fragment_List;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * project: Material
 * author : Android研发部_姓名
 * date : 2019/2/15
 * time : 19:02
 * email : 企业邮箱
 * note : 说明
 */
public class LoveFragment extends Fragment {
    public static LoveFragment newInstance() {
        Bundle args = new Bundle();
        LoveFragment fragment = new LoveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
