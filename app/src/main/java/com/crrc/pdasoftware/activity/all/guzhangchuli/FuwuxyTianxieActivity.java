package com.crrc.pdasoftware.activity.all.guzhangchuli;

import android.content.DialogInterface;
import android.content.Intent;
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

import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.fragments.dummy.DummyContent;
import com.crrc.pdasoftware.fragments.guzhangchuli.DaodaxcFragment;
import com.crrc.pdasoftware.fragments.guzhangchuli.FuwuFragment;
import com.crrc.pdasoftware.fragments.guzhangchuli.FuwuxiangyingWriteInfoFragment;
import com.crrc.pdasoftware.fragments.guzhangchuli.GuaqiFragment;
import com.crrc.pdasoftware.fragments.guzhangchuli.GuzChuliFragment;
import com.crrc.pdasoftware.fragments.guzhangchuli.HuanjianjiluFragment;
import com.crrc.pdasoftware.fragments.guzhangchuli.ResultCommitFragment;
import com.crrc.pdasoftware.fragments.guzhangchuli.SeeGuzhangpaichaFragment;
import com.crrc.pdasoftware.fragments.guzhangchuli.SeeHistoryFragment;
import com.crrc.pdasoftware.fragments.guzhangchuli.SeeHuanjianjiluFragment;
import com.crrc.pdasoftware.fragments.guzhangchuli.ShenheFragment;
import com.crrc.pdasoftware.net.Constant;
import com.crrc.pdasoftware.net.pojo.GuzhangListOfFWXYPojo;
import com.crrc.pdasoftware.utils.GuzhangTabclick;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.xuexiang.xui.widget.dialog.DialogLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import me.leefeng.promptlibrary.PromptDialog;
import rxhttp.wrapper.param.RxHttp;

import static android.media.CamcorderProfile.get;

public class FuwuxyTianxieActivity extends AppCompatActivity implements FuwuFragment.OnListFragmentInteractionListener, View.OnClickListener {

    TextView tvFuwuxiangy;
    TextView tvDaodaxianc;
    TextView tvGuzhangcl;
    TextView tvHuanjian;
    TextView tvGuaqi;


    Toolbar toolbar_fwxy_tianxie;
    GuzhangTabclick guzhangTabclick;
    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    HorizontalScrollView hscrollv;
    private String gdbianhao;
    private ArrayList<TextView> titleList;
    private int scrollViewWidth;
    private int offset = 0;

    TextView tv_gzgd_toolbbar_title;

    private Fragment currentFragment = new Fragment();
    private List<Fragment> fragments = new ArrayList<>();

    private int currentIndex = 0;
    //当前显示的fragment
    private static final String CURRENT_FRAGMENT = "STATE_FRAGMENT_SHOW";

