package com.crrc.pdasoftware.wuxianzhuanchu.all.activitys;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crrc.pdasoftware.MyApplication;
import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.utils.XToastUtils;
import com.crrc.pdasoftware.wuxianzhuanchu.all.adapters.CommonRecyclerViewAdapter;
import com.crrc.pdasoftware.wuxianzhuanchu.all.adapters.FailDownAdapter;
import com.crrc.pdasoftware.wuxianzhuanchu.all.adapters.FailRecyclerViewAdapter;
import com.crrc.pdasoftware.wuxianzhuanchu.all.adapters.MyAdapter;
import com.crrc.pdasoftware.wuxianzhuanchu.all.bean.DownloadindBean;
import com.crrc.pdasoftware.wuxianzhuanchu.all.utils.AlertUtils;
import com.crrc.pdasoftware.wuxianzhuanchu.all.utils.AnimalUtil;
import com.crrc.pdasoftware.wuxianzhuanchu.all.utils.DirDownTask;
import com.crrc.pdasoftware.wuxianzhuanchu.all.utils.FtpUtils;
import com.crrc.pdasoftware.wuxianzhuanchu.all.utils.ItemChooseData;
import com.crrc.pdasoftware.wuxianzhuanchu.all.utils.ItemChooseDataFail;
import com.crrc.pdasoftware.wuxianzhuanchu.all.utils.netUti;
import com.githang.statusbar.StatusBarCompat;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.popupwindow.bar.CookieBar;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.leefeng.promptlibrary.PromptDialog;
import ru.alexbykov.nopermission.PermissionHelper;

public class selectFileActivity extends AppCompatActivity implements OnClickListener {
    MyApplication apps;
    List<String> selectPathDir = new ArrayList<>();
    List<String> selectPathFile = new ArrayList<>();
    CopyOnWriteArrayList<String> downfail = new CopyOnWriteArrayList<>();
    CopyOnWriteArrayList<String> listDown = new CopyOnWriteArrayList<>();

    ExecutorService fixedThreadPool = null;
    boolean canGiveValue = true;
    int downloadingFileCounts = 0;
    int currentDowncount = 0;
    int totalDowncount = 0;
    boolean canAgainDownload = false;//是否可以再次下载别得任务
    MyApplication globalVar = null;
    String host, user, pass, clickBackDir;
    String clickDir = "/";
    int port;
    public boolean flagBtn = true;
    CopyOnWriteArrayList<String> countExe = new CopyOnWriteArrayList<>();//计算当前线程池已下载的文件个数（成功 失败都算）
    CopyOnWriteArrayList<String> countExeSuccess = new CopyOnWriteArrayList<>();//计算当前线程池已下载成功的文件个数（仅有成功的）
    String currentParent = "";
    public ListView lv;
    private MyAdapter mAdapter;
    private List<FtpUtils.wxhFile> list;
    public static List<FtpUtils.wxhFile> currentFiles = null;
    public static List<FtpUtils.wxhFile> movingList = null;
    public static List<FtpUtils.ItemFresh> itemFreshList = new ArrayList<>();

    public int globalCountValue = 0;
    public static CheckBox checkbox_all;
    Button bt;
    public Button bttest;
    public TextView tv_downsele;
    ImageView img;
    public int dirSize = 0;
    String downString = "";
    String parentPath = null;
    public static String dirpath;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private PromptDialog promptDialog;
    ItemChooseData itemChooseData = null;
    private Timer timer = null;
    private TimerTask mTimerTask = null;
    private PathMeasure mPathMeasure;
    /**
     * 贝塞尔曲线中间过程的点的坐标
     */
    private float[] mCurrentPosition = new float[2];
    FTPClient ftpClient;

//    List<String> backDir;

    RelativeLayout llAll;
    Map<String, DownloadindBean> downloadingMap = new ConcurrentHashMap<>();
    LinearLayout lllParent;
    ImageView imageViewDown = null;
    ImageView imageViewBack = null;
    private String localPath;
    private Timer timer2;
    private TimerTask mTimerTask2;
    private PermissionHelper mPermissionHelper;
    private String wifiresult;
    CommonRecyclerViewAdapter adapter = null;
    FailRecyclerViewAdapter adapterfail = null;
    public Handler mLoginHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            System.out.println(
                    ".what:" + msg.what
            );
            if (msg.what == 1) {
                currentFiles = list;
                movingList = list;
                apps.setDownlist(list);
                mAdapter = new MyAdapter(movingList, itemFreshList, selectFileActivity.this,
                        globalCountValue, globalVar, downloadingMap);
                // 绑定Adapter
                lv.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                promptDialog.dismiss();
            } else if (msg.what == 0) {
                promptDialog.dismiss();
                AlertUtils.alertNoListener(selectFileActivity.this, "该文件夹为空文件夹！");
            } else if (msg.what == 2) {

                XToastUtils.info("网络连接不佳！");
                promptDialog.dismiss();
            }
            return false;
        }
    });
    private int count = 0;
    private FTPClient ftpClient2;
    private FTPClient ftpClient3;
    private FTPClient ftpClient4;
    private FTPClient ftpClient5;


    public void LoginClaz() {
        Context that = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                final boolean[] sucornot = {false};
                boolean re = false;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        promptDialog.showLoading("加载中...");
                    }
                });

                canGiveValue = true;
                downloadingFileCounts = 0;
//            apps.setDownloadingFileCounts(downloadingFileCounts);

                final String hostx = ((selectFileActivity) that).host;
                String portStr = "21";
                final String userx = ((selectFileActivity) that).user;
                final String passx = ((selectFileActivity) that).pass;
                String path = clickDir;

                final FtpUtils util = new FtpUtils(selectFileActivity.this, true, selectPathDir, selectPathDir, apps);
                int port = 21;
                port = Integer.parseInt(portStr);
                try {
                    System.out.println("首页首次加载时："+host+"-"+"user:"+user);
                    sucornot[0] = util.connectServerTask(host, port, user, pass, "");
                    if (!sucornot[0]) {
                        //连接失败 则重连
                        final int finalPort = port;
                        Looper.prepare();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
//                                XToastUtils.info("重连中..");
                                sucornot[0] = util.connectServerTask(host, finalPort, user, pass, "");
                                if (!sucornot[0]) {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            sucornot[0] = util.connectServerTask(host, finalPort, user, pass, "");
                                            promptDialog.dismiss();
                                            XToastUtils.info("请检查网络");

                                        }
                                    }, 4000);
                                }
                            }
                        }, 1000);
                        Looper.loop();
                    }else{
                        //登录成功
                        apps.setLastShebeiName(apps.getCurrentShebei());
                    }
                    System.out.println("连接测试1111：" + sucornot[0]);

                    FTPFile[] listedDirectories = null;

                    list = util.getFileList(path);
                    System.out.println("select中path值：" + path);

                    for (int i = 0; i < list.size(); i++) {
                        itemFreshList.add(new FtpUtils.ItemFresh(i, ""));
                    }

                    //-----1-----首先得出本地完整存在的文件名，以及该文件名在整个listview中的position
                    //远程
                    List<FTPFile> taskFtpFiles = util.getOriginFileList(path, FtpUtils.ftpClient);
                    //path为当前所点击文件夹的完整路径名称，从而进行本地文件夹的创建（存在则不创建）
                    String[] strdir = path.split("/");
                    String mylocaldirpath = new FtpUtils(selectFileActivity.this,
                            true, selectPathDir, selectPathDir, apps).crSDFileTask(strdir);

                    System.out.println("本地的文件路径测试：" + mylocaldirpath);
                    File localFile = new File(mylocaldirpath);
                    File[] taskfiles = localFile.listFiles();

                    List<String> posHaveDown = new ArrayList<>();
                    //对比本地文件与远程文件的name，size是否一样，是则记录在posHaveDown中 在后面--2--中再找position
                    if (taskfiles != null && taskfiles.length > 0) {
                        for (int j = 0; j < taskfiles.length; j++) {
                            if (taskfiles[j].isFile()) {
                                for (int k = 0; k < taskFtpFiles.size(); k++) {
                                    if (taskfiles[j].getName().equals(taskFtpFiles.get(k).getName()) &&
                                            taskfiles[j].length() >= taskFtpFiles.get(k).getSize()) {
                                        posHaveDown.add(taskfiles[j].getName());
                                    }
                                }
                            }
                        }
                    }
                    //--------1-------------------------------------------------------------


                    if (list.size() > 0) {
                        System.out.println("点击文件夹时，记录父路径：" + path);
                        apps.setPath(path);//每次点击文件夹进入下级目录时，都记录父路径
//                    backDir.add(path);
                    }
                    //附带父亲文件夹的路径进去
                    list.add(new FtpUtils.wxhFile(path, "", 2, "", ""));


                    List<FtpUtils.wxhFile> listFile = new ArrayList<>();
                    List<FtpUtils.wxhFile> listDir = new ArrayList<>();

                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isParent) {
                            parentPath = list.get(i).filePath;
                            list.remove(i);
                            System.out.println("parent打印：" + parentPath);
                        }

                    }
                    System.out.println("刚开始的个数：" + list.size());
                    List<FtpUtils.wxhFile> removeFileIndex = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isFile) {//是文件
                            listFile.add(list.get(i));
                            removeFileIndex.add(list.get(i));//记住待删除的文件对象。
                        }
                    }
                    System.out.println("文件个数：" + listFile.size());
                    if (removeFileIndex.size() > 0) {
                        for (int i = 0; i < removeFileIndex.size(); i++) {
                            list.remove(removeFileIndex.get(i));//删除所有的文件
                        }
                    }

                    dirSize = list.size();//文件夹的个数
                    System.out.println("文件夹个数：" + list.size());
                    list.addAll(listFile);//文件夹与文件集合的拼接
                    System.out.println("拼接后个数：" + list.size());


                    //-----2-----在这里才添加“已下载”
                    if (posHaveDown.size() > 0 && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            for (int j = 0; j < posHaveDown.size(); j++) {
                                if (posHaveDown.get(j).equals(list.get(i).filename)) {
                                    System.out.println("测试最终位置结果：" + i);
                                    itemFreshList.get(i).setSucfail("已下载");
                                }
                            }
                        }
                    }

                    //------2---------------------------------------------------------------

