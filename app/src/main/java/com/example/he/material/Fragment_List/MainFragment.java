package com.example.he.material.Fragment_List;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.he.material.Adapter.MainAdapter;
import com.example.he.material.MODLE.JsonRootBean;
import com.example.he.material.MODLE.Song;
import com.example.he.material.MODLE.SongSheetList;
import com.example.he.material.R;
import com.example.he.material.Utils.GlideImageLoader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * project: Material
 * author : Android研发部_姓名
 * date : 2019/1/15
 * time : 14:09
 * email : 企业邮箱
 * note : 说明
 */
public class MainFragment extends Fragment {

    public static MainFragment newInstance(List<SongSheetList> list) {
        MainFragment newFragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", (Serializable) list);
        newFragment.setArguments(bundle);
        return newFragment;

    }

    private Banner banner;
    private RecyclerView mList;
    private MainAdapter mainAdapter;
    private SongSheetList mSheetList;
    private List<JsonRootBean> mSongSheetList;
    private OkHttpClient client;
    private JsonRootBean mJsonData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        client=new OkHttpClient.Builder().build();
        List<Drawable> images =new ArrayList<>();
        images.add(getResources().getDrawable(R.drawable.caomei));
        images.add(getResources().getDrawable(R.drawable.chengzi));
        images.add(getResources().getDrawable(R.drawable.xiangjiao));
        mList=view.findViewById(R.id.main_recycler);
        banner = (Banner) view.findViewById(R.id.banner);
        if(getArguments()!=null){
            mSongSheetList= (List<JsonRootBean>) getArguments().getSerializable("list");
        }
        List<Song> songs=new ArrayList<>();
        for(int i=0;i<30;i++){
            Song song =new Song();
            song.setSongName("zs1111");
            songs.add(song);
        }
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        if(mSongSheetList!=null && mSongSheetList.size()>0){
            mainAdapter=new MainAdapter(mSongSheetList, getContext(), new MainAdapter.OnItemClickListener() {
                @Override
                public void onClick(int position) {
                    Intent intent =new Intent();
                    Bundle bundle =new Bundle();
                    bundle.putSerializable("ClickForSheet",(Serializable) mSongSheetList.get(position));
                }

                @Override
                public void onLongClick(int position) {

                }
            });
        }
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

    public void requestSongList(){
        Request request =new Request.Builder()
                .url("https://api.bzqll.com/music/netease/hotSongList?key=579621905&cat=全部&limit=100&offset=0")
                .get()
                .build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() != null) {
                    String str =response.body().string();
                    Gson gson =new Gson();
                    mJsonData=gson.toJson(str,new TypeToken<JsonRootBean>(){}.getT);
                }

            }
        });

    }
}
