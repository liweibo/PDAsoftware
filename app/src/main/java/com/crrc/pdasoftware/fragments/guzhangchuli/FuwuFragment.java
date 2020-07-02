package com.crrc.pdasoftware.fragments.guzhangchuli;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.crrc.pdasoftware.MyApplication;
import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.activity.all.guzhangchuli.FuwuxyTianxieActivity;
import com.crrc.pdasoftware.activity.all.guzhangchuli.GuZhangActivity;
import com.crrc.pdasoftware.activity.all.guzhangchuli.GuzhangPaigongActivity;
import com.crrc.pdasoftware.adapter.guzhanggongdanadapter.FuwuXiangyingAdapter;
import com.crrc.pdasoftware.fragments.dummy.DummyContent.DummyItem;
import com.crrc.pdasoftware.net.Constant;
import com.crrc.pdasoftware.net.pojo.GuzhangListOfFWXYPojo;
import com.crrc.pdasoftware.net.pojo.GuzhangListOfFWXYTwoPojo;
import com.crrc.pdasoftware.utils.FiledDataSave;
import com.crrc.pdasoftware.utils.guzhanggddata.FuwuDataInfo;
import com.crrc.pdasoftware.utils.guzhanggddata.FuwuDataProvider;
import com.crrc.pdasoftware.utils.guzhanggddata.FuwuDataTwoProvider;
import com.crrc.pdasoftware.widget.MaterialLoadMoreView;
import com.rxjava.rxlife.RxLife;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.adapter.recyclerview.XLinearLayoutManager;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import me.leefeng.promptlibrary.PromptDialog;
import rxhttp.wrapper.param.RxHttp;

import static android.content.Context.MODE_PRIVATE;


public class FuwuFragment extends Fragment {
    MyApplication apps;
    private OnListFragmentInteractionListener mListener;

    SwipeRecyclerView recyclerView;

    SwipeRefreshLayout swipeRefreshLayout;
    private FuwuXiangyingAdapter mAdapter;
    private Handler mHandler = new Handler();
    private boolean mEnableLoadMore;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private PromptDialog promptDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        promptDialog = new PromptDialog(getActivity());
        apps = (MyApplication) getActivity().getApplication();


        pref = getActivity().getSharedPreferences("guzhanggddata", MODE_PRIVATE);
        editor = pref.edit();

