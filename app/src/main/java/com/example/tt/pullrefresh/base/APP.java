package com.example.tt.pullrefresh.base;

import android.app.Application;

import com.antfortune.freeline.FreelineCore;

/**
 * Created by Joe on 2016/10/14.
 * Email lovejjfg@gmail.com
 */

public class APP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FreelineCore.init(this);
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
//        // Normal app init code...
    }
}
