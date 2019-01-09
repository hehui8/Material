package com.example.he.material.Fragment_List;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.he.material.Adapter.SearchResultAdapter;
import com.example.he.material.MODLE.Song;
import com.example.he.material.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SearchResultFragment extends Fragment {

    private RecyclerView recyclerView;
    private Context context;
    private List<Song> searchresult;
    private String keyname;
    private SearchResultAdapter adapter;


    public static SearchResultFragment newInstance(String keyname) {
        SearchResultFragment newFragment = new SearchResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString("key", keyname);
        newFragment.setArguments(bundle);
        return newFragment;

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {

        keyname = getArguments().getString("key");
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_search_result, container, false);
        recyclerView = view.findViewById(R.id.search_result_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        initData(keyname);
        adapter = new SearchResultAdapter(searchresult, getContext());
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void initData(String searchname) {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        String url = "http://192.168.55.15:8080/TestMusic/LoginServlet";

        String requestBody = searchname;
        OkHttpClient okHttpClient = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(mediaType, requestBody))//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("request-login", "onFailure: ");
                searchresult = null;

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String requestSuccess = response.body().string();
                Log.d("request-login", "onResponse: " + requestSuccess);
                Gson gson = new Gson();
                List<Song> list = gson.fromJson(requestSuccess, new TypeToken<List<Song>>() {
                }.getType());
                if (list.size() > 0 && list != null) {
                    searchresult = list;
                }
            }
        });
    }

}