        View view = inflater.inflate(R.layout.fragment_fuwu_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_fuwu);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        recyclerView.setLayoutManager(new XLinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter = new FuwuXiangyingAdapter());
        swipeRefreshLayout.setColorSchemeColors(0xff0099cc, 0xffff4444, 0xff669900, 0xffaa66cc, 0xffff8800);
        initListen();
        return view;
    }

    private void initListen() {


        // 刷新监听。
        swipeRefreshLayout.setOnRefreshListener(mRefreshListener);
        refresh();

        //item点击监听

        mAdapter.setOnItemClickListener(new RecyclerViewHolder.OnItemClickListener<FuwuDataInfo>() {
            @Override
            public void onItemClick(View itemView, FuwuDataInfo item, int position) {

                Constant.uniqueId = item.getWorkorderid();//拿到唯一id  工作流需用到
                Constant.extraBiaoGdbh = "'" + item.getGdbh() + "'";

                FiledDataSave.UNIQUEID = item.getWorkorderid();
                Constant.statusinfo = item.getStatus();
                apps.setItemwhichPosition(position);
                System.out.println("--uniqueId----:" + Constant.uniqueId +
                        "===:" + Constant.extraBiaoGdbh);

                promptDialog.showLoading("加载中...", false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("-------------2-------");
                        promptDialog.dismissImmediately();
                        if (item.getStatus().equals("草稿")) {//进入派工界面
                            Intent intent = new Intent(getActivity(), GuzhangPaigongActivity.class);
                            intent.putExtra("gdbh", item.getGdbh());
                            intent.putExtra("peishuser", item.getPeishuyh());
                            intent.putExtra("chexing", item.getChexingvalue());
                            intent.putExtra("chehao", item.getChehaoValue());
                            intent.putExtra("fwdanweiaddress", item.getFwdanweiaddress());
                            intent.putExtra("kehuteletime", item.getKehuCALLTIME());
                            intent.putExtra("fuwudanweicontactor", item.getFuwudanweiContact());
                            intent.putExtra("xianchanghandler", item.getXianchangchulirenname());
                            intent.putExtra("xianchanghandleriphone", item.getXianchangchulirenphone());
                            intent.putExtra("paigongliyou", item.getPaigongliyou());
                            intent.putExtra("anquanguanliyaoqiu", item.getSecureGuanli());
                            startActivity(intent);

                        } else {
                            Intent intent = new Intent(getActivity(), FuwuxyTianxieActivity.class);
                            intent.putExtra("whichPosition", position);
                            startActivity(intent);
                        }

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
        //请求数据之前先清空
        FuwuDataProvider.clearFwxyListdata();
        FuwuDataTwoProvider.clearFwxyListdata();
        //请求数据
        request();
        mAdapter.refresh(FuwuDataProvider.getFwxyListdata());
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
//        enableLoadMore();

    }


    public List<Map> request() {
        List<Map> lstMap = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        RxHttp.postForm(Constant.usualInterfaceAddr)
                .add(Constant.usualKey, Constant.getFwxyreq())
                .asClass(GuzhangListOfFWXYPojo.class).
                observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    //请求开始，当前在主线程回调

                    promptDialog.showLoading("加载中...");

                })
                .doFinally(() -> {
                    //请求结束，当前在主线程回调
//                    mAdapter.refresh(FuwuDataProvider.getFwxyListdata());
                    request2();
//                    promptDialog.dismissImmediately();

                }).as(RxLife.as(this))  //感知生命周期 当退出页面时 请求未完成，则关闭请求，防止内存泄漏
                .subscribe(clz -> {//clz就是Pojo
                    List<GuzhangListOfFWXYPojo.DataBeanX.DataBean> datalevel = clz.data.data;

                    if (datalevel.size() > 0) {
                        for (int i = 0; i < datalevel.size(); i++) {//行数据个数
                            List<GuzhangListOfFWXYPojo.DataBeanX.DataBean.ValuemapBean> rowdatalevel
                                    = datalevel.get(i).valuemap;
                            String ordernum = "", status = "", chexing = "", chehao = "",
                                    peishuyh = "", kehuCALLTIME = "", paigongliyou = "",
                                    fuwudanweiContact = "", secureGuanli = "",
                                    fuwudanweiContPhone = "", leijizouxing = "", workorderid = "";
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

                                }


                            }
                            FuwuDataProvider.addOneFwxyListdata(ordernum, status, chexing, chehao,
                                    peishuyh,
                                    kehuCALLTIME,
                                    paigongliyou,
                                    fuwudanweiContact,
                                    secureGuanli,
                                    leijizouxing,
                                    fuwudanweiContPhone, workorderid);
                        }
                    }


                }, throwable ->

                {
                    //失败回调
                    System.out.println("失败结果：" + throwable.getMessage());
                    map.put("fail", throwable.getMessage());
                    lstMap.add(map);
                });


        return lstMap;
    }


    //请求草稿状态的数据
    public void request2() {

        RxHttp.postForm(Constant.usualInterfaceAddr)
                .add(Constant.usualKey, Constant.getFwxyreq2())
                .asClass(GuzhangListOfFWXYTwoPojo.class).
                observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    //请求开始，当前在主线程回调

//                    promptDialog.showLoading("加载中...");

                })
                .doFinally(() -> {
                    //请求结束，当前在主线程回调
                    List<FuwuDataInfo> ls = FuwuDataProvider.getFwxyListdata();
                    for (int i = 0; i < ls.size(); i++) {
                        System.out.println("第一次请求的数据：" + ls.get(i).getGdbh());
                        FuwuDataTwoProvider.getFwxyListdata().add(ls.get(i));
                    }
                    ((GuZhangActivity) getActivity()).bvFuwu.setBadgeNumber(
                            FuwuDataTwoProvider.getFwxyListdata().size()
                    );
                    mAdapter.refresh(FuwuDataTwoProvider.getFwxyListdata());

                    promptDialog.dismissImmediately();

                }).as(RxLife.as(this))  //感知生命周期 当退出页面时 请求未完成，则关闭请求，防止内存泄漏
                .subscribe(clz -> {//clz就是Pojo
                    System.out.println("========" + clz.data.data.get(0).valuemap.get(0).description);
                    List<GuzhangListOfFWXYTwoPojo.DataBeanX.DataBean> datalevel = clz.data.data;

                    if (datalevel.size() > 0) {
                        for (int i = 0; i < datalevel.size(); i++) {//行数据个数
                            List<GuzhangListOfFWXYTwoPojo.DataBeanX.DataBean.ValuemapBean> rowdatalevel
                                    = datalevel.get(i).valuemap;
                            String ordernum = "", status = "", chexing = "", chehao = "",
                                    peishuyh = "", kehuCALLTIME = "", paigongliyou = "",
                                    fuwudanweiContact = "", secureGuanli = "",
                                    fuwudanweiContPhone = "", leijizouxing = "", workorderid = "";
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

                                }


                            }
                            FuwuDataTwoProvider.addOneFwxyListdata(ordernum, status, chexing, chehao,
                                    peishuyh,
                                    kehuCALLTIME,
                                    paigongliyou,
                                    fuwudanweiContact,
                                    secureGuanli,
                                    leijizouxing,
                                    fuwudanweiContPhone, workorderid);
                        }
                    }


                }, throwable -> {
                    //失败回调
                    System.out.println("失败结果：" + throwable.getMessage());

                });

    }


    public static void saveFile(String str, String fileName) {
        // 创建String对象保存文件名路径
        try {
            // 创建指定路径的文件 fileName取名可为fileName.txt
            File file = new File(Environment.getExternalStorageDirectory(), fileName);
            // 如果文件不存在
            if (file.exists()) {
                // 创建新的空文件
                file.delete();
            }
            file.createNewFile();
            // 获取文件的输出流对象
            FileOutputStream outStream = new FileOutputStream(file);
            // 获取字符串对象的byte数组并写入文件流
            outStream.write(str.getBytes());
            // 最后关闭文件输出流
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                    mAdapter.loadMore(FuwuDataProvider.getFwxyListdata());
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
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
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
