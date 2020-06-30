package com.crrc.pdasoftware.fragments.guzhangchuli;

import android.app.ActionBar;
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

import com.crrc.pdasoftware.activity.all.guzhangchuli.FuwuxyTianxieActivity;
import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.net.Constant;
import com.crrc.pdasoftware.net.pojo.FwxyShenqingchuliWorkLiu;
import com.crrc.pdasoftware.net.pojo.GuzDaodaxcPostInfo;
import com.crrc.pdasoftware.net.pojo.GuzDaodaxcPostInfoForFailLib;
import com.crrc.pdasoftware.net.pojo.GuzFwxyPostInfo;
import com.crrc.pdasoftware.net.pojo.GuzWeixianValuePostInfo;
import com.crrc.pdasoftware.net.pojo.GuzhangWorkStrmActionId;
import com.crrc.pdasoftware.utils.ClearEditText;
import com.crrc.pdasoftware.utils.DemoDataProvider;
import com.crrc.pdasoftware.utils.FiledDataSave;
import com.crrc.pdasoftware.utils.XToastUtils;
import com.crrc.pdasoftware.utils.guzhanggddata.FuwuDataInfo;
import com.crrc.pdasoftware.utils.guzhanggddata.FuwuDataTwoProvider;
import com.rxjava.rxlife.RxLife;
import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;
import com.xuexiang.xui.adapter.simple.ExpandableItem;
import com.xuexiang.xui.adapter.simple.XUISimpleExpandableListAdapter;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.button.SmoothCheckBox;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xui.widget.picker.widget.configure.TimePickerType;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectChangeListener;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectListener;
import com.xuexiang.xui.widget.popupwindow.popup.XUISimpleExpandablePopup;
import com.xuexiang.xutil.data.DateUtils;
import com.xuexiang.xutil.display.DensityUtils;

import java.util.Calendar;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import me.leefeng.promptlibrary.PromptDialog;
import rxhttp.wrapper.param.RxHttp;

import static android.content.Context.MODE_PRIVATE;


public class DaodaxcFragment extends Fragment {
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    TextView gongdanchexing;
    TextView daoda_chehao_tv;
    TextView daoda_chexianghao_tv;

    SmoothCheckBox scheck_box_yiyue_fuwuxiangy;

    LinearLayout ll_daoda_fasheng_time;

    LinearLayout ll_gaipaiyuanyin_visi;
    LinearLayout ll_daoda_chexing;
    LinearLayout ll_daoda_chehao;

    LinearLayout ll_pg_guzhangshebei;
    LinearLayout ll_daoda_yunxingmodel;

    LinearLayout ll_daoda_guzhangchuli_address;//故障处理站点

    LinearLayout ll_daoda_chexianghao;//车厢号LinearLayout
    LinearLayout ll_daoda_gzcode;//故障代码LinearLayout
    LinearLayout ll_daoda_gzhouguo;//故障后果LinearLayout
    LinearLayout ll_daoda_fashengjieduan;//发生阶段LinearLayout
    LinearLayout ll_daoda_kehudingze;//客户定责LinearLayout
    LinearLayout ll_daoda_weather;//天气LinearLayout
    LinearLayout ll_daoda_lukuang;//路况LinearLayout

    TextView daoda_guzhangshebei_tv;//故障设备
    TextView daoda_fashengjieduan_tv;//发生阶段
    TextView daoda_kehudingze_tv;//客户定责
    TextView daoda_weather_tv;//天气
    TextView daoda_lukuang_tv;//路况
    TextView daoda_yunxingmodel_tv;//运行模式
    private TextView daoda_gzcode_tv;//故障代码
    private ClearEditText daoda_qianyindunwei;//牵引吨位
    TextView daoda_gzfasheng_time_tv;
    TextView daoda_gzhouguo_tv;
    TextView daoda_guzhangchuli_address_tv;

    String gdbianhao;
    String gdchexing;
    String gdchehao;

    NestedScrollView nestSv_daoda;


    ButtonView daoda_nextstep;

    private TimePickerView mTimePickerDialogxuqiudaoda;
    private TimePickerView mTimePickerDialogtelephone;

    private XUISimpleExpandablePopup mExpandableListPopup;
    private XUISimpleExpandablePopup mExpandableListPopupYunxingModel;//运行模式