    public FloatingActionMenu mFloatingActionMenu;
    FloatingActionButton fab_see_guzhangpaicha;
    FloatingActionButton fab_see_huanjian;
    FloatingActionButton fab_see_historyguzhang;
    FloatingActionButton fab_see_download;
    private LinearLayout ll_all;
    private PromptDialog promptDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuwuxy_tianxie);
        promptDialog = new PromptDialog(this);

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        System.out.println("-=-==:" + savedInstanceState);

        if (savedInstanceState != null) { // “内存重启”时调用
            //获取“内存重启”时保存的索引下标
            currentIndex = savedInstanceState.getInt(CURRENT_FRAGMENT, 0);

            //注意，添加顺序要跟下面添加的顺序一样！！！！
            fragments.removeAll(fragments);
            fragments.add(fragmentManager.findFragmentByTag(0 + ""));
            fragments.add(fragmentManager.findFragmentByTag(1 + ""));
            fragments.add(fragmentManager.findFragmentByTag(2 + ""));
            fragments.add(fragmentManager.findFragmentByTag(3 + ""));
            fragments.add(fragmentManager.findFragmentByTag(4 + ""));
            fragments.add(fragmentManager.findFragmentByTag(5 + ""));
            fragments.add(fragmentManager.findFragmentByTag(6 + ""));
            fragments.add(fragmentManager.findFragmentByTag(7 + ""));
            fragments.add(fragmentManager.findFragmentByTag(8 + ""));
            fragments.add(fragmentManager.findFragmentByTag(9 + ""));

            //恢复fragment页面
            restoreFragment();


        } else {      //正常启动时调用

            fragments.add(new FuwuxiangyingWriteInfoFragment());//0
            fragments.add(new DaodaxcFragment());//1
            fragments.add(new GuzChuliFragment());//2
            fragments.add(new HuanjianjiluFragment());//3
            fragments.add(new SeeHuanjianjiluFragment());//4
            fragments.add(new ResultCommitFragment());//5
            fragments.add(new ShenheFragment());//6
            fragments.add(new SeeGuzhangpaichaFragment());//7
            fragments.add(new GuaqiFragment());//8
            fragments.add(new SeeHistoryFragment());//9

            showFragment();
        }

        initViews();
        guzhangTabclick.clickFuwuxiangy();

        getIntentData();

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
        tvFuwuxiangy = findViewById(R.id.tv_fuwuxiangying);
        tvDaodaxianc = findViewById(R.id.tv_daodaxiancheng);
        tvGuzhangcl = findViewById(R.id.tv_guzhangchuli);
        tvHuanjian = findViewById(R.id.tv_huanjian);
        tvGuaqi = findViewById(R.id.tv_guaqi);

        tv_gzgd_toolbbar_title = findViewById(R.id.tv_gzgd_toolbbar_title);

        toolbar_fwxy_tianxie = findViewById(R.id.toolbar_fwxy_tianxie);

        mFloatingActionMenu = findViewById(R.id.fab_menu);
        fab_see_guzhangpaicha = findViewById(R.id.fab_see_guzhangpaicha);
        fab_see_huanjian = findViewById(R.id.fab_see_huanjian);
        fab_see_historyguzhang = findViewById(R.id.fab_see_historyguzhang);
        fab_see_download = findViewById(R.id.fab_see_download);
        ll_all = findViewById(R.id.frag_no);


        addTV();

        hscrollv = findViewById(R.id.hscrollv);
        hscrollv.setSmoothScrollingEnabled(true);
        guzhangTabclick = new GuzhangTabclick(tvFuwuxiangy, tvDaodaxianc, tvGuzhangcl
                , tvHuanjian, tvGuaqi);
        guzhangTabclick.clickFuwuxiangy();//初始选中 服务响应tab
        tv_gzgd_toolbbar_title.setText("故障工单-服务响应");//初始选中 标题为：服务响应

