package com.sds.bas.tablet.sub;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.sds.bas.adapter.CommentRowAdapter;
import com.sds.bas.phone.detail.ScreenshotActivity;
import com.sds.bas.util.AppParams;
import com.sds.bas.util.ReviewDialog;
import com.sds.bas.util.Utils;
import com.sds.bas.vo.Application;
import com.sds.bas.vo.Comment;
import com.sds.bas.vo.Screenshot;
import com.sds.bas.vo.UserInfo;
import com.sds.bas.R;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link AppListActivity} in two-pane mode (on tablets) or a
 * {@link AppDetailActivity} on handsets.
 */
public class AppDetailFragment extends Fragment {
	
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	private String id;
	private String url;
	private String name;
	private String categoryname;
	private String summary;
	private String desc;
	private String manual;
	private String downloadUrl;
	private String create;
	private String verName;
	private String verCode;
	private String downloaded;
	private String size;

	private DisplayImageOptions options;
	private ImageLoader imageLoader;	

	private ProgressBar pbar;
	private TextView appName, appDesc, appSummary, appManual, categoryName, created, 
					appVerName, reviewerCount, appDownloaded, fileSize;;
	private ImageView imgView, appGrade;
	private Button downloadBtn, reviewBtn;

	private ReviewDialog reviewDialog;	
	private List<Screenshot> screenshotList;
	private LinearLayout linearListView;
	private ArrayList<Comment> commentList;
	CommentRowAdapter commentRowAdapter;
	ListView listView;
	View rootView;
	
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public AppDetailFragment() {
	}

	public AppDetailFragment(Application application) {
		id = application.getAppId();
		url = application.getAppIcon();
		name = application.getAppName();
		categoryname = application.getCategoryName();
		summary = application.getAppSummary();
		desc = application.getAppDescription();
		manual = application.getAppManual();
		downloadUrl = application.getAppDownloadUrl();
		create = application.getCreated();
		verName = application.getAppVerName();
		verCode = application.getAppVerCode();
		downloaded = application.getAppDownloaded();
		size = application.getFileSize();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.application_detail,
				container, false);

		linearListView = (LinearLayout) rootView.findViewById(R.id.commentlistview);

		commentList = new ArrayList<Comment>();
		screenshotList = new ArrayList<Screenshot>();
		
		pbar = (ProgressBar) rootView.findViewById(R.id.pbardesc);
		appName = (TextView) rootView.findViewById(R.id.appname);
		categoryName = (TextView) rootView.findViewById(R.id.categoryname);
		appSummary = (TextView) rootView.findViewById(R.id.appsummary);
		appManual = (TextView) rootView.findViewById(R.id.appmanual);
		appDesc = (TextView) rootView.findViewById(R.id.appdesc);
		imgView = (ImageView) rootView.findViewById(R.id.appicon);
		downloadBtn = (Button) rootView.findViewById(R.id.downloadbtn);
		created = (TextView) rootView.findViewById(R.id.created);
		appVerName = (TextView) rootView.findViewById(R.id.appvername);
		reviewBtn = (Button) rootView.findViewById(R.id.reviewbtn);
		appGrade = (ImageView) rootView.findViewById(R.id.appgrade);
		reviewerCount = (TextView) rootView.findViewById(R.id.reviewercount);
		appDownloaded = (TextView) rootView.findViewById(R.id.appdownloaded);
		fileSize = (TextView) rootView.findViewById(R.id.filesize);
		
		reviewBtn.getLayoutParams().height = 30;
		//reviewBtn.getLayoutParams().width = 100;
		reviewBtn.setLayoutParams(reviewBtn.getLayoutParams());
		reviewBtn.setText(R.string.write_review);
		
		downloadBtn.getLayoutParams().height = 40;
		downloadBtn.setLayoutParams(downloadBtn.getLayoutParams());
		downloadBtn.setText(R.string.download);
		
		appName.setText(name);
		categoryName.setText(categoryname);
		appSummary.setText(summary);
		appDesc.setText(desc);
		appManual.setText(manual);
		created.setText(create);
		appVerName.setText(verName);
		appDownloaded.setText(downloaded);
		fileSize.setText(size);
		
		UserInfo userInfo = (UserInfo)getActivity().getApplicationContext();
		String appId = id;
		String appVerCode = verCode;

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
		
		final Application selectedApplication = new Application();
		selectedApplication.setAppDownloadUrl(downloadUrl);
		selectedApplication.setAppVerCode(verCode);
		selectedApplication.setAppId(id);
		
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
		
		if (Utils.isNetworkAvailable(getActivity())) {
			String appDetailLink = this.getString(R.string.server_address) + lastAddress;
			
			new MyTask().execute(appDetailLink);
		} else {
			showToast("No Network Connection. Try again.");
		}
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
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
			Toast.makeText(getActivity(), "confirm", 
					Toast.LENGTH_SHORT).show();
		}
	};
	
	private void loadImageFromURL(String url) {
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.noimage)
				.showImageForEmptyUrl(R.drawable.noimage).cacheInMemory()
				.cacheOnDisc().build();

		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
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
				//showToast("No data found from web!!!");
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
		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
	}
	
	public void setCommentListView() {
		float totalGrade = (float) 0.0;
		for (int i = 0; i < commentList.size(); i++) {
			
			LayoutInflater inflater = null;
			inflater = (LayoutInflater) this.getActivity().getApplicationContext()
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
			installVerName.setText(getActivity().getString(R.string.version) + " " +iVerName);
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
		HorizontalScrollView scrollView = (HorizontalScrollView) rootView.findViewById(R.id.screenshot_horizontal);

        LinearLayout topLinearLayout = new LinearLayout(getActivity());
        topLinearLayout.setOrientation(LinearLayout.HORIZONTAL); 

        for(int i=0; i<screenshotList.size(); i++){
        	ImageView image = new ImageView(getActivity()); 
        	
            options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.noimage)
			.showImageForEmptyUrl(R.drawable.noimage).cacheInMemory()
			.cacheOnDisc().build();

            topLinearLayout.addView(image);
            
            DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
            
            int width = displayMetrics.widthPixels / 3 + 10;
            int height = displayMetrics.heightPixels / 3;
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
            image.setLayoutParams(parms);
            
            final int order = i;
            image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity().getApplicationContext(), ScreenshotActivity.class);
					intent.putExtra("order", Integer.toString(order));
					intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) screenshotList);
					startActivity(intent);
				}
			});
            	
			imageLoader = ImageLoader.getInstance();
			imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
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
}
