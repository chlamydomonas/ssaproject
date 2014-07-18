package com.sds.bas.fragment.app;

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

import com.ssa.bas.R;
import com.ssa.bas.adapter.AppsRowAdapter;
import com.ssa.bas.util.Utils;
import com.ssa.bas.vo.Application;

@SuppressLint("ValidFragment")
public class FragMent1 extends Fragment implements OnItemClickListener {
	
	Context mContext;
	
	public FragMent1(Context context) {
		mContext = context;
	}
	
	//static View v; 
	private static final String rssFeed = "https://ssa-bas-project.googlecode.com/svn/0709_3";
	
	private static final String ARRAY_NAME = "application";
	private static final String ID = "applicationId";
	private static final String NAME = "applicationName";
	private static final String VERSION = "applicationVersion";
	private static final String ICON = "appIcon";
	private static final String DESC = "appDescription";
	private static final String CATEGORYNAME = "categoryName";
	private static final String CATEGORYID = "categoryId";

	List<Application> arrayOfList;
	ListView listView;
	AppsRowAdapter objAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment1_listview2, container, false);
		listView = (ListView) view.findViewById(R.id.listview);
		listView.setOnItemClickListener(this);
		
		arrayOfList = new ArrayList<Application>();
		
		
		if (Utils.isNetworkAvailable(getActivity())) {
			new MyTask().execute(rssFeed);
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
					JSONObject mainJson = new JSONObject(result);
					JSONArray jsonArray = mainJson.getJSONArray(ARRAY_NAME);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject objJson = jsonArray.getJSONObject(i);

						Application objItem = new Application();

						objItem.setId(objJson.getString(ID));
						objItem.setName(objJson.getString(NAME));
						objItem.setVersion(objJson.getString(VERSION));
						objItem.setLink(objJson.getString(ICON));
						objItem.setDescription(objJson.getString(DESC));
						objItem.setCategoryId(objJson.getString(CATEGORYID));
						objItem.setCategoryName(objJson.getString(CATEGORYNAME));

						arrayOfList.add(objItem);

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				// check data...

				/*
				Collections.sort(arrayOfList, new Comparator<Item>() {

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
		Application item = arrayOfList.get(position);
		Intent intent = new Intent(getActivity(), DetailActivity.class);
		intent.putExtra("url", item.getLink());
		intent.putExtra("name", item.getName());
		intent.putExtra("desc", item.getDescription());
		startActivity(intent);
	}
	
	private void showDeleteDialog(final int position) {
		AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
				.create();
		alertDialog.setTitle("Delete ??");
		alertDialog.setMessage("Are you sure want to Delete it??");
		alertDialog.setButton("No", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alertDialog.setButton2("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				arrayOfList.remove(position);
				objAdapter.notifyDataSetChanged();

			}
		});
		alertDialog.show();

	}
	
	public void setAdapterToListview() {
		objAdapter = new AppsRowAdapter(getActivity(), R.layout.fragment1_row2, arrayOfList);
		listView.setAdapter(objAdapter);
	}
	public void showToast(String msg) {
		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
		
	}

}
