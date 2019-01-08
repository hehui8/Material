package com.example.he.material;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;


public class ItemRemoveRecyclerview extends RecyclerView {

    public ItemRemoveRecyclerview(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        int x=(int) e.getRawX();
        int y= (int) e.getRawY();


        return super.onTouchEvent(e);


    }
}
