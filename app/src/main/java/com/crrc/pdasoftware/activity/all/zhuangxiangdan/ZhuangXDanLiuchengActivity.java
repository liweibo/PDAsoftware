package com.crrc.pdasoftware.activity.all.zhuangxiangdan;


import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.crrc.pdasoftware.MyApplication;
import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.fragments.dummy.DummyContent;
import com.crrc.pdasoftware.fragments.guzhangchuli.FuwuFragment;
import com.crrc.pdasoftware.fragments.zhuangxiangdan.FayunFragment;
import com.crrc.pdasoftware.fragments.zhuangxiangdan.JianpeiFragment;
import com.crrc.pdasoftware.fragments.zhuangxiangdan.ReceiveFragment;
import com.crrc.pdasoftware.fragments.zhuangxiangdan.ZhuangxiangFragment;
import com.crrc.pdasoftware.utils.ClearEditText;
import com.crrc.pdasoftware.utils.XToastUtils;
import com.crrc.pdasoftware.utils.ZhuangxiangdanLiucTabclick;
import com.crrc.pdasoftware.utils.zhuangxiangdandata.BaozhuangRecyItemDataInfo;
import com.crrc.pdasoftware.utils.zhuangxiangdandata.BaozhuangRecyItemDataProvider;
import com.crrc.pdasoftware.utils.zhuangxiangdandata.JianpeiRecyItemDataInfo;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.xuexiang.xui.widget.dialog.DialogLoader;

import java.util.ArrayList;
import java.util.List;

public class ZhuangXDanLiuchengActivity extends AppCompatActivity implements FuwuFragment.OnListFragmentInteractionListener {

    //多个fragment中的多个广播监听，全部集中用一个广播，并放在activity中，
    //actvivy中记录当前页面是哪个fragmnet，activity中监听到的结果，用一个全局变量保存，
    //fragment中取这一个全局变量。


    TextView tv_liucheng_jianpei;
    TextView tv_liucheng_zhuangxiang;
    TextView tv_liucheng_fayun;
    TextView tv_liucheng_receive;


    Toolbar toolbar_zhuangxd_liucheng_tianxie;
    ZhuangxiangdanLiucTabclick ZhuangxiangdanLiucTabclick;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    HorizontalScrollView hscrollv_zhuangxd_liucheng;
    private String gdbianhao;
    private ArrayList<TextView> titleList;
    private int scrollViewWidth;
    private int offset = 0;

    TextView tv_zhuangxd_liucheng_toolbbar_title;

    private Fragment currentFragment = new Fragment();
    private List<Fragment> fragments = new ArrayList<>();

