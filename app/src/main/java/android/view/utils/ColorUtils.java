package android.view.utils;

import android.graphics.Color;

/**
 * Created by wiki on 15-2-6.
 */
public class ColorUtils {

    /**
     * get the press color from backgroundColor
     * @param normalColor
     * @return press backgroundColor
     */
    public static int makePressColor(int normalColor){
        int r = (normalColor >> 16) & 0xFF;
        int g = (normalColor >> 8 ) & 0xFF;
        int b = (normalColor >> 0) & 0xFF;
        r = (r-30<0)?0:r-30;
        g = (g-30<0)?0:g-30;
        b = (b-30<0)?0:b-30;
        return Color.rgb (r,g,b);
    }
}
