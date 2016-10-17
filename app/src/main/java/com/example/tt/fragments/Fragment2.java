package com.example.tt.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tt.fragments.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * Created by Joe on 2016-06-09
 * Email: lovejjfg@gmail.com
 */
public class Fragment2 extends BaseFragment implements View.OnClickListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private FragmentManager manager;

    public Fragment2() {
    }


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Fragment2 newInstance(int sectionNumber) {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Bind(R.id.tab1)
    TextView tv1;
    @Bind(R.id.tab2)
    TextView tv2;
    @Bind(R.id.tab3)
    TextView tv3;
    private Fragment f4;
    private Fragment f5;
    private Fragment f6;
    private static final String T4 = "t4";
    private static final String T5 = "t5";
    private static final String T6 = "t6";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_2, container, false);
        ButterKnife.bind(this, rootView);
        manager = getChildFragmentManager();
        if (savedInstanceState == null) {
            f4 = new Fragment4();
            f5 = new Fragment5();
            f6 = new Fragment7();

            manager.beginTransaction()
                    .add(R.id.child_container, f4, T4)
                    .add(R.id.child_container, f5, T5)
                    .hide(f5)
                    .add(R.id.child_container, f6, T6)
                    .hide(f6)
                    .commit();
        } else {
            f4 = manager.findFragmentByTag(T4);
            f5 = manager.findFragmentByTag(T5);
            f6 = manager.findFragmentByTag(T6);
        }


        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab1:
                manager.beginTransaction().show(f4).hide(f5).hide(f6).commit();
                break;
            case R.id.tab2:
                manager.beginTransaction().show(f5).hide(f4).hide(f6).commit();
                break;
            case R.id.tab3:
                manager.beginTransaction().show(f6).hide(f4).hide(f5).commit();
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG, "onSaveInstanceState: 需要保存东西！!!!!!!!!!!!");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.e(TAG, "onSaveInstanceState: 需要恢复东西！!!!!!");
    }
}
