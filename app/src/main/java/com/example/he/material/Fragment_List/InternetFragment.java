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

import com.example.he.material.Activity.MusicActivity;
import com.example.he.material.Adapter.musicInternetAdapter;
import com.example.he.material.MODLE.GEDAN.Root;
import com.example.he.material.MODLE.Song;
import com.example.he.material.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
    private musicInternetAdapter adapter;
    private  List<Song> InternetList;


    public InternetFragment(List<Song> addressList, Context context) {
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
        adapter = new musicInternetAdapter(InternetList, new musicInternetAdapter.OnItemClickListener() {

            @Override
            public void onClick(int position) {
                Intent intent1 = new Intent(context, MusicActivity.class);
                Bundle data = new Bundle();
                data.putInt("itemId", position);
                data.putSerializable("music", (Serializable) InternetList);
                intent1.putExtra("data", data);
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
                    Request request = new Request.Builder()
                            .url("http://106.15.89.25:8080/TestMusic/Daily")
                            .get()
                            .build();
                    Response response = client.newCall(request).execute();
                    String requestJson = response.body().string();
                    Gson gson = new Gson();
                    List<Song> temp=gson.fromJson(requestJson, new TypeToken<List<Song>>() {}.getType());
                    InternetList.clear();
                    InternetList.addAll(temp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
