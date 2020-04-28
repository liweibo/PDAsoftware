package com.crrc.pdasoftware.utils;

import androidx.viewpager.widget.ViewPager;

import com.crrc.pdasoftware.R;
import com.xuexiang.xui.adapter.simple.AdapterItem;
import com.xuexiang.xui.adapter.simple.ExpandableItem;
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
public class DemoDataProvider {

    public static String[] titles = new String[]{
            "伪装者:胡歌演绎'痞子特工'",
            "无心法师:生死离别!月牙遭虐杀",
            "花千骨:尊上沦为花千骨",
            "综艺饭:胖轩偷看夏天洗澡掀波澜",
            "碟中谍4:阿汤哥高塔命悬一线,超越不可能",
    };

    public static String[] urls = new String[]{//640*360 360/640=0.5625
            "http://photocdn.sohu.com/tvmobilemvms/20150907/144160323071011277.jpg",//伪装者:胡歌演绎"痞子特工"
            "http://photocdn.sohu.com/tvmobilemvms/20150907/144158380433341332.jpg",//无心法师:生死离别!月牙遭虐杀
            "http://photocdn.sohu.com/tvmobilemvms/20150907/144160286644953923.jpg",//花千骨:尊上沦为花千骨
            "http://photocdn.sohu.com/tvmobilemvms/20150902/144115156939164801.jpg",//综艺饭:胖轩偷看夏天洗澡掀波澜
            "http://photocdn.sohu.com/tvmobilemvms/20150907/144159406950245847.jpg",//碟中谍4:阿汤哥高塔命悬一线,超越不可能
    };

    public static List<BannerItem> getBannerList() {
        ArrayList<BannerItem> list = new ArrayList<>();
        for (int i = 0; i < urls.length; i++) {
            BannerItem item = new BannerItem();
            item.imgUrl = urls[i];
            item.title = titles[i];

            list.add(item);
        }

        return list;
    }


    public static Class<? extends ViewPager.PageTransformer> transformers[] = new Class[]{
            DepthTransformer.class,
            FadeSlideTransformer.class,
            FlowTransformer.class,
            RotateDownTransformer.class,
            RotateUpTransformer.class,
            ZoomOutSlideTransformer.class,
    };


    public static String[] jicheItems = new String[]{
            "碎修",
            "临修",
            "机破",
            "D类事故",
            "C类及以上事故",
    };
    public static String[] dongcheItems = new String[]{
            "故障修",
            "出库时换车",
            "晚点",
            "安监",
            "下线",
            "D类事故",
            "C类及以上事故",
    };

    public static String[] chengguiItems = new String[]{
            "故障修",
            "晚点",
            "下线",
            "清客",
            "救援",
    };


    //运行模式
    public static String[] jicheyunxingmodelItems = new String[]{
            "单机运行",
            "单机牵引",
            "双机重联",
            "三机重联",
            "无线重联（2+0）",
            "无线重联（1+1）",
            "无线重联（2+1）"
    };
    public static String[] dongcheyunxingmodelItems = new String[]{
            "单列",
            "双列（前车）",
            "双列（后车）"
    };
    public static String[] chengguiyunxingmodelItems = new String[]{
            "ATO",
            "司机操作",
            "紧急运行"

    };


    public static String[] jicheshebeiItems = new String[]{
            "【TCMS】网络系统",
            "【TC1】牵引变流器1",

            "【TC2】牵引变流器2",

            "【TC3】牵引变流器3",

            "【APU1】辅助变流器1",

            "【APU2】辅助变流器2",

            "【TPS1】1路列车供电装置",

            "【TPS2】2路列车供电装置",

            "【OCE】无线重联控制单元",

            "【DTE】无线数据传输单元",

            "【RDTE】无线电台传输单元",

            "【GDTE】G网数据传输单元",

            "【LDP】CMD系统",

            "【WTD】WTD系统",

            "【6A】6A系统",

            "【FFCCTV】FFCCTB装置",

            "【BCG】充电机",

            "【AS】空调电源",

            "【ATDS】轴温",

            "【AE】直流车微机柜/电子柜",

            "【UR1】1架整流柜",

            "【UR2】2架整流柜",

            "【UR3】3架整流柜",

            "【AVR】励磁恒压调节器"
    };
    public static String[] dongcheshebeiItems = new String[]{
            "【TCMS】网络系统",

            "【TC】牵引变流器",

            "【APU】辅助变流器",

            "【APU3】辅助变流器3",

            "【WTD】WTD系统",

            "【BCG】充电机",

            "【ATDS】轴温",

            "【BIDS】转向架失稳检测装置",

            "【WNDS】转向架平稳检测装置",

            "【ACD】换气装置",

            "【AS】空调电源",

            "【LED】乘客信息显示器"
    };

