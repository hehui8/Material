package com.example.he.material.Fragment_List;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.he.material.Activity.SongSheetActivity;
import com.example.he.material.Adapter.SongSheetAdapter;
import com.example.he.material.MODLE.GEDAN.Data;
import com.example.he.material.MODLE.GEDAN.Root;
import com.example.he.material.MODLE.GEDAN.songs;
import com.example.he.material.R;
import com.example.he.material.UI.StickHeadScrollView;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
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
 * date : 2019/2/20
 * time : 15:47
 * email : 企业邮箱
 * note : 说明
 */
public class SongSheetFragment extends Fragment {

    private StickHeadScrollView mStickHeadScrollView;
    private RecyclerView recyclerView;
    private SongSheetAdapter adapter;
    private OkHttpClient client;
    private com.example.he.material.MODLE.Data mData;
    private List<songs> songList = new ArrayList<>();
    private Root mRoot;
    private ImageView viewImg;
    private ViewGroup mEmpty;
    private SongSheetActivity mActivity;
    private TextView mSheetNameTv;
    private ImageView mAvatarImg;

    public static SongSheetFragment newInstance(com.example.he.material.MODLE.Data data) {

        Bundle args = new Bundle();
        args.putSerializable("data", (Serializable) data);
        SongSheetFragment fragment = new SongSheetFragment();
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
        mActivity = new SongSheetActivity().getActivity();
        mSheetNameTv=view.findViewById(R.id.tv_song_sheet_name);
        mAvatarImg=view.findViewById(R.id.img_avatar);
        client = new OkHttpClient.Builder().build();
        viewImg = view.findViewById(R.id.img_bg);
        View headView = view.findViewById(R.id.tv_head);
        recyclerView = view.findViewById(R.id.love_list);
        mEmpty = view.findViewById(R.id.empty);
        mStickHeadScrollView = view.findViewById(R.id.stick_scrollview);

        //避免自动滑动到底部
        headView.setFocusable(true);
        headView.setFocusableInTouchMode(true);
        headView.requestFocus();

        mData = (com.example.he.material.MODLE.Data) getArguments().getSerializable("data");
        adapter = new SongSheetAdapter(songList, new SongSheetAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

            }

            @Override
            public void onLongClick(int position) {

            }
        });
        requestSongList();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        mStickHeadScrollView.resetHeight(headView, recyclerView);
    }

    public void requestSongList() {
        Request request = new Request.Builder()
                .url("https://api.bzqll.com/music/netease/songList?key=579621905&id=" + mData.getId() + "&limit=10" +
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
                        Toast.makeText(getContext(), "网络错误，请稍后再试", Toast.LENGTH_SHORT).show();
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
                            Data mTempData = mRoot.getData();
                            if (mTempData != null && mTempData.getSongs() != null) {
                                List<songs> temp = mTempData.getSongs();
                                if (temp != null ) {
                                    mEmpty.setVisibility(View.GONE);
                                    mStickHeadScrollView.setVisibility(View.VISIBLE);
                                    mSheetNameTv.setText(mTempData.getSongListName());
                                    songList.clear();
                                    songList.addAll(temp);

                                    Glide.with(SongSheetFragment.this)
                                            .load(mTempData.getSongListPic())
                                            .apply(new RequestOptions().error(R.drawable.default_img).bitmapTransform(new BlurTransformation(14, 15)))
                                            .into(viewImg);
                                    Glide.with(SongSheetFragment.this)
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
