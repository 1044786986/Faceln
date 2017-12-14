package com.example.ljh.faceln;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by ljh on 2017/11/28.
 */

public class TransformationManager {

    /**
     * bitmap转换byte[]
     */
    public static byte[] BitmapToByte(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    /**
     *byte[]转换为String
     */
    public static String ByteToString(byte bytes[]){
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }

    /**
     * string转byte
     */
    public static byte[] StringToByte(String string){
        return Base64.decode(string, Base64.DEFAULT);
    }

    /**
     *String转bitmap
     */
    public static Bitmap StringToBitmap(String string){
        byte bytes[] = Base64.decode(string,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }

    /**
     * 图片转字符
     */
    public static String BitmapToString(Bitmap bitmap){
        byte bytes[] = BitmapToByte(bitmap);
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }
}
