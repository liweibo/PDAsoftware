package com.crrc.pdasoftware.wuxianzhuanchu.all.activitys;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.wuxianzhuanchu.all.utils.AlertUtils;
import com.crrc.pdasoftware.wuxianzhuanchu.all.utils.FTPManager;
import com.githang.statusbar.StatusBarCompat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import me.leefeng.promptlibrary.PromptDialog;

public class FTPLoginActivity extends AppCompatActivity {

    private static final int TASK_ACTION = 0;
    EditText ipadd;
    EditText user;
    EditText psw;
    Button loginButton;
    private ImageView im_backArroFTPLOGIN;
    private Socket socket;
    private OutputStream outputStream;
    private InputStream is;
    private InputStreamReader isr;
    private BufferedReader br;

    private PromptDialog promptDialog;


    public boolean haveCheck = false;
    private CheckBox cb_edrm;
    private String mhost;
    private String muser;
    private String mpass;


    public Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                promptDialog.dismiss();

                Intent intent = new Intent(FTPLoginActivity.this,
                        selectFileActivity.class);
                //准备进入选择界面并且准备好参数
                intent.putExtra("host", mhost);
                intent.putExtra("user", muser);
                intent.putExtra("pass", mpass);
                intent.putExtra("port", Integer.parseInt("21"));
                startActivity(intent);
            } else if (msg.what == 0) {
                AlertUtils.alertNoListener(FTPLoginActivity.this, "连接失败，您可以再次连接");
                //连不上设备  尝试手动式输入ip。设置一个开关控制是否显示 ip输入框
                promptDialog.dismiss();
            }
            return false;
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftplogin);
        promptDialog = new PromptDialog(this);

        ipadd = findViewById(R.id.ipaddressss);
        user = findViewById(R.id.usernamess);
        psw = findViewById(R.id.passwordss);
        loginButton = findViewById(R.id.loginss);
        im_backArroFTPLOGIN = findViewById(R.id.im_backArroFTPLOGIN);
        cb_edrm = findViewById(R.id.cb_edrm);