//                    for (int i = 0; i < list.size(); i++) {
//                        System.out.println("大小：" + list.get(i).filesize + "--" + "时间：" + list.get(i).filetime);
//                    }


                    SharedPreferences pref = selectFileActivity.this.getSharedPreferences("mypath", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("pathname", parentPath);
                    editor.commit();

                    if (!sucornot[0]) {
                        mLoginHandler.sendEmptyMessage(2);

                    }

                    if (list == null || list.size() == 0) {
                        mLoginHandler.sendEmptyMessage(0);

                    } else {
                        mLoginHandler.sendEmptyMessage(1);

                    }

                } catch (Exception e) {
                    System.out.println("开启连接出错");
                    e.printStackTrace();
                } finally {
                    try {
                        util.closeServer();
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }


            }
        }).start();
    }

    int selectNumberthread;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences preuploadthread = this.getSharedPreferences("datathread", MODE_PRIVATE);
        SharedPreferences.Editor editoruploadthread = preuploadthread.edit();
        selectNumberthread = preuploadthread.getInt("uploadinfo", 1) + 1;
        System.out.println("并行下载任务个数：" + selectNumberthread);
        if (selectNumberthread == 1) {
            fixedThreadPool = Executors.newFixedThreadPool(1);
        } else if (selectNumberthread == 2) {
            fixedThreadPool = Executors.newFixedThreadPool(2);
        } else if (selectNumberthread == 3) {
            fixedThreadPool = Executors.newFixedThreadPool(3);
        }

        apps = (MyApplication) getApplication();
        ftpClient = new FTPClient();
        globalVar = (MyApplication) getApplication();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_select_file);
        mPermissionHelper = new PermissionHelper(this);
        getDeviceInfo();

        promptDialog = new PromptDialog(this);
//        verifyStoragePermissions(this);
        Intent intent = getIntent();
        //接收数据
        host = intent.getStringExtra("host");
        user = intent.getStringExtra("user");
        pass = intent.getStringExtra("pass");
        System.out.println("activty中初始时获取数据："+host+"-user:"+user);
        port = intent.getIntExtra("port", 21);
        lllParent = findViewById(R.id.lll_pa);
        bt = (Button) findViewById(R.id.parent);
        bt.setOnClickListener(selectFileActivity.this);
//        backDir = new ArrayList<>();

        itemChooseData = new ItemChooseData();
        /* 实例化各个控件 */
        lv = (ListView) findViewById(R.id.lv);
        llAll = findViewById(R.id.ll_all);
        img = findViewById(R.id.imtest);
        list = new ArrayList<FtpUtils.wxhFile>();
        checkbox_all = findViewById(R.id.checkbox_all);
        bttest = findViewById(R.id.bttest);
        tv_downsele = findViewById(R.id.tv_downsele);

        // 为Adapter准备数据

