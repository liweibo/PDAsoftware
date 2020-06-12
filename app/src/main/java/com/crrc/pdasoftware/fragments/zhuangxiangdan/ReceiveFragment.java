package com.crrc.pdasoftware.fragments.zhuangxiangdan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.crrc.pdasoftware.MyApplication;
import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.activity.all.zhuangxiangdan.ZhuangXDanLiuchengActivity;
import com.crrc.pdasoftware.utils.XToastUtils;


public class ReceiveFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receive, container, false);
    }






    @Override
    public void onPause() {
        super.onPause();
        unRegisterReceiver(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(getActivity());
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            unRegisterReceiver(getActivity());
            System.out.println("----装箱------隐藏");
        } else {
            registerReceiver(getActivity());
            System.out.println("------装箱----出现");

        }
    }




    //注册广播，是为了监听扫描结果
    public void registerReceiver(Context mContext) {
        IntentFilter intFilter = new IntentFilter("nlscan.action.SCANNER_RESULT");
        mContext.registerReceiver(mResultReceiver, intFilter);
    }

    public void unRegisterReceiver(Context mContext) {
        try {
            mContext.unregisterReceiver(mResultReceiver);
        } catch (Exception e) {
        }
    }

    //监听扫描结果的广播
    private BroadcastReceiver mResultReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("nlscan.action.SCANNER_RESULT".equals(action)) {
                String svalue1 = intent.getStringExtra("SCAN_BARCODE1");
                String svalue2 = intent.getStringExtra("SCAN_BARCODE2");
                int barcodeType = intent.getIntExtra("SCAN_BARCODE_TYPE", -1); // -1:unknown

                MyApplication.setScanStringtv1(svalue1);
                System.out.println("=====" + MyApplication.getScanStringtv1());
                svalue1 = svalue1 == null ? "" : svalue1;
                svalue2 = svalue2 == null ? "" : svalue2;

                XToastUtils.success("11" + svalue1);
//                if (publicTvScanValue != null) {
//                    publicTvScanValue.setText(svalue1);
//
//                }



                System.out.println("扫描的值1：" + svalue1);
                System.out.println("扫描的值2：" + svalue2);
                System.out.println("扫描de code：" + barcodeType);
                int adapPos = ((ZhuangXDanLiuchengActivity) getActivity()).getApps().getadapterPos();

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
