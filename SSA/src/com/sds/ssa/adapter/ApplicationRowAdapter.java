package com.sds.ssa.adapter;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoadingListener;
import com.sds.ssa.vo.Application;
import com.sds.ssa.vo.UserInfo;
import com.sds.ssa.R;
import com.sds.ssa.R.drawable;

public class ApplicationRowAdapter extends ArrayAdapter<Application> {

	private Activity activity;
	private List<Application> applicationList;
	private Application application;
	private UserInfo userInfo;
	private int row;
	private DisplayImageOptions options;
	ImageLoader imageLoader;

	public ApplicationRowAdapter(Activity act, int resource, List<Application> appList, 
			UserInfo loginUserInfo) {
		super(act, resource, appList);
		this.activity = act;
		this.row = resource;
		this.applicationList = appList;
		this.userInfo = loginUserInfo;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(row, null);

			holder = new ViewHolder();
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.profile)
				.showImageForEmptyUrl(R.drawable.profile).cacheInMemory()
				.cacheOnDisc().build();
		imageLoader = ImageLoader.getInstance();

		if ((applicationList == null) || ((position + 1) > applicationList.size()))
			return view;

		application = applicationList.get(position);

		holder.appName = (TextView) view.findViewById(R.id.appname);
		//holder.appVersion = (TextView) view.findViewById(R.id.appversion);
		holder.appDesc = (TextView) view.findViewById(R.id.appdesc);
		holder.appId = (TextView) view.findViewById(R.id.appid);
		holder.appGrade = (ImageView) view.findViewById(R.id.stargrade);
		holder.categoryName = (TextView) view.findViewById(R.id.categoryname);
		holder.appIcon = (ImageView) view.findViewById(R.id.image);
		holder.pbar = (ProgressBar) view.findViewById(R.id.pbar);
		holder.downloadBtn = (ImageButton) view.findViewById(R.id.downloadBtn);
		holder.downloadBtn.setBackgroundResource(R.drawable.download_selector);
		
		for(int i=0; i < userInfo.getInstalledAppInfoList().size(); i++){
			int installedAppCode = Integer.parseInt(userInfo.getInstalledAppInfoList().get(i).getAppVerCode());
			int serverAppCode = Integer.parseInt(application.getAppVerCode());

			if(application.getAppId().equals(userInfo.getInstalledAppInfoList().get(i).getAppId())){
				if(installedAppCode == serverAppCode){
					holder.downloadBtn.setBackgroundResource(R.drawable.download_pressed);
					
				} else if(installedAppCode < serverAppCode){
					holder.downloadBtn.setBackgroundResource(R.drawable.update_selector);
				}
			}
		}

		if (holder.appName != null && null != application.getAppName()
				&& application.getAppName().trim().length() > 0) {
			holder.appName.setText(Html.fromHtml(application.getAppName()));
		}
		if (holder.appDesc != null && null != application.getAppDescription()
				&& application.getAppDescription().trim().length() > 0) {
			holder.appDesc.setText(Html.fromHtml(application.getAppDescription()));
		}
		if (holder.appId != null && null != application.getAppId()
				&& application.getAppId().trim().length() > 0) {
			holder.appId.setText(Html.fromHtml(application.getAppId()));
		}
		if (holder.categoryName != null && null != application.getCategoryName()
				&& application.getCategoryName().trim().length() > 0) {
			holder.categoryName.setText(Html.fromHtml(application.getCategoryName()));
		}
		if (holder.appGrade != null && null != application.getAppGrade()
				&& application.getAppGrade().trim().length() > 0) {
			float stargrade = Float.parseFloat(application.getAppGrade());
			
			if(0 <= stargrade && stargrade < 0.25){
				holder.appGrade.setBackgroundResource(R.drawable.star_0);
			}else if(0.25 <= stargrade && stargrade < 0.75){
				holder.appGrade.setBackgroundResource(R.drawable.star_05);
			}else if(0.75 <= stargrade && stargrade < 1.25){
				holder.appGrade.setBackgroundResource(R.drawable.star_1);
			}else if(1.25 <= stargrade && stargrade < 1.75){
				holder.appGrade.setBackgroundResource(R.drawable.star_15);
			}else if(1.75 <= stargrade && stargrade < 2.25){
				holder.appGrade.setBackgroundResource(R.drawable.star_2);
			}else if(2.25 <= stargrade && stargrade < 2.75){
				holder.appGrade.setBackgroundResource(R.drawable.star_25);
			}else if(2.75 <= stargrade && stargrade < 3.25){
				holder.appGrade.setBackgroundResource(R.drawable.star_3);
			}else if(3.25 <= stargrade && stargrade < 3.75){
				holder.appGrade.setBackgroundResource(R.drawable.star_35);
			}else if(3.75 <= stargrade && stargrade < 4.25){
				holder.appGrade.setBackgroundResource(R.drawable.star_4);
			}else if(4.25 <= stargrade && stargrade < 4.75){
				holder.appGrade.setBackgroundResource(R.drawable.star_45);
			}else if(4.75 <= stargrade){
				holder.appGrade.setBackgroundResource(R.drawable.star_5);
			}
		}

		holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showInfo();                 
          	}
		});
		
		//holder.appIcon.setBackgroundResource(R.drawable.download);
		//holder.appGrade.setBackgroundResource(R.drawable.star_35);
//		holder.appIcon.setOnClickListener(new ImageView.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				showInfo("imageview");                 
//          	}
//		});

		if (holder.appIcon != null) {
			if (null != application.getAppIcon()
					&& application.getAppIcon().trim().length() > 0) {
				final ProgressBar pbar = holder.pbar;

				imageLoader.init(ImageLoaderConfiguration
						.createDefault(activity));
				imageLoader.displayImage(application.getAppIcon(), holder.appIcon,
						options, new ImageLoadingListener() {
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

			} else {
				holder.appIcon.setImageResource(R.drawable.ic_launcher);
			}
		}
		return view;
	}

	public class ViewHolder {
		public TextView appName, appDesc, appId, categoryName;
		public ImageButton downloadBtn;
		private ImageView appIcon, appGrade;
		private ProgressBar pbar;
	}
	
	public void showInfo(){
		AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this.getContext());
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
}
