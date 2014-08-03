package com.sds.ssa.fragment.app;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.sds.ssa.util.CustomDialog;
import com.sds.ssa.util.Utils;
import com.sds.ssa.vo.Application;
import com.sds.ssa.vo.Comment;
import com.sds.ssa.vo.Screenshot;

public class DetailActivity extends Activity {
	
	private CustomDialog mCustomDialog;
	private static String appDetailLink = "https://ssa-bas-project.googlecode.com/svn/appdetail";
	
	private static final String ROOT_NAME = "appDetail";
	private static final String APP_DOWNLOADED = "appDownloaded";
	private static final String FILE_SIZE = "fileSize";
	private static final String APP_SCREENSHOT = "appScreenshot";
	private static final String APP_SCREENSHOT_URL = "screenshotUrl";
	private static final String APP_COMMENT = "appComment";
	private static final String APP_COMMENT_ID = "appCommentId";
	private static final String DEPT_ID = "deptId";
	private static final String DEPT_NAME = "deptName";
	private static final String REVIEWER_NAME = "reviewerName";
	private static final String CREATED = "created";
	private static final String APP_GRADE = "appGrade";
	private static final String COMMENT = "comment";
	private static final String INSTALLED_VER_NAME = "installedVerName";
	private static final String INSTALLED_VER_CODE = "installedVerCode";
	
	private DisplayImageOptions options;
	private ImageLoader imageLoader;

	private ProgressBar pbar;
	private TextView appName, appDesc, appSummary, appManual, categoryName, created, appVerName;
	private ImageView imgView, rateStar1, rateStar2, rateStar3, rateStar4, rateStar5;
	private Button downloadBtn, reviewBtn;

