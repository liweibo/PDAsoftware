package com.crrc.pdasoftware.fragments.guzhangchuli;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.crrc.pdasoftware.MyApplication;
import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.activity.all.guzhangchuli.FuwuxyTianxieActivity;
import com.crrc.pdasoftware.activity.all.guzhangchuli.GuZhangActivity;
import com.crrc.pdasoftware.net.Constant;
import com.crrc.pdasoftware.net.pojo.FwxyShenqingchuliWorkLiu;
import com.crrc.pdasoftware.net.pojo.GuzFwxyPostInfo;
import com.crrc.pdasoftware.net.pojo.GuzWeixianValuePostInfo;
import com.crrc.pdasoftware.net.pojo.GuzhangExtraFieldGet;
import com.crrc.pdasoftware.net.pojo.GuzhangFaqiWorkLiu;
import com.crrc.pdasoftware.net.pojo.GuzhangWorkStrmActionId;
import com.crrc.pdasoftware.utils.ClearEditText;
import com.crrc.pdasoftware.utils.FiledDataSave;
import com.crrc.pdasoftware.utils.XToastUtils;
import com.crrc.pdasoftware.utils.guzhanggddata.FuwuDataInfo;
import com.crrc.pdasoftware.utils.guzhanggddata.FuwuDataTwoProvider;
import com.rxjava.rxlife.RxLife;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.button.SmoothCheckBox;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.edittext.MultiLineEditText;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xui.widget.picker.widget.configure.TimePickerType;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectChangeListener;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectListener;
import com.xuexiang.xutil.data.DateUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import me.leefeng.promptlibrary.PromptDialog;
import rxhttp.wrapper.param.RxHttp;

import static android.content.Context.MODE_PRIVATE;


public class FuwuxiangyingWriteInfoFragment extends Fragment {
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    SmoothCheckBox scheck_box_yiyue_fuwuxiangy;

    LinearLayout ll_xy_isornogaipai;
    LinearLayout ll_xy_needdaodatime;
    LinearLayout ll_xy_tele_time;
    LinearLayout ll_gaipaiyuanyin_visi;

    TextView xy_shifougaipai_tv;
    TextView xy_wexianyuan_tv;
    TextView xy_xuqiudaodatime_tv;
    TextView xy_teletime_tv;

    String gdbianhao;
    String gdchexing;
    String gdchehao;

    NestedScrollView nestSv_xy;
    TextView xy_gdbh_tv;


    ButtonView xy_nextstep_fuxy;
    ButtonView xy_gaipaibtn;

    private TimePickerView mTimePickerDialogxuqiudaoda;
    private TimePickerView mTimePickerDialogtelephone;

    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    boolean canClickbtns = false;


    ClearEditText gdpeishuuser;//配属用户
    ClearEditText gongdanbh;//工单编号
    ClearEditText gongdanchexing;//车型
    ClearEditText gongdanchehao;//车号
    TextView kehuCALLTIME;//客户来电时间
    MultiLineEditText paigongliyou;//派工理由
    ClearEditText fuwudanweiContact;//服务单位联系人

    ClearEditText xy_fuwuaddress;//服务单位地址
    ClearEditText xy_xianchangchuliusername;//现场处理人姓名
    ClearEditText xy_xianchangchuliuseriphone;//现场处理人电话

    MultiLineEditText secureGuanli;//安全管理
    MultiLineEditText text_gaipai_reason;//改派原因
    //    ClearEditText leijizouxing;//累计走行
    private PromptDialog promptDialog;

    MyApplication apps;

//    LinearLayout ll_xy_isornoweixianyuan;

    public FuwuxiangyingWriteInfoFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        pref = getActivity().getSharedPreferences("guzhanggddata", MODE_PRIVATE);
        editor = pref.edit();
        promptDialog = new PromptDialog(getActivity());
        apps = (MyApplication) getActivity().getApplication();

