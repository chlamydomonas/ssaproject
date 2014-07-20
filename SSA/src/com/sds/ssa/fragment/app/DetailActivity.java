package com.sds.ssa.fragment.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoadingListener;
import com.sds.ssa.activity.BasActivity;
import com.sds.ssa.R;

public class DetailActivity extends Activity {
	private DisplayImageOptions options;
	private ImageLoader imageLoader;

	private ProgressBar pbar;
	private TextView appName, appDesc;
	private ImageView imgView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);

		pbar = (ProgressBar) findViewById(R.id.pbardesc);
		appName = (TextView) findViewById(R.id.apptitle);
		appDesc = (TextView) findViewById(R.id.appdesc);
		imgView = (ImageView) findViewById(R.id.imgdesc);

		Bundle b = getIntent().getExtras();

		String title = b.getString("name");
		String desc = b.getString("desc");

		appName.setText(title);
		appDesc.setText(desc);

		String url = b.getString("url");
		loadImageFromURL(url);

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
