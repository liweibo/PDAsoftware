package com.crrc.pdasoftware.utils.guzhanggddata;

import java.util.ArrayList;
import java.util.List;


public class WeiwcDataProvider {

    static List<WeiwancDataInfo> WeiwcListdata = new ArrayList<>();

    public static void addOneWeiwcListdata(String Gdbh, String Status,
                                           String Chexingvalue,
                                           String ChehaoValue,
                                           String peishuyh,
                                           String kehuCALLTIME,
                                           String paigongliyou,
                                           String fuwudanweiContact,
                                           String secureGuanli,
                                           String leijizouxing,
                                           String fwdanweicontactphone,
                                           String workorderid) {

        WeiwancDataInfo xxdata = new WeiwancDataInfo(
                Gdbh, Status,
                Chexingvalue, ChehaoValue, peishuyh,
                kehuCALLTIME,
                paigongliyou,
                fuwudanweiContact,
                secureGuanli,
                leijizouxing,
                fwdanweicontactphone,
                workorderid
        );
        WeiwcListdata.add(xxdata);
    }

    public static List<WeiwancDataInfo> getWeiwcListdata() {
        return WeiwcListdata;
    }

    public static void clearWeiwcListdata() {
        WeiwcListdata.clear();
    }






}
