package com.example.he.material.Fragment_List;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.he.material.Activity.MusicActivity;
import com.example.he.material.Adapter.MusicAdapter;
import com.example.he.material.MODLE.Song;
import com.example.he.material.R;
import com.example.he.material.UI.StickHeadScrollView;
import com.example.he.material.Utils.SPUtils;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * project: Material
 * author : Android研发部_姓名
 * date : 2019/2/15
 * time : 19:02
 * email : 企业邮箱
 * note : 说明
 */
public class LoveFragment extends Fragment {
    private StickHeadScrollView mStickHeadScrollView;
    private RecyclerView recyclerView;
    private MusicAdapter adapter;
    private List<Song> songList=new ArrayList<>();
    private SPUtils spUtils;
    private SwipeRefreshLayout mRefresh;

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
        return inflater.inflate(R.layout.fragment_love, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View headView = view.findViewById(R.id.tv_head);
        recyclerView =view.findViewById(R.id.love_list);
        mStickHeadScrollView =  view.findViewById(R.id.stick_scrollview);
        //避免自动滑动到底部
        headView.setFocusable(true);
        headView.setFocusableInTouchMode(true);
        headView.requestFocus();
        //2.set height
        mRefresh=view.findViewById(R.id.layout_fresh);

        spUtils=new SPUtils(getContext());
        Gson gson=new Gson();
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Map<String,?> map=spUtils.getLoveListSP("LOVE");
                if(map!=null) {
                    songList.clear();
                    Iterator iter = map.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        String key = (String) entry.getKey();
                        String val = (String) entry.getValue();
                        Song song = new Gson().fromJson(val, Song.class);
                        if (song != null)
                            songList.add(song);
                    }
                }
                adapter.notifyDataSetChanged();
                mRefresh.setRefreshing(false);

            }
        });

        Map<String,?> map=spUtils.getLoveListSP("LOVE");
        if(map!=null){
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                String val = (String) entry.getValue();
                Song song =gson.fromJson(val,Song.class);
                if(song!=null)
                songList.add(song);
            }
        }

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adapter=new MusicAdapter(songList, new MusicAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent1 = new Intent(getContext(), MusicActivity.class);
                Bundle data = new Bundle();
                data.putInt("itemId", position);
                data.putSerializable("music", (Serializable) songList);
                intent1.putExtra("data", data);
                startActivity(intent1);
            }

            @Override
            public void onLongClick(int position) {

            }
        });
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        mStickHeadScrollView.resetHeight(headView, recyclerView);
    }
}