        View v = inflater.inflate(R.layout.fragment_fuwuxiangying_write_info,
                container, false);
        initViews(v);
        setClick();
        initValue();
        return v;
    }

    public void initViews(View v) {
        TextView tvToolbarTitle = getActivity().findViewById(R.id.tv_gzgd_toolbbar_title);
        tvToolbarTitle.setText("故障工单-服务响应");


        fragmentManager = getActivity().getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();


        gongdanbh = v.findViewById(R.id.xy_gdbianhao);
        gongdanchexing = v.findViewById(R.id.gdchexing);
        gongdanchehao = v.findViewById(R.id.gdchehao);
        gdpeishuuser = v.findViewById(R.id.gdpeishuuser);

        kehuCALLTIME = v.findViewById(R.id.xy_teletime_tv);
        paigongliyou = v.findViewById(R.id.fwxy_paigongliyou);
        fuwudanweiContact = v.findViewById(R.id.xy_fuwuname);
        secureGuanli = v.findViewById(R.id.xy_anquanyaoqiu);


        scheck_box_yiyue_fuwuxiangy = v.findViewById(R.id.scheck_box_yiyue_fuwuxiangy);
        ll_xy_isornogaipai = v.findViewById(R.id.ll_xy_isornogaipai);
        ll_xy_needdaodatime = v.findViewById(R.id.ll_xy_needdaodatime);
        ll_xy_tele_time = v.findViewById(R.id.ll_xy_tele_time);

//        ll_xy_isornoweixianyuan = v.findViewById(R.id.ll_xy_isornoweixianyuan);

        xy_shifougaipai_tv = v.findViewById(R.id.xy_shifougaipai_tv);
//        xy_wexianyuan_tv = v.findViewById(R.id.xy_weixinayuan_tv);
        xy_xuqiudaodatime_tv = v.findViewById(R.id.xy_xuqiudaodatime_tv);
        xy_teletime_tv = v.findViewById(R.id.xy_teletime_tv);

        nestSv_xy = v.findViewById(R.id.nestSv_xy);
        xy_gdbh_tv = v.findViewById(R.id.xy_gdbh_tv);

        xy_nextstep_fuxy = v.findViewById(R.id.xy_nextstep_fuxy);
        xy_gaipaibtn = v.findViewById(R.id.xy_gaipaibtn);

        ll_gaipaiyuanyin_visi = v.findViewById(R.id.ll_gaipaiyuanyin_visi);

        xy_fuwuaddress = v.findViewById(R.id.xy_fuwuaddress);
        xy_xianchangchuliusername = v.findViewById(R.id.xy_xianchangchuliusername);
        xy_xianchangchuliuseriphone = v.findViewById(R.id.xy_xianchangchuliuseriphone);
        text_gaipai_reason = v.findViewById(R.id.text_gaipai_reason);


        gdbianhao = pref.getString("gzgongdanbianhao", "不存在");
        gdchexing = pref.getString("gzgongdanchexing", "不存在");
        gdchehao = pref.getString("gzgongdanchehao", "不存在");

        gongdanbh.setText(gdbianhao);
        gongdanchexing.setText(gdchexing);
        gongdanchehao.setText(gdchehao);


        xy_gdbh_tv.setFocusable(true);
        xy_gdbh_tv.setFocusableInTouchMode(true);
        xy_gdbh_tv.requestFocus();


        xy_nextstep_fuxy.setVisibility(View.VISIBLE);
        xy_gaipaibtn.setVisibility(View.GONE);
    }

    public void initValue() {
        int pos = ((FuwuxyTianxieActivity) getActivity()).itemPos;
        System.out.println("------" + pos);
        FuwuDataInfo lsdata = FuwuDataTwoProvider.getFwxyListdata().get(pos);
        gongdanbh.setText(lsdata.getGdbh());
        gongdanchexing.setText(lsdata.getChexingvalue());
        gongdanchehao.setText(lsdata.getChehaoValue());
        gdpeishuuser.setText(lsdata.getPeishuyh());
        kehuCALLTIME.setText(lsdata.getKehuCALLTIME());
        paigongliyou.setContentText(lsdata.getPaigongliyou());
        fuwudanweiContact.setText(lsdata.getFuwudanweiContact());
        secureGuanli.setContentText(lsdata.getSecureGuanli());
    }

    public boolean canHide = true;

    public void setClick() {


        scheck_box_yiyue_fuwuxiangy.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                canClickbtns = isChecked;
                if (isChecked) {
                    XToastUtils.success("已阅");
                } else {
                    XToastUtils.info("未阅读");
                }
            }
        });


        ll_xy_isornogaipai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContextMenuDialog();


            }
        });