    public static String[] chengguishebeiItems = new String[]{
            "【TCMS】网络系统",

            "【TC】牵引变流器",

            "【APU】辅助变流器",

            "【BCG】充电机"
    };


    public static ExpandableItem[] expandableItems = new ExpandableItem[]{
            ExpandableItem.of(new AdapterItem("机车", R.mipmap.jiche)).addChild(AdapterItem.arrayof(jicheItems)),
            ExpandableItem.of(new AdapterItem("动车", R.mipmap.dongche)).addChild(AdapterItem.arrayof(dongcheItems)),
            ExpandableItem.of(new AdapterItem("城轨", R.mipmap.chenggui)).addChild(AdapterItem.arrayof(chengguiItems))
    };
    public static ExpandableItem[] guzhangshebeiitems = new ExpandableItem[]{
            ExpandableItem.of(new AdapterItem("机车", R.mipmap.jiche)).addChild(AdapterItem.arrayof(jicheshebeiItems)),
            ExpandableItem.of(new AdapterItem("动车", R.mipmap.dongche)).addChild(AdapterItem.arrayof(dongcheshebeiItems)),
            ExpandableItem.of(new AdapterItem("城轨", R.mipmap.chenggui)).addChild(AdapterItem.arrayof(chengguishebeiItems))
    };
    public static ExpandableItem[] yunxingmodelitems = new ExpandableItem[]{
            ExpandableItem.of(new AdapterItem("机车", R.mipmap.jiche)).addChild(AdapterItem.arrayof(jicheyunxingmodelItems)),
            ExpandableItem.of(new AdapterItem("动车", R.mipmap.dongche)).addChild(AdapterItem.arrayof(dongcheyunxingmodelItems)),
            ExpandableItem.of(new AdapterItem("城轨", R.mipmap.chenggui)).addChild(AdapterItem.arrayof(chengguiyunxingmodelItems))
    };


    /**
     * 拆分集合
     *
     * @param <T>
     * @param resList 要拆分的集合
     * @param count   每个集合的元素个数
     * @return 返回拆分后的各个集合
     */
    public static <T> List<List<T>> split(List<T> resList, int count) {
        if (resList == null || count < 1) {
            return null;
        }
        List<List<T>> ret = new ArrayList<>();
        int size = resList.size();
        if (size <= count) { //数据量不足count指定的大小
            ret.add(resList);
        } else {
            int pre = size / count;
            int last = size % count;
            //前面pre个集合，每个大小都是count个元素
            for (int i = 0; i < pre; i++) {
                List<T> itemList = new ArrayList<>();
                for (int j = 0; j < count; j++) {
                    itemList.add(resList.get(i * count + j));
                }
                ret.add(itemList);
            }
            //last的进行处理
            if (last > 0) {
                List<T> itemList = new ArrayList<>();
                for (int i = 0; i < last; i++) {
                    itemList.add(resList.get(pre * count + i));
                }
                ret.add(itemList);
            }
        }
        return ret;
    }


    public static Collection<String> getDemoData() {
        return Arrays.asList("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
    }

    public static Collection<String> getDemoData1() {
        return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18");
    }


    /**
     * 获取时间段
     *
     * @param interval 时间间隔（分钟）
     * @return
     */
    public static String[] getTimePeriod(int startHour, int totalHour, int interval) {
        String[] time = new String[totalHour * 60 / interval];
        int point, hour, min;
        for (int i = 0; i < time.length; i++) {
            point = i * interval + startHour * 60;
            hour = point / 60;
            min = point - hour * 60;
            time[i] = (hour < 9 ? "0" + hour : "" + hour) + ":" + (min < 9 ? "0" + min : "" + min);
        }
        return time;
    }

}
