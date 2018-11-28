package com.juying.vrmu.demo.liveanimademo;

import android.graphics.Canvas;

/**
 * @author keynes
 * @date 2018/04/28/028 上午 10:00
 * @$desc$  连续效果的View的协议
 */

public interface IQuickClickView {

    // 画圆
    void drawCircle(Canvas canvas);

    // 画弧线
    void drawRing(Canvas canvas);

    /**
     * 设置弧度
     *
     * @param degree 0~360
     */
    void setRingDegree(float degree);

    // 绘制居中文本
    void drawText(Canvas canvas);

    // 开始计时
    void startCountTime();

    void setOnAnimationListener(OnAnimationListener listener);

    interface OnAnimationListener {
        void onAnimation(boolean isStop);
    }
}
