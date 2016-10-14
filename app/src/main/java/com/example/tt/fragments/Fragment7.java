package com.example.tt.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tt.fragments.base.BaseFragment;

import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * Created by Joe on 2016-06-09
 * Email: lovejjfg@gmail.com
 */
public class Fragment7 extends BaseFragment implements View.OnClickListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public Fragment7() {
    }


    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Fragment7 newInstance(int sectionNumber) {
        Fragment7 fragment = new Fragment7();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_7, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        Log.e(TAG, "onClick: " + v.getId());
    }

}
