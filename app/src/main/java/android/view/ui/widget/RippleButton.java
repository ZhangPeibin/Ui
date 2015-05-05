package android.view.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.R;
import android.view.utils.AnimationCollections;
import android.view.utils.ColorUtils;
import android.view.utils.Logger;
import android.view.utils.UIUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;



/**
 * Created by wiki on 15-1-30.
 */
public class RippleButton extends RelativeLayout {

    private int mBackgroundColor =-1;
    private Drawable mBackgroundDrawble = null;
    private int mBackgroundRippleColor = -1;

    private int mRippleRadius = -1;
    private float mTouchX = -1f;
    private float mTouchY = -1f;
    private int mRippleSize = 3;
    private float mRippleSpeed = 12f;

    private float mScaleFrom = 1.0f;
    private float mScaleTo = 0.96f;
    private int mScaleDuration = 200;

    private int mTextColor = Color.parseColor ("#ffffff");
    private int mTextSize = 16;

    private boolean mIsAnimation = false;

    private String mTextString;
    private TypedArray mTypeArray;
    private TextView mTextView;
    private LayoutParams mLayoutParams;

    public RippleButton (Context context, AttributeSet attrs) {
        super (context, attrs);
        setAttributeSet (context, attrs);
        setDefaultProperties ();
    }

    private void setDefaultProperties () {
        super.setMinimumWidth (80);
        super.setMinimumHeight (48);
       // super.setBackgroundColor (mBackgroundColor);
        super.setEnabled (true);
        super.setClickable (true);
        super.setLongClickable (true);
    }

