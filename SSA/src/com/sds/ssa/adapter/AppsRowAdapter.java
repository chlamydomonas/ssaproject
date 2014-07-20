package com.sds.ssa.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
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
		holder.imgView = (ImageView) view.findViewById(R.id.image);
		holder.pbar = (ProgressBar) view.findViewById(R.id.pbar);

		if (holder.appName != null && null != application.getName()
				&& application.getName().trim().length() > 0) {
			holder.appName.setText(Html.fromHtml(application.getName()));
		}
		if (holder.appDesc != null && null != application.getDescription()
				&& application.getVersion().trim().length() > 0) {
			holder.appDesc.setText(Html.fromHtml(application.getDescription()));
		}
		if (holder.appId != null && null != application.getId()
				&& application.getId().trim().length() > 0) {
			holder.appId.setText(Html.fromHtml(application.getId()));
		}
		if (holder.imgView != null) {
			if (null != application.getLink()
					&& application.getLink().trim().length() > 0) {
				final ProgressBar pbar = holder.pbar;

				imageLoader.init(ImageLoaderConfiguration
						.createDefault(activity));
				imageLoader.displayImage(application.getLink(), holder.imgView,
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
				holder.imgView.setImageResource(R.drawable.ic_launcher);
			}
		}
		return view;
	}

	public class ViewHolder {
		//public TextView tvName, tvCity, tvBDate, tvGender, tvAge;
		public TextView appName, appDesc, appId;
		private ImageView imgView;
		private ProgressBar pbar;
	}
}
