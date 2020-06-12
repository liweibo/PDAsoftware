package com.crrc.pdasoftware.fragments.guzhangchuli;

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
import rxhttp.wrapper.param.RxHttp;

import static android.content.Context.MODE_PRIVATE;


public class DaodaxcFragment extends Fragment {
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    TextView gongdanchexing;
    TextView daoda_chehao_tv;
    TextView daoda_chexianghao_tv;
    TextView daoda_guzhangshebei_tv;
    TextView daoda_fashengjieduan_tv;
    TextView daoda_kehudingze_tv;
    TextView daoda_weather_tv;
    TextView daoda_lukuang_tv;
    TextView daoda_yunxingmodel_tv;
    SmoothCheckBox scheck_box_yiyue_fuwuxiangy;

    LinearLayout ll_daoda_fasheng_time;

    LinearLayout ll_gaipaiyuanyin_visi;
    LinearLayout ll_daoda_chexing;
    LinearLayout ll_daoda_chehao;

    LinearLayout ll_pg_guzhangshebei;
    LinearLayout ll_daoda_yunxingmodel;

    LinearLayout ll_daoda_guzhangchuli_address;//故障处理站点

    LinearLayout ll_daoda_chexianghao;//车厢号
    LinearLayout ll_daoda_gzcode;//故障代码
    LinearLayout ll_daoda_gzhouguo;//故障后果
    LinearLayout ll_daoda_fashengjieduan;//发生阶段
    LinearLayout ll_daoda_kehudingze;//客户定责
    LinearLayout ll_daoda_weather;//天气
    LinearLayout ll_daoda_lukuang;//路况


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


        gdbianhao = pref.getString("gzgongdanbianhao", "不存在");
        gdchexing = pref.getString("gzgongdanchexing", "不存在");
        gdchehao = pref.getString("gzgongdanchehao", "不存在");

        gongdanchexing.setText(gdchexing);
        daoda_chehao_tv.setText(gdchehao);


        daoda_guzhangchuli_address_tv.setFocusable(true);
        daoda_guzhangchuli_address_tv.setFocusableInTouchMode(true);
        daoda_guzhangchuli_address_tv.requestFocus();

        initExpandableListPopup();
        initExpandableListPopupYunxingModel();
        initExpandableListgzresultPopup();
    }

    public void setClick() {

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
        ll_daoda_chexing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFragment.showFragment(getActivity().getSupportFragmentManager(), SearchFragment.TAG);
            }
        });

//        ll_daoda_needdaodatime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                xuqiudaodatimeshowTimePickerDialog();
//            }
//        });


        //车号 搜索
        ll_daoda_chehao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFragmentchehao.showFragment(getActivity().getSupportFragmentManager(), SearchFragment.TAG);

            }
        });

        //下一步
        daoda_nextstep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FuwuxyTianxieActivity) getActivity()).gotoGuzhangChulifrgment();

            }
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
