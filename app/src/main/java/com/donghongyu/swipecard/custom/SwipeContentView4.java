package com.donghongyu.swipecard.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.donghongyu.swipecard.R;

/**
 * author : donghongyu
 * e-mail : 1358506549@qq.com
 * date   : 2019-12-3116:27
 * desc   :
 * version: 1.0
 */
public class SwipeContentView4 extends RelativeLayout {
    public SwipeContentView4(Context context) {
        this(context, null);
    }

    public SwipeContentView4(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeContentView4(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        Log.e("SwipeContentView4", "卡片View 生命周期====initView");
        LayoutInflater.from(context).inflate(R.layout.card_layout4, this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e("SwipeContentView4", "卡片View 生命周期====onAttachedToWindow");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("SwipeContentView4", "卡片View 生命周期====onDetachedFromWindow");
    }
}