//        addFragmentFwXiangyingWriteinfo();
//        showFragment();
        setClick();


    }

    private void addTV() {
        titleList.add(tvFuwuxiangy);
        titleList.add(tvDaodaxianc);
        titleList.add(tvGuzhangcl);
        titleList.add(tvHuanjian);
        titleList.add(tvGuaqi);

    }


    public void setClick() {
        fab_see_download.setOnClickListener(this);
        fab_see_guzhangpaicha.setOnClickListener(this);
        fab_see_historyguzhang.setOnClickListener(this);
        fab_see_huanjian.setOnClickListener(this);

        ll_all.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mFloatingActionMenu.close(false);

                return false;
            }
        });
        toolbar_fwxy_tianxie.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showisBackDialog();
            }
        });


        //服务响应
        tvFuwuxiangy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                gotoFuwuxyfrgment();
                horizonScrollviewtabSwitch(tvFuwuxiangy);
                guzhangTabclick.clickFuwuxiangy();

            }
        });
        //到达现场
        tvDaodaxianc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //方法1： 直接使用asclass把结果映射到GuzhangTestPojo类中。

                RxHttp.postForm(Constant.usualInterfaceAddr)
                        .add(Constant.usualKey, Constant.usualRequParaJson)
                        .asClass(GuzhangListOfFWXYPojo.class).
                        observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                            //请求开始，当前在主线程回调
                            promptDialog.showLoading("加载中...");

                        })
                        .doFinally(() -> {
                            //请求结束，当前在主线程回调
                            promptDialog.dismissImmediately();

                        })

                        .subscribe(clz -> {//clz就是GuzhangTestPojo
                            List<GuzhangListOfFWXYPojo.DataBeanX.DataBean> datalevel = clz.data.data;
                            Map<String, String> map = new HashMap<>();
                            List<Map> lstMap = new ArrayList<>();
                            System.out.println("-=-:"+datalevel.size());
                            if (datalevel.size() > 0) {
                                for (int i = 0; i < datalevel.size(); i++) {//行数据个数
                                    List<GuzhangListOfFWXYPojo.DataBeanX.DataBean.ValuemapBean> rowdatalevel
                                            = datalevel.get(i).valuemap;
                                    for (int j = 0; j < rowdatalevel.size(); j++) {
                                        String key = rowdatalevel.get(j).attribute;
                                        String va = rowdatalevel.get(j).value;
                                        map.put(key, va);

                                    }
                                    lstMap.add(map);
                                }
                                System.out.println("88888:" + lstMap.toString());
                                System.out.println("-=1-:"+lstMap.size());

                            }


                        }, throwable -> {
                            //失败回调
                            System.out.println("失败结果：" + throwable.toString());

                        });


                //方法2：
                //使用gson把string类型的json转换到GuzhangTestPojo类中

