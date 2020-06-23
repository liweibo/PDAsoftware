package com.crrc.pdasoftware.adapter.guzhanggongdanadapter;

import androidx.annotation.NonNull;

import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.utils.guzhanggddata.WeiwancDataInfo;
import com.crrc.pdasoftware.utils.guzhanggddata.WeiwancDataInfo;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;


public class WeiwancGongdanAdapter extends BaseRecyclerAdapter<WeiwancDataInfo> {

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.fragment_weiwancheng;
    }

    @Override
    public void bindData(@NonNull RecyclerViewHolder holder, int position, WeiwancDataInfo model) {
        if (model != null) {
            //给对应的item中的子组件分别赋值；
            holder.text(R.id.tv_weiwc_gdbh,model.getGdbh());//工单编号
            holder.text(R.id.tv_weiwc_chexingvalue,model.getChexingvalue());
            holder.text(R.id.tv_weiwc_status,model.getStatus());
            holder.text(R.id.weiwc_chehaovalue,model.getChehaoValue());
        }
    }

}
