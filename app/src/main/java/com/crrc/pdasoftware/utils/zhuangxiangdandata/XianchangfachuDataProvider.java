package com.crrc.pdasoftware.utils.zhuangxiangdandata;

import java.util.ArrayList;
import java.util.List;

/**
 * 演示数据
 *
 * @author xuexiang
 * @since 2018/11/23 下午5:52
 */
public class XianchangfachuDataProvider {

   
   
    public static List<XianchangfachuDataInfo> getXianchangfachuListNewInfos() {
        List<XianchangfachuDataInfo> list = new ArrayList<>();

        list.add(new XianchangfachuDataInfo("ZX-ZX-2020-8766", "2018-02-02 19:37:22","未处理",
                "现场库","邓生")
                );
        list.add(new XianchangfachuDataInfo("ZX-ZX-2020-8799", "2010-03-02 19:13:22","未处理",
                "现场库","王婷")
                );
        list.add(new XianchangfachuDataInfo("ZX-ZX-2020-8761", "2011-09-02 19:07:22","未处理",
                "现场库","冯学博")
                );
        list.add(new XianchangfachuDataInfo("ZX-ZX-2020-8666", "2019-07-02 19:37:22","未处理",
                "现场库","利德曼")
                );
        list.add(new XianchangfachuDataInfo("ZX-ZX-2020-8000", "2002-01-09 19:29:22","未处理",
                "现场库","旺春")
                );
        list.add(new XianchangfachuDataInfo("ZX-ZX-2020-8999", "1992-02-08 17:33:22","未处理",
                "现场库","李四")
                );

       return list;
    }



}
