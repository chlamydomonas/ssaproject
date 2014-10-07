package com.sds.bas.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.sds.bas.phone.PhoneActivity;
import com.sds.bas.tablet.main.ItemListActivity;
import com.sds.bas.util.Utils;
import com.sds.bas.vo.UserInfo;

public class BasActivity extends Activity {

	private String userInfoParam;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		
		if(intent.hasExtra("userInfo")){
			userInfoParam = intent.getExtras().getString("userInfo");
			UserInfo userInfo = Utils.getUserInfo(userInfoParam);

			UserInfo loginUserInfo = (UserInfo)getApplicationContext();
			loginUserInfo.setUserId(userInfo.getUserId());
			loginUserInfo.setUserDept(userInfo.getUserDept());
			loginUserInfo.setSecurityLevel(userInfo.getSecurityLevel());
			loginUserInfo.setInstalledAppInfoList(userInfo.getInstalledAppInfoList());
		}
		
		boolean isPhone = true;
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width=dm.widthPixels;
		int height=dm.heightPixels;
		int dens=dm.densityDpi;
		double wi=(double)width/(double)dens;
		double hi=(double)height/(double)dens;
		double x = Math.pow(wi,2);
		double y = Math.pow(hi,2);
		double screenInches = Math.sqrt(x+y);
		
		Log.v("bas", Build.VERSION.RELEASE); // 4.4.2
		Log.v("bas", Integer.toString(Build.VERSION.SDK_INT)); //19
		Log.v("bas", Double.toString(screenInches)); //4.41021541423999
		
		int deviceSdkVersion = Build.VERSION.SDK_INT;
		
		if(deviceSdkVersion < 10){ // phone
			isPhone = true;
			
		}else if(11 <= deviceSdkVersion &&  deviceSdkVersion <= 13){ // tablet
			isPhone = false;
			
		}else if(deviceSdkVersion >= 14){
			int smaller = width;
			if(width > height){
				smaller = height;
			}
			if(screenInches > 7.0 && smaller > 700){
				isPhone = false;
			}else{
				isPhone = true;
			}
		}
		
		if(isPhone){
			Intent phoneIntent = new Intent(getApplicationContext(), PhoneActivity.class);
			phoneIntent.putExtra("userInfo", userInfoParam);
			startActivity(phoneIntent);
			finish();
		}else{
			Intent tabletIntent = new Intent(getApplicationContext(), ItemListActivity.class);
			tabletIntent.putExtra("userInfo", userInfoParam);
			startActivity(tabletIntent);
			finish();
		}
	}

}
