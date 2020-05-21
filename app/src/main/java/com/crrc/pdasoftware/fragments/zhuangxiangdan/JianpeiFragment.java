package com.crrc.pdasoftware.fragments.zhuangxiangdan;

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
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.activity.all.guzhangchuli.GuZhangActivity;
import com.crrc.pdasoftware.activity.all.zhuangxiangdan.ZhuangXDanLiuchengActivity;
import com.crrc.pdasoftware.adapter.zhuangxiangdan.JianpeiWuliaoAdapter;
import com.crrc.pdasoftware.fragments.guzhangchuli.DaodaxcFragment;
import com.crrc.pdasoftware.utils.XToastUtils;
import com.crrc.pdasoftware.utils.guzhanggddata.FuwuDataProvider;
import com.crrc.pdasoftware.utils.zhuangxiangdandata.JianpeiRecyItemDataInfo;
import com.crrc.pdasoftware.utils.zhuangxiangdandata.JianpeiRecyItemDataProvider;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.adapter.recyclerview.XLinearLayoutManager;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xui.widget.picker.widget.configure.TimePickerType;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectChangeListener;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectListener;
import com.xuexiang.xutil.data.DateUtils;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.io.LineNumberReader;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;


public class JianpeiFragment extends Fragment {
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    ButtonView jianpei_nextstep_fujianpei;
    ButtonView jianpei_gaipaibtn;

    private TimePickerView mTimePickerDialogxuqiudaoda;
    private TimePickerView mTimePickerDialogtelephone;

    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    LinearLayout ll_jianpei_item_wuliaocode;

    boolean canClickbtns = false;

    SwipeRecyclerView recyclerView;
    JianpeiWuliaoAdapter mAdapter;

    public JianpeiFragment() {
    }

    //物料扫码：
//    1.行记录中，有多个物料（按批次号）；2.行记录中，一行对应一个物料（按序列号）
//    1 中情况扫一次批次号，再手动输入总物料数；2中情况需每个都扫描。
//    不管是1还是2，只要是一个类别的物料扫描完成了，就点击完成，提交扫描数据。
//    提交后，再扫描其他的物料类别，按1 2中方式进行
//
//


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        pref = getActivity().getSharedPreferences("guzhanggddata", MODE_PRIVATE);
        editor = pref.edit();
        View v = inflater.inflate(R.layout.fragment_jianpei,
                container, false);
        initViews(v);
        setClick();
        return v;
    }

    public void initViews(View v) {
        TextView tvToolbarTitle = getActivity().findViewById(R.id.tv_zhuangxd_liucheng_toolbbar_title);
        tvToolbarTitle.setText("装箱单-拣配");


        fragmentManager = getActivity().getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        jianpei_nextstep_fujianpei = v.findViewById(R.id.jianpei_nextstep_fujianpei);
        jianpei_gaipaibtn = v.findViewById(R.id.jianpei_gaipaibtn);
        ll_jianpei_item_wuliaocode = v.findViewById(R.id.ll_jianpei_item_wuliaocode);


        jianpei_nextstep_fujianpei.setVisibility(View.VISIBLE);
        jianpei_gaipaibtn.setVisibility(View.GONE);

        recyclerView = v.findViewById(R.id.recyclerView_jianpei_wuliao);
        recyclerView.setLayoutManager(new XLinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter = new JianpeiWuliaoAdapter());

        mAdapter.setOnItemClickListener(new RecyclerViewHolder.OnItemClickListener<JianpeiRecyItemDataInfo>() {
            @Override
            public void onItemClick(View itemView, JianpeiRecyItemDataInfo item, int position) {
                LinearLayout llWuliaoCode = itemView.findViewById(R.id.ll_jianpei_item_wuliaocode);
                LinearLayout llWuliaoPicihao = itemView.findViewById(R.id.ll_jianpei_item_picihao);
                LinearLayout llWuliaoXuliehao = itemView.findViewById(R.id.ll_jianpei_item_xuliehao);
                if (llWuliaoCode!=null){
                    XToastUtils.success("wuliao_code");

                }
            }
        });
        mAdapter.refresh(JianpeiRecyItemDataProvider.getJianpeiListNewInfos());
    }


    public void setClick() {

        //物料编码 扫描
//        ll_jianpei_item_wuliaocode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("88888888888888");
//            }
//        });


        //下一步
        jianpei_nextstep_fujianpei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //改派 转办
        jianpei_gaipaibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().startActivity(new Intent(((ZhuangXDanLiuchengActivity) getActivity()),
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
