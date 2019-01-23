package com.example.he.material.Activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.he.material.MODLE.Music;
import com.example.he.material.MODLE.Song;
import com.example.he.material.R;
import com.example.he.material.Service.MyService;
import com.example.he.material.Controler.Utils;

import java.io.Serializable;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MusicActivity extends AppCompatActivity {

    private boolean isplaying = true;//播放标志
    public static String TAG = "MUSIC";
    private ImageView mBack;
    private ImageView mplay;
    private ImageView mnext;
    private static SeekBar mSeekBar;
    private static TextView tv_progress;
    private static TextView tv_total;
    private static TextView music_title;
    private CircleImageView mCircleImageView;
    private TextView mPricture;
    private static MyService mService;
    private Intent mIntent;
    private ServiceConnection mConn;
    private int mProgress;
    private int CurrentPosition;//当前播放位置
    private ObjectAnimator animator;
    private ViewPager mViewPager;
    //music对象属性

    private List<Music> LocalList;
    private List<Song> InternetList;
    private int clickItem;
    private Toolbar mToolbar;
    private int stateFrom;


    @SuppressLint("ResourceType")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musicplay);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        music_title = findViewById(R.id.music_title);
        mToolbar = findViewById(R.id.mytoolbar_music);
        mBack = findViewById(R.id.back);
        mplay = findViewById(R.id.play);
        mnext = findViewById(R.id.next);
        mSeekBar = findViewById(R.id.sb);
        tv_progress = findViewById(R.id.tv_progress);
        tv_total = findViewById(R.id.tv_total);
        mCircleImageView = findViewById(R.id.pricture_img);
        mPricture = findViewById(R.id.geci_text);


        //接受从fragment传送过来的bundle（点击的位置）和music集合
        Intent intent_data = getIntent();
        mIntent = new Intent(this, MyService.class);
        //区分从local 和 internet 发送来的Intent
        if (intent_data.getStringExtra("from").equals("Local")) {
            stateFrom = 0;
            Bundle mbundle = intent_data.getBundleExtra("data");
            clickItem = mbundle.getInt("itemId");//点击的位置编号
            CurrentPosition = clickItem;//当前播放位置等于点击位置
            LocalList = (List<Music>) getIntent().getSerializableExtra("music");
            mIntent.putExtra("from", "Local");
            mIntent.putExtra("position", clickItem);
            mIntent.putExtra("music", (Serializable) LocalList);
            startService(mIntent);
            music_title.setText(LocalList.get(CurrentPosition).getName());
            Glide.with(this).load(getResources().getDrawable(R.drawable.default_img)).into(mCircleImageView);
        } else if (intent_data.getStringExtra("from").equals("Internet")) {
            stateFrom = 1;
            Bundle mbundle = intent_data.getBundleExtra("data");
            clickItem = mbundle.getInt("itemId");//点击的位置编号
            CurrentPosition = clickItem;//当前播放位置等于点击位置
            InternetList = (List<Song>) getIntent().getSerializableExtra("song");
            //将数据传给service
            mIntent.putExtra("from", "Internet");
            mIntent.putExtra("position", clickItem);
            mIntent.putExtra("song", (Serializable) InternetList);
            startService(mIntent);
            music_title.setText(InternetList.get(CurrentPosition).getSongName());
            if (mCircleImageView != null) {
                Glide.with(this).load(R.drawable.default_img).into(mCircleImageView);
            }
        } else if (intent_data.getStringExtra("from").equals("result")) {
            stateFrom = 2;
            Bundle bundle = intent_data.getBundleExtra("clickResult");
            String url = bundle.getString("pathUrl");

            if (url != null && !url.isEmpty()) {
                music_title.setText(bundle.getString("Title"));
                mIntent.putExtra("from", "urlPath");
                mIntent.putExtra("urlpath",url);
                startService(mIntent);
            }
            //将数据传给service


        }

        //与服务器端交互的接口方法 绑定服务的时候被回调，在这个方法获取绑定Service传递过来的IBinder对象，
        //通过这个IBinder对象，实现activity和Service的交互。
        mConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MyService.MyBinder binder = (MyService.MyBinder) service;
                mService = binder.getService();
                //可以通过mService调用service中方法
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        //绑定服务
        bindService(mIntent, mConn, Service.BIND_AUTO_CREATE);
        //动画
        initanimator();

        //当从主activity切换至musicactivity时，歌曲自动开始播放，动画开始
        animator.start();
        mplay.setBackground(getResources().getDrawable(R.drawable.ic_stop));


        //为播放键设置点击监听事件
        mplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isplaying = !isplaying;
                if (isplaying) {
                    mService.play();
                    mplay.setBackground(getResources().getDrawable(R.drawable.pause));
                    if (animator.isPaused()) {
                        animator.resume();
                    } else {
                        animator.start();
                    }
                } else {
                    mService.pause();
                    mplay.setBackground(getResources().getDrawable(R.drawable.play));
                    animator.pause();
                }

            }
        });

        mCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* mCircleImageView.setVisibility(View.INVISIBLE);
                mPricture.setVisibility(View.VISIBLE);*/
            }
        });

        mPricture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPricture.setVisibility(View.INVISIBLE);
                mCircleImageView.setVisibility(View.VISIBLE);
            }
        });
        mnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stateFrom == 0) {
                    if (CurrentPosition < (LocalList.size() - 1)) {
                        CurrentPosition++;//如果选择下一首，那么当前播放位置加一
                        mCircleImageView.setImageBitmap(Utils.getArtAlbum(MusicActivity.this,
                                LocalList.get(CurrentPosition).getImageId()));
                        music_title.setText(LocalList.get(CurrentPosition).getName());
                        animator.start();
                        mService.next();
                    } else {
                        Toast.makeText(MusicActivity.this, "已经是最后一首了", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (CurrentPosition < (InternetList.size() - 1)) {
                        CurrentPosition++;//如果选择下一首，那么当前播放位置加一

                        Glide.with(MusicActivity.this)
                                .load(R.drawable.default_img)
                                .into(mCircleImageView);
                        music_title.setText(InternetList.get(CurrentPosition).getSongName());
                        animator.start();
                        mService.next();
                    } else {
                        Toast.makeText(MusicActivity.this, "已经是最后一首了", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stateFrom == 0) {
                    if (CurrentPosition < (LocalList.size() - 1)) {
                        CurrentPosition--;//如果选择上一首，那么当前播放位置加一
                        mCircleImageView.setImageBitmap(Utils.getArtAlbum(MusicActivity.this,
                                LocalList.get(CurrentPosition).getImageId()));
                        music_title.setText(LocalList.get(CurrentPosition).getName());
                        animator.start();
                        mService.back();
                    } else {
                        Toast.makeText(MusicActivity.this, "已经是第一首了", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (CurrentPosition < (InternetList.size() - 1)) {
                        CurrentPosition--;//如果选择上一首，那么当前播放位置减一
                        Glide.with(MusicActivity.this).load(R.drawable.default_img).into(mCircleImageView);
                        music_title.setText(InternetList.get(CurrentPosition).getSongName());
                        animator.start();
                        mService.back();
                    } else {
                        Toast.makeText(MusicActivity.this, "已经是第一首了", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mProgress = mSeekBar.getProgress();
                mService.setProgress(mProgress);
            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @SuppressLint("HandlerLeak")
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            //歌曲的总时长(毫秒)
            int duration = bundle.getInt("duration");
            //歌曲的当前进度(毫秒)
            int currentPostition = bundle.getInt("currentPosition");
            //刷新滑块的进度
            mSeekBar.setMax(duration);
            mSeekBar.setProgress(currentPostition);

            //歌曲的总时长
            int minute = duration / 1000 / 60;
            int second = duration / 1000 % 60;
            String strMinute = null;
            String strSecond = null;
            //如果歌曲的时间中的分钟小于10
            if (minute < 10) {
                //在分钟的前面加一个0
                strMinute = "0" + minute;
            } else {
                strMinute = minute + "";
            }
            //如果歌曲的时间中的秒钟小于10
            if (second < 10) {
                //在秒钟前面加一个0
                strSecond = "0" + second;
            } else {
                strSecond = second + "";
            }
            tv_total.setText(strMinute + ":" + strSecond);

            //歌曲当前播放时长
            minute = currentPostition / 1000 / 60;
            second = currentPostition / 1000 % 60;
            //如果歌曲的时间中的分钟小于10
            if (minute < 10) {

                //在分钟的前面加一个0
                strMinute = "0" + minute;
            } else {
                strMinute = minute + "";
            }
            //如果歌曲的时间中的秒钟小于10
            if (second < 10) {
                //在秒钟前面加一个0
                strSecond = "0" + second;
            } else {
                strSecond = second + "";
            }
            tv_progress.setText(strMinute + ":" + strSecond);

        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConn);
    }

    public void initanimator() {
        animator = ObjectAnimator.ofFloat(mCircleImageView, "rotation", 0f, 359f);
        animator.setDuration(10000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(Animation.INFINITE);

    }

}