package android.view.utils;

import android.view.View;
import android.view.animation.Interpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * 动画集合类
 * Created by wiki on 15-1-16.
 */
public class AnimationCollections {

    private static final long S_DEFALUT_DURATION_TIME = 900;

    private static final float S_FROM_SCALE = 0F;
    private static final float S_TO_SCALE = 1.0F;
    private static final float TEXT_SMALL = 0.6F;
    private static final float TEXT_FROM_SCALE = 1.0F;
    private static final float TEXT_TO_SCALE = 1.4F;


    public static  void scaleRippleButton(View view,Interpolator interpolator,
                                          float from,
                                          float to,
                                          final int duration){
        final AnimatorSet animatorSet = new AnimatorSet ();
        final AnimatorSet backAnimatorSet = new AnimatorSet ();
        animatorSet.playTogether (
                ObjectAnimator.ofFloat(view, "scaleX", from, to),
                ObjectAnimator.ofFloat(view, "scaleY", from, to)
        );

        backAnimatorSet.playTogether (
                ObjectAnimator.ofFloat(view, "scaleX", to, from),
                ObjectAnimator.ofFloat(view, "scaleY", to, from)
        );

        if(interpolator!=null){
            animatorSet.setInterpolator (interpolator);
        }
        animatorSet.addListener (new Animator.AnimatorListener () {
            @Override
            public void onAnimationStart (Animator animation) {
            }
            @Override
            public void onAnimationEnd (Animator animation) {
                backAnimatorSet.setDuration (duration).start ();
                animatorSet.removeAllListeners ();
            }
            @Override
            public void onAnimationCancel (Animator animation) {
            }
            @Override
            public void onAnimationRepeat (Animator animation) {
            }
        });
        animatorSet.setDuration (duration).start ();
    }

}
