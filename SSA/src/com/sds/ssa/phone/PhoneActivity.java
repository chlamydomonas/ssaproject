package com.sds.ssa.phone;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.sds.ssa.R;
import com.sds.ssa.fragment.app.FragMent1;
import com.sds.ssa.fragment.category.FragMent2;
import com.sds.ssa.fragment.update.FragMent3;
import com.sds.ssa.search.SearchActivity;
import com.sds.ssa.util.Utils;
import com.sds.ssa.vo.UserInfo;

@SuppressLint("DefaultLocale")
public class PhoneActivity extends FragmentActivity {
	
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		
		if(intent.hasExtra("userInfo")){
			String userInfoParam = intent.getExtras().getString("userInfo");
			UserInfo userInfo = Utils.getUserInfo(userInfoParam);

			UserInfo loginUserInfo = (UserInfo)getApplicationContext();
			loginUserInfo.setUserId(userInfo.getUserId());
			loginUserInfo.setUserDept(userInfo.getUserDept());
			loginUserInfo.setSecurityLevel(userInfo.getSecurityLevel());
			loginUserInfo.setInstalledAppInfoList(userInfo.getInstalledAppInfoList());
		}
		
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.tab);
		
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4d4d4d"))); // ?�상 �?��(?�상코드)
		//?�거 ???�로??expandableListView 그룹 ?�고 ?�기 ?�이 �?��?�엇??
		
		
		PagerTabStrip pagerTabStrip = (PagerTabStrip) findViewById(R.id.pager_title_strip);
		pagerTabStrip.setDrawFullUnderline(true);
		pagerTabStrip.setTabIndicatorColor(Color.parseColor("#2680ff"));
		
		supportInvalidateOptionsMenu();
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getApplicationContext(),
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	@SuppressLint("DefaultLocale")
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		Context mContext;

		public SectionsPagerAdapter(Context context, FragmentManager fm) {
			super(fm);
			mContext = context;
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			switch(position) {
			case 0:
				return new FragMent1(mContext);
			case 1:
				return new FragMent2(mContext);
			case 2:
				return new FragMent3(mContext);
			}
			return null;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}
		
		@SuppressLint("DefaultLocale")
		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			switch (position) {
			case 0:
				return mContext.getString(R.string.title_section1).toUpperCase();
			case 1:
				return mContext.getString(R.string.title_section2).toUpperCase();
			case 2:
				return mContext.getString(R.string.title_section3).toUpperCase();
			}
			return null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search, menu);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_settings).getActionView();
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
	    searchView.setQueryHint(getString(R.string.searchword));
	    searchView.setOnQueryTextListener(queryTextListener);
        return true;
	}
	
	private OnQueryTextListener queryTextListener = new OnQueryTextListener() {
		@Override
		public boolean onQueryTextSubmit(String query) {
			Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
			intent.putExtra("searchWord", query);
			startActivity(intent);
			return false;
		}

		@Override
		public boolean onQueryTextChange(String newText) {
			// TODO Auto-generated method stub
			return false;
		}
	};

}
