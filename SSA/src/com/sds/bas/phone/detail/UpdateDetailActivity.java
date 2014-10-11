package com.sds.bas.phone.detail;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoadingListener;
import com.sds.bas.R;
import com.sds.bas.util.Utils;
import com.sds.bas.vo.Application;

public class UpdateDetailActivity extends Activity {
	private DisplayImageOptions options;
	private ImageLoader imageLoader;

	private ProgressBar pbar;
	private TextView appName, categoryName, appVerName, appVerDiff;
	private ImageView imgView;
	private Button downloadBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_detail);
		
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4d4d4d"))); 

		pbar = (ProgressBar) findViewById(R.id.pbardesc);
		appName = (TextView) findViewById(R.id.appname);
		categoryName = (TextView) findViewById(R.id.categoryname);
		appVerName = (TextView) findViewById(R.id.appvername);
		appVerDiff = (TextView) findViewById(R.id.appverdiff);
		imgView = (ImageView) findViewById(R.id.appicon);
		downloadBtn = (Button) findViewById(R.id.downloadbtn);

		Bundle b = getIntent().getExtras();

		String name = b.getString("name");
		String categoryname = b.getString("categoryname");
		String vername = b.getString("vername");
		String appverdiff = b.getString("appverdiff");

		appName.setText(name);
		categoryName.setText(categoryname);
		appVerName.setText(vername); //error occurs! why???
		appVerDiff.setText(appverdiff);

		downloadBtn.getLayoutParams().height = 80;
		downloadBtn.setLayoutParams(downloadBtn.getLayoutParams());
		downloadBtn.setText(R.string.update);
		
		final Application selectedApplication = new Application();
		selectedApplication.setAppDownloadUrl(b.getString("downloadUrl"));
		selectedApplication.setAppVerCode(b.getString("verCode"));
		selectedApplication.setAppId(b.getString("id"));
		
		downloadBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Utils.showDownload(selectedApplication, v);  
          	}
		});
		
		String url = b.getString("url");
		loadImageFromURL(url);
	}
	
	private void loadImageFromURL(String url) {
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.noimage)
				.showImageForEmptyUrl(R.drawable.noimage).cacheInMemory()
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
	
	//onMenuItemSelected events didn't get called in Fragment
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
            return true;
            
	    default:
	    	return super.onOptionsItemSelected(item);
	    }
	}
}
