package com.crrc.pdasoftware;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.crrc.pdasoftware.activity.all.guzhangchuli.GuZhangActivity;
import com.crrc.pdasoftware.activity.all.zhuangxiangdan.ZhuangxiangdanActivity;
import com.crrc.pdasoftware.utils.Colors;
import com.crrc.pdasoftware.wuxianzhuanchu.all.activitys.HomeActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.xuexiang.xui.utils.StatusBarUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    AppBarLayout appbarLayout;

    LinearLayout llGuzhang;
    LinearLayout llZhuangxiang;
    LinearLayout llDiaobo;
    TabLayout mTabLayout;

    NestedScrollView main_renwu;
    NestedScrollView main_shenhe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initBottomTab();
    }


    /**
     * 初始化底部Tab
     */
    private void initBottomTab() {
        TabLayout.Tab component = mTabLayout.newTab();
        component.setText("任务");
        component.setIcon(R.drawable.selector_icon_tabbar_component);
        mTabLayout.addTab(component);

        TabLayout.Tab util = mTabLayout.newTab();
        util.setText("审核");
        util.setIcon(R.drawable.selector_icon_tabbar_util);
        mTabLayout.addTab(util);

        //最开始的显示
        main_renwu.setVisibility(View.VISIBLE);
        main_shenhe.setVisibility(View.INVISIBLE);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabTitle = tab.getText().toString().trim();
                if (tabTitle.equals("任务")) {
                    main_renwu.setVisibility(View.VISIBLE);
                    main_shenhe.setVisibility(View.INVISIBLE);
                } else if (tabTitle.equals("审核")) {
                    main_renwu.setVisibility(View.INVISIBLE);
                    main_shenhe.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void initViews() {
        toolbar = findViewById(R.id.appbar_layout_toolbar);
        appbarLayout = findViewById(R.id.appbar_layout);

        llGuzhang = findViewById(R.id.ll_guzhang);
        llZhuangxiang = findViewById(R.id.ll_zhuangxiangdan);
        llDiaobo = findViewById(R.id.ll_diaobozhuankudan);
        mTabLayout = findViewById(R.id.tabs);

        main_renwu = findViewById(R.id.main_renwu);
        main_shenhe = findViewById(R.id.main_shenhe);

        setClick();

        initBar();
    }

    private void setClick() {
        llGuzhang.setOnClickListener(this);
        llZhuangxiang.setOnClickListener(this);
        llDiaobo.setOnClickListener(this);
    }

    public void initBar() {
        StatusBarUtils.translucent(this, Colors.TRANSPARENT);
        StatusBarUtils.setStatusBarLightMode(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    StatusBarUtils.setStatusBarDarkMode(MainActivity.this);
                } else {
                    StatusBarUtils.setStatusBarLightMode(MainActivity.this);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //故障处理
            case R.id.ll_guzhang:
                startActivity(new Intent(MainActivity.this, GuZhangActivity.class));
                break;

            //调拨转库
            case R.id.ll_diaobozhuankudan:
                startActivity(new Intent(MainActivity.this, HomeActivity.class));

                break;

            //装箱单
            case R.id.ll_zhuangxiangdan:

                startActivity(new Intent(MainActivity.this, ZhuangxiangdanActivity.class));

                break;


        }
    }
}
