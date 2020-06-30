package com.crrc.pdasoftware.activity.all.guzhangchuli;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;

import android.os.Bundle;
import android.view.View;

import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.adapter.guzhanggongdanadapter.FuwuXiangyingAdapter;
import com.crrc.pdasoftware.adapter.guzhanggongdanadapter.WeiwancAdapter;
import com.crrc.pdasoftware.utils.FiledDataSave;
import com.crrc.pdasoftware.utils.guzhanggddata.WeiwancDataProvider;
import com.crrc.pdasoftware.utils.guzhanggddata.WeiwanchengDataInfo;
import com.crrc.pdasoftware.utils.guzhanggddata.WeiwanchengDataProvider;
import com.xuexiang.xui.adapter.recyclerview.XLinearLayoutManager;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

public class WeiwcActivity extends AppCompatActivity {

    private SwipeRecyclerView recyclerView;
    private WeiwancAdapter mAdapter;
    Toolbar toolbar_weiwancheng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weiwc);
        recyclerView = findViewById(R.id.recyclerVie_weiwc);

        recyclerView.setLayoutManager(new XLinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        initViews();
        clickEvent();


        if (FiledDataSave.whichTab == 1) {//点击的是未完成
            WeiwanchengDataProvider.clearWeiwcListdata();
            for (int i = 0; i < 20; i++) {
                WeiwanchengDataProvider.addOneweiwcListdata("未完成title" + i, "content" + i);
            }
        } else if (FiledDataSave.whichTab == 2) {//点击的是已完成
            WeiwanchengDataProvider.clearWeiwcListdata();
            for (int i = 0; i < 20; i++) {
                WeiwanchengDataProvider.addOneweiwcListdata("已完成title" + i, "content" + i);
            }
        }


        recyclerView.setAdapter(mAdapter = new WeiwancAdapter());
        mAdapter.refresh(WeiwanchengDataProvider.getWeiwcListdata());
    }

    private void initViews() {
        toolbar_weiwancheng = findViewById(R.id.toolbar_weiwancheng);
    }
    private void clickEvent() {
        toolbar_weiwancheng.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
