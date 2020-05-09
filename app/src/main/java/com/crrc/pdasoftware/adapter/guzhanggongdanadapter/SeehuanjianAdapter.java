package com.crrc.pdasoftware.adapter.guzhanggongdanadapter;

import androidx.annotation.NonNull;

import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.utils.guzhanggddata.SeePaichaDataInfo;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;


public class SeehuanjianAdapter extends BaseRecyclerAdapter<SeePaichaDataInfo> {

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.see_huanjian_cardview;
    }

    @Override
    public void bindData(@NonNull RecyclerViewHolder holder, int position, SeePaichaDataInfo model) {
        if (model != null) {
            //给对应的item中的子组件分别赋值；
            int l = position+1;
            holder.text(R.id.huanjian_header_title,"换件记录 "+l);


        }
    }

}
