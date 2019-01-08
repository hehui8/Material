package com.example.he.material.Fragment_List;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.he.material.Activity.MainActivity;
import com.example.he.material.Activity.MusicActivity;
import com.example.he.material.Adapter.Music_Internet_Adapter;
import com.example.he.material.MODLE.GEDAN.Root;
import com.example.he.material.MODLE.GEDAN.Songs;
import com.example.he.material.R;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@SuppressLint("ValidFragment")
public class InternetFragment extends Fragment {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Context context;
    public static Intent intent;
    private Music_Internet_Adapter adapter;
    private static List<Songs> InternetList;


    public InternetFragment(List<Songs> addressList, Context context) {
        this.InternetList = addressList;
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = LayoutInflater.from(getContext()).inflate(R.layout.internet_fragment, container, false);
        swipeRefreshLayout = view.findViewById(R.id.internet_layout);

        recyclerView = view.findViewById(R.id.recycler_view_address);

        //配置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));

        /**
         *    构造适配器时传入一个数据数组和一个监听接口并重写其点击事件方法
         */
        adapter = new Music_Internet_Adapter(InternetList, new Music_Internet_Adapter.OnItemClickListener() {

            @Override
            public void onClick(int position) {
                Intent intent1 = new Intent(context, MusicActivity.class);
                Log.i("itemtouch", position + "移除后的点击位置");
                Songs music = InternetList.get(position);
                Bundle data = new Bundle();
                data.putInt("itemId", position);
                intent1.putExtra("from", "Internet");
                intent1.putExtra("data", data);
                intent1.putExtra("song", (Serializable) InternetList);
                startActivity(intent1);
            }

            @Override
            public void onLongClick(int position) {

            }
        });
        //设置适配器
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refresh();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void refresh(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient.Builder().build();
                    String json = "{\"TransCode\":\"020111\",\"OpenId\":\"TEST\",\"Body\":{\"SongListId\":" + "2429050789" + "}}";
                    RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);
                    Request request = new Request.Builder()
                            .url("https://api.hibai.cn/api/index/index")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    String reuqest = response.body().string();
                    Gson gson = new Gson();
                    Root root = gson.fromJson(reuqest, Root.class);
                    InternetList.clear();
                    InternetList.addAll( root.getBody().getSongs());
                    Log.d("number","number is "+InternetList.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
