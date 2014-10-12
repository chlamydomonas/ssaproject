package com.sds.bas.tablet.main;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sds.bas.adapter.ApplicationRowAdapter;
import com.sds.bas.tablet.sub.AppListActivity;
import com.sds.bas.util.AppParams;
import com.sds.bas.util.Utils;
import com.sds.bas.vo.Application;
import com.sds.bas.R;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link ItemListActivity} in two-pane mode (on tablets) or a
 * {@link ItemDetailActivity} on handsets.
 */
public class ItemDetailFragment extends Fragment {
	
	List<Application> applicationList;
	ListView listView;
	TextView textView;
	ApplicationRowAdapter appsRowAdapter;
	
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	private String selectedId;
	private String search = null;
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ItemDetailFragment() {
	}
	
	public ItemDetailFragment(String id, String searchWord) {
		selectedId = id;
		search = searchWord;
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
		textView = (TextView) rootView.findViewById(R.id.nolist);

		applicationList = new ArrayList<Application>();

		if (Utils.isNetworkAvailable(getActivity())) {
			Locale systemLocale = getResources().getConfiguration().locale;
			String systemLanguage = systemLocale.getLanguage();
			String link = this.getString(R.string.server_address);

			if(selectedId.equals("ALL")){
				link += this.getString(R.string.app_link);
				
				if(systemLanguage.equals("en")){
					link += "_en";
				}
			}else if(selectedId.equals("SEARCH")){
				
				//add searchword later
				link += this.getString(R.string.search_link);
				
				if(systemLanguage.equals("en")){
					link += "_en";
				}
			}else{
				link += this.getString(R.string.app_category_link);
				link += "_" + selectedId;
				
				if(systemLanguage.equals("en")){
					link += "_en";
				}
			}
			Log.v("bas", link);
			new MyTask().execute(link);
		} else {
			showToast("No Network Connection. Try again.");
		}
		listView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					final int position, long id) {

				Intent intent = new Intent(getActivity(), AppListActivity.class);
				String keyWord = "";
	
				if(!selectedId.equals("ALL") && !selectedId.equals("SEARCH")){
					keyWord = applicationList.get(0).getCategoryName();
				}
				if(selectedId.equals("SEARCH")){
					keyWord = search;
				}

				intent.putExtra("order", position);
				intent.putExtra("selectedType", selectedId); //ALL, UPDATE, CATEGORY, SEARCH
				intent.putExtra("keyWord",  keyWord);
				intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) applicationList);
				startActivity(intent);
			}
		});
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
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
	
	public void setAdapterToListview() {
		appsRowAdapter = new ApplicationRowAdapter(getActivity(), R.layout.application_row, applicationList);
		listView.setAdapter(appsRowAdapter);
	}
	
	public void showToast(String msg) {
		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
	}
}
