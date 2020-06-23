package com.crrc.pdasoftware.activity.all.guzhangchuli;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.net.Constant;
import com.crrc.pdasoftware.net.pojo.GuzhangFaqiWorkLiu;
import com.crrc.pdasoftware.net.pojo.GuzpaigongPostInfo;
import com.crrc.pdasoftware.utils.ClearEditText;
import com.crrc.pdasoftware.utils.DemoDataProvider;
import com.crrc.pdasoftware.utils.XToastUtils;
import com.rxjava.rxlife.RxLife;
import com.xuexiang.xui.adapter.simple.ExpandableItem;
import com.xuexiang.xui.adapter.simple.XUISimpleExpandableListAdapter;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.edittext.MultiLineEditText;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xui.widget.picker.widget.configure.TimePickerType;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectChangeListener;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectListener;
import com.xuexiang.xui.widget.popupwindow.popup.XUISimpleExpandablePopup;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.xuexiang.xutil.data.DateUtils;
import com.xuexiang.xutil.display.DensityUtils;

import java.util.Calendar;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import me.leefeng.promptlibrary.PromptDialog;
import rxhttp.wrapper.param.RxHttp;

public class GuzhangPaigongActivity extends AppCompatActivity {
    Toolbar toolbar;
    SuperTextView xuqiudaodatime;
    SuperTextView telephonetime;
    SuperTextView guzhangresult;

    LinearLayout ll_pg_guzhangresult;
    LinearLayout ll_pg_needdaodatime;
    LinearLayout ll_pg_tele_time;

//    TextView pg_xuqiudaodatime_tv;

    TextView pg_guzhangresult_tv;

    ClearEditText pg_gongdanbh;//工单编号
    ClearEditText pg_peishuuser;//工配属用户
    ClearEditText pg_chexing;//车型
    ClearEditText pg_chehao;//车号
    ClearEditText pg_fuwuaddress;//服务单位地址
    TextView pg_teletime_tv;
    //客户来电时间
    ClearEditText pg_fuwuname;
    //服务单位联系人
    ClearEditText pg_xianchangchuliusername;
    //现场处理人
    ClearEditText pg_xianchangchuliuseriphone;
    //现场处理人电话
    MultiLineEditText pg_paigongliyou;
    //派工理由
    MultiLineEditText pg_secureyaoqiu;
    //安全管理要求


    NestedScrollView nestSv;

    ButtonView benbanshichu;
    ButtonView kuabanshichu;
    private TimePickerView mTimePickerDialogxuqiudaoda;
    private TimePickerView mTimePickerDialogtelephone;

    private XUISimpleExpandablePopup mExpandableListPopup;

    private PromptDialog promptDialog;

