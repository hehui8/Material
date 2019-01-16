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
import android.widget.Toast;

import com.example.he.material.Activity.NewRecentSearchActivity;
import com.example.he.material.Adapter.SearchRecentAdapter;
import com.example.he.material.Adapter.SearchResultAdapter;
import com.example.he.material.MODLE.Song;
import com.example.he.material.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
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
    private ArrayList<Song> searchresult;
    private SearchResultAdapter adapter;
    private  View mEmpty;


    public static SearchResultFragment newInstance() {
        SearchResultFragment newFragment = new SearchResultFragment();
        Bundle bundle = new Bundle();
        newFragment.setArguments(bundle);
        return newFragment;

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_search_result, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.search_result_list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getNewRecentSearchActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        mEmpty =view.findViewById(R.id.empty);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        searchresult=getNewRecentSearchActivity().getList();
        if(searchresult == null){
            searchresult =new ArrayList<>();
        }
        if (adapter == null) {
            adapter = new SearchResultAdapter(searchresult, new SearchRecentAdapter.OnItemClickListener() {
                @Override
                public void onClick(int position) {
                    if (position > 0) {
                   /*     getNewRecentSearchActivity().getSearchText().setText(searchresult.get(position));*/

                    }
                }
                @Override
                public void onLongClick(int position) {
                }
            });
        }
        recyclerView.setAdapter(adapter);
        updateList();
    }


    private NewRecentSearchActivity getNewRecentSearchActivity() {
        if (getActivity() instanceof NewRecentSearchActivity) {
            return (NewRecentSearchActivity) getActivity();
        }
        return null;
    }

    public void updateList() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void onVisible() {
        updateList();
    }


    public void setEmpty(){
        if(mEmpty!=null){
            mEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
    }

}