//        ActionBar actionBar = this.getSupportActionBar();
//        actionBar.setTitle("文件下载");
//        actionBar.setDisplayHomeAsUpEnabled(true);


        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorAccent), true);

        imageViewDown = findViewById(R.id.im_downloading);
        imageViewBack = findViewById(R.id.im_backArro);
        ObjectAnimator animator = AnimalUtil.tada(imageViewDown);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();
        setClick();
        System.out.println("oncreate调用了");
    }


    private void getDeviceInfo() {
        mPermissionHelper.check(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .onSuccess(this::onSuccess).onDenied(this::onDenied).
                onNeverAskAgain(this::onNeverAskAgain).run();
    }

    private void onSuccess() {
    }

    private void onDenied() {

    }

    private void onNeverAskAgain() {
        XToastUtils.info("请同意权限申请");
        System.out.println("用户拒绝onNeverAskAgain");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        ItemChooseData.clearFileName();
        ItemChooseData.clearFilePath();
        ItemChooseData.clearPathAndisfile();
        apps.setAllCount(0);

        globalVar.hashmap.clear();
        clearItemrefresh();
        checkbox_all.setChecked(false);
        MyAdapter.clearBoolList();


//        if (promptDialog.onBackPressed()){
//            super.onBackPressed();
//        }

        showSimpleConfirmDialog();
        return;//拦截 物理返回按键；
    }

    void threaddownload(final String myfilename, String remotePath, FTPClient ftpClient,
                        long singleFileSize) throws InterruptedException {
        String mylocaldirpath = null;
        DownloadindBean downloadindBean = new DownloadindBean();
        long currentSize = 0;//当前已下载的大小
        try {
            ftpClient.setSoTimeout(6000);
        } catch (SocketException e) {
            System.out.println("线程任务卡死--------------------");
            //成功下载完后的某个文件，再次下载 应该算入总数吗？若是，那成功的数量也该增加吧
            e.printStackTrace();
        }
        try {

            String[] strdir = remotePath.split("/");

            mylocaldirpath = new FtpUtils(selectFileActivity.this, true,
                    selectPathDir, selectPathDir, apps).crSDFile(strdir);
            System.out.println("文件夹前缀：" + mylocaldirpath);
            File localFile = new File(mylocaldirpath + "/" + myfilename);
            long localSize = 0;

//            boolean newFile = localFile.createNewFile();


            final byte[] b = new byte[1024];


            float process = 0;//进度值


            localSize = localFile.length();

            //表示本地已存在
            if (localFile.exists()) {
                if (localSize >= singleFileSize) {
                    //表示本地已存在,且是完整的文件
                    downloadindBean.setCurrent(100);
                    downloadindBean.setDownloaded("已下载");
                    downloadingMap.put(remotePath + myfilename, downloadindBean);
                    apps.setDownBean(downloadingMap);

                    apps.removeOneQueueDownloading();

                    System.out.println("已经下载过了：" + remotePath + myfilename);
                    if (apps.getallTaskDowned() <= apps.getallTask()) {
                        apps.setallTaskDowned(remotePath + myfilename);//记录下载成功的个数

                    }
                } else {
                    //表示本地已存在,不完整，断点续传

//                if (localFile.exists()) {//不进行断点续传 而是完全重新下载
//                    localFile.delete();
//                    localFile = new File(mylocaldirpath + "/" + myfilename);
//                }
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    //断点续传
                    ftpClient.setRestartOffset(localSize);
                    currentSize = localSize;

                    FileOutputStream out = new FileOutputStream(localFile, true);
                    //若要使用断点续传，需使用此获取流的方法
                    InputStream input = ftpClient.retrieveFileStream(remotePath + myfilename);
                    //该retrieveFileStream在特殊ftp服务器上生效 但是使用了此方法 断点续传是不可以的。
//                    InputStream input = ftpClient.retrieveFileStream(remotePath + new String(myfilename.getBytes("UTF-8"), "iso-8859-1"));

                    int length = 0;


                    if (input == null) {
                        input = ftpClient.retrieveFileStream(remotePath + myfilename);
                    }

                    while (input != null &&
                            ((length = input.read(b)) != -1)) {
                        out.write(b, 0, length);//写文件
                        currentSize = currentSize + length;

                        int numJindu = (int) (((float) currentSize / singleFileSize) * 100);
//                    System.out.println("文件-" + myfilename + "下载进度：" + numJindu);
//                        System.out.println("文件-" + myfilename + "下载进度：" + currentSize + "/" + singleFileSize);
                        if (numJindu == 100) {
                            System.out.println("chenggong=========");
                            downloadindBean.setDownloaded("已下载");
                            downloadindBean.setCurrent(100);
                            downloadingMap.put(remotePath + myfilename, downloadindBean);
                            apps.setDownBean(downloadingMap);

                        } else if (numJindu > 100) {
                            if (currentSize == singleFileSize) {

                            }
                            System.out.println("进度出现大于100====" + numJindu + ">>" +
                                    myfilename + ">>" + currentSize + "---" + singleFileSize
                                    + ">>process" + process + ">>step"
                            );
                        } else {
                            downloadindBean.setCurrent(numJindu);
                            downloadindBean.setDownloaded("下载中..");
                            downloadingMap.put(remotePath + myfilename, downloadindBean);
                            apps.setDownBean(downloadingMap);
                        }


                    }

                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                    if (input != null) {
                        input.close();
                    }

                    // 此方法是来确保流处理完毕，如果没有此方法，可能会造成现程序死掉
                    if (ftpClient.completePendingCommand()) {
//                    apps.setSuccessDown();

                        File tmpf = new File(mylocaldirpath + "/" + myfilename);
                        if (tmpf.exists() &&
                                tmpf.length() > 0 &&
                                tmpf.length() >= singleFileSize) {
                            if (apps.getallTaskDowned() <= apps.getallTask()) {
                                apps.setallTaskDowned(remotePath + myfilename);//记录下载成功的个数

                            }
                            System.out.println(remotePath + myfilename + ":下载成功=====222===========================" + apps.getallTaskDowned());
                        }
//                    else {
//                        downfail.add(remotePath + myfilename);
//                        System.out.println("下载失败===流却关闭成功=============================" + apps.getallTaskDowned());
//
//                        downloadindBean.setCurrent(finalPro);
//                        downloadindBean.setDownloaded("下载中..");
//                        downloadingMap.put(remotePath + myfilename, downloadindBean);
//                        apps.setDownBean(downloadingMap);
//                    }


                    } else {
                        downfail.add(remotePath + myfilename);
                        System.out.println("2处记录失败：" + myfilename);

                    }


                }
            } else {//表示本地不存在

//                if (singleFileSize==0){//文件大小为0
//                    downloadindBean.setCurrent(100);
//                    downloadindBean.setDownloaded("已下载");
//                    downloadingMap.put(remotePath + myfilename, downloadindBean);
//                    apps.setDownBean(downloadingMap);
//
//                    apps.removeOneQueueDownloading();
//
//                    System.out.println("已经下载过了：" + remotePath + myfilename);
//                    if (apps.getallTaskDowned() <= apps.getallTask()) {
//                        apps.setallTaskDowned(remotePath + myfilename);//记录下载成功的个数
//
//                    }
//                }else{


                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                FileOutputStream out = new FileOutputStream(localFile, true);
//                InputStream input = ftpClient.retrieveFileStream(remotePath + myfilename);
                ftpClient.enterLocalPassiveMode();
                InputStream input = ftpClient.retrieveFileStream(remotePath + new String(myfilename.getBytes("UTF-8"), "iso-8859-1"));

                int length = 0;


                if (input == null) {
                    input = ftpClient.retrieveFileStream(remotePath + myfilename);
                }


                //文件大小为0
                if (singleFileSize == 0) {
                    downloadindBean.setCurrent(100);
                    downloadindBean.setDownloaded("已下载");
                    downloadingMap.put(remotePath + myfilename, downloadindBean);
                    apps.setDownBean(downloadingMap);

                    apps.removeOneQueueDownloading();

                    System.out.println("已经下载过了：" + remotePath + myfilename);
                    if (apps.getallTaskDowned() <= apps.getallTask()) {
                        apps.setallTaskDowned(remotePath + myfilename);//记录下载成功的个数
                    }
                }



                while (input != null &&
                        ((length = input.read(b)) != -1)) {
                    out.write(b, 0, length);//写文件
                    currentSize = currentSize + length;
                    int numJindu = (int) (((float) currentSize / singleFileSize) * 100);
//                    System.out.println("文件-" + myfilename + "下载进度：" + numJindu);
//                    System.out.println("文件-" + myfilename + "下载进度：" + currentSize + "/" + singleFileSize);
                    if (numJindu == 100) {
                        System.out.println("chenggong=========");
                        downloadindBean.setDownloaded("已下载");
                        downloadindBean.setCurrent(100);
                        downloadingMap.put(remotePath + myfilename, downloadindBean);
                        apps.setDownBean(downloadingMap);

                    } else if (numJindu > 100) {
                        if (currentSize == singleFileSize) {

                        }
                        System.out.println("进度出现大于100====" + numJindu + ">>" +
                                myfilename + ">>" + currentSize + "---" + singleFileSize
                                + ">>process" + process + ">>step"
                        );
                    } else {
                        downloadindBean.setCurrent(numJindu);
                        downloadindBean.setDownloaded("下载中..");
                        downloadingMap.put(remotePath + myfilename, downloadindBean);
                        apps.setDownBean(downloadingMap);
                    }


                }

                if (out != null) {
                    out.flush();
                    out.close();
                }
                if (input != null) {
                    input.close();
                }

                // 此方法是来确保流处理完毕，如果没有此方法，可能会造成现程序死掉
                if (ftpClient.completePendingCommand()) {
//                    apps.setSuccessDown();

                    File tmpf = new File(mylocaldirpath + "/" + myfilename);
                    if (tmpf.exists() &&
                            tmpf.length() > 0 &&
                            tmpf.length() >= singleFileSize) {
                        if (apps.getallTaskDowned() <= apps.getallTask()) {
                            apps.setallTaskDowned(remotePath + myfilename);//记录下载成功的个数

                        }
                        System.out.println(remotePath + myfilename + "下载成功====111============================" + apps.getallTaskDowned());
                    }
//                    else {
//                        downfail.add(remotePath + myfilename);
//                        System.out.println("下载失败===流却关闭成功=============================" + apps.getallTaskDowned());
//
//                        downloadindBean.setCurrent(finalPro);
//                        downloadindBean.setDownloaded("下载中..");
//                        downloadingMap.put(remotePath + myfilename, downloadindBean);
//                        apps.setDownBean(downloadingMap);
//                    }


                } else {
                    downfail.add(remotePath + myfilename);
                    System.out.println("2处记录失败：" + myfilename);

                }

            }

//            }


        } catch (FileNotFoundException e) {
            if (currentSize < singleFileSize) {
                System.out.println("没有下载完的文件：" + myfilename + "--断点的字节数：" + currentSize);
            }
            System.out.println("没有找到" + remotePath + "文件---------------------");

            downfail.add(remotePath + myfilename);
            System.out.println("3处记录失败：" + myfilename);


            Thread.sleep(1000);

            if (!isNetworkAvailable()) {
                handleFailtask();
            }

            e.printStackTrace();
        } catch (SocketException e) {
            if (currentSize < singleFileSize) {
                System.out.println("没有下载完的文件：" + myfilename + "--断点的字节数：" + currentSize);
            }
            System.out.println("连接FTP失败----读取超时----线程卡住-------------:" + myfilename);
            apps.setneterror(true);
            downfail.add(remotePath + myfilename);
            System.out.println("4处记录失败：" + myfilename);


            Thread.sleep(1000);
            if (!isNetworkAvailable()) {
                handleFailtask();
            }

            e.printStackTrace();
        } catch (IOException e) {
            if (currentSize < singleFileSize) {
                System.out.println("没有下载完的文件：" + myfilename + "--断点的字节数：" + currentSize);
            }
            System.out.println("文件读取错误，---------------------");

            downfail.add(remotePath + myfilename);
            System.out.println("5处记录失败：" + myfilename);


            Thread.sleep(1000);
            if (!isNetworkAvailable()) {
                handleFailtask();
            }

            e.printStackTrace();
        } finally {
            if (currentSize < singleFileSize) {
                System.out.println("没有下载完的文件：" + myfilename + "--断点的字节数：" + currentSize);
            }
            File tmpf = new File(mylocaldirpath + "/" + myfilename);

//            if (!(tmpf.exists() &&
//                    tmpf.length() > 0 &&
//                    tmpf.length() >= singleFileSize)) {
//                System.out.println("特殊情况：" + remotePath + myfilename);
//                downloadindBean.setCurrent(finalPro);
//
//                downloadindBean.setDownloaded("下载中..");
//                downloadingMap.put(remotePath + myfilename, downloadindBean);
//                apps.setDownBean(downloadingMap);
//                downfail.add(remotePath + myfilename);
//
//
//            }


//            System.out.println("finally执行顺序2222222-------");
//            for (int i = 0; i < downfail.size(); i++) {
//                System.out.println("最后的失败的数据：" + i + "--" + downfail.get(i));
//
//            }
            apps.setQueueFail(downfail);

//            for (int i = 0; i < apps.getQueueFail().size(); i++) {
//                System.out.println("最后的失败的数据：" + i + "--" + apps.getQueueFail().toArray()[i]);
//
//            }

            taskCom();

        }
    }

    //出现异常后 把剩下的任务全部放在未下载的任务中去
    public void handleFailtask() {
//        Looper.prepare();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
        System.out.println("执行顺序---111111----");

        int sze = apps.getQueueDownloading().size();
        ConcurrentLinkedQueue que = apps.getQueueDownloading();
        if (sze > 0) {
            for (int i = 0; i < sze; i++) {
                Object obj = que.poll();
                if (obj != null) {
                    downfail.add(obj.toString());
                    System.out.println("6处记录失败：" + obj.toString());

                }

                countExe.add("t");
//                countExeSuccess.add("t");

                apps.sethaveDownloadingFileCounts(countExe.size());
            }
        }

        for (int i = 0; i < downfail.size(); i++) {
            System.out.println(
                    "打印未下载的任务：" + downfail.get(i)
            );
        }
//            }
//        }, 2000);
//        Looper.loop();
    }

    /**
     * 检查是否有网络
     */
    public boolean isNetworkAvailable() {
        NetworkInfo info = getNetworkInfo();
        return info != null && info.isAvailable();
    }

    private NetworkInfo getNetworkInfo() {

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    private void showSimpleConfirmDialog() {
        DialogLoader.getInstance().showConfirmDialog(
                selectFileActivity.this,
                "是否确定离开下载页面？",
                "是",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        apps.setChangeShebei(false);
                        finish();
                        dialog.dismiss();
                    }
                },
                "否",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );


    }


    public void setHavedown() {
        //检查是否刚下载的文件被删除了，删除则不显示在列表
        String prefixxxx = Environment.getExternalStorageDirectory().getPath() +
                File.separator + "CRRC/DOWNLOAD";
        SharedPreferences sharedPreferences = getSharedPreferences("filedirname",
                Activity.MODE_PRIVATE);
        //dir1  dir2为下框框信息勾选后生成的一级目录和设备二级目录。
        String dir1 = sharedPreferences.getString("filedirnamevalue", "");
        String dir2 = sharedPreferences.getString("shebeinamevalue", "");

        if (dir1.length() > 0) {
            if (dir2.length() > 0) {
                prefixxxx = prefixxxx + "/" + dir1 + "/" + dir2;
            } else {
                prefixxxx = prefixxxx + "/" + dir1;
            }
        }
        Iterator<String> iterator = apps.getQueue().iterator();
        while (iterator.hasNext()) {
            String filename = iterator.next();
            //  filename = filename.substring(filename.lastIndexOf('/') + 1);
            //检测该文件名（包含全路径），在本地是否存在，存在则添加到downloadingFileList2
            //不存在，则不添加，不显示。这是为了防止刚刚下载的文件被删除后，依然显示在下载列表，
            //要求是不显示已删除的文件。
            File localFile = new File(prefixxxx + filename);
            if (!localFile.exists()) {
                System.out.println("不存在：" + filename);
//                apps.getQueue().remove(filename);
                apps.clearOnefromQueue(filename);//下载的总数
                apps.clearOneTaskDowned();//成功下载记录的个数
            }
        }

        if (apps.getQueue().size() == 0) {
            XToastUtils.info("无下载任务");
        } else {
            showBottomSheetListDialog();
            adapter.notifyDataSetChanged();

        }
    }

    public void setHavedown2() {
        //检查是否刚下载的文件被删除了，删除则不显示在列表
        String prefixxxx = Environment.getExternalStorageDirectory().getPath() +
                File.separator + "CRRC/DOWNLOAD";
        SharedPreferences sharedPreferences = getSharedPreferences("filedirname",
                Activity.MODE_PRIVATE);
        //dir1  dir2为下框框信息勾选后生成的一级目录和设备二级目录。
        String dir1 = sharedPreferences.getString("filedirnamevalue", "");
        String dir2 = sharedPreferences.getString("shebeinamevalue", "");

        if (dir1.length() > 0) {
            if (dir2.length() > 0) {
                prefixxxx = prefixxxx + "/" + dir1 + "/" + dir2;
            } else {
                prefixxxx = prefixxxx + "/" + dir1;
            }
        }
        Iterator<String> iterator = apps.getQueue().iterator();
        while (iterator.hasNext()) {
            String filename = iterator.next();
            //  filename = filename.substring(filename.lastIndexOf('/') + 1);
            //检测该文件名（包含全路径），在本地是否存在，存在则添加到downloadingFileList2
            //不存在，则不添加，不显示。这是为了防止刚刚下载的文件被删除后，依然显示在下载列表，
            //要求是不显示已删除的文件。
            File localFile = new File(prefixxxx + filename);
            if (!localFile.exists()) {
                System.out.println("不存在：" + filename);
//                apps.getQueue().remove(filename);//下载的总数
                apps.clearOnefromQueue(filename);//下载的总数

                apps.clearOneTaskDowned();//成功下载记录的个数


            }
        }

    }

    public void setClick() {
        imageViewDown.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到显示下载过程进度的activity
//                Intent intent = new Intent(selectFileActivity.this, DownloadingFileListActivity.class);
//                startActivity(intent);

                setHavedown();

            }
        });
        imageViewBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                showSimpleConfirmDialog();
            }
        });

        //全选
        checkbox_all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (currentFiles != null) {
                    ItemChooseData.clearFilePath();
                    ItemChooseData.clearPathAndisfile();
                    apps.setAllCount(0);

                    if (isChecked) {
                        apps.setAllCount(currentFiles.size());
                        globalVar.setCheck(1);
                        // 遍历list的长度，将MyAdapter中的map值全部设为true
                        for (int i = 0; i < currentFiles.size(); i++) {
                            MyAdapter.getIsSelected().put(i, true);
                        }
                        System.out.println("点全选时，文件总个数：" + currentFiles.size());

                        mAdapter.notifyDataSetChanged();

                    } else {
                        apps.setAllCount(currentFiles.size());
                        globalVar.setCheck(0);

                        for (int i = 0; i < currentFiles.size(); i++) {
                            MyAdapter.getIsSelected().put(i, false);

                        }
                        System.out.println("点非全选时，文件总个数：" + currentFiles.size());
                        mAdapter.notifyDataSetChanged();
                    }

                }
            }

        });


        //下载按钮的监听.....实际为添加下载任务按钮
        bttest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        xMeth();
                    }
                }).start();
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (currentFiles.get(arg2).isFile) {
                    //点击的是文件
                } else {
                    //是文件夹
                    whileDir(arg2);
                }
            }
        });


