package com.example.tt.fragments;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
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
//                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                view.setLayoutParams(lp);
//                view.setText(position + ":" + view);
        view.setScaleType(ImageView.ScaleType.FIT_XY);
//                view.setBackgroundColor(Color.parseColor("#44ff0000"));

        view.setImageResource(imgRes[position % imgRes.length]);
        container.addView(view);
//                view.setAdjustViewBounds(true);
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
