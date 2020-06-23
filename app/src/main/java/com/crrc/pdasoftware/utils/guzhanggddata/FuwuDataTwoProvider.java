package com.crrc.pdasoftware.utils.guzhanggddata;

import java.util.ArrayList;
import java.util.List;


//-----当前处理人是我的草稿状态的故障工单
//        select * from workorder where type='故障' andstatus='草稿' and SERVENGINEER='20137614'


public class FuwuDataTwoProvider {

    static List<FuwuDataInfo> FwxyListdata = new ArrayList<>();

    public static void addOneFwxyListdata(String Gdbh, String Status,
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

        FuwuDataInfo xxdata = new FuwuDataInfo(Gdbh, Status, Chexingvalue, ChehaoValue,
                peishuyh,
                kehuCALLTIME,
                paigongliyou,
                fuwudanweiContact,
                secureGuanli,
                leijizouxing,
                fwdanweicontactphone,
                workorderid);
        FwxyListdata.add(xxdata);
    }

    public static List<FuwuDataInfo> getFwxyListdata() {
        return FwxyListdata;
    }

    public static void clearFwxyListdata() {
        FwxyListdata.clear();
    }


}
