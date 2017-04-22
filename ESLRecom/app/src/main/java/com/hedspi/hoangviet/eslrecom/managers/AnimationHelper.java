package com.hedspi.hoangviet.eslrecom.managers;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;

public class AnimationHelper {
    public static void playNotifyAnimation(View view){

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "translationY", 0, 16, -16, 16, -16, 9, -9, 5, -5, 3, -3, 2, -2, 1, -1, 0, 0, 0);
        //ObjectAnimator animator3 = ObjectAnimator.ofFloat(view,"scaleY",1,1.15f,1);
        animator2.setRepeatCount(ObjectAnimator.INFINITE);
        animator2.setRepeatMode(ObjectAnimator.RESTART);
        //animator3.setRepeatCount(ObjectAnimator.INFINITE);
        //animator3.setRepeatMode(ObjectAnimator.RESTART);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator2/*, animator3*/);
        set.setDuration(1500);

        set.start();
    }

    public static void showViewDown(View view, Animator.AnimatorListener onFinishListener){
        view.clearAnimation();
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0.0f);
        view.setTranslationY(-50f);
        view.animate().translationY(0).setListener(onFinishListener).alpha(1.0f);
    }

    public static void showViewUp(View view, Animator.AnimatorListener onFinishListener){
        view.clearAnimation();
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0.0f);
        view.setTranslationY(50f);
        view.animate().translationY(0).setListener(onFinishListener).alpha(1.0f);
    }

    public static void playAssetAnimation(Context context, View view, int animId){
        view.setVisibility(View.VISIBLE);
        playAssetAnimation(context, view, animId, null);
    }

    public static void playAssetAnimation(Context context, View view, int animId, Animation.AnimationListener onFinishListener){
        Animation animation = AnimationUtils.loadAnimation(context, animId);
        animation.setAnimationListener(onFinishListener);
        view.startAnimation(animation);
    }

    public static void playHelperAnimation(final View helper, final View text1Layout, final View text2Layout){

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playHelperSpeakAnimation(helper, 250);
                playDisappearAnimation(text1Layout, 0);
                playAppearAnimation(text2Layout, 250);

                playHelperSpeakAnimation(helper, 4250);
                playDisappearAnimation(text2Layout, 4000);
                playAppearAnimation(text1Layout, 4250);

                handler.postDelayed(this, 8000);
            }
        }, 1000);

    }

    public static void playAppearAnimation(View view, long delay){
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator2);
        set.setDuration(700);
        set.setStartDelay(delay);

        set.start();
    }

    public static void playDisappearAnimation(View view, long delay){
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "alpha", 1, 0);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator2);
        set.setDuration(350);
        set.setStartDelay(delay);

        set.start();

    }

    public static void playHelperSpeakAnimation(View view, long delay){

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "translationY", 0, -16, -9, -5, -3, 0);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animator2);
        set.setDuration(500);
        set.setStartDelay(delay);

        set.start();
    }

    public void expandCardView(final CardView cardView, int height) {

        ValueAnimator anim = ValueAnimator.ofInt(cardView.getMeasuredHeightAndState(),
                height);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = cardView.getLayoutParams();
                layoutParams.height = val;
                cardView.setLayoutParams(layoutParams);
            }
        });
        anim.start();

    }

}
