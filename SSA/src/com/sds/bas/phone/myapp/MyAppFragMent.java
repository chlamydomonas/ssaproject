package com.sds.bas.phone.myapp;

import java.util.ArrayList;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.sds.bas.R;
import com.sds.bas.adapter.ApplicationRowAdapter;
import com.sds.bas.adapter.UpdateRowAdapter;
import com.sds.bas.phone.detail.DetailActivity;
import com.sds.bas.phone.search.SearchActivity;
import com.sds.bas.util.AppParams;
import com.sds.bas.util.Utils;
import com.sds.bas.vo.Application;
import com.sds.bas.vo.UserInfo;

@SuppressLint("ValidFragment")
public class MyAppFragMent extends Fragment implements OnItemClickListener {

    public static MyAppFragMent newInstance() {
    	MyAppFragMent fragment = new MyAppFragMent();
        return fragment;
    }	

	List<Application> applicationList;
	List<Application> installedList;
	List<Application> updateList;
	ListView updatelistview;
	ListView installedlistview;
	ApplicationRowAdapter installedRowAdapter;
	UpdateRowAdapter updateRowAdapter;
	private boolean searchCheck;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.myapp_listview, container, false);
		updatelistview = (ListView) view.findViewById(R.id.updatelistview);
		updatelistview.setItemsCanFocus(false);
		
		installedlistview = (ListView) view.findViewById(R.id.installedlistview);
		installedlistview.setItemsCanFocus(false);
		
		applicationList = new ArrayList<Application>();
		installedList = new ArrayList<Application>();
		updateList = new ArrayList<Application>();
		
		if (Utils.isNetworkAvailable(getActivity())) {
			Locale systemLocale = getResources().getConfiguration().locale;
			String systemLanguage = systemLocale.getLanguage();
			String appLink = this.getString(R.string.server_address) + this.getString(R.string.app_link);
			
			if(systemLanguage.equals("en")){
				appLink = appLink + "_en";
			}
			new MyTask().execute(appLink);
		} else {
			showToast("No Network Connection. Try again.");
		}
		
		
		updatelistview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					final int position, long id) {
				
				Application application = updateList.get(position);
				Intent intent = new Intent(getActivity(), DetailActivity.class);
				intent.putExtra("id", application.getAppId());
				intent.putExtra("url", application.getAppIcon());
				intent.putExtra("name", application.getAppName());
				intent.putExtra("categoryname", application.getCategoryName());
				intent.putExtra("summary", application.getAppSummary());
				intent.putExtra("desc", application.getAppDescription());
				intent.putExtra("manual", application.getAppManual());
				intent.putExtra("downloadUrl", application.getAppDownloadUrl());
				intent.putExtra("created", application.getCreated());
				intent.putExtra("verName", application.getAppVerName());
				intent.putExtra("verCode", application.getAppVerCode());
				intent.putExtra("appDownloaded", application.getAppDownloaded());
				intent.putExtra("fileSize", application.getFileSize());
				startActivity(intent);
			}
		});
		
		installedlistview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					final int position, long id) {
				
				Application application = installedList.get(position);
				Intent intent = new Intent(getActivity(), DetailActivity.class);
				intent.putExtra("id", application.getAppId());
				intent.putExtra("url", application.getAppIcon());
				intent.putExtra("name", application.getAppName());
				intent.putExtra("categoryname", application.getCategoryName());
				intent.putExtra("summary", application.getAppSummary());
				intent.putExtra("desc", application.getAppDescription());
				intent.putExtra("manual", application.getAppManual());
				intent.putExtra("downloadUrl", application.getAppDownloadUrl());
				intent.putExtra("created", application.getCreated());
				intent.putExtra("verName", application.getAppVerName());
				intent.putExtra("verCode", application.getAppVerCode());
				intent.putExtra("appDownloaded", application.getAppDownloaded());
				intent.putExtra("fileSize", application.getFileSize());
				startActivity(intent);
			}
		});
		return view;
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
					    	   	    
	    menu.findItem(R.id.menu_update).setVisible(true);		
		menu.findItem(R.id.menu_search).setVisible(true);	
  	    
		searchCheck = false;	
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_update:
			String updateAppUrls = "";
			for(int i=0; i < updateList.size(); i++){
				updateAppUrls += updateList.get(i).getAppDownloadUrl()+";";
			}
			Utils.showDownloadAll(updateAppUrls, this.getView());
			break;
			
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
	
	/*
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
	*/
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
					JSONObject applicationJson = new JSONObject(result);
					JSONArray applicationArray = applicationJson.getJSONArray(AppParams.APP_ARRAY_NAME);
					for (int i = 0; i < applicationArray.length(); i++) {
						JSONObject appJsonObj = applicationArray.getJSONObject(i);

						Application application = new Application();

						application.setAppId(appJsonObj.getString(AppParams.APP_ID));
						application.setAppName(appJsonObj.getString(AppParams.APP_NAME));
						application.setAppVerCode(appJsonObj.getString(AppParams.APP_VER_CODE));
						application.setAppVerName(appJsonObj.getString(AppParams.APP_VER_NAME));
						application.setAppPackageName(appJsonObj.getString(AppParams.APP_PACKAGE_NAME));
						application.setAppIcon(appJsonObj.getString(AppParams.APP_ICON));
						application.setAppSummary(appJsonObj.getString(AppParams.APP_SUMMARY));
						application.setAppDescription(appJsonObj.getString(AppParams.APP_DESCRIPTION));
						application.setAppManual(appJsonObj.getString(AppParams.APP_MANUAL));
						application.setAppGrade(appJsonObj.getString(AppParams.APP_GRADE));
						application.setAppGradeCount(appJsonObj.getString(AppParams.APP_GRADE_COUNT));
						application.setAppDownloadUrl(appJsonObj.getString(AppParams.APP_DOWNLOAD_URL));
						application.setCreated(appJsonObj.getString(AppParams.APP_CREATED));
						application.setAppUploadedDate(appJsonObj.getString(AppParams.APP_UPLOAD_DATE));
						application.setCategoryId(appJsonObj.getString(AppParams.CATEGORY_ID));
						application.setCategoryName(appJsonObj.getString(AppParams.CATEGORY_NAME));
						application.setAppDownloaded(appJsonObj.getString(AppParams.APP_DOWNLOADED));
						application.setFileSize(appJsonObj.getString(AppParams.FILE_SIZE));

						applicationList.add(application);
					}
					
					for(int i=0; i<applicationList.size(); i++){
						UserInfo userInfo = (UserInfo)getActivity().getApplicationContext();

						for(int j=0; j < userInfo.getInstalledAppInfoList().size(); j++){
							int installedAppCode = Integer.parseInt(userInfo.getInstalledAppInfoList().get(j).getAppVerCode());
							int serverAppCode = Integer.parseInt(applicationList.get(i).getAppVerCode());

							if(applicationList.get(i).getAppId().equals(userInfo.getInstalledAppInfoList().get(j).getAppId())){
								if(installedAppCode == serverAppCode){
									installedList.add(applicationList.get(i));
								} else if(installedAppCode < serverAppCode){
									updateList.add(applicationList.get(i));
								}
							}
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				// check data...

				setAdapterToListview();
			}
		}
	}
	
	public void setAdapterToListview() {
		updateRowAdapter = new UpdateRowAdapter(getActivity(), R.layout.application_row, updateList);
		updatelistview.setAdapter(updateRowAdapter);
		
		installedRowAdapter = new ApplicationRowAdapter(getActivity(), R.layout.application_row, installedList);
		installedlistview.setAdapter(installedRowAdapter);
	}
	
	public void showToast(String msg) {
		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}

}