//        this.getSupportActionBar().hide();
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.colorAccent), true);

        setClick();

    }


    public void setClick() {

        SharedPreferences sharedPreferences = getSharedPreferences("ftplogininfo",
                Activity.MODE_PRIVATE);
        String ipadd = sharedPreferences.getString("ipadd", "");
        String users = sharedPreferences.getString("user", "");
        String psws = sharedPreferences.getString("psw", "");

        this.ipadd.setText(ipadd);
        this.user.setText(users);
        this.psw.setText(psws);

        //登录
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        im_backArroFTPLOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FTPLoginActivity.this,
                        HomeActivity.class));

            }
        });

        cb_edrm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    haveCheck = true;
                    System.out.println("---是edrm");
                } else {
                    haveCheck = false;
                    System.out.println("---不是edrm");
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        cb_edrm.setChecked(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cb_edrm.setChecked(false);

    }

    public boolean infoIsComple() {
        boolean isCom = false;


        String addre = ipadd.getText().toString().trim();
        String usr = user.getText().toString().trim();
        String ps = psw.getText().toString().trim();


        if (addre.length() > 0 && usr.length() > 0 && ps.length() > 0) {
            isCom = true;
            SharedPreferences pref = FTPLoginActivity.this.getSharedPreferences("ftplogininfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("ipadd", addre);
            editor.putString("user", usr);
            editor.putString("psw", ps);
            editor.commit();
        } else {
            isCom = false;
        }
        return isCom;
    }

    public void login() {
        if (infoIsComple()) {
            SharedPreferences sharedPreferences = getSharedPreferences("ftplogininfo",
                    Activity.MODE_PRIVATE);
            String ipadd = sharedPreferences.getString("ipadd", "");
            String users = sharedPreferences.getString("user", "");
            String psws = sharedPreferences.getString("psw", "");

            String can[] = new String[4];
            can[0] = ipadd;
            can[1] = "21";
            can[2] = users;
            can[3] = psws;

            mhost = ipadd;
            muser = users;
            mpass = psws;

            promptDialog.showLoading("登录中...", false);
            LoginClaz();
//            LogTask task = new LogTask(FTPLoginActivity.this);
//            task.execute(can);
        } else {
            Toast.makeText(FTPLoginActivity.this, "请输入完整信息", Toast.LENGTH_SHORT).show();
        }
    }

    public void LoginClaz() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean re = false;
                if (haveCheck) {
                    re = checkLogin(mhost, muser, mpass, 8001);
                } else {
                    re = nocheckLogin(mhost, muser, mpass);
                }
                if (re) {
                    mHandler.sendEmptyMessage(1);
                } else {
                    //登陆失败
                    mHandler.sendEmptyMessage(0);
                }

            }
        }).start();
    }

    public class LogTask extends AsyncTask<String, Void, Boolean> {
        Context mContext;
        String host;
        String user;
        String pass;

        public LogTask(Context ctx) {
            mContext = ctx;
            System.out.println("登陆-------------1---------");

        }

        protected Boolean doInBackground(String... Params) {
            host = Params[0];
            user = Params[2];
            pass = Params[3];
            System.out.println("host:" + host);
            System.out.println("user:" + user);
            System.out.println("pass:" + pass);
            if (haveCheck) {
                return checkLogin(host, user, pass, 8001);

            } else {
                return nocheckLogin(host, user, pass);
            }
        }

        protected void onPostExecute(Boolean flag) {
            if (flag) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        promptDialog.dismiss();
                    }
                });

                Intent intent = new Intent(FTPLoginActivity.this,
                        selectFileActivity.class);
                //准备进入选择界面并且准备好参数
                intent.putExtra("host", host);
                intent.putExtra("user", user);
                intent.putExtra("pass", pass);
                intent.putExtra("port", Integer.parseInt("21"));
                startActivity(intent);
            } else {
                AlertUtils.alertNoListener(FTPLoginActivity.this, "连接失败，您可以再次连接");
                //连不上设备  尝试手动式输入ip。设置一个开关控制是否显示 ip输入框
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        promptDialog.dismiss();
                    }
                });
            }
        }


    }

    @Override
    public void onBackPressed() {
        if (promptDialog.onBackPressed())
            super.onBackPressed();
    }

    //校验 只有edrm模块需要校验  其他模块直接登陆即可。
    public boolean nocheckLogin(String _host, String _user, String _pass) {
        String host1 = _host;
        String user1 = _user;
        String pass1 = _pass;
        int port1 = Integer.parseInt("21");
        boolean flag = false;
        FTPManager manager = new FTPManager();
        System.out.println("登陆-------------2---------");
        try {
            flag = manager.connect(host1, port1, user1, pass1);
            if (!flag) return flag;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //最后不管是否连接上了 都关闭一下
            try {
                manager.closeFTP();
            } catch (Exception e1) {

            }
            return flag;
        }

    }


    //校验 只有edrm模块需要校验  其他模块直接登陆即可。
    public boolean checkLogin(String _host, String _user, String _pass, int socketPort) {
        String host1 = _host;
        String user1 = _user;
        String pass1 = _pass;
        int port1 = Integer.parseInt("21");

        boolean flag = false;
        FTPManager manager = new FTPManager();
        try {
            // 创建Socket对象 & 指定服务端的IP 及 端口号
//            socket = new Socket("10.0.1.5", 8001);
            socket = new Socket(host1, socketPort);

            // 判断客户端和服务器是否连接成功
            System.out.println("连接" + socket.isConnected());
            if (socket.isConnected()) {
                // 步骤1：从Socket 获得输出流对象OutputStream
                // 该对象作用：发送数据
                outputStream = socket.getOutputStream();

                // 步骤2：写入需要发送的数据到输出流对象中
                outputStream.write(("Request").getBytes("US-ASCII"));
                // 特别注意：数据的结尾加上换行符才可让服务器端的readline()停止阻塞

                // 步骤3：发送数据到服务端
                outputStream.flush();
                System.out.println("发request:" + Arrays.toString(("Request").getBytes("US-ASCII")));

                System.out.println("发");

//                                接收

                // 步骤1：创建输入流对象InputStream
                is = socket.getInputStream();

                // 步骤2：创建输入流读取器对象 并传入输入流对象
                // 该对象作用：获取服务器返回的数据
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);

//                                List<char> list = new ArrayList<>();
                char[] ch = new char[40];
                String strs = "";//Response
                String strs1 = "";//Response后的字符

                for (int i = 1; i < 41; i++) {
                    ch[i - 1] = (char) br.read();
                    if (i < 9) {
                        strs += ch[i - 1];

                    }
                    if (i > 8) {
                        strs1 += ch[i - 1];

                    }
                    System.out.println("接收br第" + i + ":" + ch[i - 1]);
                }


                System.out.println("数组：" + Arrays.toString(ch));
                System.out.println("str前：" + strs);
                System.out.println("str后：" + strs1);

                //////////////////////////////第二次发//////////////////////////////////////
                char[] ch2 = new char[40];
                String strs2 = "";
                if (strs.equals("Response")) {
                    System.out.println("能收到Response");

                    //对Response后的字符转换成十六进制字节数组
                    if (strs1 == null || strs1.trim().equals("")) {
                        return false;
                    }

                    byte[] bytes16 = new byte[strs1.length() / 2];
                    for (int i = 0; i < strs1.length() / 2; i++) {
                        String subStr = strs1.substring(i * 2, i * 2 + 2);
                        bytes16[i] = (byte) Integer.parseInt(subStr, 16);
                    }

                    System.out.println("bytes16[i]:" + Arrays.toString(bytes16));


                    //加密。。。。。。

                    String strTo = "5531129003a313b176c539d223da4182";
                    byte[] bytess16 = new byte[strTo.length() / 2];
                    for (int i = 0; i < strTo.length() / 2; i++) {
                        String subStr = strTo.substring(i * 2, i * 2 + 2);
                        bytess16[i] = (byte) Integer.parseInt(subStr, 16);
                    }
                    System.out.println("strToEny:" + Arrays.toString(bytess16));
                    //先根据字节数组生成 AES密钥对象
                    // 再使用 AES密钥对象 加密数据。

                    try {
                        byte[] byteEny = HomeActivity.encrypt(bytess16, bytes16);
                        System.out.println("byteEny:" + Arrays.toString(byteEny));

                        String strEny = HomeActivity.byte2hex(byteEny);
                        System.out.println("把字节数组转换成16进值字符串:" + strEny);

//                                        把字节数组转换成16进值字符串
//                                        String strEny = byteEny.toString();
                        int[] intEny = new int[byteEny.length];

                        for (int i = 0; i < byteEny.length; i++) {
                            if (byteEny[i] < 0) {
                                intEny[i] = 256 + byteEny[i];
                            } else {
                                intEny[i] = byteEny[i];

                            }
                        }
                        System.out.println("intEny:" + Arrays.toString(intEny));
                        String strjia = "";
                        for (int i = 0; i < intEny.length; i++) {
                            strjia += (char) intEny[i];
                        }


                        System.out.println("strjia:" + strjia);

                        String sendstrpsw = "Accredit" + strEny;
                        byte[] byteEnys = sendstrpsw.getBytes("US-ASCII");

                        outputStream = socket.getOutputStream();
                        outputStream.write(byteEnys);
                        outputStream.flush();


                        System.out.println("发jiami");


//                                接收
                        is = socket.getInputStream();
                        isr = new InputStreamReader(is);
                        br = new BufferedReader(isr);
                        System.out.println("收jiami");
                        String strOk = "";
                        for (int i = 1; i < 25; i++) {
                            char jiami = (char) br.read();
                            strOk += jiami;
                            System.out.println("////：" + jiami);
                        }
                        System.out.println("加密后收到数据：" + strOk);

                        if (strOk.indexOf("ResponseAccreditOK") == 0) {
                            System.out.println("接受数据正常.....");


                        }

                    } catch (Exception e) {

                    }

                    System.out.println("字节数组：" + Arrays.toString(bytes16));


                    outputStream = socket.getOutputStream();
                    outputStream.write(("WriteFaultToFile").getBytes("US-ASCII"));
                    outputStream.flush();

                    System.out.println("发WriteFaultToFile");


//                                接收
                    is = socket.getInputStream();
                    isr = new InputStreamReader(is);
                    br = new BufferedReader(isr);
                    String str22 = "";
                    for (int i = 1; i < 41; i++) {
                        ch2[i - 1] = (char) br.read();
                        str22 += ch2[i - 1];

                        System.out.println("接收br第" + i + ":" + ch2[i - 1]);
                    }
                    System.out.println("WaiteForWrite打印：" + str22);
                    if (str22.indexOf("WaiteForWrite") == 0 || str22.indexOf("WaiteForWrite_e") == 0) {
                        System.out.println("str22.indexOf(\"WaiteForWrite\")==0成立.....");


                        try {
                            flag = manager.connect(host1, port1, user1, pass1);
                            if (!flag) return false;
                            System.out.println("我的了flag：" + flag);


                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            //最后不管是否连接上了 都关闭一下
                            try {
                                manager.closeFTP();
                            } catch (Exception e1) {

                            }
                        }


                    }

                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //最后不管是否连接上了 都关闭一下
            try {
                manager.closeFTP();
            } catch (Exception e1) {

            }
            return flag;
        }
    }


    public static byte[] encrypt(byte[] plainBytes, byte[] key) throws Exception {

        // 获取 AES 密码器
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");


        int blockSize = cipher.getBlockSize();
        int length = plainBytes.length;
        //计算需填充长度
        if (length % blockSize != 0) {
            length = length + (blockSize - (length % blockSize));
        }
        byte[] plaintext = new byte[length];
        //填充
        System.arraycopy(plainBytes, 0, plaintext, 0, plainBytes.length);
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");


        // 初始化密码器（加密模型）
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        // 加密数据, 返回密文
        return cipher.doFinal(plaintext);
    }


    // /** 字节数组转成16进制字符串 **/
    public static String byte2hex(byte[] b) { // 一个字节的数，
        StringBuffer sb = new StringBuffer(b.length * 2);
        String tmp = "";
        for (int n = 0; n < b.length; n++) {
            // 整数转成十六进制表示
            tmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
        }
        return sb.toString().toUpperCase(); // 转成da写
    }


}
