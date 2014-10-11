package com.sds.bas.phone.detail;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoadingListener;
import com.sds.bas.R;
import com.sds.bas.adapter.CommentRowAdapter;
import com.sds.bas.util.AppParams;
import com.sds.bas.util.ReviewDialog;
import com.sds.bas.util.Utils;
import com.sds.bas.vo.Application;
import com.sds.bas.vo.Comment;
import com.sds.bas.vo.Screenshot;
import com.sds.bas.vo.UserInfo;

public class DetailActivity extends Activity {

	private DisplayImageOptions options;
	private ImageLoader imageLoader;	

	private ProgressBar pbar;
	private TextView appName, appDesc, appSummary, appManual, categoryName, created, 
						appVerName, reviewerCount, appDownloaded, fileSize;
	private ImageView imgView, appGrade;
	private Button downloadBtn, reviewBtn;

	private ReviewDialog reviewDialog;	
	private List<Screenshot> screenshotList;
	private LinearLayout linearListView;
	private ArrayList<Comment> commentList;
	CommentRowAdapter commentRowAdapter;
	ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.application_detail);		
		linearListView = (LinearLayout) findViewById(R.id.commentlistview);
		
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4d4d4d"))); 

		commentList = new ArrayList<Comment>();
		screenshotList = new ArrayList<Screenshot>();
		
		pbar = (ProgressBar) findViewById(R.id.pbardesc);
		appName = (TextView) findViewById(R.id.appname);
		categoryName = (TextView) findViewById(R.id.categoryname);
		appSummary = (TextView) findViewById(R.id.appsummary);
		appManual = (TextView) findViewById(R.id.appmanual);
		appDesc = (TextView) findViewById(R.id.appdesc);
		imgView = (ImageView) findViewById(R.id.appicon);
		downloadBtn = (Button) findViewById(R.id.downloadbtn);
		created = (TextView) findViewById(R.id.created);
		appVerName = (TextView) findViewById(R.id.appvername);
		reviewBtn = (Button) findViewById(R.id.reviewbtn);
		appGrade = (ImageView) findViewById(R.id.appgrade);
		reviewerCount = (TextView) findViewById(R.id.reviewercount);
		appDownloaded = (TextView) findViewById(R.id.appdownloaded);
		fileSize = (TextView) findViewById(R.id.filesize);
		
		reviewBtn.getLayoutParams().height = 60;
		//reviewBtn.getLayoutParams().width = 100;
		reviewBtn.setLayoutParams(reviewBtn.getLayoutParams());
		reviewBtn.setText(R.string.write_review);
		
		downloadBtn.getLayoutParams().height = 80;
		downloadBtn.setLayoutParams(downloadBtn.getLayoutParams());
		downloadBtn.setText(R.string.download);
		
		Bundle b = getIntent().getExtras();

		String name = b.getString("name");
		String summary = b.getString("summary");
		String desc = b.getString("desc");
		String manual = b.getString("manual");
		String categoryname = b.getString("categoryname");
		String create = b.getString("created");
		String verName = b.getString("verName");
		String downloaded = b.getString("appDownloaded");
		String size = b.getString("fileSize");
		//final String downloadUrl = b.getString("downloadUrl");
		//final String verCode = b.getString("verCode");
		String appId = b.getString("id");

		final Application selectedApplication = new Application();
		selectedApplication.setAppDownloadUrl(b.getString("downloadUrl"));
		selectedApplication.setAppVerCode(b.getString("verCode"));
		selectedApplication.setAppId(b.getString("id"));

		appName.setText(name);
		categoryName.setText(categoryname);
		appSummary.setText(summary);
		appDesc.setText(desc);
		appManual.setText(manual);
		created.setText(create);
		appVerName.setText(verName);
		appDownloaded.setText(downloaded);
		fileSize.setText(size);
		
		UserInfo userInfo = (UserInfo)this.getApplicationContext();
		String appVerCode = b.getString("verCode");

		for(int i=0; i < userInfo.getInstalledAppInfoList().size(); i++){
			int installedAppCode = Integer.parseInt(userInfo.getInstalledAppInfoList().get(i).getAppVerCode());
			int serverAppCode = Integer.parseInt(appVerCode);

			if(appId.equals(userInfo.getInstalledAppInfoList().get(i).getAppId())){
				if(installedAppCode == serverAppCode){
					downloadBtn.setVisibility(View.GONE);
					
				} else if(installedAppCode < serverAppCode){
					downloadBtn.setText(R.string.update);
				}
			}
			
			String installedAppId = userInfo.getInstalledAppInfoList().get(i).getAppId();
			if(appId.equals(installedAppId)){
				reviewBtn.setVisibility(View.VISIBLE);
			}
		}

		downloadBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Utils.showDownload(selectedApplication, v);
          	}
		});

		final String thisUserId = userInfo.getUserId();
		reviewBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			
				int registeredGrade = 0;
				for(int i=0; i<commentList.size(); i++){
					if(commentList.get(i).getCommentUserId().equals(thisUserId)){
						registeredGrade = Integer.parseInt(commentList.get(i).getAppGrade());
					}
				}
				registerComment(v, registeredGrade);
          	}
		});
		
		String url = b.getString("url");
		loadImageFromURL(url);

		// Hardcoding start
		String idNum = appId.split("_")[1];
		int lastNum = Integer.parseInt(idNum);
		String lastAddress = this.getString(R.string.detail_link_1);
		if(lastNum % 4 == 1){
			lastAddress = this.getString(R.string.detail_link_1);
		}else if(lastNum % 4 == 2){
			lastAddress = this.getString(R.string.detail_link_2);
		}else if(lastNum % 4 == 3){
			lastAddress = this.getString(R.string.detail_link_3);
		}else if(lastNum % 4 == 0){
			lastAddress = this.getString(R.string.detail_link_4);
		}
		// Hardcoding end
		
		if (Utils.isNetworkAvailable(DetailActivity.this)) {
			String appDetailLink = this.getString(R.string.server_address) + lastAddress;
			
			new MyTask().execute(appDetailLink);
		} else {
			showToast("No Network Connection. Try again.");
		}
	}
	
	protected void registerComment(View v, int star) {
		reviewDialog = new ReviewDialog(v.getContext(), 
				star,
				cancelClickListener,
				confirmClickListener);
		reviewDialog.show();
	}

	private View.OnClickListener cancelClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			//reset();
			reviewDialog.dismiss();
		}
	};

	private View.OnClickListener confirmClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			//();
			Toast.makeText(getApplicationContext(), "confirm", 
					Toast.LENGTH_SHORT).show();
		}
	};
	
	private void loadImageFromURL(String url) {
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.noimage)
				.showImageForEmptyUrl(R.drawable.noimage).cacheInMemory()
				.cacheOnDisc().build();

		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		imageLoader.displayImage(url, imgView, options, new ImageLoadingListener() {
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
				//DetailActivity.this.finish(); //?�래 app fragment?�서 ?�어???�보�?보여�?��.
			} else {

				try {
					JSONObject appDetailJson = new JSONObject(result);
					JSONObject appDetail = appDetailJson.getJSONObject(AppParams.DETAIL_ROOT_NAME);

					Application application = new Application();
					application.setAppDownloaded((String) appDetail.get(AppParams.APP_DOWNLOADED));
					application.setFileSize((String) appDetail.get(AppParams.FILE_SIZE));

					JSONArray screenshotArray = appDetail.getJSONArray(AppParams.APP_SCREENSHOT);

					for (int i = 0; i < screenshotArray.length(); i++) {
						JSONObject screenshotObj = screenshotArray.getJSONObject(i);
						
						Screenshot screenshot = new Screenshot();
						screenshot.setScreenShotUrl(screenshotObj.getString(AppParams.APP_SCREENSHOT_URL));
						
						screenshotList.add(screenshot);
					}
					
					JSONArray commentArray = appDetail.getJSONArray(AppParams.APP_COMMENT);
					
					for (int i = 0; i < commentArray.length(); i++) {
						JSONObject commentObj = commentArray.getJSONObject(i);
						
						Comment comment = new Comment();
						comment.setAppGrade(commentObj.getString(AppParams.APP_GRADE));
						comment.setComments(commentObj.getString(AppParams.COMMENTS));
						comment.setCommentDate(commentObj.getString(AppParams.COMMENT_DATE));
						comment.setCommentAppId(commentObj.getString(AppParams.COMMENT_APP_ID));
						comment.setCommentAppVersion(commentObj.getString(AppParams.COMMENT_APP_VERSION));
						comment.setCommentUserId(commentObj.getString(AppParams.COMMENT_USER_ID));
						comment.setCommentDeptName(commentObj.getString(AppParams.COMMENT_DEPT_NAME));
						comment.setCommentUserName(commentObj.getString(AppParams.COMMENT_USER_NAME));
						
						commentList.add(comment);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				setScreenshotToHorizontal();
				
				if(commentList.size()>0){
					setCommentListView();
				}else{
					appGrade.setImageResource(R.drawable.star_0);
					reviewerCount.setText("(0)");
				}
			}
		}
	}

	public void showToast(String msg) {
		Toast.makeText(DetailActivity.this, msg, Toast.LENGTH_LONG).show();
	}
	
	public void setCommentListView() {
		//commentRowAdapter = new CommentRowAdapter(DetailActivity.this, R.layout.application_detail_comment_row, commentList);
		//listView.setAdapter(commentRowAdapter);

		float totalGrade = (float) 0.0;
		for (int i = 0; i < commentList.size(); i++) {
			
			LayoutInflater inflater = null;
			inflater = (LayoutInflater) getApplicationContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			View linearView = inflater.inflate(R.layout.application_detail_comment_row, null);

			TextView userName = (TextView) linearView.findViewById(R.id.username);
			TextView installVerName = (TextView) linearView.findViewById(R.id.installvername);
			TextView comment = (TextView) linearView.findViewById(R.id.comment);
			TextView createdDate = (TextView) linearView.findViewById(R.id.created);
			ImageView userGrade = (ImageView) linearView.findViewById(R.id.usergrade);
		

			final String uName = commentList.get(i).getCommentUserName();
			final String dName = commentList.get(i).getCommentDeptName();
			final String iVerName = commentList.get(i).getCommentAppVersion();
			final String comm = commentList.get(i).getComments();
			final String create = commentList.get(i).getCommentDate();
		    final int uGrade = Integer.parseInt(commentList.get(i).getAppGrade());

		    totalGrade += uGrade;
			userName.setText(uName+" ("+dName+")");
			installVerName.setText(this.getString(R.string.version) + " " +iVerName);
			comment.setText(comm);
			createdDate.setText(create);

			if(uGrade == 1){
				userGrade.setBackgroundResource(R.drawable.star_1);
			}else if(uGrade == 2){
				userGrade.setBackgroundResource(R.drawable.star_2);
			}else if(uGrade == 3){
				userGrade.setBackgroundResource(R.drawable.star_3);
			}else if(uGrade == 4){
				userGrade.setBackgroundResource(R.drawable.star_4);
			}else if(uGrade == 5){
				userGrade.setBackgroundResource(R.drawable.star_5);
			}

			linearListView.addView(linearView);
		}

		float commentSize = commentList.size();
		float averageGrade = totalGrade/commentSize;

		if(averageGrade < 0.25){
			appGrade.setImageResource(R.drawable.star_0);
		}else if(0.25 <= averageGrade && averageGrade < 0.75){
			appGrade.setImageResource(R.drawable.star_05);
		}else if(0.75 <= averageGrade && averageGrade < 1.25){
			appGrade.setImageResource(R.drawable.star_1);
		}else if(1.25 <= averageGrade && averageGrade < 1.75){
			appGrade.setImageResource(R.drawable.star_15);
		}else if(1.75 <= averageGrade && averageGrade < 2.25){
			appGrade.setImageResource(R.drawable.star_2);
		}else if(2.25 <= averageGrade && averageGrade < 2.75){
			appGrade.setImageResource(R.drawable.star_25);
		}else if(2.75 <= averageGrade && averageGrade < 3.25){
			appGrade.setImageResource(R.drawable.star_3);
		}else if(3.25 <= averageGrade && averageGrade < 3.75){
			appGrade.setImageResource(R.drawable.star_35);
		}else if(3.75 <= averageGrade && averageGrade < 4.25){
			appGrade.setImageResource(R.drawable.star_4);
		}else if(4.25 <= averageGrade && averageGrade < 4.75){
			appGrade.setImageResource(R.drawable.star_45);
		}else if(4.75 <= averageGrade){
			appGrade.setImageResource(R.drawable.star_5);
		}
		reviewerCount.setText("("+commentList.size()+")");
	}


	public void setScreenshotToHorizontal() {
		HorizontalScrollView scrollView = (HorizontalScrollView) findViewById(R.id.screenshot_horizontal);

        LinearLayout topLinearLayout = new LinearLayout(this);
        topLinearLayout.setOrientation(LinearLayout.HORIZONTAL); 

        for(int i=0; i<screenshotList.size(); i++){
        	ImageView image = new ImageView( this ); 
        	
            options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.noimage)
			.showImageForEmptyUrl(R.drawable.noimage).cacheInMemory()
			.cacheOnDisc().build();

            topLinearLayout.addView(image);
            
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            
            int width = displayMetrics.widthPixels / 3 + 10;
            int height = displayMetrics.heightPixels / 3;
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
            image.setLayoutParams(parms);
            
            final int order = i;
            image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(), ScreenshotActivity.class);
					intent.putExtra("order", Integer.toString(order));
					intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) screenshotList);
					startActivity(intent);
				}
			});
            	
			imageLoader = ImageLoader.getInstance();
			imageLoader.init(ImageLoaderConfiguration.createDefault(this));
			imageLoader.displayImage(screenshotList.get(i).getScreenShotUrl(), image, options,
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
        scrollView.addView(topLinearLayout);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case android.R.id.home:
            /*
        	Intent intent = new Intent(this, BasActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            */
        	onBackPressed();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
	}
}
