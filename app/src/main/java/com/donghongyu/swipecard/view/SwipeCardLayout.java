package com.donghongyu.swipecard.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.List;


/**
 * Created by donghongyu on 2019/12/30.
 * 高仿探探、陌陌左右，滑动卡片容器，这个容器是不销毁任何的View，添加几个存在集合，可以循环♻️的进行展示
 */
public class SwipeCardLayout extends RelativeLayout {
    private Paint paint;

    float mScale = 0.9f;
    int mTranslate;
    private float mDy;
    private int mHeight;
    private int mWidth;
    private CardAdapter adapter;
    private RectF rect;
    private float round;
    private float density;

    public static final int TYPE_LEFT = 1;
    public static final int TYPE_RIGHT = 2;
    private OnSwipeListener onSwipeListener;
    private Paint paint_s;

    public SwipeCardLayout(Context context) {
        super(context);
        init();
    }

    public SwipeCardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwipeCardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);

        setClipToPadding(false);
        density = getResources().getDisplayMetrics().density;

        // TODO 这里定义的大小，根据自己的需要自己修改
        mWidth = 400;
        mHeight = 500;
        mTranslate = (int) ((mHeight - mHeight * mScale) / 2 + density * 10);

        // 突出的卡片底部高度
        mDy = mTranslate - (mHeight * (1 - mScale) / 2);
//        mDy = density * 5;
        paint = new Paint();
        paint.setColor(Color.parseColor("#dddddd"));
        paint.setAntiAlias(true);

        paint_s = new Paint();
        paint_s.setColor(Color.parseColor("#aaaaaa"));
        paint_s.setStyle(Paint.Style.STROKE);
        paint_s.setStrokeWidth(2);
        paint_s.setAntiAlias(true);
        rect = new RectF();

        round = density * 8;
    }


    public interface OnSwipeListener {
        void onSwipe(int type);
    }

    /**
     * 设置滑动监听
     *
     * @param onSwipeListener 滑动监听
     */
    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
    }


    /**
     * 设置适配器
     *
     * @param adapter 适配器
     */
    public void setAdapter(CardAdapter adapter) {
        this.adapter = adapter;

        for (int i = 0; i < adapter.tQueue.size(); i++) {
            SwipeLayout swipeLayout = new SwipeLayout(getContext());
            LayoutParams params = new LayoutParams(mWidth, mHeight);
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            swipeLayout.setLayoutParams(params);

            swipeLayout.addView(adapter.tQueue.get(i));
            addView(swipeLayout);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Log.i(TAG, "onDraw: " + x + " " + y);
        float width = getWidth() / 2.0f;
        float height = getHeight() / 2.0f;
        View childAt = getChildAt(0);

        //画固定底片
        if (adapter.tQueue.size() > 1) {
            float s_width1 = childAt.getWidth() * mScale;
            float s_height1 = childAt.getHeight() * mScale;
            float dx1 = (s_width1 - s_width1 * mScale) / 2;
            float l1 = width - s_width1 / 2 + dx1;
            float t1 = height + s_height1 / 2 + mTranslate;
            float r1 = width + s_width1 / 2 - dx1;
            float b1 = t1 + mDy;

            rect.left = l1;
            rect.top = t1 - 20 * density;
            rect.right = r1;
            rect.bottom = b1;
            canvas.drawRoundRect(rect, round, round, paint);
            canvas.drawRoundRect(rect, round, round, paint_s);
        }


        if (adapter.tQueue.size() > 0) {
            float scaleX = childAt.getScaleX();
            float s_width = childAt.getWidth() * scaleX;
            float s_height = childAt.getHeight() * scaleX;

            float dx = (s_width - s_width * mScale) / 2;
            float l = width - s_width / 2 + dx;
            float t = height + s_height / 2 + childAt.getTranslationY();
            float r = width + s_width / 2 - dx;
            float b = t + mDy;

            rect.left = l;
            rect.top = t - 20 * density;
            rect.right = r;
            rect.bottom = b;
            canvas.drawRoundRect(rect, round, round, paint);
            canvas.drawRoundRect(rect, round, round, paint_s);
        }

    }

    public static class CardAdapter {
        List<View> tQueue;

        public CardAdapter(List<View> tQueue) {
            this.tQueue = tQueue;
        }
    }


    // 卡片布局
    class SwipeLayout extends FrameLayout {

        private float eventRawX;
        private float eventRawY;
        private boolean isAnimation;
        private boolean isBringToBack;

        public SwipeLayout(Context context) {
            super(context);
        }

        public SwipeLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        // 刷新底部的层叠效果
        void refreshFloor() {
            float translationX = Math.abs(getTranslationX());
            if (translationX <= 300) {
                float p = translationX / 300;
                RelativeLayout parent = (RelativeLayout) getParent();

                for (int i = 0; i < parent.getChildCount(); i++) {
                    View childAt = parent.getChildAt(i);
                    if (i < parent.getChildCount() - 1) {
                        if (childAt != this) {
                            childAt.setScaleX(mScale + (1 - mScale) * p);
                            childAt.setScaleY(mScale + (1 - mScale) * p);
                            childAt.setTranslationY(mTranslate - (mTranslate * p));
                            parent.invalidate();
                        }
                    } else {
                        childAt.setScaleX(1);
                        childAt.setScaleY(1);
                        childAt.setTranslationY(0);
                        parent.invalidate();
                    }
                }
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (isAnimation) {
                return true;
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    eventRawX = event.getRawX();
                    eventRawY = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float rawX = event.getRawX();
                    float rawY = event.getRawY();
                    float dx = rawX - eventRawX;
                    float dy = rawY - eventRawY;
                    float translationX = getTranslationX();
                    setTranslationX(translationX + dx);
                    setTranslationY(getTranslationY() + dy);

                    if (translationX <= 300) {
                        setRotation(getTranslationX() / 300 * 15);
                    }
                    refreshFloor();

                    eventRawX = rawX;
                    eventRawY = rawY;
                    break;
                case MotionEvent.ACTION_UP:
                    final float translationX1 = getTranslationX();
                    if (translationX1 > 300 || translationX1 < -300) {//消失
                        // 将当前的View放到ViewGroup的第一个位置，即在ViewGroup的最底层进行绘制
                        bringToIndex(this, 0);
                        isBringToBack = true;
                    }
                    //复原
                    PropertyValuesHolder holder1 = PropertyValuesHolder.ofFloat("translationX", translationX1, 0);
                    PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("translationY", getTranslationY(), 0);
                    PropertyValuesHolder holder3 = PropertyValuesHolder.ofFloat("rotation", getRotation(), 0);
                    ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(this, holder1, holder2, holder3);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            refreshFloor();
                        }
                    });
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            isAnimation = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (isBringToBack && onSwipeListener != null) {
                                int type = translationX1 > 300 ? TYPE_RIGHT : TYPE_LEFT;
                                onSwipeListener.onSwipe(type);
                            }
                            isAnimation = false;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            isAnimation = false;
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    animator.setInterpolator(new OvershootInterpolator());
                    animator.setDuration(150);
                    animator.start();
                    break;
            }
            return true;
        }
    }

    // 将指定的子view替换到指定的位置层级
    private void bringToIndex(View child, int index) {
        ViewGroup mParent = (ViewGroup) child.getParent();
        if (index >= 0) {
            // 将child移除
            mParent.removeView(child);
            // 将child添加到最底部
            mParent.addView(child, index);
            // 请求刷新布局进行重新绘制
            mParent.requestLayout();
            mParent.invalidate();
        }
    }
}

