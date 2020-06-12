package com.crrc.pdasoftware.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.TextView;

import com.crrc.pdasoftware.MyApplication;

public class ScanUtils {
    TextView tvScanvalue;

    public ScanUtils(TextView tvScanvalue){

    }
    //启动扫描
    public static void springScan(Context mContext) {
        Intent intent = new Intent("nlscan.action.SCANNER_TRIG");
        intent.putExtra("SCAN_TIMEOUT", 2);//单位为秒，值为 int 类型，且不超过 9 秒
        intent.putExtra("EXTRA_SCAN_MODE", 3);//广播方式输出
        mContext.sendBroadcast(intent);


    }


    public  void registerReceiver(Context mContext) {
        IntentFilter intFilter = new IntentFilter("nlscan.action.SCANNER_RESULT");
        mContext.registerReceiver(mResultReceiver, intFilter);
    }

    public  void unRegisterReceiver(Context mContext) {
        try {
            mContext.unregisterReceiver(mResultReceiver);
        } catch (Exception e) {
        }
    }

    //监听扫描结果的广播
    private  BroadcastReceiver mResultReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("nlscan.action.SCANNER_RESULT".equals(action)) {
                String svalue1 = intent.getStringExtra("SCAN_BARCODE1");
                String svalue2 = intent.getStringExtra("SCAN_BARCODE2");
                int barcodeType = intent.getIntExtra("SCAN_BARCODE_TYPE", -1); // -1:unknown

                MyApplication.setScanStringtv1(svalue1);
                System.out.println("====="+MyApplication.getScanStringtv1());
                svalue1 = svalue1 == null ? "" : svalue1;
                svalue2 = svalue2 == null ? "" : svalue2;

                System.out.println("扫描的值1：" + svalue1);
                System.out.println("扫描的值2：" + svalue2);
                System.out.println("扫描de code：" + barcodeType);

                final String scanStatus = intent.getStringExtra("SCAN_STATE");
                if ("ok".equals(scanStatus)) {
                    //成功
                    XToastUtils.success("扫描成功");
                } else {
                    //失败如超时等
                    XToastUtils.error("扫描失败");
                }


            }
        }
    };


}
