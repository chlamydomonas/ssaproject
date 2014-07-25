package com.sds.ssa.fragment.category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

import com.sds.ssa.R;
import com.sds.ssa.adapter.CategoryExpandableListAdapter;
import com.sds.ssa.util.Utils;
import com.sds.ssa.vo.Application;
import com.sds.ssa.vo.Category;

@SuppressLint("ValidFragment")
public class FragMent2 extends Fragment {

	Context mContext;
	
	private static String categoryLink = "https://ssa-bas-project.googlecode.com/svn/category";
	
	private static final String CATEGORY_ARRAY_NAME = "category";
	private static final String ID = "categoryId";
	private static final String NAME = "categoryName";
	
	private static final String APPLICATION_ARRAY_NAME = "application";
	private static final String APPID = "appId";
	private static final String APPNAME = "appName";
	private static final String APPVERCODE = "appVerCode";
	private static final String APPVERNAME = "appVerName";
	private static final String APPPACKAGENAME = "appPackageName";
	private static final String APPICON = "appIcon";
	private static final String APPSUMMARY = "appSummary";
	private static final String APPDESCRIPTION = "appDescription";
	private static final String APPMANUAL = "appManual";
	private static final String APPGRADE = "appGrade";
	private static final String APPGRADECOUNT = "appGradeCount";
	private static final String APPDOWNLOADULR = "appDownloadUrl";
	private static final String APPCREATED = "created";
	private static final String APPUPLOADDATE = "appUploadedDate";
	
	CategoryExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<Category> listDataHeader;
    HashMap<Category, List<Application>> listDataChild;

	public FragMent2(Context context) {
		mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment2_expandable, container, false);
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
			
			if(systemLanguage.equals("en")){
				categoryLink = "https://ssa-bas-project.googlecode.com/svn/category_en";
			}
			new MyTask().execute(categoryLink);
		} else {
			showToast("No Network Connection!!!");
		}
		
		expListView.setOnChildClickListener(new OnChildClickListener() {
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
						return false;
			}
		});
        return rootView;
    }

	private int GetDipsFromPixel(float pixels) {
		// Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
	}

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
					JSONArray categoryArray = mainJson.getJSONArray(CATEGORY_ARRAY_NAME);
					for (int i = 0; i < categoryArray.length(); i++) {
						JSONObject categoryObj = categoryArray.getJSONObject(i);

						Category category = new Category();
						category.setId(categoryObj.getString(ID));
						category.setName(categoryObj.getString(NAME));
						listDataHeader.add(category);
						
						JSONArray applicationArray = categoryObj.getJSONArray(APPLICATION_ARRAY_NAME);
						List<Application> applicationList = new ArrayList<Application>();						
						
						for (int j = 0; j < applicationArray.length(); j++) {
							JSONObject applicationObj = applicationArray.getJSONObject(j);

							Application application = new Application();
							
							application.setAppId(applicationObj.getString(APPID));
							application.setAppName(applicationObj.getString(APPNAME));
							application.setAppVerCode(applicationObj.getString(APPVERCODE));
							application.setAppVerName(applicationObj.getString(APPVERNAME));
							application.setAppPackageName(applicationObj.getString(APPPACKAGENAME));
							application.setAppIcon(applicationObj.getString(APPICON));
							application.setAppSummary(applicationObj.getString(APPSUMMARY));
							application.setAppDescription(applicationObj.getString(APPDESCRIPTION));
							application.setAppManual(applicationObj.getString(APPMANUAL));
							application.setAppGrade(applicationObj.getString(APPGRADE));
							application.setAppGradeCount(applicationObj.getString(APPGRADECOUNT));
							application.setAppDownloadUrl(applicationObj.getString(APPDOWNLOADULR));
							application.setCreated(applicationObj.getString(APPCREATED));
							application.setAppUploadedDate(applicationObj.getString(APPUPLOADDATE));
							application.setCategoryId(applicationObj.getString(ID));
							application.setCategoryName(applicationObj.getString(NAME));

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
    	
    	listAdapter = new CategoryExpandableListAdapter(mContext, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
	}

    public void showToast(String msg) {
		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
		
	}
}
