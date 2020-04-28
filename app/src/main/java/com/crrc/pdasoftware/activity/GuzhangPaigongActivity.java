package com.crrc.pdasoftware.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.crrc.pdasoftware.GuZhangActivity;
import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.utils.ClearEditText;
import com.crrc.pdasoftware.utils.DemoDataProvider;
import com.crrc.pdasoftware.utils.XToastUtils;
import com.xuexiang.xui.adapter.simple.ExpandableItem;
import com.xuexiang.xui.adapter.simple.XUISimpleExpandableListAdapter;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xui.widget.picker.widget.configure.TimePickerType;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectChangeListener;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectListener;
import com.xuexiang.xui.widget.popupwindow.popup.XUISimpleExpandablePopup;
import com.xuexiang.xui.widget.progress.loading.LoadingViewLayout;
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView;
import com.xuexiang.xutil.data.DateUtils;
import com.xuexiang.xutil.display.DensityUtils;

import java.util.Calendar;
import java.util.Date;

import io.reactivex.functions.Consumer;
import me.leefeng.promptlibrary.PromptDialog;

public class GuzhangPaigongActivity extends AppCompatActivity {
    Toolbar toolbar;
    SuperTextView xuqiudaodatime;
    SuperTextView telephonetime;
    SuperTextView guzhangresult;

    LinearLayout ll_pg_guzhangresult;
    LinearLayout ll_pg_needdaodatime;
    LinearLayout ll_pg_tele_time;

    TextView pg_xuqiudaodatime_tv;
    TextView pg_teletime_tv;
    TextView pg_guzhangresult_tv;

    ClearEditText pg_gongdanbh;

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

    }


    private void initViews() {

        promptDialog = new PromptDialog(this);


        toolbar = findViewById(R.id.toolbar_gz_paigong);

        ll_pg_guzhangresult = findViewById(R.id.ll_pg_guzhangresult);
        ll_pg_needdaodatime = findViewById(R.id.ll_pg_needdaodatime);
        ll_pg_tele_time = findViewById(R.id.ll_pg_tele_time);

        pg_gongdanbh = findViewById(R.id.pg_gongdanbh);
        nestSv = findViewById(R.id.nestSv);

        pg_xuqiudaodatime_tv = findViewById(R.id.pg_xuqiudaodatime_tv);
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
        ll_pg_needdaodatime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuqiudaodatimeshowTimePickerDialog();
            }
        });

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


    private void xuqiudaodatimeshowTimePickerDialog() {
        if (mTimePickerDialogxuqiudaoda == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.string2Date("2013-07-08 12:32:46", DateUtils.yyyyMMddHHmmss.get()));
            mTimePickerDialogxuqiudaoda = new TimePickerBuilder(GuzhangPaigongActivity.this, new OnTimeSelectListener() {
                @Override
                public void onTimeSelected(Date date, View v) {
                    XToastUtils.toast(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get()));
                    pg_xuqiudaodatime_tv.setText(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get()));

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
}
