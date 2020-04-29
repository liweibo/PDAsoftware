package com.crrc.pdasoftware.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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

import com.crrc.pdasoftware.FuwuxyTianxieActivity;
import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.activity.GuzhangPaigongActivity;
import com.crrc.pdasoftware.utils.ClearEditText;
import com.crrc.pdasoftware.utils.DemoDataProvider;
import com.crrc.pdasoftware.utils.XToastUtils;
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
import com.xuexiang.xui.widget.toast.XToast;
import com.xuexiang.xutil.data.DateUtils;
import com.xuexiang.xutil.display.DensityUtils;

import java.util.Calendar;
import java.util.Date;

import me.leefeng.promptlibrary.PromptDialog;

import static android.content.Context.MODE_PRIVATE;


public class GuzChuliFragment extends Fragment {
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

    TextView guzchuli_chulijieguo_tv;
    TextView guzchuli_chulicshi_tv;
    TextView guzchuli_guzyuanyin_tv;
    TextView guzchuli_guzxianx_tv;
    TextView guzchuli_xitonggnjian_tv;


    LinearLayout ll_guzchuli_guajingshipai;//挂警示牌
    LinearLayout ll_guzchuli_yijichanpingxuhao;//一级产品序号 扫码


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

    private XUISimpleExpandablePopup mExpandableListPopup;
    private XUISimpleExpandablePopup mExpandableListPopupYunxingModel;//运行模式


    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    boolean canClickbtns = false;
    SearchFragment searchFragment;
    SearchFragment searchFragmentchehao;

    private PromptDialog promptDialog;

    ImageView danger_file_see;

    boolean dangerFlag = true;//默认是危险作业（true），为空也看做是危险作业；false表示非危险作业

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
        TextView tvToolbarTitle = getActivity().findViewById(R.id.tv_gzgd_toolbbar_title);
        tvToolbarTitle.setText("故障工单-到达现场");
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


        guzchuli_chulijieguo_tv = v.findViewById(R.id.guzchuli_chulijieguo_tv);
        guzchuli_chulicshi_tv = v.findViewById(R.id.guzchuli_chulicshi_tv);
        guzchuli_guzyuanyin_tv = v.findViewById(R.id.guzchuli_guzyuanyin_tv);
        guzchuli_guzxianx_tv = v.findViewById(R.id.guzchuli_guzxianx_tv);
        guzchuli_xitonggnjian_tv = v.findViewById(R.id.guzchuli_xitonggnjian_tv);

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

        initExpandableListPopup();
        initExpandableListPopupYunxingModel();
    }

    public void setClick() {
        danger_file_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XToastUtils.success("危险源确认指导文件查看");
            }
        });

        //挂警示牌 拍照
        ll_guzchuli_guajingshipai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                XToastUtils.success("启动拍照");
            }
        });

        //一级产品序号
        ll_guzchuli_yijichanpingxuhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                XToastUtils.success("启动扫码");
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


    //下拉选项  有多个分类时
    private void initExpandableListPopup() {
        mExpandableListPopup = new XUISimpleExpandablePopup(getActivity(),
                DemoDataProvider.guzhangshebeiitems)
                .create(DensityUtils.dip2px(getActivity(), 200), DensityUtils.dip2px(
                        getActivity(), 200))
                .setOnExpandableItemClickListener(true, new XUISimpleExpandablePopup.OnExpandableItemClickListener() {
                    @Override
                    public void onExpandableItemClick(XUISimpleExpandableListAdapter adapter, ExpandableItem group, int groupPosition, int childPosition) {
                        guzchuli_guzhangshebei_tv.setText(group.getChildItem(childPosition).getTitle());
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

                        guzchuli_yunxingmodel_tv.setText(group.getChildItem(childPosition).getTitle());
                    }
                });
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
                            XToastUtils.success("挂起按钮激活或直接跳到挂起页面");
                        } else if (tvText.equals("换件处理")) {
                            XToastUtils.success("上部换件按钮激活");
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

    //下拉选项为一个类别时  天气
    private void showContextMenuDialogWeather() {
        new MaterialDialog.Builder(getActivity())
                .title("请选择")
                .items(R.array.weather_values)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        guzchuli_weather_tv.setText(text);
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
                        guzchuli_lukuang_tv.setText(text);
                    }
                })
                .show();
    }

}

