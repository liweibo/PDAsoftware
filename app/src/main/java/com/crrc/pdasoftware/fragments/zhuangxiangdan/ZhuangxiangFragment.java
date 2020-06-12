package com.crrc.pdasoftware.fragments.zhuangxiangdan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.crrc.pdasoftware.MyApplication;
import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.activity.all.zhuangxiangdan.ZhuangXDanLiuchengActivity;
import com.crrc.pdasoftware.adapter.zhuangxiangdan.BaozhuangWuliaoAdapter;
import com.crrc.pdasoftware.utils.ClearEditText;
import com.crrc.pdasoftware.utils.TimeUtils;
import com.crrc.pdasoftware.utils.XToastUtils;
import com.crrc.pdasoftware.utils.zhuangxiangdandata.BaozhuangRecyItemDataInfo;
import com.crrc.pdasoftware.utils.zhuangxiangdandata.BaozhuangRecyItemDataProvider;
import com.crrc.pdasoftware.utils.zhuangxiangdandata.JianpeiRecyItemDataInfo;
import com.xuexiang.xui.adapter.recyclerview.XLinearLayoutManager;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;


public class ZhuangxiangFragment extends Fragment {
    private int position = 0;

    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    LinearLayout ll_baozhuang_item_wuliaocode;
    Button baozhuang_complete;
    SwipeRecyclerView recyclerView;
    BaozhuangWuliaoAdapter mAdapter;
    View v;
    private boolean freshtime = true;

    //物料扫码：
//    1.行记录中，有多个物料（按批次号）；2.行记录中，一行对应一个物料（按序列号）
//    1 中情况扫一次批次号，再手动输入总物料数；2中情况需每个都扫描。
//    不管是1还是2，只要是一个类别的物料扫描完成了，就点击完成，提交扫描数据。
//    提交后，再扫描其他的物料类别，按1 2中方式进行
//
//

    List<JianpeiRecyItemDataInfo> getRe = new ArrayList<>();
    List<BaozhuangRecyItemDataInfo> listnetbaozhaung = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getRe = ((ZhuangXDanLiuchengActivity) getActivity()).getApps().getJianpeilist();

        v = inflater.inflate(R.layout.fragment_zhuangxiang,
                container, false);
        initViews(v);
        return v;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //   进入当前Fragment
        if (enter && !freshtime) {
            freshtime = true;
            //   这里可以做需要的数据刷新操作
            BaozhuangRecyItemDataProvider.clearBaozhuangListNewInfos();
            getRe = ((ZhuangXDanLiuchengActivity) getActivity()).getApps().getJianpeilist();
            jianpeidataToBaozhuangdata(getRe);

            XToastUtils.success("refresh");
        } else {
            freshtime = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    public void initViews(View v) {


        TextView tvToolbarTitle = getActivity().findViewById(R.id.tv_zhuangxd_liucheng_toolbbar_title);
        tvToolbarTitle.setText("装箱单-装箱");


        fragmentManager = getActivity().getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        baozhuang_complete = v.findViewById(R.id.baozhuang_complete);


        recyclerView = v.findViewById(R.id.recyclerView_baozhuang_wuliao);
        recyclerView.setLayoutManager(new XLinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter = new BaozhuangWuliaoAdapter(getActivity()));

        mAdapter.refresh(BaozhuangRecyItemDataProvider.getBaozhuangListNewInfos());

        jianpeidataToBaozhuangdata(getRe);
        baozhuang_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ZhuangXDanLiuchengActivity) getActivity()).gotoFayun();

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
        unRegisterReceiver(getActivity());
        freshtime = false;
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

    public void jianpeidataToBaozhuangdata(List<JianpeiRecyItemDataInfo> listnetjianpeiinfo) {
        listnetbaozhaung.clear();//这一步不能少，做清除。
        for (JianpeiRecyItemDataInfo info : listnetjianpeiinfo
        ) {
            BaozhuangRecyItemDataInfo baoinfo =
                    new BaozhuangRecyItemDataInfo(info.getWuliaoname(),
                            info.getfachukufang(),
                            "本页面网络获取", info.getwuliaozhuangtai(),
                            info.getwuliaocode(), info.getpicihao(),
                            info.getXuliehao(), info.getDiaobonumber(),
                            "待扫描");
            listnetbaozhaung.add(baoinfo);
        }
        BaozhuangRecyItemDataProvider.clearBaozhuangListNewInfos();
        BaozhuangRecyItemDataProvider.setBaozhuangListNewInfos(listnetbaozhaung);
        mAdapter.refresh(BaozhuangRecyItemDataProvider.getBaozhuangListNewInfos());
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
                ((ClearEditText) v.findViewById(R.id.baozhuang_item_wuliaocode)).setText(svalue1);


                mAdapter.refresh(BaozhuangRecyItemDataProvider.getBaozhuangListNewInfos());

                System.out.println("扫描的值1：" + svalue1);
                System.out.println("扫描的值2：" + svalue2);
                System.out.println("扫描de code：" + barcodeType);
                int adapPos = ((ZhuangXDanLiuchengActivity) getActivity()).getApps().getadapterPos();

                final String scanStatus = intent.getStringExtra("SCAN_STATE");
                if ("ok".equals(scanStatus)) {
                    //扫描到的值 赋值到list中；
                    if (adapPos < listnetbaozhaung.size()) {
                        if (adapPos == -1) {
                            listnetbaozhaung.get(position).setbaozhuangxianghao("111111");
                            position++;

                        } else {
                            listnetbaozhaung.get(adapPos).setbaozhuangxianghao("111111");

                        }
                    }
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
