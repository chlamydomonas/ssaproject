package com.sds.ssa.fragment.app;

import java.util.ArrayList;
import java.util.HashSet;
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
import android.util.Log;
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
import com.sds.ssa.R;
import com.sds.ssa.activity.BasActivity;
import com.sds.ssa.adapter.CommentRowAdapter;
import com.sds.ssa.util.AppParams;
import com.sds.ssa.util.ReviewDialog;
import com.sds.ssa.util.Utils;
import com.sds.ssa.vo.Application;
import com.sds.ssa.vo.Comment;
import com.sds.ssa.vo.Screenshot;
import com.sds.ssa.vo.UserInfo;

public class DetailActivity extends Activity {

	private DisplayImageOptions options;
	private ImageLoader imageLoader;	

	private ProgressBar pbar;
	private TextView appName, appDesc, appSummary, appManual, categoryName, created, appVerName, reviewerCount;
	private ImageView imgView, rateStar1, rateStar2, rateStar3, rateStar4, rateStar5, appGrade;
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
		rateStar1 = (ImageView) findViewById(R.id.ratestar1);
		rateStar2 = (ImageView) findViewById(R.id.ratestar2);	
		rateStar3 = (ImageView) findViewById(R.id.ratestar3);
		rateStar4 = (ImageView) findViewById(R.id.ratestar4);
		rateStar5 = (ImageView) findViewById(R.id.ratestar5);
		reviewBtn = (Button) findViewById(R.id.reviewbtn);
		appGrade = (ImageView) findViewById(R.id.appgrade);
		reviewerCount = (TextView) findViewById(R.id.reviewercount);
		
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
		final String downloadUrl = b.getString("downloadUrl");

		appName.setText(name);
		categoryName.setText(categoryname);
		appSummary.setText(summary);
		appDesc.setText(desc);
		appManual.setText(manual);
		created.setText(create);
		appVerName.setText(verName);
		
		UserInfo userInfo = (UserInfo)this.getApplicationContext();
		String appId = b.getString("id");
		String appVerCode = b.getString("verCode");
		
		int downloadType = 0;
		for(int i=0; i < userInfo.getInstalledAppInfoList().size(); i++){
			int installedAppCode = Integer.parseInt(userInfo.getInstalledAppInfoList().get(i).getAppVerCode());
			int serverAppCode = Integer.parseInt(appVerCode);

			if(appId.equals(userInfo.getInstalledAppInfoList().get(i).getAppId())){
				if(installedAppCode == serverAppCode){
					downloadType = 2;
					downloadBtn.setVisibility(View.GONE);
					
				} else if(installedAppCode < serverAppCode){
					downloadType = 1;
					downloadBtn.setText(R.string.update);
				}
			}
			
			String installedAppId = userInfo.getInstalledAppInfoList().get(i).getAppId();
			if(appId.equals(installedAppId)){
				reviewBtn.setVisibility(View.VISIBLE);
				Log.v("bas", userInfo.getInstalledAppInfoList().get(i).getAppId());
			}
			
		}

		downloadBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//showInfo();          
				Utils.showDownload(downloadUrl, v);  
				
          	}
		});
		
