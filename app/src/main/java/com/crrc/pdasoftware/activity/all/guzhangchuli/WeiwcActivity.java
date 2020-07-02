package com.crrc.pdasoftware.activity.all.guzhangchuli;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.crrc.pdasoftware.R;
import com.crrc.pdasoftware.adapter.guzhanggongdanadapter.WeiwancAdapter;
import com.crrc.pdasoftware.adapter.guzhanggongdanadapter.ywancAdapter;
import com.crrc.pdasoftware.net.Constant;
import com.crrc.pdasoftware.net.pojo.GuzhangExtraFieldGet;
import com.crrc.pdasoftware.utils.FiledDataSave;
import com.crrc.pdasoftware.utils.guzhanggddata.FuwuDataInfo;
import com.crrc.pdasoftware.utils.guzhanggddata.FuwuDataTwoProvider;
import com.crrc.pdasoftware.utils.guzhanggddata.WeiwancDataInfo;
import com.crrc.pdasoftware.utils.guzhanggddata.WeiwanchengDataInfo;
import com.crrc.pdasoftware.utils.guzhanggddata.WeiwanchengDataProvider;
import com.crrc.pdasoftware.utils.guzhanggddata.WeiwcDataProvider;
import com.crrc.pdasoftware.utils.guzhanggddata.YiwancDataInfo;
import com.crrc.pdasoftware.utils.guzhanggddata.YiwancDataProvider;
import com.crrc.pdasoftware.utils.guzhanggddata.YiwcDataProvider;
import com.crrc.pdasoftware.utils.guzhanggddata.ywcTwoparaDataProvider;
import com.rxjava.rxlife.RxLife;
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder;
import com.xuexiang.xui.adapter.recyclerview.XLinearLayoutManager;
import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import me.leefeng.promptlibrary.PromptDialog;
import rxhttp.wrapper.param.RxHttp;

public class WeiwcActivity extends AppCompatActivity {

