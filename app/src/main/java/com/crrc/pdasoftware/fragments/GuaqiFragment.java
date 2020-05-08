package com.crrc.pdasoftware.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.utils.TimeUtils;


public class GuaqiFragment extends Fragment {
    TextView tvCurrentTime;
    private boolean freshtime = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_guaqi, container, false);
        initViews(v);
        return v;
    }

    private void initViews(View v) {
        //挂起时间：
        tvCurrentTime = v.findViewById(R.id.guaqitime_tv);
//        tvCurrentTime.setText("" + TimeUtils.getCurrentTimeFormat());
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //   进入当前Fragment
        if (enter && !freshtime) {
            freshtime = true;
            //   这里可以做需要的数据刷新操作
            tvCurrentTime.setText("" + TimeUtils.getCurrentTimeFormat());


        } else {
            freshtime = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void onPause() {
        super.onPause();
        freshtime = false;
    }
}
