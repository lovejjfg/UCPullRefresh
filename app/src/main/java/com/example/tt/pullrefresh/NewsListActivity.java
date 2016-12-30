package com.example.tt.pullrefresh;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;

import com.example.tt.pullrefresh.R;

public class NewsListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        Log.e("TAG", "onCreate: ");
    }
}
