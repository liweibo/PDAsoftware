package com.crrc.pdasoftware.activity.all.zhuangxiangdan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.fragments.dummy.DummyContent;
import com.crrc.pdasoftware.fragments.guzhangchuli.FuwuFragment;
import com.crrc.pdasoftware.fragments.guzhangchuli.WeiwanchengFragment;
import com.crrc.pdasoftware.fragments.guzhangchuli.yiwanchengFragment;
import com.crrc.pdasoftware.fragments.zhuangxiangdan.Xianchangfachu;
import com.crrc.pdasoftware.fragments.zhuangxiangdan.Zhongxinfachu;
import com.crrc.pdasoftware.utils.XToastUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.xuexiang.xui.adapter.FragmentAdapter;
import com.xuexiang.xui.utils.SnackbarUtils;
import com.xuexiang.xui.widget.searchview.MaterialSearchView;

import java.util.ArrayList;

import static com.google.android.material.tabs.TabLayout.MODE_FIXED;

public class ZhuangxiangdanActivity extends AppCompatActivity
        implements TabLayout.OnTabSelectedListener,
        WeiwanchengFragment.OnListFragmentInteractionListener,
        yiwanchengFragment.OnListFragmentInteractionListener,
        FuwuFragment.OnListFragmentInteractionListener {
    TabLayout mTabLayout;
    ViewPager mViewPager;
    Toolbar toolbar;
    ImageView imSearch;
    MaterialSearchView mSearchView;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhuangxiangdan);
        initViews();
    }

    private void initViews() {
        mTabLayout = findViewById(R.id.tab_layout_zhuangxiangdan);
        mViewPager = findViewById(R.id.view_pager_zhuangxiangdan);
        toolbar = findViewById(R.id.toolbar_zhuangxiangdan);
        imSearch = findViewById(R.id.im_search_zhuangxiangdan);
        mSearchView = findViewById(R.id.search_view_zhuangxiangdan);
        fab = findViewById(R.id.fab_recycler_guzhang_zhuangxiangdan);
        mTabLayout.addOnTabSelectedListener(this);
        mTabLayout.setupWithViewPager(mViewPager);
        setToolBarClick();
        initViewPager();

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    public void initViewPager() {
        FragmentAdapter<Fragment> adapter = new FragmentAdapter<>(getSupportFragmentManager());
        mTabLayout.setTabMode(MODE_FIXED);
        adapter.addFragment(new Zhongxinfachu(), "中心发出");
        adapter.addFragment(new Xianchangfachu(), "现场发出");
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);


    }

    public void setToolBarClick() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XToastUtils.toast("新建故障工单");
                //跳转到工单派活界面
//                startActivity(new Intent(com.crrc.pdasoftware.activity.all.guzhangchuli.GuZhangActivity.this,
//                        GuzhangPaigongActivity.class));
            }
        });
        //搜索
        imSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(GuZhangActivity.this,SearchActivity.class));
                mSearchView.showSearch();
                mSearchView.setHint("输入工单号搜索..");
                mSearchView.setHintTextColor(Color.parseColor("#C0C0C0"));
            }
        });

        mSearchView.setVoiceSearch(false);
//        mSearchView.setCursorDrawable(R.drawable.custom_cursor);//自定义光标颜色
        mSearchView.setEllipsize(true);
        mSearchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SnackbarUtils.Long(mSearchView, "Query: " + query).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });
        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
        mSearchView.setSubmitOnClick(true);
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    mSearchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
