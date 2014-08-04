package com.sds.ssa.fragment.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Window;

import com.sds.ssa.R;
import com.sds.ssa.adapter.ScreenshotAdapter;
import com.sds.ssa.vo.Screenshot;

public class ScreenshotActivity extends Activity {
	
	private List<Screenshot> screenshotList;
	private int order;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.screenshot_main);
	  	
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

	    ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
	    ScreenshotAdapter adapter = new ScreenshotAdapter(this, order, screenshotList);
	    viewPager.setAdapter(adapter);
	  }

}
