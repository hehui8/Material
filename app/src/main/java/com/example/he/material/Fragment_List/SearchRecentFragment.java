package com.example.he.material.Fragment_List;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.he.material.Activity.NewRecentSearchActivity;
import com.example.he.material.Adapter.SearchRecentAdapter;
import com.example.he.material.Adapter.SearchResultAdapter;
import com.example.he.material.MODLE.Song;
import com.example.he.material.Utils.DataUtils;
import com.example.he.material.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.he.material.Utils.DataUtils.putData;

/**
 * project: Material
 * author : Android研发部_姓名
 * date : 2019/1/9
 * time : 17:53
 * email : 企业邮箱
 * note : 说明
 */
public class SearchRecentFragment extends Fragment {

    private RecyclerView recyclerView;

    private List<String> recentSearch;


    private ImageView mDelte;
    private SearchRecentAdapter adapter;


    public static SearchRecentFragment newInstance() {
        SearchRecentFragment newFragment = new SearchRecentFragment();
        Bundle bundle = new Bundle();
        newFragment.setArguments(bundle);
        return newFragment;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {

        return inflater.inflate(R.layout.fragment_search_recent, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView=view.findViewById(R.id.search_recent_list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        mDelte = view.findViewById(R.id.clear_data);
        mDelte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataUtils.clearDataList();
                recentSearch=DataUtils.getData();
                updateList();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * 在这做网络请求之类的操作
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recentSearch =DataUtils.getData();

        if (adapter == null) {
            adapter = new SearchRecentAdapter(recentSearch, new SearchRecentAdapter.OnItemClickListener() {
                @Override
                public void onClick(int position) {
                    if (position >=0) {
                        getNewRecentSearchActivity().getSearchText().setText(recentSearch.get(position));

                    }
                }
                @Override
                public void onLongClick(int position) {
                }
            });
        }
        recyclerView.setAdapter(adapter);

    }



    private NewRecentSearchActivity getNewRecentSearchActivity() {
        if (getActivity() instanceof NewRecentSearchActivity) {
            return (NewRecentSearchActivity) getActivity();
        }
        return null;
    }

    /**
     * 获取searchRecent中指定位置的历史记录
     *
     * @param position
     * @return
     */
    private String getKey(int position) {
        if (position < recentSearch.size() && position > 0) {
            String key = recentSearch.get(position).toString();
            return key;
        }
        return null;
    }

    public void onVisible() {
        updateList();
    }

    public void updateList() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }


}
