package com.sds.bas.phone.home.category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

import com.sds.bas.adapter.CategoryExpandableListAdapter;
import com.sds.bas.phone.home.app.DetailActivity;
import com.sds.bas.phone.search.SearchActivity;
import com.sds.bas.util.AppParams;
import com.sds.bas.util.Utils;
import com.sds.bas.vo.Application;
import com.sds.bas.vo.Category;
import com.sds.ssa.R;

@SuppressLint("ValidFragment")
public class FragMent2 extends Fragment {

	CategoryExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<Category> listDataHeader;
    HashMap<Category, List<Application>> listDataChild;
    private boolean searchCheck;

	 public static FragMent2 newInstance() {
		 FragMent2 fragment = new FragMent2();
	     return fragment;
	}	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.category_expandable, container, false);
		expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);

		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels; 

		if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
			expListView.setIndicatorBounds(width-GetDipsFromPixel(35), width-GetDipsFromPixel(5));
		} else {
			expListView.setIndicatorBoundsRelative(width-GetDipsFromPixel(35), width-GetDipsFromPixel(5));
		}
		
		if (Utils.isNetworkAvailable(getActivity())) {
			Locale systemLocale = getResources().getConfiguration().locale;
			String systemLanguage = systemLocale.getLanguage();
			String categoryLink = this.getString(R.string.server_address) + this.getString(R.string.category_link);
			
			if(systemLanguage.equals("en")){
				categoryLink = categoryLink + "_en";
			}
			new MyTask().execute(categoryLink);
		} else {
			showToast("No Network Connection!!!");
		}
		

        return rootView;
    }

	private int GetDipsFromPixel(float pixels) {
		// Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);		
		inflater.inflate(R.menu.menu, menu);
		
	    SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
	    searchView.setQueryHint(this.getString(R.string.search));
	    
	    ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text))
        .setHintTextColor(getResources().getColor(R.color.white));	    
	    searchView.setOnQueryTextListener(OnQuerySearchView);
					    	   	    
	    menu.findItem(R.id.menu_update).setVisible(false);		
		menu.findItem(R.id.menu_search).setVisible(true);	
  	    
		searchCheck = false;	
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_search:
			searchCheck = true;
			break;
		}		
		return true;
	}	
	
	private OnQueryTextListener OnQuerySearchView = new OnQueryTextListener() {
		
		@Override
		public boolean onQueryTextSubmit(String query) {
			Intent intent = new Intent(getActivity(), SearchActivity.class);
			intent.putExtra("searchWord", query);
			startActivity(intent);
			return false;
		}
		
		@Override
		public boolean onQueryTextChange(String query) {
			if (searchCheck){
				Log.v("bas", "onQueryTextChange");
				//자동 완성 같은 건 없음;
			}
			return false;
		}
	};
	
	class MyTask extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Loading...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			return Utils.getJSONString(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == result || result.length() == 0) {
				showToast("No data found from web!!!");
				getActivity().finish();
			} else {

				try {					
					listDataHeader = new ArrayList<Category>();
					listDataChild = new HashMap<Category, List<Application>>();
					
					JSONObject mainJson = new JSONObject(result);
					JSONArray categoryArray = mainJson.getJSONArray(AppParams.CATEGORY_ARRAY_NAME);
					for (int i = 0; i < categoryArray.length(); i++) {
						JSONObject categoryObj = categoryArray.getJSONObject(i);

						Category category = new Category();
						category.setId(categoryObj.getString(AppParams.CATEGORY_ID));
						category.setName(categoryObj.getString(AppParams.CATEGORY_NAME));
						listDataHeader.add(category);
						
						JSONArray applicationArray = categoryObj.getJSONArray(AppParams.APPLICATION_ARRAY_NAME);
						List<Application> applicationList = new ArrayList<Application>();						
						
						for (int j = 0; j < applicationArray.length(); j++) {
							JSONObject applicationObj = applicationArray.getJSONObject(j);

							Application application = new Application();
							
							application.setAppId(applicationObj.getString(AppParams.APP_ID));
							application.setAppName(applicationObj.getString(AppParams.APP_NAME));
							application.setAppVerCode(applicationObj.getString(AppParams.APP_VER_CODE));
							application.setAppVerName(applicationObj.getString(AppParams.APP_VER_NAME));
							application.setAppPackageName(applicationObj.getString(AppParams.APP_PACKAGE_NAME));
							application.setAppIcon(applicationObj.getString(AppParams.APP_ICON));
							application.setAppSummary(applicationObj.getString(AppParams.APP_SUMMARY));
							application.setAppDescription(applicationObj.getString(AppParams.APP_DESCRIPTION));
							application.setAppManual(applicationObj.getString(AppParams.APP_MANUAL));
							application.setAppGrade(applicationObj.getString(AppParams.APP_GRADE));
							application.setAppGradeCount(applicationObj.getString(AppParams.APP_GRADE_COUNT));
							application.setAppDownloadUrl(applicationObj.getString(AppParams.APP_DOWNLOAD_URL));
							application.setCreated(applicationObj.getString(AppParams.APP_CREATED));
							application.setAppUploadedDate(applicationObj.getString(AppParams.APP_UPLOAD_DATE));
							application.setCategoryId(applicationObj.getString(AppParams.CATEGORY_ID));
							application.setCategoryName(applicationObj.getString(AppParams.CATEGORY_NAME));

							applicationList.add(application);							
						}
						listDataChild.put(listDataHeader.get(i), applicationList);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

				setAdapterToListview();
			}
		}
	}
	
    public void setAdapterToListview() {
		//objAdapter = new AppsRowAdapter(getActivity(), R.layout.fragment1_row2, arrayOfList);
		//listView.setAdapter(objAdapter);
    	
    	listAdapter = new CategoryExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
		expListView.setOnChildClickListener(new OnChildClickListener() {
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {

				Log.v("bas", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getAppIcon());
				
				Intent intent = new Intent(getActivity(), DetailActivity.class);
				
				intent.putExtra("id", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getAppId());
				intent.putExtra("url", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getAppIcon());
				intent.putExtra("name", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getAppName());
				intent.putExtra("categoryname", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getCategoryName());
				intent.putExtra("summary", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getAppSummary());
				intent.putExtra("desc", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getAppDescription());
				intent.putExtra("manual", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getAppManual());
				intent.putExtra("downloadUrl", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getAppDownloadUrl());
				intent.putExtra("created", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getCreated());
				intent.putExtra("verName", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getAppVerName());
				intent.putExtra("verCode", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getAppVerCode());
				startActivity(intent);
				
				return false;
			}
		});
	}

    public void showToast(String msg) {
		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
		
	}
}
