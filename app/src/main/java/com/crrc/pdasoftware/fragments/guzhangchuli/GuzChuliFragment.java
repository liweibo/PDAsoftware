package com.crrc.pdasoftware.fragments.guzhangchuli;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.crrc.pdasoftware.activity.all.guzhangchuli.FuwuxyTianxieActivity;
import com.crrc.pdasoftware.MyApplication;
import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.utils.BitmapUtils;
import com.crrc.pdasoftware.utils.DemoDataProvider;
import com.crrc.pdasoftware.utils.GlideEngine;
import com.crrc.pdasoftware.utils.ScanUtils;
import com.crrc.pdasoftware.utils.XToastUtils;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.wyt.searchbox.SearchFragment;
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
import com.xuexiang.xutil.file.FileUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import me.leefeng.promptlibrary.PromptDialog;

import static android.content.Context.MODE_PRIVATE;


public class GuzChuliFragment extends Fragment {


    TextView publicTvScanValue;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    TextView gongdanchexing;
    TextView guzchuli_chehao_tv;
    TextView guzchuli_chexianghao_tv;
    TextView guzchuli_guzhangshebei_tv;
    TextView guzchuli_fashengjieduan_tv;

    TextView gzchuli_danger_tv;//危险源tv
    TextView gzchuli_chulimethod_tv;//故障处理方式tv
    TextView guzchuli_kehudingze_tv;
    TextView guzchuli_weather_tv;
    TextView guzchuli_lukuang_tv;
    TextView guzchuli_yunxingmodel_tv;
    SmoothCheckBox scheck_box_yiyue_fuwuxiangy;


    LinearLayout ll_gaipaiyuanyin_visi;

    LinearLayout ll_pg_guzhangshebei;


    LinearLayout ll_gzchuli_danger;//危险源
    LinearLayout ll_gzchuli_chulimethod;//故障处理方式
    LinearLayout ll_down;//危险源下部分 ui

    LinearLayout ll_guzchuli_xitonggongnnegjian;//系统功能键
    LinearLayout ll_guzchuli_guzxianx;
    LinearLayout ll_guzchuli_guzyaunyin;
    LinearLayout ll_guzchuli_chulicshi;
    LinearLayout ll_guzchuli_chulijieguo;

    //故障后果 等
    LinearLayout ll_gzchuli_faultresult;
    LinearLayout ll_gzchuli_whynodada;
    LinearLayout ll_guzchuli_kehu_dingze;
    LinearLayout ll_gzchuli_kehu_xu_fenxibaogao;
    LinearLayout ll_gzchuli_zuizhongjieguo;

    //故障后果等等
    TextView gzchuli_faultresult_tv;
    TextView gzchuli_whynodata_tv;
    TextView gzchuli_kehu_dingze_tv;
    TextView gzchuli_kehu_xu_fenxibaogao_tv;
    TextView gzchuli_zuizhongjieguo_tv;


    TextView guzchuli_chulijieguo_tv;
    TextView guzchuli_chulicshi_tv;
    TextView guzchuli_guzyuanyin_tv;
    TextView guzchuli_guzxianx_tv;
    TextView guzchuli_xitonggnjian_tv;


    LinearLayout ll_guzchuli_guajingshipai;//挂警示牌
    LinearLayout ll_guzchuli_yijichanpingxuhao;//一级产品序号 扫码
    TextView guzchuli_yijichanpingxuhao;

    TextView guzchuli_gzfasheng_time_tv;
    TextView guzchuli_teletime_tv;

    String gdbianhao;
    String gdchexing;
    String gdchehao;

    NestedScrollView nestSv_guzchuli;
    TextView guzchuli_yijichanpingname;


    ButtonView downloadre;
    ButtonView uploadre;
    ButtonView getzhenduanresult;

    Button btn_paichaqueren;

    private TimePickerView mTimePickerDialogxuqiuguzchuli;
    private TimePickerView mTimePickerDialogtelephone;

