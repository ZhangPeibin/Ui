package android.view.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.R;
import android.view.View;
import android.view.ViewGroup;
import android.view.ui.MainActivity;
import android.view.ui.widget.AnimatedExpandableListView;
import android.view.ui.widget.AnimatedExpandableListView.AnimatedExpandableListAdapter;
import android.view.ui.widget.NavigationFooter;
import android.view.ui.widget.NavigationTextView;
import android.view.ui.widget.RobotoRegularTextView;
import android.view.utils.VersionUtil;
import android.widget.ExpandableListView;
import android.widget.ImageView;

public class NavigationFragment extends Fragment {


    private AnimatedExpandableListView mExpandableListView;
    private NavigationAdapter mNavigationAdapter;
    private ImageView mNavigationStatusBar;
    private View mRootView;
    private View mHeaderView;
    private View mFooterView;
    private View mGroupShouldFocusView;
    private LayoutInflater mLayoutInflater;
    private NavigationFooter mNavigationFooter;

    private String [] mNavigationGroups;
    private String [][] mNavigationChildrens;

    private String [] mMaterialDesign;
    private String [] mWhatIsMaterial;
    private String [] mAnimation;
    private String [] mStyle;
    private String [] mLayout;
    private String [] mComponents;
    private String [] mPatterns;
    private String [] mUsability;
    private String [] mResources;
    private String [] mWhatIsNew;

    public NavigationFragment () {
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        mRootView = inflater.inflate (R.layout.fragment_nav, container, false);
        mNavigationStatusBar = (ImageView) mRootView.findViewById (R.id.navigation_status_bar);
        mExpandableListView = (AnimatedExpandableListView) mRootView.findViewById (R.id.navigation_listview);
        if(!VersionUtil.hasLolipop ()){
            mNavigationStatusBar.setVisibility (View.GONE);
        }

        initNavigationItem ();

        mNavigationAdapter = new NavigationAdapter (getActivity ().getApplicationContext ());
        mHeaderView = mLayoutInflater.inflate (R.layout.navigation_logo,mExpandableListView,false);
        mFooterView = mLayoutInflater.inflate (R.layout.navigation_footer,mExpandableListView,false);
        mNavigationFooter = (NavigationFooter) mFooterView.findViewById (R.id.footer_text);
        mExpandableListView.addHeaderView (mHeaderView);
        mExpandableListView.addFooterView (mFooterView);
        mExpandableListView.setAdapter (mNavigationAdapter);
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener () {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if(mGroupShouldFocusView!=null){
                    mGroupShouldFocusView.setBackgroundColor (getResources ().getColor (R.color.navigation_background));
                    mGroupShouldFocusView = null;
                }
                mGroupShouldFocusView = v;
                v.setBackgroundColor (getResources ().getColor (R.color.navigation_background_press));
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                if (mExpandableListView.isGroupExpanded(groupPosition)) {//展开
                    mExpandableListView.collapseGroupWithAnimation(groupPosition);//回收
                } else {
                    mExpandableListView.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });

        mExpandableListView.setOnChildClickListener (new ExpandableListView.OnChildClickListener () {
            @Override
            public boolean onChildClick (ExpandableListView parent, View v,
                                         int groupPosition, int childPosition, long id) {
                ((MainActivity)getActivity ()).updateUIColorWhenNaviItemClick(groupPosition,
                        mNavigationStatusBar,
                        mNavigationFooter);


                if(childPosition == 0){
                    ((MainActivity)getActivity ()).updateToolbarToExtendToolbar (mNavigationGroups[groupPosition]);
                }else{
                    ((MainActivity)getActivity ()).updateExtendToolbarToToolbar (
                            mNavigationChildrens[groupPosition][childPosition]
                            );
                }

                return false;
            }
        });
        return mRootView;
    }


    private void initNavigationItem(){
        mNavigationGroups = getResources ().getStringArray (R.array.navigation_group);

        mMaterialDesign = getResources ().getStringArray (R.array.materialdesign);
        mWhatIsMaterial = getResources ().getStringArray (R.array.whatismaterial);
        mAnimation = getResources ().getStringArray (R.array.animation);
        mStyle = getResources ().getStringArray (R.array.style);
        mLayout = getResources ().getStringArray (R.array.layout);
        mComponents = getResources ().getStringArray (R.array.components);
        mPatterns = getResources ().getStringArray (R.array.patterns);
        mUsability = getResources ().getStringArray (R.array.usability);
        mResources = getResources ().getStringArray (R.array.resources);
        mWhatIsNew = getResources ().getStringArray (R.array.whatisnew);

        mNavigationChildrens = new String[][]{
                mMaterialDesign,
                mWhatIsMaterial,
                mAnimation,
                mStyle,
                mLayout,
                mComponents,
                mPatterns,
                mUsability,
                mResources,
                mWhatIsNew
        };
    }


    private class NavigationAdapter extends AnimatedExpandableListAdapter {

        private GroupHolder mGroupHolder;
        private ChildrenHolder mChildrenHolder;

        public NavigationAdapter(Context context){
            mLayoutInflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getGroupCount () {
            return mNavigationGroups.length;
        }

        @Override
        public int getRealChildrenCount (int groupPosition) {
            return mNavigationChildrens[groupPosition].length;
        }

        @Override
        public Object getGroup (int groupPosition) {
            return mNavigationGroups[groupPosition];
        }

        @Override
        public Object getChild (int groupPosition, int childPosition) {
            return mNavigationChildrens[groupPosition][childPosition];
        }

        @Override
        public long getGroupId (int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId (int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getGroupView (int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null){
                mGroupHolder = new GroupHolder ();
                convertView  = mLayoutInflater.inflate (R.layout.navi_group_view,parent,false);
                mGroupHolder.mTextView = (NavigationTextView) convertView.findViewById (R.id.item_title);
                convertView.setTag (mGroupHolder);
            }else{
                mGroupHolder = (GroupHolder) convertView.getTag ();
            }
            mGroupHolder.mTextView.setText (mNavigationGroups[groupPosition]);
            return convertView;
        }

        @Override
        public View getRealChildView (int groupPosition, int childPosition, boolean isLastChild,
                                  View convertView, ViewGroup parent) {
            if(convertView == null){
                mChildrenHolder = new ChildrenHolder ();
                convertView  = mLayoutInflater.inflate (R.layout.navi_childern_view,parent,false);
                mChildrenHolder.mTextView = (RobotoRegularTextView) convertView.findViewById (R.id.item_children_title);
                convertView.setTag (mChildrenHolder);
            }else{
                mChildrenHolder = (ChildrenHolder) convertView.getTag ();
            }
            mChildrenHolder.mTextView.setText (mNavigationChildrens[groupPosition][childPosition]);
            return convertView;
        }

        @Override
        public boolean hasStableIds () {
            return true;
        }

        @Override
        public boolean isChildSelectable (int groupPosition, int childPosition) {
            return true;
        }


        private class GroupHolder{
            NavigationTextView mTextView;
        }

        private class ChildrenHolder{
            RobotoRegularTextView mTextView;
        }
    }

}
