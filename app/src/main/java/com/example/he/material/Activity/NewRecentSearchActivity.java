package com.example.he.material.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;

import com.example.he.material.Utils.DataUtils;

import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.he.material.Adapter.SearchRecentAdapter;
import com.example.he.material.Fragment_List.SearchRecentFragment;
import com.example.he.material.Fragment_List.SearchResultFragment;
import com.example.he.material.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.he.material.Utils.DataUtils.hideKeyboard;
import static com.example.he.material.Utils.DataUtils.hideSoftKeyboard;
import static com.example.he.material.Utils.DataUtils.putData;

/**
 * project: Material
 * author : Android研发部_姓名
 * date : 2019/1/8
 * time : 16:56
 * email : 企业邮箱
 * note : 说明
 */
public class NewRecentSearchActivity extends AppCompatActivity {
    private RecyclerView mRecyclerview;
    private EditText mSearchText;
    private TextView mCancelText;
    private TextView mCleanText;
    private ImageView mClearText;
    private SearchRecentAdapter mAdapter;
    private LinearLayout mLinearlayout;
    private List<String> fragmentStacks;

    private SearchResultFragment mResultFragment;
    private SearchRecentFragment mRecentFragment;
    private View mFocusView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_new);


        mFocusView = findViewById(R.id.focus_view);
        searchInitView();
        showRecentFragment();
        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {


                if (TextUtils.isEmpty(editable)) {
                    mCancelText.setText("取消");
                    mClearText.setVisibility(View.INVISIBLE);
                } else if (mSearchText.hasFocus()) {
                    mCancelText.setText("搜索");
                    mClearText.setVisibility(View.VISIBLE);
                }
            }
        });

        mSearchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mSearchText != null) {
                    if (!mSearchText.hasFocus()) {
                        mClearText.setVisibility(View.GONE);
                    } else if (!TextUtils.isEmpty(mSearchText.getEditableText())) {
                        mClearText.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        mClearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchText.setText("");
            }
        });

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mFocusView.requestFocus();
                    hideKeyboard(mCancelText);
                    String name = mSearchText.getText().toString();
                    putData(name);
                    mRecentFragment.updateList();
                    return true;
                }
                return false;
            }
        });


        mCancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCancelText.getText().toString().equals("搜索")){
                    search();
                    mFocusView.requestFocus();
                }
                if(mCancelText.getText().toString().equals("取消")){
                    finish();
                }
            }
        });


    }


    private void searchInitView() {
        mClearText = findViewById(R.id.search_clear);
        mCancelText = findViewById(R.id.search_cancel);
        mSearchText = findViewById(R.id.search_text);
        mLinearlayout = findViewById(R.id.search_result_container);
        fragmentStacks = new ArrayList<>();

    }

    public void search() {
        if (mSearchText != null) {
            String name = mSearchText.getText().toString();
            if (name.equals("")) {
                Toast.makeText(NewRecentSearchActivity.this, "请输入搜索内容", Toast.LENGTH_SHORT).show();
            } else{
                putData(name);
                mRecentFragment.updateList();
                hideKeyboard(mCancelText);
                mSearchText.clearFocus();
                mClearText.setVisibility(View.INVISIBLE);
            }
        }
    }

    public EditText getSearchText() {
        return mSearchText;
    }

    public TextView getCleanText() {
        return mCleanText;
    }

    public TextView getmCancelText() {
        return mCancelText;
    }

    public ImageView getmClearText() {
        return mClearText;
    }

    public void showRecentFragment() {

        if (fragmentStacks != null) {
            String fragmentName = SearchRecentFragment.class.getSimpleName();
            fragmentStacks.remove(fragmentName);
            fragmentStacks.add(fragmentName);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (mResultFragment != null) {
            fragmentTransaction.hide(mResultFragment);
        }
        if (mRecentFragment == null) {
            mRecentFragment = SearchRecentFragment.newInstance();
        } else {
            mRecentFragment.onVisible();
        }
        if (mRecentFragment.isAdded()) {
            fragmentTransaction.show(mRecentFragment).commitAllowingStateLoss();
        } else {
            fragmentTransaction.add(R.id.search_result_container, mRecentFragment)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(fragmentStacks!=null) {
            fragmentStacks.clear();
            fragmentStacks=null;
        }
    }
}
