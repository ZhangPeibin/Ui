package android.view.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.View;

/**
 * Created by wiki on 15-2-6.
 */
public class UIUtils {
    public static int calculateDpToPx (Context context, float padding_in_dp) {
        final float scale = context.getResources ().getDisplayMetrics ().density;
        return (int) (padding_in_dp * scale + 0.5f);
    }

    public static int calculatePxToDp (Context context, float pxValue) {
        final float scale = context.getResources ().getDisplayMetrics ().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static Point getSrcViewPointAtDestView(View mSrcView,View mDestView){
        int [] srcPoint = new int[2];
        int [] destPoint = new int[2];
        mSrcView.getLocationOnScreen (srcPoint);
        mDestView.getLocationOnScreen (destPoint);

        return new Point ( srcPoint[0] - destPoint[0] + mSrcView.getWidth ()/2,
                srcPoint[1] - destPoint[1]+mSrcView.getHeight ()/2);
    }


}
