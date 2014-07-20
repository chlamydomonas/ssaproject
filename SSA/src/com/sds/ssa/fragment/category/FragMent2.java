package com.sds.ssa.fragment.category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.sds.ssa.adapter.AppsRowAdapter;
import com.sds.ssa.adapter.ExpandableListAdapter;
import com.sds.ssa.util.Utils;
import com.sds.ssa.vo.Application;
import com.sds.ssa.R;

@SuppressLint("ValidFragment")
public class FragMent2 extends Fragment {

	Context mContext;
	
	private static final String rssFeed = "https://ssa-bas-project.googlecode.com/svn/category";
	
	private static final String CATEGORY_ARRAY_NAME = "category";
	private static final String ID = "categoryId";
	private static final String NAME = "categoryName";
	
	private static final String APPLICATION_ARRAY_NAME = "application";
	private static final String APPID = "applicationId";
	private static final String APPNAME = "applicationName";
	
	ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    
	
	public FragMent2(Context context) {
		mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment2_expandable, container, false);
		expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);
		
		// preparing list data
        //prepareListData();
        
        
        //listAdapter = new ExpandableListAdapter(mContext, listDataHeader, listDataChild);
        
        // setting list adapter
        //expListView.setAdapter(listAdapter);
        
        
        if (Utils.isNetworkAvailable(getActivity())) {
			new MyTask().execute(rssFeed);
		} else {
			showToast("No Network Connection!!!");
		}
        
        
        return rootView;
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
					listDataHeader = new ArrayList<String>();
					listDataChild = new HashMap<String, List<String>>();
					
					JSONObject mainJson = new JSONObject(result);
					JSONArray categoryArray = mainJson.getJSONArray(CATEGORY_ARRAY_NAME);
					for (int i = 0; i < categoryArray.length(); i++) {
						JSONObject categoryObj = categoryArray.getJSONObject(i);
						listDataHeader.add(categoryObj.getString(NAME));

						JSONArray applicationArray = categoryObj.getJSONArray(APPLICATION_ARRAY_NAME);
						
						List<String> subApplication = new ArrayList<String>();
						for (int j = 0; j < applicationArray.length(); j++) {
							JSONObject applicationObj = applicationArray.getJSONObject(j);
							subApplication.add(applicationObj.getString(APPNAME));
							
							//나중에 쓴다. 반드시
//							Application objItem = new Application();
//
//							objItem.setId(applicationObj.getString(APPID));
//							objItem.setName(applicationObj.getString(APPNAME));
//
//							arrayOfList.add(objItem);
							
						}
						listDataChild.put(listDataHeader.get(i), subApplication);
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
    	listAdapter = new ExpandableListAdapter(mContext, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
	}

    public void showToast(String msg) {
		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
		
	}
}
