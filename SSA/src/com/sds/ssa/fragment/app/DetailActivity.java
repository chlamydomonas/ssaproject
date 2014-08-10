package com.sds.ssa.fragment.app;

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
import com.sds.ssa.R;
import com.sds.ssa.activity.BasActivity;
import com.sds.ssa.adapter.CommentRowAdapter;
import com.sds.ssa.util.AppParams;
import com.sds.ssa.util.ReviewDialog;
import com.sds.ssa.util.Utils;
import com.sds.ssa.vo.Application;
import com.sds.ssa.vo.Comment;
import com.sds.ssa.vo.Screenshot;

public class DetailActivity extends Activity {

	private DisplayImageOptions options;
	private ImageLoader imageLoader;	

	private ProgressBar pbar;
	private TextView appName, appDesc, appSummary, appManual, categoryName, created, appVerName;
	private ImageView imgView, rateStar1, rateStar2, rateStar3, rateStar4, rateStar5;
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
		
		reviewBtn.getLayoutParams().height = 60;
		//reviewBtn.getLayoutParams().width = 100;
		reviewBtn.setLayoutParams(reviewBtn.getLayoutParams());
		
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

		if (Utils.isNetworkAvailable(DetailActivity.this)) {
			String appDetailLink = this.getString(R.string.server_address) + this.getString(R.string.detail_link);
			
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
						comment.setAppCommentId(commentObj.getString(AppParams.APP_COMMENT_ID));
						comment.setDeptId(commentObj.getString(AppParams.DEPT_ID));
						comment.setDeptName(commentObj.getString(AppParams.DEPT_NAME));
						comment.setReviewerName(commentObj.getString(AppParams.REVIEWER_NAME));
						comment.setCreated(commentObj.getString(AppParams.APP_CREATED));
						comment.setAppGrade(commentObj.getString(AppParams.APP_GRADE));
						comment.setComment(commentObj.getString(AppParams.COMMENT));
						comment.setInstalledVerName(commentObj.getString(AppParams.INSTALLED_VER_NAME));
						comment.setInstalledVerCode(commentObj.getString(AppParams.INSTALLED_VER_CODE));
						
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
		//commentRowAdapter = new CommentRowAdapter(this, R.layout.application_detail_comment_row, commentList);
		//listView.setAdapter(commentRowAdapter);

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
		

			final String uName = commentList.get(i).getReviewerName();
			final String dName = commentList.get(i).getDeptName();
			final String iVerName = commentList.get(i).getInstalledVerName();
			final String comm = commentList.get(i).getComment();
			final String create = commentList.get(i).getCreated();
		    final int uGrade = Integer.parseInt(commentList.get(i).getAppGrade());

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
