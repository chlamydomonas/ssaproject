package com.sds.ssa.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sds.ssa.R;
import com.sds.ssa.vo.Screenshot;

public class ScreenshotAdapter extends PagerAdapter {
	Context context;
	private ArrayList<Integer> items;
	int order;
	List<Screenshot> screenshotList;

	public ScreenshotAdapter(Context context, int order,
			List<Screenshot> screenshotList) {
		super();
    	this.context=context;
    	items = new ArrayList<Integer>();
    	this.order = order;
    	this.screenshotList = screenshotList;
    	
    	for(int i=0; i<screenshotList.size(); i++){
    		items.add(i);	
    	}
	}

	@Override
    public int getCount() {
		return items == null ? 0:items.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
      return view == ((ImageView) object);
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//      ImageView imageView = new ImageView(context);
//      int padding = context.getResources().getDimensionPixelSize(R.dimen.padding_medium);
//      imageView.setPadding(padding, padding, padding, padding);
//      imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//      imageView.setImageResource(GalImages[position]);
//      ((ViewPager) container).addView(imageView, 0);
//      return imageView;
//    }

	@Override public Object instantiateItem(View pager, int position){
		//View v = BkUtils.getView(context);	//해당 포지션의 아이템 뷰를 생성한다
		//((ViewPager)pager).addView(v);
		//return v;
		
		
		ImageView imageView = new ImageView(context);
		imageView.setImageResource(R.drawable.filled_star);
		View v = imageView;
		((ViewPager)pager).addView(v);
		return v;
	}
	
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      ((ViewPager) container).removeView((ImageView) object);
    }

 	@Override public void finishUpdate(View arg0) {}
 	@Override public void restoreState(Parcelable arg0, ClassLoader arg1) {}
 	@Override public Parcelable saveState() { return null; }
 	@Override public void startUpdate(View arg0) {}
 	
 	/**
 	 * 동적으로 아이템을 추가하는 메소드
 	 * @param type - 아이템 타입
 	 */
 	public void addItem(int type){
 		items.add(type);				//아이템 목록에 추가
 		notifyDataSetChanged();		//아답터에 데이터 변경되었다고 알림. 알아서 새로고침
 	}
  }
