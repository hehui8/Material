package com.example.he.material.Activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.he.material.Adapter.SongSheetAdapter;
import com.example.he.material.MODLE.Data;
import com.example.he.material.MODLE.GEDAN.Root;
import com.example.he.material.MODLE.GEDAN.songs;
import com.example.he.material.R;
import com.example.he.material.UI.StickHeadScrollView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * project: Material
 * author : Android研发部_姓名
 * date : 2019/2/18
 * time : 19:23
 * email : 企业邮箱
 * note : 说明
 */
public class SongSheetActivity extends AppCompatActivity {


    private StickHeadScrollView mStickHeadScrollView;
    private RecyclerView recyclerView;
    private SongSheetAdapter adapter;
    private OkHttpClient client;
    private com.example.he.material.MODLE.Data mData;
    private List<songs> songList = new ArrayList<>();
    private Root mRoot;
    private ImageView viewImg;
    private ViewGroup mEmpty;
    private ViewGroup mProgress;
    private SongSheetActivity mActivity;
    private TextView mSheetNameTv;
    private ImageView mAvatarImg;
    private Toolbar mToolBar;
    private int toolBarHeight;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_sheet);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("DATA");
            if (bundle != null) {
                mData = (Data) bundle.getSerializable("CLICK_SONG_SHEET_POSITION");
            }
        }

        mActivity = this;
        mSheetNameTv = findViewById(R.id.tv_song_sheet_name);
        mAvatarImg = findViewById(R.id.img_avatar);
        client = new OkHttpClient.Builder().build();
        viewImg = findViewById(R.id.img_bg);
        View headView = findViewById(R.id.tv_head);
        recyclerView = findViewById(R.id.love_list);
        mEmpty = findViewById(R.id.empty);
        mProgress = findViewById(R.id.progress);
        mStickHeadScrollView = findViewById(R.id.stick_scrollview);
        mToolBar = findViewById(R.id.my_toolbar_sheet);
        //避免自动滑动到底部
        headView.setFocusable(true);
        headView.setFocusableInTouchMode(true);
        headView.requestFocus();
        adapter = new SongSheetAdapter(songList, new SongSheetAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

            }

            @Override
            public void onLongClick(int position) {

            }
        });

        requestSongList();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        mStickHeadScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

//                float scale = 0;
//                int alpha = 0;
//                if (scrollY <= toolBarHeight) {
//                    scale = (float) scrollY / toolBarHeight;
//                    alpha = (int) (255 * scale);
//                    // 随着滑动距离改变透明度
//                    // Log.e("al=","="+alpha);
//                    mToolBar.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
//                } else {
//                    if (alpha < 255) {
//                        // 防止频繁重复设置相同的值影响性能
//                        alpha = 255;
//                        mToolBar.setBackgroundColor(Color.argb(alpha, 255, 0, 0));
//                    }
//                }
            }
        });
        mStickHeadScrollView.resetHeight(headView, recyclerView);

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        toolBarHeight = mToolBar.getHeight();
        Log.d("tag22", "qweqweqe" + toolBarHeight);
    }

    public SongSheetActivity getActivity() {
        return this;
    }

    public void requestSongList() {
        Request request = new Request.Builder()
                .url("https://api.bzqll.com/music/netease/songList?key=579621905&id=" + mData.getId() + "&limit" +
                        "=10" +
                        "&offset=0")
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mEmpty.setVisibility(View.VISIBLE);
                        mStickHeadScrollView.setVisibility(View.GONE);
                        Toast.makeText(SongSheetActivity.this, "网络错误，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() != null) {
                    final String str = response.body().string();
                    Gson gson = new Gson();
                    mRoot = gson.fromJson(str, Root.class);
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            com.example.he.material.MODLE.GEDAN.Data mTempData = mRoot.getData();
                            if (mTempData != null && mTempData.getSongs() != null) {
                                List<songs> temp = mTempData.getSongs();
                                if (temp != null) {
                                    mSheetNameTv.setText(mTempData.getSongListName());
                                    songList.clear();
                                    songList.addAll(temp);
                                    Glide.with(mActivity)
                                            .load(mTempData.getSongListPic())
                                            .apply(new RequestOptions().error(R.drawable.default_img).bitmapTransform(new BlurTransformation(14, 15)))
                                            .into(viewImg);
                                    Glide.with(mActivity)
                                            .load(mTempData.getSongListPic())
                                            .apply(new RequestOptions().error(R.drawable.default_img))
                                            .into(mAvatarImg);
                                    adapter.notifyDataSetChanged();
                                }
                            } else {
                                mEmpty.setVisibility(View.VISIBLE);
                                mStickHeadScrollView.setVisibility(View.GONE);
                            }

                        }
                    });

                }

            }
        });

    }
}
