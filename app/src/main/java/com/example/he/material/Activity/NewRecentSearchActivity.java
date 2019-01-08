package com.example.he.material.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.he.material.Adapter.SearchRecentAdapter;
import com.example.he.material.R;

/**
 * project: Material
 * author : Android研发部_姓名
 * date : 2019/1/8
 * time : 16:56
 * email : 企业邮箱
 * note : 说明
 */
public class NewRecentSearchActivity extends AppCompatActivity
{
    private RecyclerView mRecyclerview;
    private TextView mSearchText;
    private TextView mCancelText;
    private TextView mCleanText;
    private SearchRecentAdapter mAdapter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_new);
        searchInitView();

    }


    private void  searchInitView(){
        mRecyclerview=findViewById(R.id.search_data);
        mCancelText =findViewById(R.id.search_cancel);
        mCleanText =findViewById(R.id.search_clean);

        mSearchText=  findViewById(R.id.search_text);
    }
}
