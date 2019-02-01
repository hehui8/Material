package com.example.he.material.Activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;

import com.example.he.material.MODLE.Song;
import com.example.he.material.Utils.AndroidWorkaround;
import com.example.he.material.Utils.DataUtils;

import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.he.material.Fragment_List.SearchRecentFragment;
import com.example.he.material.Fragment_List.SearchResultFragment;
import com.example.he.material.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static com.example.he.material.Utils.DataUtils.hideKeyboard;
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
    private EditText mSearchText;
    private TextView mCancelText;
    private ImageView mClearText;

    private LinearLayout mLinearlayout;
    private List<String> fragmentStacks;

    private SearchResultFragment mResultFragment;
    private SearchRecentFragment mRecentFragment;
    private View mFocusView;
    private ArrayList<Song> list = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        }
        setContentView(R.layout.activity_search_new);
        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
        }
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
                if (mCancelText.getText().toString().equals("搜索")) {
                    search();
                    mFocusView.requestFocus();
                    showSearchFragment();
                }
                if (mCancelText.getText().toString().equals("取消")) {
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
            } else {
                requestForSearch(name);
                putData(name);
                hideKeyboard(mCancelText);
                mSearchText.clearFocus();
                mClearText.setVisibility(View.INVISIBLE);
            }
        }
    }

    public EditText getSearchText() {
        return mSearchText;
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

    public void showSearchFragment() {

        if (fragmentStacks != null) {
            String fragmentName = SearchResultFragment.class.getSimpleName();
            fragmentStacks.remove(fragmentName);
            fragmentStacks.add(fragmentName);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (mRecentFragment != null) {
            fragmentTransaction.hide(mRecentFragment);
        }
        if (mResultFragment == null) {
            mResultFragment = SearchResultFragment.newInstance();
        } else {
            mResultFragment.onVisible();
        }
        if (mResultFragment.isAdded()) {
            fragmentTransaction.show(mResultFragment).commit();
        } else {
            fragmentTransaction.add(R.id.search_result_container, mResultFragment).commit();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fragmentStacks != null) {
            fragmentStacks.clear();
            fragmentStacks = null;
        }
    }

    public ArrayList<Song> getList() {
        return list;
    }

    public void requestForSearch(String string) {
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        String url = "http://106.15.89.25:8080/TestMusic/SearchServlet";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(mediaType, string))
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "稍后再试", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String getSearch = response.body().string();
                Log.d("SEACH", getSearch);
                Gson json = new GsonBuilder().create();
                try {
                    List<Song> tempList = json.fromJson(getSearch, new TypeToken<ArrayList<Song>>() {
                    }.getType());
                    list.clear();
                    list.addAll(tempList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!list.isEmpty()) {
                            mResultFragment.updateList();
                            mResultFragment.setEmpty(false);
                        } else {
                            mResultFragment.setEmpty(true);
                        }
                    }
                });


            }
        });

    }

    @Override
    public void onBackPressed() {
        /*
         * 回退处理，根据fragmentStack记录进行回退操作
         */
        //如果fragmentStacks为空或者stacks中就一个fragment，那么点击返回键直接结束当前activity
        if (mSearchText.getText() != null) {
            mSearchText.setText("");
            mSearchText.requestFocus();
        }
        if (fragmentStacks == null || fragmentStacks.size() == 1) {
            finish();
        } else {
            //返回上一个fragment，调用show（Fragment名）
            fragmentStacks.remove(fragmentStacks.size() - 1);
            String fragmentName = fragmentStacks.get(fragmentStacks.size() - 1);
            if (SearchRecentFragment.class.getSimpleName().equals(fragmentName)) {
                showRecentFragment();
            } else if (SearchResultFragment.class.getSimpleName().equals(fragmentName)) {
                showSearchFragment();
            } else {
                finish();
            }
        }
    }
}
