package com.example.he.material.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.he.material.R;

import java.util.List;

/**
 * project: Material
 * author : Android研发部_姓名
 * date : 2019/1/8
 * time : 17:06
 * email : 企业邮箱
 * note : 说明
 */
public class SearchRecentAdapter extends RecyclerView.Adapter<SearchRecentAdapter.SearchRecentHolder> {



    private Context mContext;
    private List<String> mData;
    private SearchRecentAdapter.OnItemClickListener mOnItemClickListener;

    public SearchRecentAdapter(List<String> recentData, SearchRecentAdapter.OnItemClickListener listener) {
        this.mOnItemClickListener=listener;
        this.mData =recentData;
    }

    class  SearchRecentHolder extends  RecyclerView.ViewHolder{

        TextView textView;
        public SearchRecentHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.recent_data_text);
        }
    }


    @NonNull
    @Override
    public SearchRecentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext==null){
            mContext=parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_recent_item,parent,false);

        return new SearchRecentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRecentHolder holder, final int position) {
        if(mData!=null && mData.size()>0){
            String data =mData.get(position);
            if( holder!=null){
                if(data!=null){
                    holder.textView.setText(data);
                }
                if(mOnItemClickListener!=null){
                   holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mOnItemClickListener.onClick(position);
                        }
                    });
                }
            }
        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    public interface OnItemClickListener{
        void onClick(int position);
        // 长按事件
        void onLongClick(int position);
    }
}