    private XUISimpleExpandablePopup mExpandablegzresultPopup;//故障后果

    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    boolean canClickbtns = false;
    SearchFragment searchFragment;
    SearchFragment searchFragmentchehao;

    private PromptDialog promptDialog;

    ImageView danger_file_see;

    boolean dangerFlag = true;//默认是危险作业（true），为空也看做是危险作业；false表示非危险作业
    private ArrayList<Photo> selectedPhotoList = new ArrayList<>();


    public GuzChuliFragment() {
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
        View v = inflater.inflate(R.layout.fragment_guz_chuli,
                container, false);
        initViews(v);
        setClick();
        String dangerTv = gzchuli_danger_tv.getText().toString().trim();
        if (dangerFlag) {
            ll_down.setVisibility(View.GONE);
            if (dangerTv.length() == 0) {
                danger_file_see.setVisibility(View.GONE);
            } else {
                danger_file_see.setVisibility(View.VISIBLE);
            }


        } else {
            ll_down.setVisibility(View.VISIBLE);
            danger_file_see.setVisibility(View.GONE);


        }


        return v;
    }

    public void initViews(View v) {
        searchFragment = SearchFragment.newInstance();
        searchFragmentchehao = SearchFragment.newInstance();
        promptDialog = new PromptDialog(getActivity());

        fragmentManager = getActivity().getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();


        gzchuli_danger_tv = v.findViewById(R.id.gzchuli_danger_tv);
        gzchuli_chulimethod_tv = v.findViewById(R.id.gzchuli_chulimethod_tv);

        scheck_box_yiyue_fuwuxiangy = v.findViewById(R.id.scheck_box_yiyue_fuwuxiangy);
        ll_pg_guzhangshebei = v.findViewById(R.id.ll_pg_guzhangshebei);

        ll_gzchuli_danger = v.findViewById(R.id.ll_gzchuli_danger);
        ll_gzchuli_chulimethod = v.findViewById(R.id.ll_gzchuli_chulimethod);
        ll_guzchuli_xitonggongnnegjian = v.findViewById(R.id.ll_guzchuli_xitonggongnnegjian);


        ll_guzchuli_guzxianx = v.findViewById(R.id.ll_guzchuli_guzxianx);
        ll_guzchuli_guzyaunyin = v.findViewById(R.id.ll_guzchuli_guzyaunyin);
        ll_guzchuli_chulicshi = v.findViewById(R.id.ll_guzchuli_chulicshi);
        ll_guzchuli_chulijieguo = v.findViewById(R.id.ll_guzchuli_chulijieguo);


        ll_gzchuli_faultresult = v.findViewById(R.id.ll_gzchuli_faultresult);
        ll_gzchuli_whynodada = v.findViewById(R.id.ll_gzchuli_whynodada);
        ll_guzchuli_kehu_dingze = v.findViewById(R.id.ll_guzchuli_kehu_dingze);
        ll_gzchuli_kehu_xu_fenxibaogao = v.findViewById(R.id.ll_gzchuli_kehu_xu_fenxibaogao);
        ll_gzchuli_zuizhongjieguo = v.findViewById(R.id.ll_gzchuli_zuizhongjieguo);


        guzchuli_chulijieguo_tv = v.findViewById(R.id.guzchuli_chulijieguo_tv);
        guzchuli_chulicshi_tv = v.findViewById(R.id.guzchuli_chulicshi_tv);
        guzchuli_guzyuanyin_tv = v.findViewById(R.id.guzchuli_guzyuanyin_tv);
        guzchuli_guzxianx_tv = v.findViewById(R.id.guzchuli_guzxianx_tv);
        guzchuli_xitonggnjian_tv = v.findViewById(R.id.guzchuli_xitonggnjian_tv);


        gzchuli_faultresult_tv = v.findViewById(R.id.gzchuli_faultresult_tv);
        gzchuli_whynodata_tv = v.findViewById(R.id.gzchuli_whynodata_tv);
        gzchuli_kehu_dingze_tv = v.findViewById(R.id.gzchuli_kehu_dingze_tv);
        gzchuli_kehu_xu_fenxibaogao_tv = v.findViewById(R.id.gzchuli_kehu_xu_fenxibaogao_tv);
        gzchuli_kehu_xu_fenxibaogao_tv.setText("否");
        gzchuli_zuizhongjieguo_tv = v.findViewById(R.id.gzchuli_zuizhongjieguo_tv);
        guzchuli_yijichanpingxuhao = v.findViewById(R.id.guzchuli_yijichanpingxuhao);

        ll_down = v.findViewById(R.id.ll_down);


        //挂警示牌 拍照
        ll_guzchuli_guajingshipai = v.findViewById(R.id.ll_guzchuli_guajingshipai);

        //一级产品序号 扫码
        ll_guzchuli_yijichanpingxuhao = v.findViewById(R.id.ll_guzchuli_yijichanpingxuhao);


        nestSv_guzchuli = v.findViewById(R.id.nestSv_guzchuli);
        guzchuli_yijichanpingname = v.findViewById(R.id.guzchuli_yijichanpingname);

        downloadre = v.findViewById(R.id.downloadre);
        uploadre = v.findViewById(R.id.uploadre);
        getzhenduanresult = v.findViewById(R.id.getzhenduanresult);
        btn_paichaqueren = v.findViewById(R.id.btn_paichaqueren);

        ll_gaipaiyuanyin_visi = v.findViewById(R.id.ll_gaipaiyuanyin_visi);


        //危险指引文件查看
        danger_file_see = v.findViewById(R.id.danger_file_see);


        gdbianhao = pref.getString("gzgongdanbianhao", "不存在");
        gdchexing = pref.getString("gzgongdanchexing", "不存在");
        gdchehao = pref.getString("gzgongdanchehao", "不存在");


        guzchuli_yijichanpingname.setFocusable(true);
        guzchuli_yijichanpingname.setFocusableInTouchMode(true);
        guzchuli_yijichanpingname.requestFocus();

        initExpandableListgzresultPopup();




    }


