package com.crrc.pdasoftware.utils.guzhanggddata;

import java.util.ArrayList;
import java.util.List;

public class ywcTwoparaDataProvider {

    static List<ywcTwoparaDataInfo> yiwcListdata = new ArrayList<>();

    public static void addOneywcListdata(String title, String content
                                        ) {

        ywcTwoparaDataInfo xxdata = new ywcTwoparaDataInfo(title, content);
        yiwcListdata.add(xxdata);
    }

    public static List<ywcTwoparaDataInfo> getyiwcListdata() {
        return yiwcListdata;
    }

    public static void clearyiwcListdata() {
        yiwcListdata.clear();
    }

}