//        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                //如果item是文件夹 才进行下一步处理。不是文件夹 不做任何操作
//                boolean click = false;
//                FtpUtils.wxhFile wxhFile = currentFiles.get(i);
//                if (wxhFile.isFile) {
//                    click = false;
//                } else {
//
//
//                    //是文件夹
//                    final String dirDownloadPath = wxhFile.filePath;//所需下载的文件夹/文件的 远程路径
//                    System.out.println("选得下载的总路径：" + dirDownloadPath);
//                    AlertDialog.Builder builder = new AlertDialog.Builder(selectFileActivity.this);
//                    builder.setTitle("文件夹下载");
//                    builder.setMessage("是否下载该文件夹下所有文件？");
//                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            String portStr = new Integer(port).toString();
//                            String can[] = new String[4];
//                            can[0] = host;
//                            can[1] = portStr;
//                            can[2] = user;
//                            can[3] = pass;
//
//                            DirDownTask dirtask = new DirDownTask(selectFileActivity.this, dirDownloadPath,
//                                    Environment.getExternalStorageDirectory().getPath());
//                            dirtask.execute(can);
//
//
//                        }
//                    });
//                    builder.setNegativeButton("取消", null);
//                    builder.show();
//
//                    System.out.println("文件下对应的路径：" + dirDownloadPath);
//                    click = true;
//                }
//                return click;
//            }
//        });

    }


    public void xMeth() {
        count++;
        //点击下载按钮时，先得到所有的待下载文件的下载完整路径（带文件名）
        getAllFileDownloadPath();
        //bean初始化（下载的展示列表）
        JinduInit();

        //添加任务动画
        if (ItemChooseData.getPathAndIsfile().size() > 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addCartAni();
                }
            });
        }
        countShowfail = 0;
        //下载代码块
        downloadclick(false);//下载代码块
        Looper.prepare();
        timerSpring();//定时检查是否有 失败的任务添加进来。有则启动下载。
        Looper.loop();
    }

    public void timerSpring() {
        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    //刷新适配器
                    if (adapter != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }

                    //启动重新下载的代码块
                    if (apps.getagainDown()) {
                        apps.setagainDown(false);
                        System.out.println(
                                "--------QueueFail删除后的大小-----------------:" + apps.getQueueFail().size()
                        );
                        Iterator<String> iterator3 = apps.getQueueFail().iterator();
                        while (iterator3.hasNext()) {
                            String filename = iterator3.next();
                            System.out.println("QueueFail中元素：" + filename);
                        }
                        downfail.clear();
                        //启动下载代码 下载失败的任务
                        countShowfail = 0;
                        downloadclick(true);//下载代码块

                        //失败的任务启动下载时，1.总任务个数中不添加 失败的个数。2.失败的downfail变量设为0；
                    }
                }
            }
        };
        timer2 = new Timer();
        mTimerTask2 = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 0;
                mHandler.sendMessage(message);
            }
        };
        timer2.schedule(mTimerTask2, 0, 1500);//每隔一秒使用handler发送一下消息,也就是每隔一秒执行一次,一直重复执行
    }


    //给显示进度的bean初始化
    public void JinduInit() {
        //需要下载的文件个数，当点击gettask backtask时需要置为0 点击下载按钮时才赋值（如下）
        downloadingFileCounts = apps.getDownloadingFileCounts();//勾选的待下载任务个数

        //转化为list列表
        List<String> downloadingFileListbean = new ArrayList<>();
        Iterator<String> iterator = apps.getQueue().iterator();
        while (iterator.hasNext()) {
            String filename = iterator.next();
            //存带路径的文件名，仅有文件名的话，当
            // 不同的路径下有相同的文件名时，会被误判为同一个文件
//                        filename = filename.substring(filename.lastIndexOf('/') + 1);
            downloadingFileListbean.add(filename);
        }

        //循环 downloadingFileListbean为整个显示出来的下载列表
        //先给hashmap赋初值，很重要 不然listview会出现进度错乱
        DownloadindBean beaning;
        int haveDownSize = apps.getHaveDownList().size();
        for (int i = 0; i < downloadingFileListbean.size(); i++) {//完整的显示队列
            if (haveDownSize != 0) {
                boolean isContain = apps.getHaveDownList().contains(downloadingFileListbean.get(i));
                if (isContain) {
                    System.out.println("包含了的---" + downloadingFileListbean.get(i));
                    beaning = new DownloadindBean();
                    beaning.setDownloaded("已下载");
                    beaning.setCurrent(100);
                    //key：文件名，value：进度值bean
                    //从路径中取出文件名 作为key
                    String tmp = downloadingFileListbean.get(i);
//                    tmp = tmp.substring(tmp.lastIndexOf('/') + 1);
                    //存的路径
                    downloadingMap.put(tmp, beaning);
                } else {
                    //如果标志 已下载 的不用重新赋值为空值即“”；否则赋值为 下载中
                    System.out.println("不包含测试--" + downloadingFileListbean.get(i));
                    beaning = new DownloadindBean();
                    beaning.setDownloaded("下载中..");
                    beaning.setCurrent(0);
                    //key：文件名，value：进度值bean
                    String tmp = downloadingFileListbean.get(i);
//                    tmp = tmp.substring(tmp.lastIndexOf('/') + 1);
                    downloadingMap.put(tmp, beaning);
                }
            } else {//为null 表示第一次点击下载
                System.out.println("没有已经下载的队列");
                //如果标志 已下载 的不用重新赋值为空值即“”；否则赋值为 下载中
                beaning = new DownloadindBean();
                beaning.setDownloaded("下载中..");
                beaning.setCurrent(0);
                //key：文件名，value：进度值bean
                String tmp = downloadingFileListbean.get(i);
//                tmp = tmp.substring(tmp.lastIndexOf('/') + 1);
                downloadingMap.put(tmp, beaning);
            }

        }

        apps.setDownBean(downloadingMap);


    }

    public void JinduInitWhileAllSuccess() {
        //转化为list列表
        List<String> downloadingFileListbean = new ArrayList<>();
        Iterator<String> iterator = apps.getQueue().iterator();
        while (iterator.hasNext()) {
            String filename = iterator.next();
            downloadingFileListbean.add(filename);
        }
        DownloadindBean beaning;
        for (int i = 0; i < downloadingFileListbean.size(); i++) {//完整的显示队列
            beaning = new DownloadindBean();
            beaning.setDownloaded("已下载");
            beaning.setCurrent(100);
            String tmp = downloadingFileListbean.get(i);
            downloadingMap.put(tmp, beaning);
        }

        apps.setDownBean(downloadingMap);


    }


    //点击下载按钮时，先得到所有的待下载文件的下载完整路径（带文件名）
    public void getAllFileDownloadPath() {
        int allCount = ItemChooseData.getPathAndIsfile().size();
        selectPathFile.clear();
        selectPathDir.clear();
        if (allCount > 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    promptDialog.showLoading("任务添加中...");
                }
            });
            System.out.println("用来对比的：" + selectPathFile.toString());
            for (Map.Entry<String, Boolean> entry : ItemChooseData.getPathAndIsfile().entrySet()) {
                String filename = entry.getKey();
                System.out.println("文件夹名称：" + filename);
                filename = filename.substring(filename.lastIndexOf('/') + 1);
                System.out.println("substring文件夹名称：" + filename);
                if (entry.getValue()) {//是文件
                    if (!selectPathFile.contains(filename)) {
                        selectPathFile.add(filename);
                    }
                } else {//是文件夹
                    if (!selectPathDir.contains(filename)) {
                        selectPathDir.add(filename);
                    }
                }
            }


            String myAllPath = "";

            if (apps.getInfo()[4] == null) {
                myAllPath = "/";
            } else {
                myAllPath = apps.getInfo()[4];
            }
            int cou = 0;
            if (apps.getAllCount() == 0 && allCount > 0) {
                cou = currentFiles.size();
            } else {
                cou = apps.getAllCount();
            }
            System.out.println("完全路径：" + myAllPath);
            System.out.println("列表项个数：" + cou + "--选择个数" + allCount);

            //完全 全选
            if ((apps.getCheck() == 1) && (cou == allCount)) {
                //得到当前页面的总路径即可
                System.out.println("wanquan111全选-完全路径：" + myAllPath);
                dirAndFileDown(myAllPath, true, selectPathDir, selectPathFile);//下载代码
                for (int i = 0; i < apps.getDirPathList().size(); i++) {
                    System.out.println("选的文件夹的所有路径:" + i + ":" + apps.getDirPathList().get(i));
                }
            } else if (cou == allCount) { //也是完全 全选
                dirAndFileDown(myAllPath, true, selectPathDir, selectPathFile);//下载代码
                System.out.println("wanquan222全选-完全路径：" + myAllPath);
                for (int i = 0; i < apps.getDirPathList().size(); i++) {
                    System.out.println("选的文件夹的所有路径:" + i + ":" + apps.getDirPathList().get(i));
                }
            } else {
                //不是全选 把记录的文件夹 文件传给下载模块，
                // 便于区分仅仅下载传过去的文件夹 文件
                System.out.println("不是全选-完全路径：" + myAllPath);
                if (selectPathDir.size() > 0) {
                    System.out.println("不是全选-文件夹：" + selectPathDir.toString());
                }
                if (selectPathFile.size() > 0) {
                    System.out.println("不是全选-文件：" + selectPathFile.toString());
                }
                dirAndFileDown(myAllPath, false, selectPathDir, selectPathFile);//下载代码
                for (int i = 0; i < apps.getDirPathList().size(); i++) {
                    System.out.println("选的文件夹的所有路径:" + i + ":" + apps.getDirPathList().get(i));
                }
            }

            if (apps.getDirPathList() != null && apps.getDirPathList().size() > 0) {
                for (int i = 0; i < apps.getDirPathList().size(); i++) {
                    if (i == apps.getDirPathList().size() - 1) {
                        //最后一个路径是单独加上的，
                        // 循环的是最后一个路径，需要对比该路径下的文件哪些是勾选上的。
                        if (selectPathFile.size() > 0) {
                            FtpUtils.getAllfilepathInEveryDirLastOne(apps.getDirPathList().get(i));
                        }

                    } else {
                        //不取出最后一个路径下的所有文件。
                        FtpUtils.getAllfilepathInEveryDir(apps.getDirPathList().get(i));
                    }
                }
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    promptDialog.dismiss();
                }
            });

