package com.example.he.material.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.he.material.MODLE.Song;
import com.example.he.material.R;
import com.example.he.material.UI.RoundImageView;

import java.util.List;

/**
 * project: Material
 * author : Android研发部_姓名
 * date : 2019/2/12
 * time : 17:14
 * email : 企业邮箱
 * note : 说明
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainHolder> {

    private List<Song> mMusicList;
    private Context mContext;

    public MainAdapter(List<Song> mMusicList, Context context) {
        super();
        this.mMusicList = mMusicList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext=parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.main_list_item,parent,false);

        return new  MainHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder holder, int position) {
        if(mMusicList!=null && !mMusicList.isEmpty()){
            Song song =mMusicList.get(position);
            if(song!=null && !song.getSongName().isEmpty()){
                holder.mTextView.setText(song.getSongName());
            }
        }
    }

    @Override
    public int getItemCount() {
        if(mMusicList!=null && !mMusicList.isEmpty()){
            return mMusicList.size();
        }
        return 0;
    }

    class MainHolder extends RecyclerView.ViewHolder {
        RoundImageView mRoundImg;
        TextView mTextView ;

        public MainHolder(View itemView) {
            super(itemView);
            mRoundImg = itemView.findViewById(R.id.main_image);
            mTextView=itemView.findViewById(R.id.main_text);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);

        // 长按事件
        void onLongClick(int position);
    }
}