    private XUISimpleExpandablePopup mExpandablegzresultPopup;//故障后果


    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    boolean canClickbtns = false;
    SearchFragment searchFragment;
    SearchFragment searchFragmentchehao;

    private PromptDialog promptDialog;
    private ClearEditText daoda_allmiles;
    private ClearEditText daoda_gzname_edit;
    private ClearEditText gzchuli_gongdanbainhao_et;
    LinearLayout ll_daodaxc_isornoweixianyuan;
    LinearLayout ll_down_weixianyuan;
    private TextView daodaxc_weixinayuan_tv;


    public DaodaxcFragment() {
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
        View v = inflater.inflate(R.layout.fragment_daodaxc,
                container, false);
        initViews(v);
        setClick();


        return v;
    }

    public void initViews(View v) {
        TextView tvToolbarTitle = getActivity().findViewById(R.id.tv_gzgd_toolbbar_title);
        tvToolbarTitle.setText("故障工单-到达现场");
        searchFragment = SearchFragment.newInstance();
        searchFragmentchehao = SearchFragment.newInstance();
        promptDialog = new PromptDialog(getActivity());

        fragmentManager = getActivity().getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();


        gongdanchexing = v.findViewById(R.id.daoda_chexing_tv);
        ll_daodaxc_isornoweixianyuan = v.findViewById(R.id.ll_daodaxc_isornoweixianyuan);
        daodaxc_weixinayuan_tv = v.findViewById(R.id.daodaxc_weixinayuan_tv);
        ll_down_weixianyuan = v.findViewById(R.id.ll_down_weixianyuan);

        daoda_allmiles = v.findViewById(R.id.daoda_allmiles);
        daoda_guzhangchuli_address_tv = v.findViewById(R.id.daoda_guzhangchuli_address_tv);
        daoda_gzname_edit = v.findViewById(R.id.daoda_gzname_edit);
        gzchuli_gongdanbainhao_et = v.findViewById(R.id.gzchuli_gongdanbainhao_et);


        daoda_chehao_tv = v.findViewById(R.id.daoda_chehao_tv);
        daoda_gzfasheng_time_tv = v.findViewById(R.id.daoda_gzfasheng_time_tv);
        daoda_gzhouguo_tv = v.findViewById(R.id.daoda_gzhouguo_tv);
        daoda_chexianghao_tv = v.findViewById(R.id.daoda_chexianghao_tv);
        daoda_guzhangshebei_tv = v.findViewById(R.id.daoda_guzhangshebei_tv);
        daoda_fashengjieduan_tv = v.findViewById(R.id.daoda_fashengjieduan_tv);
        daoda_kehudingze_tv = v.findViewById(R.id.daoda_kehudingze_tv);
        daoda_weather_tv = v.findViewById(R.id.daoda_weather_tv);
        daoda_lukuang_tv = v.findViewById(R.id.daoda_lukuang_tv);
        daoda_yunxingmodel_tv = v.findViewById(R.id.daoda_yunxingmodel_tv);
        scheck_box_yiyue_fuwuxiangy = v.findViewById(R.id.scheck_box_yiyue_fuwuxiangy);
        ll_daoda_chexing = v.findViewById(R.id.ll_daoda_chexing);
        ll_daoda_chehao = v.findViewById(R.id.ll_daoda_chehao);
        ll_pg_guzhangshebei = v.findViewById(R.id.ll_pg_guzhangshebei);
        ll_daoda_yunxingmodel = v.findViewById(R.id.ll_daoda_yunxingmodel);
        ll_daoda_fashengjieduan = v.findViewById(R.id.ll_daoda_fashengjieduan);
        ll_daoda_kehudingze = v.findViewById(R.id.ll_daoda_kehudingze);
        ll_daoda_weather = v.findViewById(R.id.ll_daoda_weather);
        ll_daoda_lukuang = v.findViewById(R.id.ll_daoda_lukuang);
        ll_daoda_fasheng_time = v.findViewById(R.id.ll_daoda_fasheng_time);
        ll_daoda_guzhangchuli_address = v.findViewById(R.id.ll_daoda_guzhangchuli_address);


        nestSv_daoda = v.findViewById(R.id.nestSv_daoda);
        daoda_guzhangchuli_address_tv = v.findViewById(R.id.daoda_guzhangchuli_address_tv);

        daoda_nextstep = v.findViewById(R.id.daoda_nextstep);

        ll_gaipaiyuanyin_visi = v.findViewById(R.id.ll_gaipaiyuanyin_visi);
        //车厢号
        ll_daoda_chexianghao = v.findViewById(R.id.ll_daoda_chexianghao);
        //故障代码
        ll_daoda_gzcode = v.findViewById(R.id.ll_daoda_gzcode);
        //故障后果
        ll_daoda_gzhouguo = v.findViewById(R.id.ll_daoda_gzhouguo);


        daoda_gzcode_tv = v.findViewById(R.id.daoda_gzcode_tv);
        daoda_gzfasheng_time_tv = v.findViewById(R.id.daoda_gzfasheng_time_tv);
        daoda_qianyindunwei = v.findViewById(R.id.daoda_qianyindunwei);


        daoda_guzhangchuli_address_tv.setFocusable(true);
        daoda_guzhangchuli_address_tv.setFocusableInTouchMode(true);
        daoda_guzhangchuli_address_tv.requestFocus();

        initExpandableListPopup();
        initExpandableListPopupYunxingModel();
        initExpandableListgzresultPopup();

        //系统带出的字段数据展示
        setValue();
    }