    private int currentIndex = 0;
    //当前显示的fragment
    private static final String CURRENT_FRAGMENT = "STATE_FRAGMENT_SHOW";
    MyApplication apps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuang_x_dan_liucheng);
        apps = (MyApplication) getApplication();
        apps.setwhichfragment("jianpei");//当前打开的是拣配页面

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        if (savedInstanceState != null) { // “内存重启”时调用
            //获取“内存重启”时保存的索引下标
            currentIndex = savedInstanceState.getInt(CURRENT_FRAGMENT, 0);
            //注意，添加顺序要跟下面添加的顺序一样！！！！
            fragments.removeAll(fragments);
            fragments.add(fragmentManager.findFragmentByTag(0 + ""));
            fragments.add(fragmentManager.findFragmentByTag(1 + ""));
            fragments.add(fragmentManager.findFragmentByTag(2 + ""));
            fragments.add(fragmentManager.findFragmentByTag(3 + ""));

            //恢复fragment页面
            restoreFragment();


        } else {      //正常启动时调用

            fragments.add(new JianpeiFragment());//0
            fragments.add(new ZhuangxiangFragment());//1
            fragments.add(new FayunFragment());//2
            fragments.add(new ReceiveFragment());//3

            showFragment();
        }

        initViews();
        ZhuangxiangdanLiucTabclick.clickJianpei();

        getIntentData();
    }

    public MyApplication getApps() {
        return apps;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        //“内存重启”时保存当前的fragment名字
        outState.putInt(CURRENT_FRAGMENT, currentIndex);
        super.onSaveInstanceState(outState);
    }

    public void getIntentData() {
        Intent intent = getIntent();
        //接收数据
        gdbianhao = intent.getStringExtra("host");

    }

    public void initViews() {
        titleList = new ArrayList<>();
        tv_liucheng_jianpei = findViewById(R.id.tv_liucheng_jianpei);
        tv_liucheng_zhuangxiang = findViewById(R.id.tv_liucheng_zhuangxiang);
        tv_liucheng_fayun = findViewById(R.id.tv_liucheng_fayun);
        tv_liucheng_receive = findViewById(R.id.tv_liucheng_receive);

        tv_zhuangxd_liucheng_toolbbar_title = findViewById(R.id.tv_zhuangxd_liucheng_toolbbar_title);

        toolbar_zhuangxd_liucheng_tianxie = findViewById(R.id.toolbar_zhuangxd_liucheng_tianxie);


        addTV();

        hscrollv_zhuangxd_liucheng = findViewById(R.id.hscrollv_zhuangxd_liucheng);
        hscrollv_zhuangxd_liucheng.setSmoothScrollingEnabled(true);
        ZhuangxiangdanLiucTabclick = new ZhuangxiangdanLiucTabclick(tv_liucheng_jianpei,
                tv_liucheng_zhuangxiang, tv_liucheng_fayun
                , tv_liucheng_receive);

        ZhuangxiangdanLiucTabclick.clickJianpei();//初始选中 服务响应tab
        tv_zhuangxd_liucheng_toolbbar_title.setText("装箱单-拣配");//

        setClick();


    }

    private void addTV() {
        titleList.add(tv_liucheng_jianpei);
        titleList.add(tv_liucheng_zhuangxiang);
        titleList.add(tv_liucheng_fayun);
        titleList.add(tv_liucheng_receive);
    }


    public void setClick() {

        toolbar_zhuangxd_liucheng_tianxie.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showisBackDialog();
            }
        });


        //拣配
        tv_liucheng_jianpei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gotoJianpei();
                horizonScrollviewtabSwitch(tv_liucheng_jianpei);
                ZhuangxiangdanLiucTabclick.clickJianpei();
                apps.setwhichfragment("jianpei");//当前打开的是拣配页面

            }
        });

        //装箱 baozhuang
        tv_liucheng_zhuangxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gotoBaozhuang();
                horizonScrollviewtabSwitch(tv_liucheng_zhuangxiang);
                ZhuangxiangdanLiucTabclick.clickBaozhuang();
                apps.setwhichfragment("baozhuang");//当前打开的是包装页面

            }
        });

        //发运
        tv_liucheng_fayun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoFayun();
                horizonScrollviewtabSwitch(tv_liucheng_fayun);
                ZhuangxiangdanLiucTabclick.clickFayun();
                apps.setwhichfragment("fayun");//当前打开的是发运页面

            }
        });

        //接收
        tv_liucheng_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZhuangxiangdanLiucTabclick.clickReceive();
                gotoHuanjianFrgment();
                horizonScrollviewtabSwitch(tv_liucheng_receive);
                apps.setwhichfragment("jieshou");//当前打开的是接收页面

            }
        });


    }


    public void gotoJianpei() {
        ZhuangxiangdanLiucTabclick.clickJianpei();
        tv_zhuangxd_liucheng_toolbbar_title.setText("装箱单-拣配");
        horizonScrollviewtabSwitch(tv_liucheng_jianpei);
        currentIndex = 0;
        showFragment();
    }

    public void gotoBaozhuang() {
        tv_zhuangxd_liucheng_toolbbar_title.setText("装箱单-装箱");
        ZhuangxiangdanLiucTabclick.clickBaozhuang();
        horizonScrollviewtabSwitch(tv_liucheng_zhuangxiang);
        //下面两行是相关联的。
        currentIndex = 1;
        showFragment();
    }

    public void gotoFayun() {

        tv_zhuangxd_liucheng_toolbbar_title.setText("装箱单-发运");
        ZhuangxiangdanLiucTabclick.clickFayun();
        horizonScrollviewtabSwitch(tv_liucheng_fayun);
        currentIndex = 2;
        showFragment();
    }

    public void gotoHuanjianFrgment() {
        ZhuangxiangdanLiucTabclick.clickReceive();

        tv_zhuangxd_liucheng_toolbbar_title.setText("装箱单-接收");
        horizonScrollviewtabSwitch(tv_liucheng_receive);
        currentIndex = 3;
        showFragment();
    }


    public void horizonScrollviewtabSwitch(TextView textView) {
        scrollViewWidth = 0;
        offset = 0;//需赋值为0  再次点击时才会有正确的反应
//        final TextView textView = titleList.get(5);
//                textView.setTextColor(color);

        scrollViewWidth = hscrollv_zhuangxd_liucheng.getWidth();

        if ((scrollViewWidth + offset) < textView.getRight()) {//需要向右移动
            hscrollv_zhuangxd_liucheng.smoothScrollBy(textView.getRight() - (scrollViewWidth + offset), 0);
            offset += textView.getRight() - (scrollViewWidth + offset);
        }

        if (offset > textView.getLeft()) {//需要向左移动
            hscrollv_zhuangxd_liucheng.smoothScrollBy(textView.getLeft() - offset, 0);
            offset += textView.getLeft() - offset;
        }

    }


    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }


    /**
     * 使用show() hide()切换页面
     * 显示fragment
     */
    private void showFragment() {

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //如果之前没有添加过
        if (!fragments.get(currentIndex).isAdded()) {
            transaction
                    .hide(currentFragment)
                    .add(R.id.frag_zhuangxiang_liucheng, fragments.get(currentIndex), "" + currentIndex);  //第三个参数为添加当前的fragment时绑定一个tag

        } else {
            transaction
                    .hide(currentFragment)
                    .show(fragments.get(currentIndex));
        }

        currentFragment = fragments.get(currentIndex);

        transaction.commit();

    }


    /**
     * 恢复fragment
     */
    private void restoreFragment() {
        FragmentTransaction mBeginTreansaction = fragmentManager.beginTransaction();

        for (int i = 0; i < fragments.size(); i++) {

            if (i == currentIndex) {
                mBeginTreansaction.show(fragments.get(i));
            } else {
                mBeginTreansaction.hide(fragments.get(i));
            }

        }

        mBeginTreansaction.commit();

        //把当前显示的fragment记录下来
        currentFragment = fragments.get(currentIndex);

    }


    @Override
    public void onBackPressed() {
        showisBackDialog();
        return;//拦截 物理返回按键；
    }

    private void showisBackDialog() {
        DialogLoader.getInstance().showConfirmDialog(
                this,
                "确定离开装箱单页面吗？",
                "是",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.dismiss();
                    }
                },
                "否",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );


    }


    @Override
    public void onPause() {
        super.onPause();
        unRegisterReceiver(this);
        XToastUtils.success("装箱单activity-onpause调用");
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(this);
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

                System.out.println("扫描的值1：" + svalue1);
                System.out.println("扫描的值2：" + svalue2);
                System.out.println("扫描de code：" + barcodeType);

                final String scanStatus = intent.getStringExtra("SCAN_STATE");
                if ("ok".equals(scanStatus)) {
                    //扫描到的值 赋值到list中；
                    apps.setScanStringtv1(svalue1);
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