//            if (apps.getQueueDownloading() != null && apps.getQueueDownloading().size() > 0) {
//                for (int i = 0; i < apps.getQueueDownloading().size(); i++) {
//                    System.out.println("所有勾选的文件的下载路径（带文件名）：" + apps.getQueueDownloading().toArray()[i]);
//                }
//            }
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    XToastUtils.info("请勾选下载项！");
                    return;
                }
            });
        }
    }


    //点击下载按钮时调用的方法
    public void downloadclick(boolean isFailtask) {
        //记录当前点击按钮的下载任务的个数
        apps.setDownloadingFileCounts(apps.getQueueDownloading().size());

        //所有的任务个数（包含多次点击添加，包含重复的任务,不包含失败任务）
        apps.setallTask(apps.getQueue().size());

        final List<FTPClient> ftpList = new ArrayList();
        ftpList.clear();

        if (selectNumberthread == 1) {
            ftpClient2 = FtpUtils.connectServer(host, 21, user, pass, "");
            ftpList.add(ftpClient2);
        } else if (selectNumberthread == 2) {
            ftpClient2 = FtpUtils.connectServer(host, 21, user, pass, "");
            ftpClient3 = FtpUtils.connectServer(host, 21, user, pass, "");
            ftpList.add(ftpClient2);
            ftpList.add(ftpClient3);
        } else if (selectNumberthread == 3) {
            ftpClient2 = FtpUtils.connectServer(host, 21, user, pass, "");
            ftpClient3 = FtpUtils.connectServer(host, 21, user, pass, "");
            ftpClient4 = FtpUtils.connectServer(host, 21, user, pass, "");

            ftpList.add(ftpClient2);
            ftpList.add(ftpClient3);
            ftpList.add(ftpClient4);
        }


        //下载
        String portStr = new Integer(port).toString();
        String can[] = new String[4];
        can[0] = host;
        can[1] = portStr;
        can[2] = user;
        can[3] = pass;
        downString = "";
        int countT = 0;


        final Boolean[] xClinull = {false};
        //2.循环所有的文件进行下载
        Iterator<String> iterator = apps.getQueueDownloading().iterator();
        while (iterator.hasNext()) {
            //---------某个任务的下载路路径----------------
            String index = iterator.next();

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    synchronized (index) {
                        //取得下载的文件名
                        final String finalIndex = index.substring(index.lastIndexOf('/') + 1);
                        //取得下载文件名的路径（完整路径去掉文件名）
                        final String oneFilePath = index.replace(finalIndex, "");
                        System.out.println("for中的需下载文件名：" + finalIndex);


//                    String thname = Thread.currentThread().getName();
//                    System.out.println("当前执行的线程：" + thname);

                        FTPClient getListFtp = null;
                        synchronized (ftpList) {
                            if (ftpList.size() > 0) {
                                getListFtp = ftpList.get(0);
                                ftpList.remove(0);
                            }

                        }
                        //当外界的网络断开或者不好时，应该做如下处理-----------------
                        if (getListFtp == null) {
                            System.out.println("ftp为空，");
                            xClinull[0] = true;
                            downfail.add(oneFilePath + finalIndex);
                            apps.setQueueFail(downfail);
                            taskCom();
                            //                            return;
                        } else {
                            elseNotNUll(getListFtp, oneFilePath, finalIndex, ftpList);

                        }
                    }


                }

            };
            if (xClinull[0]) {
                xClinull[0] = false;
                continue;
            }
            fixedThreadPool.execute(runnable);


        }

