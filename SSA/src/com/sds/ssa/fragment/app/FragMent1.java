package com.sds.ssa.fragment.app;

import java.util.ArrayList;
import java.util.List;

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

import com.sds.ssa.adapter.AppsRowAdapter;
import com.sds.ssa.util.Utils;
import com.sds.ssa.vo.Application;
import com.sds.ssa.R;

@SuppressLint("ValidFragment")
public class FragMent1 extends Fragment implements OnItemClickListener {
	
	Context mContext;
	
	public FragMent1(Context context) {
		mContext = context;
	}
	
	//static View v; 
	private static final String appLink = "https://ssa-bas-project.googlecode.com/svn/0709_3";
	
	private static final String ARRAY_NAME = "application";
	private static final String ID = "applicationId";
	private static final String NAME = "applicationName";
	private static final String VERSION = "applicationVersion";
	private static final String ICON = "appIcon";
	private static final String DESC = "appDescription";
	private static final String CATEGORYNAME = "categoryName";
	private static final String CATEGORYID = "categoryId";

	List<Application> applicationList;
	ListView listView;
	AppsRowAdapter appsRowAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//View view = inflater.inflate(R.layout.fragment1_listview2, container, false);
		View view = inflater.inflate(R.layout.fragment1_listview, container, false);
		listView = (ListView) view.findViewById(R.id.listview);
		listView.setOnItemClickListener(this);
		
		applicationList = new ArrayList<Application>();

		if (Utils.isNetworkAvailable(getActivity())) {
			new MyTask().execute(appLink);
		} else {
			showToast("No Network Connection!!!");
		}
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

						application.setId(appJsonObj.getString(ID));
						application.setName(appJsonObj.getString(NAME));
						application.setVersion(appJsonObj.getString(VERSION));
						application.setLink(appJsonObj.getString(ICON));
						application.setDescription(appJsonObj.getString(DESC));
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//showDeleteDialog(position);
		Application application = applicationList.get(position);
		Intent intent = new Intent(getActivity(), DetailActivity.class);
		intent.putExtra("url", application.getLink());
		intent.putExtra("name", application.getName());
		intent.putExtra("desc", application.getDescription());
		startActivity(intent);
	}
	
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
		//appsRowAdapter = new AppsRowAdapter(getActivity(), R.layout.fragment1_row2, applicationList);
		appsRowAdapter = new AppsRowAdapter(getActivity(), R.layout.fragment1_row1, applicationList);
		listView.setAdapter(appsRowAdapter);
	}
	public void showToast(String msg) {
		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
		
	}

}
