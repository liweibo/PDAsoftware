package com.crrc.pdasoftware.adapter.guzhanggongdanadapter;

import androidx.annotation.NonNull;

import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.utils.guzhanggddata.YiwancDataInfo;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;


public class YiwancGongdanAdapter extends BaseRecyclerAdapter<YiwancDataInfo> {

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.fragment_yiwancheng;
    }



    @Override
    protected void bindData(@NonNull RecyclerViewHolder holder, int position, YiwancDataInfo model) {
        if (model != null) {
            //给对应的item中的子组件分别赋值；
            holder.text(R.id.tv_yiwc_gdbh,model.getGdbh());//工单编号
            holder.text(R.id.tv_yiwc_chexingvalue,model.getChexingvalue());
            holder.text(R.id.tv_yiwc_status,model.getStatus());
            holder.text(R.id.tv_yiwc_time,model.getTime());
            holder.text(R.id.tv_yiwc_xiangmuvalue,model.getItemvalue());
            holder.text(R.id.yiwc_chehaovalue,model.getChehaoValue());

        }
    }
}
