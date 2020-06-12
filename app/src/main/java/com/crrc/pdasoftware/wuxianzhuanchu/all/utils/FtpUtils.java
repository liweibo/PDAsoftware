package com.crrc.pdasoftware.wuxianzhuanchu.all.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Looper;

import com.crrc.pdasoftware.MyApplication;
import com.crrc.pdasoftware.utils.XToastUtils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class FtpUtils {
    public static FTPClient ftpClient;
    private static String localDirectoryPath;
    private static String beginPath;
    private String strencoding;

    static String host;
    static String user;
    static String pass;
    static int port;
    static String portStr;
    static Context mContext;
    static long dirFileTotalSize = 0;
    static int fileCount = 0;
    public static boolean allSele = false;
    public static List<String> dirLi = new ArrayList<>();
    public static List<String> fileLi = new ArrayList<>();
    /**
     * FTP根目录.
     */
    public static final String REMOTE_PATH = "/";

    /**
     * FTP当前目录.
     */
    private static String currentPath = "";

    static LinkedList<String> somedirlist = new LinkedList<>();

    public static boolean fir = false;
    static MyApplication apps;

    public FtpUtils(Context context, boolean allselect, List<String> dir, List<String> file, MyApplication apps) {
        this.mContext = context;
        dirLi.clear();
        fileLi.clear();
        this.apps = apps;
        allSele = allselect;
        dirLi.addAll(dir);
        fileLi.addAll(file);
        dirLi.addAll(fileLi);
        somedirlist.clear();
        this.apps.clearDirPathList();
        fir = false;

    }


    public static FTPClient connectServer(String ip, int port, String userName, String userPwd, String path) {
        ftpClient = new FTPClient();
        ftpClient.setDefaultTimeout(6 * 1000);
        ftpClient.setConnectTimeout(6 * 1000);
        ftpClient.setDataTimeout(6000);

        ftpClient.configure(new FTPClientConfig(FTPClientConfig.SYST_UNIX));
        ftpClient.setAutodetectUTF8(true);//中文文件夹中的数据没法下载 需要该设置。
        try {
            // 连接
            ftpClient.connect(ip, port);
            // 登录
            ftpClient.login(userName, userPwd);
            if (ftpClient != null) {
                ftpClient.setControlEncoding("UTF-8");
            }
            if (path != null && path.length() > 0) {
                // 跳转到指定目录
                ftpClient.changeWorkingDirectory(path);
            }
        } catch (SocketException e) {
            ftpClient = null;
            System.out.println("链接出错了==21====");
            e.printStackTrace();
        } catch (IOException e) {
            ftpClient = null;
            System.out.println("链接出错了==2111111====");
            e.printStackTrace();
        }
        return ftpClient;
    }


    //gettask  backtask中使用
    public static boolean connectServerTask(String ip, int port, String userName, String userPwd, String path) {

        try {
            ftpClient = new FTPClient();
            ftpClient.configure(new FTPClientConfig(FTPClientConfig.SYST_UNIX));
            ftpClient.setAutodetectUTF8(true);//中文文件夹中的数据没法下载 需要该设置。
            ftpClient.setDataTimeout(6000);
            ftpClient.setDefaultTimeout(6 * 1000);
            ftpClient.setConnectTimeout(6 * 1000);
            // 连接
            ftpClient.connect(ip, port);

            // 登录
            ftpClient.login(userName, userPwd);
            ftpClient.setControlEncoding("UTF-8");
            if (path != null && path.length() > 0) {
                // 跳转到指定目录
                ftpClient.changeWorkingDirectory(path);
            }
        } catch (Exception e) {
            System.out.println("连接异常打印,可能是超时---");
//            e.printStackTrace();
            return false;
        }
        return true;


    }

    /**
     * @throws IOException function:关闭连接
     */
    public static void closeServer() {
        if (ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param path
     * @return function:读取指定目录下的文件名
     * @throws IOException
     */
    public List<wxhFile> getFileList(String path) throws ParseException {
        List<wxhFile> fileLists = new ArrayList<wxhFile>();
        List<wxhFile> fileListsNoSpace = new ArrayList<wxhFile>();
        // 获得指定目录下所有文件名
        FTPFile[] ftpFiles = null;
        ftpClient.configure(new FTPClientConfig(FTPClientConfig.SYST_UNIX));
        FTPFile[] listedDirectories = null;

        System.out.println("path的值：" + path);
        //path的值为当前点击的文件夹的完整路径。
        if (path.contains(" ")) {
            try {//以下几句代码的作用是为了避免该问题：当文件夹命名中带有空格无法进入该文件夹目录。
                ftpClient.cwd(path);
                listedDirectories = ftpClient.listFiles();
                ftpClient.cdup();
            } catch (IOException e) {
                e.printStackTrace();
            }

            int dotsSpace = 0;
            for (int i = 0; i < listedDirectories.length; i++) {
                if (listedDirectories[i].getName().equals(".") || listedDirectories[i].getName().equals("..")) {
                    dotsSpace++;
                }
            }
            for (int i = dotsSpace; listedDirectories != null && i < listedDirectories.length; i++) {
                FTPFile file = listedDirectories[i];
                System.out.println("打印测试：" + file.getName());
                if (file.isFile()) {
                    String tmp = file.getName();
                    tmp = tmp.replace("/", "");
                    tmp = tmp.replace("//", "");
                    FtpUtils.wxhFile a = new FtpUtils.wxhFile(path + tmp, tmp,
                            1, fileUtils.setTimeStamp(file.getTimestamp().getTime()), fileUtils.setSize(file.getSize()));
                    fileLists.add(a);
                } else if (file.isDirectory()) {

                    String name = file.getName();
                    if (name.equals(".") || name.equals("..") || name.equals("./")
                            || name.equals("../") || name.equals(".../")) continue;
                    name = name.replace("/", "");
                    name = name.replace("//", "");
                    FtpUtils.wxhFile a = new FtpUtils.wxhFile(path + name, name,
                            0, "", "");
                    fileLists.add(a);
                }
            }
            fileListsNoSpace.clear();
            fileListsNoSpace.addAll(fileLists);

        } else {

            try {
//            ftpClient.enterLocalActiveMode();
//            ftpClient.enterLocalPassiveMode();
                // 更改FTP目录
                ftpClient.changeWorkingDirectory(path);
                // 得到FTP当前目录下所有文件
                ftpFiles = ftpClient.listFiles();
//                ftpFiles = ftpClient.listFiles(path);
                System.out.println(ftpFiles.length + "长度");
            } catch (Exception e) {

                e.printStackTrace();
            }
            for (int i = 0; ftpFiles != null && i < ftpFiles.length; i++) {
                FTPFile file = ftpFiles[i];
                if (file.isFile()) {
                    System.out.println("文件夹下面的文件=====" + file.getName());
                    String tmp = file.getName();
                    tmp = tmp.replace("/", "");
                    tmp = tmp.replace("//", "");
                    wxhFile a = new wxhFile(path + tmp, tmp,
                            1, fileUtils.setTimeStamp(file.getTimestamp().getTime()), fileUtils.setSize(file.getSize()));
                    fileListsNoSpace.add(a);
                } else if (file.isDirectory()) {
                    String name = file.getName();

                    if (name.equals(".") || name.equals("..") || name.equals("./")
                            || name.equals("../") || name.equals(".../")) continue;
                    name = name.replace("/", "");
                    name = name.replace("//", "");
                    System.out.println("文件夹名称为==去掉/后===" + name);

                    wxhFile a = new wxhFile(path + name, name, 0
                            , "", "");
                    fileListsNoSpace.add(a);
                }
            }
        }


        return fileListsNoSpace;
    }

    //获取当前页面文件或文件夹的信息
    public List<FTPFile> getOriginFileList(String path, FTPClient ftpClient) {
        List<FTPFile> fileLists = new ArrayList<>();
        ftpClient.configure(new FTPClientConfig(FTPClientConfig.SYST_UNIX));
        FTPFile[] listedDirectories = null;
        //path的值为当前点击的文件夹的完整路径。
        if (path.contains(" ")) {
            try {//以下几句代码的作用是为了避免该问题：当文件夹命名中带有空格无法进入该文件夹目录。
                ftpClient.cwd(path);
                listedDirectories = ftpClient.listFiles();
                ftpClient.cdup();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int i = 0; listedDirectories != null && i < listedDirectories.length; i++) {
                if (listedDirectories[i].getName().equals(".") || listedDirectories[i].getName().equals("..")) {
                } else {
                    FTPFile file = listedDirectories[i];
                    fileLists.add(file);
                }
            }


        } else {
            try {
                listedDirectories = ftpClient.listFiles(path);
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int i = 0; listedDirectories != null && i < listedDirectories.length; i++) {
                if (listedDirectories[i].getName().equals(".") || listedDirectories[i].getName().equals("..")) {
                } else {
                    FTPFile file = listedDirectories[i];
                    fileLists.add(file);
                }
            }
        }

        return fileLists;
    }

    public List<wxhFile> firstgetFileList() throws ParseException {
        List<wxhFile> fileLists = new ArrayList<wxhFile>();
        // 获得指定目录下所有文件名
        FTPFile[] ftpFiles = null;
        try {
            ftpFiles = ftpClient.listFiles();//区别在这里，没有参数。
            System.out.println(ftpFiles.length + "长度");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; ftpFiles != null && i < ftpFiles.length; i++) {
            FTPFile file = ftpFiles[i];
            if (file.isFile()) {
                System.out.println("文件夹下面的文件first=====" + file.getName());
                wxhFile a = new wxhFile("" + file.getName(), file.getName(), 1, fileUtils.setTimeStamp(file.getTimestamp().getTime())
                        , fileUtils.setSize(file.getSize()));
                fileLists.add(a);
            } else if (file.isDirectory()) {
                String name = file.getName();
                if (name.equals(".") || name.equals("..")) continue;
                System.out.println("文件夹名称为first=====" + name);
                wxhFile a = new wxhFile("" + file.getName(), file.getName(), 0, "", "");
                fileLists.add(a);
            }
        }

        return fileLists;
    }

    /**
     * @param fileName
     * @return function:从服务器上读取指定的文件
     * @throws ParseException
     * @throws IOException
     */
    public String readFile(String fileName) throws ParseException {
        InputStream ins = null;
        StringBuilder builder = null;
        try {
            // 从服务器上读取指定的文件
            ins = ftpClient.retrieveFileStream(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins, strencoding));
            String line;
            builder = new StringBuilder(150);
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                builder.append(line);
            }
            reader.close();
            if (ins != null) {
                ins.close();
            }
            // 主动调用一次getReply()把接下来的226消费掉. 这样做是可以解决这个返回null问题
            ftpClient.getReply();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    /**
     * @param fileName function:删除文件
     */
    public void deleteFile(String fileName) {
        try {
            ftpClient.deleteFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static class wxhFile {
        public String filePath;
        public boolean isFile;
        public String filename;
        public String filetime;
        public String filesize;
        public boolean isParent = false;

        public wxhFile(String path, String name, int flag, String time, String size) {
            if (flag == 0) isFile = false;
            else if (flag == 1) isFile = true;
            else if (flag == 2) {
                isParent = true;
            }
            filePath = path;
            filename = name;
            filesize = size;
            filetime = time;
        }

        boolean ifFile() {
            return isFile;
        }

        String getFileName() {
            return filename;
        }
    }

    public static class ItemFresh {
        public int pos;
        public String sucfail;

        public int getPos() {
            return pos;
        }

        public String getSucfail() {
            return sucfail;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

        public void setSucfail(String sucfail) {
            this.sucfail = sucfail;
        }

        public ItemFresh(int pos, String sucfail) {
            this.pos = pos;
            this.sucfail = sucfail;
        }

    }

    private static long lastClickTime;

    public static boolean isFastClick(long ClickIntervalTime) {
        long ClickingTime = System.currentTimeMillis();
        if (ClickingTime - lastClickTime < ClickIntervalTime) {
            return true;
        }
        lastClickTime = ClickingTime;
        return false;
    }


    public void downloadFile(String remoteDirPath) {
        List<String> ls = new ArrayList<>();
        try {
            ls.clear();
            if (ftpClient==null){
                Looper.prepare();
                XToastUtils.error("请检查网络");
                Looper.loop();
                return;
            }
            ftpClient.changeWorkingDirectory(remoteDirPath);
            FTPFile[] allFile = ftpClient.listFiles();
            for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
                if (!allFile[currentFile].getName().equals("..")
                        && !allFile[currentFile].getName().equals(".") &&
                        !allFile[currentFile].getName().equals("./") &&
                        !allFile[currentFile].getName().equals("../") &&
                        !allFile[currentFile].getName().equals(".../")) {
                    if (!allSele) {//不是全选 才需要做排查
                        if (!dirLi.contains(allFile[currentFile].getName().
                                replace("/", ""))) {
                            continue;
                        }
                    }

                    if (allFile[currentFile].isFile()) {
                        //文件的整个路径（包含文件名）
                        String strremotefilePath = (remoteDirPath + "/" +
                                allFile[currentFile].getName().replace("/", "")).replaceAll("(/)\\/{1,}", "/");
                        System.out.println("当只勾选文件时：" + strremotefilePath);
                        ls.add(strremotefilePath);
                    }
                }
            }
//            apps.setallTask(ls.size());//每次点击下载时，即可添加任务个数到一个变量中汇总。
            //所有的任务个数（包含多次点击添加，包含重复的任务,不包含失败任务）

            apps.setQueue(ls);
            for (int i = 0; i < apps.getQueueDownloading().size(); i++) {
                System.out.println("下载用到的：" + apps.getQueueDownloading().toArray()[i]);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getAllfilepathInEveryDir(String remoteDirPath) {
        List<String> ls = new ArrayList<>();
        try {
            ls.clear();
            if (!remoteDirPath.equals("/")) {
                String[] strdir = remoteDirPath.split("/");
                crSDFile(strdir);//为每个出现的文件夹的路径都先创建本地文件夹，防止空文件夹不创建
            }
            ftpClient.changeWorkingDirectory(remoteDirPath);
            FTPFile[] allFile = ftpClient.listFiles();
            for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
                if (!allFile[currentFile].getName().equals("..")
                        && !allFile[currentFile].getName().equals(".") &&
                        !allFile[currentFile].getName().equals("./") &&
                        !allFile[currentFile].getName().equals("../") &&
                        !allFile[currentFile].getName().equals(".../")) {

                    if (allFile[currentFile].isFile()) {
                        //文件的整个路径（包含文件名）
                        String strremotefilePath = (remoteDirPath + "/" +
                                allFile[currentFile].getName().replace("/", "")).replaceAll("(/)\\/{1,}", "/");
                        ls.add(strremotefilePath);
                    }
                }
            }
//            apps.setallTask(ls.size());//每次点击下载时，即可添加任务个数到一个变量中汇总。
            //所有的任务个数（包含多次点击添加，包含重复的任务,不包含失败任务）

            apps.setQueue(ls);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getAllfilepathInEveryDirLastOne(String remoteDirPath) {
        List<String> ls = new ArrayList<>();
        try {
            ls.clear();
            if (!remoteDirPath.equals("/")) {
                String[] strdir = remoteDirPath.split("/");
                crSDFile(strdir);//为每个出现的文件夹的路径都先创建本地文件夹，防止空文件夹不创建
            }
            ftpClient.changeWorkingDirectory(remoteDirPath);
            FTPFile[] allFile = ftpClient.listFiles();
            for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
                if (!allFile[currentFile].getName().equals("..")
                        && !allFile[currentFile].getName().equals(".") &&
                        !allFile[currentFile].getName().equals("./") &&
                        !allFile[currentFile].getName().equals("../")
                        &&
                        !allFile[currentFile].getName().equals(".../")) {

//                    如在顶目录：
//                    会生成该目录：“/”
//                    那么循环时，会取出/ 下的所有文件，
//                    但是，并不是需要所有的文件，只需要勾选的文件，此时 循环取出/ 下的所有文件时应该做一个判断，
//                    是否是勾选文件中的。
                    if (!allSele) {//不是全选 才需要做排查
                        if (!dirLi.contains(allFile[currentFile].getName().replace("/", ""))) {
                            continue;
                        }
                    }
                    if (allFile[currentFile].isFile()) {
                        //文件的整个路径（包含文件名）
                        String strremotefilePath = (remoteDirPath + "/" +
                                allFile[currentFile].getName().replace("/", "")).replaceAll("(/)\\/{1,}", "/");
                        ls.add(strremotefilePath);
                        System.out.println("添加任务时==========：" + strremotefilePath);
                    }
                }
            }
//            apps.setallTask(ls.size());//每次点击下载时，即可添加任务个数到一个变量中汇总。
            //所有的任务个数（包含多次点击添加，包含重复的任务,不包含失败任务）

            apps.setQueue(ls);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 下载整个目录
     *
     * @param remotePath FTP目录
     * @param localPath  本地目录
     * @return Result 成功下载的文件数量
     * @throws IOException
     */
    public static void downloadFolder(String remotePath, String localPath) {
        try {
            localDirectoryPath = (localPath + "/" + remotePath.substring(remotePath.lastIndexOf("/"))).replaceAll("(/)\\/{1,}", "/");
            //以下两句代码结合 ftpClient.retrieveFile 可以避免文件名带有空格的问题
            System.out.println("测试remotePath：" + remotePath);
            ftpClient.changeWorkingDirectory(remotePath);
            FTPFile[] allFile = ftpClient.listFiles();
            for (int i = 0; i < allFile.length; i++) {
                System.out.println("测试allFile：" + allFile[i]);
                System.out.println("测试allFile名称：" + allFile[i].getName());
                System.out.println("测试allFile名称：" + allFile[i].isDirectory());
            }
            for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
                if (!allFile[currentFile].getName().equals("..")
                        && !allFile[currentFile].getName().equals(".") &&
                        !allFile[currentFile].getName().equals("./") &&
                        !allFile[currentFile].getName().equals("../")
                        &&
                        !allFile[currentFile].getName().equals(".../")) {
                    if (!allSele) {//不是全选 才需要做排查
                        if (!dirLi.contains(allFile[currentFile].getName().replace("/", ""))) {
                            continue;
                        }
                    }

                    if (allFile[currentFile].isDirectory()) {
                        String strremoteDirectoryPath = (remotePath + "/" +
                                allFile[currentFile].getName().replace("/", ""))
                                .replaceAll("(/)\\/{1,}", "/");
                        System.out.println("记录：" + strremoteDirectoryPath + "/");
                        apps.setDirPathList(strremoteDirectoryPath + "/");//每次进入的文件夹的路径 都存入队列
                        //只要第二次，三次....进入下一个文件夹，则记录该文件夹中的文件名/文件夹名，便于上面的continue做排查
                        dosome(strremoteDirectoryPath);
                        downloadFolder(strremoteDirectoryPath, localDirectoryPath);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


//        // 循环遍历
//        for (FTPFile ftpFile : ftpFiles) {
//            if (!ftpFile.getName().equals("..")
//                    && !ftpFile.getName().equals(".") &&
//                    !ftpFile.getName().equals("./") &&
//                    !ftpFile.getName().equals(".//")) {
//
//
////                if (!allSele) {//不是全选 才需要做排查
////                    if (!dirLi.contains(ftpFile.getName())) {
////                        continue;
////                    }
////                }
//
//                if (ftpFile.isDirectory()) {
//                    //只要第二次，三次....进入下一个文件夹，则记录该文件夹中的文件名/文件夹名，便于上面的continue做排查
////                    ftpClient.changeWorkingDirectory(currentPath + "/" + ftpFile.getName());
////                    FTPFile[] myftpFiles = ftpClient.listFiles();
////                    if(myftpFiles.length>0){
////                    for (FTPFile oneftpFile : myftpFiles) {
////                        if (!oneftpFile.getName().equals("..")
////                                && !oneftpFile.getName().equals(".") &&
////                                !oneftpFile.getName().equals("./") &&
////                                !oneftpFile.getName().equals(".//")) {
////                            dirLi.add(oneftpFile.getName());
////                        }}
////                    }
//
//
//                    System.out.println("到下层文件夹时的远程路径：" + (currentPath + "/" + ftpFile.getName()).replaceAll("(/)\\/{1,}", "/"));
//                    System.out.println("到下层文件夹时的对应本地存储路径：" + localPath);
//                    //下载文件夹
//                    downloadFolder((currentPath + "/" + ftpFile.getName()).replaceAll("(/)\\/{1,}", "/"), localPath);
//                } else if (ftpFile.isFile()) {
//                    // 下载单个文件
////                    boolean flag = downloadSingle(new File(localPath + "/" + ftpFile.getName()), ftpFile);
//
////                    new DirDownTask(mContext, "", "", allSele, dirLi, fileLi).
////                            download(new File(localPath + "/" + ftpFile.getName()),
////                                    ftpFile, ftpClient);
//
////                    System.out.println(
////                            "递归-文件-的路径;"+localPath + "/" + ftpFile.getName()
////                    );
//
//                }
//
//
//            }
//        }
    }


    public static void allFilePath() {


    }


    public static void dosome(String remotePath) throws IOException {
        ftpClient.changeWorkingDirectory(remotePath);
        FTPFile[] allFile = ftpClient.listFiles();
        for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
            if (!allFile[currentFile].getName().equals("..")
                    && !allFile[currentFile].getName().equals(".") &&
                    !allFile[currentFile].getName().equals("./") &&
                    !allFile[currentFile].getName().equals("../")
                    &&
                    !allFile[currentFile].getName().equals(".../")
            ) {
                if (!dirLi.contains(allFile[currentFile].getName().replace("/", "")))
                    dirLi.add(allFile[currentFile].getName().replace("/", ""));
            }
        }
    }

    public static void setZero() {
        dirFileTotalSize = 0;
    }

    public static long getRealSize() {
        return dirFileTotalSize;
    }

    public static void setfileCountZero() {
        fileCount = 0;
    }

    public static int getfileCount() {
        return fileCount;
    }

    //计算某文件夹下所有的文件的总大小
    public static void calculateDirFileTotalSize(String remotePath) throws IOException {

        // 初始化FTP当前目录
        currentPath = remotePath;
        //以下两句代码结合 ftpClient.retrieveFile 可以避免文件名带有空格的问题
        // 更改FTP目录
        ftpClient.changeWorkingDirectory(remotePath);
        //remotePath是路径，即里面只能是文件夹与/组成的字符串，不能包含有文件名


        // 得到FTP当前目录下所有文件
        FTPFile[] ftpFiles = ftpClient.listFiles();

        // 循环遍历
        for (FTPFile ftpFile : ftpFiles) {
            if (!ftpFile.getName().equals("..")
                    && !ftpFile.getName().equals(".") && !ftpFile.getName().equals("./")
                    && !ftpFile.getName().equals("../") && !ftpFile.getName().equals(".../")) {
                System.out.println("远程路径是：" + remotePath);
                System.out.println("ftpfile是文件夹还是文件:" + ftpFile.getName());
                if (ftpFile.isDirectory()) {
                    System.out.println("总大小--文件夹：" + ftpFile.getName());
                    //下载文件夹
                    calculateDirFileTotalSize(currentPath + "/" + ftpFile.getName());
                } else if (ftpFile.isFile()) {
                    System.out.println("总大小--文件：" + ftpFile.getName());

                    //计算当前文件的大小 并 累加
                    dirFileTotalSize += ftpFile.getSize();
                    fileCount++;
                }
            }
        }
    }





    public static String crSDFile(String... folder) {

        int length = folder.length;
        String genFolder8 = Environment.getExternalStorageDirectory().getPath() +
                File.separator + "CRRC";
        File file, file2, file3, file4, file8, filejidongcheng;
        file8 = new File(genFolder8);
        if (!file8.exists()) {
            file8.mkdir();
        }

        String genFolder = genFolder8 +
                File.separator + "DOWNLOAD";
        file2 = new File(genFolder);
        if (!file2.exists()) {
            file2.mkdir();
        }
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("filedirname",
                Activity.MODE_PRIVATE);
        String jidongcheng = sharedPreferences.getString("chexing", "");//机车产品线  ..文件夹创建
        if (jidongcheng.equals("机车")) {
            jidongcheng = "机车产品线";
        } else if (jidongcheng.equals("动车")) {
            jidongcheng = "动车产品线";

        } else if (jidongcheng.equals("城轨")) {
            jidongcheng = "城轨产品线";
        }
        String dir1 = sharedPreferences.getString("filedirnamevalue", "");
        String dir2 = sharedPreferences.getString("shebeinamevalue", "");
        System.out.println("一级：" + dir1);
        System.out.println("二级：" + dir2);
////        机车产品线 等文件夹创建
//        String genFolderJidongcheng = genFolder +
//                File.separator + jidongcheng + File.separator;
//        filejidongcheng = new File(genFolderJidongcheng);
//        if (!filejidongcheng.exists()) {
//            filejidongcheng.mkdir();
//        }

        String genFolder1 = genFolder +
                File.separator + dir1;
        file3 = new File(genFolder1);
        if (!file3.exists()) {
            file3.mkdir();
        }

        String genFolder2 = genFolder1 +
                File.separator + dir2 + File.separator;
        file4 = new File(genFolder2);
        if (!file4.exists()) {
            file4.mkdir();
        }


        String str = genFolder2;
        for (int i = 0; i < length; i++) {

            str = str + folder[i] + "/";
            file = new File(str);

            if (!file.exists()) {
                file.mkdir();

            }

        }

        System.out.println("111路径：" + str);
        return str;

    }


    public static String crSDFileTask(String... folder) {

        int length = folder.length;
        String genFolder8 = Environment.getExternalStorageDirectory().getPath().toString() +
                File.separator + "CRRC";
        File file, file2, file3, file4, file8, filejidongcheng;
        file8 = new File(genFolder8);
        if (!file8.exists()) {
            file8.mkdir();
        }

        String genFolder = genFolder8 +
                File.separator + "DOWNLOAD";
        file2 = new File(genFolder);
        if (!file2.exists()) {
            file2.mkdir();
        }
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("filedirname",
                Activity.MODE_PRIVATE);

        String dir1 = sharedPreferences.getString("filedirnamevalue", "");
        String dir2 = sharedPreferences.getString("shebeinamevalue", "");
        System.out.println("一级：" + dir1);
        System.out.println("二级：" + dir2);

        String genFolder1 = genFolder +
                File.separator + dir1;
        file3 = new File(genFolder1);
        String genFolder2 = "";
        if (file3.exists()) {
            genFolder2 = genFolder1 +
                    File.separator + dir2 + File.separator;
        } else {
            return genFolder;
        }


        file4 = new File(genFolder2);
        String str = "";
        if (file4.exists()) {
            str = genFolder2;
        } else {
            return genFolder1;
        }


        for (int i = 0; i < length; i++) {

            str = str + folder[i] + "/";
            file = new File(str);
            if (file.exists()) {
                continue;
            } else {
                return str;
            }

        }

        System.out.println("task中路径：" + str);
        return str;

    }

    public long getFilesSize(String path, FTPClient ftpClient, List<String> dnameList) throws IOException {
        long totalSize = 0;
        ftpClient.changeWorkingDirectory(path);
        FTPFile[] files = ftpClient.listFiles();
        int dots = 0;
        for (int k = 0; k < files.length; k++) {
            if (files[k].getName().equals("..") || files[k].getName().equals(".")) {
                dots++;
            }
        }
        if (files.length - dots == 0) {
            return 0;
        }
        for (int i = dots; i < files.length; i++) {
            if (files[i].isFile() && dnameList.contains(files[i].getName())) {
                System.out.println("选择的文件并计算长度：" + files[i].getName());
                long serverSize = files[i].getSize(); // 获取远程文件的长度
                totalSize += serverSize;
            }

        }
        return totalSize;
    }

    public List<String> getRemoteFileSize(List<FTPFile> taskFtpFiles) {
        List<String> remotSize = new ArrayList<>();
        for (int i = 0; i < taskFtpFiles.size(); i++) {
            if (taskFtpFiles.get(i).isFile()) {

            } else if (taskFtpFiles.get(i).isDirectory()) {

            }
        }
        return remotSize;
    }

//
//    /***
//     * 下载文件
//     * @param remoteFileName 待下载文件名称
//     * @param localDires 下载到当地那个路径下
//     * @param remoteDownLoadPath remoteFileName所在的路径
//     * */
//    public boolean downloadFile(String remoteFileName, String localDires,
//                                String remoteDownLoadPath) {
//        String strFilePath = localDires + remoteFileName;
//        BufferedOutputStream outStream = null;
//        boolean success = false;
//        try {
//            this.ftpClient.changeWorkingDirectory(remoteDownLoadPath);
//            outStream = new BufferedOutputStream(new FileOutputStream(
//                    strFilePath));
//            logger.info(remoteFileName + "开始下载....");
//            success = this.ftpClient.retrieveFile(remoteFileName, outStream);
//            if (success == true) {
//                logger.info(remoteFileName + "成功下载到" + strFilePath);
//                return success;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error(remoteFileName + "下载失败");
//        } finally {
//            if (null != outStream) {
//                try {
//                    outStream.flush();
//                    outStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        if (success == false) {
//            logger.error(remoteFileName + "下载失败!!!");
//        }
//        return success;
//    }
//
//
//    /***
//     * @下载文件夹
//     * @param localDirectoryPath本地地址
//     * @param remoteDirectory 远程文件夹
//     * */
//    public boolean downLoadDirectory(String localDirectoryPath, String remoteDirectory) {
//        try {
//            String fileName = new File(remoteDirectory).getName();
//            localDirectoryPath = localDirectoryPath + fileName + "//";
//            new File(localDirectoryPath).mkdirs();
//            FTPFile[] allFile = this.ftpClient.listFiles(remoteDirectory);
//            for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
//                if (!allFile[currentFile].isDirectory()) {
//                    downloadFile(allFile[currentFile].getName(), localDirectoryPath, remoteDirectory);
//                }
//            }
//            for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
//                if (allFile[currentFile].isDirectory()) {
//                    String strremoteDirectoryPath = remoteDirectory + "/" + allFile[currentFile].getName();
//                    downLoadDirectory(localDirectoryPath, strremoteDirectoryPath);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }


}