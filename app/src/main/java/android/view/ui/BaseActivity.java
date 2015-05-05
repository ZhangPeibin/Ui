package android.view.ui;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.R;
import android.view.View;
import android.view.ViewGroup;
import android.view.ui.fragment.NavigationFragment;
import android.view.ui.widget.NavigationFooter;
import android.view.utils.VersionUtil;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseActivity extends ActionBarActivity{


    private boolean mIsToolbarExtended = false;

    private DrawerLayout mDrawerLayout;

    protected int mColorPrimary = -1;
    protected int mColorPrimaryDark = -1;


    private NavigationFragment mNavigationFragment;

    private TypedValue mTypedValue;

    private TextView mToolbarTextView;

    private Toolbar mToolbar;
    private ViewGroup.LayoutParams mToolbarLayoutParams;

    private ActionBarDrawerToggle mDrawerToggle;

    protected  int [][] mGroupColorsKinds = new int[][]{
      new int[]{1,2,9,10},
            new int[]{3},
            new int[]{4},
            new int[]{5},
            new int[]{6},
            new int[]{7},
            new int[]{8}
    };

    protected  int[][] mGroupColors = new int[][]{
            new int[]{R.color.colorPrimary,
                   R.color.colorPrimaryDark},
            new int[]{R.color.colorPurple,
                      R.color.colorPurpleDark},
            new int[]{R.color.colorTeal,
                    R.color.colorTealDark},
            new int[]{R.color.colorPink,
                    R.color.colorPinkDark},
            new int[]{R.color.colorIndigo,
                    R.color.colorIndigoDark},
            new int[]{R.color.colorOrange,
                    R.color.colorOrangeDark},
            new int[]{R.color.colorBlueGrey,
                    R.color.colorBlueGreyDark},

    };

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }

    @Override
    public void setContentView (int layoutResID) {
        super.setContentView (layoutResID);
        getToolActionBar ();
        setUpNavLayout ();
    }

    protected Toolbar getToolActionBar(){
        if(mToolbar == null){
            mToolbar = (Toolbar) findViewById (R.id.toolbarinclude);
            if(mToolbar!=null){
                setSupportActionBar (mToolbar);
                setupNavDrawer ();
            }
        }
        return mToolbar;
    }

    protected  void tryToSetActionBar(){
    }


    protected void setupNavDrawer() {
        // What nav drawer item should be selected?
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout == null) {
            return;
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,R.string.open,R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened (drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed (drawerView);
            }
        };

        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener (mDrawerToggle);
        mDrawerLayout.setStatusBarBackgroundColor (getResources ().getColor (R.color.colorPrimaryDark));
    }

    protected void setUpNavLayout(){
        if(mNavigationFragment == null){
            FragmentManager fragmentManager = getFragmentManager ();
            mNavigationFragment = new NavigationFragment ();
            fragmentManager.beginTransaction ().add (R.id.lv_left_fragment,mNavigationFragment).commit ();
        }
    }

    protected void asyncDrawerLayoutState(){
        if(mDrawerLayout!=null){
            if(mDrawerLayout.isDrawerOpen (Gravity.LEFT)) {
                mDrawerLayout.closeDrawer (Gravity.LEFT);
                mDrawerToggle.syncState ();
            }
        }
    }

    protected DrawerLayout getmDrawerLayout(){
        return  mDrawerLayout;
    }

    protected void setStatusBarColor(int stutasBarColor){
        if(mDrawerToggle != null && mDrawerLayout!=null){
            mDrawerLayout.setStatusBarBackgroundColor (stutasBarColor);
            mDrawerLayout.invalidate ();
        }
    }


    public  void updateUIColorWhenNaviItemClick(int groupPosition,
                                                ImageView navigationStatusBar,
                                                NavigationFooter navigationFooter){
        asyncDrawerLayoutState();

        int position = 0;
        int [] mGroupColorsKindsItem ;
        for (int i = 0; i < mGroupColorsKinds.length; i++) {
            mGroupColorsKindsItem = mGroupColorsKinds[i];
            for (int j = 0; j < mGroupColorsKindsItem.length; j++) {
                if(groupPosition +1 == mGroupColorsKindsItem[j]){
                    position = i;
                    break;
                }
            }
        }

        if(position < 0 || position > mGroupColors.length-1){
            position = 0 ;
        }

        int [] mGroupColor = mGroupColors[position];

        updateToolbarColor (getResources ().getColor (mGroupColor[0]),getResources ().getColor (mGroupColor[1]));

        if(VersionUtil.hasLolipop ()){
            navigationStatusBar.setBackgroundColor (getResources ().getColor (mGroupColor[0]));
        }

        navigationFooter.setTextColor (getResources ().getColor (mGroupColor[0]));
    }

    //update toolbar attr by user interact
    protected void updateToolbarColor(int colorPrimary,int colorPrimaryDark){
        if(mToolbar!=null){
            this.mColorPrimary = colorPrimary;
            this.mColorPrimaryDark = colorPrimaryDark;
            mToolbar.setBackgroundColor (colorPrimary);
            setStatusBarColor (colorPrimaryDark);
        }
    }

    public  void updateToolbarToExtendToolbar(String mGroupName){
        if(mToolbar!=null){
            if(mIsToolbarExtended){
                //if now is extended , we just refresh it's name
                mToolbarTextView.setText (mGroupName);
                return;
            }
            mToolbarLayoutParams = mToolbar.getLayoutParams ();
            mToolbarLayoutParams.height = getResources ().getDimensionPixelOffset (R.dimen.toolbar_extend_height);
            mToolbar.setLayoutParams (mToolbarLayoutParams);
            mToolbar.setTitle ("");
            mToolbar.addView (createToolbarTitleTextView (mGroupName));
            mIsToolbarExtended = true;
        }
    }

    public  void updateExtendToolbarToToolbar(String mChildName){
        if(mToolbar!=null){
            if(!mIsToolbarExtended){
                mToolbar.setTitle (mChildName);
                return;
            }
            mToolbarLayoutParams = mToolbar.getLayoutParams ();
            mTypedValue = new TypedValue();
            if (getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.actionBarSize, mTypedValue, true))
            {
                mToolbarLayoutParams.height  = TypedValue.complexToDimensionPixelSize(mTypedValue.data,getResources().getDisplayMetrics());
            }
            mToolbar.setLayoutParams (mToolbarLayoutParams);
            mToolbar.setTitle (mChildName);
            if(mToolbar.findViewWithTag ("mToolbarTextView") != null){
                mToolbar.removeView (mToolbar.findViewWithTag ("mToolbarTextView"));
            }
            mIsToolbarExtended = false;
        }
    }


    private TextView createToolbarTitleTextView(String titleText){
        if(mToolbarTextView == null){
            mToolbarTextView  = new TextView (getApplicationContext ());
            mToolbarTextView.setText (titleText);
            mToolbarTextView.setTextColor (getResources ().getColor (R.color.text_white));
            mToolbarTextView.setTextSize (TypedValue.COMPLEX_UNIT_SP, 24);
            mToolbarTextView.setPadding (0,
                    getResources ().getDimensionPixelOffset (R.dimen.toolbar_extend_title_padding_top)
                    ,0,0);
            mToolbarTextView.setTag ("mToolbarTextView");
        }else{
            mToolbarTextView.setText (titleText);
        }
        return mToolbarTextView;
    }


    protected void checkVersion4Toolbar(){
        if(!VersionUtil.hasLolipop ()){
            mToolbar.setTitleTextColor (getResources ().getColor (R.color.textColor));
            mToolbar.setBackgroundColor (getResources ().getColor (R.color.colorPrimary));
        }
    }

}
