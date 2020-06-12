package com.crrc.pdasoftware.fragments.zhuangxiangdan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.crrc.pdasoftware.MyApplication;
import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.activity.all.zhuangxiangdan.ZhuangXDanLiuchengActivity;
import com.crrc.pdasoftware.adapter.zhuangxiangdan.JianpeiWuliaoAdapter;
import com.crrc.pdasoftware.utils.ClearEditText;
import com.crrc.pdasoftware.utils.XToastUtils;
import com.crrc.pdasoftware.utils.zhuangxiangdandata.JianpeiRecyItemDataInfo;
import com.crrc.pdasoftware.utils.zhuangxiangdandata.JianpeiRecyItemDataProvider;
import com.xuexiang.xui.adapter.recyclerview.XLinearLayoutManager;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class JianpeiFragment extends Fragment {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int position =0;

    private TimePickerView mTimePickerDialogxuqiudaoda;
    private TimePickerView mTimePickerDialogtelephone;

    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    LinearLayout ll_jianpei_item_wuliaocode;
    Button jianpei_complete;

    boolean canClickbtns = false;

    SwipeRecyclerView recyclerView;
    JianpeiWuliaoAdapter mAdapter;
    ClearEditText wuliaocode;
    ClearEditText wuliaopicihao;
    ClearEditText wuliaoxuliehao;
    ClearEditText publicclearedit;
    ClearEditText publicTvScanValue;
    View v;
    List<JianpeiRecyItemDataInfo> listnet = new ArrayList<>();

    public JianpeiFragment() {
    }

    //物料扫码：
//    1.行记录中，有多个物料（按批次号）；2.行记录中，一行对应一个物料（按序列号）
//    1 中情况扫一次批次号，再手动输入总物料数；2中情况需每个都扫描。
//    不管是1还是2，只要是一个类别的物料扫描完成了，就点击完成，提交扫描数据。
//    提交后，再扫描其他的物料类别，按1 2中方式进行
//
//
    //扫描新方案：只管一直扫描，不用知道是扫描哪个，
    // 但是扫描过程中，要与所有的行记录（list数组）
// 进行对比，是否属于list中（即仓位匹配，......）

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        pref = getActivity().getSharedPreferences("guzhanggddata", MODE_PRIVATE);
        editor = pref.edit();
        v = inflater.inflate(R.layout.fragment_jianpei,
                container, false);
        initViews(v);
        return v;
    }

    public void initViews(View v) {
        TextView tvToolbarTitle = getActivity().findViewById(R.id.tv_zhuangxd_liucheng_toolbbar_title);
        tvToolbarTitle.setText("装箱单-拣配");


        fragmentManager = getActivity().getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        jianpei_complete = v.findViewById(R.id.jianpei_complete);

        ll_jianpei_item_wuliaocode = v.findViewById(R.id.ll_jianpei_item_wuliaocode);

        wuliaocode = v.findViewById(R.id.jianpei_item_wuliaocode);
        wuliaopicihao = v.findViewById(R.id.jianpei_item_picihao);
        wuliaoxuliehao = v.findViewById(R.id.jianpei_item_xuliehao);
        publicclearedit = wuliaocode;

        //模拟网络请求数据
        listnet.clear();
        JianpeiRecyItemDataProvider.clearJianpeiListNewInfos();
        JianpeiRecyItemDataProvider.setJianpeiListNewInfos(netRequest());

        recyclerView = v.findViewById(R.id.recyclerView_jianpei_wuliao);
        recyclerView.setLayoutManager(new XLinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter = new JianpeiWuliaoAdapter(getActivity()));

        //展示数据
        mAdapter.refresh(JianpeiRecyItemDataProvider.getJianpeiListNewInfos());

        //点击拣配完成
        jianpei_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ZhuangXDanLiuchengActivity) getActivity()).getApps().setJianpeilist(listnet);
                for (JianpeiRecyItemDataInfo in : listnet
                ) {
                    System.out.println("=====:" + in.getwuliaocode());
                }

                ((ZhuangXDanLiuchengActivity) getActivity()).gotoBaozhuang();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onPause() {
        super.onPause();
        System.out.println("jiapei--onpause调用"
        );
        unRegisterReceiver(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("jiapei--onresume调用"
        );
        registerReceiver(getActivity());

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            unRegisterReceiver(getActivity());
            System.out.println("----拣配------隐藏");
        } else {
            registerReceiver(getActivity());
            System.out.println("------拣配----出现");

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

//                ((ClearEditText) v.findViewById(R.id.jianpei_item_wuliaocode)).setText(svalue1);
                System.out.println("扫描的值1：" + svalue1);
                System.out.println("扫描的值2：" + svalue2);
                System.out.println("扫描de code：" + barcodeType);

                int adapPos = ((ZhuangXDanLiuchengActivity) getActivity()).getApps().getadapterPos();


                final String scanStatus = intent.getStringExtra("SCAN_STATE");
                if ("ok".equals(scanStatus)) {
                    //扫描到的值 赋值到list中；
                    if (adapPos < listnet.size()) {
                        XToastUtils.success("" + adapPos);
                        if (adapPos == -1) {
                            listnet.get(position).setwuliaocode("8888");
                            listnet.get(position).setpicihao("9999");
                            listnet.get(position).setXuliehao("00000");

                            position ++;

                        } else {
                            listnet.get(adapPos).setwuliaocode("8888");
                            listnet.get(adapPos).setpicihao("9999");
                            listnet.get(adapPos).setXuliehao("00000");
                        }

                    }
                    ((ZhuangXDanLiuchengActivity) getActivity()).getApps().setJianpeilist(listnet);


                    //刷新adapter  展示最新数据
                    mAdapter.refresh(listnet);
                    //成功
                    XToastUtils.success("扫描成功");
                } else {
                    //失败如超时等
                    XToastUtils.error("扫描失败");
                }


            }
        }
    };


    //模拟网络请求
    public List<JianpeiRecyItemDataInfo> netRequest() {

        //模拟请求到的数据
        JianpeiRecyItemDataInfo info;
        for (int i = 0; i < 3; i++) {
            info = new JianpeiRecyItemDataInfo("变流器" + i,
                    "中心库" + i, "xx仓位" + i, "未拣配" + i,
                    "", "",
                    "", "1",
                    "0");
            listnet.add(info);
        }

        return listnet;

    }


}
