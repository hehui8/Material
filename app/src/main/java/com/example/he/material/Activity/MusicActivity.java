package com.example.he.material.Activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.he.material.MODLE.Song;
import com.example.he.material.R;
import com.example.he.material.Service.MyService;
import com.example.he.material.Utils.AndroidWorkaround;
import com.example.he.material.Utils.SPUtils;
import com.google.gson.Gson;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class MusicActivity extends AppCompatActivity {


    private boolean isplaying = true;//播放标志
    public static String TAG = "MUSIC";
    private ImageView mBack;
    private static ImageView mplay;
    private ImageView mnext;
    private ImageView mLove;
    private static SeekBar mSeekBar;
    private static TextView tv_progress;
    private static TextView tv_total;
    private static TextView music_title;
    private static CircleImageView mCircleImageView;
    private TextView mPricture;
    private static MyService mService;
    private Intent mIntent;
    private ServiceConnection mConn;
    private int mProgress;
    private int CurrentPosition;//当前播放位置
    private static ObjectAnimator animator;
    private ViewPager mViewPager;
    //music对象属性
    private static List<Song> songListLastTime;
    private static List<Song> songList;
    private int clickItem;
    private Toolbar mToolbar;
    private int stateFrom;
    private ImageView mRandom;
    private static int mode = 0;
    private int lovestate = 0;
    private static View background;
    private MediaPlayer mediaPlayer;
    public static MyHandler myHandler;

    private static int playPosition;
    private static Context mContext;
    private SPUtils spUtils;


    @Override
    protected void onStart() {
        super.onStart();
    }

    @SuppressLint("ResourceType")
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.musicplay);

        if (AndroidWorkaround.checkDeviceHasNavigationBar(this)) {
            AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
        }

        mContext = getBaseContext();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        music_title = findViewById(R.id.music_title);
        background = findViewById(R.id.line1);
        mToolbar = findViewById(R.id.mytoolbar_music);
        mBack = findViewById(R.id.back);
        mplay = findViewById(R.id.play);
        mnext = findViewById(R.id.next);
        mLove = findViewById(R.id.love);
        mSeekBar = findViewById(R.id.sb);
        tv_progress = findViewById(R.id.tv_progress);
        tv_total = findViewById(R.id.tv_total);
        mCircleImageView = findViewById(R.id.pricture_img);
        mPricture = findViewById(R.id.geci_text);
        mRandom = findViewById(R.id.random);

        //默认列表循环

        myHandler = new MyHandler(this);

        if (savedInstanceState != null) {
            mode = savedInstanceState.getInt("MODE");
        }

        if (mode == 0) {
            mRandom.setBackground(getDrawable(R.drawable.ic_list_play));
        } else if (mode == 1) {
            mRandom.setBackground(getDrawable(R.drawable.ic_only_one));
        } else if (mode == 2) {
            mRandom.setBackground(getDrawable(R.drawable.ic_random));
        }

        mRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode++;
                mService.setPlayMode(mode);
                if (mode % 3 == 0) {
                    mode = 0;
                }
                if (mode == 0) {
                    mRandom.setBackground(getDrawable(R.drawable.ic_list_play));
                } else if (mode == 1) {
                    mRandom.setBackground(getDrawable(R.drawable.ic_only_one));
                } else if (mode == 2) {
                    mRandom.setBackground(getDrawable(R.drawable.ic_random));
                }

            }
        });

        Intent intent = getIntent();
        songList = new ArrayList<>();
        mIntent = new Intent(this, MyService.class);
        //接受从fragment传送过来的bundle（点击的位置）和music集合
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
        if (intent.getBundleExtra("data") != null) {
            Bundle mbundle = intent.getBundleExtra("data");
            clickItem = mbundle.getInt("itemId");//点击的位置编号
            CurrentPosition = clickItem;//当前播放位置等于点击位置
            songList.clear();
            songList = (List<Song>) mbundle.getSerializable("music");
            //传给service
            mIntent.putExtra("position", clickItem);
            mIntent.putExtra("music", (Serializable) songList);
//            mIntent.putExtra("handler", (Serializable) myHandler);
            startService(mIntent);
            if (music_title != null) {
                music_title.setText(songList.get(CurrentPosition).getSongName());
            }
        }
            if (mCircleImageView != null) {
            if (songList != null && songList.size() > 0) {
                if (songList.get(CurrentPosition).getPicpath() != null) {
                    Glide.with(this)
                            .load(songList.get(CurrentPosition).getPicpath())
                            .apply(new RequestOptions().error(R.drawable.default_img))
                            .into(mCircleImageView);
                    SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                            background.setBackground(resource);
                        }
                    };
                    Glide.with(this)
                            .load(songList.get(CurrentPosition).getPicpath())
                            .apply(new RequestOptions().error(R.drawable.default_img))
                            .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3)))
                            .into(simpleTarget);
                }
            }
        }


        //动画
        initanimator();
        //当从主activity切换至musicactivity时，歌曲自动开始播放，动画开始
        animator.start();

        mplay.setBackground(getResources().getDrawable(R.drawable.ic_pause));
        //为播放键设置点击监听事件
        mplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isplaying = !isplaying;
                if (isplaying) {
                    mService.play();
                    mplay.setBackground(getResources().getDrawable(R.drawable.ic_pause));
                    if (animator.isPaused()) {
                        animator.resume();
                    } else {
                        animator.start();
                    }
                } else {
                    mService.pause();
                    mplay.setBackground(getResources().getDrawable(R.drawable.ic_play));
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
                mplay.setBackgroundResource(R.drawable.ic_pause);
                if (CurrentPosition < (songList.size() - 1)) {
                    CurrentPosition++;//如果选择下一首，那么当前播放位置加一
                    music_title.setText(songList.get(CurrentPosition).getSongName());
                    animator.start();
                    mService.next();
                    if (mCircleImageView != null) {
                        if (songList != null && songList.size() > 0) {
                            if (songList.get(CurrentPosition).getPicpath() != null) {
                                Glide.with(MusicActivity.this)
                                        .load(songList.get(CurrentPosition).getPicpath())
                                        .apply(new RequestOptions().placeholder(R.drawable.default_img).error(R.drawable.default_img).skipMemoryCache(true))
                                        .into(mCircleImageView);
                                SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                        background.setBackground(resource);
                                    }
                                };
                                Glide.with(MusicActivity.this)
                                        .load(songList.get(CurrentPosition).getPicpath())
                                        .apply(new RequestOptions().error(R.drawable.default_img).skipMemoryCache(true))
                                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3)))
                                        .into(simpleTarget);
                            }
                        }
                    }

                } else {
                    Toast.makeText(MusicActivity.this, "已经是最后一首了", Toast.LENGTH_SHORT).show();
                }

            }
        });
        mBack.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                mplay.setBackgroundResource(R.drawable.ic_pause);
                if (CurrentPosition <= (songList.size() - 1) && CurrentPosition > 0) {
                    CurrentPosition--;//如果选择上一首，那么当前播放位置减一
                    music_title.setText(songList.get(CurrentPosition).getSongName());
                    animator.start();
                    mService.back();
                    if (mCircleImageView != null) {
                        if (songList != null && songList.size() > 0) {
                            if (songList.get(CurrentPosition).getPicpath() != null) {
                                Glide.with(MusicActivity.this)
                                        .load(songList.get(CurrentPosition).getPicpath())
                                        .apply(new RequestOptions().placeholder(R.drawable.default_img).error(R.drawable.default_img).skipMemoryCache(true))
                                        .into(mCircleImageView);
                                SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                        background.setBackground(resource);
                                    }
                                };
                                Glide.with(MusicActivity.this)
                                        .load(songList.get(CurrentPosition).getPicpath())
                                        .apply(new RequestOptions().error(R.drawable.default_img).skipMemoryCache(true))
                                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3)))
                                        .into(simpleTarget);
                            }
                        }
                    }
                } else {
                    Toast.makeText(MusicActivity.this, "已经是第一首了", Toast.LENGTH_SHORT).show();
                }

            }
        });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()

        {
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
        mToolbar.setNavigationOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final SPUtils spUtils =new SPUtils(MusicActivity.this);
        boolean isLove=spUtils.getSingleLoveSong(String.valueOf(songList.get(CurrentPosition).getId()));
        if(isLove){
            lovestate=1;
            mLove.setBackgroundResource(R.drawable.ic_love_red);
        }else{
            lovestate=0;
            mLove.setBackgroundResource(R.drawable.ic_love_gray);
        }
        mLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson =new Gson();
                String strMusic= gson.toJson(songList.get(playPosition));
                if (lovestate == 0) {
                    mLove.setBackgroundResource(R.drawable.ic_love_red);
                    if(playPosition>=0){
                        if(!TextUtils.isEmpty(strMusic)){
                            spUtils.addLoveSongSP(songList.get(playPosition).getId()+"",strMusic);
                        }
                    }
                    lovestate = 1;
                } else if (lovestate == 1) {
                    mLove.setBackground(null);
                    mLove.setBackgroundResource(R.drawable.ic_love_gray);
                    spUtils.removeLoveListSP(songList.get(playPosition).getId()+"");
                    lovestate = 0;
                }
            }
        });
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mode = savedInstanceState.getInt("MODE");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mode == 0) {
            mRandom.setBackground(getDrawable(R.drawable.ic_list_play));
        } else if (mode == 1) {
            mRandom.setBackground(getDrawable(R.drawable.ic_only_one));
        } else if (mode == 2) {
            mRandom.setBackground(getDrawable(R.drawable.ic_random));
        }

    }


    public static class MyHandler extends Handler {

        private final WeakReference<MusicActivity> mAct;

        public MyHandler(MusicActivity musicActivity) {
            mAct = new WeakReference<MusicActivity>(musicActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            MusicActivity mainAct = mAct.get();
            super.handleMessage(msg);
            if (mainAct != null) {
                switch (msg.what) {
                    case 0:
                        Bundle bundle = msg.getData();
                        //歌曲的总时长(毫秒)
                        int duration = bundle.getInt("duration");
                        //歌曲的当前进度(毫秒)
                        int currentPostition = bundle.getInt("currentPosition");
                        setDuration(duration, currentPostition);
                        break;
                    case 1:
                        Bundle bundle1 = msg.getData();
                        if (bundle1.getString("titlename") != null) {
                            music_title.setText(bundle1.getString("titlename"));
                        }
                        break;
                    case 2:
                        Bundle bundle2 = msg.getData();
                        if (bundle2.getInt("click") > -1) {
                            int position = bundle2.getInt("click");
                            playPosition=position;
                            if (mCircleImageView != null) {
                                if (songList != null && songList.size() > 0) {
                                    if (songList.get(position).getPicpath() != null) {
                                        Glide.with(mContext)
                                                .load(songList.get(position).getPicpath())
                                                .apply(new RequestOptions().placeholder(R.drawable.default_img).error(R.drawable.default_img).skipMemoryCache(true))
                                                .into(mCircleImageView);
                                        SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
                                            @Override
                                            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                                if (background != null) {
                                                    background.setBackground(resource);
                                                }
                                            }
                                        };
                                        Glide.with(mContext)
                                                .load(songList.get(position).getPicpath())
                                                .apply(new RequestOptions().error(R.drawable.default_img).skipMemoryCache(true))
                                                .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3)))
                                                .into(simpleTarget);
                                    }
                                }
                            }
                        }
                        break;
                    case 3:
                        setDuration(0, 0);
                        mSeekBar.setProgress(0);
                        mplay.setBackgroundResource(R.drawable.ic_play);
                        animator.cancel();
                        break;
                }
            }
        }

    }


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

    public TextView getTitleName() {
        return music_title;
    }


    public static void setDuration(int duration, int currentPosition) {
        //刷新滑块的进度
        mSeekBar.setMax(duration);
        mSeekBar.setProgress(currentPosition);

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
        minute = currentPosition / 1000 / 60;
        second = currentPosition / 1000 % 60;
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("MODE", mode);
    }

    public void next() {
        mplay.setBackgroundResource(R.drawable.ic_pause);
        if (CurrentPosition < (songList.size() - 1)) {
            CurrentPosition++;//如果选择下一首，那么当前播放位置加一
            music_title.setText(songList.get(CurrentPosition).getSongName());
            animator.start();
            mService.next();
        } else {
            mediaPlayer.pause();
            mediaPlayer.stop();
        }
    }


    public static MyHandler getHandler() {
        if (myHandler != null) {
            return myHandler;
        }
        return null;
    }
}