	private List<Screenshot> screenshotList;
	private LinearLayout linearListView;
	private ArrayList<Comment> commentList;
	CommentRowAdapter commentRowAdapter;
	
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
			new MyTask().execute(appDetailLink);
		} else {
			showToast("No Network Connection. Try again.");
		}
	}
	
	protected void registerComment(View v, int star) {
		mCustomDialog = new CustomDialog(v.getContext(), 
				star,
				cancelClickListener,
				confirmClickListener);
		mCustomDialog.show();
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
			mCustomDialog.dismiss();
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
					JSONObject appDetail = appDetailJson.getJSONObject(ROOT_NAME);

					Application application = new Application();
					application.setAppDownloaded((String) appDetail.get(APP_DOWNLOADED));
					application.setFileSize((String) appDetail.get(FILE_SIZE));

					JSONArray screenshotArray = appDetail.getJSONArray(APP_SCREENSHOT);

					for (int i = 0; i < screenshotArray.length(); i++) {
						JSONObject screenshotObj = screenshotArray.getJSONObject(i);
						
						Screenshot screenshot = new Screenshot();
						screenshot.setScreenShotUrl(screenshotObj.getString(APP_SCREENSHOT_URL));
						
						screenshotList.add(screenshot);
					}
					
					JSONArray commentArray = appDetail.getJSONArray(APP_COMMENT);

					for (int i = 0; i < commentArray.length(); i++) {
						JSONObject commentObj = commentArray.getJSONObject(i);
						
						Comment comment = new Comment();
						comment.setAppCommentId(commentObj.getString(APP_COMMENT_ID));
						comment.setDeptId(commentObj.getString(DEPT_ID));
						comment.setDeptName(commentObj.getString(DEPT_NAME));
						comment.setReviewerName(commentObj.getString(REVIEWER_NAME));
						comment.setCreated(commentObj.getString(CREATED));
						comment.setAppGrade(commentObj.getString(APP_GRADE));
						comment.setComment(commentObj.getString(COMMENT));
						comment.setInstalledVerName(commentObj.getString(INSTALLED_VER_NAME));
						comment.setInstalledVerCode(commentObj.getString(INSTALLED_VER_CODE));
						
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
//		commentRowAdapter = new CommentRowAdapter(this, R.layout.fragment1_row1, commentList);
//		
//		
//		LinearLayout list=(LinearLayout)findViewById(R.id.listActivities);
//		adapter=new LazyAdapter(this, activityList);
//		list.setAdapter(adapter);
		
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
       // topLinearLayout.setLayoutParams(android.widget.LinearLayout.LayoutParams.FILL_PARENT,android.widget.LinearLayout.LayoutParams.FILL_PARENT);
        topLinearLayout.setOrientation(LinearLayout.HORIZONTAL); 

        for(int i=0; i<screenshotList.size(); i++){
//        	final ImageView imageView = new ImageView (this);
//            imageView.setTag(i);
//            imageView.setImageResource(R.drawable.ic_launcher);
//            topLinearLayout.addView(imageView);

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
//					// Declare Variables
//					ViewPager viewPager;
//					PagerAdapter adapter;
//					String[] rank;
//					int[] flag;
//					
//					setContentView(R.layout.application_detail_screenshot_viewpager);
//					
//					getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//							WindowManager.LayoutParams.FLAG_FULLSCREEN);
//					
//					// Generate sample data
//					rank = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
//
//
//					flag = new int[] { R.drawable.china, R.drawable.india,
//							R.drawable.unitedstates, R.drawable.indonesia,
//							R.drawable.brazil, R.drawable.pakistan, R.drawable.nigeria,
//							R.drawable.bangladesh, R.drawable.russia, R.drawable.japan };
//
//					// Locate the ViewPager in viewpager_main.xml
//					viewPager = (ViewPager) findViewById(R.id.pager);
//					// Pass results to ViewPagerAdapter Class
//					adapter = new ScreenshotViewPagerAdapter(DetailActivity.this, rank, flag);
//					// Binds the Adapter to the ViewPager
//					viewPager.setAdapter(adapter);
					
					
					Intent intent = new Intent(getApplicationContext(), ScreenshotActivity.class);
					//intent.putExtra("order", Integer.toString(order));
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
	
	public void setScreenshotToHorizontal_origin() {
		HorizontalScrollView scrollView = (HorizontalScrollView) findViewById(R.id.screenshot_horizontal);

        LinearLayout topLinearLayout = new LinearLayout(this);
       // topLinearLayout.setLayoutParams(android.widget.LinearLayout.LayoutParams.FILL_PARENT,android.widget.LinearLayout.LayoutParams.FILL_PARENT);
        topLinearLayout.setOrientation(LinearLayout.HORIZONTAL); 

        for (int i = 0; i < 15; i++){
        	
            final ImageView imageView = new ImageView (this);
            imageView.setTag(i);
            imageView.setImageResource(R.drawable.ic_launcher);

            //imageView.setImageURI(Uri.parse("https://ssa-bas-project.googlecode.com/svn/screenshot_01_1.png"));
            topLinearLayout.addView(imageView);

//            imageView.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					
//				}
//			});
        }

        scrollView.addView(topLinearLayout);


//      ImageView img=(ImageView)findViewById(R.id.imageView1);
//      final ImageLoader imageLoader = ImageLoader.getInstance();
//      
//      
//      
//      ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
//      .threadPoolSize(3)
//      .threadPriority(Thread.NORM_PRIORITY - 2)
//      .memoryCacheSize(1500000) // 1.5 Mb
//      .discCacheSize(50000000) // 50 Mb
//      .httpReadTimeout(10000) // 10 s
//      .denyCacheImageMultipleSizesInMemory()
//      .enableLogging() // Not necessary in common
//      .build();
//  // Initialize ImageLoader with configuration.
//  ImageLoader.getInstance().init(config);
//      
//      final DisplayImageOptions options = new DisplayImageOptions.Builder()
//      .showStubImage(R.drawable.ic_launcher)
//      .cacheInMemory()
//      .cacheOnDisc()
//      .build();
//      
//      imageLoader.displayImage("http://3.bp.blogspot.com/_Sd45ASngYHA/TVC78RORKoI/AAAAAAAAARk/y0GcNkTmb40/s1600/android+logo1.jpg",img,options);
//      
//      img.setOnClickListener(new OnClickListener()
//      {
//          
//          @Overridt
//          public void onClick(View v)
//          {
//              // TODO Auto-generated method stub
//              Dialog d =new Dialog(TestActivity.this);
//              d.setContentView(R.layout.dialog);
//          
//              d.setCancelable(true);
//              
//              ImageView d_img=(ImageView)d.findViewById(R.id.dialog_img);
////                d.setLayoutParams(L)
//              imageLoader.displayImage("http://3.bp.blogspot.com/_Sd45ASngYHA/TVC78RORKoI/AAAAAAAAARk/y0GcNkTmb40/s1600/android+logo1.jpg",d_img,options);
//              
//              d.show();
//              }
//      });
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
