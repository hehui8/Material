package com.example.he.material.Activity;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.he.material.Fragment_List.LoveFragment;
import com.example.he.material.MODLE.Song;
import com.example.he.material.R;

import java.util.List;

/**
 * project: Material
 * author : Android研发部_姓名
 * date : 2019/2/18
 * time : 19:23
 * email : 企业邮箱
 * note : 说明
 */
public class SongSheetActivity extends Activity {

    private LoveFragment loveFragment;
    private List<Song> mSheetList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_sheet);
        loveFragment = LoveFragment.newInstance(mSheetList);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager. beginTransaction();
        if(transaction!=null){
            if(loveFragment!=null){
                transaction.add(R.id.layout_container,loveFragment);
                transaction.commit();
            }
        }


    }
}
