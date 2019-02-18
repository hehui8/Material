package com.example.he.material.Activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.he.material.Adapter.MyFragmentAdapter;
import com.example.he.material.Controler.Utils;
import com.example.he.material.Fragment_List.InternetFragment;
import com.example.he.material.Fragment_List.LocalMusicFragment;
import com.example.he.material.Fragment_List.LoveFragment;
import com.example.he.material.Fragment_List.MainFragment;
import com.example.he.material.MODLE.Song;
import com.example.he.material.MODLE.User;
import com.example.he.material.R;
import com.example.he.material.Utils.AndroidWorkaround;
import com.example.he.material.Utils.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tab0;
    private TextView tab1;
    private TextView tab2;
    private TextView tab3;
    private TextView mTextview;
    private TextView mToolbar_title;
    private ImageView mImageView_search;
    private CircleImageView circleImageView;
    private Toolbar mytoolbar;
    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private List<Song> mMusicList, musicList;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<Song> musicInternetList = new ArrayList<>();
    private LocalMusicFragment s1;
    private InternetFragment i1;
    private User user = null;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ViewPager viewPager;//viewpager 对象
    private static int FLAG = 0; //登陆状态，默认为0（未登录）
    private static String strRequestCloudmusic;//该变量接受api接口返回的json字符串
    private long exitTime = 0;
    private MainFragment mainFragment;
    private LoveFragment mLoveFragment;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
        }
        if (savedInstanceState != null) {
            if (((List<Song>) savedInstanceState.getSerializable("online")) != null && !((List<Song>) savedInstanceState.getSerializable("online")).isEmpty()) {
                musicInternetList = (List<Song>) savedInstanceState.getSerializable("online");
            }
        }



        mToolbar_title = this.findViewById(R.id.toolbar_title);
        tab0 = this.findViewById(R.id.txt_0);
        tab1 = this.findViewById(R.id.txt_1);
        tab2 = this.findViewById(R.id.txt_2);
        tab3 = this.findViewById(R.id.txt_3);
        mImageView_search = findViewById(R.id.search);
        navigationView = findViewById(R.id.navigation);
        mDrawerLayout = findViewById(R.id.internet_layout);
        viewPager = findViewById(R.id.viewpage);
        navigationView = findViewById(R.id.navigation);
        View headView = navigationView.getHeaderView(0);
        circleImageView = headView.findViewById(R.id.daohang_view);
        mTextview = headView.findViewById(R.id.daohang_name);
        mytoolbar = findViewById(R.id.mytoolbar);
        initData();
        bindView();
        Intent intentFrom = getIntent();
        if (intentFrom != null) {
            FLAG = 1;
            user = (User) intentFrom.getSerializableExtra("user");
            if (user != null) {
                mTextview.setText(user.getName());
                FLAG = 1;
                try {
                    sendRequest();
                    Gson gson = new Gson();
                    List<Song> temp = gson.fromJson(strRequestCloudmusic, new TypeToken<List<Song>>() {
                    }.getType());
                    Log.d("hess", "onCreate: " + strRequestCloudmusic);
                    musicInternetList.clear();
                    musicInternetList.addAll(temp);
                    i1 = InternetFragment.newInstance(musicInternetList);
                    mFragmentList.add(i1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (viewPager.getAdapter() != null) {
                    viewPager.getAdapter().notifyDataSetChanged();
                }
            }
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        mainFragment = MainFragment.newInstance(musicInternetList);
        mFragmentList.add(0, mainFragment);

        s1 = LocalMusicFragment.newInstance(musicList);
        mFragmentList.add(1,s1);

        mLoveFragment=mLoveFragment.newInstance();
        mFragmentList.add(mLoveFragment);



        //实现toolbar的导航栏点击功能
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mytoolbar, R.string
                .drawer_layout_open, R.string.drawer_layout_close);
        //将fragmentList绑定到viewpager
        viewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(), mFragmentList));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 0:
                        selectImg(tab0);
                        break;
                    case 1:
                        selectImg(tab1);
                        break;
                    case 2:
                        selectImg(tab2);
                        break;
                    case 3:
                        selectImg(tab3);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private void initData() {

        MediaScannerConnection.scanFile(this, new String[]{
                Environment.getExternalStorageDirectory().getAbsolutePath()}, null, null);

        mMusicList = Utils.getmusic(MainActivity.this);
        if (mMusicList != null) {
            musicList = mMusicList;
        }
    }

    //UI组件初始化与事件绑定
    private void bindView() {
        //点击头像图标触发登录选项
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FLAG == 0) {
                    Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent1);
                }
            }
        });
        /*
         *   侧滑栏的menu点击事件
         * */
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.list_message:
                        break;
                    case R.id.list_person_info:
                        break;
                    case R.id.list_set:
                        break;
                    case R.id.list_exit:
                        if (FLAG == 1) {
                            item.setChecked(true);
                            Log.d("alert", "zhixingdaozheli");
                            final AlertDialog.Builder localBuilder = new AlertDialog.Builder(MainActivity.this);
                            localBuilder.setTitle("提示");
                            localBuilder.setMessage("确定退出当前账号吗？");
                            localBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            localBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    outRequest();
                                    Intent intent = new Intent();
                                    intent.setAction("Adapter.is.changer");
                                    Log.d("guangbo_exit", "f发送退出广播");
                                    MainActivity.this.sendBroadcast(intent);
                                }

                            });
                            localBuilder.setCancelable(false).create();
                            localBuilder.show();
                        } else {
                            Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                        }
                }
                item.setCheckable(true);
                item.setChecked(false);
                return false;

            }

        });
        tab0.setOnClickListener(this);
        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        tab3.setOnClickListener(this);
        mImageView_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Drawable drawable = null;
        switch (v.getId()) {
            case R.id.txt_0:
                selectImg(v);
                viewPager.setCurrentItem(0);
                break;
            case R.id.txt_1:
                selectImg(v);
                viewPager.setCurrentItem(1, true);
                break;
            case R.id.txt_2:
                if (FLAG == 1) {
                    selectImg(v);
                    viewPager.setCurrentItem(2, true);
                } else {
                    Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txt_3:
                selectImg(v);
                viewPager.setCurrentItem(3, true);
                break;
            case R.id.search:
                if (/*FLAG == 1*/true) {
                  /*  Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra("Internet_data", (Serializable) musicInternetList);*/
                    Intent intent = new Intent(MainActivity.this, NewRecentSearchActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        transaction.commit();
    }


    public void sendRequest() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient.Builder().build();
                    Request request = new Request.Builder()
                            .url("http://106.15.89.25:8080/TestMusic/Daily")
                            .get()
                            .build();
                    Response response = client.newCall(request).execute();
                    strRequestCloudmusic = response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        try {
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("online", (Serializable) musicInternetList);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        exit();
    }

    private void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(),
                    "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            Intent intent = new Intent();
            intent.setAction("Adapter.is.changer");
            Log.d("guangbo_exit", "f发送退出广播");
            MainActivity.this.sendBroadcast(intent);
            finish();
            //System.exit(0);
        }
    }

    public void selectImg(View v) {
        if (v != null) {
            if (v.getId() == R.id.txt_0) {
                mToolbar_title.setText(R.string.home);
                Drawable drawable = getDrawable(R.drawable.ic_music_select);
                if (drawable != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tab0.setCompoundDrawables(null, drawable, null, null);
                    tab0.setTextColor(getResources().getColor(R.color.tab_icon_select));
                }
                drawable = getDrawable(R.drawable.ic_music_default);
                if (drawable != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tab1.setCompoundDrawables(null, drawable, null, null);
                    tab1.setTextColor(getResources().getColor(R.color.tab_icon_default));
                }
                drawable = getDrawable(R.drawable.ic_in_default);
                if (drawable != null && tab2 != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tab2.setCompoundDrawables(null, drawable, null, null);
                    tab2.setTextColor(getResources().getColor(R.color.tab_icon_default));

                }
                drawable = getDrawable(R.drawable.user_default);
                if (drawable != null && tab3 != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tab3.setCompoundDrawables(null, drawable, null, null);
                    tab3.setTextColor(getResources().getColor(R.color.tab_icon_default));
                }
            } else if (v.getId() == R.id.txt_1) {
                mToolbar_title.setText(R.string.local);
                Drawable drawable = getDrawable(R.drawable.ic_music_default);
                if (drawable != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tab0.setCompoundDrawables(null, drawable, null, null);
                    tab0.setTextColor(getResources().getColor(R.color.tab_icon_default));
                }

                drawable = getDrawable(R.drawable.ic_music_select);
                if (drawable != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tab1.setCompoundDrawables(null, drawable, null, null);
                    tab1.setTextColor(getResources().getColor(R.color.tab_icon_select));
                }
                drawable = getDrawable(R.drawable.ic_in_default);
                if (drawable != null && tab2 != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tab2.setCompoundDrawables(null, drawable, null, null);
                    tab2.setTextColor(getResources().getColor(R.color.tab_icon_default));
                }
                drawable = getDrawable(R.drawable.user_default);
                if (drawable != null && tab3 != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tab3.setCompoundDrawables(null, drawable, null, null);
                    tab3.setTextColor(getResources().getColor(R.color.tab_icon_default));
                }

            } else if (v.getId() == R.id.txt_2) {
                mToolbar_title.setText(R.string.internet);
                Drawable drawable = getDrawable(R.drawable.ic_in_select);
                if (drawable != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tab2.setCompoundDrawables(null, drawable, null, null);
                    tab2.setTextColor(getResources().getColor(R.color.tab_icon_select));
                }
                drawable = getDrawable(R.drawable.ic_music_default);
                if (drawable != null && tab2 != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tab1.setCompoundDrawables(null, drawable, null, null);
                    tab1.setTextColor(getResources().getColor(R.color.tab_icon_default));
                }
                drawable = getDrawable(R.drawable.ic_music_default);
                if (drawable != null && tab2 != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tab0.setCompoundDrawables(null, drawable, null, null);
                    tab0.setTextColor(getResources().getColor(R.color.tab_icon_default));
                }
                drawable = getDrawable(R.drawable.user_default);
                if (drawable != null && tab3 != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tab3.setCompoundDrawables(null, drawable, null, null);
                    tab3.setTextColor(getResources().getColor(R.color.tab_icon_default));
                }
            } else {
                mToolbar_title.setText(R.string.favourite);
                Drawable drawable = getDrawable(R.drawable.user_select);
                if (drawable != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tab3.setCompoundDrawables(null, drawable, null, null);
                    tab3.setTextColor(getResources().getColor(R.color.tab_icon_select));
                }
                drawable = getDrawable(R.drawable.ic_in_default);
                if (drawable != null && tab2 != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tab2.setCompoundDrawables(null, drawable, null, null);
                    tab2.setTextColor(getResources().getColor(R.color.tab_icon_default));

                }
                drawable = getDrawable(R.drawable.ic_music_default);
                if (drawable != null && tab1 != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tab1.setCompoundDrawables(null, drawable, null, null);
                    tab1.setTextColor(getResources().getColor(R.color.tab_default));
                }
                drawable = getDrawable(R.drawable.ic_music_default);
                if (drawable != null && tab2 != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tab0.setCompoundDrawables(null, drawable, null, null);
                    tab0.setTextColor(getResources().getColor(R.color.tab_icon_default));
                }
            }

        }

    }

    public void outRequest() {
        SPUtils spUtils = new SPUtils(this);
        spUtils.addSP("lastUser", "");
        Gson gson = new Gson();
        String json = gson.toJson(user);
        //设置请求相关参数
        OkHttpClient client_exit = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
        //构造表单数据（通过json）
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; " +
                "charset=utf-8"), json);
        final Request request = new Request.Builder()
                //.url("http://118.25.27.150:8080/Music/Servlet_1")
                .url("http://106.15.89.25:8080/TestMusic/LogoutServlet")
                .post(requestBody)
                .build();
        Call call = client_exit.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请稍后再试", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String state_flag = response.body().string();
                Log.d("exit", "退出测试1" + state_flag);
                if (true) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTextview.setText("点击头像登陆");
                            FLAG = 0;
                            if (!mFragmentList.isEmpty()) {
                                mFragmentList.remove(2);
                                if (viewPager.getAdapter() != null) {
                                    viewPager.getAdapter().notifyDataSetChanged();
                                }
                            }

                        }
                    });
                }
            }
        });
    }
}
