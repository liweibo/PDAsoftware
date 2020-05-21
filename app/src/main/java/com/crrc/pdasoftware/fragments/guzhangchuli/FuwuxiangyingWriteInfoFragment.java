package com.crrc.pdasoftware.fragments.guzhangchuli;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.crrc.pdasoftware.activity.all.guzhangchuli.GuZhangActivity;
import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.utils.ClearEditText;
import com.crrc.pdasoftware.utils.XToastUtils;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.button.SmoothCheckBox;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xui.widget.picker.widget.configure.TimePickerType;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectChangeListener;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectListener;
import com.xuexiang.xutil.data.DateUtils;

import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;


public class FuwuxiangyingWriteInfoFragment extends Fragment {
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    ClearEditText gongdanbh;
    ClearEditText gongdanchexing;
    ClearEditText gongdanchehao;
    SmoothCheckBox scheck_box_yiyue_fuwuxiangy;

    LinearLayout ll_xy_isornogaipai;
    LinearLayout ll_xy_needdaodatime;
    LinearLayout ll_xy_tele_time;
    LinearLayout ll_gaipaiyuanyin_visi;

    TextView xy_shifougaipai_tv;
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
        View v = inflater.inflate(R.layout.fragment_fuwuxiangying_write_info,
                container, false);
        initViews(v);
        setClick();
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
        scheck_box_yiyue_fuwuxiangy = v.findViewById(R.id.scheck_box_yiyue_fuwuxiangy);
        ll_xy_isornogaipai = v.findViewById(R.id.ll_xy_isornogaipai);
        ll_xy_needdaodatime = v.findViewById(R.id.ll_xy_needdaodatime);
        ll_xy_tele_time = v.findViewById(R.id.ll_xy_tele_time);

        xy_shifougaipai_tv = v.findViewById(R.id.xy_shifougaipai_tv);
        xy_xuqiudaodatime_tv = v.findViewById(R.id.xy_xuqiudaodatime_tv);
        xy_teletime_tv = v.findViewById(R.id.xy_teletime_tv);

        nestSv_xy = v.findViewById(R.id.nestSv_xy);
        xy_gdbh_tv = v.findViewById(R.id.xy_gdbh_tv);

        xy_nextstep_fuxy = v.findViewById(R.id.xy_nextstep_fuxy);
        xy_gaipaibtn = v.findViewById(R.id.xy_gaipaibtn);

        ll_gaipaiyuanyin_visi = v.findViewById(R.id.ll_gaipaiyuanyin_visi);


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

        ll_xy_needdaodatime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuqiudaodatimeshowTimePickerDialog();
            }
        });

        ll_xy_tele_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telephonetimeshowTimePickerDialog();
            }
        });

        //下一步
        xy_nextstep_fuxy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canClickbtns) {
                    XToastUtils.error("请阅读管理要求");
                } else {
                    ((FuwuxyTianxieActivity) getActivity()).gotoDaodaxiancfrgment();

                }
            }
        });

        //改派 转办
        xy_gaipaibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!canClickbtns) {
//                    XToastUtils.error("请阅读管理要求");
//                } else {
//
//                    addFragmentdaodaxcWriteinfo();
//                }
                getActivity().startActivity(new Intent(((FuwuxyTianxieActivity) getActivity()),
                        GuZhangActivity.class));
            }
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
                .items(R.array.isornot_values)
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
