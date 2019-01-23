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
import com.example.he.material.MODLE.Song;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class
MyService extends Service {

    private static int clickPositionSecond = -1;
    public MyBinder mMyBinder = new MyBinder();
    private static MediaPlayer mPlayer;
    private Timer timer;
    private String path_service;
    private List<Music> localMusicList;
    private List<Song> internetMusicList;
    private static int positonClickLocal;//当前lcoal点击的选项标号
    private static int positonClickInternet;//当前internet点击的选项标号
    private boolean isplay;
    private int State = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        //initplay();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String type = intent.getStringExtra("from");
        if (type != null) {
            //获取从MusicActivity传送过来的数据，同样需作区分
            if (type.equals("Local")) {
                State = 0;
                localMusicList = (List<Music>) intent.getSerializableExtra("music");
                positonClickLocal = intent.getIntExtra("position", -1);
                //clickPositionSecond ===当前播放位置
                if ((clickPositionSecond == -1) || (clickPositionSecond != positonClickLocal)) {
                    clickPositionSecond = positonClickLocal;
                    initplay(localMusicList.get(clickPositionSecond).getPath());
                }
            } else if (type.equals("Internet")) {
                State = 1;
                internetMusicList = (List<Song>) intent.getSerializableExtra("song");
                positonClickInternet = intent.getIntExtra("position", -1);

                if ((clickPositionSecond == -1) || (clickPositionSecond != positonClickInternet)) {
                    clickPositionSecond = positonClickInternet;
                    initplay(internetMusicList.get(clickPositionSecond).getPath());
                }
            } else if (type.equals("urlPath")) {
                State = 2;
                String urlPath = intent.getStringExtra("urlpath");
                initplay(urlPath);
            }
            if (mPlayer != null) {
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (State == 0 || State == 1) {
                            next();
                        } else {
                            if (localMusicList!=null && !localMusicList.isEmpty()) {
                                initByPosition1(0);
                            }else{
                                pause();
                            }
                        }
                    }
                });
            }
        }
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
            // 返回当前对象MyService,这样我们就可在activity调用Service的公共方法
            return MyService.this;
        }
    }

    public static class RecycleViewBrodcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("guangbo", "接收到广播");

            if (mPlayer != null && mPlayer.isPlaying()) {
                mPlayer.pause();
                clickPositionSecond = -1;
            }
        }
    }

    /*
     * 初始化播放
     *
     * */
    public void initplay(String url) {
        try {
            if (mPlayer == null) {
                mPlayer = new MediaPlayer();
            }
            //重置
            mPlayer.reset();
            //加载多媒体文件
            mPlayer.setDataSource(url);
            //准备播放音乐
            mPlayer.prepareAsync();
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    //音乐prepare后触发监听，播放
                    mPlayer.start();
                    //添加计时器
                    addTimer();
                }
            });
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
        if (!mPlayer.isPlaying()) {
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
        if (State == 0) {
            positonClickLocal++;
            Log.i("MUSIC", "NEXT" + positonClickLocal);
        } else {
            positonClickInternet++;
            Log.i("MUSIC", "NEXT" + positonClickInternet);
        }
        timer.cancel();
        try {
            //重置
            mPlayer.reset();
            //加载多媒体文件
            if (State == 0) {
                if (positonClickLocal < localMusicList.size()) {
                    mPlayer.setDataSource(localMusicList.get(positonClickLocal).getPath());
                    Log.i("MUSIC", "NEXT" + positonClickLocal);
                    //准备播放音乐
                    mPlayer.prepare();
                    //播放音乐
                    mPlayer.start();
                    addTimer();
                }
            } else if (State == 1) {
                if (positonClickInternet < internetMusicList.size()) {
                    mPlayer.setDataSource(internetMusicList.get(positonClickInternet).getPath());
                    mPlayer.prepare();
                    //播放音乐
                    mPlayer.start();
                    addTimer();
                } else {
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void back() {
        if (State == 0) {
            positonClickLocal--;
        } else {
            positonClickInternet--;
        }
        timer.cancel();
        mPlayer.reset();

        if (State == 0) {
            if (positonClickLocal >= 0) {
                initByPosition1(positonClickLocal);
            }
        } else {
            if (positonClickInternet > 0) {
                initByPosition2(positonClickInternet);
            }
        }
    }

    public void initByPosition1(int position) {
        try {
            mPlayer.reset();
            mPlayer.setDataSource(localMusicList.get(position).getPath());
            mPlayer.prepareAsync();
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mPlayer.start();
                    addTimer();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initByPosition2(int position) {
        try {
            mPlayer.reset();
            mPlayer.setDataSource(internetMusicList.get(position).getPath());
            mPlayer.prepareAsync();
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mPlayer.start();
                    addTimer();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
