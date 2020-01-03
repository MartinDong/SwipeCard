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
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.Queue;


/**
 * Created by donghongyu on 2019/12/30.
 * 高仿探探、陌陌左右，滑动卡片容器，
 * 这个容器只有两个View实体存在，每滑动一次将从集合中删除一个
 * <p>
 * 使用这个传入布局
 *
 * @see CardAdapter#bindLayout
 * <p>
 * 使用这个将数据绑定到布局中
 * @see CardAdapter#bindData
 */
public class SwipeCardLayout2 extends RelativeLayout {

    private Paint paint;

    float mScale = 0.7f;
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

    public SwipeCardLayout2(Context context) {
        super(context);
        init();
    }

    public SwipeCardLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwipeCardLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);


        setClipToPadding(false);
        density = getResources().getDisplayMetrics().density;

        mWidth = 400;
        mHeight = 500;
        mTranslate = (int) ((mHeight - mHeight * mScale) / 2 + density * 10);


        //突出的卡片底部高度
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

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {

        this.onSwipeListener = onSwipeListener;
    }

    public void setAdapter(CardAdapter adapter) {
        this.adapter = adapter;


        SwipeLayout swipeLayout = new SwipeLayout(getContext());
        LayoutParams params = new LayoutParams(mWidth, mHeight);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        swipeLayout.setLayoutParams(params);
        swipeLayout.setTranslationY(mTranslate);
        swipeLayout.setScaleX(mScale);
        swipeLayout.setScaleY(mScale);

        View bindLayout = adapter.bindLayout();
        swipeLayout.addView(bindLayout);

        addView(swipeLayout);


        SwipeLayout swipeLayout1 = new SwipeLayout(getContext());
        LayoutParams params1 = new LayoutParams(mWidth, mHeight);
        params1.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        swipeLayout1.setLayoutParams(params1);

        View bindLayout1 = adapter.bindLayout();
        swipeLayout1.addView(bindLayout1);

        addView(swipeLayout1);

        CardEntity cardEntity1 = (CardEntity) adapter.tQueue.poll();
        //添加到队尾
        adapter.tQueue.add(cardEntity1);

        adapter.bindData(cardEntity1, bindLayout1);

        CardEntity cardEntity = (CardEntity) adapter.tQueue.poll();
        //添加到队尾
        adapter.tQueue.add(cardEntity);
        adapter.bindData(cardEntity, bindLayout);

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

    public static abstract class CardAdapter<T> {
        Queue<T> tQueue;

        public CardAdapter(Queue<T> tQueue) {
            this.tQueue = tQueue;
        }

        public abstract View bindLayout();

        public abstract void bindData(T data, View convertView);
    }

    class SwipeLayout extends FrameLayout {

        private float eventRawX;
        private float eventRawY;
        private boolean isAnimation;

        public SwipeLayout(Context context) {
            super(context);
        }

        public SwipeLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        void refreshFloor() {
            float translationX = Math.abs(getTranslationX());
            if (translationX <= 300) {

                float p = translationX / 300;

                RelativeLayout parent = (RelativeLayout) getParent();
                View childAt = parent.getChildAt(0);
                if (childAt != this) {
                    childAt.setScaleX(mScale + (1 - mScale) * p);
                    childAt.setScaleY(mScale + (1 - mScale) * p);
                    childAt.setTranslationY(mTranslate - (mTranslate * p));
                    parent.invalidate();
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
                    final RelativeLayout parent = (RelativeLayout) getParent();
                    if (translationX1 > 300 || translationX1 < -300) {//消失
                        int tx = translationX1 > 300 ? parent.getWidth() : -parent.getWidth() / 2 - getWidth();
                        PropertyValuesHolder holder = PropertyValuesHolder.ofFloat("translationX", translationX1, tx);
                        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(this, holder);
                        animator.setDuration(100);
                        animator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                isAnimation = true;
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                if (onSwipeListener != null) {
                                    int type = translationX1 > 300 ? TYPE_RIGHT : TYPE_LEFT;
                                    onSwipeListener.onSwipe(type);
                                }
                                if (adapter.tQueue.size() > 0) {
                                    SwipeLayout childAt = (SwipeLayout) parent.getChildAt(1);

                                    CardEntity cardEntity = (CardEntity) adapter.tQueue.poll();

                                    //添加到队尾
                                    adapter.tQueue.add(cardEntity);
                                    adapter.bindData(cardEntity, childAt.getChildAt(0));

                                } else {
//                                    parent.removeView(SwipeLayout.this);
                                    return;
                                }

                                if (adapter.tQueue.size() >= 0) {
                                    // TODO 这里将内部的子View提前到第一个位置
                                    parent.getChildAt(0).bringToFront();
                                    setRotation(0);
                                    setTranslationX(0);

                                    setTranslationY(mTranslate);
                                    setScaleX(mScale);
                                    setScaleY(mScale);
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

                        animator.start();
                    } else {//复原


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
                    }
                    break;
            }
            return true;
        }
    }

    public static class CardEntity {
        public CardEntity(int resId, String content) {
            this.resId = resId;
            this.content = content;
        }

        public int resId;
        public String content;
    }
}

