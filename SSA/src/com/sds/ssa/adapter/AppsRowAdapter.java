package com.sds.ssa.adapter;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.sds.ssa.R;

public class AppsRowAdapter extends ArrayAdapter<Application> {

	private Activity activity;
	private List<Application> applicationList;
	private Application application;
	private int row;
	private DisplayImageOptions options;
	ImageLoader imageLoader;

	public AppsRowAdapter(Activity act, int resource, List<Application> appList) {
		super(act, resource, appList);
		this.activity = act;
		this.row = resource;
		this.applicationList = appList;
		
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.profile)
				.showImageForEmptyUrl(R.drawable.profile).cacheInMemory()
				.cacheOnDisc().build();
		imageLoader = ImageLoader.getInstance();
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
		holder.downloadBtn = (ImageButton) view.findViewById(R.id.imageView1);

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

		holder.downloadBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showInfo();                 
          	}
		});
		
		//holder.appIcon.setBackgroundResource(R.drawable.download);
		holder.appGrade.setBackgroundResource(R.drawable.star_35);
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
