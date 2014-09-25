package com.sds.ssa.tablet;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.sds.ssa.R;
import com.sds.ssa.adapter.ApplicationRowAdapter;
import com.sds.ssa.fragment.app.DetailActivity;
import com.sds.ssa.tablet.dummy.DummyContent;
import com.sds.ssa.util.AppParams;
import com.sds.ssa.util.Utils;
import com.sds.ssa.vo.Application;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link ItemListActivity} in two-pane mode (on tablets) or a
 * {@link ItemDetailActivity} on handsets.
 */
public class ItemDetailFragment extends Fragment {
	
	List<Application> applicationList;
	ListView listView;
	ApplicationRowAdapter appsRowAdapter;
	
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private DummyContent.DummyItem mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ItemDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = DummyContent.ITEM_MAP.get(getArguments().getString(
					ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_item_detail,
				container, false);

		// Show the dummy content as text in a TextView.
		if (mItem != null) {

			//View view = inflater.inflate(R.layout.application_listview, container, false);
			listView = (ListView) rootView.findViewById(R.id.detaillistview);
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
					startActivity(intent);
				}
			});
			
			//detailList.setAdapter(appsRowAdapter);
			//rootView.findViewById(R.id.item_detail)).setText(mItem.content);
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

				setAdapterToListview();
			}
		}
	}
	
	public void setAdapterToListview() {
		appsRowAdapter = new ApplicationRowAdapter(getActivity(), R.layout.application_row, applicationList);
		listView.setAdapter(appsRowAdapter);
	}
	
	public void showToast(String msg) {
		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
	}
}
