package com.sds.ssa.fragment.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.sds.ssa.R;
import com.sds.ssa.adapter.ScreenshotAdapter;

public class ScreenshotActivity extends Activity {
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.screenshot_main);
	    
	    Intent intent = getIntent();
		int order;
		if(intent.hasExtra("order")){
			order = Integer.parseInt(intent.getExtras().getString("order"));
		}else{
			order = 0;
		}

	    ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
	    //ScreenshotAdapter adapter = new ScreenshotAdapter(this, order);
	    ScreenshotAdapter adapter = new ScreenshotAdapter(this);
	    viewPager.setAdapter(adapter);
	  }

}
