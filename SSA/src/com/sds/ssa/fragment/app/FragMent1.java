package com.sds.ssa.fragment.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.sds.ssa.adapter.ApplicationRowAdapter;
import com.sds.ssa.util.Utils;
import com.sds.ssa.vo.Application;
import com.sds.ssa.vo.UserInfo;
import com.sds.ssa.R;

@SuppressLint("ValidFragment")
public class FragMent1 extends Fragment implements OnItemClickListener {
	
	Context mContext;
	
	public FragMent1(Context context) {
		mContext = context;
	}
	
	private static String appLink = "https://ssa-bas-project.googlecode.com/svn/app";

	private static final String ARRAY_NAME = "application";
	private static final String ID = "appId";
	private static final String NAME = "appName";
	private static final String VERCODE = "appVerCode";
	private static final String VERNAME = "appVerName";
	private static final String PACKAGENAME = "appPackageName";
	private static final String ICON = "appIcon";
	private static final String SUMMARY = "appSummary";
	private static final String DESCRIPTION = "appDescription";
	private static final String MANUAL = "appManual";
	private static final String GRADE = "appGrade";
	private static final String GRADECOUNT = "appGradeCount";
	private static final String DOWNLOADULR = "appDownloadUrl";
	private static final String CREATED = "created";
	private static final String UPLOADDATE = "appUploadedDate";
	private static final String CATEGORYID = "categoryId";
	private static final String CATEGORYNAME = "categoryName";

	List<Application> applicationList;
	ListView listView;
	ApplicationRowAdapter appsRowAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment1_listview, container, false);
		listView = (ListView) view.findViewById(R.id.listview);
		listView.setItemsCanFocus(false);
		//listView.setOnItemClickListener(this);
		
		applicationList = new ArrayList<Application>();

		if (Utils.isNetworkAvailable(getActivity())) {
			Locale systemLocale = getResources().getConfiguration().locale;
			String systemLanguage = systemLocale.getLanguage();
			
			if(systemLanguage.equals("en")){
				appLink = "https://ssa-bas-project.googlecode.com/svn/app_en";
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

						application.setAppId(appJsonObj.getString(ID));
						application.setAppName(appJsonObj.getString(NAME));
						application.setAppVerCode(appJsonObj.getString(VERCODE));
						application.setAppVerName(appJsonObj.getString(VERNAME));
						application.setAppPackageName(appJsonObj.getString(PACKAGENAME));
						application.setAppIcon(appJsonObj.getString(ICON));
						application.setAppSummary(appJsonObj.getString(SUMMARY));
						application.setAppDescription(appJsonObj.getString(DESCRIPTION));
						application.setAppManual(appJsonObj.getString(MANUAL));
						application.setAppGrade(appJsonObj.getString(GRADE));
						application.setAppGradeCount(appJsonObj.getString(GRADECOUNT));
						application.setAppDownloadUrl(appJsonObj.getString(DOWNLOADULR));
						application.setCreated(appJsonObj.getString(CREATED));
						application.setAppUploadedDate(appJsonObj.getString(UPLOADDATE));
						application.setCategoryId(appJsonObj.getString(CATEGORYID));
						application.setCategoryName(appJsonObj.getString(CATEGORYNAME));

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
		appsRowAdapter = new ApplicationRowAdapter(getActivity(), R.layout.fragment1_row1, applicationList, loginUserInfo);
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
