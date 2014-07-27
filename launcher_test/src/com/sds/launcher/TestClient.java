package com.sds.launcher;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.sds.launcher.vo.InstalledAppInfo;
import com.sds.launcher.vo.UserInfo;

public class TestClient extends Activity implements View.OnClickListener {

	private static final String DATABASE_NAME = "myDB.db";
	private static final String DATABASE_TABLE_NAME = "INSTALLAPP";
	private static final String DATABASE_CREATE_TABLE = "create table if not exists "
	            + DATABASE_TABLE_NAME
	            + " (_id integer primary key autoincrement, "
	            + " app_id text not null, "
	            + " app_ver_code text not null, " + " app_install_date text not null)";
	private static final String DATABASE_DELETE_TABLE = "drop table if exists "
            + DATABASE_TABLE_NAME;
	
	List<InstalledAppInfo> installedAppInfoList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_client);

		// Open a new private SQLiteDatabase associated with this Context's
        // application package. Create database if it doesn't exist.
        SQLiteDatabase myDB = openOrCreateDatabase(DATABASE_NAME,
                Context.MODE_PRIVATE, null);

        // Create database table called "COUNTRY"
        myDB.execSQL(DATABASE_DELETE_TABLE);
        myDB.execSQL(DATABASE_CREATE_TABLE);

        // Create new rows (hard-coded value for the simplicity of the exercise)
        // and insert it into the table.
        ContentValues newRow = new ContentValues();

        // hard-coded for simplicity
        newRow.put("app_id", "appId_02");
        newRow.put("app_ver_code", "1");
        newRow.put("app_install_date", "2014-07-01");
        myDB.insert(DATABASE_TABLE_NAME, null, newRow);

        newRow = new ContentValues();
        newRow.put("app_id", "appId_11");
        newRow.put("app_ver_code", "2");
        newRow.put("app_install_date", "2014-07-01");
        myDB.insert(DATABASE_TABLE_NAME, null, newRow);

        newRow = new ContentValues();
        newRow.put("app_id", "appId_19");
        newRow.put("app_ver_code", "2");
        newRow.put("app_install_date", "2014-07-01");
        myDB.insert(DATABASE_TABLE_NAME, null, newRow);

        // Select columns to retrieve in the form of String array
        String[] resultColumns = new String[] { "_id", "app_id",
                "app_ver_code", "app_install_date" };
        Cursor cursor = myDB.query(DATABASE_TABLE_NAME, resultColumns, null,
                null, null, null, null, null);
        cursor.moveToFirst();
        
        installedAppInfoList = new ArrayList<InstalledAppInfo>();

        if (cursor != null) {
            while(cursor.isAfterLast() == false){
                InstalledAppInfo installedAppInfo = new InstalledAppInfo();
                installedAppInfo.setAppId(cursor.getString(cursor.getColumnIndex("app_id")));
                installedAppInfo.setAppVerCode(cursor.getString(cursor.getColumnIndex("app_ver_code")));
                installedAppInfo.setAppInstalledDate(cursor.getString(cursor.getColumnIndex("app_install_date")));
                
                installedAppInfoList.add(installedAppInfo);
                cursor.moveToNext();
            }
        }

        myDB.close();
        
		View loginButton = findViewById(R.id.button1);
		loginButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test_client, menu);
		return true;
	}

	@Override
	public void onClick(View v) {

		String rootUserInfo = "userInfo";
		String installedAppList = "installedAppList";
		
		JSONObject rootObj = new JSONObject();
		JSONObject userInfoObj = new JSONObject();
		
		JSONArray userAppInfoArray = new JSONArray();
		
		UserInfo userInfo = new UserInfo();
		userInfo.setUserId("admin");
		userInfo.setUserDept("deptId_01");
		userInfo.setSecurityLevel("1");
		
		try {
			userInfoObj.put("userId", userInfo.getUserId());
			userInfoObj.put("deptId", userInfo.getUserDept());
			userInfoObj.put("securityLevel", userInfo.getSecurityLevel());
			
			rootObj.put(rootUserInfo, userInfoObj);			

			for(int i=0; i<installedAppInfoList.size(); i++){
				JSONObject installAppInfo = new JSONObject();
				 
				installAppInfo.put("appId", installedAppInfoList.get(i).getAppId());
				installAppInfo.put("appVerCode", installedAppInfoList.get(i).getAppVerCode());
				installAppInfo.put("appInstalledDate", installedAppInfoList.get(i).getAppInstalledDate());
			 
				userAppInfoArray.put(installAppInfo);
			}
			userInfoObj.put(installedAppList, userAppInfoArray);
				
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Intent intent = new Intent();
		intent.setClassName("com.sds.ssa", // Package name
				"com.sds.ssa.activity.BasActivity");
		intent.putExtra(rootUserInfo, rootObj.toString());
		startActivity(intent);
	}
}
