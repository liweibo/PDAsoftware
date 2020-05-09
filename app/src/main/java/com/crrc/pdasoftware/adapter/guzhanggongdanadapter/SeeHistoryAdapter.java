package com.crrc.pdasoftware.adapter.guzhanggongdanadapter;

import androidx.annotation.NonNull;

import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.utils.guzhanggddata.FuwuDataInfo;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;


public class SeeHistoryAdapter extends BaseRecyclerAdapter<FuwuDataInfo> {

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.see_histotyguzhang_list_item;
    }

    @Override
    public void bindData(@NonNull RecyclerViewHolder holder, int position, FuwuDataInfo model) {
        if (model != null) {
            //给对应的item中的子组件分别赋值；
            holder.text(R.id.tv_see_history_gdbh,model.getGdbh());//工单编号
            holder.text(R.id.tv_see_history_chexingvalue,model.getChexingvalue());
            holder.text(R.id.tv_see_history_status,model.getStatus());
            holder.text(R.id.tv_see_history_time,model.getTime());
            holder.text(R.id.tv_see_history_xiangmuvalue,model.getItemvalue());
            holder.text(R.id.tv_see_history_chehaovalue,model.getChehaoValue());

        }
    }

}
