package com.example.ljh.faceln;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.FileNotFoundException;

/**
 * Created by ljh on 2017/12/1.
 */

public class CompressManager {
    static BitmapFactory.Options options;

    static void initOptions(){
        options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
    }

    static Bitmap CompressFromCamera(Context context, Uri uri) {
        initOptions();
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri),null,options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