    /**
     * 自定义属性
     *
     * @param attributeSet
     */
    @TargetApi (Build.VERSION_CODES.JELLY_BEAN)
    private void setAttributeSet (Context context, AttributeSet attributeSet) {
        mTypeArray = context.obtainStyledAttributes (attributeSet, R.styleable.ripple);
        if (mTypeArray != null) {
            mRippleSize = mTypeArray.getInteger (R.styleable.ripple_ripple_size, mRippleSize);

            mRippleSpeed = mTypeArray.getFloat (R.styleable.ripple_ripple_speed,mRippleSpeed);

            mTextString = mTypeArray.getString (R.styleable.ripple_text_string);
            mTextColor = mTypeArray.getColor (R.styleable.ripple_text_color, Color.parseColor ("#ffffff"));
            mTextSize = mTypeArray.getDimensionPixelSize (R.styleable.ripple_text_size, mTextSize);
            mBackgroundColor = mTypeArray.getColor (R.styleable.ripple_bg_color,-1);
            mBackgroundDrawble = mTypeArray.getDrawable (R.styleable.ripple_bg_drawable);
            mBackgroundRippleColor = mTypeArray.getColor (R.styleable.ripple_ripple_color,-1);
            mIsAnimation = mTypeArray.getBoolean (R.styleable.ripple_is_animation_is,false);
        }


        if(mTextString!=null){
            mTextView = new TextView (context);
            mTextView.setTextColor (mTextColor);
            mTextView.setTextSize (TypedValue.COMPLEX_UNIT_PX,mTextSize);
            mTextView.setText (mTextString);
            mLayoutParams = new LayoutParams (LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            mLayoutParams.addRule (RelativeLayout.CENTER_IN_PARENT);
            mTextView.setLayoutParams (mLayoutParams);
            addView (mTextView);
        }

        if(mBackgroundColor!=-1){
            setBackgroundColor (mBackgroundColor);
        }else{
            if(mBackgroundDrawble !=null){
                if(mBackgroundRippleColor == -1){
                    throw new IllegalStateException ("you must set rippleColor when you set RippleBackgroundDrawable");
                }

                setBackground (mBackgroundDrawble);
            }
        }

        mTypeArray.recycle ();
    }



    @Override
    public boolean onTouchEvent (MotionEvent event) {
        if (isEnabled ()) {
            invalidate ();
            switch (event.getAction ()) {
                case MotionEvent.ACTION_DOWN:
                    if(mIsAnimation){
                        AnimationCollections.scaleRippleButton (this, null, mScaleFrom, mScaleTo, mScaleDuration);
                    }
                    mTouchX = event.getX ();
                    mTouchY = event.getY ();
                    mRippleRadius = getHeight () / mRippleSize;
                    break;
                case MotionEvent.ACTION_MOVE:
                    mTouchX = event.getX ();
                    mTouchY = event.getY ();
                    mRippleRadius = getHeight () / mRippleSize;
                    if (!(event.getX () <= getWidth () && event.getX () >= 0)
                            && !(event.getY () <= getHeight () && event.getY () >= 0)) {
                        mTouchX = -1;
                        mTouchY = -1;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if ((event.getX () <= getWidth () && event.getX () >= 0)
                            && (event.getY () <= getHeight () && event.getY () >= 0)) {
                        mRippleRadius++;
                    } else {
                        mTouchX = -1;
                        mTouchY = -1;
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    if (!(event.getX () <= getWidth () && event.getX () >= 0) || !(event.getY () <= getHeight () && event.getY () >= 0)) {
                        mTouchX = -1;
                        mTouchY = -1;
                    }
                    break;
            }
        }
        return true;
    }


    public Bitmap makeCircle () {
        Bitmap output = Bitmap.createBitmap (
                getWidth () - UIUtils.calculateDpToPx (getContext (), 6), getHeight ()
                        - UIUtils.calculateDpToPx (getContext (), 7), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas (output);
        canvas.drawARGB (0, 0, 0, 0);
        Paint paint = new Paint ();
        paint.setAntiAlias (true);
        paint.setColor (mBackgroundRippleColor == -1?
                ColorUtils.makePressColor (this.mBackgroundColor):
                mBackgroundRippleColor);

        canvas.drawCircle (mTouchX, mTouchY, mRippleRadius, paint);

        if (mRippleRadius > getHeight () / mRippleSize)
            mRippleRadius += mRippleSpeed;
        if (mRippleRadius >= getWidth ()) {
            mTouchX = -1;
            mTouchY = -1;
            mRippleRadius = getHeight () / mRippleSize;
            performClick ();
        }
        return output;
    }


    @Override
    protected void onFocusChanged (boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged (gainFocus, direction, previouslyFocusedRect);
        if (!gainFocus) {
            mTouchX = -1;
            mTouchY = -1;
        }
    }

    /**
     * 是否拦截事件
     *
     * @param ev
     *
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent (MotionEvent ev) {
        return true;
    }

    @Override
    protected void onDraw (Canvas canvas) {
        super.onDraw (canvas);
        if (mTouchX != -1) {
            Rect src = new Rect (0, 0,
                    getWidth (),
                    getHeight ());
            Rect dst = new Rect (UIUtils.calculateDpToPx (getContext (), 1),
                    UIUtils.calculateDpToPx (getContext (), 1),
                    getWidth (),
                    getHeight ());
            canvas.drawBitmap (makeCircle (), src, dst, null);
            invalidate ();
        }
    }

    @TargetApi (Build.VERSION_CODES.JELLY_BEAN)
    public void setmBackgroundDrawble(Drawable drawble){
       setBackground (drawble);
    }

    public void setTextSize(int textSpSize){
        if(mTextView!=null){
           mTextView.setTextSize (TypedValue.COMPLEX_UNIT_SP,textSpSize);
        }
    }

    public void setTextColor(int textColor){
        if(mTextView!=null){
            mTextView.setTextColor (textColor);
        }
    }

    public void setRippleColor(int rippleColor){
        this.mBackgroundRippleColor = rippleColor;
    }

    @Override
    protected void onAttachedToWindow () {
        super.onAttachedToWindow ();
    }

    @Override
    protected void onDetachedFromWindow () {
        super.onDetachedFromWindow ();
        if(mTextView != null){
            mTextView = null;
        }
    }
}
