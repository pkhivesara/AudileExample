package com.pratik.customclass;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

public class CustomViewPager extends ViewPager {

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(PagerAdapter mAdapter) {
        super.setAdapter(mAdapter);
        setCurrentItem(0);
    }

    @Override
    public void setCurrentItem(int item) {
        // TODO Auto-generated method stub
        setCurrentItem(item, true);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        item = getOffsetAmount() + (item % getAdapter().getCount());
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public int getCurrentItem() {
        int position = super.getCurrentItem();
        if (getAdapter() instanceof ViewPagerAdapter) {
            ViewPagerAdapter infAdapter = (ViewPagerAdapter) getAdapter();
            return (position % infAdapter.getActualCount());
        } else {
            return super.getCurrentItem();
        }
    }

    private int getOffsetAmount() {
        if (getAdapter() instanceof ViewPagerAdapter) {
            ViewPagerAdapter infAdapter = (ViewPagerAdapter) getAdapter();
            return infAdapter.getActualCount() * 100;
        } else {
            return 0;
        }
    }

}
