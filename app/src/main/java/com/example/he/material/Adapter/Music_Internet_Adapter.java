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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.he.material.MODLE.GEDAN.Songs;
import com.example.he.material.R;

import java.util.List;

public class Music_Internet_Adapter extends RecyclerView.Adapter<Music_Internet_Adapter.ViewHolderInternet>{

        private Context mContext;
        private List<Songs> mMusicList;
        private OnItemClickListener mOnItemClickListener;


    //viewholder创建
    class ViewHolderInternet extends RecyclerView.ViewHolder{
        ImageView mMusicImage;
        TextView mMusicName;
        public ViewHolderInternet(View view) {
            super(view);
            mMusicImage =(ImageView)view.findViewById(R.id.address_picture_1);
            mMusicName =(TextView)view.findViewById(R.id.address_name_1);


        }
    }

    //构造适配器时需要传入一个musiclist和 一个监听接口
    public Music_Internet_Adapter(List<Songs> musicList, Music_Internet_Adapter.OnItemClickListener mOnItemClickListener) {
        this.mMusicList = musicList;
        this.mOnItemClickListener=mOnItemClickListener;
    }


        @Override
        public ViewHolderInternet onCreateViewHolder(ViewGroup parent, int viewType) {
            if (mContext==null){
                mContext=parent.getContext();
            }
            View view= LayoutInflater.from(mContext).inflate(R.layout.song_item,parent,false);
            ViewHolderInternet viewHolderInternet =new ViewHolderInternet(view);

            return viewHolderInternet;
        }

        @Override
        public void onBindViewHolder(ViewHolderInternet holder, final int position) {
            Songs songs = mMusicList.get(position);
            System.out.print("运行到这里");
            Log.i("onbindview"," " +songs.getTitle());
            holder.mMusicName.setText("  "+songs.getTitle());

            Glide.with(mContext)

                    .load(songs.getPic())

                    .into(holder.mMusicImage);
            //设置图片
            //Log.i("huoqu",music.getImageId()+"imageid");
            //Bitmap albumBitmapItem = Utils.getArtAlbum(mContext,music.getImageId());
            //holder.mMusicImage.setImageBitmap(albumBitmapItem);


            if( mOnItemClickListener!= null) {
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
    /*
    * 返回点击的item的位置
    *
    * */
        @Override
        public int getItemCount() {
            return mMusicList.size();
        }

        //声明一个ITEM点击事件接口
        public interface OnItemClickListener{
            void onClick(int position);
            // 长按事件
            void onLongClick(int position);
        }


    }
