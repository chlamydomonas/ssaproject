package com.sds.bas.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class Screenshot implements Parcelable {
	private String screenShotUrl;
	
	
	public Screenshot() {
    }
   
    public Screenshot(Parcel in) {
       readFromParcel(in);
    }

	public Screenshot(String screenShotUrl) {
         this.screenShotUrl = screenShotUrl;
    }
    
	public String getScreenShotUrl() {
		return screenShotUrl;
	}
	public void setScreenShotUrl(String screenShotUrl) {
		this.screenShotUrl = screenShotUrl;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(screenShotUrl);
	}
	
    private void readFromParcel(Parcel in) {
    	screenShotUrl = in.readString();
	}
    
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Screenshot createFromParcel(Parcel in) {
             return new Screenshot(in);
       }

       public Screenshot[] newArray(int size) {
            return new Screenshot[size];
       }
   };
}

