package com.example.he.material.Activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.he.material.Adapter.MyFragmentAdapter;
import com.example.he.material.DataBase;
import com.example.he.material.Controler.DataBaseControler;
import com.example.he.material.Fragment_List.InternetFragment;
import com.example.he.material.MODLE.GEDAN.Root;
import com.example.he.material.MODLE.GEDAN.Songs;
import com.example.he.material.MODLE.Music;
import com.example.he.material.MODLE.User;
import com.example.he.material.R;
import com.example.he.material.Fragment_List.LocalMusicFragment;
import com.example.he.material.Controler.Utils;
import com.google.gson.Gson;

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
    private List<Music> mMusicList, musicList;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<Songs> music_internet_list = new ArrayList<>();
    private LocalMusicFragment s1;
    private InternetFragment i1;
    private User user = null;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ViewPager viewPager;//viewpager 对象
    private int offset = 0;//移动条图片的偏移量
    private int currIndex = 0;//当前页面的编号
    private int bmpWidth;// 移动条图片的长度
    private int one = 0; //移动条滑动一页的距离
    private int two = 0; //滑动条移动两页的距离
    private static int FLAG = 0; //登陆状态，默认为0（未登录）
    private static String str_request_cloudmusic;//该变量接受api接口返回的json字符串
    private Root root;
    private static boolean isExit = false;

    private long exitTime = 0;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mytoolbar = findViewById(R.id.mytoolbar);
        initData();
        bindView();

        s1 = new LocalMusicFragment(musicList, MainActivity.this);
        mFragmentList.add(s1);

        //实现toolbar的导航栏点击功能
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mytoolbar, R.string
                .drawer_layout_open, R.string.drawer_layout_close);

        //将fragmentList绑定到viewpager
        initview();
        viewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager(), mFragmentList));

    }


    private void initData() {

        MediaScannerConnection.scanFile(this, new String[]{
                Environment.getExternalStorageDirectory().getAbsolutePath()}, null, null);

        mMusicList = Utils.getmusic(MainActivity.this);
        System.out.print("id" + mMusicList.size());

        //将相关信息保存至数据库中
        DataBase mDatabase = new DataBase(this, "music", null, 1);
        mDatabase.getWritableDatabase();
        DataBaseControler mDBC = new DataBaseControler();
        for (int i = 0; i < mMusicList.size(); i++) {
            if (mMusicList.get(i) != null) {
                Music music = mMusicList.get(i);
                System.out.print("id" + mMusicList.get(i).getId());

                Utils mUtils = new Utils();

                mDBC.insert(mDatabase, music.getId(), music.getName(), music.getSinger(), music.getImageId(), music
                        .getPath());
            }
        }
        musicList = mDBC.query(mDatabase);
    }

    //UI组件初始化与事件绑定
    private void bindView() {
        navigationView = (NavigationView) findViewById(R.id.navigation);
        View headView = navigationView.getHeaderView(0);
        circleImageView = (CircleImageView) headView.findViewById(R.id.daohang_view);
        mTextview = (TextView) headView.findViewById(R.id.daohang_name);
        //点击头像图标触发登录选项
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (FLAG == 0) {
                    Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(intent1, 100);
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
                                            .url("http://118.25.27.150:8080/Music/Servlet_2")
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
                                                        mFragmentList.remove(1);
                                                        viewPager.getAdapter().notifyDataSetChanged();
                                                    }
                                                });
                                            }
                                        }
                                    });
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

        initview();
        //控件初始化

        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
        tab3.setOnClickListener(this);
        mImageView_search.setOnClickListener(this);

    }

    /*
     * 对xml文件中的控件进行绑定
     * */

    public void initview() {
        mToolbar_title = this.findViewById(R.id.toolbar_title);
        tab1 = this.findViewById(R.id.txt_1);
        tab2 = this.findViewById(R.id.txt_2);
        tab3 = this.findViewById(R.id.txt_3);
        mImageView_search = findViewById(R.id.search);
        navigationView = findViewById(R.id.navigation);
        mDrawerLayout = findViewById(R.id.internet_layout);
        viewPager = findViewById(R.id.viewpage);

        Drawable drawable = getDrawable(R.drawable.ic_loacl_select);
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tab1.setCompoundDrawables(null, drawable, null, null);
            tab1.setTextColor(getResources().getColor(R.color.tab_select));
        }


    }

    /**
     * 处理从子activity返回后主界面数据处理
     *
     * @param requestCode 请求跳转的activity标记编号
     * @param resultCode
     * @param data        intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (data != null) {
                String name = data.getStringExtra("name");
                user = (User) data.getSerializableExtra("USER");
                System.out.println(name);
                mTextview.setText(name);
                FLAG = 1;
                try {
                    sendRequest("2429050789");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Log.i("ResponseToStiring_1",str_request_cloudmusic);

                root = JsonToBean(str_request_cloudmusic);
                music_internet_list = root.getBody().getSongs();
                i1 = new InternetFragment(music_internet_list, MainActivity.this);

                mFragmentList.add(i1);
                viewPager.getAdapter().notifyDataSetChanged();

            }
        } else {
            FLAG = 0;

        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Drawable drawable = null;
        switch (v.getId()) {
            case R.id.txt_1:
                mToolbar_title.setText(R.string.local);
                select_img(v);
                viewPager.setCurrentItem(0, true);
                break;
            case R.id.txt_2:
                mToolbar_title.setText(R.string.internet);
                if (FLAG == 1) {
                    select_img(v);
                    viewPager.setCurrentItem(1, true);
                } else {
                    Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.txt_3:
                mToolbar_title.setText(R.string.favourite);
                select_img(v);
                viewPager.setCurrentItem(2, true);
            case R.id.search:
                if (FLAG == 1) {
                  /*  Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra("Internet_data", (Serializable) music_internet_list);*/
                  Intent intent =new Intent(MainActivity.this,NewRecentSearchActivity.class);

                    startActivity(intent);
                } else {

                    Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
                }
                break;


        }
        transaction.commit();
    }
    /**
     * @param json_str json 字符串
     */
    public Root JsonToBean(String json_str) {
        Gson gson = new Gson();
        Root root = gson.fromJson(json_str, Root.class);
        //Log.d("body","test"+body.getName());
        return root;
    }

    public void sendRequest(final String id) throws InterruptedException {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient.Builder().build();
                    String json = "{\"TransCode\":\"020111\",\"OpenId\":\"TEST\",\"Body\":{\"SongListId\":" + id + "}}";
                    RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), json);
                    Request request = new Request.Builder()

                            .url("https://api.hibai.cn/api/index/index")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    str_request_cloudmusic = response.body().string();


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        };
        thread.start();
        thread.join();
        //join方法作用是父线程等待子线程任务处理完毕
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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

    public void select_img(View v) {
        if (v != null) {
            if (v.getId() == R.id.txt_1) {
                Drawable drawable = getDrawable(R.drawable.ic_loacl_select);
                if (drawable != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tab1.setCompoundDrawables(null, drawable, null, null);
                    tab1.setTextColor(getResources().getColor(R.color.tab_select));
                }
                drawable = getDrawable(R.drawable.ic_internet);
                if (drawable != null && tab2 != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tab2.setCompoundDrawables(null, drawable, null, null);
                    tab2.setTextColor(getResources().getColor(R.color.tab_default));

                }
                drawable = getDrawable(R.drawable.user_default);
                if (drawable != null && tab3 != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tab3.setCompoundDrawables(null, drawable, null, null);
                    tab3.setTextColor(getResources().getColor(R.color.tab_default));
                }

            }else if(v.getId() == R.id.txt_2){
                Drawable drawable = getDrawable(R.drawable.ic_internet_select);
                if (drawable != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tab2.setCompoundDrawables(null, drawable, null, null);
                    tab2.setTextColor(getResources().getColor(R.color.tab_select));
                }
                drawable = getDrawable(R.drawable.ic_loacl_dafault);
                if (drawable != null && tab2 != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tab1.setCompoundDrawables(null, drawable, null, null);
                    tab1.setTextColor(getResources().getColor(R.color.tab_default));

                }
                drawable = getDrawable(R.drawable.user_default);
                if (drawable != null && tab3 != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tab3.setCompoundDrawables(null, drawable, null, null);
                    tab3.setTextColor(getResources().getColor(R.color.tab_default));
                }
            }else {
                Drawable drawable = getDrawable(R.drawable.user_select);
                if (drawable != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tab3.setCompoundDrawables(null, drawable, null, null);
                    tab3.setTextColor(getResources().getColor(R.color.tab_select));
                }
                drawable = getDrawable(R.drawable.ic_loacl_dafault);
                if (drawable != null && tab2 != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tab2.setCompoundDrawables(null, drawable, null, null);
                    tab2.setTextColor(getResources().getColor(R.color.tab_default));

                }
                drawable = getDrawable(R.drawable.ic_loacl_dafault);
                if (drawable != null && tab1 != null) {
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    tab1.setCompoundDrawables(null, drawable, null, null);
                    tab1.setTextColor(getResources().getColor(R.color.tab_default));
                }
            }

        }

    }
}
