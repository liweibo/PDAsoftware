package com.crrc.pdasoftware.utils.guzhanggddata;

import java.util.ArrayList;
import java.util.List;


public class YiwcDataProvider {

    static List<YiwancDataInfo> YiwcListdata = new ArrayList<>();

    public static void addOneYiwcListdata(String Gdbh, String Status,
                                          String Chexingvalue,
                                          String ChehaoValue,
                                          String peishuyh,
                                          String kehuCALLTIME,
                                          String paigongliyou,
                                          String fuwudanweiContact,
                                          String secureGuanli,
                                          String leijizouxing,
                                          String fwdanweicontactphone,
                                          String workorderid,String xytime) {

        YiwancDataInfo xxdata = new YiwancDataInfo(Gdbh, Status,
                Chexingvalue, ChehaoValue, peishuyh,
                kehuCALLTIME,
                paigongliyou,
                fuwudanweiContact,
                secureGuanli,
                leijizouxing,
                fwdanweicontactphone,
                workorderid,xytime);
        YiwcListdata.add(xxdata);
    }

    public static List<YiwancDataInfo> getYiwcListdata() {
        return YiwcListdata;
    }

    public static void clearYiwcListdata() {
        YiwcListdata.clear();
    }






}