//        ll_xy_isornoweixianyuan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showContextMenuDialogWeixianyuan();
//
//
//            }
//        });

//        ll_xy_needdaodatime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                xuqiudaodatimeshowTimePickerDialog();
//            }
//        });

//        ll_xy_tele_time.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                telephonetimeshowTimePickerDialog();
//            }
//        });

        //下一步
        xy_nextstep_fuxy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canClickbtns) {
                    XToastUtils.error("请阅读管理要求");
                } else {
                    String va = "\"ISREADSAFEREQ\"" + ":" + "\"是\"";
                    Constant.keyValuePosttomro = va;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            requestForPostInfotoMro();
                        }
                    }, 200);


                }
            }
        });

        //改派 转办
        xy_gaipaibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canClickbtns) {
                    XToastUtils.error("请阅读管理要求");
                } else {
                    //判断是否改派理由是否为空
                    String a = text_gaipai_reason.getContentText().toString().trim();
                    if (a.length() > 0) {
                        //发起工作流
                        Constant.yijian = a;
                        System.out.println("意见：" + Constant.yijian);
                        System.out.println("ActionId：" + Constant.ActionId);
                        System.out.println("uniqueId：" + Constant.uniqueId);
                        System.out.println("userId：" + Constant.userId);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                requestWorkLiu();

                            }
                        }, 200);

                    } else {
                        XToastUtils.error("请填写改派转办原因");
                    }
                }

            }
        });


        request2();


    }

