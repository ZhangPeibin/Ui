package android.view.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.util.AttributeSet;
import android.view.R;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.utils.Logger;
import android.view.utils.UIUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
/**
 * Created by wiki on 15-2-6.
 */
public class RippleView extends RelativeLayout {


    public static final int DEFAULT_DURATION = 360;

    public static final int DEFAULT_CIRCLE_DURATION =60;
    public static final int DEFAULT_DELAY = 60;

    private static final int DEFAULT_CIRCLE_SIZE =60;//dp
    private static final int DEFAULT_CIRCLE_DISTANCE = 200;
    private static final int DEFAULT_CHILDREN_COUNT = 6;
    private ImageButton[] mImageViews;

    private Point mCenterPoint;
    private Point[] mImagePoints;

    private static final float SCALE = 8f;
    private View mView;//
    private ShapeDrawable mShapeDrawable;
    private ViewPropertyAnimator mViewPropertyAnimator;

    private Animator animation = null;
    private Animator mBackAnimation = null;

    private FloatButton mFloatButton;

    private float mStartScale = -0.1f;
    private float mEndScale = -0.1f;

    public RippleView (Context context) {
        this (context, null);
    }

    public RippleView (Context context, AttributeSet attrs) {
        super (context, attrs);
        setDefaultProperties ();
        //prepareCircleView();
    }