//下载完 要把保存“成功”字样的全局变量清空
        globalVar.hashmap.clear();

    }


    public void elseNotNUll(FTPClient getListFtp, String oneFilePath, String
            finalIndex, List<FTPClient> ftpList) {
        //--------得到某个文件的远程size----------------------------------------
        System.out.println("==2222");
        try {
            getListFtp.changeWorkingDirectory(oneFilePath);
            System.out.println("==2888");

            FTPFile[] fileGetSize = new FTPFile[0];
            System.out.println("打印getListFtp.listFiles()：" + getListFtp.listFiles());
            fileGetSize = getListFtp.listFiles();

            System.out.println("==2222000");

            long singleFileSize = 0;
            System.out.println("==2222size：" + fileGetSize.length);

            for (int j = 0; j < fileGetSize.length; j++) {
                if (fileGetSize[j].getName().equals(finalIndex)) {
                    singleFileSize = fileGetSize[j].getSize();
                    System.out.println("远程size：" + singleFileSize);
                }
            }

            apps.removeOneQueueDownloading();

            threaddownload(finalIndex, oneFilePath, getListFtp, singleFileSize);

            ftpList.add(getListFtp);
        } catch (IOException e) {
            System.out.println("IO异常=======");
            downfail.add(oneFilePath + finalIndex);
            apps.setQueueFail(downfail);
            taskCom();
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("异常中断===========");
            e.printStackTrace();
        }
    }

    //点击文件夹列表项时；
    public void whileDir(int arg2) {
        String can[] = new String[5];
        can[0] = host;
        can[1] = new Integer(port).toString();
        can[2] = user;
        can[3] = pass;
        can[4] = currentFiles.get(arg2).filePath + "/";

        clickDir = currentFiles.get(arg2).filePath + "/";

        //在点击进入新文件夹之前记住现在的父亲路径是谁
//        GetTask task = new GetTask(selectFileActivity.this);
//        task.execute(can);


        LoginClaz();

        apps.setLastInfo(can);
        System.out.println("进入文件夹以后：" + apps.getInfo()[4]);
        ItemChooseData.clearFilePath();
        ItemChooseData.clearPathAndisfile();
        apps.setAllCount(0);

        ItemChooseData.clearFileName();
        globalVar.hashmap.clear();
        clearItemrefresh();
        MyAdapter.clearBoolList();
        checkbox_all.setChecked(false);//跳到下级目录，自动设为全不选


        for (int i = 0; i < currentFiles.size(); i++) {
            if (!MyAdapter.isSelected.get(i)) {
                checkbox_all.setChecked(false);
            }
        }
    }

    public void clearItemrefresh() {//把下载成功与否的数组还原到原始值
        for (int i = 0; i < itemFreshList.size(); i++) {
            itemFreshList.get(i).setSucfail("");
        }
    }


    //点击返回上级目录时：
    @Override
    public void onClick(View view) {
        if (apps.getPath().size() - 2 < 0) {
            XToastUtils.info("已经是顶级目录！");
        } else {
            String portStr = new Integer(port).toString();
            String can[] = new String[5];
            can[0] = host;
            can[1] = portStr;
            can[2] = user;
            can[3] = pass;
//            can[4] = currentParent;

            can[4] = apps.getPath().get(apps.getPath().size() - 2);
            System.out.println("整个路径数组：" + apps.getPath().toString());
            System.out.println(
                    "点击返回上级目录，跳转目的路径为：-2" + can[4] + "--size:" + apps.getPath().size()
            );


            GetTaskBackDir task = new GetTaskBackDir(this);
            task.execute(can);
            apps.setLastInfo(can);
            ItemChooseData.clearFileName();
            ItemChooseData.clearFilePath();
            ItemChooseData.clearPathAndisfile();
            apps.setAllCount(0);

            globalVar.hashmap.clear();
            clearItemrefresh();
            MyAdapter.clearBoolList();

            checkbox_all.setChecked(false);//跳到上级目录，自动设为全不选
            for (int i = 0; i < currentFiles.size(); i++) {
                if (!MyAdapter.isSelected.get(i)) {
                    checkbox_all.setChecked(false);
                }
            }

//            backDir.remove(backDir.size() - 1);
            apps.removeOne();
        }

    }

    // 初始化数据
    private void initDate() {
        String portStr = new Integer(port).toString();
        String can[] = new String[5];
        can[0] = host;
        can[1] = portStr;
        can[2] = user;
        can[3] = pass;
        can[4] = "/";
        System.out.println("设备更换了没？"+apps.isChangeShebei());
        if (apps.getInfo()[4] != null && apps.getBackAc()&&!apps.isChangeShebei()) {
            initDate2();
            ItemChooseData.clearPathAndisfile();


            ItemChooseData.clearFileName();
            ItemChooseData.clearFilePath();
            apps.setAllCount(0);
            globalVar.hashmap.clear();
            clearItemrefresh();
            MyAdapter.clearBoolList();
            checkbox_all.setChecked(false);

        } else {
//            GetTask task = new GetTask(selectFileActivity.this);
//            task.execute(can);


            LoginClaz();
            ItemChooseData.clearPathAndisfile();
            apps.setLastInfo(can);
        }

    }

    private void initDate2() {
//        GetTask task = new GetTask(selectFileActivity.this);
//        task.execute(apps.getInfo());
        //如果，换了车载设备，host，user等的值还是得用activit中初始获取到的值。

        //没有切换设备，则直接使用上次记录的值；

        //设定一个全局变量记录是否切换了设备。

        host = apps.getInfo()[0];
        user = apps.getInfo()[2];
        pass = apps.getInfo()[3];
        clickDir = apps.getInfo()[4];
        LoginClaz();
    }


    @Override
    protected void onResume() {
        super.onResume();
        initDate();
        System.out.println("onresume:" + apps.getBackAc());

    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onstart:" + apps.getBackAc());

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("onRestart:" + apps.getBackAc());

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    class GetTask extends AsyncTask<String, Void, List<FtpUtils.wxhFile>> {
        Context mContext;
        boolean sucornot = false;

        public GetTask(Context ctx) {
            mContext = ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected List<FtpUtils.wxhFile> doInBackground(String... Params) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    promptDialog.showLoading("加载中...");
                }
            });
            canGiveValue = true;
            downloadingFileCounts = 0;
