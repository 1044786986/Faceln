package com.example.ljh.faceln;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;

/**
 * Created by ljh on 2017/12/3.
 */

public class AutoFitTextureView extends TextureView{

    private int mRatioWidth = 0;
    private int mRatioHeight = 0;
    public AutoFitTextureView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    public void setAspectRatio(int width, int height)
    {
        mRatioWidth = width;
        mRatioHeight = height;
        requestLayout();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (0 == mRatioWidth || 0 == mRatioHeight)
        {
            setMeasuredDimension(width, height);
            //setMeasuredDimension(1,1);
        }
        else
        {
            if (width < height * mRatioWidth / mRatioHeight)
            {
                //setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
                setMeasuredDimension(1,1);
            }
            else
            {
                //setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
                setMeasuredDimension(1,1);
            }
        }
    }
}
