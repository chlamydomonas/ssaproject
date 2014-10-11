package com.sds.bas.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoadingListener;
import com.sds.bas.vo.Application;
import com.sds.bas.R;

public class TabletApplicationRowAdapter extends ArrayAdapter<Application> {

	private Activity activity;
	private List<Application> applicationList;
	private Application application;
	private int row;
	private DisplayImageOptions options;
	private int order;
	ImageLoader imageLoader;

	public TabletApplicationRowAdapter(Activity act, int resource, List<Application> appList, int order) {
		super(act, resource, appList);
		this.activity = act;
		this.row = resource;
		this.applicationList = appList;
		this.order = order;
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
				.showStubImage(R.drawable.noimage)
				.showImageForEmptyUrl(R.drawable.noimage).cacheInMemory()
				.cacheOnDisc().build();
		imageLoader = ImageLoader.getInstance();

		if(position==order){
			view.setBackgroundColor(Color.parseColor("#ffa500"));
		} else {
			view.setBackgroundColor(Color.WHITE);
		}

		if ((applicationList == null) || ((position + 1) > applicationList.size()))
			return view;

		application = applicationList.get(position);

		holder.appName = (TextView) view.findViewById(R.id.appname);
		//holder.appVersion = (TextView) view.findViewById(R.id.appversion);
		holder.appDesc = (TextView) view.findViewById(R.id.appdesc);
		holder.appId = (TextView) view.findViewById(R.id.appid);
		holder.categoryName = (TextView) view.findViewById(R.id.categoryname);
		holder.appIcon = (ImageView) view.findViewById(R.id.image);
		holder.pbar = (ProgressBar) view.findViewById(R.id.pbar);
		holder.isNewIcon = (ImageView) view.findViewById(R.id.isnewicon);

		String appUpdatedDate = application.getAppUploadedDate();
	    long timeInMillis;
	    long days = 0;
		try {
			timeInMillis = System.currentTimeMillis() -
					                    new SimpleDateFormat("yyyy-MM-dd").parse(appUpdatedDate).getTime();
			days = timeInMillis / (24L * 60 * 60 * 1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String isNewDay = getContext().getString(R.string.is_new_day);
	    if(days < Integer.parseInt(isNewDay)){
	    	holder.isNewIcon.setBackgroundResource(R.drawable.new_icon);
	    }else{
	    	holder.isNewIcon.setBackgroundResource(0);
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
		private ImageView appIcon, isNewIcon;
		private ProgressBar pbar;
	}
	
	public void setOrder(int order) {
		this.order = order;
	}
}
