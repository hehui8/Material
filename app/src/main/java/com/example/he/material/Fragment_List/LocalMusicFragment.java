package com.example.he.material.Fragment_List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.he.material.Activity.MusicActivity;
import com.example.he.material.Adapter.MusicAdapter;
import com.example.he.material.MODLE.Music;
import com.example.he.material.R;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@SuppressLint("ValidFragment")
public class LocalMusicFragment extends Fragment {
    private static List<Music> addressList;

    private RecyclerView recyclerView;
    private LinearLayout linearLayout;
    private Context context;
    public static Intent intent;
    private MusicAdapter adapter;

    private Handler mhandle = new Handler();
    private int REFRESH_UI = 100;

    public LocalMusicFragment(List<Music> addressList, Context context) {
        this.addressList = addressList;
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.local_fragment, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);

        //配置布局管理器

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        //item的滑动效果
        ItemTouchHelper.Callback mCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
            /**
             * @param recyclerView
             * @param viewHolder 拖动的ViewHolder
             * @param target 目标位置的ViewHolder
             * @return
             */
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
                int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
                if (fromPosition < toPosition) {
                    //分别把中间所有的item的位置重新交换
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(addressList, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(addressList, i, i - 1);
                    }
                }
                adapter.notifyItemMoved(fromPosition, toPosition);
                //返回true表示执行拖动
                return true;
            }

            @Override
            //左移或者右移之后实现的具体功能
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Log.i("itemtouch", position + "移除position   ");
                addressList.remove(position);

                adapter.notifyItemRemoved(position);
                adapter.notifyDataSetChanged();
                //adapter 发生变化，发送广播通知myservice
                Intent intent = new Intent();
                intent.setAction("Adapter.is.changer");
                Log.d("guangbo", "fasong");
                context.sendBroadcast(intent);


            }

        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
//      c重写item点击事件
        adapter = new MusicAdapter(addressList, new MusicAdapter.OnItemClickListener() {

            @Override
            public void onClick(int position) {
                Intent intent1 = new Intent(context, MusicActivity.class);
                Log.i("itemtouch", position + "移除后的点击位置");
                Music music = addressList.get(position);
                Bundle data = new Bundle();
                data.putInt("itemId", position);
                intent1.putExtra("from", "Local");
                intent1.putExtra("data", data);
                intent1.putExtra("music", (Serializable) addressList);
                startActivity(intent1);
            }
            @Override
            public void onLongClick(int position) {
            }
        });

        //设置适配器
        recyclerView.setAdapter(adapter);
        return view;

    }


}
