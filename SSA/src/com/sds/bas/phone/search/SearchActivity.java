package com.sds.bas.phone.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sds.bas.R;
import com.sds.bas.adapter.ApplicationRowAdapter;
import com.sds.bas.phone.detail.DetailActivity;
import com.sds.bas.util.AppParams;
import com.sds.bas.util.Utils;
import com.sds.bas.vo.Application;


public class SearchActivity extends Activity implements OnItemClickListener {

	List<Application> applicationList;
	ListView listView;
	TextView textView;
	ApplicationRowAdapter appsRowAdapter;
	private boolean searchCheck;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.application_listview);
		
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4d4d4d"))); 
		
		Intent intent = getIntent();
		String searchWord = intent.getExtras().getString("searchWord");
		setTitle(searchWord);
			
		listView = (ListView) findViewById(R.id.listview);
		listView.setOnItemClickListener(this);
		textView = (TextView) findViewById(R.id.nolist);

		applicationList = new ArrayList<Application>();
		
		if (Utils.isNetworkAvailable(SearchActivity.this)) {
			Locale systemLocale = getResources().getConfiguration().locale;
			String systemLanguage = systemLocale.getLanguage();
			String searchLink = this.getString(R.string.server_address) + this.getString(R.string.search_link);
			
			if(systemLanguage.equals("en")){
				searchLink = searchLink + "_en";
			}
			new MyTask().execute(searchLink);
		} else {
			showToast("No Network Connection!!!");
		}
		listView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					final int position, long id) {
				
				Application application = applicationList.get(position);
				Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
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
	}

	class MyTask extends AsyncTask<String, Void, String> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(SearchActivity.this);
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
				SearchActivity.this.finish();
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
				} catch (JSONException e) {
					e.printStackTrace();
				}

				if(applicationList.size() > 0) {
					setAdapterToListview();
				}else{
					textView.setVisibility(View.VISIBLE);
				}
			}

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
	}

	public void setAdapterToListview() {
		appsRowAdapter = new ApplicationRowAdapter(this, R.layout.application_row, applicationList);
		listView.setAdapter(appsRowAdapter);
	}

	public void showToast(String msg) {
		Toast.makeText(SearchActivity.this, msg, Toast.LENGTH_LONG).show();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case android.R.id.home:
        	onBackPressed();
            break;
      
        case R.id.menu_search:
        	searchCheck = true;
    		break;
            
        default:
            return super.onOptionsItemSelected(item);
        }
		return true;
	}
}
