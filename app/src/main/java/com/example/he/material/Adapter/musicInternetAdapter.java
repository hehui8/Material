package com.example.he.material.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.he.material.MODLE.GEDAN.Songs;
import com.example.he.material.MODLE.Song;
import com.example.he.material.R;

import java.util.List;

public class musicInternetAdapter extends RecyclerView.Adapter<musicInternetAdapter.ViewHolderInternet> {

    private Context mContext;
    private List<Song> mMusicList;
    private OnItemClickListener mOnItemClickListener;


    //viewholder创建
    class ViewHolderInternet extends RecyclerView.ViewHolder {
        ImageView mMusicImage;
        TextView mMusicName;

        public ViewHolderInternet(View view) {
            super(view);
            mMusicImage = (ImageView) view.findViewById(R.id.address_picture_1);
            mMusicName = (TextView) view.findViewById(R.id.address_name_1);


        }
    }

    //构造适配器时需要传入一个musiclist和 一个监听接口
    public musicInternetAdapter(List<Song> musicList, musicInternetAdapter.OnItemClickListener mOnItemClickListener) {
        this.mMusicList = musicList;
        this.mOnItemClickListener = mOnItemClickListener;
    }


    @Override
    public ViewHolderInternet onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.song_item, parent, false);
        ViewHolderInternet viewHolderInternet = new ViewHolderInternet(view);

        return viewHolderInternet;
    }

    @Override
    public void onBindViewHolder(ViewHolderInternet holder, final int position) {
        if (mMusicList != null) {
            Song song = mMusicList.get(position);
            holder.mMusicName.setText("  " + song.getSongName());
            Glide.with(mContext).load(R.drawable.default_img).into(holder.mMusicImage);
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onClick(position);
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mOnItemClickListener.onLongClick(position);
                        return false;
                    }
                });
            }
        } else {


        }
    }

    /*
     * 返回点击的item的位置
     *
     * */

    @Override
    public int getItemCount() {
        return mMusicList.size();
    }

    //声明一个ITEM点击事件接口
    public interface OnItemClickListener {
        void onClick(int position);

        // 长按事件
        void onLongClick(int position);
    }


}