    public void setValue() {
        //累计走行公里
        int pos = ((FuwuxyTianxieActivity) getActivity()).itemPos;
        System.out.println("--daodaxianchang position----" + pos);
        FuwuDataInfo lsdata = FuwuDataTwoProvider.getFwxyListdata().get(pos);
        daoda_allmiles.setText(lsdata.getLeijizouxing());

        daoda_guzhangchuli_address_tv.setText(FiledDataSave.WHICHSTATION);
        daoda_gzhouguo_tv.setText(FiledDataSave.FAULTCONSEQ);
        daoda_gzname_edit.setText(FiledDataSave.FAILUREDESC);
        daoda_chexianghao_tv.setText(FiledDataSave.CARSECTIONNUM);
        gzchuli_gongdanbainhao_et.setText(lsdata.getGdbh());
        gongdanchexing.setText(lsdata.getChexingvalue());
        daoda_chehao_tv.setText(lsdata.getChehaoValue());

        daoda_gzcode_tv.setText(FiledDataSave.FAILURECODE);
        daoda_guzhangshebei_tv.setText(FiledDataSave.PRODUCTNICKNAME);
        daoda_fashengjieduan_tv.setText(FiledDataSave.FINDPROCESS);
        daoda_kehudingze_tv.setText(FiledDataSave.FAULTQUALIT);
        daoda_weather_tv.setText(FiledDataSave.FAILWEATHER);
        daoda_lukuang_tv.setText(FiledDataSave.ROADTYPE);
        daoda_yunxingmodel_tv.setText(FiledDataSave.RUNNINGMODE);


    }

    //该页面需要提交的数据组合的key value。
    public String getStringPost() {
        String s = "";
        return s;
    }


