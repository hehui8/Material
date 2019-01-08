package com.example.he.material.Adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import java.util.List;
//viewpageAdapter
public class MyFragmentAdapter extends FragmentStatePagerAdapter {
    List<Fragment> mList;
    public  Fragment currentFragment;
    public MyFragmentAdapter(FragmentManager fm,List<Fragment>list) {
        super(fm);
        this.mList=list;
    }

    public MyFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    //返回要展示的fragment
    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }
    //返回fragment的数量
    @Override
    public int getCount() {
        return mList.size();
    }

    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        this.currentFragment= (Fragment) object;
        super.setPrimaryItem(container, position, object);
    }
/*
* viewpager中彻底性动态添加、删除Fragment
为了解决彻底删除fragment，我们要做的是：
1.将FragmentPagerAdapter 替换成FragmentStatePagerAdapter，因为前者只要加载过，fragment中的视图就一直在内存中，在这个过程中无论你怎么刷新，清除都是无用的，直至程序退出； 后者 可以满足我们的需求。
2.我们可以重写Adapter的方法--getItemPosition()，让其返回PagerAdapter.POSITION_NONE即可；

　　　　 @Override
	public int getItemPosition(Object object) {
		return PagerAdapter.POSITION_NONE;
	}
 到这一步我们就可以真正的实现随意、彻底删除viewpager中的fragment；[随意添加完全OK]
*
* */
    @Override
    public int getItemPosition(@NonNull Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}
