package com.juying.vrmu.demo.liveanimademo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

public class MainActivity extends AppCompatActivity {
    ViewGroup giftLayout;
    QuickClickView quickClickView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         quickClickView = findViewById(R.id.quick_click_view);
        giftLayout = findViewById(R.id.rl_gift);
        quickClickView.startCountTime();

        initAnima();
    }

    private void initAnima() {
        quickClickView.setOnAnimationListener(new IQuickClickView.OnAnimationListener() {
            @Override
            public void onAnimation(boolean isStop) {
                if (isStop) {
                    // 连击倒计时结束时，清空缩放动画
                    giftLayout.clearAnimation();
                } else if (giftLayout.getAnimation() == null) {
                    // 连击时，这个方法会多次触发，避免不断的重load资源和突然变大，需要加个判断，null时，才需要重新加载动画。
                    giftLayout.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale_gift_layout));
                }
            }
        });

    }
}
