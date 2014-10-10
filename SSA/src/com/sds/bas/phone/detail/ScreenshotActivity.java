package com.sds.bas.phone.detail;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.ImageLoadingListener;
import com.sds.bas.vo.Screenshot;
import com.sds.bas.R;

public class ScreenshotActivity extends Activity {
	
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	
	private ProgressBar pbar;
	private ImageView imgView;
	private ViewPager pager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

	    setContentView(R.layout.screenshot_pager);

	    List<Screenshot> screenshotList;
		int order;
		
	    Intent intent = getIntent();
		
		if(intent.hasExtra("order")){
			order = Integer.parseInt(intent.getExtras().getString("order"));
		}else{
			order = 0;
		}
		
		screenshotList = new ArrayList<Screenshot>();
		if(intent.hasExtra("list")){
			screenshotList = intent.getParcelableArrayListExtra("list");
		}else{
			screenshotList = new ArrayList<Screenshot>();
		}
		
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.noimage)
				.showImageForEmptyUrl(R.drawable.noimage).cacheInMemory()
				.cacheOnDisc().build();
		
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(new ImagePagerAdapter(screenshotList));
		pager.setCurrentItem(order);
	}
	
	private class ImagePagerAdapter extends PagerAdapter{
		private LayoutInflater inflater;
		private List<Screenshot> screenshotList;
		
		ImagePagerAdapter(List<Screenshot> screenshotList){
			this.screenshotList = screenshotList;
			inflater = getLayoutInflater();
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return screenshotList.size();
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			View imageLayout = inflater.inflate(R.layout.screenshot_pager_image, view, false);
			assert imageLayout != null;
			
			pbar = (ProgressBar) imageLayout.findViewById(R.id.loading);
			imgView = (ImageView) imageLayout.findViewById(R.id.image);
			
			imageLoader = ImageLoader.getInstance();
			imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
			imageLoader.displayImage(screenshotList.get(position).getScreenShotUrl(), imgView, options, new ImageLoadingListener() {
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
			view.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}
}