    TextView pg_gdbh_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guzhang_paigong);
        initViews();
        Intent intent = getIntent();
        getIntendata(intent);


    }

    public void getIntendata(Intent intent) {
        String gdbh = intent.getStringExtra("gdbh");
        pg_gongdanbh.setText(gdbh);

        String peishuser = intent.getStringExtra("peishuser");
        pg_peishuuser.setText(peishuser);

        String chexing = intent.getStringExtra("chexing");
        pg_chexing.setText(chexing);

        String chehao = intent.getStringExtra("chehao");
        pg_chehao.setText(chehao);

        String fwdanweiaddress = intent.getStringExtra("fwdanweiaddress");
        pg_fuwuaddress.setText(fwdanweiaddress);

        String kehuteletime = intent.getStringExtra("kehuteletime");
        pg_teletime_tv.setText(kehuteletime);

        String fuwudanweicontactor = intent.getStringExtra("fuwudanweicontactor");
        pg_fuwuname.setText(fuwudanweicontactor);

        String xianchanghandler = intent.getStringExtra("xianchanghandler");
        pg_xianchangchuliusername.setText(xianchanghandler);

        String xianchanghandleriphone = intent.getStringExtra("xianchanghandleriphone");
        pg_xianchangchuliuseriphone.setText(xianchanghandleriphone);

        String paigongliyou = intent.getStringExtra("paigongliyou");
        pg_paigongliyou.setContentText(paigongliyou);

        String anquanguanliyaoqiu = intent.getStringExtra("anquanguanliyaoqiu");
        pg_secureyaoqiu.setContentText(anquanguanliyaoqiu);

    }

    public boolean isEmptybool(ClearEditText pg_gongdanbh,
                               ClearEditText pg_peishuuser,
                               ClearEditText pg_chexing,
                               ClearEditText pg_chehao,
                               ClearEditText pg_fuwuaddress,
                               TextView pg_teletime_tv,
                               ClearEditText pg_xianchangchuliusername,
                               ClearEditText pg_xianchangchuliuseriphone,
                               MultiLineEditText pg_paigongliyou,
                               MultiLineEditText pg_secureyaoqiu,
                               TextView pg_guzhangresult_tv
    ) {
        boolean xx = false;
        if (pg_gongdanbh.getText().toString().trim().length() > 0 &&
                pg_peishuuser.getText().toString().trim().length() > 0 &&
                pg_chexing.getText().toString().trim().length() > 0 &&
                pg_chehao.getText().toString().trim().length() > 0 &&
//                pg_fuwuaddress.getText().toString().trim().length() > 0
//                &&
                pg_teletime_tv.getText().toString().trim().length() > 0 &&
                pg_xianchangchuliusername.getText().toString().trim().length() > 0 &&
                pg_xianchangchuliuseriphone.getText().toString().trim().length() > 0 &&
                pg_paigongliyou.getContentText().trim().length() > 0 &&
                pg_secureyaoqiu.getContentText().trim().length() > 0 &&
                pg_guzhangresult_tv.getText().toString().trim().length() > 0) {
            xx = false;//表示不为空


        } else {
            xx = true;
        }

        return xx;
    }

    //需要提交的字段数据。
    public String getFieldValue(
            ClearEditText pg_peishuuser,
            ClearEditText pg_chexing,
            ClearEditText pg_chehao,
            ClearEditText pg_fuwuaddress,
            TextView pg_teletime_tv,
            ClearEditText pg_xianchangchuliusername,
            ClearEditText pg_xianchangchuliuseriphone,
            MultiLineEditText pg_paigongliyou,
            MultiLineEditText pg_secureyaoqiu,
            TextView pg_guzhangresult_tv
    ) {
        //有三个字段是要提交到别的表中
        String peishu = pg_peishuuser.getText().toString().trim();
        String chexing = pg_chexing.getText().toString().trim();
        String chehao = pg_chehao.getText().toString().trim();
        String fuwuaddress = pg_fuwuaddress.getText().toString().trim();
        String teletime = pg_teletime_tv.getText().toString().trim();
        String chulirenname = pg_xianchangchuliusername.getText().toString().trim();
        String xianchangchuliuseriphone = pg_xianchangchuliuseriphone.getText().toString().trim();
        String paigongliyou = pg_paigongliyou.getContentText().toString().trim();
        String anquangunaliyaoqiu = pg_secureyaoqiu.getContentText();
        String guzhangjieguo = pg_guzhangresult_tv.getText().toString().trim();
        String va = "\"ASSIGNEDUSER\"" + ":" + "\"" + peishu + "\"" + ","
                + "\"MODELPROJECT\"" + ":" + "\"" + chexing + "\"" + ","
                + "\"CARNUM\"" + ":" + "\"" + chehao + "\"" + ","
                + "\"SERVCOMPANY\"" + ":" + "\"" + fuwuaddress + "\"" + ","
                + "\"CALLTIME\"" + ":" + "\"" + teletime + "\"" + ","
                + "\"DISPATCHREASON\"" + ":" + "\"" + paigongliyou + "\"" + ","
                + "\"MANAGEMENTREQ\"" + ":" + "\"" + anquangunaliyaoqiu + "\"";
        String va2 = "\"ISREADSAFEREQ\"" + ":" + "\"是\"";
        return va;
    }


    private void initViews() {

        promptDialog = new PromptDialog(this);


        toolbar = findViewById(R.id.toolbar_gz_paigong);

        ll_pg_guzhangresult = findViewById(R.id.ll_pg_guzhangresult);
//        ll_pg_needdaodatime = findViewById(R.id.ll_pg_needdaodatime);
        ll_pg_tele_time = findViewById(R.id.ll_pg_tele_time);

        pg_gongdanbh = findViewById(R.id.pg_gongdanbh);
        pg_peishuuser = findViewById(R.id.pg_peishuuser);
        pg_chexing = findViewById(R.id.pg_chexing);
        pg_chehao = findViewById(R.id.pg_chehao);
        pg_fuwuaddress = findViewById(R.id.pg_fuwuaddress);
        pg_fuwuname = findViewById(R.id.pg_fuwuname);
        pg_xianchangchuliusername = findViewById(R.id.pg_xianchangchuliusername);
        pg_xianchangchuliuseriphone = findViewById(R.id.pg_xianchangchuliuseriphone);
        pg_paigongliyou = findViewById(R.id.pg_paigongliyou);
        pg_secureyaoqiu = findViewById(R.id.pg_secureyaoqiu);


        nestSv = findViewById(R.id.nestSv);

//        pg_xuqiudaodatime_tv = findViewById(R.id.pg_xuqiudaodatime_tv);
        pg_teletime_tv = findViewById(R.id.pg_teletime_tv);
        pg_guzhangresult_tv = findViewById(R.id.pg_guzhangresult_tv);

        benbanshichu = findViewById(R.id.benbanshichu);
        kuabanshichu = findViewById(R.id.kuabanshichu);
        pg_gdbh_tv = findViewById(R.id.pg_gdbh_tv);

        pg_gdbh_tv.setFocusable(true);
        pg_gdbh_tv.setFocusableInTouchMode(true);
        pg_gdbh_tv.requestFocus();

        initExpandableListPopup();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        pg_gongdanbh.setFocusableInTouchMode(true);
        ll_pg_tele_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telephonetimeshowTimePickerDialog();
            }
        });
