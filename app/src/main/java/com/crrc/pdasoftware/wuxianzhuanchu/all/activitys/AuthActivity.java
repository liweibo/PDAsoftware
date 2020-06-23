package com.crrc.pdasoftware.wuxianzhuanchu.all.activitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.crrc.pdasoftware.MainActivity;
import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.net.Constant;
import com.crrc.pdasoftware.utils.XToastUtils;
import com.githang.statusbar.StatusBarCompat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kongzue.dialog.v2.DialogSettings;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ru.alexbykov.nopermission.PermissionHelper;

import static com.kongzue.dialog.v2.DialogSettings.STYLE_IOS;


public class AuthActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btGo;
    private FloatingActionButton fab;
    private PromptDialog promptDialog;
    private CheckBox cb_psw;

    private PermissionHelper mPermissionHelper;
    private String wifiresult = "noname";
    int ct = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_auth);

        mPermissionHelper = new PermissionHelper(this);
        getDeviceInfo();


        initView();
        setListener();
        promptDialog = new PromptDialog(this);

        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.authbar), true);

        /*
         * 获取远程版本信息
         * */
        final AlertDialog.Builder dialog = new AlertDialog.Builder(AuthActivity.this);
        PackageManager packageManager = this.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(this.getPackageName(), 0);
            final String version = packageInfo.versionName;
            System.out.println("当前本地的版本号：" + version);
            String url2 = "http://www.rongswift.com:8089/forsee/getType?type=wuxian";
            String url = "http://www.liweiboxcode.cn:8089/wuxianversion";
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    System.out.println(response.body() + "--------");

                    String dataResult = response.body().string();
                    System.out.println("线上版本：" + dataResult);

                    //                        JSONObject jsonObject = new JSONObject(dataResult);
//                        System.out.println(jsonObject.getJSONArray("result").length() + "*-*//////-----");
//                        final String version_master = jsonObject.getJSONArray("result").getJSONObject(0).getString("number");
//                        System.out.println(version_master);
                    if (!dataResult.equals(version)) {
//                        if (1 == 2) {
                        System.out.println("有新的apk可以更新");
                        Looper.prepare();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DialogSettings.style = STYLE_IOS;
                                DialogSettings.use_blur = true;
                                DialogSettings.blur_alpha = 200;

                                AlertDialog.Builder builder = new AlertDialog.Builder(AuthActivity.this);
                                builder.setTitle("版本更新");
                                builder.setMessage("存在新版本：V" + dataResult +
                                        "，需下载安装吗?");
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Uri uri = Uri.parse("http://www.liweiboxcode.cn/download/downloadAPK.html");
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    }
                                });
                                builder.setNegativeButton("取消", null);
                                builder.show();

                            }
                        });

                        Looper.loop();
                    }
                }
            });
            System.out.println(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }


    private void getDeviceInfo() {
        mPermissionHelper.check(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.CHANGE_NETWORK_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS)
                .onSuccess(this::onSuccess).onDenied(this::onDenied).onNeverAskAgain(this::onNeverAskAgain).run();
    }

    private void onSuccess() {
        StringBuilder builder = new StringBuilder();
        wifiresult = builder + "";
        System.out.println(" 打印wifi名称：" + wifiresult);
    }

    private void onDenied() {
        System.out.println("用户拒绝");
        ct++;
        if (ct <= 1) {
            getDeviceInfo();
        }
    }

    private void onNeverAskAgain() {
        System.out.println("用户拒绝onNeverAskAgain");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mPermissionHelper != null) {
            mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void initView() {

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btGo = findViewById(R.id.bt_go);
//        fab = findViewById(R.id.fab);

        cb_psw = findViewById(R.id.cb_psw);

        SharedPreferences sharedPreferences = getSharedPreferences("userpsw",
                Activity.MODE_PRIVATE);
        String user = sharedPreferences.getString("user", "");
        String psw = sharedPreferences.getString("psw", "");
        if (!user.equals("") && !psw.equals("")) {
            cb_psw.setChecked(true);
            etUsername.setText(user);
            etPassword.setText(psw);

        } else {
            cb_psw.setChecked(false);
        }

    }

    private void setListener() {
        cb_psw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences pref = AuthActivity.this.getSharedPreferences("userpsw", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                if (b) {//勾选
                    //判空，存储
                    String usr = etUsername.getText().toString().trim();
                    String psw = etPassword.getText().toString().trim();

                    if (!usr.equals("") && !psw.equals("")) {
                        editor.putString("user", usr).commit();
                        editor.putString("psw", psw).commit();
                    } else {
                        DialogSettings.style = STYLE_IOS;
                        DialogSettings.use_blur = true;
                        DialogSettings.blur_alpha = 200;
//                        AlertUtils.alertNoListener(AuthActivity.this, "请输入完整用户名密码");
                    }


                } else {
                    //存储的数据清空
                    editor.putString("user", "").commit();
                    editor.putString("psw", "").commit();
                }
            }
        });

        etUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cb_psw.setChecked(false);
            }
        });
        etPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cb_psw.setChecked(false);
            }
        });

        btGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String users = etUsername.getText().toString().trim();
                if (users.length() > 0) {
                    Constant.userId = users.toString().trim();
                    Constant.x = "'" + users.toString().trim() + "'";
                    Constant.y = "'" + users.toString().trim() + "'";
                    Intent i2 = new Intent(AuthActivity.this, MainActivity.class);
                    startActivity(i2);
                } else {
                    XToastUtils.info("请输入用户名");
                }


