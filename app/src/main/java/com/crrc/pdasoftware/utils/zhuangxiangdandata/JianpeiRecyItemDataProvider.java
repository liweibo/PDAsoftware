package com.crrc.pdasoftware.utils.zhuangxiangdandata;

import java.util.ArrayList;
import java.util.List;


public class JianpeiRecyItemDataProvider {
    static List<JianpeiRecyItemDataInfo> list = new ArrayList<>();
    public static List<JianpeiRecyItemDataInfo> getJianpeiListNewInfos() {
        return list;
    }

    //网络获取或扫码的值，赋值方法
    public static void setJianpeiListNewInfos(List<JianpeiRecyItemDataInfo> infoList) {
        for (JianpeiRecyItemDataInfo info : infoList
        ) {
            list.add(info);
        }
    }

    public static void clearJianpeiListNewInfos() {
        list.clear();
    }
}
