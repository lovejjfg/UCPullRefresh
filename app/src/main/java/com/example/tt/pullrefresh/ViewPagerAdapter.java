package com.example.tt.pullrefresh;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


import java.util.ArrayList;

/**
 * Created by Joe on 2016/10/12.
 * Email lovejjfg@gmail.com
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    @Nullable
    private ArrayList<Fragment> fragments;
    private int position;
    private long baseId = 0;
    private final FragmentManager manager;
    private  String[] names;

    public ViewPagerAdapter(FragmentManager fm, @Nullable ArrayList<Fragment> fragments, String[] names) {
        super(fm);
        manager = fm;
        this.fragments = fragments;
        this.names = names;
    }

    public void setFragments(@Nullable ArrayList<Fragment> fragments) {
        this.fragments = fragments;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
//        int i = position % 2;
        return fragments.get(position);
//        return pullrefresh.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Fragment fragment = fragments.get(position);
        int anInt = fragment.getArguments().getInt(ListFragment.CURRENT_ID, 0);
        return names[anInt];
    }
    //每次在instantiateItem中调用这个的时候，都会是不同的id。适配器发现找不到之前的碎片，
    //就会重新调用getItem来新建碎片。
    //这个方法是适配器用来组装tag的一部分。只要改变了它，也就改变了tag。
//    @Override
//    public long getItemId(int position) {
//        // give an ID different from position when position has been changed
//        return baseId + position;
//    }

//    /**
//     * 删除之后，在调用notifyDataSetChanged之前，先调用这个notifyChangeInPosition(1)。
//     * 改变tag。
//     */
//    public void notifyChangeInPosition(int n) {
//        // shift the ID returned by getItemId outside the range of all previous pullrefresh
//        baseId += getCount() + n;
//    }

    @Override
    public int getItemPosition(Object object) {
        int i = fragments.indexOf(object);
        if (i == -1) {
            return POSITION_NONE;
        }
        return i;
    }

    public void updateItems(String[] itemses) {
        this.names = itemses;
    }
}
