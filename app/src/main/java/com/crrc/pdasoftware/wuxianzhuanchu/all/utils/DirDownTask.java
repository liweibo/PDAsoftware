package com.crrc.pdasoftware.wuxianzhuanchu.all.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.crrc.pdasoftware.MyApplication;

import java.util.ArrayList;
import java.util.List;


public class DirDownTask {

    Context mContext;
    public static ProgressDialog pdialog;
    String remoteDirPath = "";//文件夹的远程路径
    String localPath = "";//文件夹的下载到本地的路径

    private static long numberTotalsize = 0;
    private static int countValue = 0;
    private static String downloadingFileName = "";
    private static int currentDowncount = 0;//已下载文件个数

    private String strencoding;

    static String host;
    static String user;
    static String pass;
    static int port;
    static String portStr;

    public static double currentSize = 0;//当前已下载的大小
    public static String globalTotalFileSize = "";
    public boolean allSele = false;
    public List<String> dirLi = new ArrayList<>();
    public List<String> fileLi = new ArrayList<>();
    static MyApplication appss;

    public DirDownTask(Context ctx, String remoteDirPath, String localPath,
                       boolean allselect, List<String> dir, List<String> file, MyApplication appss) {//传入文件夹的远程路径
        mContext = ctx;
        this.remoteDirPath = remoteDirPath;
        this.localPath = localPath;
        dirLi.clear();
        fileLi.clear();

        allSele = allselect;
        dirLi.addAll(dir);
        fileLi.addAll(file);
        this.appss = appss;
        this.appss.clearDirPathList();

    }

    public void doInBackground(String[] Params) {
        host = Params[0];
        portStr = Params[1];
        user = Params[2];
        pass = Params[3];
        FtpUtils.connectServer(host, 21, user, pass, "");

        SharedPreferences sp = mContext.getSharedPreferences("filedirname",
                Activity.MODE_PRIVATE);
        String dir1 = sp.getString("filedirnamevalue", "");
        String dir2 = sp.getString("shebeinamevalue", "");

//        localPath = FtpUtils.crSDFile(strdir);//在本地新建文件夹 建的文件夹到用户点击的当前界面显示的为止

        String genFolder8 = Environment.getExternalStorageDirectory().getPath() +
                "/CRRC" + "/DOWNLOAD/";
        genFolder8 = genFolder8 +"/"+dir1 + "/"+dir2+"/";
        System.out.println("传的路径：" + genFolder8);

        if (dirLi.size() == 0 && fileLi.size() > 0) {
            new FtpUtils(mContext, allSele, dirLi, fileLi, appss).downloadFile(remoteDirPath);
        }
        if (dirLi.size() > 0) {
            new FtpUtils(mContext, allSele, dirLi, fileLi, appss).downloadFolder(remoteDirPath, genFolder8);//这里面的文件夹下载，如果下一级还有文件夹，还会继续在本地创建文件夹
        }
    }

//
//    public boolean download(File localFile, FTPFile ftpFile, FTPClient ftpClient) {
//        boolean suc = false;
//        try {
//            double process = 0;//进度值
//            double step;
//            long localSize = 0;
//            // 进度
//            //当文件特别小时 做如下处理
//            if (numberTotalsize < 100) {
//                step = numberTotalsize * 0.01;
//            } else {
//                step = numberTotalsize / 100;
//                System.out.println("step的值：" + step);
//            }
//            localSize = localFile.length();
//
//            //存在且完整
//            if (localFile.exists() && (localSize >= ftpFile.getSize())) {
//                currentDowncount++;
//                currentSize += ftpFile.getSize();
//                if (currentSize / step != process) {
//                    process = currentSize / step;//下载的百分比
//                    countValue = (int) process;//全局的下载进度
//                    System.out.println("进度值：" + (long) process);
//                    publishProgress((long) process);
//                    downloadingFileName = ftpFile.getName();//全局的下载filename
//                }
//            }
//            //存在不完整，或者不存在
//            else {
//                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
//                ftpClient.setRestartOffset(localSize);//断点续传的开始点
//                OutputStream out = new FileOutputStream(localFile, true);
//                InputStream input = ftpClient.retrieveFileStream(ftpFile.getName());
//                byte[] b = new byte[4096];
//                int length = 0;
//                while (((length = input.read(b)) != -1)) {
//                    out.write(b, 0, length);//写文件
//                    //记得加上 localSize大小。-------------
//                    currentSize = currentSize + localSize + length;
//                    //-------------------------------------
//                    System.out.println("当前已下载的大小：" + currentSize);
//
//                    if (currentSize / step != process) {
//                        process = currentSize / step;//下载的百分比
//                        countValue = (int) process;//全局的下载进度
//                        System.out.println("进度值：" + (long) process);
//                        publishProgress((long) process);
//                        downloadingFileName = ftpFile.getName();//全局的下载filename
//                    }
//                }
//                out.flush();
//                out.close();
//                input.close();
//
//                // 此方法是来确保流处理完毕，如果没有此方法，可能会造成现程序死掉
//                if (ftpClient.completePendingCommand()) {
//                    suc = true;
//                    //统计已下载的文件个数 并显示
//                    ++currentDowncount;
//                } else {
//                    //单个文件下载流失败
//                    return false;
//                }
//
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        } finally {
//            return suc;
//        }
//
//    }


}