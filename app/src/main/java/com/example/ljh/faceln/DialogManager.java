package com.example.ljh.faceln;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Created by ljh on 2017/12/6.
 */

public class DialogManager {
    static ProgressDialog progressDialog;
    static AlertDialog alertDialog;
    static AlertDialog.Builder builder;

    public static void showProgressDialog(Context context,String message){
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void dismissProgressDialog(){
        if(progressDialog.isShowing() && progressDialog != null){
            progressDialog.dismiss();
        }
    }

    public static void showDialog(Context context,String message){
         builder = new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("确定",null);
        alertDialog = builder.create();
        alertDialog.show();
    }

    public static void dismissDialog(){
        if(alertDialog.isShowing() && alertDialog != null){
            alertDialog.dismiss();
        }
    }
}
