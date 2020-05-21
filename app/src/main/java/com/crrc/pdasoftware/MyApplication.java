package com.crrc.pdasoftware;

import android.app.Application;
import android.content.Context;

import com.xuexiang.xui.XUI;
import com.xuexiang.xutil.XUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class MyApplication extends Application {
    public static String scantv1 = "";
    public static String scantv2 = "";


    public boolean showDia;
    //    private List<FtpUtils.wxhFile> downlist;
//    Map<String, DownloadindBean> downloadingMap;
    //保存所有的下载任务队列
    ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue();
    //下载任务队列，执行一个任务，则从中去掉一个
    ConcurrentLinkedQueue queueDonotdownloading = new ConcurrentLinkedQueue();

    ConcurrentLinkedQueue queueSelect = new ConcurrentLinkedQueue();

    //下载过的任务放在一个队列
    ConcurrentLinkedQueue queueHavedownloading = new ConcurrentLinkedQueue();

    //记录当前app存活时，下载按钮的点击次数
    int btCount = 0;

    int downloadingFileCounts = 0;//记录当前选择的任务个数
    int haveDownloadFileCounts = 0;//记录当前选择的任务中，已下载的个数


    private static Context context;
    HashMap hashmap = null;
    public int check = -1;
    public int checkfail = -1;

    //记录跳转到下载列表activity列表时，最后展示下载勾选列表界面的信息
    String info[] = new String[5];

    //记录下载显示列表activity是否退出了
    public boolean activityBack = true;//默认退出

    //记录每次点击文件夹时，当前的路径。便于返回上级目录使用
    List<String> backDir = new ArrayList<>();


    //记录点击全选时，当前列表所有的文件的个数
    public int selectAllFileCount = 0;

    //存储所有勾选的文件夹的完整远程路径 记得在ftputils构造函数中clear
    static List<String> somedirlist = new ArrayList<>();

    //记录添加的所有的任务是否执行完毕 默认没有执行完
    public boolean threadIsComple = false;

    public int allTask = 0;//所有的任务个数，包含多次重复点击下载的
    //所有的已下载任务个数，包含多次重复点击下载的
    public CopyOnWriteArrayList<String> allTaskdowned = new CopyOnWriteArrayList<>();
    CopyOnWriteArrayList<String> countExeSuccess = new CopyOnWriteArrayList<>();//计算当前线程池已下载成功的文件个数（仅有成功的）

    boolean againDown = false;
    boolean neterror = false;

    public MyApplication() {
        hashmap = new HashMap();

    }

    public static void setScanStringtv1(String tv1) {
        scantv1 = tv1;
    }

    public static void setScanStringtv2(String tv2) {
        scantv2 = tv2;
    }

    public static String getScanStringtv1() {
        return scantv1;
    }

    public static String getScanStringtv2() {
        return scantv2;
    }


    public void setneterror(boolean net) {
        neterror = net;
    }

    public boolean getneterror() {
        return neterror;
    }

    //重新下载失败的任务的开关  为true表示要出发监听器去下载失败的任务。
    public void setagainDown(boolean againDowns) {
        againDown = againDowns;
    }

    public boolean getagainDown() {
        return againDown;
    }

    public void setSuccessDown() {
        countExeSuccess.add("t");
    }

    public void clearSuccessDown() {
        countExeSuccess.clear();
    }

    public int getSuccessDownSize() {
        return countExeSuccess.size();
    }


    //下载失败的的任务放在一个队列
    ConcurrentLinkedQueue failDown = new ConcurrentLinkedQueue();

    public void setallTask(int all) {
        allTask = all;
    }


    public int getallTask() {
        return allTask;
    }

    public void clearallTask() {
        allTask = 0;
    }

    public void setallTaskDowned(String file) {
//        int size = allTaskdowned.size();
//
//        if (size > 0) {
//            for (int i = 0; i < size; i++) {
//                if (!allTaskdowned.contains(file)) {//去重
//                    allTaskdowned.add(file);
//                }
//            }
//        } else {
//            allTaskdowned.add(file);
//        }

        allTaskdowned.add(file);
    }

    public int getallTaskDowned() {
        return allTaskdowned.size();
    }

    public void clearallTaskDowned() {
        allTaskdowned.clear();
    }

    public void setShowDia(boolean fl) {
        showDia
                = fl;
    }

    public boolean getShowDia() {
        return showDia;
    }

    //记录单次添加的任务的线程是否执行完毕 默认没有执行完
    public void setTaskIsComple(boolean b) {
        threadIsComple = b;
    }

    public boolean getTaskIsComple() {
        return threadIsComple;
    }

    //存储所有勾选的文件夹的完整远程路径 记得在ftputils构造函数中clear
    public void setDirPathList(String dirpath) {
        if (!somedirlist.contains(dirpath)) {
            somedirlist.add(dirpath);
        }
    }

    public void clearDirPathList() {
        somedirlist.clear();
    }

    public List<String> getDirPathList() {
        return somedirlist;
    }

    //记录点击全选时，当前列表所有的文件的个数

    public void setAllCount(int all) {
        selectAllFileCount = all;
    }

    public void setAllCountPlus() {
        ++selectAllFileCount;
        System.out.println("确实点击了+++:" + selectAllFileCount);
    }

    public void setAllCountJian() {
        --selectAllFileCount;
        System.out.println("确实点击了---:" + selectAllFileCount);

    }

    public int getAllCount() {
        return selectAllFileCount;
    }


    //记录路径
    public void setPath(String path) {
        if (!backDir.contains(path)) {
            backDir.add(path);
        }
    }

    public List<String> getPath() {
        return backDir;
    }

    public void removeOne() {
        if (backDir.size() - 1 >= 0) {
            backDir.remove(backDir.size() - 1);
        }
    }

    //当退出selectActivity时，调用该方法，清楚list
    public void clearPath() {
        backDir.clear();
    }

    //设置下载显示列表activity是否退出
    public void setBackAc(boolean back) {
        activityBack = back;
    }

    public boolean getBackAc() {
        return activityBack;
    }

    //将数组置空
    public String[] resetArray(String[] a) {
        String[] b2 = new String[a.length];
        for (int i = 0; i < a.length; i++) {
            a[i] = b2[i];
        }
        return a;
    }

    public void giveValueArray(String[] a) {
        for (int i = 0; i < a.length; i++) {
            this.info[i] = a[i];
        }
    }

    //最后的勾选列表的信息
    public void setLastInfo(String info[]) {
        int size = info.length;
        if (size >= 0) {
            resetArray(this.info);//先清空
            giveValueArray(info);//赋值
        }
    }

    public String[] getInfo() {
        return this.info;
    }

    //下载按钮点击
    public void setBtnCount() {
        btCount++;
    }

    public int getBtCount() {
        return btCount;
    }

    //设置 获取 需下载的任务个数
    public void setDownloadingFileCounts(int counts) {
        this.downloadingFileCounts = counts;
    }

    public int getDownloadingFileCounts() {
        return this.downloadingFileCounts;
    }

    //设置 获取 需下载的任务中的已下载的任务个数
    public void sethaveDownloadingFileCounts(int counts) {
        this.haveDownloadFileCounts = counts;
    }

    public int gethaveDownloadingFileCounts() {
        return this.haveDownloadFileCounts;
    }


//    public void setDownlist(List<FtpUtils.wxhFile> downlist) {
//        this.downlist = downlist;
//    }
//
//    public List<FtpUtils.wxhFile> getDownlist() {
//        return this.downlist;
//    }
//
//    public void setDownBean(Map<String, DownloadindBean> downloadingMap) {
//        this.downloadingMap = downloadingMap;
//    }
//
//    public Map<String, DownloadindBean> getDownBean() {
//        return this.downloadingMap;
//    }

    //下载任务队列
    public void setQueue(List<String> downloadingList) {
        if (downloadingList.size() > 0) {
            for (int i = 0; i < downloadingList.size(); i++) {
//                if (!queue.contains(downloadingList.get(i))) {
                if (downloadingList.get(i) != null) {
                    queue.offer(downloadingList.get(i));
                    queueDonotdownloading.offer(downloadingList.get(i));
                }

//                }
            }
        }

    }

    public void setQueueSelect(String downloadingList) {
        if (downloadingList != null) {
            queueSelect.offer(downloadingList);
        }
    }

    public ConcurrentLinkedQueue getQueueSelect() {
        return queueSelect;
    }

    public void clearQueueSelect(List<String> ls) {
        if (queueSelect.size() == ls.size()) {
            queueSelect.clear();
        } else {
            if (ls.size() > 0) {
                for (int i = 0; i < ls.size(); i++) {
                    if (queueSelect != null) {
                        if (queueSelect.contains(ls.get(i))) {
                            queueSelect.remove(ls.get(i));
                        }
                    }

                }
            }
        }
    }

    public void setQueueDowning(List<String> failList) {
        if (failList.size() > 0) {
            for (int i = 0; i < failList.size(); i++) {
                if (failList.get(i) != null) {
                    queueDonotdownloading.offer(failList.get(i));
                }
            }
        }
    }


    public ConcurrentLinkedQueue getQueue() {
        return this.queue;
    }

    //清空下载列表中显示的所有文件
    public void clearQueueList() {
        this.queue.clear();

    }

    //从队列头取出一个任务,并删除
    public void removeOneQueueDownloading() {
        Object obj = this.queueDonotdownloading.poll();
        if (obj != null) {
            queueHavedownloading.add(obj);//保存已经下载过的
        }
    }

    public void clearQueueDownloading() {
        this.queueDonotdownloading.clear();

    }

    //下载失败任务队列
    public void setQueueFail(List<String> fail) {
        if (fail.size() > 0) {
            for (int i = 0; i < fail.size(); i++) {
                if (!failDown.contains(fail.get(i))) {
                    failDown.offer(fail.get(i));
                }
            }
        }

    }

    public ConcurrentLinkedQueue getQueueFail() {
        return failDown;
    }

    public void clearQueueFailDirectly() {
        failDown.clear();

    }

    public void clearQueueFail(List<String> rm) {
        if (failDown.size() == rm.size()) {
            failDown.clear();
        } else {
            if (rm.size() > 0) {
                for (int i = 0; i < rm.size(); i++) {
                    if (failDown != null) {
                        if (failDown.contains(rm.get(i))) {
                            failDown.remove(rm.get(i));
                        }
                    }

                }
            }
        }


    }

    public List<String> getHaveDownList() {
        List<String> list = new ArrayList<>();
        Iterator<String> iterator = queueHavedownloading.iterator();
        while (iterator.hasNext()) {
            String filename = iterator.next();
            //存带路径的文件名，仅有文件名的话，当不同的路径下有相同的文件名时，会被误判为同一个文件
//            filename = filename.substring(filename.lastIndexOf('/') + 1);
            list.add(filename);
        }
        return list;
    }

    public ConcurrentLinkedQueue getQueueDownloading() {
        return this.queueDonotdownloading;
    }


    public void setCheck(int check) {
        this.check = check;
    }

    public int getCheck() {
        return check;
    }

    public void setCheckfail(int checkfail) {
        this.checkfail = checkfail;
    }

    public int getCheckfail() {
        return checkfail;
    }

    public HashMap getSucFail() {
        return hashmap;
    }

    public void setSucFail(int position, String sucfail) {
        this.hashmap.put(position, sucfail);
    }

    @Override
    public void onCreate() {

        super.onCreate();
        XUI.init(this); //初始化UI框架
        XUI.debug(true);  //开启UI框架调试日志
        XUtil.init(this);
        context = getApplicationContext();

    }

    public static Context getContext() {
        return context;
    }


}
