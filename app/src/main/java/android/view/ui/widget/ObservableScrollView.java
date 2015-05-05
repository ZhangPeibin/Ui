package android.view.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import java.util.ArrayList;

/**
 * Created by wiki on 15-3-6.
 */
public class ObservableScrollView extends ScrollView {

    private ArrayList<ObservedScrollChanged> mObservedScrollChangeds = new ArrayList<> ();

    public ObservableScrollView (Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged (int l, int t, int oldl, int oldt) {
        super.onScrollChanged (l, t, oldl, oldt);

        for (ObservedScrollChanged o : mObservedScrollChangeds){
            o.onScrollChanged (l-oldl,t-oldt);
        }

    }

    public void addObservable(ObservedScrollChanged observedScrollChanged){
        if(observedScrollChanged!=null){
            mObservedScrollChangeds.add (observedScrollChanged);
        }
    }

    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    public interface ObservedScrollChanged{
        void onScrollChanged(int l , int t);
    }


}
