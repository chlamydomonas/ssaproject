package com.sds.ssa.fragment.app;

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
import com.sds.ssa.adapter.ApplicationRowAdapter;
import com.sds.ssa.util.Utils;
import com.sds.ssa.util.AppParams;
import com.sds.ssa.vo.Application;
import com.sds.ssa.vo.UserInfo;

@SuppressLint("ValidFragment")
public class FragMent1 extends Fragment implements OnItemClickListener {
	
	Context mContext;
	
	public FragMent1(Context context) {
		mContext = context;
	}

	List<Application> applicationList;
	ListView listView;
	ApplicationRowAdapter appsRowAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.application_listview, container, false);
		listView = (ListView) view.findViewById(R.id.listview);
		listView.setItemsCanFocus(false);
		//listView.setOnItemClickListener(this);
		
		applicationList = new ArrayList<Application>();

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
		listView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					final int position, long id) {
				
				Application application = applicationList.get(position);
				Intent intent = new Intent(getActivity(), DetailActivity.class);
				intent.putExtra("url", application.getAppIcon());
				intent.putExtra("name", application.getAppName());
				intent.putExtra("categoryname", application.getCategoryName());
				intent.putExtra("summary", application.getAppSummary());
				intent.putExtra("desc", application.getAppDescription());
				intent.putExtra("manual", application.getAppManual());
				intent.putExtra("downloadUrl", application.getAppDownloadUrl());
				intent.putExtra("created", application.getCreated());
				intent.putExtra("verName", application.getAppVerName());
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

						applicationList.add(application);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				// check data...

				/*
				Collections.sort(applicationList, new Comparator<Item>() {

					@Override
					public int compare(Item lhs, Item rhs) {
						return (lhs.getAge() - rhs.getAge());
					}
				});*/
				setAdapterToListview();
			}
		}
	}

//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//		//showDeleteDialog(position);
//		Application application = applicationList.get(position);
//		Intent intent = new Intent(getActivity(), DetailActivity.class);
//		intent.putExtra("url", application.getAppIcon());
//		intent.putExtra("name", application.getAppName());
//		intent.putExtra("desc", application.getAppDescription());
//		startActivity(intent);
//	}
	
//	private void showDeleteDialog(final int position) {
//		AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
//				.create();
//		alertDialog.setTitle("Delete ??");
//		alertDialog.setMessage("Are you sure want to Delete it??");
//		alertDialog.setButton("No", new DialogInterface.OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//			}
//		});
//		alertDialog.setButton2("Yes", new DialogInterface.OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				applicationList.remove(position);
//				appsRowAdapter.notifyDataSetChanged();
//
//			}
//		});
//		alertDialog.show();
//	}
	
	public void setAdapterToListview() {
		UserInfo loginUserInfo = (UserInfo)getActivity().getApplicationContext();
		appsRowAdapter = new ApplicationRowAdapter(getActivity(), R.layout.application_row, applicationList, loginUserInfo);
		listView.setAdapter(appsRowAdapter);
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
