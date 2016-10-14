package com.example.tt.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tt.fragments.base.BaseFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Joe on 2016-06-09
 * Email: lovejjfg@gmail.com
 */
public class Fragment3 extends BaseFragment implements View.OnClickListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ViewPager.PageTransformer pageTransformer;
    private String[] names;
    private ArrayList<Fragment> fragments;
    private ViewPagerAdapter mAdapter;

    public Fragment3() {
    }


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Fragment3 newInstance(int sectionNumber) {
        Fragment3 fragment = new Fragment3();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Bind(R.id.tab)
    TabLayout mTab;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;



    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_3, container, false);
        rootView.findViewById(R.id.tv_add).setOnClickListener(this);
        rootView.findViewById(R.id.tv_delete).setOnClickListener(this);
        rootView.findViewById(R.id.tv_clear).setOnClickListener(this);
        ButterKnife.bind(this, rootView);

//        mViewPager.setPageMarginDrawable(R.mipmap.ic_launcher);
//        pageTransformer = new TranslatePagerTransformer();
        mViewPager.setPageTransformer(false, pageTransformer);
        names = getResources().getStringArray(R.array.names);
        fragments = initFragment();
        mAdapter = new ViewPagerAdapter(getChildFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(fragments.size());
        mTab.setupWithViewPager(mViewPager);

//        setUpIndicatorWidth();
        return rootView;
    }



    @NonNull
    private ArrayList<Fragment> initFragment() {
        ArrayList<Fragment> fragments = new ArrayList<>(8);
        fragments.add( ListFragment.newInstance(Constants.TYPE_NORMAL));
        fragments.add( ListFragment.newInstance(Constants.TYPE_BIG_IMG));
        fragments.add( ListFragment.newInstance(Constants.TYPE_NORMAL));
        fragments.add( ListFragment.newInstance(Constants.TYPE_BIG_IMG));
        fragments.add( ListFragment.newInstance(Constants.TYPE_NORMAL));
        fragments.add( ListFragment.newInstance(Constants.TYPE_BIG_IMG));
        fragments.add( ListFragment.newInstance(Constants.TYPE_NORMAL));
        fragments.add( ListFragment.newInstance(Constants.TYPE_BIG_IMG));
        return fragments;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_clear) {
            fragments.clear();
        } else if (i == R.id.tv_add) {
            ListFragment fragment1 =  ListFragment.newInstance(Constants.TYPE_NORMAL);
            fragment1.setUpdate(true);
            fragments.add(0,fragment1);
        } else if (i == R.id.tv_delete) {
            ListFragment remove = (ListFragment) fragments.remove(0);
            remove.setUpdate(true);
        }
        mAdapter.setFragments(fragments);
        mViewPager.setOffscreenPageLimit(fragments.size());
        mViewPager.setCurrentItem(mViewPager.getCurrentItem() == fragments.size() ? fragments.size() - 1 : mViewPager.getCurrentItem());
        mTab.setupWithViewPager(mViewPager);
    }

//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//    private void setUpIndicatorWidth()  {
//        try {
//            Class<?> tablayout = mTab.getClass();
//            Field tabStrip = tablayout.getDeclaredField("mTabStrip");
//            tabStrip.setAccessible(true);
//            LinearLayout ll_tab= (LinearLayout) tabStrip.get(mTab);
//            for (int i = 0; i < ll_tab.getChildCount(); i++) {
//                View child = ll_tab.getChildAt(i);
//                child.setPadding(0,0,0,0);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,1);
//                child.setLayoutParams(params);
//                child.invalidate();
//            }
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
//    }


}
