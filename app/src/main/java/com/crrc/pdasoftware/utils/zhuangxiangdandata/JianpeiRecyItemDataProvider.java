package com.crrc.pdasoftware.utils.zhuangxiangdandata;

import java.util.ArrayList;
import java.util.List;


public class JianpeiRecyItemDataProvider {

   
   
    public static List<JianpeiRecyItemDataInfo> getJianpeiListNewInfos() {
        List<JianpeiRecyItemDataInfo> list = new ArrayList<>();

        list.add(new JianpeiRecyItemDataInfo("变流器",
                        "中心库", "xx仓位","未拣配",
                "TE003-351300","ddie22211",
                        "Z002005177","1",
                        "0")
                );
        list.add(new JianpeiRecyItemDataInfo("螺丝",
                        "中心库", "xx仓位","未拣配",
                "TE003-891300","无",
                        "无","100",
                        "0")
                );


       return list;
    }



}