    public void setClick() {
        danger_file_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        //挂警示牌 拍照
        ll_guzchuli_guajingshipai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EasyPhotos.createAlbum(getActivity(), true, GlideEngine.getInstance())
                        .setFileProviderAuthority("com.huantansheng.easyphotos.demo.fileprovider")
                        .setCount(1)
                        .start(new SelectCallback() {
                            @Override
                            public void onResult(ArrayList<Photo> photos, boolean isOriginal) {
                                selectedPhotoList.clear();
                                selectedPhotoList.addAll(photos);
                                System.out.println("打印uri：" + photos.get(0).uri.toString());
                                System.out.println("打印path：" + photos.get(0).path);
                                System.out.println("打印size：" + photos.get(0).size);

                                scaleImage(photos.get(0).uri);
                            }
                        });


            }
        });


        //一级产品序号
        ll_guzchuli_yijichanpingxuhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定是哪个textview中赋扫出来的值；
                publicTvScanValue = guzchuli_yijichanpingxuhao;
                //启动扫码；
                ScanUtils.springScan(getActivity());

            }
        });


        //危险源
        ll_gzchuli_danger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContextMenuDialogDanger();
            }
        });

        //故障处理方式
        ll_gzchuli_chulimethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContextMenuDialogGuzhangclfangshi();
            }
        });


        //下载
        downloadre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XToastUtils.success("下载");
            }
        });
        //上传
        uploadre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XToastUtils.success("上传");

            }
        });
