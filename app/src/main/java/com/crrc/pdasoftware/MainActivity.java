package com.crrc.pdasoftware;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.crrc.pdasoftware.utils.Colors;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.xuexiang.xui.utils.StatusBarUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    CollapsingToolbarLayout collapseLayout;
    AppBarLayout appbarLayout;

    LinearLayout llGuzhang;
    LinearLayout llZhuangxiang;
    LinearLayout llDiaobo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        toolbar = findViewById(R.id.appbar_layout_toolbar);
        collapseLayout = findViewById(R.id.collapse_layout);
        appbarLayout = findViewById(R.id.appbar_layout);

        llGuzhang = findViewById(R.id.ll_guzhang);
        llZhuangxiang = findViewById(R.id.ll_zhuangxiangdan);
        llDiaobo = findViewById(R.id.ll_diaobozhuanku);

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
           case R.id.ll_guzhang:
startActivity(new Intent(MainActivity.this,GuZhangActivity.class));
            break;

            case R.id.ll_diaobozhuanku:

                break;

            case R.id.ll_zhuangxiangdan:

                break;


        }
    }
}
