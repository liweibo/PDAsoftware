package com.crrc.pdasoftware.utils.zhuangxiangdandata;

import java.util.ArrayList;
import java.util.List;


public class BaozhuangRecyItemDataProvider {

    static List<BaozhuangRecyItemDataInfo> list = new ArrayList<>();

    public static List<BaozhuangRecyItemDataInfo> getBaozhuangListNewInfos() {
        return list;
    }

    //网络获取或扫码的值，赋值方法
    public static void setBaozhuangListNewInfos(List<BaozhuangRecyItemDataInfo> infoList) {
        for (BaozhuangRecyItemDataInfo info : infoList
        ) {
            list.add(info);
        }
    }

    public static void clearBaozhuangListNewInfos() {
        list.clear();
    }

}
