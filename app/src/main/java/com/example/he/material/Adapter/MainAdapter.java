package com.example.he.material.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.he.material.MODLE.Data;
import com.example.he.material.R;

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

    private Context mContext;
    private List<Data> mSheetList;
    private MainAdapter.OnItemClickListener mOnItemClickListener;

    public MainAdapter(List<Data> mSheetList, Context context, OnItemClickListener l) {
        super();
        this.mSheetList = mSheetList;
        this.mContext = context;
        this.mOnItemClickListener = l;
    }

    @NonNull
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.main_list_item, parent, false);

        return new MainHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder holder, final int position) {
        if (mSheetList != null) {
            holder.mTextView.setText(mSheetList.get(position).getTitle());
            holder.mRoundImg.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onClick(position);
                }
            });

            RoundedCorners roundedCorners = new RoundedCorners(6);
            //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
            RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
            Glide.with(mContext)
                    .load(mSheetList.get(position).getCoverImgUrl())
                    .apply(options)
                    .into(holder.mRoundImg);
        }
    }

    @Override
    public int getItemCount() {
        if (mSheetList != null) {
            return mSheetList.size();
        }
        return 0;
    }

    class MainHolder extends RecyclerView.ViewHolder {
        ImageView mRoundImg;
        TextView mTextView;

        public MainHolder(View itemView) {
            super(itemView);
            mRoundImg = itemView.findViewById(R.id.main_image);
            mTextView = itemView.findViewById(R.id.main_text);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);

        // 长按事件
        void onLongClick(int position);
    }
}
