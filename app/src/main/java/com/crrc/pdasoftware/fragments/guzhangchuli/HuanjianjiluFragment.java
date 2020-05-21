package com.crrc.pdasoftware.fragments.guzhangchuli;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.crrc.pdasoftware.activity.all.guzhangchuli.FuwuxyTianxieActivity;
import com.crrc.pdasoftware.R;
import com.xuexiang.xui.widget.button.ButtonView;

public class HuanjianjiluFragment extends Fragment implements View.OnClickListener {

    ButtonView btnPost;//提交数据按钮

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_huanjianjilu, container, false);
        initView(v);

        return v;
    }


    public void initView(View v) {
        //findView
        btnPost = v.findViewById(R.id.huanjian_post);


        //设置监听
        setClickListen();
    }

    private void setClickListen() {
        btnPost.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.huanjian_post:
                ((FuwuxyTianxieActivity) getActivity()).gotoGuzhangChulifrgment();

                break;
        }

    }


}