    public void setClick() {
        if (Constant.statusinfo.equals("已派工")){
            ll_daodaxc_isornoweixianyuan.setEnabled(true);
            ll_daodaxc_isornoweixianyuan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showContextMenuDialogWeixianyuan();
                }
            });
        }else{
            ll_down_weixianyuan.setVisibility(View.VISIBLE);
            daodaxc_weixinayuan_tv.setText("否");
            ll_daodaxc_isornoweixianyuan.setEnabled(false);
        }


        //故障处理站点
        ll_daoda_guzhangchuli_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XToastUtils.info("搜索..........");
            }

        });

        //车厢号
        ll_daoda_chexianghao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showSingleChoiceDialogCheXianghao();
                showContextMenuDialogChexianghao();
            }
        });
        //故障代码
        ll_daoda_gzcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XToastUtils.info("搜索..........");
            }
        });
        //故障后果
        ll_daoda_gzhouguo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandablegzresultPopup.clearExpandStatus();
                mExpandablegzresultPopup.showDown(v);
            }
        });
        //故障设备
        ll_pg_guzhangshebei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandableListPopup.clearExpandStatus();
                mExpandableListPopup.showDown(v);
            }
        });
        //运行模式
        ll_daoda_yunxingmodel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandableListPopupYunxingModel.clearExpandStatus();
                mExpandableListPopupYunxingModel.showDown(v);
            }
        });
        //发生阶段
        ll_daoda_fashengjieduan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContextMenuDialogFashengjieduan();
            }
        });
        //客户定责
        ll_daoda_kehudingze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContextMenuDialogkehudingze();
            }
        });
        //天气
        ll_daoda_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContextMenuDialogWeather();
            }
        });
        //路况
        ll_daoda_lukuang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContextMenuDialoglukuang();
            }
        });
        //故障发生时间
        ll_daoda_fasheng_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gzfashengtimeshowTimePickerDialog();
            }
        });


        //搜索框......车型
        searchFragment.setOnSearchClickListener(new IOnSearchClickListener() {
            @Override
            public void OnSearchClick(String keyword) {
                //点击搜索项 进行搜索...
                XToastUtils.success("搜索" + keyword);

                promptDialog.showLoading("搜索中...");
                //展示模拟联网搜索动画，比如需1秒
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        promptDialog.dismiss();
                        //把搜索的结果展示在单选框中  若没有搜索到结果，则提示用户
                        showSingleChoiceDialog();

                    }
                }, 1000);


            }
        });

        //车号
        searchFragmentchehao.setOnSearchClickListener(new IOnSearchClickListener() {
            @Override
            public void OnSearchClick(String keyword) {
                //点击搜索项 进行搜索...
                XToastUtils.success("搜索" + keyword);

                promptDialog.showLoading("搜索中...");
                //展示模拟联网搜索动画，比如需1秒
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        promptDialog.dismiss();
                        //把搜索的结果展示在单选框中  若没有搜索到结果，则提示用户
                        showSingleChoiceDialogChehao();

                    }
                }, 1000);


            }
        });


        //车型 搜索
//        ll_daoda_chexing.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchFragment.showFragment(getActivity().getSupportFragmentManager(), SearchFragment.TAG);
//            }
//        });

//        ll_daoda_needdaodatime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                xuqiudaodatimeshowTimePickerDialog();
//            }
//        });


