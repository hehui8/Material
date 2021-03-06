package com.example.he.material.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.he.material.MODLE.Song;
import com.example.he.material.R;

import java.util.ArrayList;
import java.util.List;

/**
 * project: Material
 * author : Android研发部_姓名
 * date : 2019/1/9
 * time : 17:08
 * email : 企业邮箱
 * note : 说明
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultHolder> {
    private Context mContext;
    private SearchRecentAdapter.OnItemClickListener mOnItemClickListener;
    private ArrayList<Song> mData;


    public SearchResultAdapter(ArrayList<Song> searchresult, SearchRecentAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
        this.mData = searchresult;
    }

    class SearchResultHolder extends RecyclerView.ViewHolder {

        TextView textViewId;
        TextView textViewTitle;

        SearchResultHolder(View itemView) {
            super(itemView);
            textViewId = itemView.findViewById(R.id.result_id);
            textViewTitle = itemView.findViewById(R.id.result_title);
        }
    }


    @NonNull
    @Override
    public SearchResultHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_result_item, parent, false);
        Log.d("seach", "adapter oncreatedviewholder");
        return new SearchResultHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultHolder holder, final int position) {
        if (mData != null && !mData.isEmpty()) {
            String songname = mData.get(position).getSongName();
            if (songname != null) {
                holder.textViewTitle.setText(songname);
                int temp =position;
                temp++;
                holder.textViewId.setText(String.valueOf(temp));
            }
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickListener.onClick(position);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 10;
    }

    public interface OnItemClickListener {
        void onClick(int position);

        // 长按事件
        void onLongClick(int position);
    }
}
