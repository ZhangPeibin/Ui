package android.view.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.R;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by wiki on 15-3-8.
 */
public class ExtendToolbar extends LinearLayout {


    private RobotoRegularTextView mGroupTextView;
    private ImageView mRightImageView;
    private RobotoRegularTextView mChildTextView;

    public ExtendToolbar (Context context) {
        super (context);
        init (context);
    }

    public ExtendToolbar (Context context, AttributeSet attrs) {
        super (context, attrs);
        init (context);
    }

    public ExtendToolbar (Context context, AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        init (context);
    }

    @TargetApi (Build.VERSION_CODES.JELLY_BEAN)
    private void init(Context context){
        this.setOrientation (LinearLayout.HORIZONTAL);
        this.setGravity (Gravity.CENTER_VERTICAL);
        setBackgroundColor (context.getResources ().getColor (R.color.colorAccent));
        mGroupTextView = new RobotoRegularTextView (context);
        mGroupTextView.setTextSize (TypedValue.COMPLEX_UNIT_SP, 16);
        mGroupTextView.setPadding (0,0,context.getResources ().getDimensionPixelOffset (R.dimen.activity_horizontal_margin),
                0);
        mGroupTextView.setTextColor (context.getResources ().getColor (R.color.text_white));

        mRightImageView = new ImageView (context);
        mRightImageView.setBackground (context.getResources ().getDrawable (R.drawable.arrow_right));
        mRightImageView.setLayoutParams (new LayoutParams (context.getResources ().getDimensionPixelOffset (R.dimen.icon_size),
                context.getResources ().getDimensionPixelOffset (R.dimen.icon_size)
                ));

        mChildTextView = new RobotoRegularTextView (context);
        mChildTextView.setTextSize (TypedValue.COMPLEX_UNIT_SP, 16);
        mChildTextView.setPadding (context.getResources ().getDimensionPixelOffset (R.dimen.activity_horizontal_margin),
                0,0,0);
        mChildTextView.setTextColor (context.getResources ().getColor (R.color.text_white));

        addView (mGroupTextView);
        addView (mRightImageView);
        addView (mChildTextView);
    }
    
    public void setGroupAndChildText(String mGroupText,String mChildText){
        mGroupTextView.setText (mGroupText);
        mChildTextView.setText (mChildText);
    }
    
    @Override
    protected void onDetachedFromWindow () {
        super.onDetachedFromWindow ();
    }
}