//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        promptDialog.showLoading("登录中...", false);
//                    }
//                });
//                final String users = etUsername.getText().toString().trim();
//                final String psw = etPassword.getText().toString().trim();
//                if (users.trim().length() > 0 && psw.trim().length() > 0) {
//                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                            .connectTimeout(3, TimeUnit.SECONDS)
//                            .readTimeout(6, TimeUnit.SECONDS)
//                            .build();
//
//                    RequestBody formBody = new FormBody.Builder()
//                            .add("userid", users).add("password", psw)
//                            .build();
//                    Request request = new Request.Builder()
//                            .url("http://mro.csrzic.com/comm/mro/authenticateuser")
//                            .addHeader("content-type", "application/x-www-form-urlencoded")
//                            // .post(RequestBody.create(MEDIA_TYPE_TEXT, postBody))
//                            .post(formBody)
//                            // 表单提交
//                            .build();
//                    System.out.println("测试11");
//
//                    okHttpClient.newCall(request).enqueue(new Callback() {
//
//
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    promptDialog.dismiss();
//                                }
//                            });
//                            System.out.println("超时-错误：" + e.toString());
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    DialogSettings.style = STYLE_IOS;
//                                    DialogSettings.use_blur = true;
//                                    DialogSettings.blur_alpha = 200;
////                                    AlertUtils.alertNoListener(AuthActivity.this, "未检测到可交互的网络，请连接4G或可联网的WIFI");
//                                }
//                            });
//
//                        }
//
//                        @Override
//                        public void onResponse(Call call, Response response) throws IOException {
//
//
//                            String dataResult = response.body().string();
//                            JSONObject jsonObject = null;
//                            try {
//                                jsonObject = new JSONObject(dataResult);
//                                boolean actionResult = jsonObject.getBoolean("actionResult");
//                                System.out.println("测试2");
//                                if (actionResult) {
//                                    Intent i2 = new Intent(AuthActivity.this, HomeActivity.class);
//                                    startActivity(i2);
//
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            promptDialog.dismiss();
//                                        }
//                                    });
//                                } else {
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            promptDialog.dismiss();
//                                        }
//                                    });
//
//
//                                    runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            DialogSettings.style = STYLE_IOS;
//                                            DialogSettings.use_blur = true;
//                                            DialogSettings.blur_alpha = 200;
////                                            AlertUtils.alertNoListener(AuthActivity.this, "用户名或密码输入错误");
//                                        }
//                                    });
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                } else {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            promptDialog.dismiss();
//                        }
//                    });
//                    DialogSettings.style = STYLE_IOS;
//                    DialogSettings.use_blur = true;
//                    DialogSettings.blur_alpha = 200;
////                    AlertUtils.alertNoListener(AuthActivity.this, "请输入用户名、密码");
//
//                }


            }
        });
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                getWindow().setExitTransition(null);
//                getWindow().setEnterTransition(null);
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(AuthActivity.this, fab, fab.getTransitionName());
//                startActivity(new Intent(AuthActivity.this, MainActivity.class), options.toBundle());
//
////                startActivity(new Intent(AuthActivity.this, AuthfailActivity.class));
//            }
//        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onRestart() {
        super.onRestart();
//        fab.setVisibility(View.GONE);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onResume() {
        super.onResume();
//        fab.setVisibility(View.VISIBLE);
    }
}


