package com.crrc.pdasoftware.wuxianzhuanchu.all.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemChooseDataFail {
    public static List<String> listFilePath = new ArrayList<>();

    public static synchronized void addFilePath(String filepath) {
        listFilePath.add(filepath);
//        System.out.println("添加下载路径：" + listFilePath.toString());
    }

    public static synchronized List<String> getFilePath() {//去重  异步处理
        List<String> newList = new ArrayList<String>();
        for (String cd : listFilePath) {
            if (!newList.contains(cd)) {
                newList.add(cd);
            }
        }
        listFilePath.clear();
        listFilePath.addAll(newList);
//        System.out.println("listFilePath变了没:" + listFilePath.toString());
        return newList;
    }


    public static synchronized void removeFilePath(int i, List<String> lsfail) {
        if (listFilePath.size() > 0) {
            //不能按序号直接删，应根据序号先找出lsfail中对应的文件的路径，再删除该路径
            String s1 =lsfail.get(i);

            int x1 = listFilePath.indexOf(s1);//当x1为-1时 代表 s1不在listFilePath中
            if (x1 != -1) {
                Iterator<String> iterator = listFilePath.iterator();
                while (iterator.hasNext()) {
                    String item = iterator.next();
                    if (item.equals(s1)) {
                        System.out.println(item + "========" + s1);
                        iterator.remove();
                    }
                }
            }
        }
    }

    public static synchronized void clearFilePath() {
        listFilePath.clear();
    }


}
