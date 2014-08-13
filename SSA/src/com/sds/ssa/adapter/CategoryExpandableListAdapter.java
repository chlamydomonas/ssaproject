package com.sds.ssa.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
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
import com.sds.ssa.vo.Category;
import com.sds.ssa.vo.UserInfo;

public class CategoryExpandableListAdapter extends BaseExpandableListAdapter {
 
    private Context context;
    private List<Category> listDataHeader;
    private HashMap<Category, List<Application>> listDataChild;
    
    // from "AppsRowAdater" start
    private Application application;
    DisplayImageOptions options;
    ImageLoader imageLoader;
	// from "AppsRowAdater" end
	  
    public CategoryExpandableListAdapter(Context context,
			List<Category> listDataHeader,
			HashMap<Category, List<Application>> listDataChild) {

    	this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listDataChild;
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

    	// from "AppsRowAdater" start    	
    	options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.profile)
				.showImageForEmptyUrl(R.drawable.profile).cacheInMemory()
				.cacheOnDisc().build();
    	imageLoader = ImageLoader.getInstance();
        // from "AppsRowAdater" end
    	
        // From "AppsRowAdapter" start
        View view = convertView;
		ViewHolder holder;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			//view = inflater.inflate(R.layout.category_child, null);
			view = inflater.inflate(R.layout.category_child, parent, false);	
			
			holder = new ViewHolder();
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		application = (Application) getChild(groupPosition, childPosition);

		holder.appName = (TextView) view.findViewById(R.id.appname);
		holder.appDesc = (TextView) view.findViewById(R.id.appdesc);
		holder.appGrade = (ImageView) view.findViewById(R.id.stargrade);
		holder.created = (TextView) view.findViewById(R.id.created);
		holder.appIcon = (ImageView) view.findViewById(R.id.image);
		holder.pbar = (ProgressBar) view.findViewById(R.id.pbar);
		holder.downloadBtn = (ImageButton) view.findViewById(R.id.downloadBtn);
		holder.downloadBtn.setBackgroundResource(R.drawable.download_selector);
		
		UserInfo userInfo = (UserInfo)this.context;

		int downloadType = 0;
		for(int i=0; i < userInfo.getInstalledAppInfoList().size(); i++){
			int installedAppCode = Integer.parseInt(userInfo.getInstalledAppInfoList().get(i).getAppVerCode());
			int serverAppCode = Integer.parseInt(application.getAppVerCode());

			if(application.getAppId().equals(userInfo.getInstalledAppInfoList().get(i).getAppId())){
				if(installedAppCode == serverAppCode){
					downloadType = 2;
					holder.downloadBtn.setBackgroundResource(R.drawable.download_pressed);
					
				} else if(installedAppCode < serverAppCode){
					downloadType = 1;
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
		if (holder.created != null && null != application.getCreated()
				&& application.getCreated().trim().length() > 0) {
			holder.created.setText(Html.fromHtml(application.getCreated()));
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
				Utils.showDownload(application.getAppDownloadUrl(), v);            
          	}
		});
		
		if (holder.appIcon != null) {
			if (null != application.getAppIcon()
					&& application.getAppIcon().trim().length() > 0) {
				final ProgressBar pbar = holder.pbar;

				imageLoader.init(ImageLoaderConfiguration
						.createDefault(context));
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
		
//		view.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				
//				Application clickedApp = (Application) getChild(groupPosition, childPosition);
//				Intent intent = new Intent(context, DetailActivity.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intent.putExtra("url", clickedApp.getAppIcon());
//				intent.putExtra("name", clickedApp.getAppName());
//				intent.putExtra("categoryname", clickedApp.getCategoryName());
//				intent.putExtra("summary", clickedApp.getAppSummary());
//				intent.putExtra("desc", clickedApp.getAppDescription());
//				intent.putExtra("manual", clickedApp.getAppManual());
//				intent.putExtra("downloadUrl", clickedApp.getAppDownloadUrl());
//				intent.putExtra("created", clickedApp.getCreated());
//				intent.putExtra("verName", clickedApp.getAppVerName());
//				context.startActivity(intent);
//			}
//		});
		
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
            convertView = infalInflater.inflate(R.layout.category_group, null);
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
		public TextView appName, appDesc, appId, created;
		public ImageButton downloadBtn;
		private ImageView appIcon, appGrade;
		private ProgressBar pbar;
	}
}