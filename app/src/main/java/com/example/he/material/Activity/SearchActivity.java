package com.example.he.material.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.he.material.Adapter.MyAdapter;
import com.example.he.material.Interface.FilterListener;
import com.example.he.material.MODLE.GEDAN.Songs;
import com.example.he.material.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {//使用toolbar需继承AppCompatActivity
  // private RecyclerView mRecyclerView_search;
   private ListView mListview;
    private EditText mEditText_search;

    //private List<String> list = new ArrayList<String>();
    private List<Songs> list =new ArrayList<Songs>();
    boolean isFilter;
    private MyAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
        list=(List<Songs>) getIntent().getSerializableExtra("Internet_data");
        Initview();
        setData();
        setListeners();



    }

    private  void Initview (){
       mEditText_search=(EditText)findViewById(R.id.editTxet_search);
       mListview =(ListView) findViewById(R.id.recylcer_search);


    }

    private void setData(){
        //initData();
        adapter =new MyAdapter(list, this, new FilterListener() {
            @Override
            public void getFilterData(List<Songs> list) {
                setItemClick(list);
            }
        });
        mListview.setAdapter(adapter);
    }

    protected void setItemClick(final List<Songs> filter_lists) {
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 点击对应的item时，弹出toast提示所点击的内容
                Toast.makeText(SearchActivity.this, filter_lists.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setListeners() {
        // 没有进行搜索的时候，也要添加对listView的item单击监听
        setItemClick(list);

        /**
         * 对编辑框添加文本改变监听，搜索的具体功能在这里实现
         * 很简单，文本该变的时候进行搜索。关键方法是重写的onTextChanged（）方法。
         */
        mEditText_search.addTextChangedListener(new TextWatcher() {

            /**
             *
             * 编辑框内容改变的时候会执行该方法
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 如果adapter不为空的话就根据编辑框中的内容来过滤数据
                if(adapter != null){
                    adapter.getFilter().filter(s);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
    }



}
