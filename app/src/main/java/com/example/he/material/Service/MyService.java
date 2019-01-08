package com.example.he.material.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.example.he.material.Activity.MusicActivity;
import com.example.he.material.MODLE.GEDAN.Songs;
import com.example.he.material.MODLE.Music;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MyService extends Service {

    private static int click_position_second = -1;
    public MyBinder mMyBinder = new MyBinder();
    private static MediaPlayer mPlayer;
    private Timer timer;
    private String path_service;
    private List<Music> Local_Music_list;
    private List<Songs> Internet_Music_list;
    private static int positon_click_local;//当前lcoal点击的选项标号
    private static int positon_click_internet;//当前internet点击的选项标号
    private boolean isplay;
    private int State =0;

    @Override
    public void onCreate() {
        super.onCreate();
        //initplay();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("intent ",intent.getStringExtra("from"));
        //获取从MusicActivity传送过来的数据，同样需作区分
        if(intent.getStringExtra("from").equals("Local")){
            State= 0;
            Local_Music_list = (List<Music>) intent.getSerializableExtra("music");
            positon_click_local = intent.getIntExtra("position", -1);
            //click_position_second ===当前播放位置
            if ((click_position_second == -1) || (click_position_second != positon_click_local)) {
                click_position_second = positon_click_local;
                Log.i("MUSIC", "选中 " + positon_click_local + "  cps " + click_position_second);
                initplay(Local_Music_list.get(click_position_second).getPath());
            } else if (click_position_second == positon_click_local) {
                Log.i("MUSIC", "tongyishou");
            }
        }
        else if(intent.getStringExtra("from").equals("Internet")){
            State=1;
            Internet_Music_list=(List<Songs>) intent.getSerializableExtra("song");
            positon_click_internet = intent.getIntExtra("position", -1);

            if ((click_position_second == -1) || (click_position_second != positon_click_internet)) {
                click_position_second = positon_click_internet;
                Log.i("MUSIC", "选中 " + positon_click_internet + "  cps " + click_position_second);
                initplay(Internet_Music_list.get(click_position_second).getUrl());

            } else if (click_position_second == positon_click_internet) {
                Log.i("MUSIC", "tongyishou");
            }

        }
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
               next();
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.pause();
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mMyBinder;
    }

    /*
     *
     *  声明一个内部类binder,b
     *
     */
     public class MyBinder extends Binder {
        public MyService getService() {
            // 返回当前对象MyService,这样我们就可在客户端端调用Service的公共方法
            return MyService.this;
        }
    }

   public static class RecycleViewBrodcast extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("guangbo","接收到广播");

            if(mPlayer!=null && mPlayer.isPlaying()){
                mPlayer.pause();
                click_position_second=-1;
            }
        }
    }
/*
* 初始化播放
*
* */
    public   void initplay(String url) {
        try {
            if (mPlayer == null) {
                mPlayer = new MediaPlayer();
            }
            //重置
            mPlayer.reset();
            //加载多媒体文件
            mPlayer.setDataSource(url);
            //准备播放音乐
            mPlayer.prepare();
            //播放音乐
            mPlayer.start();
            //添加计时器
            addTimer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //计时器
    public void addTimer() {

        //创建计时器对象

        timer = new Timer();
        Log.i("MUSIC", "NEW TIMER");
        timer.schedule(new TimerTask() {
            //执行计时任务
            @Override
            public void run() {
                //获得歌曲总时长
                int duration;
                int currentPosition;
                if (mPlayer.isPlaying()) {
                    duration = mPlayer.getDuration();
                    //获得歌曲的当前播放进度
                    currentPosition = mPlayer.getCurrentPosition();
                    //创建消息对象
                    Message msg = MusicActivity.handler.obtainMessage();

                    //将音乐的播放进度封装至消息对象中
                    Bundle bundle = new Bundle();
                    bundle.putInt("duration", duration);
                    bundle.putInt("currentPosition", currentPosition);
                    msg.setData(bundle);
                    //将消息发送到主线程的消息队列
                    MusicActivity.handler.sendMessage(msg);
                }
            }
        }, 0, 500);
        //开始计时任务后的5毫秒，第一次执行run方法，以后每500毫秒执行一次

    }

    public void play() {
        if (mPlayer.isPlaying()) {
            //mPlayer.start();
        } else {
            mPlayer.start();
        }

    }

    public void pause() {
        mPlayer.pause();
    }

    public void setProgress(int progress) {
        mPlayer.seekTo(progress);
    }


    public void next() {
        if(State ==0){
            positon_click_local++;
            Log.i("MUSIC", "NEXT" + positon_click_local);
        }
        else {
            positon_click_internet++;
            Log.i("MUSIC", "NEXT" + positon_click_internet);
        }
        timer.cancel();
        try {
            //重置
            mPlayer.reset();
            //加载多媒体文件
            if(State==0) {
                if (positon_click_local < Local_Music_list.size()) {
                    mPlayer.setDataSource(Local_Music_list.get(positon_click_local).getPath());
                    Log.i("MUSIC", "NEXT" + positon_click_local);
                    //准备播放音乐
                    mPlayer.prepare();
                    //播放音乐
                    mPlayer.start();
                    addTimer();
                } else {
                    Log.i("MUSIC", "zuihou");
                }
            }
            else {
                if(positon_click_internet<Internet_Music_list.size()){
                    mPlayer.setDataSource(Internet_Music_list.get(positon_click_internet).getUrl());
                    mPlayer.prepare();
                    //播放音乐
                    mPlayer.start();
                    addTimer();
                }
                else{}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void back() {
        if(State ==0){
            positon_click_local--;
            Log.i("MUSIC", "NEXT" + positon_click_local);
        }
        else {
            positon_click_internet--;
            Log.i("MUSIC", "NEXT" + positon_click_internet);
        }
        timer.cancel();
        mPlayer.reset();
        try {

            //重置
            //加载多媒体文件
            if(State==0) {
                if (positon_click_local >= 0) {
                    mPlayer.setDataSource(Local_Music_list.get(positon_click_local).getPath());
                    Log.i("MUSIC", "back" + positon_click_local);
                    //准备播放音乐
                    mPlayer.prepare();
                    //播放音乐
                    mPlayer.start();
                    addTimer();
                } else {
                    Log.i("MUSIC", "最前");
                }
            }
            else {
                if(positon_click_internet>0){
                    mPlayer.setDataSource(Internet_Music_list.get(positon_click_internet).getUrl());
                    mPlayer.prepare();
                    //播放音乐
                    mPlayer.start();
                    addTimer();
                }
                else{}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }




}
