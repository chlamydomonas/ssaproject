package com.sds.ssa.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoadingListener;
import com.sds.ssa.R;
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
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
    	
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
						.createDefault(context));
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