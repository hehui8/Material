package com.example.he.material.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.he.material.Adapter.SearchRecentAdapter;
import com.example.he.material.Fragment_List.SearchResultFragment;
import com.example.he.material.R;

import java.util.ArrayList;
import java.util.List;

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
    private LinearLayout mLinearlayout;
    private List<String> fragmentStacks;



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
        mLinearlayout =findViewById(R.id.search_result_container);
        fragmentStacks = new ArrayList<>();

    }

    private void showRecnetFragment(){
        if (fragmentStacks != null) {
            String fragmentName = SearchResultFragment.class.getSimpleName();
            fragmentStacks.remove(fragmentName);
            fragmentStacks.add(fragmentName);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

    }




    public TextView getSearchText(){
        return  mSearchText;
    }
    public TextView getCleanText(){
        return  mCleanText;
    }
}
