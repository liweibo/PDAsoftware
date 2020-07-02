package com.crrc.pdasoftware.adapter.guzhanggongdanadapter;

import androidx.annotation.NonNull;

import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.utils.guzhanggddata.WeiwanchengDataInfo;
import com.crrc.pdasoftware.utils.guzhanggddata.ywcTwoparaDataInfo;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;


public class ywancAdapter extends BaseRecyclerAdapter<ywcTwoparaDataInfo> {

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.adapter_weiwanc;
    }

    @Override
    public void bindData(@NonNull RecyclerViewHolder holder, int position, ywcTwoparaDataInfo model) {
        if (model != null) {
            //给对应的item中的子组件分别赋值；
            holder.text(R.id.weiwc_adapter_title, model.getmTitle());
            holder.text(R.id.weiwc_adapter_content, model.getmContent());
        }
    }

}
