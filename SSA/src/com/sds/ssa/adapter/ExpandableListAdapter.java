package com.sds.ssa.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoadingListener;
import com.sds.ssa.R;
import com.sds.ssa.fragment.app.DetailActivity;
import com.sds.ssa.vo.Application;
import com.sds.ssa.vo.Category;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
 
    private Context context;
    private List<Category> listDataHeader;
    private HashMap<Category, List<Application>> listDataChild;
    
    // from "AppsRowAdater" start
    private Application application;
	private DisplayImageOptions options;
	ImageLoader imageLoader;
	// from "AppsRowAdater" end
	  
    public ExpandableListAdapter(Context context,
			List<Category> listDataHeader,
			HashMap<Category, List<Application>> listDataChild) {

    	this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listDataChild;
        
        // from "AppsRowAdater" start
        options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.profile)
				.showImageForEmptyUrl(R.drawable.profile).cacheInMemory()
				.cacheOnDisc().build();
        imageLoader = ImageLoader.getInstance();
        // from "AppsRowAdater" end
	}

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosititon);
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    @Override
    public View getChildView(int groupPosition, int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
    	
    	final int a = groupPosition;
    	final int b = childPosition;
        application = (Application) getChild(groupPosition, childPosition);
        
        /*
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.fragment2_list_item, null);
        }
 
        TextView listChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
 
        listChild.setText(childText);
        return convertView;
        */

        
        // From "AppsRowAdapter" start
        View view = convertView;
		ViewHolder holder;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			view = inflater.inflate(R.layout.fragment1_row1, null);

			holder = new ViewHolder();
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		holder.appName = (TextView) view.findViewById(R.id.appname);
		//holder.appVersion = (TextView) view.findViewById(R.id.appversion);
		holder.appDesc = (TextView) view.findViewById(R.id.appdesc);
		holder.appId = (TextView) view.findViewById(R.id.appid);
		holder.imgView = (ImageView) view.findViewById(R.id.image);
		holder.pbar = (ProgressBar) view.findViewById(R.id.pbar);

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
		
		if (holder.imgView != null) {
			if (null != application.getAppIcon()
					&& application.getAppIcon().trim().length() > 0) {
				final ProgressBar pbar = holder.pbar;

				imageLoader.init(ImageLoaderConfiguration
						.createDefault(context));
				imageLoader.displayImage(application.getAppIcon(), holder.imgView,
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
		view.setOnClickListener(new OnClickListener() {
			//application = (Application) getChild(groupPosition, childPosition);
			
			@Override
			public void onClick(View v) {
				
//				Application clickedApp = (Application) getChild(a, b);
//				Intent intent = new Intent(context, DetailActivity.class);
//				intent.putExtra("url", clickedApp.getLink());
//				intent.putExtra("name", clickedApp.getName());
//				intent.putExtra("desc", clickedApp.getDescription());
//				context.startActivity(intent);
				
				Application clickedApp = (Application) getChild(a, b);
				Toast.makeText(context, clickedApp.getAppName(),
						Toast.LENGTH_SHORT).show();
			}
		});
		return view;
		// From "AppsRowAdapter" end
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .size();
    }
 
    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
    	
        Category category = (Category) getGroup(groupPosition);
        String headerTitle = category.getName();
        
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.fragment2_list, null);
        }
 
        TextView listHeader = (TextView) convertView
                .findViewById(R.id.categoryName);
        listHeader.setTypeface(null, Typeface.BOLD);
        listHeader.setText(headerTitle);
 
        return convertView;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    
    // From "AppsRowAdapter" start
    public class ViewHolder {
		public TextView appName, appDesc, appId;
		private ImageView imgView;
		private ProgressBar pbar;
	}
    // From "AppsRowAdapter" end
}