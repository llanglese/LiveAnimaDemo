package com.juying.vrmu.demo.liveanimademo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author keynes
 * @date 2018/04/28/028 上午 10:01
 * @$desc$ 连续点击送礼物效果的倒计时View
 */

public class QuickClickView extends View implements IQuickClickView{
    private Paint mPaint;

    // 当前的计数
    private int mCurCount = 1;

    // 底部圆的颜色，带点透明
    private final String sBgColor = "#59000000";
    // 圆环的颜色
    private final String sRingColor = "#FF6F2EB1";

    // 字体当前的大小
    private float mTextCurSize;
    // 字体的最大值
    private float mTextMaxSize = 70;
    // 圆环的弧度的区域
    private RectF mRectF;
    // 圆环的弧度。
    private float mRingDegree = 360;
    // 圆环的宽度
    private final static int mRingWidth = 10;
    // 圆环缩短的动画
    private ValueAnimator mRingAnimator;
    // 动画时长，毫秒
    private final static int sAnimatorTime = 3000;

    private ValueAnimator mTextAnimator;

    private float cx, cy;

    private OnAnimationListener listener;

    public QuickClickView(Context context) {
        this(context, null);
    }

    public QuickClickView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickClickView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, @Nullable AttributeSet attrs) {

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTypeface(Typeface.create("", Typeface.BOLD_ITALIC));
        mPaint.setTextAlign(Paint.Align.CENTER);

        /*
         * 倒计时, 控制圆环的弧度和字体的大小的变化。
         * 根据时间的比率，来计算并设置。
         */
        mRingAnimator = ValueAnimator.ofInt(0, 100);
        mRingAnimator.setDuration(sAnimatorTime);

        mRingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int percent = Integer.valueOf(String.valueOf(animation.getAnimatedValue()));

                setRingDegree(360 - (360 * (percent / 100f)));
                // 走完, 隐藏掉
                if (percent == 100) {
                    if (listener != null) listener.onAnimation(true);
                    setVisibility(INVISIBLE);
                }
            }
        });
//        mRingAnimator.addUpdateListener(animation -> {
//
//        });

        mTextAnimator = ValueAnimator.ofFloat(0, mTextMaxSize);
        mTextAnimator.setDuration(250).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTextCurSize = Float.valueOf(String.valueOf(animation.getAnimatedValue()));
                    invalidate();
            }
        });
//        mTextAnimator.setDuration(250)
//                .addUpdateListener(animation -> {
//                    mTextCurSize = Float.valueOf(String.valueOf(animation.getAnimatedValue()));
//                    invalidate();
//                });

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mRectF == null) {
            cx = getMeasuredWidth() / 2;
            cy = getMeasuredHeight() / 2;
            mRectF = new RectF();
            mRectF.top = mRectF.left = mRingWidth / 2;
            mRectF.right = getMeasuredWidth() - mRectF.left;
            mRectF.bottom = getMeasuredHeight() - mRectF.top;
        }

        drawCircle(canvas);
        drawText(canvas);
        drawRing(canvas);
    }

    @Override
    public void drawCircle(Canvas canvas) {

        // 绘制底部圆
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor(sBgColor));
        canvas.drawCircle(cx, cy, cx, mPaint);
    }

    @Override
    public void drawRing(Canvas canvas) {

        // 绘制圆环。
        // setStrokeWidth这个方法，并不是往圆内侧增加圆环宽度的，
        // 而是往外侧增加一半，往内侧增加一半。
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mRingWidth);
        mPaint.setColor(Color.parseColor(sRingColor));
        canvas.drawArc(mRectF, -90, mRingDegree, false, mPaint);
    }


    @Override
    public void drawText(final Canvas canvas) {

        // 文本样式
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(mTextCurSize);

        // 获取文本的基线，使其居中
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();

        // 绘制文本
        canvas.drawText(String.valueOf("x" + mCurCount), cx,
                (int) (cx - fontMetrics.top / 2 - fontMetrics.bottom / 2), mPaint);
    }

    @Override
    public void startCountTime() {

        if (mRingAnimator.isRunning()) {
            // 如果还在倒计时中，计数自增，并取消动画。
            ++mCurCount;
            mRingAnimator.cancel();
        } else {
            // 如果没有在倒计时中，则重置为1
            mCurCount = 1;
            setVisibility(VISIBLE);
        }

        if (mTextAnimator.isRunning()) mTextAnimator.cancel();
        if (listener != null) listener.onAnimation(false);

        // 启动动画
        mRingAnimator.start();
        mTextAnimator.start();
    }

    @Override
    public void setOnAnimationListener(OnAnimationListener listener) {
        this.listener = listener;
    }

    @Override
    public void setRingDegree(float degree) {

        // 设置圆环弧度
        mRingDegree = degree;
        // 让View重绘
        invalidate();
    }

}