    @TargetApi (Build.VERSION_CODES.JELLY_BEAN)
    private void setDefaultProperties () {
        mView = new View (getContext ().getApplicationContext ());
        mShapeDrawable = new ShapeDrawable (new OvalShape ());
        mView.setBackground (mShapeDrawable);
        addView (mView,0);
        mView.setVisibility (View.INVISIBLE);

        prepareCircleView ();
        prepareCirclePoint();
        hideAllCircle ();
        readyCircleAnimation ();
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize (widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize (heightMeasureSpec);
        setMeasuredDimension (measureWidth, measureHeight);
        final float circleSize = (float) Math.sqrt (measureWidth * measureWidth + measureHeight * measureHeight) * 2f;
        final int size = (int) (circleSize / SCALE);
        final int sizeSpec = MeasureSpec.makeMeasureSpec (size, MeasureSpec.EXACTLY);
        mView.measure (sizeSpec, sizeSpec);

        for (int i = 0; i < mImageViews.length ; i++) {
            mImageViews[i].measure (UIUtils.calculateDpToPx (getContext (),DEFAULT_CIRCLE_SIZE),
                    UIUtils.calculateDpToPx (getContext (),DEFAULT_CIRCLE_SIZE));
        }
    }

    @Override
    protected void onLayout (boolean changed, int l, int t, int r, int b) {
        super.onLayout (changed, l, t, r, b);
        mView.layout (l, t, l + mView.getMeasuredWidth (), t + mView.getMeasuredHeight ());

        for (int i = 0; i < mImageViews.length ; i++) {
            mImageViews[i]. layout ((l+getMeasuredWidth ()-mImageViews[i].getMeasuredWidth())/2,
                    (t+getMeasuredHeight ()-mImageViews[i].getMeasuredHeight())/2,
                    (l+getMeasuredWidth ()+mImageViews[i].getMeasuredWidth())/2,
                    (t+getMeasuredHeight ()+mImageViews[i].getMeasuredHeight())/2);
        }
    }

    /**
     * 开始进行ripple效果
     *
     * @param touchX          我们点击button或者其他点击view相对于这个view的x轴坐标
     * @param touchY          我们点击button或者其他点击view相对于这个view的Y轴坐标
     * @param startRadius     我们开始进行的ripple的radius
     * @param backgroundColor 背景颜色
     */
    public synchronized  void showRipple (FloatButton floatButton,int touchX, int touchY, float startRadius, int backgroundColor, final Animator.AnimatorListener animatorListener) {
        if (mViewPropertyAnimator != null) {
            mViewPropertyAnimator.cancel ();
        }
        mFloatButton = floatButton;

        mView.setVisibility (View.VISIBLE);

        mShapeDrawable.getPaint ().setColor (backgroundColor);
        mStartScale = calculateStartScale (startRadius);
        prepareShapeView (touchX, touchY, mStartScale);
        mEndScale = calculateScale (touchX, touchY) * (SCALE + 1);
        mViewPropertyAnimator = mView.animate ()
                .scaleX (mEndScale).scaleY (mEndScale).setDuration (DEFAULT_DURATION);
        mViewPropertyAnimator.setListener (new Animator.AnimatorListener () {
            @Override
            public void onAnimationStart (Animator animation) {
                if (animatorListener != null) {
                    animatorListener.onAnimationStart (animation);
                }
            }
            @Override
            public void onAnimationEnd (Animator animation) {
                if (animatorListener != null) {
                    animatorListener.onAnimationEnd (animation);
                }
                showAllCircle ();
                startAnimation ();
            }
            @Override
            public void onAnimationCancel (Animator animation) {
                if (animatorListener != null) {
                    animatorListener.onAnimationCancel (animation);
                }
            }
            @Override
            public void onAnimationRepeat (Animator animation) {
                if (animatorListener != null) {
                    animatorListener.onAnimationRepeat (animation);
                }
            }
        });
        mViewPropertyAnimator.setInterpolator (new BakedBezierInterpolator ());
        mViewPropertyAnimator.start ();
    }

    public  synchronized void hideRipple (int touchX, int touchY, float startRadius, final Animator.AnimatorListener animatorListener) {
        readyCircleBackAnimation (new Animator.AnimatorListener () {
            @Override
            public void onAnimationStart (Animator animation) {
            }
            @Override
            public void onAnimationEnd (Animator animation) {
                hideAllCircle ();
                hide (animatorListener);
            }
            @Override
            public void onAnimationCancel (Animator animation) {
            }
            @Override
            public void onAnimationRepeat (Animator animation) {
            }
        });
        startBackAnimation ();
    }


    private void hide(final Animator.AnimatorListener animatorListener){
        mViewPropertyAnimator = mView.animate ()
                .scaleX (mStartScale).scaleY (mStartScale).setDuration (DEFAULT_DURATION);
        mViewPropertyAnimator.setListener (new Animator.AnimatorListener () {
            @Override
            public void onAnimationStart (Animator animation) {
                if (animatorListener != null) {
                    animatorListener.onAnimationStart (animation);
                }
            }
            @Override
            public void onAnimationEnd (Animator animation) {
                if (animatorListener != null) {
                    Logger.v (TAG,"onAnimationEnd");
                    animatorListener.onAnimationEnd (animation);
                }
            }
            @Override
            public void onAnimationCancel (Animator animation) {
                if (animatorListener != null) {
                    animatorListener.onAnimationCancel (animation);
                }
            }
            @Override
            public void onAnimationRepeat (Animator animation) {
                if (animatorListener != null) {
                    animatorListener.onAnimationRepeat (animation);
                }
            }
        });
        mViewPropertyAnimator.setInterpolator (new BakedBezierInterpolator ());
    }

    private float calculateStartScale (float startRadius) {
        return startRadius * 2 / mView.getMeasuredWidth ();
    }

    private void prepareShapeView (int touchX, int touchY, float mStartScale) {
        int shapePositionX = mView.getWidth () / 2;
        int shapePositionY = mView.getHeight () / 2;
        int translateX = touchX - shapePositionX;
        int translateY = touchY - shapePositionY;
        mView.setTranslationX (translateX);
        mView.setTranslationY (translateY);
        mView.setScaleX (mStartScale);
        mView.setScaleY (mStartScale);
        mView.setPivotX (mView.getWidth () / 2);
        mView.setPivotY (mView.getHeight () / 2);
    }

    private float calculateScale (int x, int y) {
        final float centerX = getWidth () / 2f;
        final float centerY = getHeight () / 2f;
        final float maxDistance = (float) Math.sqrt (centerX * centerX + centerY * centerY);
        final float deltaX = centerX - x;
        final float deltaY = centerY - y;
        final float distance = (float) Math.sqrt (deltaX * deltaX + deltaY * deltaY);
        final float scale = 0.5f + (distance / maxDistance) * 0.5f;
        return scale;
    }

    @TargetApi (Build.VERSION_CODES.JELLY_BEAN)
    private void prepareCircleView () {
        mImageViews = new ImageButton[DEFAULT_CHILDREN_COUNT];
        for (int i = 0; i<DEFAULT_CHILDREN_COUNT ; i++) {
             mImageViews[i] = new ImageButton (getContext ().getApplicationContext ());
             addView (mImageViews[i],i+1);
             switch (i){
                 case 0:
                     mImageViews[i].setBackgroundResource (R.drawable.chat);
                     break;
                 case 1:
                     mImageViews[i].setBackgroundResource (R.drawable.vedio);
                     break;
                 case 2:
                     mImageViews[i].setBackgroundResource (R.drawable.text);
                     break;
                 case 3:
                     mImageViews[i].setBackgroundResource (R.drawable.link);
                     break;
                 case 4:
                     mImageViews[i].setBackgroundResource (R.drawable.quote);
                     break;
                 case 5:
                     mImageViews[i].setBackgroundResource (R.drawable.photo);
                     break;
             }
        }
    }

    private void prepareCirclePoint(){

        mCenterPoint = new Point (getMeasuredWidth ()/2,getMeasuredHeight ()/2);

        mImagePoints = new Point[DEFAULT_CHILDREN_COUNT];

        mImagePoints[0] = new Point (mCenterPoint.x,mCenterPoint.y-DEFAULT_CIRCLE_DISTANCE);

        mImagePoints[1] = new Point ((int)(mCenterPoint.x-Math.sin (36)*DEFAULT_CIRCLE_DISTANCE),
                (int)(mCenterPoint.y+Math.cos (36)*DEFAULT_CIRCLE_DISTANCE));

        mImagePoints[2] = new Point ((int)(mCenterPoint.x-Math.sin (54)*DEFAULT_CIRCLE_DISTANCE),
                (int)(mCenterPoint.y-Math.cos (72)*DEFAULT_CIRCLE_DISTANCE));


        mImagePoints[3] = new Point ((int)(mCenterPoint.x+Math.sin (54)*DEFAULT_CIRCLE_DISTANCE),
                (int)(mCenterPoint.y-Math.cos (72)*DEFAULT_CIRCLE_DISTANCE));


        mImagePoints[4] = new Point ((int)(mCenterPoint.x+Math.sin (36)*DEFAULT_CIRCLE_DISTANCE),
                (int)(mCenterPoint.y+Math.cos (36)*DEFAULT_CIRCLE_DISTANCE));
    }

    private void hideAllCircle(){
        for (int i = 0; i < DEFAULT_CHILDREN_COUNT ; i++) {
            mImageViews[i].setVisibility (View.INVISIBLE);
        }
    }

    private void showAllCircle(){
        for (int i = 0; i < DEFAULT_CHILDREN_COUNT ; i++) {
            mImageViews[i].setVisibility (View.VISIBLE);
        }
    }

    private void readyCircleAnimation(){
        if(animation == null){
            ObjectAnimator one = ObjectAnimator.ofFloat (mImageViews[0],"translationX",mCenterPoint.x,
                    mImagePoints[0].x);
            one.setDuration (DEFAULT_CIRCLE_DURATION) ;
            ObjectAnimator two = ObjectAnimator.ofFloat (mImageViews[0],"translationY",mCenterPoint.y,
                    mImagePoints[0].y);
            two.setDuration (DEFAULT_CIRCLE_DURATION) ;

            ObjectAnimator three = ObjectAnimator.ofFloat (mImageViews[1],"translationX",mCenterPoint.x,
                    mImagePoints[1].x);
            three.setDuration (DEFAULT_CIRCLE_DURATION) ;
            three.setStartDelay (DEFAULT_DELAY);
            ObjectAnimator four = ObjectAnimator.ofFloat (mImageViews[1],"translationY",mCenterPoint.y,
                    mImagePoints[1].y);
            four.setDuration (DEFAULT_CIRCLE_DURATION) ;
            four.setStartDelay (DEFAULT_DELAY);

            ObjectAnimator five = ObjectAnimator.ofFloat (mImageViews[2],"translationX",mCenterPoint.x,
                    mImagePoints[2].x);
            five.setDuration (DEFAULT_CIRCLE_DURATION) ;
            five.setStartDelay (DEFAULT_DELAY*2);
            ObjectAnimator six = ObjectAnimator.ofFloat (mImageViews[2],"translationY",mCenterPoint.y,
                    mImagePoints[2].y);
            six.setDuration (DEFAULT_CIRCLE_DURATION) ;
            six.setStartDelay (DEFAULT_DELAY*2);



            ObjectAnimator seven = ObjectAnimator.ofFloat (mImageViews[3],"translationX",mCenterPoint.x,
                    mImagePoints[3].x);
            seven.setDuration (DEFAULT_CIRCLE_DURATION) ;
            seven.setStartDelay (DEFAULT_DELAY*3);
            ObjectAnimator eight = ObjectAnimator.ofFloat (mImageViews[3],"translationY",mCenterPoint.y,
                    mImagePoints[3].y);
            eight.setDuration (DEFAULT_CIRCLE_DURATION) ;
            eight.setStartDelay (DEFAULT_DELAY*3);

            ObjectAnimator ten = ObjectAnimator.ofFloat (mImageViews[4],"translationX",mCenterPoint.x,
                    mImagePoints[4].x);
            ten.setDuration (DEFAULT_CIRCLE_DURATION);
            ten.setStartDelay (DEFAULT_DELAY*4);
            ObjectAnimator ten1 = ObjectAnimator.ofFloat (mImageViews[4],"translationY",mCenterPoint.y,
                    mImagePoints[4].y);
            ten1.setDuration (DEFAULT_CIRCLE_DURATION) ;
            ten1.setStartDelay (DEFAULT_DELAY * 4);
            animation = new AnimatorSet ();
            animation.addListener (new Animator.AnimatorListener () {
                @Override
                public void onAnimationStart (Animator animation) {
                }
                @Override
                public void onAnimationEnd (Animator animation) {
                    if(mFloatButton!=null){
                        mFloatButton.setClickable (true);
                        mFloatButton.setEnabled (true);
                    }
                }
                @Override
                public void onAnimationCancel (Animator animation) {
                }
                @Override
                public void onAnimationRepeat (Animator animation) {

                }
            });
            ((AnimatorSet) animation).playTogether (one, two, three, four, five, six, seven, eight, ten, ten1);
        }
    }


    private void readyCircleBackAnimation( Animator.AnimatorListener listener){
        if(mBackAnimation == null){
            ObjectAnimator one = ObjectAnimator.ofFloat (mImageViews[0],"translationX",
                    mImagePoints[0].x,mCenterPoint.x);
            one.setDuration (DEFAULT_CIRCLE_DURATION) ;
            ObjectAnimator two = ObjectAnimator.ofFloat (mImageViews[0],"translationY",
                    mImagePoints[0].y,mCenterPoint.y);
            two.setDuration (DEFAULT_CIRCLE_DURATION) ;

            ObjectAnimator three = ObjectAnimator.ofFloat (mImageViews[1],"translationX",
                    mImagePoints[1].x,mCenterPoint.x);
            three.setDuration (DEFAULT_CIRCLE_DURATION) ;
            three.setStartDelay (DEFAULT_DELAY);
            ObjectAnimator four = ObjectAnimator.ofFloat (mImageViews[1],"translationY",
                    mImagePoints[1].y,mCenterPoint.y);
            four.setDuration (DEFAULT_CIRCLE_DURATION) ;
            four.setStartDelay (DEFAULT_DELAY);

            ObjectAnimator five = ObjectAnimator.ofFloat (mImageViews[2],"translationX",
                    mImagePoints[2].x,mCenterPoint.x);
            five.setDuration (DEFAULT_CIRCLE_DURATION) ;
            five.setStartDelay (DEFAULT_DELAY*2);
            ObjectAnimator six = ObjectAnimator.ofFloat (mImageViews[2],"translationY",
                    mImagePoints[2].y,mCenterPoint.y);
            six.setDuration (DEFAULT_CIRCLE_DURATION) ;
            six.setStartDelay (DEFAULT_DELAY*2);

            ObjectAnimator seven = ObjectAnimator.ofFloat (mImageViews[3],"translationX",
                    mImagePoints[3].x,mCenterPoint.x);
            seven.setDuration (DEFAULT_CIRCLE_DURATION) ;
            seven.setStartDelay (DEFAULT_DELAY*3);
            ObjectAnimator eight = ObjectAnimator.ofFloat (mImageViews[3],"translationY",
                    mImagePoints[3].y,mCenterPoint.y);
            eight.setDuration (DEFAULT_CIRCLE_DURATION) ;
            eight.setStartDelay (DEFAULT_DELAY*3);


            ObjectAnimator ten = ObjectAnimator.ofFloat (mImageViews[4],"translationX",
                    mImagePoints[4].x,mCenterPoint.x);
            ten.setDuration (DEFAULT_CIRCLE_DURATION);
            ten.setStartDelay (DEFAULT_DELAY*4);
            ObjectAnimator ten1 = ObjectAnimator.ofFloat (mImageViews[4],"translationY",
                    mImagePoints[4].y,mCenterPoint.y);
            ten1.setDuration (DEFAULT_CIRCLE_DURATION) ;
            ten1.setStartDelay (DEFAULT_DELAY * 4);
            mBackAnimation = new AnimatorSet ();
            if(listener!=null){
                mBackAnimation.addListener (listener);
            }
            ((AnimatorSet) mBackAnimation).playTogether(one,two,three,four,five,six,seven,eight,ten,ten1);
        }
    }

    private void startAnimation(){
        if(animation !=null)
            animation.start ();
    }

    private void startBackAnimation(){
        if(mBackAnimation != null)
            mBackAnimation.start ();
    }
}
