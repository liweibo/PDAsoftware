//package com.crrc.pdasoftware.net;
//
//
//import android.app.Activity;
//
//import com.crrc.pdasoftware.net.pojo.GuzhangListOfFWXYPojo;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.disposables.Disposable;
//import me.leefeng.promptlibrary.PromptDialog;
//import rxhttp.wrapper.param.RxHttp;
//
//public class NetUtils {
//    public PromptDialog promptDialog;
//    public Activity activitythis;
//    public static Disposable dispos=null;
//
//    public NetUtils(Activity activity) {
//        promptDialog = new PromptDialog(activity);
//        activitythis = activity;
//    }
//
//    public static Gson getGson() {
//        return new GsonBuilder()
//                // 不过滤空值
//                .serializeNulls()
//                .create();
//    }
//
//
//    //服务响应列表
//    public  List<Map> request() {
//        List<Map> lstMap = new ArrayList<>();
//        Map<String, String> map = new HashMap<>();
//        dispos = RxHttp.postForm(Constant.usualInterfaceAddr)
//                .add(Constant.usualKey, new Constant(apps).fwxyReq)
//                .asClass(GuzhangListOfFWXYPojo.class).
//                        observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe(disposable -> {
//                    //请求开始，当前在主线程回调
//                    promptDialog.showLoading("加载中...");
//
//                })
//                .doFinally(() -> {
//                    //请求结束，当前在主线程回调
//                    promptDialog.dismissImmediately();
//
//                })
//                .subscribe(clz -> {//clz就是GuzhangTestPojo
//                    List<GuzhangListOfFWXYPojo.DataBeanX.DataBean> datalevel = clz.data.data;
//
//                    if (datalevel.size() > 0) {
//                        for (int i = 0; i < datalevel.size(); i++) {//行数据个数
//                            List<GuzhangListOfFWXYPojo.DataBeanX.DataBean.ValuemapBean> rowdatalevel
//                                    = datalevel.get(i).valuemap;
//                            for (int j = 0; j < rowdatalevel.size(); j++) {
//                                String key = rowdatalevel.get(j).attribute;
//                                String va = rowdatalevel.get(j).value;
//                                map.put(key, va);
//
//                            }
//                            lstMap.add(map);
//                        }
//                        System.out.println("接口数据打印:" + lstMap.toString());
//                    }
//
//
//                }, throwable -> {
//                    //失败回调
//                    System.out.println("失败结果：" + throwable.getMessage());
//                    map.put("fail",throwable.getMessage());
//                    lstMap.add(map);
//                });
//
//
//        return lstMap;
//    }
//
//
//}
//
//
////方法1： 直接使用asclass把结果映射到GuzhangTestPojo类中。
////                RxHttp.postForm(Constant.usualInterfaceAddr)
////                        .add(Constant.usualKey, Constant.usualRequParaJson)
////                        .asClass(GuzhangListOfFWXYPojo.class).
////                        observeOn(AndroidSchedulers.mainThread())
////                        .doOnSubscribe(disposable -> {
////                            //请求开始，当前在主线程回调
////                            promptDialog.showLoading("加载中...");
////
////                        })
////                        .doFinally(() -> {
////                            //请求结束，当前在主线程回调
////                            promptDialog.dismissImmediately();
////
////                        })
////                        .subscribe(clz -> {//clz就是GuzhangTestPojo
////                            List<GuzhangListOfFWXYPojo.DataBeanX.DataBean> datalevel = clz.data.data;
////                            Map<String, String> map = new HashMap<>();
////                            List<Map> lstMap = new ArrayList<>();
////                            System.out.println("-=-:"+datalevel.size());
////                            if (datalevel.size() > 0) {
////                                for (int i = 0; i < datalevel.size(); i++) {//行数据个数
////                                    List<GuzhangListOfFWXYPojo.DataBeanX.DataBean.ValuemapBean> rowdatalevel
////                                            = datalevel.get(i).valuemap;
////                                    for (int j = 0; j < rowdatalevel.size(); j++) {
////                                        String key = rowdatalevel.get(j).attribute;
////                                        String va = rowdatalevel.get(j).value;
////                                        map.put(key, va);
////
////                                    }
////                                    lstMap.add(map);
////                                }
////                                System.out.println("88888:" + lstMap.toString());
////                                System.out.println("-=1-:"+lstMap.size());
////
////                            }
////
////
////                        }, throwable -> {
////                            //失败回调
////                            System.out.println("失败结果：" + throwable.toString());
////
////                        });
//
//
////方法2：
////使用gson把string类型的json转换到GuzhangTestPojo类中
//
////                RxHttp.postForm(Constant.usualInterfaceAddr)
////                        .add(Constant.usualKey, Constant.usualRequParaJson)
////                        .asString()
////                        .subscribe(s -> { //这里的s为String类型，即Http请求的返回结果
////                            //成功回调
////                            GuzhangTestPojo clz = NetUtils.getGson().fromJson(s, GuzhangTestPojo.class);
////
////                            List<GuzhangTestPojo.DataBeanX.DataBean> datalevel = clz.data.data;
////
////
////                            Map<String, String> map = new HashMap<>();
////                            List<Map> lstMap = new ArrayList<>();
////                            if (datalevel.size() > 0) {
////                                for (int i = 0; i < datalevel.size(); i++) {//行数据个数
////                                    List<GuzhangTestPojo.DataBeanX.DataBean.ValuemapBean> rowdatalevel
////                                            = datalevel.get(i).valuemap;
////                                    for (int j = 0; j < rowdatalevel.size(); j++) {
////                                        String key = rowdatalevel.get(j).attribute;
////                                        String va = rowdatalevel.get(j).value;
////                                        System.out.println("key:" + key);
////                                        System.out.println("value:" + va);
////                                        map.put(key, va);
////
////                                    }
////                                    lstMap.add(map);
////                                }
////                                System.out.println("88888:" + map.toString());
////                            }
////
////
////                        }, throwable -> {
////                            //失败回调
////                            System.out.println("失败结果：" + throwable.toString());
////
////                        });
