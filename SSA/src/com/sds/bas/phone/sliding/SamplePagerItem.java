package com.sds.bas.phone.sliding;

import android.support.v4.app.Fragment;

import com.sds.bas.phone.home.app.FragMent1;
import com.sds.bas.phone.home.category.FragMent2;
import com.sds.bas.phone.home.update.FragMent3;

public class SamplePagerItem {
	
	private final CharSequence mTitle;
    private final int mIndicatorColor;
    private final int mDividerColor;
    private final int position;
        
    private Fragment[] listFragments;
    public SamplePagerItem(int position, CharSequence title, int indicatorColor, int dividerColor) {
        this.mTitle = title;
        this.position = position;
        this.mIndicatorColor = indicatorColor;
        this.mDividerColor = dividerColor;
 
        listFragments = new Fragment[] {FragMent1.newInstance(), FragMent2.newInstance(), FragMent3.newInstance()};
    }

    public Fragment createFragment() {
		return listFragments[position];
    }

    public CharSequence getTitle() {
        return mTitle;
    }

    public int getIndicatorColor() {
        return mIndicatorColor;
    }

    public int getDividerColor() {
        return mDividerColor;
    }
}
