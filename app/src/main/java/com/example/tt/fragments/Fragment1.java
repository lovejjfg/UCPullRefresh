package com.example.tt.fragments;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tt.fragments.base.BaseFragment;
import com.example.tt.fragments.pagetransformer.AccordionTransformer;
import com.example.tt.fragments.pagetransformer.ScaleInTransformer;
import com.example.tt.fragments.pagetransformer.ScalePageTransformer;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * Created by Joe on 2016-06-09
 * Email: lovejjfg@gmail.com
 */
public class Fragment1 extends BaseFragment implements View.OnClickListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ViewPager.PageTransformer pageTransformer;

    public Fragment1() {
    }


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Fragment1 newInstance(int sectionNumber) {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Bind(R.id.view_pager)
    ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_1, container, false);
        ButterKnife.bind(this, rootView);
        mViewPager.setAdapter(new ImagePagerAdapter());
        pageTransformer = new ScaleInTransformer();
        mViewPager.setPageMargin(5);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageTransformer(true, pageTransformer);

        if (savedInstanceState == null) {

//            mViewPager.post(new Runnable() {
//                @Override
//                public void run() {
//                    mViewPager.setCurrentItem(50, true);
//                }
//            });
//            mViewPager.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mViewPager.setVisibility(View.VISIBLE);
//                }
//            }, 200);
        } else {
//            mViewPager.setVisibility(View.VISIBLE);
        }

        return rootView;
    }


    @Override
    public void onClick(View v) {
        Log.e(TAG, "onClick: " + v.getId());
    }

}
