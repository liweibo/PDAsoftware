package com.crrc.pdasoftware.utils.guzhanggddata;

import java.util.ArrayList;
import java.util.List;

/**
 * 演示数据
 *
 * @author xuexiang
 * @since 2018/11/23 下午5:52
 */
public class FuwuDataProvider {

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

        FuwuDataInfo xxdata = new FuwuDataInfo(Gdbh, Status,
                Chexingvalue, ChehaoValue, peishuyh,
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


    public static List<SeePaichaDataInfo> getDemoSeepaichaInfos() {
        List<SeePaichaDataInfo> list = new ArrayList<>();

        list.add(new SeePaichaDataInfo("变流器", "test", "test",
                "test", "test")
        );
        list.add(new SeePaichaDataInfo("牵引", "test", "test",
                "test", "test")
        );
        list.add(new SeePaichaDataInfo("6A", "test", "test",
                "test", "test")
        );
        list.add(new SeePaichaDataInfo("xxx", "test", "test",
                "test", "test")
        );
        list.add(new SeePaichaDataInfo("yyy", "test", "test",
                "test", "test")
        );
        list.add(new SeePaichaDataInfo("zzz", "test", "test",
                "test", "test")
        );

        return list;
    }


}