//            apps.setDownloadingFileCounts(downloadingFileCounts);

            final String host = Params[0];
            String portStr = Params[1];
            final String user = Params[2];
            final String pass = Params[3];
            String path = Params[4];
            final FtpUtils util = new FtpUtils(selectFileActivity.this, true, selectPathDir, selectPathDir, apps);
            List<FtpUtils.wxhFile> list = null;
            int port = 21;
            port = Integer.parseInt(portStr);
            try {
                sucornot = util.connectServerTask(host, port, user, pass, "");
                if (!sucornot) {
                    //连接失败 则重连
                    final int finalPort = port;
                    Looper.prepare();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            XToastUtils.info("重连第1次");
                            sucornot = util.connectServerTask(host, finalPort, user, pass, "");
                            if (!sucornot) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        XToastUtils.info("重连第2次");
                                        sucornot = util.connectServerTask(host, finalPort, user, pass, "");
                                        promptDialog.dismiss();
                                        XToastUtils.info("请检查网络");
                                    }
                                }, 2000);
                            }
                        }
                    }, 1000);
                    Looper.loop();
                }
                System.out.println("连接测试1111：" + sucornot);

                FTPFile[] listedDirectories = null;

                list = util.getFileList(path);
                System.out.println("select中path值：" + path);

                for (int i = 0; i < list.size(); i++) {
                    itemFreshList.add(new FtpUtils.ItemFresh(i, ""));
                }

                //-----1-----首先得出本地完整存在的文件名，以及该文件名在整个listview中的position
                //远程
                List<FTPFile> taskFtpFiles = util.getOriginFileList(path, FtpUtils.ftpClient);
                //path为当前所点击文件夹的完整路径名称，从而进行本地文件夹的创建（存在则不创建）
                String[] strdir = path.split("/");
                String mylocaldirpath = new FtpUtils(selectFileActivity.this,
                        true, selectPathDir, selectPathDir, apps).crSDFileTask(strdir);
                System.out.println("本地的文件路径测试：" + mylocaldirpath);
                File localFile = new File(mylocaldirpath);
                File[] taskfiles = localFile.listFiles();

                List<String> posHaveDown = new ArrayList<>();
                //对比本地文件与远程文件的name，size是否一样，
                // 是则记录在posHaveDown中 在后面--2--中再找position
                if (taskfiles != null && taskfiles.length > 0) {
                    for (int j = 0; j < taskfiles.length; j++) {
                        if (taskfiles[j].isFile()) {
                            for (int k = 0; k < taskFtpFiles.size(); k++) {
                                if (taskfiles[j].getName().equals(taskFtpFiles.get(k).getName()) &&
                                        taskfiles[j].length() >= taskFtpFiles.get(k).getSize()) {
                                    posHaveDown.add(taskfiles[j].getName());
                                }
                            }
                        }
                    }
                }
                //--------1-------------------------------------------------------------


                if (list.size() > 0) {
                    System.out.println("点击文件夹时，记录父路径：" + path);
                    apps.setPath(path);//每次点击文件夹进入下级目录时，都记录父路径
//                    backDir.add(path);
                }
                //附带父亲文件夹的路径进去
                list.add(new FtpUtils.wxhFile(path, "", 2, "", ""));


                List<FtpUtils.wxhFile> listFile = new ArrayList<>();
                List<FtpUtils.wxhFile> listDir = new ArrayList<>();

                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isParent) {
                        parentPath = list.get(i).filePath;
                        list.remove(i);
                        System.out.println("parent打印：" + parentPath);
                    }

                }
                System.out.println("刚开始的个数：" + list.size());
                List<FtpUtils.wxhFile> removeFileIndex = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isFile) {//是文件
                        listFile.add(list.get(i));
                        removeFileIndex.add(list.get(i));//记住待删除的文件对象。
                    }
                }
                System.out.println("文件个数：" + listFile.size());
                if (removeFileIndex.size() > 0) {
                    for (int i = 0; i < removeFileIndex.size(); i++) {
                        list.remove(removeFileIndex.get(i));//删除所有的文件
                    }
                }

                dirSize = list.size();//文件夹的个数
                System.out.println("文件夹个数：" + list.size());
                list.addAll(listFile);//文件夹与文件集合的拼接
                System.out.println("拼接后个数：" + list.size());


                //-----2-----在这里才添加“已下载”
                if (posHaveDown.size() > 0 && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        for (int j = 0; j < posHaveDown.size(); j++) {
                            if (posHaveDown.get(j).equals(list.get(i).filename)) {
                                System.out.println("测试最终位置结果：" + i);
                                itemFreshList.get(i).setSucfail("已下载");
                            }
                        }
                    }
                }

                //------2---------------------------------------------------------------

                for (int i = 0; i < list.size(); i++) {
                    System.out.println("大小：" + list.get(i).filesize + "--" + "时间：" + list.get(i).filetime);
                }
            } catch (Exception e) {
                System.out.println("开启连接出错");
                e.printStackTrace();
            } finally {
                try {
                    util.closeServer();
                } catch (Exception e) {
                    e.printStackTrace();

                }
                return list;
            }
        }

        protected void onPostExecute(List<FtpUtils.wxhFile> list) {
            SharedPreferences pref = selectFileActivity.this.getSharedPreferences("mypath", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("pathname", parentPath);
            editor.commit();
            System.out.println("这个父亲文件夹为：" + parentPath);
            System.out.println("处理结果函数内部");

            if (!sucornot) {
                XToastUtils.info("网络连接不佳!");
                promptDialog.dismiss();
                return;
            }
            if (list == null || list.size() == 0) {
                promptDialog.dismiss();
                AlertUtils.alertNoListener(selectFileActivity.this, "该文件夹为空文件夹！");
            } else {
                //更新文件list
                currentFiles = list;
                movingList = list;
                apps.setDownlist(list);
                System.out.println("刚开始的父路径：" + list.get(0).filePath);

                // 实例化自定义的MyAdapter
                mAdapter = new MyAdapter(movingList, itemFreshList, selectFileActivity.this,
                        globalCountValue, globalVar, downloadingMap);
                // 绑定Adapter
                lv.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        promptDialog.dismiss();
                    }
                });
            }

        }
    }


    class GetTaskBackDir extends AsyncTask<String, Void, List<FtpUtils.wxhFile>> {
        Context mContext;
        boolean sucornot = false;

        public GetTaskBackDir(Context ctx) {
            mContext = ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected List<FtpUtils.wxhFile> doInBackground(String... Params) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    promptDialog.showLoading("加载中...");
                }
            });
            canGiveValue = true;
            downloadingFileCounts = 0;
            String host = Params[0];
            String portStr = Params[1];
            String user = Params[2];
            String pass = Params[3];
            String path = Params[4];
            FtpUtils util = new FtpUtils(selectFileActivity.this, true, selectPathDir, selectPathDir, apps);
            List<FtpUtils.wxhFile> list = null;
            int port = 21;
            port = Integer.parseInt(portStr);
            try {
//                util.connectServer(host, port, user, pass, "");
                sucornot = util.connectServerTask(host, port, user, pass, "");
                if (!sucornot) {
                    //连接失败 则2秒重连一次
                    Thread.sleep(500);//再连一次
                    sucornot = util.connectServerTask(host, port, user, pass, "");
                    if (!sucornot) {
                        Thread.sleep(2000);//2秒后再连一次
                        sucornot = util.connectServerTask(host, port, user, pass, "");
                    }
                }
                list = util.getFileList(path);
                for (int i = 0; i < list.size(); i++) {
                    itemFreshList.add(new FtpUtils.ItemFresh(i, ""));
                }

                //-----1-----首先得出本地完整存在的文件名，以及该文件名在整个listview中的position
                //远程
                List<FTPFile> taskFtpFiles = util.getOriginFileList(path, FtpUtils.ftpClient);
                //path为当前所点击文件夹的完整路径名称，从而进行本地文件夹的创建（存在则不创建）
                String[] strdir = path.split("/");
                String mylocaldirpath = new FtpUtils(selectFileActivity.this,
                        true, selectPathDir, selectPathDir, apps).crSDFileTask(strdir);
                System.out.println("本地的文件路径测试：" + mylocaldirpath);
                File localFile = new File(mylocaldirpath);
                File[] taskfiles = localFile.listFiles();

                List<String> posHaveDown = new ArrayList<>();
                //对比本地文件与远程文件的name，size是否一样，是则记录在posHaveDown中 在后面--2--中再找position
                if (taskfiles != null && taskfiles.length > 0) {
                    for (int j = 0; j < taskfiles.length; j++) {
                        if (taskfiles[j].isFile()) {
                            for (int k = 0; k < taskFtpFiles.size(); k++) {
                                if (taskfiles[j].getName().equals(taskFtpFiles.get(k).getName()) &&
                                        taskfiles[j].length() >= taskFtpFiles.get(k).getSize()) {
                                    posHaveDown.add(taskfiles[j].getName());
                                }
                            }
                        }
                    }
                }

                //--------1-------------------------------------------------------------


                //附带父亲文件夹的路径进去
                list.add(new FtpUtils.wxhFile(path, "", 2, "", ""));


                List<FtpUtils.wxhFile> listFile = new ArrayList<>();

                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isParent) {
                        parentPath = list.get(i).filePath;
                        list.remove(i);
                    }
                }
                System.out.println("刚开始的个数：" + list.size());
                List<FtpUtils.wxhFile> removeFileIndex = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isFile) {//是文件
                        listFile.add(list.get(i));
                        removeFileIndex.add(list.get(i));//记住待删除的文件对象。
                    }
                }
                System.out.println("文件个数：" + listFile.size());


                for (int i = 0; i < removeFileIndex.size(); i++) {
                    list.remove(removeFileIndex.get(i));//删除所有的文件
                }

                System.out.println("文件夹个数：" + list.size());
                list.addAll(listFile);//文件夹与文件集合的拼接
                for (int i = 0; i < list.size(); i++) {
                    System.out.println(
                            "文件夹文件顺序：" + list.get(i).filename
                    );
                }
                System.out.println("拼接后个数：" + list.size());

                //-----2-----在这里才添加“已下载”
                if (posHaveDown.size() > 0 && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        for (int j = 0; j < posHaveDown.size(); j++) {
                            if (posHaveDown.get(j).equals(list.get(i).filename)) {
                                System.out.println("测试最终位置结果：" + i);
                                itemFreshList.get(i).setSucfail("已下载");
                            }
                        }
                    }
                }
                //------2---------------------------------------------------------------
            } catch (Exception e) {
                System.out.println("开启连接出错");
                e.printStackTrace();
            } finally {
                try {
                    util.closeServer();
                } catch (Exception e) {
                    e.printStackTrace();

                }
                return list;
            }
        }

        protected void onPostExecute(List<FtpUtils.wxhFile> list) {
            SharedPreferences pref = selectFileActivity.this.getSharedPreferences("mypath", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("pathname", parentPath);
            editor.commit();
            System.out.println("这个父亲文件夹为：" + parentPath);
            System.out.println("处理结果函数内部");
            if (!sucornot) {
                XToastUtils.info("网络连接不佳!");
                promptDialog.dismiss();
                return;
            }
            if (list == null || list.size() == 0) {
                promptDialog.dismiss();

                AlertUtils.alertNoListener(selectFileActivity.this, "该文件夹为空文件夹！");


            } else {
                System.out.println("找到了！！");
                //更新文件list
                currentFiles = list;
                movingList = list;
                System.out.println("刚开始的父路径：" + list.get(0).filePath);
                //调用刷新来显示我们的列表
//                inflateListView(list,parentPath);

                // 实例化自定义的MyAdapter
                mAdapter = new MyAdapter(list, itemFreshList, selectFileActivity.
                        this, globalCountValue, globalVar, downloadingMap);
                // 绑定Adapter
                lv.setAdapter(mAdapter);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        promptDialog.dismiss();
                    }
                });
            }

        }
    }


//    public static void verifyStoragePermissions(Activity activity) {
//        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(
//                    activity,
//                    PERMISSIONS_STORAGE,
//                    REQUEST_EXTERNAL_STORAGE
//            );
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ItemChooseData.clearFilePath();
        ItemChooseData.clearPathAndisfile();
        apps.setAllCount(0);

        ItemChooseData.clearFileName();
        globalVar.hashmap.clear();
        clearItemrefresh();
        MyAdapter.clearBoolList();
        checkbox_all.setChecked(false);//跳到下级目录，自动设为全不选
//        apps.clearPath();//退出activity时，应清除记录的路径
        System.out.println("selectactivity退出了。");
    }

    private void addCart(ImageView iv) {
//      一、创造出执行动画的主题---imageview
        //代码new一个imageview，图片资源是上面的imageview的图片
        // (这个图片就是执行动画的图片，从开始位置出发，经过一个抛物线（贝塞尔曲线），移动到购物车里)
        final ImageView goods = new ImageView(selectFileActivity.this);
        goods.setImageDrawable(iv.getDrawable());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        llAll.addView(goods, params);

//        二、计算动画开始/结束点的坐标的准备工作
        //得到父布局的起始点坐标（用于辅助计算动画开始/结束时的点的坐标）
        int[] parentLocation = new int[2];
        llAll.getLocationInWindow(parentLocation);

        //得到商品图片的坐标（用于计算动画开始的坐标）
        int startLoc[] = new int[2];
        iv.getLocationInWindow(startLoc);

        //得到购物车图片的坐标(用于计算动画结束后的坐标)
        int endLoc[] = new int[2];
        imageViewDown.getLocationInWindow(endLoc);


//        三、正式开始计算动画开始/结束的坐标
        //开始掉落的商品的起始点：商品起始点-父布局起始点+该商品图片的一半
        float startX = startLoc[0] - parentLocation[0] + iv.getWidth() / 2;
        float startY = startLoc[1] - parentLocation[1] + iv.getHeight() / 2;

        //商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
        float toX = endLoc[0] - parentLocation[0] + imageViewDown.getWidth() / 5;
        float toY = endLoc[1] - parentLocation[1];

//        四、计算中间动画的插值坐标（贝塞尔曲线）（其实就是用贝塞尔曲线来完成起终点的过程）
        //开始绘制贝塞尔曲线
        Path path = new Path();
        //移动到起始点（贝塞尔曲线的起点）
        path.moveTo(startX, startY);
        //使用二次萨贝尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        path.quadTo((startX + toX) / 2, startY, toX, toY);
        //mPathMeasure用来计算贝塞尔曲线的曲线长度和贝塞尔曲线中间插值的坐标，
        // 如果是true，path会形成一个闭环
        mPathMeasure = new PathMeasure(path, false);

        //★★★属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算，获取中间过程的距离值）
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(1000);
        // 匀速线性插值器
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 当插值计算进行时，获取中间的每个值，
                // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
                float value = (Float) animation.getAnimatedValue();
                // ★★★★★获取当前点坐标封装到mCurrentPosition
                // boolean getPosTan(float distance, float[] pos, float[] tan) ：
                // 传入一个距离distance(0<=distance<=getLength())，然后会计算当前距
                // 离的坐标点和切线，pos会自动填充上坐标，这个方法很重要。
                mPathMeasure.getPosTan(value, mCurrentPosition, null);//mCurrentPosition此时就是中间距离点的坐标值
                // 移动的商品图片（动画图片）的坐标设置为该中间点的坐标
                goods.setTranslationX(mCurrentPosition[0]);
                goods.setTranslationY(mCurrentPosition[1]);
            }
        });
//      五、 开始执行动画
        valueAnimator.start();

