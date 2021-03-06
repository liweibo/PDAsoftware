package com.crrc.pdasoftware.adapter.guzhanggongdanadapter;

import androidx.annotation.NonNull;

import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.utils.guzhanggddata.SeePaichaDataInfo;
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;


public class SeeGzhangpaichaAdapter extends BaseRecyclerAdapter<SeePaichaDataInfo> {

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.see_guzhnag_paicha;
    }

    @Override
    public void bindData(@NonNull RecyclerViewHolder holder, int position, SeePaichaDataInfo model) {
        if (model != null) {
            //给对应的item中的子组件分别赋值；
            int l = position+1;
            holder.text(R.id.guzpaicheadertitle,"故障排查 "+l);
            holder.text(R.id.see_guzpaicha_xitonggnjian_tv,model.getxitonggongnjian());//工单编号
            holder.text(R.id.see_guzpaicha_guzxianx_tv,model.getguzhangxianx());
            holder.text(R.id.see_guzpaicha_guzyuanyin_tv,model.getguzhangyuanyin());
            holder.text(R.id.see_guzpaicha_chulicshi_tv,model.getchulicuoshi());
            holder.text(R.id.see_guzpaicha_chulijieguo_tv,model.getchulijieguo());

        }
    }

}
