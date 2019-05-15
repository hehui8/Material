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
import android.widget.Toast;

import com.example.he.material.Activity.MusicActivity;
import com.example.he.material.MODLE.Song;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class
MyService extends Service {
    //0为列表循环，1为单曲，2为随机
    public static int TYPE_PLAY_MODE = 0;

    private static MediaPlayer mPlayer;
    private static int currentClick = -1;
    public MyBinder mMyBinder = new MyBinder();
    private Timer timer;
    private List<Song> SongList;
    private static int ClickPosition;//当前lcoal点击的选项标号
    private int State = 0;
    private boolean isPlay = true;

    private MusicActivity.MyHandler myHandler;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (SongList != null && SongList.size() > 0) {
            SongList.clear();
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
                mPlayer.reset();
            }
        }
        myHandler = MusicActivity.getHandler();
        SongList = (List<Song>) intent.getSerializableExtra("music");
        ClickPosition = intent.getIntExtra("position", -1);
        //currentClick ===当前播放位置
        if (SongList != null && !SongList.isEmpty()) {
                initplay(SongList.get(ClickPosition).getPath());
        }
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (SongList.size() < ClickPosition + 1) {
                    next();
                } else {
                    Message msg = myHandler.obtainMessage();
                    msg.what = 3;
                    myHandler.sendMessage(msg);
                }
                Message msg = myHandler.obtainMessage();
                msg.what = 2;
                Bundle bundle = new Bundle();
                bundle.putInt("click", ClickPosition);
                msg.setData(bundle);
                myHandler.sendMessage(msg);
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
                mPlayer.stop();
                mPlayer.release();
                mPlayer = null;
                currentClick = -1;
            }
        }

    }

    /*
     * 初始化播放
     *
     * */
    public void initplay(String url) {
        isPlay =false;
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
                    isPlay = true;
                }
            });
            mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    Toast.makeText(getBaseContext(),"网络出错了，请稍后再试",Toast.LENGTH_SHORT).show();
                    Message msg = myHandler.obtainMessage();
                    msg.what = 4;
                    myHandler.sendMessage(msg);
                    return  false;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            isPlay = true;

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
                if (mPlayer != null) {
                    if (mPlayer.isPlaying()) {
                        duration = mPlayer.getDuration();
                        //获得歌曲的当前播放进度
                        currentPosition = mPlayer.getCurrentPosition();
                        //创建消息对象
                        Message msg = myHandler.obtainMessage();
                        msg.what = 0;
                        //将音乐的播放进度封装至消息对象中
                        Bundle bundle = new Bundle();
                        bundle.putInt("duration", duration);
                        bundle.putInt("currentPosition", currentPosition);
                        msg.setData(bundle);
                        //将消息发送到主线程的消息队列
                        myHandler.sendMessage(msg);
                    }
                }
            }
        }, 0, 500);
        //开始计时任务后的5毫秒，第一次执行run方法，以后每500毫秒执行一次
    }

    public void play() {
        isPlay = true;
        if (!mPlayer.isPlaying()) {
            mPlayer.start();
        }
    }

    public void pause() {
        mPlayer.pause();
        isPlay = false;
    }

    public void setProgress(int progress) {
        mPlayer.seekTo(progress);
    }


    public void next() {
        isPlay = true;
        String titleName = null;
        if (SongList.size() == 1) {
        } else if (SongList.size() > 1) {
            ClickPosition++;
        }
        if(timer !=null) {
            timer.cancel();
            try {
                //重置
                mPlayer.reset();
                //加载多媒体文件

                if (ClickPosition < SongList.size()) {
                    if (TYPE_PLAY_MODE == 0) {
                        mPlayer.setDataSource(SongList.get(ClickPosition).getPath());
                        titleName = SongList.get(ClickPosition).getSongName();
                    } else if (TYPE_PLAY_MODE == 1) {
                        ClickPosition--;
                        mPlayer.setDataSource(SongList.get(ClickPosition).getPath());
                        titleName = SongList.get(ClickPosition).getSongName();
                    } else if (TYPE_PLAY_MODE == 2) {
                        int index = (int) (Math.random() * SongList.size());
                        mPlayer.setDataSource(SongList.get(index).getPath());
                        titleName = SongList.get(index).getSongName();
                        if (index < SongList.size()) {
                            ClickPosition = index;
                        }
                    }
                    //准备播放音乐
                    try {
                        mPlayer.prepareAsync();
                        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mPlayer.start();
                                addTimer();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //播放音乐
                    Message msg = myHandler.obtainMessage();
                    msg.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putString("titlename", titleName);
                    msg.setData(bundle);
                    myHandler.sendMessage(msg);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void back() {
        isPlay = true;
        if (SongList.size() == 1) {

        } else if (SongList.size() > 1) {
            ClickPosition--;
        }
        timer.cancel();
        mPlayer.reset();
        if (ClickPosition >= 0) {
            initByPosition(ClickPosition);
        }
    }

    public void initByPosition(int position) {
        try {

            mPlayer.reset();
            if (TYPE_PLAY_MODE == 1) {
                position++;
                mPlayer.setDataSource(SongList.get(position).getPath());
            } else if (TYPE_PLAY_MODE == 0) {
                mPlayer.setDataSource(SongList.get(position).getPath());
            } else if (TYPE_PLAY_MODE == 2) {
                int index = (int) (Math.random() * SongList.size());
                mPlayer.setDataSource(SongList.get(index).getPath());
            }
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

    public void setPlayMode(int mode) {
        TYPE_PLAY_MODE = mode;
    }

    public void updateList(List<Song> updatelist) {
        if (updatelist != null && !updatelist.isEmpty()) {
            if (SongList != null && !SongList.isEmpty()) {
                SongList.clear();
                SongList.addAll(updatelist);
            }
        }
    }

    public MediaPlayer getmPlayer() {
        if (mPlayer != null) {
            return mPlayer;
        }
        return null;
    }

    public int getCurrentPlay() {
        return currentClick;
    }

}
