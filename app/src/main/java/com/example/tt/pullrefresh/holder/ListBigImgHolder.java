package com.example.tt.pullrefresh.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tt.pullrefresh.R;
import com.example.tt.pullrefresh.model.ModelBean;


/**
 * Created by Joe on 2016/10/10.
 * Email lovejjfg@gmail.com
 */

public class ListBigImgHolder extends RecyclerView.ViewHolder {

    private final TextView mTittle;
    private final TextView mContent;
    private final TextView mDate;
    private final ImageView mImg;

    public ListBigImgHolder(View itemView) {
        super(itemView);
        mTittle = (TextView) itemView.findViewById(R.id.tv_tittle);
        mContent = (TextView) itemView.findViewById(R.id.tv_content);
        mDate = (TextView) itemView.findViewById(R.id.tv_time);
        mImg = (ImageView) itemView.findViewById(R.id.image);
    }

    public void onBind(ModelBean position) {
        // TODO: 2016/10/10 具体资源加载 是否加载图片等逻辑
        mTittle.setText(position.getTittle());
    }
}
