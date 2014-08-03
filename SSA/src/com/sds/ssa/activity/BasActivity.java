package com.sds.ssa.activity;

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
import android.widget.Toast;

import com.sds.ssa.R;
import com.sds.ssa.fragment.app.DetailActivity;
import com.sds.ssa.fragment.app.FragMent1;
import com.sds.ssa.fragment.category.FragMent2;
import com.sds.ssa.fragment.update.FragMent3;
import com.sds.ssa.util.Utils;
import com.sds.ssa.vo.UserInfo;

@SuppressLint("DefaultLocale")
public class BasActivity extends FragmentActivity {
	
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
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4d4d4d"))); // 색상 변경(색상코드)
		//이거 한 뒤로는 expandableListView 그룹 열고 닫기 색이 변경되엇음?
		
		
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
		getMenuInflater().inflate(R.menu.tab, menu);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//            searchItem = menu.findItem(R.id.action_settings);
//            searchView = (SearchView) searchItem.getActionView();
//            searchView.setQueryHint("물품명 또는 분류");
//            searchView.setOnQueryTextListener(queryTextListener);      
// 
//            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//            if(null!=searchManager ) {   
//                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//            }
//            searchView.setIconifiedByDefault(true);
//             
//        }
		
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

			//Toast.makeText(
			//		BasActivity.this,		// Qualify 'this" with Activity class
    		//		"You selected " + query,		
    		//		Toast.LENGTH_SHORT).show();
			//setContentView(R.layout.tab);
			
			//Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
			//startActivity(intent);
            	
			Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
			intent.putExtra("url", "https://ssa-bas-project.googlecode.com/svn/b.png");
			intent.putExtra("name", "애플리케이션_01");
			intent.putExtra("categoryname", "카테고리_01");
			intent.putExtra("summary", "A, a는 로마자의 첫 번째 글자이다");
			intent.putExtra("desc", "A는 소를 의미하는 그림 문자에서부터 왔다.\n이름[편집]\n라틴어·독일어·네덜란드어·인도네시아어·베트남어·에스페란토: 아 [aː]\n프랑스어·이탈리아어·스페인어: 아 [a]\n영어: 에이 [eɪ] (오스트레일리아 영어: 아이 [aɪ])\n일본어: 에 エー [eː]\n역사[편집]");
			intent.putExtra("manual", "A는 다음을 가리키는 말이다. \n교육에서 A는 최고 성적을 나타낸다.\n그리스어 접두사 a-는 '...이/가없음'을 뜻하는 접두사로, 영어와 독일어, 그리고 로망스어군에 속한 언어들의 많은 낱말에서 쓰인다.\n논리학에서 뒤집힌 A(∀)는 '모든...'을 뜻한다.\n도량형에서 A는 SI 단위계에서 전류의 단위인 암페어를 나타낸다.\n a(atto)는 10-18을 뜻하는 SI 접두사이다.a는 넓이의 단위인 아르를 뜻한다.\n수학에서 11진법 이상의 진법의 수에서 A를 숫자 '열'이란 뜻의 자리수로 쓴다.\n mathbb{A}는 대수적 수 전체의 집합을 나타낸다.\n영어에서 'a'는 부정관사이다.\n영화 《A》는 1969년도의 이탈리아 영화이다.\n음악에서 A는 음이름이다. C장조에서 A는 '라'가 된다.\n 의학에서 혈액형 가운데 하나인 A형\n영양소 가운데 하나인 비타민 A\n전기에서 A는 건전지의 크기를 나타낸다.\n종이의 A 시리즈는 종이규격 가운데 하나다.\n컴퓨터에서 <a>는 특정 페이지를 가리키는 HTML 요소이다.\n플레잉 카드에서 A는 에이스를 나타낸다.\n미국의 마이너 리그 베이스볼은 A (싱글 A), AA (더블 A), AAA (트리플 A) 등으로 나뉘며 이 중 AAA (트리플 A)가 수준이 가장 높다.");
			intent.putExtra("downloadUrl", "appDownloadUrl_01");
			intent.putExtra("created", "2014-07-01");
			intent.putExtra("verName", "1.0");
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
