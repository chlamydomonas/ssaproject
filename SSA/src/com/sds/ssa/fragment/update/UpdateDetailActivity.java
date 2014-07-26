package com.sds.ssa.fragment.update;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.sds.ssa.R;
import com.sds.ssa.activity.BasActivity;

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
		setContentView(R.layout.fragment3_detail);
		
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);

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
		//appVerName.setText(vername); //error occurs! why???
		appVerDiff.setText(appverdiff);

		downloadBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showInfo();                 
          	}
		});
		
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
	
	protected void showInfo() {
		AlertDialog.Builder alert_confirm = new AlertDialog.Builder(UpdateDetailActivity.this);
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
