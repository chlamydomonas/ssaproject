package com.sds.bas.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class Application implements Parcelable{
	private String appId;
	private String appName;
	private String appVerCode;
	private String appVerName;
	private String appPackageName;
	private String appIcon;
	private String appSummary;
	private String appDescription;
	private String appManual;
	private String appGrade;
	private String appGradeCount;
	private String appDownloadUrl;
	private String created;
	private String appUploadedDate;
	private String categoryId;
	private String categoryName;
	private String appVerDiff;
	private String appDownloaded;
	private String fileSize;
	
	public Application(){
	}
	
	public Application(Parcel in){
		readFromParcel(in);
	}
	
	public Application(String appId,
			String appName,
			String appVerCode,
			String appVerName,
			String appPackageName,
			String appIcon,
			String appSummary,
			String appDescription,
			String appManual,
			String appGrade,
			String appGradeCount,
			String appDownloadUrl,
			String created,
			String appUploadedDate,
			String categoryId,
			String categoryName,
			String appVerDiff,
			String appDownloaded,
			String fileSize){
		this.appId = appId;
		this.appName = appName;
		this.appVerCode = appVerCode;
		this.appVerName = appVerName;
		this.appPackageName = appPackageName;
		this.appIcon = appIcon;
		this.appSummary = appSummary;
		this.appDescription = appDescription;
		this.appManual = appManual;
		this.appGrade = appGrade;
		this.appGradeCount = appGradeCount;
		this.appDownloadUrl = appDownloadUrl;
		this.created = created;
		this.appUploadedDate = appUploadedDate;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.appVerDiff = appVerDiff;
		this.appDownloaded = appDownloaded;
		this.fileSize = fileSize;
	}
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppVerCode() {
		return appVerCode;
	}
	public void setAppVerCode(String appVerCode) {
		this.appVerCode = appVerCode;
	}
	public String getAppVerName() {
		return appVerName;
	}
	public void setAppVerName(String appVerName) {
		this.appVerName = appVerName;
	}
	public String getAppPackageName() {
		return appPackageName;
	}
	public void setAppPackageName(String appPackageName) {
		this.appPackageName = appPackageName;
	}
	public String getAppIcon() {
		return appIcon;
	}
	public void setAppIcon(String appIcon) {
		this.appIcon = appIcon;
	}
	public String getAppSummary() {
		return appSummary;
	}
	public void setAppSummary(String appSummary) {
		this.appSummary = appSummary;
	}
	public String getAppDescription() {
		return appDescription;
	}
	public void setAppDescription(String appDescription) {
		this.appDescription = appDescription;
	}
	public String getAppManual() {
		return appManual;
	}
	public void setAppManual(String appManual) {
		this.appManual = appManual;
	}
	public String getAppGrade() {
		return appGrade;
	}
	public void setAppGrade(String appGrade) {
		this.appGrade = appGrade;
	}
	public String getAppGradeCount() {
		return appGradeCount;
	}
	public void setAppGradeCount(String appGradeCount) {
		this.appGradeCount = appGradeCount;
	}
	public String getAppDownloadUrl() {
		return appDownloadUrl;
	}
	public void setAppDownloadUrl(String appDownloadUrl) {
		this.appDownloadUrl = appDownloadUrl;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getAppUploadedDate() {
		return appUploadedDate;
	}
	public void setAppUploadedDate(String appUploadedDate) {
		this.appUploadedDate = appUploadedDate;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getAppVerDiff() {
		return appVerDiff;
	}
	public void setAppVerDiff(String appVerDiff) {
		this.appVerDiff = appVerDiff;
	}
	public String getAppDownloaded() {
		return appDownloaded;
	}
	public void setAppDownloaded(String appDownloaded) {
		this.appDownloaded = appDownloaded;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(appId);
		dest.writeString(appName);
		dest.writeString(appVerCode);
		dest.writeString(appVerName);
		dest.writeString(appPackageName);
		dest.writeString(appIcon);
		dest.writeString(appSummary);
		dest.writeString(appDescription);
		dest.writeString(appManual);
		dest.writeString(appGrade);
		dest.writeString(appGradeCount);
		dest.writeString(appDownloadUrl);
		dest.writeString(created);
		dest.writeString(appUploadedDate);
		dest.writeString(categoryId);
		dest.writeString(categoryName);
		dest.writeString(appVerDiff);
		dest.writeString(appDownloaded);
		dest.writeString(fileSize);
	}
	private void readFromParcel(Parcel in) {
		appId = in.readString();
    	appName = in.readString();
    	appVerCode = in.readString();
    	appVerName = in.readString();
    	appPackageName = in.readString();
    	appIcon = in.readString();
    	appSummary = in.readString();
    	appDescription = in.readString();
    	appManual = in.readString();
    	appGrade = in.readString();
    	appGradeCount = in.readString();
    	appDownloadUrl = in.readString();
    	created = in.readString();
    	appUploadedDate = in.readString();
    	categoryId = in.readString();
    	categoryName = in.readString();
    	appVerDiff = in.readString();
    	appDownloaded = in.readString();
    	fileSize = in.readString();
	}
    
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Application createFromParcel(Parcel in) {
             return new Application(in);
       }

       public Screenshot[] newArray(int size) {
            return new Screenshot[size];
       }
   };
}
