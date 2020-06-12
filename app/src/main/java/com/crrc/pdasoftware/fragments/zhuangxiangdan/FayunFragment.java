package com.crrc.pdasoftware.fragments.zhuangxiangdan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.crrc.pdasoftware.MyApplication;
import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.activity.all.zhuangxiangdan.ZhuangXDanLiuchengActivity;
import com.crrc.pdasoftware.utils.ClearEditText;
import com.crrc.pdasoftware.utils.ScanUtils;
import com.crrc.pdasoftware.utils.XToastUtils;
import com.crrc.pdasoftware.utils.zhuangxiangdandata.BaozhuangRecyItemDataProvider;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xui.widget.picker.widget.configure.TimePickerType;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectChangeListener;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectListener;
import com.xuexiang.xutil.data.DateUtils;

import java.util.Calendar;
import java.util.Date;

public class FayunFragment extends Fragment {
    LinearLayout ll_fayun_yundanbianhao;//运单编号
    LinearLayout ll_fayun_time;//发货日期
    LinearLayout ll_fayun_yidongtype;//移动类型
    TextView fayun_time_tv;//发运日期
    TextView fayun_yidongtype_tv;//移动类型
    private TimePickerView mTimePickerDialog;
    ClearEditText fayun_edittext_yundanbianhao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fayun, container, false);
        findview(view);
        click();
        return view;
    }


    public void findview(View view) {
        ll_fayun_yundanbianhao = view.findViewById(R.id.ll_fayun_yundanbianhao);
        ll_fayun_time = view.findViewById(R.id.ll_fayun_time);
        ll_fayun_yidongtype = view.findViewById(R.id.ll_fayun_yidongtype);
        fayun_time_tv = view.findViewById(R.id.fayun_time_tv);
        fayun_yidongtype_tv = view.findViewById(R.id.fayun_yidongtype_tv);
        fayun_edittext_yundanbianhao = view.findViewById(R.id.fayun_edittext_yundanbianhao);
    }

    public void click() {
        //扫描
        ll_fayun_yundanbianhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanUtils.springScan(getActivity());
            }
        });

        //发货日期
        ll_fayun_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog();
            }
        });
        //移动类型
        ll_fayun_yidongtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContextMenuDialog();
            }
        });


    }


    private void showTimeDialog() {
        if (mTimePickerDialog == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.string2Date("2013-07-08 12:32:46", DateUtils.yyyyMMddHHmmss.get()));
            mTimePickerDialog = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
                @Override
                public void onTimeSelected(Date date, View v) {
                    XToastUtils.toast(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get()));
                    fayun_time_tv.setText(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get()));

                }
            })
                    .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                        @Override
                        public void onTimeSelectChanged(Date date) {
                        }
                    })
                    .setType(TimePickerType.ALL)
                    .setTitleText("时间选择")
                    .isDialog(true)
                    .setOutSideCancelable(false)
                    .setDate(calendar)
                    .build();
        }
        mTimePickerDialog.show();
    }


    //下拉选项为一个类别时  是否改派转办
    private void showContextMenuDialog() {
        new MaterialDialog.Builder(getActivity())
                .title("请选择")
                .items(R.array.yidongtye_values)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        fayun_yidongtype_tv.setText(text);
                    }
                })
                .show();
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("fayun--onpause调用"
        );
        unRegisterReceiver(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("fayun--onresume调用"
        );
        registerReceiver(getActivity());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            unRegisterReceiver(getActivity());
            System.out.println("----发运------隐藏");
        } else {
            registerReceiver(getActivity());
            System.out.println("-----发运-----出现");

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

                if (fayun_edittext_yundanbianhao != null) {
                    fayun_edittext_yundanbianhao.setText(svalue1);

                }
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
