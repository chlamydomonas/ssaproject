package com.sds.ssa.tablet;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.sds.ssa.R;
import com.sds.ssa.adapter.UpdateRowAdapter;
import com.sds.ssa.tablet.sub.AppListActivity;
import com.sds.ssa.util.AppParams;
import com.sds.ssa.util.Utils;
import com.sds.ssa.vo.Application;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link ItemListActivity} in two-pane mode (on tablets) or a
 * {@link ItemDetailActivity} on handsets.
 */
public class ItemUpdateDetailFragment extends Fragment {
	
	List<Application> applicationList;
	ListView listView;
	UpdateRowAdapter updateRowAdapter;
	Activity mActivity;
	
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	private String selectedId;
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ItemUpdateDetailFragment() {
	}
	
	public ItemUpdateDetailFragment(String id) {
		selectedId = id;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.tablet_fragment_item_detail,
				container, false);

		listView = (ListView) rootView.findViewById(R.id.detaillistview);
		listView.setItemsCanFocus(false);

		applicationList = new ArrayList<Application>();

		if (Utils.isNetworkAvailable(getActivity())) {
			Locale systemLocale = getResources().getConfiguration().locale;
			String systemLanguage = systemLocale.getLanguage();
			String link = this.getString(R.string.server_address);

			link += this.getString(R.string.update_link);
			
			if(systemLanguage.equals("en")){
				link += "_en";
			}
			new MyTask().execute(link);
		} else {
			showToast("No Network Connection. Try again.");
		}
		listView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					final int position, long id) {

				Intent intent = new Intent(getActivity(), AppListActivity.class);
				
				intent.putExtra("order", position);
				intent.putExtra("selectedType", selectedId);
				intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) applicationList);
				startActivity(intent);
			}
		});
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mActivity = activity;
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
						application.setAppVerName(appJsonObj.getString(AppParams.APP_VER_NAME));
						application.setAppIcon(appJsonObj.getString(AppParams.APP_ICON));
						application.setAppDownloadUrl(appJsonObj.getString(AppParams.APP_DOWNLOAD_URL));
						application.setCreated(appJsonObj.getString(AppParams.APP_CREATED));
						application.setCategoryName(appJsonObj.getString(AppParams.CATEGORY_NAME));
						application.setAppVerDiff(appJsonObj.getString(AppParams.APP_VER_DIFF));

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
		updateRowAdapter = new UpdateRowAdapter(mActivity, R.layout.update_row, applicationList);
		listView.setAdapter(updateRowAdapter);
	}
	
	public void showToast(String msg) {
		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
	}
}
