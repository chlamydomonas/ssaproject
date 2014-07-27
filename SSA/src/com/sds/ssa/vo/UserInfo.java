package com.sds.ssa.vo;

import java.util.List;
import android.app.Application;

public class UserInfo extends Application {
	private String userId;
	private String userDept;
	private String securityLevel;
	private List<InstalledAppInfo> installedAppInfoList;
	
	@Override
    public void onCreate() {
		userId = null;
		userDept = null;
		securityLevel = null;
		installedAppInfoList = null;
		
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
    
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserDept() {
		return userDept;
	}
	public void setUserDept(String userDept) {
		this.userDept = userDept;
	}
	public String getSecurityLevel() {
		return securityLevel;
	}
	public void setSecurityLevel(String securityLevel) {
		this.securityLevel = securityLevel;
	}
	public List<InstalledAppInfo> getInstalledAppInfoList() {
		return installedAppInfoList;
	}
	public void setInstalledAppInfoList(List<InstalledAppInfo> installedAppInfoList) {
		this.installedAppInfoList = installedAppInfoList;
	}
	
}
