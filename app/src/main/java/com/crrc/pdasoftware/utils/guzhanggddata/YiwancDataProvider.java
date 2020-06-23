package com.crrc.pdasoftware.utils.guzhanggddata;

import androidx.viewpager.widget.ViewPager;

import com.xuexiang.xui.widget.banner.transform.DepthTransformer;
import com.xuexiang.xui.widget.banner.transform.FadeSlideTransformer;
import com.xuexiang.xui.widget.banner.transform.FlowTransformer;
import com.xuexiang.xui.widget.banner.transform.RotateDownTransformer;
import com.xuexiang.xui.widget.banner.transform.RotateUpTransformer;
import com.xuexiang.xui.widget.banner.transform.ZoomOutSlideTransformer;
import com.xuexiang.xui.widget.banner.widget.banner.BannerItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 演示数据
 *
 * @author xuexiang
 * @since 2018/11/23 下午5:52
 */
public class YiwancDataProvider {


    public static List<FuwuDataInfo> getEmptyNewInfo() {
        List<FuwuDataInfo> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new FuwuDataInfo());
        }
        return list;
    }


    public static List<YiwancDataInfo> getDemoNewInfos() {
        List<YiwancDataInfo> list = new ArrayList<>();

//        list.add(new YiwancDataInfo("1111", "2020-02-02 19:33:22","待处理",
//                "杭州地铁8号线牵引系统_售后","杭州地铁8号线","2022")
//                );
//        list.add(new YiwancDataInfo("2222", "2020-04-02 19:13:22","待处理",
//                "南京地铁1号线牵引系统_售后","南京地铁1号线","1033")
//                );
//        list.add(new YiwancDataInfo("3333", "2019-02-02 19:02:22","待处理",
//                "四方股份电气牵引系统_售后","HXD1C","8776")
//                );
//        list.add(new YiwancDataInfo("4444", "2020-02-02 19:34:22","待处理",
//                "株机电气牵引系统_售后","HXD1D","1248")
//                );
//        list.add(new YiwancDataInfo("5555", "2020-01-01 19:39:22","待处理",
//                "长沙地铁1号线牵引系统_售后","长沙地铁1号线","0096")
//                );
//        list.add(new YiwancDataInfo("0028", "2018-02-02 19:33:22","待处理",
//                "长沙地铁3号线牵引系统_售后","长沙地铁3号线","3700")
//                );

       return list;
    }




}
