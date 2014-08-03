package com.sds.ssa.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.sds.ssa.R;

public class ScreenshotAdapter extends PagerAdapter {
	Context context;
    private int[] GalImages = new int[] {
    		R.drawable.china, R.drawable.india,
			R.drawable.unitedstates, R.drawable.indonesia,
			R.drawable.brazil, R.drawable.pakistan, R.drawable.nigeria,
			R.drawable.bangladesh, R.drawable.russia, R.drawable.japan 
    };
    public ScreenshotAdapter(Context context){
    	this.context=context;
    }

	@Override
    public int getCount() {
      return GalImages.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
      return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
      ImageView imageView = new ImageView(context);
      int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_medium);
      imageView.setPadding(padding, padding, padding, padding);
      imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
      imageView.setImageResource(GalImages[position]);
      ((ViewPager) container).addView(imageView, 0);
      return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      ((ViewPager) container).removeView((ImageView) object);
    }
  }
