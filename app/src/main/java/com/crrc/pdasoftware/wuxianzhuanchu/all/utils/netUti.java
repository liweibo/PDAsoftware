package com.crrc.pdasoftware.wuxianzhuanchu.all.utils;


import android.content.Context;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Message;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class netUti {
    private Context context;
    private Handler mHandler;

    private long lastTotalRxBytes = 0;
    private long lastTimeStamp = 0;

    public netUti(Context context, Handler mHandler) {
        this.context = context;
        this.mHandler = mHandler;
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            showNetSpeed();
        }
    };

    public void startShowNetSpeed() {
        lastTotalRxBytes = getTotalRxBytes();
        lastTimeStamp = System.currentTimeMillis();
        new Timer().schedule(task, 1000, 1500); // 1s后启动任务，每2s执行一次

    }

    private long getTotalRxBytes() {
        return TrafficStats.getUidRxBytes(context.getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ? 0 : (TrafficStats.getTotalRxBytes() / 1024);//转为KB
    }

    private void showNetSpeed() {
        long nowTotalRxBytes = getTotalRxBytes();
        long nowTimeStamp = System.currentTimeMillis();
        long speed = ((nowTotalRxBytes - lastTotalRxBytes) * 1000 / (nowTimeStamp - lastTimeStamp));//毫秒转换

        lastTimeStamp = nowTimeStamp;
        lastTotalRxBytes = nowTotalRxBytes;

        Message msg = mHandler.obtainMessage();
        msg.what = 10000;
        if (speed > 1024) {
            DecimalFormat showFloatFormat = new DecimalFormat("0.0");
            String sp = showFloatFormat.format(speed / 1024) + " M/s";
            msg.obj = sp;
        } else {
            msg.obj = speed + " K/s";
        }
        mHandler.sendMessage(msg);//更新界面
    }
}
