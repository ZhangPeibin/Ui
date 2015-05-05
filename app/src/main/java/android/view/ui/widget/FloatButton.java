package android.view.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.util.AttributeSet;
import android.view.R;
import android.view.ViewGroup;
import android.view.utils.ColorUtils;
import android.view.utils.UIUtils;
import android.view.utils.VersionUtil;
import android.widget.ImageButton;

/**
 * Floating action button
 * Created by wiki on 15-2-6.
 */
public class FloatButton extends ImageButton {

    private TypedArray mTypedArray;

    private static final int DEFAULT_SHADOW_SIZE = 12;//dp

    private int mCircleColor;
    private int mFloatButtonShadowSize = 12;

    private boolean mButtonSizeType = true;//false == min , true == normal
    private boolean mIsAnimation = false;
    private boolean mIsNeedShadow = true;

    private ShapeDrawable mShapeDrawable = null;



    public FloatButton (Context context, AttributeSet attrs) {
        super (context, attrs);
        setDefaultProperties (context);
        setAttrs (context, attrs);
        createCircleView (context);
    }

    private void setDefaultProperties (Context context) {
        super.setMinimumHeight (context.getResources ().getDimensionPixelSize (R.dimen.float_action_button_size));
        super.setMinimumWidth (context.getResources ().getDimensionPixelSize (R.dimen.float_action_button_size));
    }

    private void setAttrs (Context context, AttributeSet attributeSet) {
        mTypedArray = context.obtainStyledAttributes (attributeSet, R.styleable.float_btn);
        if (mTypedArray != null) {
            mButtonSizeType = mTypedArray.getBoolean (R.styleable.float_btn_size,true);
            mCircleColor = mTypedArray.getColor (R.styleable.float_btn_circle_color,
                    context.getResources ().getColor (R.color.colorAccent));
            mIsAnimation = mTypedArray.getBoolean (R.styleable.float_btn_is_animation, false);
            mFloatButtonShadowSize = mTypedArray.getDimensionPixelSize (R.styleable.float_btn_shadow_size,
                    UIUtils.calculateDpToPx (context,DEFAULT_SHADOW_SIZE));
        }
        mTypedArray.recycle ();
    }

    @TargetApi (Build.VERSION_CODES.LOLLIPOP)
    private void createCircleView (Context context) {
       updateBackground (context);
       setMarginsWithoutShadow ();
       setShadowForLolipop ();
    }

    private void updateBackground(Context context) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed}, createDrawable(context, ColorUtils.makePressColor (mCircleColor)));
        drawable.addState(new int[]{}, createDrawable(context,mCircleColor));
        setBackground (drawable);
    }

    private Drawable createDrawable (Context context,int mCircleColor) {
        mShapeDrawable = new ShapeDrawable (new OvalShape ());
        mShapeDrawable.getPaint ().setColor (mCircleColor);

        if(!mIsNeedShadow){
            return mShapeDrawable;
        }

        if (!VersionUtil.hasLolipop ()) {
            Drawable shadowDrawable = context.getResources ().getDrawable (R.drawable.shadow);
            LayerDrawable layerDrawable = new LayerDrawable (new Drawable[] {shadowDrawable, mShapeDrawable});
            layerDrawable.setLayerInset (1, mFloatButtonShadowSize, mFloatButtonShadowSize, mFloatButtonShadowSize, mFloatButtonShadowSize);
            return layerDrawable;
        } else {
            return mShapeDrawable;
        }
    }

    private void setMarginsWithoutShadow () {
        if (getLayoutParams () instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams ();
            int leftMargin = layoutParams.leftMargin - mFloatButtonShadowSize;
            int topMargin = layoutParams.topMargin - mFloatButtonShadowSize;
            int rightMargin = layoutParams.rightMargin - mFloatButtonShadowSize;
            int bottomMargin = layoutParams.bottomMargin - mFloatButtonShadowSize;
            layoutParams.setMargins (leftMargin, topMargin, rightMargin, bottomMargin);
            requestLayout ();
        }
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        int mFloatButtonSize = mButtonSizeType?getResources ().getDimensionPixelSize (R.dimen.float_action_button_size):
                getResources ().getDimensionPixelSize (R.dimen.float_action_small_button_size);
        if(!VersionUtil.hasLolipop ()){
            mFloatButtonSize+=mFloatButtonShadowSize*2;
        }
        setMeasuredDimension (mFloatButtonSize, mFloatButtonSize);
    }

    public void updateFloatBackGround(){
        mIsNeedShadow = false;
        updateBackground (getContext ().getApplicationContext ());
        setShadowForLolipop ();
    }

    public void updateShadowBackGround(){
        mIsNeedShadow = true;
        updateBackground (getContext ().getApplicationContext ());
        setShadowForLolipop();
    }

    private void setShadowForLolipop(){
        if(VersionUtil.hasLolipop ()){
            if(mIsNeedShadow){
                setElevation (getResources ().getDimension (R.dimen.float_action_button_elevation));
                setTranslationZ (getResources ().getDimension (R.dimen.float_action_button_tanslatez));
            }else{
                setElevation (0);
                setTranslationZ (0);
            }
        }
    }
}
