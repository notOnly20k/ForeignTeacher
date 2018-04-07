package cn.sinata.xldutils.activitys;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import cn.sinata.xldutils.R;

public abstract class TabLayoutActivity extends TitleActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    protected abstract Fragment getFragment(int position);
    protected abstract String[] getTitles();
    private String[] titles ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_tab_layout);
        initView();
    }

    protected void initView() {

        mViewPager = bind(R.id.mViewPager);
        mTabLayout = bind(R.id.mTabLayout);

        titles = getTitles();
        if (titles!=null && titles.length>0){
            mViewPager.setAdapter(pagerAdapter);
            mTabLayout.setupWithViewPager(mViewPager);
            mViewPager.setOffscreenPageLimit(Math.min(3,titles.length));
        }
    }

    private FragmentStatePagerAdapter pagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

        @Override
        public Fragment getItem(int position) {
            return getFragment(position);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    };

    protected void setTabMode(int tabMode){
        mTabLayout.setTabMode(tabMode);
    }

    public void resetTitles(){

    }

}
