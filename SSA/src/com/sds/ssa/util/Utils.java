package com.sds.ssa.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.sds.ssa.R;
import com.sds.ssa.vo.InstalledAppInfo;
import com.sds.ssa.vo.UserInfo;

public class Utils {
	
	static List<InstalledAppInfo> installedAppInfoList;
	static UserInfo userInfo;
	
	public static String getJSONString(String url) {
		String jsonString = null;
		HttpURLConnection linkConnection = null;
		try {
			URL linkurl = new URL(url);
			linkConnection = (HttpURLConnection) linkurl.openConnection();
			int responseCode = linkConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream linkinStream = linkConnection.getInputStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int j = 0;
				while ((j = linkinStream.read()) != -1) {
					baos.write(j);
				}
				byte[] data = baos.toByteArray();
				jsonString = new String(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (linkConnection != null) {
				linkConnection.disconnect();
			}
		}
		return jsonString;
	}

	public static boolean isNetworkAvailable(Activity activity) {
		ConnectivityManager connectivity = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static UserInfo getUserInfo(String userInfoParam) {
		installedAppInfoList = new ArrayList<InstalledAppInfo>();
		userInfo = new UserInfo();
		
		try {
			JSONObject userInfoJson = new JSONObject(userInfoParam);
			JSONObject userInfoObj = userInfoJson.getJSONObject("userInfo");
	
			userInfo.setUserId((String) userInfoObj.get("userId"));
			userInfo.setUserDept((String) userInfoObj.get("userDept"));
			userInfo.setSecurityLevel((String) userInfoObj.get("securityLevel"));
	
			JSONArray installedAppListArray = userInfoObj.getJSONArray("installedAppList");
	
			for (int i = 0; i < installedAppListArray.length(); i++) {
				JSONObject installedAppListObj = installedAppListArray.getJSONObject(i);
		
				InstalledAppInfo installedAppInfo = new InstalledAppInfo();
				installedAppInfo.setAppId(installedAppListObj.getString("appId"));
				installedAppInfo.setAppVerCode(installedAppListObj.getString("appVerCode"));
				installedAppInfo.setAppInstalledDate(installedAppListObj.getString("appInstalledDate"));
				
				installedAppInfoList.add(installedAppInfo);
			}
			userInfo.setInstalledAppInfoList(installedAppInfoList);
			
		}catch (JSONException e) {
			e.printStackTrace();
		}
		return userInfo;
	}
	
	public static void showDownload(final String appDownloadUrl, final View v){
		AlertDialog.Builder alert_confirm = new AlertDialog.Builder(v.getContext());
		alert_confirm
		.setTitle(R.string.download)
		.setMessage(R.string.downloadMsg).setCancelable(false)
		.setPositiveButton(R.string.yes,
				new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	Intent intent = new Intent();
				intent.setClassName("com.sds.launcher", // Package name
						"com.sds.launcher.TestLauncher");
				intent.putExtra("appDownloadUrl", appDownloadUrl);
				v.getContext().startActivity(intent);
				
				android.os.Process.killProcess(android.os.Process.myPid());
		    }
		})
		.setNegativeButton(R.string.no,
		new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        // 'No'
		    return;
		    }
		});
		AlertDialog alert = alert_confirm.create();
		alert.show();
    }
}