    private SwipeRecyclerView recyclerView;
    private WeiwancAdapter mAdapter;
    private ywancAdapter mAdapterywc;
    Toolbar toolbar_weiwancheng;
    private PromptDialog promptDialog;
    Button tongguo_shenhe_btn;
    Button bohui_shenhe_btn;
    LinearLayout ll_shenhe_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weiwc);
        recyclerView = findViewById(R.id.recyclerVie_weiwc);
        promptDialog = new PromptDialog(this);

        recyclerView.setLayoutManager(new XLinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        initViews();
        if (FiledDataSave.whichTab == 1) {//点击的是未完成
            ll_shenhe_btn.setVisibility(View.VISIBLE);
            WeiwancDataInfo lsdata = WeiwcDataProvider.getWeiwcListdata().get(FiledDataSave.whichPos);
            weiwanchengData(lsdata);

            recyclerView.setAdapter(mAdapter = new WeiwancAdapter());
            mAdapter.refresh(WeiwanchengDataProvider.getWeiwcListdata());

            request2();


        } else if (FiledDataSave.whichTab == 2) {//点击的是已完成
            ll_shenhe_btn.setVisibility(View.GONE);
            YiwancDataInfo lsdata = YiwcDataProvider.getYiwcListdata().get(FiledDataSave.whichPos);
            yiwanchengData(lsdata);

            recyclerView.setAdapter(mAdapterywc = new ywancAdapter());
            mAdapterywc.refresh(ywcTwoparaDataProvider.getyiwcListdata());

            request3();
        }


        clickEvent();

        System.out.println("===oncreate======");


    }


    @Override
    protected void onResume() {
        System.out.println("===resueme");
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WeiwanchengDataProvider.clearWeiwcListdata();
        ywcTwoparaDataProvider.clearyiwcListdata();

        System.out.println("=====destroy===");
    }

    private void initViews() {
        toolbar_weiwancheng = findViewById(R.id.toolbar_weiwancheng);
        tongguo_shenhe_btn = findViewById(R.id.tongguo_shenhe_btn);
        bohui_shenhe_btn = findViewById(R.id.bohui_shenhe_btn);
        ll_shenhe_btn = findViewById(R.id.ll_shenhe_btn);
    }

    private void clickEvent() {
        toolbar_weiwancheng.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tongguo_shenhe_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("0000000000000000");
            }
        });

        bohui_shenhe_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public void weiwanchengDataFromExtraBiao() {
        String gzfashentime = FiledDataSave.FAULTTIME,//FAULTTIME
                gzshebei = FiledDataSave.PRODUCTNICKNAME,//PRODUCTNICKNAME
                guzhangcode = FiledDataSave.FAILURECODE,//FAILURECODE
                guzhangname = FiledDataSave.FAILUREDESC,//FAILUREDESC
                kehudingze = FiledDataSave.FAULTQUALIT,//FAULTQUALIT
                guzhangchuliaddress = FiledDataSave.WHICHSTATION,//WHICHSTATION
                guzhanghouguo = FiledDataSave.FAULTCONSEQ,//FAULTCONSEQ
                fasgnegjieduan = FiledDataSave.FINDPROCESS;//FINDPROCESS

        WeiwanchengDataProvider.addOneweiwcListdata("客户定责", kehudingze != null ? kehudingze : "--");
        WeiwanchengDataProvider.addOneweiwcListdata("故障发生时间", gzfashentime != null ? gzfashentime : "--");
        WeiwanchengDataProvider.addOneweiwcListdata("故障设备", gzshebei != null ? gzshebei : "--");
        WeiwanchengDataProvider.addOneweiwcListdata("故障代码", guzhangcode != null ? guzhangcode : "--");
        WeiwanchengDataProvider.addOneweiwcListdata("故障名称", guzhangname != null ? guzhangname : "--");
        WeiwanchengDataProvider.addOneweiwcListdata("故障处理站点", guzhangchuliaddress != null ? guzhangchuliaddress : "--");
        WeiwanchengDataProvider.addOneweiwcListdata("故障后果", guzhanghouguo != null ? guzhanghouguo : "--");
        WeiwanchengDataProvider.addOneweiwcListdata("发生阶段", fasgnegjieduan != null ? fasgnegjieduan : "--");

    }

    public void yiwanchengDataFromExtraBiao() {
        String gzfashentime = FiledDataSave.FAULTTIME,//FAULTTIME
                gzshebei = FiledDataSave.PRODUCTNICKNAME,//PRODUCTNICKNAME
                guzhangcode = FiledDataSave.FAILURECODE,//FAILURECODE
                guzhangname = FiledDataSave.FAILUREDESC,//FAILUREDESC
                kehudingze = FiledDataSave.FAULTQUALIT,//FAULTQUALIT
                guzhangchuliaddress = FiledDataSave.WHICHSTATION,//WHICHSTATION
                guzhanghouguo = FiledDataSave.FAULTCONSEQ,//FAULTCONSEQ
                fasgnegjieduan = FiledDataSave.FINDPROCESS;//FINDPROCESS
        ywcTwoparaDataProvider.addOneywcListdata("客户定责", kehudingze != null ? kehudingze : "--");
        ywcTwoparaDataProvider.addOneywcListdata("故障发生时间", gzfashentime != null ? gzfashentime : "--");
        ywcTwoparaDataProvider.addOneywcListdata("故障设备", gzshebei != null ? gzshebei : "--");
        ywcTwoparaDataProvider.addOneywcListdata("故障代码", guzhangcode != null ? guzhangcode : "--");
        ywcTwoparaDataProvider.addOneywcListdata("故障名称", guzhangname != null ? guzhangname : "--");
        ywcTwoparaDataProvider.addOneywcListdata("故障处理站点", guzhangchuliaddress != null ? guzhangchuliaddress : "--");
        ywcTwoparaDataProvider.addOneywcListdata("故障后果", guzhanghouguo != null ? guzhanghouguo : "--");
        ywcTwoparaDataProvider.addOneywcListdata("发生阶段", fasgnegjieduan != null ? fasgnegjieduan : "--");

    }

    public void weiwanchengData(WeiwancDataInfo lsdata) {
        String gdbh = lsdata.getGdbh(),
                chexing = lsdata.getChexingvalue(),
                chehao = lsdata.getChehaoValue(),
                leijizoux = lsdata.getLeijizouxing(),
                kehuteletime = lsdata.getKehuCALLTIME(),
                xytime = lsdata.getXytime(),
                status = lsdata.getStatus();

        WeiwanchengDataProvider.addOneweiwcListdata("工单编号", gdbh.length() > 0 ? gdbh : "--");
        WeiwanchengDataProvider.addOneweiwcListdata("状态", status.length() > 0 ? status : "--");
        WeiwanchengDataProvider.addOneweiwcListdata("车型", chexing.length() > 0 ? chexing : "--");
        WeiwanchengDataProvider.addOneweiwcListdata("车号", chehao.length() > 0 ? chehao : "--");
        WeiwanchengDataProvider.addOneweiwcListdata("累计走行公里", leijizoux.length() > 0 ? leijizoux : "--");
        WeiwanchengDataProvider.addOneweiwcListdata("客户来电时间", kehuteletime.length() > 0 ? kehuteletime : "--");
        WeiwanchengDataProvider.addOneweiwcListdata("响应时间", xytime.length() > 0 ? xytime : "--");

    }

    public void yiwanchengData(YiwancDataInfo lsdata) {
        String gdbh = lsdata.getGdbh(),
                chexing = lsdata.getChexingvalue(),
                chehao = lsdata.getChehaoValue(),
                leijizoux = lsdata.getLeijizouxing(),
                kehuteletime = lsdata.getKehuCALLTIME(),
                xytime = lsdata.getXytime(),
                status = lsdata.getStatus();

        ywcTwoparaDataProvider.addOneywcListdata("工单编号", gdbh.length() > 0 ? gdbh : "--");
        ywcTwoparaDataProvider.addOneywcListdata("状态", status.length() > 0 ? status : "--");
        ywcTwoparaDataProvider.addOneywcListdata("车型", chexing.length() > 0 ? chexing : "--");
        ywcTwoparaDataProvider.addOneywcListdata("车号", chehao.length() > 0 ? chehao : "--");
        ywcTwoparaDataProvider.addOneywcListdata("累计走行公里", leijizoux.length() > 0 ? leijizoux : "--");
        ywcTwoparaDataProvider.addOneywcListdata("客户来电时间", kehuteletime.length() > 0 ? kehuteletime : "--");
        ywcTwoparaDataProvider.addOneywcListdata("响应时间", xytime.length() > 0 ? xytime : "--");

    }

    //请求其他表的数据 weiwanchneg
    public void request2() {
        RxHttp.postForm(Constant.usualInterfaceAddr)
                .add(Constant.usualKey, Constant.getExtraBiao())
                .asClass(GuzhangExtraFieldGet.class).
                observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    //请求开始，当前在主线程回调
                    promptDialog.showLoading("加载中...");
                })
                .doFinally(() -> {
                    //请求结束，当前在主线程回调


                    promptDialog.dismissImmediately();
                }).as(RxLife.as(this))  //感知生命周期 当退出页面时 请求未完成，则关闭请求，防止内存泄漏
                .subscribe(clz -> {//clz就是Pojo
                    List<GuzhangExtraFieldGet.DataBeanX.DataBean> datalevel = clz.data.data;
                    if (datalevel.size() > 0) {
                        GuzhangExtraFieldGet.DataBeanX.DataBean rowdatalevel = datalevel.get(0);
                        FiledDataSave.WHICHSTATION = rowdatalevel.WHICHSTATION;//故障处理站点
                        FiledDataSave.FAULTCONSEQ = rowdatalevel.FAULTCONSEQ;//故障后果
                        FiledDataSave.FAILUREDESC = rowdatalevel.FAILUREDESC;//故障名称
                        FiledDataSave.CARSECTIONNUM = rowdatalevel.CARSECTIONNUM;//车厢号

                        FiledDataSave.FAILURECODE = rowdatalevel.FAILURECODE;//故障代码
                        FiledDataSave.PRODUCTNICKNAME = rowdatalevel.PRODUCTNICKNAME;
                        FiledDataSave.FAULTTIME = rowdatalevel.FAULTTIME;
                        FiledDataSave.FINDPROCESS = rowdatalevel.FINDPROCESS;
                        FiledDataSave.RUNNINGMODE = rowdatalevel.RUNNINGMODE;
                        FiledDataSave.FAULTQUALIT = rowdatalevel.FAULTQUALIT;
                        FiledDataSave.QYFZDW = rowdatalevel.QYFZDW;
                        FiledDataSave.FAILWEATHER = rowdatalevel.FAILWEATHER;
                        FiledDataSave.ROADTYPE = rowdatalevel.ROADTYPE;

                        weiwanchengDataFromExtraBiao();
                        mAdapter.refresh(WeiwanchengDataProvider.getWeiwcListdata());
                        System.out.println("CARSECTIONNUM====" + rowdatalevel.CARSECTIONNUM);
                    }


                }, throwable -> {
                    //失败回调
                    System.out.println("失败结果---：" + throwable.getMessage());

                });

    }


    //请求其他表的数据  yiwancheng
    public void request3() {
        RxHttp.postForm(Constant.usualInterfaceAddr)
                .add(Constant.usualKey, Constant.getExtraBiao())
                .asClass(GuzhangExtraFieldGet.class).
                observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    //请求开始，当前在主线程回调
                    promptDialog.showLoading("加载中...");
                })
                .doFinally(() -> {
                    //请求结束，当前在主线程回调
                    promptDialog.dismissImmediately();
                }).as(RxLife.as(this))  //感知生命周期 当退出页面时 请求未完成，则关闭请求，防止内存泄漏
                .subscribe(clz -> {//clz就是Pojo
                    List<GuzhangExtraFieldGet.DataBeanX.DataBean> datalevel = clz.data.data;
                    if (datalevel.size() > 0) {
                        GuzhangExtraFieldGet.DataBeanX.DataBean rowdatalevel = datalevel.get(0);
                        FiledDataSave.WHICHSTATION = rowdatalevel.WHICHSTATION;//故障处理站点
                        FiledDataSave.FAULTCONSEQ = rowdatalevel.FAULTCONSEQ;//故障后果
                        FiledDataSave.FAILUREDESC = rowdatalevel.FAILUREDESC;//故障名称
                        FiledDataSave.CARSECTIONNUM = rowdatalevel.CARSECTIONNUM;//车厢号

                        FiledDataSave.FAILURECODE = rowdatalevel.FAILURECODE;//故障代码
                        FiledDataSave.PRODUCTNICKNAME = rowdatalevel.PRODUCTNICKNAME;
                        FiledDataSave.FAULTTIME = rowdatalevel.FAULTTIME;
                        FiledDataSave.FINDPROCESS = rowdatalevel.FINDPROCESS;
                        FiledDataSave.RUNNINGMODE = rowdatalevel.RUNNINGMODE;
                        FiledDataSave.FAULTQUALIT = rowdatalevel.FAULTQUALIT;
                        FiledDataSave.QYFZDW = rowdatalevel.QYFZDW;
                        FiledDataSave.FAILWEATHER = rowdatalevel.FAILWEATHER;
                        FiledDataSave.ROADTYPE = rowdatalevel.ROADTYPE;

                        yiwanchengDataFromExtraBiao();
                        mAdapterywc.refresh(ywcTwoparaDataProvider.getyiwcListdata());
                    }


                }, throwable -> {
                    //失败回调
                    System.out.println("失败结果---：" + throwable.getMessage());

                });

    }


}
