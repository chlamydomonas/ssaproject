package com.sds.ssa.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoadingListener;
import com.sds.ssa.R;
import com.sds.ssa.util.Utils;
import com.sds.ssa.vo.Application;

public class TabletUpdateRowAdapter extends ArrayAdapter<Application> {

	private Activity activity;
	private List<Application> applicationList;
	private Application application;
	private int row;
	private DisplayImageOptions options;
	ImageLoader imageLoader;

	public TabletUpdateRowAdapter(Activity act, int resource, List<Application> appList) {
		super(act, resource, appList);
		this.activity = act;
		this.row = resource;
		this.applicationList = appList;
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

		if ((applicationList == null) || ((position + 1) > applicationList.size()))
			return view;

		application = applicationList.get(position);

		holder.appName = (TextView) view.findViewById(R.id.appname);
		holder.categoryName = (TextView) view.findViewById(R.id.categoryname);
		holder.appVerName = (TextView) view.findViewById(R.id.appvername);
		holder.created = (TextView) view.findViewById(R.id.created);
		holder.appIcon = (ImageView) view.findViewById(R.id.image);
		holder.pbar = (ProgressBar) view.findViewById(R.id.pbar);

		if (holder.appName != null && null != application.getAppName()
				&& application.getAppName().trim().length() > 0) {
			holder.appName.setText(Html.fromHtml(application.getAppName()));
		}

		if (holder.categoryName != null && null != application.getCategoryName()
				&& application.getCategoryName().trim().length() > 0) {
			holder.categoryName.setText(Html.fromHtml(application.getCategoryName()));
		}
		
		if (holder.appVerName != null && null != application.getAppVerName()
				&& application.getAppVerName().trim().length() > 0) {
			holder.appVerName.setText("Ver." + Html.fromHtml(application.getAppVerName()));
		}
		
		if (holder.created != null && null != application.getCreated()
				&& application.getCreated().trim().length() > 0) {
			holder.created.setText(Html.fromHtml(application.getCreated()));
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
		public TextView appName, categoryName, appVerName, created;
		private ImageView appIcon;
		private ProgressBar pbar;
	}
}
