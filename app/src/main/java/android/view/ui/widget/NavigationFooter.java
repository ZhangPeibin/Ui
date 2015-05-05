package android.view.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.R;
import android.widget.LinearLayout;

/**
 * Created by wiki on 15-3-8.
 */
public class NavigationFooter extends LinearLayout {

    private int mColorPrimary = -1;

    private RobotoRegularTextView mPrivacyText;
    private RobotoRegularTextView mCharText;
    private RobotoRegularTextView mTermsText;

    public NavigationFooter (Context context) {
        super (context);
        init (context);
    }

    public NavigationFooter (Context context, AttributeSet attrs) {
        super (context, attrs);
        init (context);
    }

    public NavigationFooter (Context context, AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        init (context);
    }

    private void init(Context context){
        this.setOrientation (LinearLayout.HORIZONTAL);
        mPrivacyText = new RobotoRegularTextView (context);
        mPrivacyText.setTextSize (TypedValue.COMPLEX_UNIT_SP,14);
        mPrivacyText.setTextColor (context.getResources ().getColor (R.color.colorPrimary));
        mPrivacyText.setText (context.getResources ().getString (R.string.navigation_footer_privacy));


        mCharText = new RobotoRegularTextView (context);
        mCharText.setTextSize (TypedValue.COMPLEX_UNIT_SP,14);
        mCharText.setPadding (context.getResources ().getDimensionPixelOffset (R.dimen.navigation_footer_padding),
                0,
                context.getResources ().getDimensionPixelOffset (R.dimen.navigation_footer_padding),0);
        mCharText.setTextColor (context.getResources ().getColor (R.color.navigation_text_color));
        mCharText.setText (context.getResources ().getString (R.string.navigation_footer_char));

        mTermsText = new RobotoRegularTextView (context);
        mTermsText.setTextSize (TypedValue.COMPLEX_UNIT_SP,14);
        mTermsText.setTextColor (context.getResources ().getColor (R.color.colorPrimary));
        mTermsText.setText (context.getResources ().getString (R.string.navigation_footer_terms));

        addView (mPrivacyText);
        addView (mCharText);
        addView (mTermsText);
    }

    public void setTextColor(int colorPaimary){
        this.mColorPrimary = colorPaimary;
        mPrivacyText.setTextColor (colorPaimary);
        mTermsText.setTextColor (colorPaimary);
    }

    @Override
    protected void onDetachedFromWindow () {
        super.onDetachedFromWindow ();
        mPrivacyText = null;
        mPrivacyText =null;
        mTermsText = null;
    }
}
