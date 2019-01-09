package com.example.he.material.Fragment_List;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.he.material.Activity.NewRecentSearchActivity;
import com.example.he.material.Adapter.SearchRecentAdapter;
import com.example.he.material.Adapter.SearchResultAdapter;
import com.example.he.material.MODLE.Song;
import com.example.he.material.R;

import java.util.ArrayList;
import java.util.List;

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
    private Context context;
    private List<String> searchRecent;
    private String keyname;
    private SearchRecentAdapter adapter;


    public static SearchRecentFragment newInstance(String keyname) {
        SearchRecentFragment newFragment = new SearchRecentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("key", keyname);
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
        String searchName = getNewRecentSearchActivity().getSearchText().getText().toString();
        if (searchRecent != null) {
            if (searchRecent.size() < 10) {
                searchRecent.add(searchName);
            } else {
               searchRecent.add(0,searchName);
            }
        }

        super.onViewCreated(view, savedInstanceState);
    }

    private NewRecentSearchActivity getNewRecentSearchActivity() {
        if (getActivity() instanceof NewRecentSearchActivity) {
            return (NewRecentSearchActivity) getActivity();
        }
        return null;
    }
}
