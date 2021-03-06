package com.crrc.pdasoftware.fragments.guzhangchuli;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.activity.all.guzhangchuli.FuwuxyTianxieActivity;
import com.crrc.pdasoftware.activity.all.guzhangchuli.GuZhangActivity;
import com.crrc.pdasoftware.activity.all.guzhangchuli.GuzhangPaigongActivity;
import com.crrc.pdasoftware.activity.all.guzhangchuli.WeiwcActivity;
import com.crrc.pdasoftware.adapter.guzhanggongdanadapter.WeiwancGongdanAdapter;
import com.crrc.pdasoftware.fragments.dummy.DummyContent.DummyItem;
import com.crrc.pdasoftware.net.Constant;
import com.crrc.pdasoftware.net.pojo.GuzhangListOfFWXYPojo;
import com.crrc.pdasoftware.net.pojo.GuzhangListOfWeiwcPojo;
import com.crrc.pdasoftware.utils.FiledDataSave;
import com.crrc.pdasoftware.utils.guzhanggddata.FuwuDataInfo;
import com.crrc.pdasoftware.utils.guzhanggddata.FuwuDataProvider;
import com.crrc.pdasoftware.utils.guzhanggddata.FuwuDataTwoProvider;
import com.crrc.pdasoftware.utils.guzhanggddata.WeiwancDataInfo;
import com.crrc.pdasoftware.utils.guzhanggddata.WeiwanchengDataInfo;
import com.crrc.pdasoftware.utils.guzhanggddata.WeiwcDataProvider;
import com.crrc.pdasoftware.widget.MaterialLoadMoreView;
import com.rxjava.rxlife.RxLife;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.adapter.recyclerview.XLinearLayoutManager;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import me.leefeng.promptlibrary.PromptDialog;
import rxhttp.wrapper.param.RxHttp;


public class WeiwanchengFragment extends Fragment {
    private PromptDialog promptDialog;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private FuwuFragment.OnListFragmentInteractionListener mListener;

    SwipeRecyclerView recyclerView;

