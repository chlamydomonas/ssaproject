package com.sds.bas.tablet.sub;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoadingListener;
import com.sds.bas.util.Utils;
import com.sds.bas.vo.Application;
import com.sds.ssa.R;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link AppListActivity} in two-pane mode (on tablets) or a
 * {@link AppDetailActivity} on handsets.
 */
public class AppUpdateDetailFragment extends Fragment {
	
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */

	private String url;
	private String name;
	private String categoryname;
	private String vername;
	private String appverdiff;
	private String downloadUrl;
	
	private DisplayImageOptions options;
	private ImageLoader imageLoader;	

	private ProgressBar pbar;
	private TextView appName, categoryName, appVerName, appVerDiff;
	private ImageView imgView;
	private Button downloadBtn;

	ListView listView;
	View rootView;
	
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public AppUpdateDetailFragment() {
	}

	public AppUpdateDetailFragment(Application application) {
		url = application.getAppIcon();
		name = application.getAppName();
		categoryname = application.getCategoryName();
		vername = application.getAppVerName();
		appverdiff = application.getAppVerDiff();
		downloadUrl = application.getAppDownloadUrl();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.update_detail,
				container, false);

		pbar = (ProgressBar) rootView.findViewById(R.id.pbardesc);
		appName = (TextView) rootView.findViewById(R.id.appname);
		categoryName = (TextView) rootView.findViewById(R.id.categoryname);
		appVerName = (TextView) rootView.findViewById(R.id.appvername);
		appVerDiff = (TextView) rootView.findViewById(R.id.appverdiff);
		imgView = (ImageView) rootView.findViewById(R.id.appicon);
		downloadBtn = (Button) rootView.findViewById(R.id.downloadbtn);

		appName.setText(name);
		categoryName.setText(categoryname);
		appVerName.setText(vername);
		appVerDiff.setText(appverdiff);
		
		downloadBtn.getLayoutParams().height = 80;
		downloadBtn.setLayoutParams(downloadBtn.getLayoutParams());
		downloadBtn.setText(R.string.update);
		
		downloadBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Utils.showDownload(downloadUrl, v);  
				
          	}
		});
		loadImageFromURL(url);
		return rootView;
	}

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
}
