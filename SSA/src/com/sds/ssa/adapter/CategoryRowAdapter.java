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
import com.sds.ssa.vo.Category;
import com.sds.ssa.R;

public class CategoryRowAdapter extends ArrayAdapter<Category> {

	private Activity activity;
	private List<Category> items;
	private Category objBean;
	private int row;
	private DisplayImageOptions options;
	ImageLoader imageLoader;

	public CategoryRowAdapter(Activity act, int resource, List<Category> arrayList) {
		super(act, resource, arrayList);
		this.activity = act;
		this.row = resource;
		this.items = arrayList;
		
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

		if ((items == null) || ((position + 1) > items.size()))
			return view;

		objBean = items.get(position);

		holder.categoryName = (TextView) view.findViewById(R.id.appname);
		//holder.appVersion = (TextView) view.findViewById(R.id.appversion);
		//holder.appDesc = (TextView) view.findViewById(R.id.appdesc);
		holder.categoryId = (TextView) view.findViewById(R.id.appid);
		//holder.imgView = (ImageView) view.findViewById(R.id.image);
		//holder.pbar = (ProgressBar) view.findViewById(R.id.pbar);

		if (holder.categoryName != null && null != objBean.getName()
				&& objBean.getName().trim().length() > 0) {
			holder.categoryName.setText(Html.fromHtml(objBean.getName()));
		}
		if (holder.categoryId != null && null != objBean.getId()
				&& objBean.getId().trim().length() > 0) {
			holder.categoryId.setText(Html.fromHtml(objBean.getId()));
		}
		
		/*
		if (holder.appDesc != null && null != objBean.getDescription()
				&& objBean.getVersion().trim().length() > 0) {
			holder.appDesc.setText(Html.fromHtml(objBean.getDescription()));
		}
		if (holder.appId != null && null != objBean.getId()
				&& objBean.getId().trim().length() > 0) {
			holder.appId.setText(Html.fromHtml(objBean.getId()));
		}
		if (holder.imgView != null) {
			if (null != objBean.getLink()
					&& objBean.getLink().trim().length() > 0) {
				final ProgressBar pbar = holder.pbar;

				imageLoader.init(ImageLoaderConfiguration
						.createDefault(activity));
				imageLoader.displayImage(objBean.getLink(), holder.imgView,
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
		}*/
		return view;
	}

	public class ViewHolder {
		//public TextView tvName, tvCity, tvBDate, tvGender, tvAge;
		public TextView categoryName, categoryId;
		//private ImageView imgView;
		//private ProgressBar pbar;
	}
}
