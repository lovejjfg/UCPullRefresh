package com.example.tt.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FragmentManager manager;
    @Bind(R.id.tab1)
    TextView tv1;
    @Bind(R.id.tab2)
    TextView tv2;
    @Bind(R.id.tab3)
    TextView tv3;
    //    @Bind(R.id.view_pager)
//    ViewPager mViewPager;
    private Fragment f1;
    private Fragment f2;
    private Fragment f3;
    private static final String T1 = "T1";
    private static final String T2 = "T2";
    private static final String T3 = "T3";
    private static final String CURRENT_TAG = "CURRENT_TAG";
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        manager = getSupportFragmentManager();
        Log.e(TAG, "onSaveInstanceState: 当前没有相关状态！！");
        if (savedInstanceState == null) {
            f1 = new Fragment1();
            f2 = new Fragment2();
            f3 = new Fragment3();
            manager.beginTransaction()
                    .add(R.id.fragment_container, f1, T1)
//                .addToBackStack("TAG1")
                    .add(R.id.fragment_container, f2, T2)
//                .addToBackStack("TAG2")
                    .hide(f2)
                    .add(R.id.fragment_container, f3, T3)
//                .addToBackStack("TAG3")
                    .hide(f3)
                    .commit();
        } else {
            f1 = getSupportFragmentManager().findFragmentByTag(T1);
            f2 = getSupportFragmentManager().findFragmentByTag(T2);
            f3 = getSupportFragmentManager().findFragmentByTag(T3);
        }

//        }
        //加入回退栈
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab1:
                manager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .show(f1)
                        .hide(f3)
                        .hide(f2)
                        .commit();
                break;
            case R.id.tab2:
                manager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
//                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .show(f2)
                        .hide(f1)
                        .hide(f3)
                        .commit();
                break;
            case R.id.tab3:
                manager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
//                        .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        .show(f3)
                        .hide(f1)
                        .hide(f2)
                        .commit();
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.e(TAG, "onSaveInstanceState: 保存当前TAG");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        savedInstanceState.clear();

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        //如果加入了回退栈，那么在返回的时候会先去退栈
        super.onBackPressed();
    }

}
