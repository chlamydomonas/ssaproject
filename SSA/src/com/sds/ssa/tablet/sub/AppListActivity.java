package com.sds.ssa.tablet.sub;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.sds.ssa.R;
import com.sds.ssa.tablet.ItemListActivity;
import com.sds.ssa.vo.Application;

/**
 * An activity representing a list of Items. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link AppDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link AppListFragment} and the item details (if present) is a
 * {@link AppDetailFragment}.
 * <p>
 * This activity also implements the required {@link AppListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class AppListActivity extends ActionBarActivity implements
		AppListFragment.Callbacks {
	
	private List<Application> applicationList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tablet_activity_app_list);

		((AppListFragment) getFragmentManager().findFragmentById(
				R.id.app_list)).setActivateOnItemClick(true);

		Intent intent = getIntent();
		applicationList = new ArrayList<Application>();

		applicationList = intent.getParcelableArrayListExtra("list");
		int order = intent.getExtras().getInt("order");
		String selectedType = intent.getExtras().getString("selectedType");

		if(selectedType.equals("UPDATE")){
			AppUpdateDetailFragment fragment = new AppUpdateDetailFragment(applicationList.get(order));
			getFragmentManager().beginTransaction()
					.replace(R.id.app_detail_container, fragment).commit();
		}else{
			AppDetailFragment fragment = new AppDetailFragment(applicationList.get(order));
			getFragmentManager().beginTransaction()
					.replace(R.id.app_detail_container, fragment).commit();
		}
	}

	/**
	 * Callback method from {@link AppListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(Application application) {
		// In two-pane mode, show the detail view in this activity by
		// adding or replacing the detail fragment using a
		// fragment transaction.
		
		Intent intent = getIntent();
		String selectedType = intent.getExtras().getString("selectedType");

		if(selectedType.equals("UPDATE")){
			AppUpdateDetailFragment fragment = new AppUpdateDetailFragment(application);
			getFragmentManager().beginTransaction()
					.replace(R.id.app_detail_container, fragment).commit();
		}else{
			AppDetailFragment fragment = new AppDetailFragment(application);
			getFragmentManager().beginTransaction()
					.replace(R.id.app_detail_container, fragment).commit();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case android.R.id.home:
        	Intent intent = new Intent(this, ItemListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
	}
}
