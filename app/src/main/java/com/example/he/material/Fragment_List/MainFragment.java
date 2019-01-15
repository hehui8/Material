package com.example.he.material.Fragment_List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.he.material.R;
import com.tmall.ultraviewpager.UltraViewPager;

/**
 * project: Material
 * author : Android研发部_姓名
 * date : 2019/1/15
 * time : 14:09
 * email : 企业邮箱
 * note : 说明
 */
public class MainFragment extends Fragment {

    public static SearchRecentFragment newInstance() {
        SearchRecentFragment newFragment = new SearchRecentFragment();
        Bundle bundle = new Bundle();
        newFragment.setArguments(bundle);
        return newFragment;

    }

    private UltraViewPager mUltraViewPager;
    private TextView mPersonTv;
    private TextView mDailyTv;
    private TextView mPaiHangTv;
    private ImageView mPersonImg;
    private ImageView mDailyImg;
    private ImageView PaiHangImg;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mUltraViewPager = view.findViewById(R.id.ultra_viewpager);
        mUltraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        PagerAdapter adapter = new UltraPagerAdapter(false);
        mUltraViewPager.setAdapter(adapter);


        mUltraViewPager.initIndicator();
        //设置indicator样式
        mUltraViewPager.getIndicator()
                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                .setFocusColor(Color.GREEN)
                .setNormalColor(Color.WHITE)
                .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources()
                        .getDisplayMetrics()));
        //设置indicator对齐方式
        mUltraViewPager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        //构造indicator,绑定到UltraViewPager
        mUltraViewPager.getIndicator().build();

        //设定页面循环播放
        mUltraViewPager.setInfiniteLoop(true);
        //设定页面自动切换  间隔2秒
        mUltraViewPager.setAutoScroll(2000);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
