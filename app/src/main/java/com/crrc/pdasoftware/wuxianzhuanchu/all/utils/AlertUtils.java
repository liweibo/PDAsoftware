package com.crrc.pdasoftware.wuxianzhuanchu.all.utils;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

public class AlertUtils {
    public static void alertNoListener(Context context, String subTitle){
        AlertDialog.Builder builder  = new AlertDialog.Builder(context);
        builder.setTitle("提示" ) ;
        builder.setMessage(subTitle) ;
        builder.setPositiveButton("知道了" ,  null );
        builder.show();
    }
}
