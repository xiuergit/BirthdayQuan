package com.bigdata.marketsdk.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.bigdata.marketsdk.Adapter.MyViewPagerAdapter;
import com.bigdata.marketsdk.R;
import com.bigdata.marketsdk.base.BaseActivity;
import com.bigdata.marketsdk.fragment.HomeFragment;
import com.bigdata.marketsdk.fragment.MoreFragment;
import com.bigdata.marketsdk.fragment.SubFragmentAGu;
import com.bigdata.marketsdk.fragment.SubFragmentZhiShu;
import com.blankj.utilcode.utils.NetworkUtils;
import com.blankj.utilcode.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * user:kun
 * Date:2016/10/25 or 下午1:44
 * email:hekun@gamil.com
 * Desc: 行情
 */

public class MarketStockActivity extends BaseActivity {


    private TabLayout mTabLayout;

    private ViewPager mViewPager;


    private String[] mTitles;

    private List<Fragment> mFragments;

    private MyViewPagerAdapter mViewPagerAdapter;


    @Override
    protected void baseDestory() {
        mFragments = null;

    }

    @Override
    protected void baseCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_marketstock);
        mTabLayout = (TabLayout) findViewById(R.id.TableLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        boolean connected = NetworkUtils.isConnected(this);
        if (!connected) {
            ToastUtils.showShortToast(this, "没有网络");
            return;
        }
        initData();
        configViews();


    }

    private void initData() {
        mTitles = getResources().getStringArray(R.array.tab_titles);
        mFragments = new ArrayList<>();

        mFragments.add(0, new HomeFragment());
        SubFragmentAGu fragmentAGu = new SubFragmentAGu();
        Bundle bundleAG = new Bundle();
        bundleAG.putString("sector_id", "100924");
        fragmentAGu.setArguments(bundleAG);

        SubFragmentAGu fragmentAGu2 = new SubFragmentAGu();
        Bundle bundleAG2 = new Bundle();
        bundleAG2.putString("sector_id", "100929");
        fragmentAGu2.setArguments(bundleAG2);

        SubFragmentAGu fragmentAGu3 = new SubFragmentAGu();
        Bundle bundleAG3 = new Bundle();
        bundleAG3.putString("sector_id", "100943");
        fragmentAGu3.setArguments(bundleAG3);

        MoreFragment moreFragment=new MoreFragment();

        mFragments.add(1, fragmentAGu);
        mFragments.add(2, new SubFragmentZhiShu());

        mFragments.add(3, fragmentAGu2);
        mFragments.add(4, fragmentAGu3);

        mFragments.add(5,moreFragment);

    }

    private void configViews() {
        mViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), mTitles, mFragments);
        mViewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(mViewPagerAdapter);
    }
}
