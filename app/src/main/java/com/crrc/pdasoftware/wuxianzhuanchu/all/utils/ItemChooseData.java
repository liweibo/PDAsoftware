package com.crrc.pdasoftware.wuxianzhuanchu.all.utils;

import com.crrc.pdasoftware.wuxianzhuanchu.all.activitys.selectFileActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

public class ItemChooseData {
    public static List<String> listFilePath = new ArrayList<>();
    public static LinkedHashMap<String, Boolean> pathAndIsfile = new LinkedHashMap<>();
    public static List<String> listFilename = new ArrayList<>();
    public static List<String> fileDownloadSucOrFailName = new ArrayList<>();
    public static List<Boolean> fileDownloadSucOrFailTrueOrfalse = new ArrayList<>();

    public static synchronized void addDownloadSucOrFail(String file, boolean sucOrFail) {//记录文件是否下载成功
        fileDownloadSucOrFailName.add(file);
        fileDownloadSucOrFailTrueOrfalse.add(sucOrFail);
    }

    //记录勾选的文件/文件夹 对应的路径，是否是 文件
    public static synchronized void addPathAndIsfile(String path, boolean isfile) {
        if (!pathAndIsfile.containsKey(path))
            pathAndIsfile.put(path, isfile);
    }

    public static synchronized void removePathAndisfile(int i) {//传入删除的listview的位置值
        if (pathAndIsfile.size() > 0) {
            //不能按序号直接删，应根据序号先找出currentFiles中对应的文件的路径，再删除该路径
            String s1 = selectFileActivity.currentFiles.get(i).filePath;
            boolean contains = pathAndIsfile.containsKey(s1);
            if (contains) {
                pathAndIsfile.remove(s1);
            }
            System.out.println("remove过后的下载路径列表：" + pathAndIsfile.keySet().toString());
        }
    }

    public static synchronized void clearPathAndisfile() {
        pathAndIsfile.clear();
    }


    public static synchronized HashMap<String, Boolean> getPathAndIsfile() {
        return pathAndIsfile;
    }

    public static synchronized void addFileName(String filename) {
        listFilename.add(filename);
    }

    public static List<String> getFileName() {//去重
        List<String> newList = new ArrayList<String>();
        for (String cd : listFilename) {
            if (!newList.contains(cd)) {
                newList.add(cd);
            }
        }
        listFilename.clear();
        listFilename.addAll(newList);
        return newList;
    }

    public static synchronized void removeFileName(int i) {
        if (listFilename.size() > 0) {
            //不能按序号直接删，应根据序号先找出currentFiles中对应的文件的name
            // ，再删除该路径
            String s1 = selectFileActivity.currentFiles.get(i).filename;

            int x1 = listFilename.indexOf(s1);//这里容易出错
            System.out.println("搜索序号name：" + x1);
            if (x1 != -1) {
//                getFileName().remove(x1);
                Iterator<String> iterator = listFilename.iterator();
                while (iterator.hasNext()) {
                    String item = iterator.next();
                    if (item.equals(s1)) {
                        iterator.remove();
                    }
                }

            }

        }
    }


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


    public static synchronized void removeFilePath(int i) {
        if (listFilePath.size() > 0) {
            //不能按序号直接删，应根据序号先找出currentFiles中对应的文件的路径，再删除该路径
            String s1 = selectFileActivity.currentFiles.get(i).filePath;
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
            System.out.println("remove过后的下载路径列表：" + listFilePath);
        }
    }

    public static synchronized void clearFilePath() {
        listFilePath.clear();
    }

    public static synchronized void clearFileName() {
        listFilename.clear();
    }

}
