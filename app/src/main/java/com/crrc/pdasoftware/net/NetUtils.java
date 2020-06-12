package com.crrc.pdasoftware.net;


import com.crrc.pdasoftware.net.pojo.GuzhangListOfFWXYPojo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import rxhttp.wrapper.param.RxHttp;

public  class NetUtils {



    public static Gson getGson() {
        return new GsonBuilder()
                // 不过滤空值
                .serializeNulls()
                .create();
    }

    public void request(){
//        RxHttp.postForm(Constant.usualInterfaceAddr)
//                .add(Constant.usualKey, Constant.usualRequParaJson)
//                .asClass(GuzhangListOfFWXYPojo.class).
    }
}
