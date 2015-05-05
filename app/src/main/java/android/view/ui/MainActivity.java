package android.view.ui;

import android.animation.Animator;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.R;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.ui.widget.FloatButton;
import android.view.ui.widget.RippleView;
import android.view.utils.UIUtils;

import com.nineoldandroids.view.ViewPropertyAnimator;


public class MainActivity extends BaseActivity {

    private AnimationSet mAnimationSetIn;
    private AnimationSet mAnimationSetOut;

    private FloatButton mFloatButton;
    private RippleView mRippleView;
    private View mSelectView;
    private Toolbar mToolbar;

    private Handler mHandler = new Handler ();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        mRippleView = (RippleView) this.findViewById (R.id.rippleview);
        mFloatButton = (FloatButton) this.findViewById (R.id.float_button);
        mFloatButton.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                startRipple ();
            }
        });
        initFloatButtonAnimation ();
        com.wiki4zhang.library.common.Logger.d("CurrentLogMessage is %s","success");
    }

    private void startRipple () {
        Point point = UIUtils.getSrcViewPointAtDestView (mFloatButton, mRippleView);
        if (mSelectView == null) {
            //show
            dismissToolbar (true);
            diseableFloatButton (false);
            mRippleView.showRipple (mFloatButton,point.x, point.y, mFloatButton.getWidth () / 2,
                    getResources ().getColor (R.color.colorAccent),
                    new Animator.AnimatorListener () {
                @Override
                public void onAnimationStart (Animator animation) {
                    postSetStatusBarColor(getResources ().getColor (R.color.colorAccent),
                            RippleView.DEFAULT_DURATION * 76/160);
                }
                @Override
                public void onAnimationEnd (Animator animation) {
                    getmDrawerLayout ().setDrawerLockMode (DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }
                @Override
                public void onAnimationCancel (Animator animation) {
                }
                @Override
                public void onAnimationRepeat (Animator animation) {
                }
            });
            mSelectView = mFloatButton;
        } else {
            //hide
            dismissToolbar (false);
            postSetStatusBarColor(mColorPrimaryDark == -1 ? getResources ().getColor (R.color.colorPrimaryDark) : mColorPrimaryDark,
                    RippleView.DEFAULT_DURATION *15/18 +RippleView.DEFAULT_DELAY*15/20);

            mRippleView.hideRipple (point.x, point.y, mFloatButton.getWidth () / 2,new Animator.AnimatorListener () {
                @Override
                public void onAnimationStart (Animator animation) {
                    diseableFloatButton (false);

                }
                @Override
                public void onAnimationEnd (Animator animation) {
                    getmDrawerLayout ().setDrawerLockMode (DrawerLayout.LOCK_MODE_UNLOCKED);
                    diseableFloatButton (true);
                }
                @Override
                public void onAnimationCancel (Animator animation) {
                }
                @Override
                public void onAnimationRepeat (Animator animation) {

                }
            });
            mSelectView = null;
        }
    }

    private void postSetStatusBarColor(final  int color,int delay){
        mHandler.postDelayed (new Runnable () {
            @Override
            public void run () {
                runOnUiThread (new Runnable () {
                    @Override
                    public void run () {
                        setStatusBarColor (color);
                    }
                });
            }
        },delay);
    }

    private void dismissToolbar (Boolean hideToolBar) {
        ViewPropertyAnimator.animate (mToolbar).alpha (hideToolBar?0f:1f)
                .setDuration ((long) (RippleView.DEFAULT_DURATION * 0.95f)).start ();
        mRippleView.setClickable (hideToolBar);
        mRippleView.requestFocus ();
        dismissFloatButton (hideToolBar);
        animateFloatButton (hideToolBar);
    }

    private void dismissFloatButton(boolean hideFloatButton){
        if(hideFloatButton){
            mFloatButton.updateFloatBackGround ();
        }else{
            mFloatButton.updateShadowBackGround ();
        }
    }

    private void diseableFloatButton(boolean isEnable){
        mFloatButton.setEnabled (isEnable);
        mFloatButton.setClickable (isEnable);
    }

    private void initFloatButtonAnimation(){
        mAnimationSetIn = (AnimationSet) AnimationUtils.loadAnimation (getApplicationContext (),R.anim.rotation_in);
        mAnimationSetIn.setDuration (RippleView.DEFAULT_DURATION );

        mAnimationSetOut = (AnimationSet) AnimationUtils.loadAnimation (getApplicationContext (),R.anim.rotation_out);
        mAnimationSetOut.setDuration (RippleView.DEFAULT_DURATION);
    }

    private void animateFloatButton(Boolean hideToolBar){
        mFloatButton.startAnimation (hideToolBar?mAnimationSetIn:mAnimationSetOut);
    }

    @Override
    protected void onPostCreate (Bundle savedInstanceState) {
        super.onPostCreate (savedInstanceState);
        tryToSetActionBar();
    }
    @Override
    protected void tryToSetActionBar () {
        mToolbar = getToolActionBar ();
        //默认显示第一个
        updateToolbarToExtendToolbar ("Material design");
        checkVersion4Toolbar ();
    }
}
