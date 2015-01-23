package com.pratik.customclass;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerAdapter extends PagerAdapter {

	private PagerAdapter mPagerAdapter;

	public ViewPagerAdapter(PagerAdapter mPagerAdapter) {
		this.mPagerAdapter = mPagerAdapter;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE;
	}

	public int getActualCount() {
		return mPagerAdapter.getCount();
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		int tempPosition = position % getActualCount();
		return mPagerAdapter.instantiateItem(container, tempPosition);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		int tempPosition = position % getActualCount();
		mPagerAdapter.destroyItem(container, tempPosition, object);
	}

	@Override
	public void finishUpdate(ViewGroup container) {
		mPagerAdapter.finishUpdate(container);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return mPagerAdapter.isViewFromObject(view, object);
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
		mPagerAdapter.restoreState(state, loader);
	}

	@Override
	public Parcelable saveState() {
		return mPagerAdapter.saveState();
	}

	@Override
	public void startUpdate(ViewGroup container) {
		mPagerAdapter.startUpdate(container);
	}

}
