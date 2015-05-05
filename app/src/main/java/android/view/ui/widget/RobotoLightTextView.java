package android.view.ui.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.common.TypeFaces;
import android.widget.TextView;


/**
 * Created by wiki on 14-11-19.
 */
public class RobotoLightTextView extends TextView {

    public RobotoLightTextView (Context context) {
        super(context);
        setTypeface (null,0);
    }

    public RobotoLightTextView (Context context, AttributeSet attrs) {
        super (context, attrs);
        setTypeface (null,0);
    }

    public RobotoLightTextView (Context context, AttributeSet attrs, int defStyle) {
        super (context, attrs, defStyle);
        setTypeface (null,0);
    }

    @Override
    public void setTypeface (Typeface tf, int style) {
        super.setTypeface (TypeFaces.get (getContext (), "fonts/Roboto-Light.ttf"), style);
    }

    @Override
    protected void onDetachedFromWindow () {
        super.onDetachedFromWindow ();
    }
}