//      六、动画结束后的处理
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            //当动画结束后：
            @Override
            public void onAnimationEnd(Animator animation) {
                // 购物车的数量加1
                // 把移动的图片imageview从父布局里移除
                llAll.removeView(goods);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void dirAndFileDown(String myAllPath, boolean allselect, List<
            String> dir, List<String> file) {
        String portStr = new Integer(port).toString();
        String can[] = new String[4];
        can[0] = host;
        can[1] = portStr;
        can[2] = user;
        can[3] = pass;

        DirDownTask dirtask = new DirDownTask(selectFileActivity.this, myAllPath,
                Environment.getExternalStorageDirectory().getPath(), allselect, dir, file, apps);
        dirtask.doInBackground(can);
        if (apps.getDirPathList().size() > 0) {
            String tmp = apps.getDirPathList().get(0);
            String oo = tmp.substring(tmp.lastIndexOf("/", tmp.lastIndexOf("/") - 1) + 1);
            System.out.println("oo:" + oo);
            tmp = tmp.replace(oo, "");
            System.out.println("tmp:" + tmp);
            apps.setDirPathList(tmp);
        }
    }

    //执行添加购物车动画
    public void addCartAni() {
        imageViewDown.setEnabled(false);
        addCart(img);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addCart(img);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //跳转到下载进度展示的acticity
//                        Intent intent = new Intent(selectFileActivity.this, DownloadingFileListActivity.class);
//                        startActivity(intent);
                        showBottomSheetListDialog();
                        imageViewDown.setEnabled(true);

                    }
                }, 1000);
            }
        }, 500);
    }


    //======================下载进度展示区域=============================================
    //===================================================================
    BottomSheetDialog dialogfail;
    BottomSheetDialog dialog;
    View view;
    int countShowfail = 0;

    public void taskCom() {
        int alltask = apps.getallTask();
        int haveDownloadingFileCounts = apps.getallTaskDowned();
        int downFail = apps.getQueueFail().size();
        System.out.println("所有：" + alltask);
        System.out.println("成功下载：" + haveDownloadingFileCounts);
        System.out.println("下载失败的：" + downFail);

        if (downFail != 0 && (haveDownloadingFileCounts <= alltask) &&
                (downFail + haveDownloadingFileCounts == alltask)
                && (countShowfail == 0)
        ) {
            ++countShowfail;
            apps.clearQueueDownloading();
            System.out.println("有下载失败的=======");
            dialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CookieBar.builder(selectFileActivity.this)
                            .setMessage("提示\n\n某些文件下载失败，请重新下载！")
                            .setDuration(-1)
                            .setActionWithIcon(R.mipmap.ic_action_close, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
//                                faildownagainOnclick();
                                    setHavedown2();

                                    showFailBottomSheetListDialog();
                                }
                            })
                            .show();
                }
            });
        }
        //有下载失败的，且成功的个数等于总个数，这种情况属于特殊情况处理
        //可能是由于下载进度计算时，本来没有到100，却按100算了，此时，本地文件大小可能就小于远程大小
        //导致软件记录当前文件为失败文件
        if (downFail != 0 && (haveDownloadingFileCounts == alltask)
        ) {
            apps.clearQueueDownloading();

            dialog.dismiss();
            System.out.println("有下载失败的，且所有任务下载完毕-等于成功的个数");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    CookieBar.builder(selectFileActivity.this)
                            .setMessage("提示\n\n某些文件下载失败，请重新下载！")
                            .setDuration(-1)
                            .setActionWithIcon(R.mipmap.ic_action_close, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    setHavedown2();

                                    showFailBottomSheetListDialog();

                                }
                            })
                            .show();
                }
            });

        }

        if (downFail == 0 && (haveDownloadingFileCounts <= alltask) &&
                (downFail + haveDownloadingFileCounts == alltask)
        ) {
            System.out.println("没有==下载失败的，且所有任务下载完毕：" + apps.getShowDia());
            apps.clearQueueDownloading();

            if (dialog!=null){
                dialog.dismiss();
            }
            JinduInitWhileAllSuccess();//成功下载后，再把列表中的所有文件进度重新赋值为100%
            //以免下载完的文件 依然显示 下载中。
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    XToastUtils.success("文件下载完毕！");

                    CookieBar.builder(selectFileActivity.this)
                            .setMessage("提示\n\n文件下载完毕！")
                            .setDuration(-1)
                            .setActionWithIcon(R.mipmap.ic_action_close, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    setHavedown2();

                                }
                            })
                            .show();
                }
            });

        }

    }

    public void taskComToast() {
        int alltask = apps.getallTask();
        int haveDownloadingFileCounts = apps.getallTaskDowned();
        int downFail = apps.getQueueFail().size();
        System.out.println("所有：" + alltask);
        if (downFail == 0 && (haveDownloadingFileCounts <= alltask) &&
                (downFail + haveDownloadingFileCounts == alltask)
        ) {
            apps.clearQueueDownloading();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    XToastUtils.success("文件下载完毕！");
                }
            });

        }

    }


    private void showBottomSheetListDialog() {

        dialog = new BottomSheetDialog(selectFileActivity.this);
        view = LayoutInflater.from(selectFileActivity.this).inflate(R.layout.dialog_bottom_sheet, null);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        initDialogList(recyclerView);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        view.findViewById(R.id.tv_failtask).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (apps.getQueueFail().size() > 0) {
                    dialog.dismiss();
                    showFailBottomSheetListDialog();
                } else {
                    XToastUtils.info("当前没有下载失败的任务");
                }

            }
        });


        Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 10000:
                        TextView tv = (TextView) view.findViewById(R.id.tv_speed);
                        tv.setText(msg.obj.toString());
                        break;
                }
                super.handleMessage(msg);
            }
        };
        new netUti(selectFileActivity.this, mHandler).startShowNetSpeed();

    }

    private void initDialogList(RecyclerView recyclerView) {
        LinearLayoutManager manager = new LinearLayoutManager(selectFileActivity.this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addItemDecoration(new DividerItemDecoration(
                selectFileActivity.this, DividerItemDecoration.HORIZONTAL));

        adapter = new CommonRecyclerViewAdapter(apps.getQueue(),
                selectFileActivity.this,
                apps.getDownBean());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }

    CheckBox checkB = null;


    private void showFailBottomSheetListDialog() {
        dialogfail = new BottomSheetDialog(selectFileActivity.this);
        View view = LayoutInflater.from(selectFileActivity.this).
                inflate(R.layout.fail_dialog_bottom_sheet, null);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewFail);
        initFailDialogList(recyclerView);
        dialogfail.setContentView(view);
        dialogfail.setCancelable(true);
        dialogfail.setCanceledOnTouchOutside(true);
        dialogfail.show();

        view.findViewById(R.id.im_backArrofail).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogfail.dismiss();

                showBottomSheetListDialog();
            }
        });

        //重新下载
        downloadFailtaskAgain(view);

        checkB(view);//全选监听

    }

    private void initFailDialogList(RecyclerView recyclerView) {
        LinearLayoutManager manager = new LinearLayoutManager(selectFileActivity.this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addItemDecoration(new DividerItemDecoration(
                selectFileActivity.this, DividerItemDecoration.HORIZONTAL));

        adapterfail = new FailRecyclerViewAdapter(selectFileActivity.this,
                apps, apps.getQueueFail());
        recyclerView.setAdapter(adapterfail);
        adapterfail.notifyDataSetChanged();


    }

    public void downloadFailtaskAgain(View view) {
        view.findViewById(R.id.downloadbtnagaintask).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (apps.getQueueFail() == null || !(apps.getQueueFail().size() > 0)) {
                    ItemChooseDataFail.clearFilePath();
                }
                int sizeLs = ItemChooseDataFail.getFilePath().size();

                //temChooseDataFail.getFilePath()存的是文件完整下载路径（带文件名）
                if (sizeLs > 0) {
                    if (!isNetworkAvailable()) {
                        XToastUtils.info("当前网络状况不佳！");
                        return;
                    }

                    //把失败列表Tab下的失败列表展示清除(勾选的才删除)
                    apps.clearQueueFail(ItemChooseDataFail.getFilePath());

                    //下载失败的任务时，应把勾选的失败的任务在 已被线程处理的任务列表中 清除。
                    apps.clearQueueSelect(ItemChooseDataFail.getFilePath());

                    System.out.println(
                            "DownloadingFileListActivity是否清空：" + apps.getQueueFail().size()
                    );
                    apps.setQueueDowning(ItemChooseDataFail.getFilePath());
                    apps.setagainDown(true);

                    //显示下载列表中的进度
                    dialogfail.dismiss();
                    dialog.show();
                    //显示下载列表中的进度
//                    downingagainOnclick();
//                    mFailAdapter.notifyDataSetChanged();

                } else {
                    XToastUtils.info("请勾选下载项");
                }
            }
        });
    }

    public void checkB(View view) {
        checkB = view.findViewById(R.id.checkbox_all_again);
        checkB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (apps.getQueueFail() != null) {
                    ItemChooseDataFail.clearFilePath();

                    if (isChecked) {
                        apps.setCheckfail(1);
                        for (int i = 0; i < apps.getQueueFail().size(); i++) {
                            new FailDownAdapter(selectFileActivity.this,
                                    apps, apps.getQueueFail()).checks[i] = true;
//                            FailDownAdapter.getIsSelected().put(i, true);
                        }
                        adapterfail.notifyDataSetChanged();

                    } else {
                        apps.setCheckfail(0);

                        for (int i = 0; i < apps.getQueueFail().size(); i++) {
//                            FailDownAdapter.getIsSelected().put(i, false);
                            new FailDownAdapter(selectFileActivity.this,
                                    apps, apps.getQueueFail()).checks[i] = false;

                        }
                        adapterfail.notifyDataSetChanged();
                    }

                }
            }

        });

    }


}