//    private void showContextMenuDialogWeixianyuan() {
//
//        new MaterialDialog.Builder(getActivity())
//                .title("请选择")
//                .items(R.array.isornot_values)
//                .itemsCallback(new MaterialDialog.ListCallback() {
//                    @Override
//                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
//                        xy_wexianyuan_tv.setText(text);
//
//                        String isorno = xy_wexianyuan_tv.getText().toString().trim();
//
//                        if (isorno.length() > 0) {
//                            if (isorno.equals("是危险源")) {
//
//                                //先把是，否的危险源值以及是否审批的值发送到mro
//                                //再查看actionid
//                                //再发送审批工作流
//                                requestPostWeixianValue();
//
//
//                                //先获取actionid
////                                requestWorkLiuActionId();
//
//                            } else if (isorno.equals("非危险源")) {
//
//
//                            }
//                        } else {
//
//                        }
//
//                    }
//                })
//                .show();
//    }

    //发送危险源值。
    private void requestPostWeixianValue() {
        RxHttp.postForm(Constant.usualInterfaceAddr)
                .add(Constant.usualKey, Constant.getPostWeixianvalueInfo())
                .asClass(GuzWeixianValuePostInfo.class).
                observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    //请求开始，当前在主线程回调
                    promptDialog.showLoading("加载中...");
                })
                .doFinally(() -> {
                    //请求结束，当前在主线程回调
//                    promptDialog.dismissImmediately();

                }).as(RxLife.as(this))  //感知生命周期 当退出页面时 请求未完成，则关闭请求，防止内存泄漏
                .subscribe(clz -> {//clz就是Pojo
                    if (clz.code.equals("S")) {
                        System.out.println("危险源值更新成功！");
                        //请求actionid有哪些。
                        //再根据actionid
                        requestWorkLiuActionId();
                    } else {
                        System.out.println("危险源值更新失败！");
                    }

                }, throwable -> {
                    //失败回调
                    System.out.println("失败结果---：" + throwable.getMessage());

                });
    }


    public void request2() {
        final String[] fwdanweiaddress = new String[1];
        final String[] xianchangchulirenname = new String[1];
        final String[] xianchangchulirenphone = new String[1];

        RxHttp.postForm(Constant.usualInterfaceAddr)
                .add(Constant.usualKey, Constant.getExtraBiao())
                .asClass(GuzhangExtraFieldGet.class).
                observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    //请求开始，当前在主线程回调
                    promptDialog.showLoading("加载中...");
                })
                .doFinally(() -> {
                    //请求结束，当前在主线程回调
                    xy_fuwuaddress.setText(fwdanweiaddress[0]);
                    xy_xianchangchuliusername.setText(xianchangchulirenname[0]);
                    xy_xianchangchuliuseriphone.setText(xianchangchulirenphone[0]);

                    promptDialog.dismissImmediately();

                }).as(RxLife.as(this))  //感知生命周期 当退出页面时 请求未完成，则关闭请求，防止内存泄漏
                .subscribe(clz -> {//clz就是Pojo
                    System.out.println("-------------8-------");

                    List<GuzhangExtraFieldGet.DataBeanX.DataBean> datalevel = clz.data.data;
                    if (datalevel.size() > 0) {
                        GuzhangExtraFieldGet.DataBeanX.DataBean rowdatalevel = datalevel.get(0);
                        //服务单位地址
                        fwdanweiaddress[0] = rowdatalevel.ADDRESS;
                        //现场处理人
                        xianchangchulirenname[0] = rowdatalevel.DISPLAYNAME;
                        xianchangchulirenphone[0] = rowdatalevel.PHONENUM;

                        //到达现场界面用到的3个数据字段
                        FiledDataSave.WHICHSTATION = rowdatalevel.WHICHSTATION;//故障处理站点
                        FiledDataSave.FAULTCONSEQ = rowdatalevel.FAULTCONSEQ;//故障后果
                        FiledDataSave.FAILUREDESC = rowdatalevel.FAILUREDESC;//故障名称
                        FiledDataSave.CARSECTIONNUM = rowdatalevel.CARSECTIONNUM;//车厢号

                        FiledDataSave.FAILURECODE = rowdatalevel.FAILURECODE;
                        FiledDataSave.PRODUCTNICKNAME = rowdatalevel.PRODUCTNICKNAME;
                        FiledDataSave.FAULTTIME = rowdatalevel.FAULTTIME;
                        FiledDataSave.FINDPROCESS = rowdatalevel.FINDPROCESS;
                        FiledDataSave.RUNNINGMODE = rowdatalevel.RUNNINGMODE;
                        FiledDataSave.FAULTQUALIT = rowdatalevel.FAULTQUALIT;
                        FiledDataSave.QYFZDW = rowdatalevel.QYFZDW;
                        FiledDataSave.FAILWEATHER = rowdatalevel.FAILWEATHER;
                        FiledDataSave.ROADTYPE = rowdatalevel.ROADTYPE;


                        FiledDataSave.FAULTQUALIT = rowdatalevel.FAULTQUALIT;
                        FiledDataSave.NODATAREASON = rowdatalevel.NODATAREASON;
                        FiledDataSave.FAULTDESC = rowdatalevel.FAULTDESC;
                        System.out.println("guzhang现象："+rowdatalevel.FAULTDESC);
                        FiledDataSave.PREREASONALYS = rowdatalevel.PREREASONALYS;
                        FiledDataSave.DEALMEASURE = rowdatalevel.DEALMEASURE;
                        FiledDataSave.FAULTCOMPONENTNAME = rowdatalevel.FAULTCOMPONENTNAME;
                        FiledDataSave.ANALYSISREPNE = rowdatalevel.ANALYSISREPNE;
                        FiledDataSave.DEALMETHOD = rowdatalevel.DEALMETHOD;


                        FiledDataSave.FAILURELIBID = rowdatalevel.FAILURELIBID;//唯一id

                        System.out.println("CARSECTIONNUM====" + rowdatalevel.CARSECTIONNUM);
                    }


                }, throwable -> {
                    //失败回调
                    System.out.println("失败结果---：" + throwable.getMessage());

                });

    }


    //点击下一步时先提交数据，再出发工作流。
    public void requestForPostInfotoMro() {
        final String[] fwdanweiaddress = new String[1];
        final String[] xianchangchulirenname = new String[1];
        final String[] xianchangchulirenphone = new String[1];

        RxHttp.postForm(Constant.usualInterfaceAddr)
                .add(Constant.usualKey, Constant.getFuxyPostInfo())
                .asClass(GuzFwxyPostInfo.class).
                observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    //请求开始，当前在主线程回调
                    promptDialog.showLoading("加载中...");
                })
                .doFinally(() -> {
                    //请求结束，当前在主线程回调
                    xy_fuwuaddress.setText(fwdanweiaddress[0]);
                    xy_xianchangchuliusername.setText(xianchangchulirenname[0]);
                    xy_xianchangchuliuseriphone.setText(xianchangchulirenphone[0]);
                    promptDialog.dismissImmediately();

                }).as(RxLife.as(this))  //感知生命周期 当退出页面时 请求未完成，则关闭请求，防止内存泄漏
                .subscribe(clz -> {//clz就是Pojo

                    if (clz.code.equals("S") && clz.msg.equals("操作成功")) {
                        FiledDataSave.fwxyBtnEffect = true;//表示可以点击 到达现场顶部按钮
                        XToastUtils.success("提交成功");
                        //数据提交成功后再申请服务响应申请处理的工作流
                        System.out.println("申请工作流：" +
                                Constant.getFaqiFwxyWorkStream());

//                        requestQingqiuWorkliu();
                        ((FuwuxyTianxieActivity) getActivity()).gotoDaodaxiancfrgment();

                    } else {
                        XToastUtils.error("提交失败");
                        FiledDataSave.fwxyBtnEffect = false;//表示可以不可点击 到达现场顶部按钮

                    }


                }, throwable -> {
                    FiledDataSave.fwxyBtnEffect = false;//表示可以不可点击 到达现场顶部按钮
                    XToastUtils.error("提交失败");

                    //失败回调
                    System.out.println("失败结果服务响应提交---：" + throwable.getMessage());

                });

    }

    //服务响应申请处理工作流
    private void requestQingqiuWorkliu() {
        RxHttp.postForm(Constant.usualInterfaceAddr)
                .add(Constant.usualKey, Constant.getFaqiFwxyWorkStream())
                .asClass(FwxyShenqingchuliWorkLiu.class).
                observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    //请求开始，当前在主线程回调
                })
                .doFinally(() -> {
                    promptDialog.dismissImmediately();

                }).as(RxLife.as(this))  //感知生命周期 当退出页面时 请求未完成，则关闭请求，防止内存泄漏
                .subscribe(clz -> {//clz就是Pojo

                    if (clz.code.equals("S")) {
                        FiledDataSave.fwxyBtnEffect = true;//表示可以点击 到达现场顶部按钮
                        XToastUtils.success("服务响应成功");
                        ((FuwuxyTianxieActivity) getActivity()).gotoDaodaxiancfrgment();


                    } else {
                        XToastUtils.success("服务响应失败！");
                        FiledDataSave.fwxyBtnEffect = false;//表示可以不可点击 到达现场顶部按钮

                    }


                }, throwable -> {
                    FiledDataSave.fwxyBtnEffect = false;//表示可以不可点击 到达现场顶部按钮
                    XToastUtils.success("服务响应失败");

                    //失败回调
                    System.out.println("失败结果服务响应提交---：" + throwable.getMessage());

                });
    }


    public void addFragmentdaodaxcWriteinfo() {
        DaodaxcFragment fragment = new DaodaxcFragment();
        transaction.replace(R.id.frag, fragment);
        transaction.commit();
    }

    //下拉选项为一个类别时  是否改派转办
    private void showContextMenuDialog() {
        new MaterialDialog.Builder(getActivity())
                .title("请选择")
                .items(R.array.isornogaipai_values)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        xy_shifougaipai_tv.setText(text);

                        String isorno = xy_shifougaipai_tv.getText().toString().trim();

                        if (isorno.length() > 0) {
                            if (isorno.equals("是")) {
                                ll_gaipaiyuanyin_visi.setVisibility(View.VISIBLE);

                                xy_nextstep_fuxy.setVisibility(View.GONE);
                                xy_gaipaibtn.setVisibility(View.VISIBLE);

//                                requestWorkLiuActionId();

                            } else if (isorno.equals("否")) {
                                ll_gaipaiyuanyin_visi.setVisibility(View.GONE);

                                xy_nextstep_fuxy.setVisibility(View.VISIBLE);
                                xy_gaipaibtn.setVisibility(View.GONE);

                            }
                        } else {
                            ll_gaipaiyuanyin_visi.setVisibility(View.GONE);

                        }

                    }
                })
                .show();
    }

    //拿到actionid
    private void requestWorkLiuActionId() {

        final String[] actionid = new String[1];
        RxHttp.postForm(Constant.usualInterfaceAddr)
                .add(Constant.usualKey, Constant.getGZWorkStreamActionId())
                .asClass(GuzhangWorkStrmActionId.class).
                observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    //请求开始，当前在主线程回调
                })
                .doFinally(() -> {
                    //请求结束，当前在主线程回调


                    promptDialog.dismissImmediately();

                }).as(RxLife.as(this))  //感知生命周期 当退出页面时 请求未完成，则关闭请求，防止内存泄漏
                .subscribe(clz -> {//clz就是Pojo
                    System.out.println("危险源作业审批的actionid：" + clz.data.toString());
                    if (clz.data.size() > 0) {
                        for (int i = 0; i < clz.data.size(); i++) {
                            System.out.println("zhi:"+clz.data.get(i).actionid+"="+clz.data.get(i).actiondesc);
                        }
                        Constant.ActionId = clz.data.get(0).actionid;
                        System.out.println("actionid====" + Constant.ActionId);
                    }

                }, throwable -> {
                    //失败回调
                    System.out.println("失败结果---：" + throwable.getMessage());

                });


    }

    private void requestStartWorkLiu() {
        final boolean[] suc = {false};
        RxHttp.postForm(Constant.usualInterfaceAddr)
                .add(Constant.usualKey, Constant.getStatMroWorkLiu())
                .asClass(GuzhangFaqiWorkLiu.class).
                observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    //请求开始，当前在主线程回调
                    promptDialog.showLoading("加载中...");
                })
                .doFinally(() -> {
                    promptDialog.dismissImmediately();
                }).as(RxLife.as(this))  //感知生命周期 当退出页面时 请求未完成，则关闭请求，防止内存泄漏
                .subscribe(clz -> {//clz就是Pojo
                    if (clz.code.equals("S") &&
                            clz.msg.equals("工作流发送成功！")) {
                        XToastUtils.success("工作流发送成功！");
                        suc[0] = true;

                    } else {
                        System.out.println("shibai:" + clz.code + "--" + clz.msg);
                        XToastUtils.success("工作流发送失败！");
                        suc[0] = false;
                    }

                }, throwable -> {
                    //失败回调
                    System.out.println("工作流发送失败");
                    XToastUtils.success("工作流发送失败！");

                    System.out.println("失败结果---：" + throwable.getMessage());

                });


    }

    private void requestWorkLiu() {
        final boolean[] suc = {false};
        RxHttp.postForm(Constant.usualInterfaceAddr)
                .add(Constant.usualKey, Constant.getGZGaipaiWorkStream())
                .asClass(GuzhangFaqiWorkLiu.class).
                observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    //请求开始，当前在主线程回调
                    promptDialog.showLoading("加载中...");
                })
                .doFinally(() -> {
                    //请求结束，当前在主线程回调
                    promptDialog.dismissImmediately();
                }).as(RxLife.as(this))  //感知生命周期 当退出页面时 请求未完成，则关闭请求，防止内存泄漏
                .subscribe(clz -> {//clz就是Pojo
                    if (clz.code.equals("S")) {
                        XToastUtils.success("改派成功");
                        suc[0] = true;
                        getActivity().startActivity(new Intent(((FuwuxyTianxieActivity) getActivity()),
                                GuZhangActivity.class));
                    } else {
                        System.out.println("shibai:" + clz.code + "--" + clz.msg);
                        XToastUtils.error("改派失败");
                        suc[0] = false;
                    }

                }, throwable -> {
                    //失败回调
                    XToastUtils.error("改派失败");
                    System.out.println("失败结果---：" + throwable.getMessage());

                });


    }


    private void xuqiudaodatimeshowTimePickerDialog() {
        if (mTimePickerDialogxuqiudaoda == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.string2Date("2013-07-08 12:32:46", DateUtils.yyyyMMddHHmmss.get()));
            mTimePickerDialogxuqiudaoda = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
                @Override
                public void onTimeSelected(Date date, View v) {
                    XToastUtils.toast(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get()));
                    xy_xuqiudaodatime_tv.setText(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get()));

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
            mTimePickerDialogtelephone = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
                @Override
                public void onTimeSelected(Date date, View v) {
                    XToastUtils.toast(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get()));
                    xy_teletime_tv.setText(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get()));

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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
