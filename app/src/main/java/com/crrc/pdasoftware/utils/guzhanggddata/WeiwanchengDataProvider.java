package com.crrc.pdasoftware.utils.guzhanggddata;

import java.util.ArrayList;
import java.util.List;

public class WeiwanchengDataProvider {

    static List<WeiwanchengDataInfo> WeiwcListdata = new ArrayList<>();

    public static void addOneweiwcListdata(String title, String content
                                        ) {

        WeiwanchengDataInfo xxdata = new WeiwanchengDataInfo(title, content);
        WeiwcListdata.add(xxdata);
    }

    public static List<WeiwanchengDataInfo> getWeiwcListdata() {
        return WeiwcListdata;
    }

    public static void clearWeiwcListdata() {
        WeiwcListdata.clear();
    }

}
