package com.crrc.pdasoftware.utils;

import android.graphics.Color;
import android.widget.TextView;

import com.crrc.pdasoftware.R;

public class ZhuangxiangdanLiucTabclick {
    TextView tvJianpei;
    TextView tvBaozhuang;
    TextView tvFayun;
    TextView tvReceive;


    public ZhuangxiangdanLiucTabclick(TextView tvJianpei,
                           TextView tvBaozhuang,
                           TextView tvFayun,
                           TextView tvReceive

    ) {
        this.tvJianpei = tvJianpei;
        this.tvBaozhuang = tvBaozhuang;
        this.tvFayun = tvFayun;
        this.tvReceive = tvReceive;
    }

    public void clickJianpei() {
        tvJianpei.setBackgroundResource(R.drawable.textview_border_special_btn);
        tvJianpei.setTextColor(Color.parseColor("#FFFFFF"));

        tvBaozhuang.setBackgroundResource(R.drawable.textview_border);
        tvBaozhuang.setTextColor(Color.parseColor("#299EE3"));

        tvFayun.setBackgroundResource(R.drawable.textview_border);
        tvFayun.setTextColor(Color.parseColor("#299EE3"));


        tvReceive.setBackgroundResource(R.drawable.textview_border);
        tvReceive.setTextColor(Color.parseColor("#299EE3"));


    }


    public void clickBaozhuang() {
        tvBaozhuang.setBackgroundResource(R.drawable.textview_border_special_btn);
        tvBaozhuang.setTextColor(Color.parseColor("#FFFFFF"));

        tvJianpei.setBackgroundResource(R.drawable.textview_border);
        tvJianpei.setTextColor(Color.parseColor("#299EE3"));

        tvFayun.setBackgroundResource(R.drawable.textview_border);
        tvFayun.setTextColor(Color.parseColor("#299EE3"));


        tvReceive.setBackgroundResource(R.drawable.textview_border);
        tvReceive.setTextColor(Color.parseColor("#299EE3"));


    }

    public void clickFayun() {
        tvFayun.setBackgroundResource(R.drawable.textview_border_special_btn);
        tvFayun.setTextColor(Color.parseColor("#FFFFFF"));

        tvJianpei.setBackgroundResource(R.drawable.textview_border);
        tvJianpei.setTextColor(Color.parseColor("#299EE3"));

        tvBaozhuang.setBackgroundResource(R.drawable.textview_border);
        tvBaozhuang.setTextColor(Color.parseColor("#299EE3"));


        tvReceive.setBackgroundResource(R.drawable.textview_border);
        tvReceive.setTextColor(Color.parseColor("#299EE3"));


    }

    public void clickReceive() {
        tvReceive.setBackgroundResource(R.drawable.textview_border_special_btn);
        tvReceive.setTextColor(Color.parseColor("#FFFFFF"));

        tvJianpei.setBackgroundResource(R.drawable.textview_border);
        tvJianpei.setTextColor(Color.parseColor("#299EE3"));

        tvBaozhuang.setBackgroundResource(R.drawable.textview_border);
        tvBaozhuang.setTextColor(Color.parseColor("#299EE3"));

        tvFayun.setBackgroundResource(R.drawable.textview_border);
        tvFayun.setTextColor(Color.parseColor("#299EE3"));



    }



    public void clickCommitresult() {

        tvJianpei.setBackgroundResource(R.drawable.textview_border);
        tvJianpei.setTextColor(Color.parseColor("#299EE3"));

        tvBaozhuang.setBackgroundResource(R.drawable.textview_border);
        tvBaozhuang.setTextColor(Color.parseColor("#299EE3"));

        tvFayun.setBackgroundResource(R.drawable.textview_border);
        tvFayun.setTextColor(Color.parseColor("#299EE3"));

        tvReceive.setBackgroundResource(R.drawable.textview_border);
        tvReceive.setTextColor(Color.parseColor("#299EE3"));


    }


    public void clickNothing() {

        tvJianpei.setBackgroundResource(R.drawable.textview_border);
        tvJianpei.setTextColor(Color.parseColor("#299EE3"));

        tvBaozhuang.setBackgroundResource(R.drawable.textview_border);
        tvBaozhuang.setTextColor(Color.parseColor("#299EE3"));

        tvFayun.setBackgroundResource(R.drawable.textview_border);
        tvFayun.setTextColor(Color.parseColor("#299EE3"));

        tvReceive.setBackgroundResource(R.drawable.textview_border);
        tvReceive.setTextColor(Color.parseColor("#299EE3"));


    }


}
