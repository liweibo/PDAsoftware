package com.crrc.pdasoftware.adapter.zhuangxiangdan;

import androidx.annotation.NonNull;

import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.utils.guzhanggddata.WeiwancDataInfo;
import com.crrc.pdasoftware.utils.zhuangxiangdandata.XianchangfachuDataInfo;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;


public class XianchangfachuAdapter extends BaseRecyclerAdapter<XianchangfachuDataInfo> {

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.adapter_xianchangfachu;
    }

    @Override
    public void bindData(@NonNull RecyclerViewHolder holder, int position, XianchangfachuDataInfo model) {
        if (model != null) {
            //给对应的item中的子组件分别赋值；
            holder.text(R.id.tv_xianchangfachu_gdbh,model.getGdbh());//工单编号
            holder.text(R.id.tv_xianchangfachu_zhidan_people,model.getxianchang_fachu_zhidan_people());
            holder.text(R.id.tv_xianchangfachu_status,model.getStatus());
            holder.text(R.id.tv_xianchangfachu_time,model.getTime());
            holder.text(R.id.tv_xianchangfachu_xiangmuvalue,model.getfachukufang());
        }
    }

}
