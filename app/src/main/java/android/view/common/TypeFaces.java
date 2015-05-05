package android.view.common;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Created by wiki on 14-11-19.
 */
public class TypeFaces {
    private static final Hashtable<String , Typeface> typefaceSpanHashtable = new Hashtable<String, Typeface> ();

    public static Typeface get(Context context , String fontPath){
        synchronized (typefaceSpanHashtable){
            if(!typefaceSpanHashtable.containsKey (fontPath)){
                Typeface typeface =Typeface.createFromAsset(context.getAssets (),fontPath);
                typefaceSpanHashtable.put (fontPath,typeface);
                return typeface;
            }
            return typefaceSpanHashtable.get (fontPath);
        }
    }
}
