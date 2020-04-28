package com.crrc.pdasoftware;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.crrc.pdasoftware.fragments.DaodaxcFragment;
import com.crrc.pdasoftware.fragments.FuwuFragment;
import com.crrc.pdasoftware.fragments.FuwuxiangyingWriteInfoFragment;
import com.crrc.pdasoftware.fragments.GuzChuliFragment;
import com.crrc.pdasoftware.fragments.dummy.DummyContent;
import com.crrc.pdasoftware.utils.GuzhangTabclick;

import java.util.ArrayList;
import java.util.List;

public class FuwuxyTianxieActivity extends AppCompatActivity implements FuwuFragment.OnListFragmentInteractionListener {

    TextView tvFuwuxiangy;
    TextView tvDaodaxianc;
    TextView tvGuzhangcl;
    TextView tvGuzhangpaicha;
    TextView tvHuanjianjilu;
    TextView tvGuzhangjilu;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuwuxy_tianxie);


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

            //恢复fragment页面
            restoreFragment();


        } else {      //正常启动时调用

            fragments.add(new FuwuxiangyingWriteInfoFragment());
            fragments.add(new DaodaxcFragment());
            fragments.add(new GuzChuliFragment());

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
        tvGuzhangpaicha = findViewById(R.id.tv_guzhangpaicha);
        tvHuanjianjilu = findViewById(R.id.tv_huanjianjilu);
        tvGuzhangjilu = findViewById(R.id.tv_guzhangjilu);
        tv_gzgd_toolbbar_title = findViewById(R.id.tv_gzgd_toolbbar_title);

        toolbar_fwxy_tianxie = findViewById(R.id.toolbar_fwxy_tianxie);

        addTV();

        hscrollv = findViewById(R.id.hscrollv);
        hscrollv.setSmoothScrollingEnabled(true);
        guzhangTabclick = new GuzhangTabclick(tvFuwuxiangy, tvDaodaxianc, tvGuzhangcl
                , tvGuzhangpaicha, tvHuanjianjilu, tvGuzhangjilu);
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
        titleList.add(tvGuzhangpaicha);
        titleList.add(tvHuanjianjilu);
        titleList.add(tvGuzhangjilu);
    }

    public void addFragmentFwXiangyingWriteinfo() {
        FuwuxiangyingWriteInfoFragment fragment = new FuwuxiangyingWriteInfoFragment();
        transaction.replace(R.id.frag, fragment);
        transaction.commit();
    }

    public void addFragmentdaodaxcWriteinfo() {
        DaodaxcFragment fragment = new DaodaxcFragment();
        transaction.replace(R.id.frag, fragment);
        transaction.commit();
    }

    public void setClick() {
        //这里 返回按钮 模拟的是点击组件，horizonScrollView中的组件
        // 显示第六个（titleList.get(5)） --》5+1=6
        toolbar_fwxy_tianxie.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizonScrollviewtabSwitch(tvGuzhangjilu);
            }
        });

        //服务响应
        tvFuwuxiangy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gotoFuwuxyfrgment();
            }
        });
        //到达现场
        tvDaodaxianc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoDaodaxiancfrgment();
            }
        });

        //故障处理
        tvGuzhangcl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoGuzhangChulifrgment();
            }
        });

        tvGuzhangpaicha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guzhangTabclick.clickGuzhangpaicha();

            }
        });
        tvHuanjianjilu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guzhangTabclick.clickHuanjianjilu();
            }
        });
        tvGuzhangjilu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guzhangTabclick.clickGuzhangjilu();
            }
        });

    }


    public void gotoFuwuxyfrgment() {
        guzhangTabclick.clickFuwuxiangy();
        tv_gzgd_toolbbar_title.setText("故障工单-服务响应");
        currentIndex = 0;
        showFragment();
    }

    public void gotoDaodaxiancfrgment() {
        tv_gzgd_toolbbar_title.setText("故障工单-达到现场");
        guzhangTabclick.clickDaodaxianc();
        //下面两行是相关联的。
        currentIndex = 1;
        showFragment();
    }

    public void gotoGuzhangChulifrgment() {
        guzhangTabclick.clickGuzhangchuli();
        tv_gzgd_toolbbar_title.setText("故障工单-故障处理");
        currentIndex = 2;
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


}