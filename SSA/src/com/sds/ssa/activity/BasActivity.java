package com.sds.ssa.activity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.sds.ssa.fragment.app.FragMent1;
import com.sds.ssa.fragment.category.FragMent2;
import com.sds.ssa.fragment.update.FragMent3;
import com.sds.ssa.R;

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
	
	
	private static final String DATABASE_NAME = "myDB.db";
	private static final String DATABASE_TABLE_NAME = "INSTALLAPP";
	private static final String DATABASE_CREATE_TABLE = "create table if not exists "
	            + DATABASE_TABLE_NAME
	            + " (_id integer primary key autoincrement, "
	            + " app_id text not null, "
	            + " app_ver_code text not null, " + " app_install_date text not null)";
	private static final String DATABASE_DELETE_TABLE = "drop table if exists "
            + DATABASE_TABLE_NAME;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Open a new private SQLiteDatabase associated with this Context's
        // application package. Create database if it doesn't exist.
        SQLiteDatabase myDB = openOrCreateDatabase(DATABASE_NAME,
                Context.MODE_PRIVATE, null);

        // Create database table called "COUNTRY"
        myDB.execSQL(DATABASE_DELETE_TABLE);
        myDB.execSQL(DATABASE_CREATE_TABLE);

        // Create new rows (hard-coded value for the simplicity of the exercise)
        // and insert it into the table.
        ContentValues newRow = new ContentValues();

        // hard-coded for simplicity
        newRow.put("app_id", "appId_03");
        newRow.put("app_ver_code", "1");
        newRow.put("app_install_date", "2014-07-01");
        myDB.insert(DATABASE_TABLE_NAME, null, newRow);

        newRow = new ContentValues();
        newRow.put("app_id", "appId_11");
        newRow.put("app_ver_code", "1");
        newRow.put("app_install_date", "2014-07-01");
        myDB.insert(DATABASE_TABLE_NAME, null, newRow);

        newRow = new ContentValues();
        newRow.put("app_id", "appId_19");
        newRow.put("app_ver_code", "1");
        newRow.put("app_install_date", "2014-07-01");
        myDB.insert(DATABASE_TABLE_NAME, null, newRow);

        // Select columns to retrieve in the form of String array
        String[] resultColumns = new String[] { "_id", "app_id",
                "app_ver_code", "app_install_date" };
        Cursor cursor = myDB.query(DATABASE_TABLE_NAME, resultColumns, null,
                null, null, null, null, null);

        //System.out.println("##################3 " + cursor.getString(cursor.getColumnIndex("app_id")));
        
        
        //myDB.close();
        
        
		
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.tab);
		
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4d4d4d"))); // 색상 변경(색상코드)
		//이거 한 뒤로는 expandableListView 그룹 열고 닫기 색이 변경되엇음?
		
		
		PagerTabStrip pagerTabStrip = (PagerTabStrip) findViewById(R.id.pager_title_strip);
		pagerTabStrip.setDrawFullUnderline(true);
		pagerTabStrip.setTabIndicatorColor(Color.parseColor("#F94D00"));
		
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
	
	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
//	public static class DummySectionFragment extends Fragment {
//		/**
//		 * The fragment argument representing the section number for this
//		 * fragment.
//		 */
//		public static final String ARG_SECTION_NUMBER = "section_number";
//
//		public DummySectionFragment() {
//		}
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container,
//				Bundle savedInstanceState) {
//			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
//					container, false);
//			TextView dummyTextView = (TextView) rootView
//					.findViewById(R.id.section_label);
//			dummyTextView.setText(Integer.toString(getArguments().getInt(
//					ARG_SECTION_NUMBER)));
//			return rootView;
//		}
//	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.tab, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.menu_settings:
	    		Toast.makeText(
	    				BasActivity.this,		// Qualify 'this" with Activity class
	    				"You selected " + R.string.action_menu1,		
	    				Toast.LENGTH_LONG).show();	// Make sure you call show() method
	            return true;

	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
