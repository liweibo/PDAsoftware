package com.crrc.pdasoftware.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    TextView guzchuli_kehudingze_tv;
    TextView guzchuli_weather_tv;
    TextView guzchuli_lukuang_tv;
    TextView guzchuli_yunxingmodel_tv;
    SmoothCheckBox scheck_box_yiyue_fuwuxiangy;

    LinearLayout ll_guzchuli_fasheng_time;

    LinearLayout ll_gaipaiyuanyin_visi;
    LinearLayout ll_guzchuli_chexing;
    LinearLayout ll_guzchuli_chehao;

    LinearLayout ll_pg_guzhangshebei;
    LinearLayout ll_guzchuli_yunxingmodel;

    LinearLayout ll_guzchuli_chexianghao;//车厢号
    LinearLayout ll_guzchuli_gzcode;//故障代码
    LinearLayout ll_guzchuli_gzhouguo;//故障后果
    LinearLayout ll_guzchuli_fashengjieduan;//发生阶段

    LinearLayout ll_gzchuli_danger;//危险源
    LinearLayout ll_down;//危险源下部分 ui


    LinearLayout ll_guzchuli_kehudingze;//客户定责
    LinearLayout ll_guzchuli_weather;//天气
    LinearLayout ll_guzchuli_lukuang;//路况


    TextView guzchuli_gzfasheng_time_tv;
    TextView guzchuli_teletime_tv;

    String gdbianhao;
    String gdchexing;
    String gdchehao;

    NestedScrollView nestSv_guzchuli;
    TextView guzchuli_gdbh_tv;


    ButtonView guzchuli_nextstep;
    ButtonView guzchuli_gaipaibtn;

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


        gongdanchexing = v.findViewById(R.id.guzchuli_chexing_tv);
        guzchuli_chehao_tv = v.findViewById(R.id.guzchuli_chehao_tv);
        guzchuli_gzfasheng_time_tv = v.findViewById(R.id.guzchuli_gzfasheng_time_tv);
        guzchuli_chexianghao_tv = v.findViewById(R.id.guzchuli_chexianghao_tv);
        guzchuli_guzhangshebei_tv = v.findViewById(R.id.guzchuli_guzhangshebei_tv);
        guzchuli_fashengjieduan_tv = v.findViewById(R.id.guzchuli_fashengjieduan_tv);

        gzchuli_danger_tv = v.findViewById(R.id.gzchuli_danger_tv);

        guzchuli_kehudingze_tv = v.findViewById(R.id.guzchuli_kehudingze_tv);
        guzchuli_weather_tv = v.findViewById(R.id.guzchuli_weather_tv);
        guzchuli_lukuang_tv = v.findViewById(R.id.guzchuli_lukuang_tv);
        guzchuli_yunxingmodel_tv = v.findViewById(R.id.guzchuli_yunxingmodel_tv);
        scheck_box_yiyue_fuwuxiangy = v.findViewById(R.id.scheck_box_yiyue_fuwuxiangy);
        ll_guzchuli_chexing = v.findViewById(R.id.ll_guzchuli_chexing);
        ll_guzchuli_chehao = v.findViewById(R.id.ll_guzchuli_chehao);
        ll_pg_guzhangshebei = v.findViewById(R.id.ll_pg_guzhangshebei);
        ll_guzchuli_yunxingmodel = v.findViewById(R.id.ll_guzchuli_yunxingmodel);
        ll_guzchuli_fashengjieduan = v.findViewById(R.id.ll_guzchuli_fashengjieduan);

        ll_gzchuli_danger = v.findViewById(R.id.ll_gzchuli_danger);

        ll_down = v.findViewById(R.id.ll_down);

        ll_guzchuli_kehudingze = v.findViewById(R.id.ll_guzchuli_kehudingze);
        ll_guzchuli_weather = v.findViewById(R.id.ll_guzchuli_weather);
        ll_guzchuli_lukuang = v.findViewById(R.id.ll_guzchuli_lukuang);
        ll_guzchuli_fasheng_time = v.findViewById(R.id.ll_guzchuli_fasheng_time);


        nestSv_guzchuli = v.findViewById(R.id.nestSv_guzchuli);
        guzchuli_gdbh_tv = v.findViewById(R.id.guzchuli_gdbh_tv);

        guzchuli_nextstep = v.findViewById(R.id.guzchuli_nextstep);

        ll_gaipaiyuanyin_visi = v.findViewById(R.id.ll_gaipaiyuanyin_visi);
        //车厢号
        ll_guzchuli_chexianghao = v.findViewById(R.id.ll_guzchuli_chexianghao);
        //故障代码
        ll_guzchuli_gzcode = v.findViewById(R.id.ll_guzchuli_gzcode);
        //故障后果
        ll_guzchuli_gzhouguo = v.findViewById(R.id.ll_guzchuli_gzhouguo);

        //危险指引文件查看
        danger_file_see = v.findViewById(R.id.danger_file_see);


        gdbianhao = pref.getString("gzgongdanbianhao", "不存在");
        gdchexing = pref.getString("gzgongdanchexing", "不存在");
        gdchehao = pref.getString("gzgongdanchehao", "不存在");

        gongdanchexing.setText(gdchexing);
        guzchuli_chehao_tv.setText(gdchehao);


        guzchuli_gdbh_tv.setFocusable(true);
        guzchuli_gdbh_tv.setFocusableInTouchMode(true);
        guzchuli_gdbh_tv.requestFocus();

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

        //车厢号
        ll_guzchuli_chexianghao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showSingleChoiceDialogCheXianghao();
                showContextMenuDialogChexianghao();
            }
        });
        //故障代码
        ll_guzchuli_gzcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XToastUtils.info("搜索..........");
            }
        });
        //故障后果
        ll_guzchuli_gzhouguo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XToastUtils.info("搜索..........");
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
        ll_guzchuli_yunxingmodel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandableListPopupYunxingModel.clearExpandStatus();
                mExpandableListPopupYunxingModel.showDown(v);
            }
        });
        //危险源
        ll_gzchuli_danger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContextMenuDialogDanger();
            }
        });


        //客户定责
        ll_guzchuli_kehudingze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContextMenuDialogkehudingze();
            }
        });
        //天气
        ll_guzchuli_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContextMenuDialogWeather();
            }
        });
        //路况
        ll_guzchuli_lukuang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContextMenuDialoglukuang();
            }
        });
        //故障发生时间
        ll_guzchuli_fasheng_time.setOnClickListener(new View.OnClickListener() {
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
        ll_guzchuli_chexing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFragment.showFragment(getActivity().getSupportFragmentManager(), SearchFragment.TAG);
            }
        });

//        ll_guzchuli_needguzchulitime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                xuqiuguzchulitimeshowTimePickerDialog();
//            }
//        });


        //车号 搜索
        ll_guzchuli_chehao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFragmentchehao.showFragment(getActivity().getSupportFragmentManager(), SearchFragment.TAG);

            }
        });

        //下一步
        guzchuli_nextstep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FuwuxyTianxieActivity) getActivity()).gotoGuzhangChulifrgment();

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

    //下拉选项为一个类别时  车厢号
    private void showContextMenuDialogChexianghao() {
        new MaterialDialog.Builder(getActivity())
                .title("请选择")
                .items(R.array.chexianghaoarry)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        guzchuli_chexianghao_tv.setText(text);
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