//        ll_pg_needdaodatime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                xuqiudaodatimeshowTimePickerDialog();
//            }
//        });

        ll_pg_guzhangresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                showContextMenuDialog();
                mExpandableListPopup.clearExpandStatus();
                mExpandableListPopup.showDown(v);
            }
        });


        benbanshichu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean empy = isEmptybool(pg_gongdanbh,
                        pg_peishuuser,
                        pg_chexing,
                        pg_chehao,
                        pg_fuwuaddress,
                        pg_teletime_tv,
                        pg_xianchangchuliusername,
                        pg_xianchangchuliuseriphone,
                        pg_paigongliyou,
                        pg_secureyaoqiu,
                        pg_guzhangresult_tv);
                if (!empy) {//数据都是完整的；才可提交。并发起工作流派工--startWork）
                    String valu = getFieldValue(
                            pg_peishuuser,
                            pg_chexing,
                            pg_chehao,
                            pg_fuwuaddress,
                            pg_teletime_tv,
                            pg_xianchangchuliusername,
                            pg_xianchangchuliuseriphone,
                            pg_paigongliyou,
                            pg_secureyaoqiu,
                            pg_guzhangresult_tv);
//                    String va = "\"CARNUM\"" + ":" + "\"8888\"";

                    Constant.keyValuePosttomro = valu;
                    String xx = Constant.getPaigongPostInfo();
                    System.out.println("传的值打印：" + xx);
                    
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //提交数据 成功后再发起工作流。
                            requestPostpaigongData();

                        }
                    }, 500);
                } else {
                    XToastUtils.error("请填写完整信息！");
                }


            }
        });
        kuabanshichu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptDialog.showLoading("数据提交中...");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        promptDialog.dismiss();
                        XToastUtils.success("提交成功");
                        finish();

//                        startActivity(new Intent(GuzhangPaigongActivity.this, GuZhangActivity.class));

                    }
                }, 2000);

            }
        });

