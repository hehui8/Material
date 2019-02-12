package com.example.he.material.Fragment_List;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.he.material.Adapter.MainAdapter;
import com.example.he.material.MODLE.Song;
import com.example.he.material.R;
import com.example.he.material.Utils.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

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

    private Banner banner;
    private RecyclerView mList;
    private MainAdapter mainAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Drawable> images =new ArrayList<>();
        images.add(getResources().getDrawable(R.drawable.caomei));
        images.add(getResources().getDrawable(R.drawable.chengzi));
        images.add(getResources().getDrawable(R.drawable.xiangjiao));
        mList=view.findViewById(R.id.main_recycler);
        banner = (Banner) view.findViewById(R.id.banner);
        List<Song> songs=new ArrayList<>();
        for(int i=0;i<10;i++){
            Song song =new Song();
            song.setSongName("zs1111");
        }
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        mainAdapter=new MainAdapter(songs,getContext());
        mList.setLayoutManager(layoutManager);
        mList.setAdapter(mainAdapter);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        banner.setDelayTime(4500);
        banner.setBannerAnimation(Transformer.ScaleInOut);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Toast.makeText(getContext(),"第"+position+"个",Toast.LENGTH_SHORT).show();
            }
        });
        //banner设置方法全部调用完毕时最后调用
        banner.start();





    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }
}
