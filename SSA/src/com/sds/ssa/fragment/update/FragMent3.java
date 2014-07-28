package com.sds.ssa.fragment.update;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.sds.ssa.R;
import com.sds.ssa.adapter.UpdateRowAdapter;
import com.sds.ssa.util.Utils;
import com.sds.ssa.vo.Application;

@SuppressLint("ValidFragment")
public class FragMent3 extends Fragment {

	
	Context mContext;
	
	public FragMent3(Context context) {
		mContext = context;
	}
	
	private static String appupdateLink = "https://ssa-bas-project.googlecode.com/svn/appupdate";

	private static final String ARRAY_NAME = "application";
	private static final String APP_ID = "appId";
	private static final String APP_NAME = "appName";
	private static final String APP_VER_NAME = "appVerName";
	private static final String APP_ICON = "appIcon";
	private static final String APP_VER_DIFF = "appVerDiff";
	private static final String APP_DOWNLOAD_URL = "appDownloadUrl";
	private static final String CREATED = "created";
	private static final String CATEGORY_NAME = "categoryName";

	List<Application> applicationList;
	ListView listView;
	UpdateRowAdapter updateRowAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment1_listview, container, false);
		listView = (ListView) view.findViewById(R.id.listview);
		listView.setItemsCanFocus(false);

		applicationList = new ArrayList<Application>();

		if (Utils.isNetworkAvailable(getActivity())) {
			Locale systemLocale = getResources().getConfiguration().locale;
			String systemLanguage = systemLocale.getLanguage();
			
			if(systemLanguage.equals("en")){
				appupdateLink = "https://ssa-bas-project.googlecode.com/svn/appupdate_en";
			}
			new MyTask().execute(appupdateLink);
		} else {
			showToast("No Network Connection. Try again.");
		}
		listView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					final int position, long id) {
				
				Application application = applicationList.get(position);
				Intent intent = new Intent(getActivity(), UpdateDetailActivity.class);
				intent.putExtra("url", application.getAppIcon());
				intent.putExtra("name", application.getAppName());
				intent.putExtra("categoryname", application.getCategoryName());
				intent.putExtra("vername", application.getAppVerName());
				intent.putExtra("appverdiff", application.getAppVerDiff());
				startActivity(intent);
			}
		});
		return view;
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
					JSONObject applicationJson = new JSONObject(result);
					JSONArray applicationArray = applicationJson.getJSONArray(ARRAY_NAME);
					for (int i = 0; i < applicationArray.length(); i++) {
						JSONObject appJsonObj = applicationArray.getJSONObject(i);

						Application application = new Application();

						application.setAppId(appJsonObj.getString(APP_ID));
						application.setAppName(appJsonObj.getString(APP_NAME));
						application.setAppVerName(appJsonObj.getString(APP_VER_NAME));
						application.setAppIcon(appJsonObj.getString(APP_ICON));
						application.setAppDownloadUrl(appJsonObj.getString(APP_DOWNLOAD_URL));
						application.setCreated(appJsonObj.getString(CREATED));
						application.setCategoryName(appJsonObj.getString(CATEGORY_NAME));
						application.setAppVerDiff(appJsonObj.getString(APP_VER_DIFF));

						applicationList.add(application);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				setAdapterToListview();
			}
		}
	}

	public void setAdapterToListview() {
		updateRowAdapter = new UpdateRowAdapter(getActivity(), R.layout.fragment3_row, applicationList);
		listView.setAdapter(updateRowAdapter);
	}
	public void showToast(String msg) {
		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
		
	}
}