//        pg_gongdanbh.getCenterEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                System.out.println("光标" + hasFocus);
//                if (!hasFocus) {
//                    if (pg_gongdanbh.getCenterEditValue().length()>0){
//                        pg_gongdanbh.setRightString(pg_gongdanbh.getCenterEditValue());
//                        pg_gongdanbh.setCenterEditString("");
//                    }
//
//                }
//
//
//            }
//        });
//        pg_gongdanbh.getCenterEditText().addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


    }


    //发起工作流。
    private void requestStartWorkLiu() {
        final boolean[] suc = {false};
        RxHttp.postForm(Constant.usualInterfaceAddr)
                .add(Constant.usualKey, Constant.getStatMroWorkLiu())
                .asClass(GuzhangFaqiWorkLiu.class).
                observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    //请求开始，当前在主线程回调
                    promptDialog.showLoading("派工中...");
                })
                .doFinally(() -> {

                    promptDialog.dismissImmediately();
                }).as(RxLife.as(this))  //感知生命周期 当退出页面时 请求未完成，则关闭请求，防止内存泄漏
                .subscribe(clz -> {//clz就是Pojo
                    if (clz.code.equals("S")) {
                        XToastUtils.success("派工成功");
                        startActivity(new Intent(GuzhangPaigongActivity.this, GuZhangActivity.class));

                    } else {
                        XToastUtils.error("派工失败");
                    }

                }, throwable -> {
                    //失败回调
                    XToastUtils.error("派工失败！");


                    System.out.println("失败结果---：" + throwable.getMessage());

                });


    }

    //提交数据。
    private void requestPostpaigongData() {
        final boolean[] suc = {false};
        RxHttp.postForm(Constant.usualInterfaceAddr)
                .add(Constant.usualKey, Constant.getPaigongPostInfo())
                .asClass(GuzpaigongPostInfo.class).
                observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    //请求开始，当前在主线程回调
                    promptDialog.showLoading("数据提交中...");
                })
                .doFinally(() -> {
                }).as(RxLife.as(this))  //感知生命周期 当退出页面时 请求未完成，则关闭请求，防止内存泄漏
                .subscribe(clz -> {//clz就是Pojo
                    System.out.println("数据提交：" + clz.toString());
                    if (clz.code.equals("S")) {
                        XToastUtils.success("提交成功");
                        promptDialog.dismissImmediately();

                        //调用发起工作流
                        requestStartWorkLiu();
                    } else {
                        XToastUtils.error("数据提交失败");
                    }

                }, throwable -> {
                    //失败回调
                    System.out.println("数据提交失败");

                    System.out.println("失败结果222---：" + throwable.getMessage());

                });


    }


    private void xuqiudaodatimeshowTimePickerDialog() {
        if (mTimePickerDialogxuqiudaoda == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.string2Date("2013-07-08 12:32:46", DateUtils.yyyyMMddHHmmss.get()));
            mTimePickerDialogxuqiudaoda = new TimePickerBuilder(GuzhangPaigongActivity.this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelected(Date date, View v) {
                    XToastUtils.toast(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get()));
//                    pg_xuqiudaodatime_tv.setText(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get()));

                }
            })
                    .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                        @Override
                        public void onTimeSelectChanged(Date date) {
                            Log.i("pvTime", "onTimeSelectChanged");
                        }
                    })
                    .setType(TimePickerType.ALL)
                    .setTitleText("时间选择")
                    .isDialog(true)
                    .setOutSideCancelable(false)
                    .setDate(calendar)
                    .build();
        }
        mTimePickerDialogxuqiudaoda.show();
    }

    private void telephonetimeshowTimePickerDialog() {
        if (mTimePickerDialogtelephone == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.string2Date("2013-07-08 12:32:46", DateUtils.yyyyMMddHHmmss.get()));
            mTimePickerDialogtelephone = new TimePickerBuilder(GuzhangPaigongActivity.this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelected(Date date, View v) {
                    XToastUtils.toast(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get()));
                    pg_teletime_tv.setText(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get()));

                }
            })
                    .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                        @Override
                        public void onTimeSelectChanged(Date date) {
                            Log.i("pvTime", "onTimeSelectChanged");
                        }
                    })
                    .setType(TimePickerType.ALL)
                    .setTitleText("时间选择")
                    .isDialog(true)
                    .setOutSideCancelable(false)
                    .setDate(calendar)
                    .build();
        }
        mTimePickerDialogtelephone.show();
    }

    //下拉选项为一个类别时
    private void showContextMenuDialog() {
        new MaterialDialog.Builder(GuzhangPaigongActivity.this)
                .title("请选择")
                .items(R.array.menu_values)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        XToastUtils.toast(position + ": " + text);
                        guzhangresult.setCenterString(text);

                    }
                })
                .show();
    }

    //下拉选项  有多个分类时
    private void initExpandableListPopup() {
        mExpandableListPopup = new XUISimpleExpandablePopup(GuzhangPaigongActivity.this,
                DemoDataProvider.expandableItems)
                .create(DensityUtils.dip2px(GuzhangPaigongActivity.this, 200), DensityUtils.dip2px(
                        GuzhangPaigongActivity.this, 200))
                .setOnExpandableItemClickListener(true, new XUISimpleExpandablePopup.OnExpandableItemClickListener() {
                    @Override
                    public void onExpandableItemClick(XUISimpleExpandableListAdapter adapter, ExpandableItem group, int groupPosition, int childPosition) {
                        pg_guzhangresult_tv.setText(group.getChildItem(childPosition).getTitle());
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        System.out.println("物理返回=111==");

        if (promptDialog.onBackPressed()) {
            super.onBackPressed();
        }

        System.out.println("物理返回=222==");
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}