    SwipeRefreshLayout swipeRefreshLayout;
    private WeiwancGongdanAdapter mAdapter;
    private Handler mHandler = new Handler();
    private boolean mEnableLoadMore;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weiwancheng_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_guzhang_weiwanc);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout_guzhang_weiwanc);
        promptDialog = new PromptDialog(getActivity());

        recyclerView.setLayoutManager(new XLinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter = new WeiwancGongdanAdapter());
        swipeRefreshLayout.setColorSchemeColors(0xff0099cc, 0xffff4444, 0xff669900, 0xffaa66cc, 0xffff8800);
        initListen();
        return view;
    }

    private void initListen() {
        // 刷新监听。
        swipeRefreshLayout.setOnRefreshListener(mRefreshListener);
        refresh();
        //item点击监听
        mAdapter.setOnItemClickListener(new RecyclerViewHolder.OnItemClickListener<WeiwancDataInfo>() {
            @Override//WeiwancDataInfo是卡片列表，  WeiwanchengDataInfo是点击卡片进入后的列表
            public void onItemClick(View itemView, WeiwancDataInfo item, int position) {

                Constant.uniqueId = item.getWorkorderid();//拿到唯一id  工作流需用到
                Constant.extraBiaoGdbh = "'" + item.getGdbh() + "'";
                FiledDataSave.whichPos = position;//记录点击位置

                promptDialog.showLoading("加载中...", false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        promptDialog.dismissImmediately();
                        Intent intent = new Intent(getActivity(), WeiwcActivity.class);
                        startActivity(intent);
                    }
                }, 500);

            }
        });


    }


    /**
     * 刷新。
     */
    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            loadData();
        }
    };

    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        loadData();
    }

    private void loadData() {

        WeiwcDataProvider.clearWeiwcListdata();
        //请求数据
        request();

        mAdapter.refresh(WeiwcDataProvider.getWeiwcListdata());
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
//        enableLoadMore();

    }

    public List<Map> request() {
        List<Map> lstMap = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        RxHttp.postForm(Constant.usualInterfaceAddr)
                .add(Constant.usualKey, Constant.getWeiwc())
                .asClass(GuzhangListOfWeiwcPojo.class).
                observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    //请求开始，当前在主线程回调

                    promptDialog.showLoading("加载中...");

                })
                .doFinally(() -> {
                    //请求结束，当前在主线程回调
                    mAdapter.refresh(WeiwcDataProvider.getWeiwcListdata());
                    ((GuZhangActivity) getActivity()).bvWeiwanc.setBadgeNumber(
                            WeiwcDataProvider.getWeiwcListdata().size()
                    );
                    promptDialog.dismissImmediately();

                }).as(RxLife.as(this))  //感知生命周期 当退出页面时 请求未完成，则关闭请求，防止内存泄漏
                .subscribe(clz -> {//clz就是Pojo
                    List<GuzhangListOfWeiwcPojo.DataBeanX.DataBean> datalevel = clz.data.data;

                    if (datalevel.size() > 0) {
                        for (int i = 0; i < datalevel.size(); i++) {//行数据个数
                            List<GuzhangListOfWeiwcPojo.DataBeanX.DataBean.ValuemapBean> rowdatalevel
                                    = datalevel.get(i).valuemap;
                            String ordernum = "", status = "", chexing = "", chehao = "",
                                    peishuyh = "", kehuCALLTIME = "", paigongliyou = "",
                                    fuwudanweiContact = "", secureGuanli = "",
                                    fuwudanweiContPhone = "", leijizouxing = "",
                                    statusva = "",
                                    workorderid = "";
                            for (int j = 0; j < rowdatalevel.size(); j++) {
                                String key = rowdatalevel.get(j).attribute;
                                String va = rowdatalevel.get(j).value;

                                if (key.equals("ORDERNUM")) {
                                    ordernum = va;

                                } else if (key.equals("STATUS")) {
                                    status = va;

                                } else if (key.equals("MODELPROJECT")) {
                                    chexing = va;

                                } else if (key.equals("CARNUM")) {
                                    chehao = va;

                                } else if (key.equals("OWNERCUSTOMER")) {
                                    //配属用户
                                    peishuyh = va;
                                } else if (key.equals("CALLTIME")) {
                                    //客户来电时间
                                    kehuCALLTIME = va;
                                } else if (key.equals("DISPATCHREASON")) {
                                    //派工理由即故障描述
                                    paigongliyou = va;
                                } else if (key.equals("SERVCOMCONTACTOR")) {
                                    //服务单位联系人
                                    fuwudanweiContact = va;
                                } else if (key.equals("MANAGEMENTREQ")) {
                                    //安全管理要求
                                    secureGuanli = va;
                                } else if (key.equals("BACKTEL")) {
                                    //服务单位联系人
                                    fuwudanweiContPhone = va;
                                } else if (key.equals("RUNKILOMETRE")) {
                                    //-------------到达现场------------------
                                    //累计走行公里
                                    leijizouxing = va;
                                } else if (key.equals("WORKORDERID")) {
                                    workorderid = va;

                                } else if (key.equals("STATUS")) {
                                    statusva = va;

                                }


                            }
                            WeiwcDataProvider.addOneWeiwcListdata(ordernum,
                                    status, chexing, chehao,
                                    peishuyh,
                                    kehuCALLTIME,
                                    paigongliyou,
                                    fuwudanweiContact,
                                    secureGuanli,
                                    leijizouxing,
                                    fuwudanweiContPhone,
                                    workorderid,statusva);
                        }
                    }


                }, throwable -> {
                    //失败回调
                    System.out.println("失败结果：" + throwable.getMessage());
                    map.put("fail", throwable.getMessage());
                    lstMap.add(map);
                });


        return lstMap;
    }

    /**
     * 确保有数据加载了才开启加载更多
     */
    private void enableLoadMore() {
        if (recyclerView != null && !mEnableLoadMore) {
            mEnableLoadMore = true;
            useMaterialLoadMore();
            // 加载更多的监听。
            recyclerView.setLoadMoreListener(mLoadMoreListener);
            recyclerView.loadMoreFinish(false, true);
        }
    }

    private void useMaterialLoadMore() {
        MaterialLoadMoreView loadMoreView = new MaterialLoadMoreView(getContext());
        recyclerView.addFooterView(loadMoreView);
        recyclerView.setLoadMoreView(loadMoreView);
    }

    /**
     * 加载更多。
     */
    private SwipeRecyclerView.LoadMoreListener mLoadMoreListener = new SwipeRecyclerView.LoadMoreListener() {
        @Override
        public void onLoadMore() {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mAdapter.loadMore(WeiwcDataProvider.getWeiwcListdata());
                    if (recyclerView != null) {
                        recyclerView.loadMoreFinish(false, true);
                    }
                }
            }, 1000);
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FuwuFragment.OnListFragmentInteractionListener) {
            mListener = (FuwuFragment.OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }

    @Override
    public void onDestroyView() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }
}


