package com.example.ljh.faceln;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by ljh on 2017/11/29.
 */

public class PermissionManager extends AppCompatActivity{

    static final int CODE = 1;
    static final String[] permission= {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,
            };

    void CheckPermission(Context context){
        for(int i=0;i<permission.length;i++){
            if(ContextCompat.checkSelfPermission(context,permission[i])
                    != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= 23){
                ActivityCompat.requestPermissions(this,new String[]{permission[i]},CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CODE:
                if(grantResults.length <= 0 || grantResults[0] == -1) {
                    showTipDialog();
                }
                break;
        }
    }

    public void showTipDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("当前应用缺少必要权限，请单击【确定】按钮前往设置中心进行权限授权")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
