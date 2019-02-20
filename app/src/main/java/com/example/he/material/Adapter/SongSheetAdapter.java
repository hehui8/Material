package com.example.he.material.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.he.material.MODLE.GEDAN.songs;
import com.example.he.material.R;

import java.util.List;

/**
 * project: Material
 * author : Android研发部_姓名
 * date : 2019/2/20
 * time : 15:55
 * email : 企业邮箱
 * note : 说明
 */
public class SongSheetAdapter extends RecyclerView.Adapter<SongSheetAdapter.ViewHolder> {


    private Context mContext;
    private List<songs> mMusicList;
    private OnItemClickListener mOnItemClickListener;

    //viewholder创建
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mMusicImage;
        TextView mMusicName;

        public ViewHolder(View view) {
            super(view);
            mMusicImage = (ImageView) view.findViewById(R.id.address_picture);
            mMusicName = (TextView) view.findViewById(R.id.address_name);


        }
    }

    //构造方法
    //构造适配器时需要传入一个musiclist和 一个子项点击监听接口
    public SongSheetAdapter(List<songs> musicList, OnItemClickListener mOnItemClickListener) {
        this.mMusicList = musicList;
        this.mOnItemClickListener = mOnItemClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.music_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
       songs songs= mMusicList.get(position);
        holder.mMusicName.setText("  " + songs.getName());

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
    }

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