//		reviewBtn.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				mCustomDialog = new CustomDialog(v.getContext(), 
//						"8�붿쓽 �щ━�ㅻ쭏��!",
//						"�곹솕蹂대윭媛�옄~!!!",
//						leftClickListener);
//				mCustomDialog.show();
//          	}
//		});
		rateStar1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				reset();
				rateStar1.setImageResource(R.drawable.filled_star);
				registerComment(v,  1);
          	}
		});
		rateStar2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				reset();
				rateStar1.setImageResource(R.drawable.filled_star);
				rateStar2.setImageResource(R.drawable.filled_star);
				registerComment(v, 2);
          	}
		});
		rateStar3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				reset();
				rateStar1.setImageResource(R.drawable.filled_star);
				rateStar2.setImageResource(R.drawable.filled_star);
				rateStar3.setImageResource(R.drawable.filled_star);
				registerComment(v, 3);
          	}
		});
		rateStar4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				reset();
				rateStar1.setImageResource(R.drawable.filled_star);
				rateStar2.setImageResource(R.drawable.filled_star);
				rateStar3.setImageResource(R.drawable.filled_star);
				rateStar4.setImageResource(R.drawable.filled_star);
				registerComment(v, 4);
          	}
		});
		rateStar5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				reset();
				rateStar1.setImageResource(R.drawable.filled_star);
				rateStar2.setImageResource(R.drawable.filled_star);
				rateStar3.setImageResource(R.drawable.filled_star);
				rateStar4.setImageResource(R.drawable.filled_star);
				rateStar5.setImageResource(R.drawable.filled_star);
				registerComment(v, 5);
          	}
		});
		
		
		String url = b.getString("url");
		loadImageFromURL(url);
		String lastChar = appId.substring(appId.length()-1, appId.length());
		int lastNum = Integer.parseInt(lastChar);
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

	private void reset() {
		rateStar1.setImageResource(R.drawable.blank_star);
		rateStar2.setImageResource(R.drawable.blank_star);
		rateStar3.setImageResource(R.drawable.blank_star);
		rateStar4.setImageResource(R.drawable.blank_star);
		rateStar5.setImageResource(R.drawable.blank_star);
	}
	
	private View.OnClickListener cancelClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			reset();
			reviewDialog.dismiss();
		}
	};

	private View.OnClickListener confirmClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			reset();
			Toast.makeText(getApplicationContext(), "confirm", 
					Toast.LENGTH_SHORT).show();
		}
	};
	
	private void loadImageFromURL(String url) {
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.profile)
				.showImageForEmptyUrl(R.drawable.profile).cacheInMemory()
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
				//DetailActivity.this.finish(); //원래 app fragment에서 넘어온 정보만 보여준다.
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
						comment.setCommentAppId(commentObj.getString(AppParams.COMMENT_APP_ID));
						//comment.setDeptId(commentObj.getString(AppParams.DEPT_ID));
						comment.setCommentDeptName(commentObj.getString(AppParams.COMMENT_DEPT_NAME));
						comment.setCommentUserName(commentObj.getString(AppParams.COMMENT_USER_NAME));
						comment.setCommentDate(commentObj.getString(AppParams.APP_CREATED));
						comment.setAppGrade(commentObj.getString(AppParams.APP_GRADE));
						comment.setComments(commentObj.getString(AppParams.COMMENTS));
						comment.setCommentAppVersion(commentObj.getString(AppParams.COMMENT_APP_VERSION));
						
						commentList.add(comment);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
				setScreenshotToHorizontal();
				
				if(commentList.size()>0){
					setCommentListView();
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

		List<String> arrList = new ArrayList<String>();
		
		if(commentList.size() > 0){
			
			int totalGrade = 0;
			for (int i = 0; i < commentList.size(); i++) {
				arrList.add(commentList.get(i).getCommentUserId()); //사실 ID로 해야 한다. 나중에 고친다.
				
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
	
				linearView.setOnClickListener(new OnClickListener() {
	
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Toast.makeText(DetailActivity.this, "Clicked item;" + uName,
								Toast.LENGTH_SHORT).show();
					}
				});
			}
			float averageGrade = totalGrade/commentList.size();
			
			if(averageGrade == 0){
				appGrade.setImageResource(R.drawable.star_0);
			}else if(0 < averageGrade && averageGrade <= 0.5){
				appGrade.setImageResource(R.drawable.star_05);
			}else if(0.5 < averageGrade && averageGrade <= 1){
				appGrade.setImageResource(R.drawable.star_1);
			}else if(1 < averageGrade && averageGrade <= 1.5){
				appGrade.setImageResource(R.drawable.star_15);
			}else if(1.5 < averageGrade && averageGrade <= 2){
				appGrade.setImageResource(R.drawable.star_2);
			}else if(2 < averageGrade && averageGrade <= 2.5){
				appGrade.setImageResource(R.drawable.star_25);
			}else if(2.5 < averageGrade && averageGrade <= 3){
				appGrade.setImageResource(R.drawable.star_3);
			}else if(3 < averageGrade && averageGrade <= 3.5){
				appGrade.setImageResource(R.drawable.star_35);
			}else if(3.5 < averageGrade && averageGrade <= 4){
				appGrade.setImageResource(R.drawable.star_4);
			}else if(4 < averageGrade && averageGrade <= 4.5){
				appGrade.setImageResource(R.drawable.star_45);
			}else if(4.5 < averageGrade && averageGrade <= 5){
				appGrade.setImageResource(R.drawable.star_5);
			}
			
			HashSet hs = new HashSet(arrList);
			List<String> newArrList = new ArrayList<String>(hs);
			reviewerCount.setText("("+newArrList.size()+")");
		}else{
			appGrade.setImageResource(R.drawable.star_0);
			reviewerCount.setText("(0)");
		}
	}


	public void setScreenshotToHorizontal() {
		HorizontalScrollView scrollView = (HorizontalScrollView) findViewById(R.id.screenshot_horizontal);

        LinearLayout topLinearLayout = new LinearLayout(this);
        topLinearLayout.setOrientation(LinearLayout.HORIZONTAL); 

        for(int i=0; i<screenshotList.size(); i++){
        	ImageView image = new ImageView( this ); 
        	
            options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.profile)
			.showImageForEmptyUrl(R.drawable.profile).cacheInMemory()
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
