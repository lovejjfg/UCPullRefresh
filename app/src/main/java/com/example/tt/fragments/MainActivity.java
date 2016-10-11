package com.example.tt.fragments;

import android.app.SearchManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.example.tt.fragments.pagetransformer.ScalePageTransformer;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

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
    private Fragment1 f1;
    private Fragment2 f2;
    private Fragment3 f3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        manager = getSupportFragmentManager();
        f1 = new Fragment1();
        f2 = new Fragment2();
        f3 = new Fragment3();
        manager.beginTransaction()
                .add(R.id.fragment_container, f1)
                .addToBackStack("TAG1")
                .add(R.id.fragment_container, f2)
                .addToBackStack("TAG2")
                .hide(f2)
                .add(R.id.fragment_container, f3)
                .addToBackStack("TAG3")
                .hide(f3)
                .commit();
        //加入回退栈
//        manager.beginTransaction().add(R.id.fragment_container, f1)
//                .addToBackStack("TAG1").commit();
//        manager.beginTransaction().add(R.id.fragment_container, f2)
//                .addToBackStack("TAG2").commit();
//        manager.beginTransaction().add(R.id.fragment_container, f3)
//                .addToBackStack("TAG3").commit();

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
        super.onSaveInstanceState(outState);

        Log.e(TAG, "onSaveInstanceState: 需要保存东西！!!!!");
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