//                RxHttp.postForm(Constant.usualInterfaceAddr)
//                        .add(Constant.usualKey, Constant.usualRequParaJson)
//                        .asString()
//                        .subscribe(s -> { //这里的s为String类型，即Http请求的返回结果
//                            //成功回调
//                            GuzhangTestPojo clz = NetUtils.getGson().fromJson(s, GuzhangTestPojo.class);
//
//                            List<GuzhangTestPojo.DataBeanX.DataBean> datalevel = clz.data.data;
//
//
//                            Map<String, String> map = new HashMap<>();
//                            List<Map> lstMap = new ArrayList<>();
//                            if (datalevel.size() > 0) {
//                                for (int i = 0; i < datalevel.size(); i++) {//行数据个数
//                                    List<GuzhangTestPojo.DataBeanX.DataBean.ValuemapBean> rowdatalevel
//                                            = datalevel.get(i).valuemap;
//                                    for (int j = 0; j < rowdatalevel.size(); j++) {
//                                        String key = rowdatalevel.get(j).attribute;
//                                        String va = rowdatalevel.get(j).value;
//                                        System.out.println("key:" + key);
//                                        System.out.println("value:" + va);
//                                        map.put(key, va);
//
//                                    }
//                                    lstMap.add(map);
//                                }
//                                System.out.println("88888:" + map.toString());
//                            }
//
//
//                        }, throwable -> {
//                            //失败回调
//                            System.out.println("失败结果：" + throwable.toString());
//
//                        });


                gotoDaodaxiancfrgment();
                horizonScrollviewtabSwitch(tvDaodaxianc);
                guzhangTabclick.clickDaodaxianc();
            }
        });

        //故障处理
        tvGuzhangcl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoGuzhangChulifrgment();
                horizonScrollviewtabSwitch(tvGuzhangcl);
                guzhangTabclick.clickGuzhangchuli();
            }
        });

        tvHuanjian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guzhangTabclick.clickHuanjian();
                gotoHuanjianFrgment();
                horizonScrollviewtabSwitch(tvHuanjian);
            }
        });
        tvGuaqi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guzhangTabclick.clickGuaqi();
                gotoGuaqiFrgment();
                horizonScrollviewtabSwitch(tvGuaqi);
            }
        });


    }


    public void gotoFuwuxyfrgment() {
        guzhangTabclick.clickFuwuxiangy();
        tv_gzgd_toolbbar_title.setText("故障工单-服务响应");
        horizonScrollviewtabSwitch(tvFuwuxiangy);
        currentIndex = 0;
        showFragment();
    }

    public void gotoDaodaxiancfrgment() {
        tv_gzgd_toolbbar_title.setText("故障工单-到达现场");
        guzhangTabclick.clickDaodaxianc();
        horizonScrollviewtabSwitch(tvDaodaxianc);
        //下面两行是相关联的。
        currentIndex = 1;
        showFragment();
    }

    public void gotoGuzhangChulifrgment() {

        tv_gzgd_toolbbar_title.setText("故障工单-故障处理");
        guzhangTabclick.clickGuzhangchuli();
        horizonScrollviewtabSwitch(tvGuzhangcl);
        currentIndex = 2;
        showFragment();
    }

    public void gotoHuanjianFrgment() {
        guzhangTabclick.clickHuanjian();

        tv_gzgd_toolbbar_title.setText("故障工单-换件");
        horizonScrollviewtabSwitch(tvHuanjian);
        currentIndex = 3;
        showFragment();
    }

    public void gotoGuaqiFrgment() {
        guzhangTabclick.clickGuaqi();

        tv_gzgd_toolbbar_title.setText("故障工单-挂起");
        horizonScrollviewtabSwitch(tvGuaqi);
        currentIndex = 8;
        showFragment();
    }

    //            fragments.add(new ResultCommitFragment());//5
    public void gotoShenheFrgment() {
//        guzhangTabclick.clickNothing();

        tv_gzgd_toolbbar_title.setText("故障工单-审核");
        currentIndex = 6;
        showFragment();
    }

    public void gotoSeeHuanjianFrgment() {
        guzhangTabclick.clickNothing();

        tv_gzgd_toolbbar_title.setText("查看换件记录");

        currentIndex = 4;
        showFragment();
    }

    public void gotoSeeHistoryFrgment() {
        guzhangTabclick.clickNothing();

        tv_gzgd_toolbbar_title.setText("查看历史故障");

        currentIndex = 9;
        showFragment();
    }


    public void gotoSeeGuzhangPaichaFrgment() {
        guzhangTabclick.clickNothing();

        tv_gzgd_toolbbar_title.setText("查看故障排查");
        currentIndex = 7;
        showFragment();
    }

    public void horizonScrollviewtabSwitch(TextView textView) {
        scrollViewWidth = 0;
        offset = 0;//需赋值为0  再次点击时才会有正确的反应
//        final TextView textView = titleList.get(5);
//                textView.setTextColor(color);

        scrollViewWidth = hscrollv.getWidth();

        if ((scrollViewWidth + offset) < textView.getRight()) {//需要向右移动
            hscrollv.smoothScrollBy(textView.getRight() - (scrollViewWidth + offset), 0);
            offset += textView.getRight() - (scrollViewWidth + offset);
        }

        if (offset > textView.getLeft()) {//需要向左移动
            hscrollv.smoothScrollBy(textView.getLeft() - offset, 0);
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
                    .add(R.id.frag, fragments.get(currentIndex), "" + currentIndex);  //第三个参数为添加当前的fragment时绑定一个tag

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
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab_see_guzhangpaicha:
                gotoSeeGuzhangPaichaFrgment();

                break;
            case R.id.fab_see_historyguzhang:
                gotoSeeHistoryFrgment();
                break;
            case R.id.fab_see_huanjian:
                gotoSeeHuanjianFrgment();

                break;
            case R.id.fab_see_download:

                break;


        }
        mFloatingActionMenu.toggle(false);

    }

    @Override
    public void onBackPressed() {
        showisBackDialog();
        return;//拦截 物理返回按键；
    }

    private void showisBackDialog() {
        DialogLoader.getInstance().showConfirmDialog(
                FuwuxyTianxieActivity.this,
                "确定离开故障工单页面吗？",
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
}
