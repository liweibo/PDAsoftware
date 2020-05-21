package com.crrc.pdasoftware.adapter.zhuangxiangdan;

import androidx.annotation.NonNull;

import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.utils.guzhanggddata.FuwuDataInfo;
import com.crrc.pdasoftware.utils.zhuangxiangdandata.ZhongxinfachuDataInfo;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;


public class ZhongxinfachuAdapter extends BaseRecyclerAdapter<ZhongxinfachuDataInfo> {

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.adapter_zhongxinfachu;
    }

    @Override
    public void bindData(@NonNull RecyclerViewHolder holder, int position, ZhongxinfachuDataInfo model) {
        if (model != null) {
            //给对应的item中的子组件分别赋值；
            holder.text(R.id.tv_zhongxinfachu_gdbh,model.getGdbh());//工单编号
            holder.text(R.id.tv_zhongxinfachu_zhidan_people,model.getxianchang_fachu_zhidan_people());
            holder.text(R.id.tv_zhongxinfachu_status,model.getStatus());
            holder.text(R.id.tv_zhongxinfachu_time,model.getTime());
            holder.text(R.id.tv_zhongxinfachu_xiangmuvalue,model.getfachukufang());

        }
    }

}