//获取诊断结果
        getzhenduanresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XToastUtils.success("获取诊断结果");

            }
        });

        //排查确认
        btn_paichaqueren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XToastUtils.success("排查确认");
            }
        });

        //系统功能件
        ll_guzchuli_xitonggongnnegjian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContextMenuDialogXitGnjian();

            }
        });

        //故障现象
        ll_guzchuli_guzxianx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContextMenuDialoggzxianxiang();

            }
        });

        //故障原因
        ll_guzchuli_guzyaunyin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContextMenuDialogGuzhangYuanyin();

            }
        });


        //处理措施
        ll_guzchuli_chulicshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContextMenuDialogchulicuoshi();

            }
        });


        //处理结果
        ll_guzchuli_chulijieguo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContextMenuDialogChulijieguo();

            }
        });


        //故障后果
        ll_gzchuli_faultresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandablegzresultPopup.clearExpandStatus();
                mExpandablegzresultPopup.showDown(v);
//                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });
        //无数据原因
        ll_gzchuli_whynodada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContextMenuDialoggzchuli_whynodata_tv();
            }
        });
        //客户定责
        ll_guzchuli_kehu_dingze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContextMenuDialoggzchuli_kehu_dingze_tv();
            }
        });
        //客户需分析报告
        ll_gzchuli_kehu_xu_fenxibaogao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContextMenuDialoggzchuli_kehu_xu_fenxibaogao_tv();
            }
        });
        //最终结果
        ll_gzchuli_zuizhongjieguo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContextMenuDialoggzchuli_zuizhongjieguo_tv();
            }
        });
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
                        gzchuli_faultresult_tv.setText(group.getChildItem(childPosition).getTitle());
                    }
                });
    }


    private void gzfashengtimeshowTimePickerDialog() {
        if (mTimePickerDialogxuqiuguzchuli == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.string2Date("2013-07-08 12:32:46", DateUtils.yyyyMMddHHmmss.get()));
            mTimePickerDialogxuqiuguzchuli = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
                @Override
                public void onTimeSelected(Date date, View v) {
                    XToastUtils.toast(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get()));
                    guzchuli_gzfasheng_time_tv.setText(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get()));

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
        mTimePickerDialogxuqiuguzchuli.show();
    }

    private void telephonetimeshowTimePickerDialog() {
        if (mTimePickerDialogtelephone == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.string2Date("2013-07-08 12:32:46", DateUtils.yyyyMMddHHmmss.get()));
            mTimePickerDialogtelephone = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
                @Override
                public void onTimeSelected(Date date, View v) {
                    XToastUtils.toast(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get()));
                    guzchuli_teletime_tv.setText(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get()));

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
                                guzchuli_chehao_tv.setText(text);
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
                                guzchuli_chexianghao_tv.setText(text);
                                return true;
                            }
                        })
                .positiveText("选择")
                .negativeText("取消")
                .show();
    }


    //下拉选项为一个类别时  危险源
    private void showContextMenuDialogDanger() {
        new MaterialDialog.Builder(getActivity())
                .title("请选择")
                .items(R.array.dangers_values)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        gzchuli_danger_tv.setText(text);
                        String tvText = gzchuli_danger_tv.getText().toString().trim();
                        if (tvText.equals("非危险作业")) {
                            dangerFlag = false;
                            ll_down.setVisibility(View.VISIBLE);
                            danger_file_see.setVisibility(View.GONE);
                        } else {
                            //若 为空 或者为危险作业
                            dangerFlag = true;
                            ll_down.setVisibility(View.GONE);
                            danger_file_see.setVisibility(View.VISIBLE);


                        }
                    }
                })
                .show();
    }


    //下拉选项为一个类别时  故障处理方式
    private void showContextMenuDialogGuzhangclfangshi() {
        new MaterialDialog.Builder(getActivity())
                .title("请选择")
                .items(R.array.gzchulimethod_values)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        gzchuli_chulimethod_tv.setText(text);
                        String tvText = gzchuli_chulimethod_tv.getText().toString().trim();
                        if (tvText.equals("挂起-待料") || tvText.equals("挂起-其他")) {
                            ((FuwuxyTianxieActivity) getActivity()).gotoGuaqiFrgment();
                        } else if (tvText.equals("换件处理")) {
                            //下一步

                            ((FuwuxyTianxieActivity) getActivity()).gotoHuanjianFrgment();


                        }
                    }
                })
                .show();
    }


    //下拉选项为一个类别时  系统功能件
    private void showContextMenuDialogXitGnjian() {
        new MaterialDialog.Builder(getActivity())
                .title("请选择")
                .items(R.array.xitonggnjian_values)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        guzchuli_xitonggnjian_tv.setText(text);
                    }
                })
                .show();
    }


    //下拉选项为一个类别时  故障现象
    private void showContextMenuDialoggzxianxiang() {
        new MaterialDialog.Builder(getActivity())
                .title("请选择")
                .items(R.array.guzxianxiang_values)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        guzchuli_guzxianx_tv.setText(text);
                    }
                })
                .show();
    }


    //下拉选项为一个类别时  故障原因
    private void showContextMenuDialogGuzhangYuanyin() {
        new MaterialDialog.Builder(getActivity())
                .title("请选择")
                .items(R.array.gzyuanyin_values)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        guzchuli_guzyuanyin_tv.setText(text);
                    }
                })
                .show();
    }


    //下拉选项为一个类别时  处理措施
    private void showContextMenuDialogchulicuoshi() {
        new MaterialDialog.Builder(getActivity())
                .title("请选择")
                .items(R.array.chulijieguo_values)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        guzchuli_chulicshi_tv.setText(text);
                    }
                })
                .show();
    }


    //下拉选项为一个类别时  处理结果
    private void showContextMenuDialogChulijieguo() {
        new MaterialDialog.Builder(getActivity())
                .title("请选择")
                .items(R.array.chulijieguo_values)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        guzchuli_chulijieguo_tv.setText(text);
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
                        guzchuli_kehudingze_tv.setText(text);
                    }
                })
                .show();
    }


    //下拉选项为一个类别时  故障后果
    private void showDialoggzchuli_faultresult_tv() {
        new MaterialDialog.Builder(getActivity())
                .title("请选择")
                .items(R.array.kehudingze_values)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        gzchuli_faultresult_tv.setText(text);
                    }
                })

                .show();
    }


    //下拉选项为一个类别时  无数据原因
    private void showContextMenuDialoggzchuli_whynodata_tv() {
        new MaterialDialog.Builder(getActivity())
                .title("请选择")
                .items(R.array.gzchuli_whynodata_tv_values)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        gzchuli_whynodata_tv.setText(text);
                    }
                })
                .show();
    }


    //下拉选项为一个类别时  客户定责
    private void showContextMenuDialoggzchuli_kehu_dingze_tv() {
        new MaterialDialog.Builder(getActivity())
                .title("请选择")
                .items(R.array.gzchuli_kehu_dingze_tv_values)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        gzchuli_kehu_dingze_tv.setText(text);
                    }
                })
                .show();
    }


    //下拉选项为一个类别时  客户需分析报告
    private void showContextMenuDialoggzchuli_kehu_xu_fenxibaogao_tv() {
        new MaterialDialog.Builder(getActivity())
                .title("请选择")
                .items(R.array.gzchuli_kehu_xu_fenxibaogao_tv_values)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        gzchuli_kehu_xu_fenxibaogao_tv.setText(text);
                    }
                })
                .show();
    }


    //下拉选项为一个类别时  最终结果
    private void showContextMenuDialoggzchuli_zuizhongjieguo_tv() {
        new MaterialDialog.Builder(getActivity())
                .title("请选择")
                .items(R.array.lastre_values)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        gzchuli_zuizhongjieguo_tv.setText(text);
                    }
                })
                .show();
    }

    private void scaleImage(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            System.out.println("wid:" + width);
            System.out.println("hei:" + height);
            if (width > 800 || height > 800) {
                if (width > height) {
                    float scaleRate = (float) (800.0 / width);
                    width = 800;
                    height = (int) (height * scaleRate);
                    Bitmap map = Bitmap.createScaledBitmap(bitmap, width, height, true);
                    saveBitmap(map);
                } else {
                    float scaleRate = (float) (800.0 / height);
                    height = 800;
                    width = (int) (width * scaleRate);
                    Bitmap map = Bitmap.createScaledBitmap(bitmap, width, height, true);
                    saveBitmap(map);
                }
            } else {
                saveBitmap(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveBitmap(Bitmap map) {
        File file = new File(getActivity().getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
        try {

            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            map.compress(Bitmap.CompressFormat.JPEG, 90, bos);
            bos.flush();
            bos.close();

            System.out.println("路径:" + file.getAbsolutePath() + "--name:" + file.getName());
            String xpath = "/storage/emulated/0/" + "CRRC/" + "PDA_Picture/";
            boolean su = FileUtils.createOrExistsDir(xpath);
            //图片的路径变化，并改变图片的名称
            String newpath = xpath + "xxx.jpg";
            if (su) {
                BitmapUtils.renameFile(file.getAbsolutePath(), newpath);
            }


//            //路径转uri
//            Uri pathToUri =  BitmapUtils.getUri(getActivity(), xpath);
//            System.out.println("====="+pathToUri);
//            //通过uri得到图片bitmap
//            Bitmap bitmap = BitmapUtils.getBitmapFormUri(getActivity(), pathToUri);


            //把bitmap转为base64 便于上传
            String mapTobase64 = BitmapUtils.bitmapToBase64(map);

            //base64--->>>图片到本地
            File file2 = new File("/storage/emulated/0/CRRC/123.jpg");
            BufferedOutputStream bos2 = new BufferedOutputStream(new FileOutputStream(file2));
            boolean c = BitmapUtils.base64ToImageOutput(mapTobase64, bos2);
            System.out.println("base64--->>>图片到本地成功-----" + c);

            //            byte[] bt = BitmapUtils.getFileBytes(file.getAbsolutePath());
//            BitmapUtils.saveFile(bt,"/storage/emulated/0/","liweibo");
//            System.out.println("daxaio:" + file.length());
//            updateImg(file);//上传图片接口
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        unRegisterReceiver(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(getActivity());

    }


    //注册广播，是为了监听扫描结果
    public void registerReceiver(Context mContext) {
        IntentFilter intFilter = new IntentFilter("nlscan.action.SCANNER_RESULT");
        mContext.registerReceiver(mResultReceiver, intFilter);
    }

    public void unRegisterReceiver(Context mContext) {
        try {
            mContext.unregisterReceiver(mResultReceiver);
        } catch (Exception e) {
        }
    }

    //监听扫描结果的广播
    private BroadcastReceiver mResultReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("nlscan.action.SCANNER_RESULT".equals(action)) {
                String svalue1 = intent.getStringExtra("SCAN_BARCODE1");
                String svalue2 = intent.getStringExtra("SCAN_BARCODE2");
                int barcodeType = intent.getIntExtra("SCAN_BARCODE_TYPE", -1); // -1:unknown

                MyApplication.setScanStringtv1(svalue1);
                System.out.println("=====" + MyApplication.getScanStringtv1());
                svalue1 = svalue1 == null ? "" : svalue1;
                svalue2 = svalue2 == null ? "" : svalue2;

                if (publicTvScanValue != null) {
                    publicTvScanValue.setText(svalue1);

                }


                System.out.println("扫描的值1：" + svalue1);
                System.out.println("扫描的值2：" + svalue2);
                System.out.println("扫描de code：" + barcodeType);

                final String scanStatus = intent.getStringExtra("SCAN_STATE");
                if ("ok".equals(scanStatus)) {
                    //成功
                    XToastUtils.success("扫描成功");
                } else {
                    //失败如超时等
                    XToastUtils.error("扫描失败");
                }


            }
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

