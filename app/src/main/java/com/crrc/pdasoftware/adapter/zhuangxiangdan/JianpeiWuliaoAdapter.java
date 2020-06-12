package com.crrc.pdasoftware.adapter.zhuangxiangdan;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;

import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.activity.all.zhuangxiangdan.ZhuangXDanLiuchengActivity;
import com.crrc.pdasoftware.utils.ClearEditText;
import com.crrc.pdasoftware.utils.ScanUtils;
import com.crrc.pdasoftware.utils.XToastUtils;
import com.crrc.pdasoftware.utils.zhuangxiangdandata.JianpeiRecyItemDataInfo;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;


public class JianpeiWuliaoAdapter extends BaseRecyclerAdapter<JianpeiRecyItemDataInfo> {
    Activity activity;

    public JianpeiWuliaoAdapter(Activity activity

    ) {
        this.activity = activity;

    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_recy_jianpei_wuliao;
    }

    @Override
    public void bindData(@NonNull RecyclerViewHolder holder, int position, JianpeiRecyItemDataInfo model) {


        if (model != null) {
            //给对应的item中的子组件分别赋值；
            holder.text(R.id.jianpei_item_miaoshu, model.getWuliaoname());
            holder.text(R.id.jianpei_item_fachukufang, model.getfachukufang());
            holder.text(R.id.jianpei_item_cangwei, model.getcangwei());
            holder.text(R.id.jianpei_item_wuliao_zhuangtai, model.getwuliaozhuangtai());
            holder.text(R.id.jianpei_item_wuliaocode, model.getwuliaocode());
            holder.text(R.id.jianpei_item_picihao, model.getpicihao());
            holder.text(R.id.jianpei_item_xuliehao, model.getXuliehao());
            holder.text(R.id.jianpei_item_diaobonumber, model.getDiaobonumber());
            holder.text(R.id.jianpei_item_yisaomiaonumber, model.getHavescannumber());
        }

        clickListen(position, holder);

    }


    public void clickListen(final int pos, RecyclerViewHolder holder) {

        holder.itemView.findViewById(R.id.ll_jianpei_item_wuliaocode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ZhuangXDanLiuchengActivity) activity) .getApps().setadapterPos(pos);
                //启动扫码；
                ScanUtils.springScan(activity);
            }
        });

        holder.itemView.findViewById(R.id.ll_jianpei_item_picihao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ZhuangXDanLiuchengActivity) activity) .getApps().setadapterPos(pos);


                //启动扫码；
                ScanUtils.springScan(activity);
            }
        });

        holder.itemView.findViewById(R.id.ll_jianpei_item_xuliehao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ZhuangXDanLiuchengActivity) activity) .getApps().setadapterPos(pos);
                //启动扫码；
                ScanUtils.springScan(activity);
            }
        });
    }


}