//        //车号 搜索
//        ll_daoda_chehao.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchFragmentchehao.showFragment(getActivity().getSupportFragmentManager(), SearchFragment.TAG);
//
//            }
//        });

        //下一步
        daoda_nextstep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //记得failurelib中的唯一标识id
                Constant.keyValueDaodaxcPosttomroWorkorder = getFieldValue();//获取传参
                Constant.keyValueDaodaxcfailurelibPosttomro = getFieldValueForFailureLib();//获取传参
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //传到workorder表中的数据
                        //传到failurelib中的数据，所有有两次传输数据的请求

                        requestPostInfoToWorkorder();
                    }
                }, 200);

            }
        });


    }

    private void showContextMenuDialogWeixianyuan() {

        new MaterialDialog.Builder(getActivity())
                .title("请选择")
                .items(R.array.isornot_values)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        daodaxc_weixinayuan_tv.setText(text);

                        String isorno = daodaxc_weixinayuan_tv.getText().toString().trim();

                        if (isorno.length() > 0) {
                            if (isorno.equals("是")) {

                                //先把是，否的危险源值以及是否审批的值发送到mro

                                //再发送审批工作流
                                requestPostWeixianValue();

                            } else if (isorno.equals("否")) {
                                //触发 已派工至处理中的执行工作流
                                //类似服务响应
                                requestQingqiuWeixyuanFouWorkliu();
                            }
                        } else {

                        }

                    }
                })
                .show();
    }


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
//这里不获取actionid了，直接写死为1表示执行处理工作流，即执行危险源审核

                        requestQingqiuWorkliu();
                    } else {
                        System.out.println("危险源值更新失败！");
                    }

                }, throwable -> {
                    //失败回调
                    System.out.println("失败结果---：" + throwable.getMessage());

                });
    }


    //申请执行工作流
    private void requestQingqiuWorkliu() {
        //actionid = 1表示请求处理  为2表示改派。或1表示危险源审核
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
                        ll_daodaxc_isornoweixianyuan.setEnabled(false);
                        XToastUtils.success("危险源审核发送成功");
                    } else {
                        XToastUtils.error("危险源审核发送失败！");
                    }


                }, throwable -> {
                    XToastUtils.error("危险源审核发送失败！");
                    //失败回调
                    System.out.println("失败结果-1提交---：" + throwable.getMessage());
                });
    }


    //选择否，申请处理工作流
    private void requestQingqiuWeixyuanFouWorkliu() {
        RxHttp.postForm(Constant.usualInterfaceAddr)
                .add(Constant.usualKey, Constant.getFaqiFwxyWorkStream())
                .asClass(FwxyShenqingchuliWorkLiu.class).
                observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    //请求开始，当前在主线程回调
                    promptDialog.showLoading("加载中...");

                })
                .doFinally(() -> {
                    promptDialog.dismissImmediately();

                }).as(RxLife.as(this))  //感知生命周期 当退出页面时 请求未完成，则关闭请求，防止内存泄漏
                .subscribe(clz -> {//clz就是Pojo

                    if (clz.code.equals("S")) {
                        XToastUtils.success("设置成功");
                        ll_down_weixianyuan.setVisibility(View.VISIBLE);
                        ll_daodaxc_isornoweixianyuan.setEnabled(false);

                    } else {
                        daodaxc_weixinayuan_tv.setText("");
                        XToastUtils.success("设置失败");

                    }


                }, throwable -> {
                    XToastUtils.success("设置失败");
                    daodaxc_weixinayuan_tv.setText("");
                    //失败回调
                    System.out.println("失败结果服务响应提交---：" + throwable.getMessage());

                });
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
                            System.out.println("zhi:" + clz.data.get(i).actionid + "=" + clz.data.get(i).actiondesc);
                        }
                        Constant.ActionId = clz.data.get(0).actionid;
                        System.out.println("actionid====" + Constant.ActionId);
                    }

                }, throwable -> {
                    //失败回调
                    System.out.println("失败结果---：" + throwable.getMessage());

                });


    }

    //需要提交的字段数据。
    public String getFieldValue() {
        String daoda_chexianghao_tv_s = daoda_chexianghao_tv.getText().toString().trim();
        String daoda_allmiles_s = daoda_allmiles.getText().toString().trim();
        String va =
                "\"CARSECTIONNUM\"" + ":" + "\"" + daoda_chexianghao_tv_s + "\"" + ","
                        + "\"RUNKILOMETRE\"" + ":" + "\"" + daoda_allmiles_s + "\"";
        return va;
    }

    //需要提交的FailureLib字段数据。
    public String getFieldValueForFailureLib() {
        String daoda_gzname_edit_s = daoda_gzname_edit.getText().toString().trim();
        String daoda_gzhouguo_tv_s = daoda_gzhouguo_tv.getText().toString().trim();
        String daoda_gzfasheng_time_tv_s = daoda_gzfasheng_time_tv.getText().toString().trim();
        String daoda_guzhangshebei_tv_s = daoda_guzhangshebei_tv.getText().toString().trim();
        String daoda_kehudingze_tv_s = daoda_kehudingze_tv.getText().toString().trim();
        String daoda_fashengjieduan_tv_s = daoda_fashengjieduan_tv.getText().toString().trim();
        String daoda_yunxingmodel_tv_s = daoda_yunxingmodel_tv.getText().toString().trim();
        String daoda_weather_tv_s = daoda_weather_tv.getText().toString().trim();
        String daoda_lukuang_tv_s = daoda_lukuang_tv.getText().toString().trim();
        String daoda_qianyindunwei_s = daoda_qianyindunwei.getText().toString().trim();
        String va =
                "\"FAILUREDESC\"" + ":" + "\"" + daoda_gzname_edit_s + "\"" + ","
                        + "\"FAULTCONSEQ\"" + ":" + "\"" + daoda_gzhouguo_tv_s + "\"" + ","
                        + "\"FAULTTIME\"" + ":" + "\"" + daoda_gzfasheng_time_tv_s + "\"" + ","
                        + "\"PRODUCTNICKNAME\"" + ":" + "\"" + daoda_guzhangshebei_tv_s + "\"" + ","
                        + "\"FAULTQUALIT\"" + ":" + "\"" + daoda_kehudingze_tv_s + "\"" + ","
                        + "\"FINDPROCESS\"" + ":" + "\"" + daoda_fashengjieduan_tv_s + "\"" + ","
                        + "\"RUNNINGMODE\"" + ":" + "\"" + daoda_yunxingmodel_tv_s + "\"" + ","
                        + "\"FAILWEATHER\"" + ":" + "\"" + daoda_weather_tv_s + "\"" + ","
                        + "\"ROADTYPE\"" + ":" + "\"" + daoda_lukuang_tv_s + "\"" + ","
                        + "\"QYFZDW\"" + ":" + "\"" + daoda_qianyindunwei_s + "\"";
        return va;
    }

    //提交数据到workorder中
    private void requestPostInfoToWorkorder() {
        RxHttp.postForm(Constant.usualInterfaceAddr)
                .add(Constant.usualKey, Constant.getdaodaxcPostInfoToWorkorder())
                .asClass(GuzDaodaxcPostInfo.class).
                observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    //请求开始，当前在主线程回调
                    promptDialog.showLoading("加载中...");
                })
                .doFinally(() -> {
                    //请求结束，当前在主线程回调

                }).as(RxLife.as(this))  //感知生命周期 当退出页面时 请求未完成，则关闭请求，防止内存泄漏
                .subscribe(clz -> {//clz就是Pojo

                    if (clz.code.equals("S") && clz.msg.equals("操作成功")) {
                        FiledDataSave.fwxyBtnEffect = true;//表示可以点击 到达现场顶部按钮

                        requestPostInfoToFailureLib();


                    } else {
                        XToastUtils.success("提交失败");
                        FiledDataSave.fwxyBtnEffect = false;//表示可以不可点击 到达现场顶部按钮

                    }


                }, throwable -> {
                    FiledDataSave.fwxyBtnEffect = false;//表示可以不可点击 到达现场顶部按钮
                    XToastUtils.success("提交失败");
                    promptDialog.dismissImmediately();
                    //失败回调
                    System.out.println("失败结果服务响应提交---：" + throwable.getMessage());

                });

    }


    //注意failurelib中的唯一标识id的获取，传参。
    private void requestPostInfoToFailureLib() {
        RxHttp.postForm(Constant.usualInterfaceAddr)
                .add(Constant.usualKey, Constant.getdaodaxcPostInfoToFailurelib())
                .asClass(GuzDaodaxcPostInfoForFailLib.class).
                observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    //请求开始，当前在主线程回调
                })
                .doFinally(() -> {
                    //请求结束，当前在主线程回调
                    promptDialog.dismissImmediately();
                }).as(RxLife.as(this))  //感知生命周期 当退出页面时 请求未完成，则关闭请求，防止内存泄漏
                .subscribe(clz -> {//clz就是Pojo

                    if (clz.code.equals("S") && clz.msg.equals("操作成功")) {
                        FiledDataSave.fwxyBtnEffect = true;//表示可以点击 到达现场顶部按钮
                        XToastUtils.success("提交成功");

                        ((FuwuxyTianxieActivity) getActivity()).gotoGuzhangChulifrgment();

                    } else {
                        XToastUtils.success("提交失败");
                        FiledDataSave.fwxyBtnEffect = false;//表示可以不可点击 到达现场顶部按钮

                    }


                }, throwable -> {
                    FiledDataSave.fwxyBtnEffect = false;//表示可以不可点击 到达现场顶部按钮
                    XToastUtils.success("提交失败");
                    promptDialog.dismissImmediately();

                    //失败回调
                    System.out.println("失败结果22服务响应提交---：" + throwable.getMessage());

                });

    }

    private void gzfashengtimeshowTimePickerDialog() {
        if (mTimePickerDialogxuqiudaoda == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.string2Date("2013-07-08 12:32:46", DateUtils.yyyyMMddHHmmss.get()));
            mTimePickerDialogxuqiudaoda = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
                @Override
                public void onTimeSelected(Date date, View v) {
                    XToastUtils.toast(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get()));
                    daoda_gzfasheng_time_tv.setText(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get()));

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


    /**
     * 带单选项的Dialog
     */
    private void showSingleChoiceDialog() {
        new MaterialDialog.Builder(getContext())
                .title("请选择搜索的结果")
                .items(R.array.router_choice_entry)
                .itemsCallbackSingleChoice(
                        0,
                        new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                gongdanchexing.setText(text);
                                return true;
                            }
                        })
                .positiveText("选择")
                .negativeText("取消")
                .show();
    }

    /**
     * 带单选项的Dialog
     */
    private void showSingleChoiceDialogChehao() {
        new MaterialDialog.Builder(getContext())
                .title("请选择搜索的结果")
                .items(R.array.chehosearcharry)
                .itemsCallbackSingleChoice(
                        0,
                        new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                XToastUtils.toast(which + ": " + text);
                                daoda_chehao_tv.setText(text);
                                return true;
                            }
                        })
                .positiveText("选择")
                .negativeText("取消")
                .show();
    }


    /**
     * 带单选项的Dialog  ----->>>车厢号
     */
    private void showSingleChoiceDialogCheXianghao() {
        new MaterialDialog.Builder(getContext())
                .title("请选择车厢号")
                .items(R.array.chexianghaoarry)
                .itemsCallbackSingleChoice(
                        0,
                        new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                daoda_chexianghao_tv.setText(text);
                                return true;
                            }
                        })
                .positiveText("选择")
                .negativeText("取消")
                .show();
    }


    //下拉选项  有多个分类时
    private void initExpandableListPopup() {
        mExpandableListPopup = new XUISimpleExpandablePopup(getActivity(),
                DemoDataProvider.guzhangshebeiitems)
                .create(DensityUtils.dip2px(getActivity(), 200), DensityUtils.dip2px(
                        getActivity(), 200))
                .setOnExpandableItemClickListener(true, new XUISimpleExpandablePopup.OnExpandableItemClickListener() {
                    @Override
                    public void onExpandableItemClick(XUISimpleExpandableListAdapter adapter, ExpandableItem group, int groupPosition, int childPosition) {
                        daoda_guzhangshebei_tv.setText(group.getChildItem(childPosition).getTitle());
                    }
                });
    }

    //下拉选项  有多个分类时
    private void initExpandableListPopupYunxingModel() {
        mExpandableListPopupYunxingModel = new XUISimpleExpandablePopup(getActivity(),
                DemoDataProvider.yunxingmodelitems)
                .create(DensityUtils.dip2px(getActivity(), 200), DensityUtils.dip2px(
                        getActivity(), 200))
                .setOnExpandableItemClickListener(true, new XUISimpleExpandablePopup.OnExpandableItemClickListener() {
                    @Override
                    public void onExpandableItemClick(XUISimpleExpandableListAdapter adapter, ExpandableItem group, int groupPosition, int childPosition) {

                        daoda_yunxingmodel_tv.setText(group.getChildItem(childPosition).getTitle());
                    }
                });
    }


    //下拉选项为一个类别时  发生阶段
    private void showContextMenuDialogFashengjieduan() {
        new MaterialDialog.Builder(getActivity())
                .title("请选择")
                .items(R.array.fashengjieduan_values)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        daoda_fashengjieduan_tv.setText(text);
                    }
                })
                .show();
    }

    //下拉选项为一个类别时  车厢号
    private void showContextMenuDialogChexianghao() {
        new MaterialDialog.Builder(getActivity())
                .title("请选择")
                .items(R.array.chexianghaoarry)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        daoda_chexianghao_tv.setText(text);
                    }
                })
                .show();
    }

    //下拉选项为一个类别时  客户定责
    private void showContextMenuDialogkehudingze() {
        new MaterialDialog.Builder(getActivity())
                .title("请选择")
                .items(R.array.kehudingze_values)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        daoda_kehudingze_tv.setText(text);
                    }
                })
                .show();
    }

    //下拉选项为一个类别时  天气
    private void showContextMenuDialogWeather() {
        new MaterialDialog.Builder(getActivity())
                .title("请选择")
                .items(R.array.weather_values)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        daoda_weather_tv.setText(text);
                    }
                })
                .show();
    }

    //下拉选项为一个类别时  路况
    private void showContextMenuDialoglukuang() {
        new MaterialDialog.Builder(getActivity())
                .title("请选择")
                .items(R.array.lukuang_values)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        daoda_lukuang_tv.setText(text);
                    }
                })
                .show();
    }

    //下拉选项  有多个分类时 故障后果
    private void initExpandableListgzresultPopup() {
        mExpandablegzresultPopup = new XUISimpleExpandablePopup(getActivity(),
                DemoDataProvider.expandableItems)
                .create(DensityUtils.dip2px(getActivity(), 200), DensityUtils.dip2px(
                        getActivity(), 200))
                .setOnExpandableItemClickListener(true, new XUISimpleExpandablePopup.OnExpandableItemClickListener() {
                    @Override
                    public void onExpandableItemClick(XUISimpleExpandableListAdapter adapter, ExpandableItem group, int groupPosition, int childPosition) {
                        daoda_gzhouguo_tv.setText(group.getChildItem(childPosition).getTitle());
                    }
                });
    }

}
