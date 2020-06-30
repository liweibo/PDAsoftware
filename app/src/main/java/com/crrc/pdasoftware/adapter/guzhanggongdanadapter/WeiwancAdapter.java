package com.crrc.pdasoftware.adapter.guzhanggongdanadapter;

import androidx.annotation.NonNull;

import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.utils.guzhanggddata.FuwuDataInfo;
import com.crrc.pdasoftware.utils.guzhanggddata.WeiwanchengDataInfo;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;


public class WeiwancAdapter extends BaseRecyclerAdapter<WeiwanchengDataInfo> {

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.adapter_weiwanc;
    }

    @Override
    public void bindData(@NonNull RecyclerViewHolder holder, int position, WeiwanchengDataInfo model) {
        if (model != null) {
            //给对应的item中的子组件分别赋值；
            holder.text(R.id.weiwc_adapter_title, model.getmTitle());
            holder.text(R.id.weiwc_adapter_content, model.getmContent());
        }
    }

}
