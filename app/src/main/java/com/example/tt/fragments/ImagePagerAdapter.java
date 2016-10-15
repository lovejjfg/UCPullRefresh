package com.example.tt.fragments;

import android.support.v4.view.PagerAdapter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by Joe on 2016/10/11.
 * Email lovejjfg@gmail.com
 */

public class ImagePagerAdapter extends PagerAdapter {
    //        private final ArrayList<Fragment> list;
    private static final int[] imgRes = {
            R.mipmap.style_dny,
            R.mipmap.style_dzh,
            R.mipmap.style_jianyue,
            R.mipmap.style_meishi,
            R.mipmap.style_oushi,
            R.mipmap.style_rishi,
            R.mipmap.style_xiandai,
            R.mipmap.style_zhongshi
    };
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());

        view.setImageResource(imgRes[position % imgRes.length]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }


}
