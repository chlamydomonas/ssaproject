package com.sds.ssa.fragment.app;

import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoadingListener;
import com.sds.ssa.R;
import com.sds.ssa.activity.BasActivity;
import com.sds.ssa.util.Utils;
import com.sds.ssa.vo.Application;

public class DetailActivity extends Activity {
	
	private static String appDetailLink = "https://ssa-bas-project.googlecode.com/svn/appdetail";
	
	private static final String ARRAY_NAME = "appDetail";
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
	
	private DisplayImageOptions options;
	private ImageLoader imageLoader;

	private ProgressBar pbar;
	private TextView appName, appDesc, appSummary, appManual, categoryName;
	private ImageView imgView;
	private Button downloadBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);

		pbar = (ProgressBar) findViewById(R.id.pbardesc);
		appName = (TextView) findViewById(R.id.appname);
		categoryName = (TextView) findViewById(R.id.categoryname);
		appSummary = (TextView) findViewById(R.id.appsummary);
		appManual = (TextView) findViewById(R.id.appmanual);
		appDesc = (TextView) findViewById(R.id.appdesc);
		imgView = (ImageView) findViewById(R.id.appIcon);
		downloadBtn = (Button) findViewById(R.id.downloadBtn);
		
		Bundle b = getIntent().getExtras();

		String name = b.getString("name");
		String summary = b.getString("summary");
		String desc = b.getString("desc");
		String manual = b.getString("manual");
		String categoryname = b.getString("categoryname");

		appName.setText(name);
		categoryName.setText(categoryname);
		appSummary.setText(summary);
		appDesc.setText(desc);
		appManual.setText(manual);

		downloadBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showInfo();                 
          	}
		});
		
		String url = b.getString("url");
		loadImageFromURL(url);

		if (Utils.isNetworkAvailable(DetailActivity.this)) {
			new MyTask().execute(appDetailLink);
		} else {
			showToast("No Network Connection. Try again.");
		}
	}
	
	protected void showInfo() {
		AlertDialog.Builder alert_confirm = new AlertDialog.Builder(DetailActivity.this);
		alert_confirm
		.setTitle(R.string.download)
		.setMessage(R.string.downloadMsg).setCancelable(false)
		.setPositiveButton(R.string.yes,
				new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        // 'YES'
		    }
		})
		.setNegativeButton(R.string.no,
		new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        // 'No'
		    return;
		    }
		});
		AlertDialog alert = alert_confirm.create();
		alert.show();
	}

	private void loadImageFromURL(String url) {
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.profile)
				.showImageForEmptyUrl(R.drawable.profile).cacheInMemory()
				.cacheOnDisc().build();

		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		imageLoader.displayImage(url, imgView, options,
				new ImageLoadingListener() {
					@Override
					public void onLoadingComplete() {
						pbar.setVisibility(View.INVISIBLE);

					}

					@Override
					public void onLoadingFailed() {

						pbar.setVisibility(View.INVISIBLE);
					}

					@Override
					public void onLoadingStarted() {
						pbar.setVisibility(View.VISIBLE);
					}
				});
	}
	
	
	class MyTask extends AsyncTask<String, Void, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(DetailActivity.this);
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
				//showToast("No data found from web!!!");
				//DetailActivity.this.finish(); //원래 app fragment에서 넘어온 정보만 보여준다.
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

						//applicationList.add(application);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				//setAdapterToListview();
			}
		}
	}

	public void showToast(String msg) {
		Toast.makeText(DetailActivity.this, msg, Toast.LENGTH_LONG).show();
		
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case android.R.id.home:
            //finish();
        	Intent intent = new Intent(this, BasActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
	}
}
