package com.sds.ssa.tablet.sub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.sds.ssa.R;
import com.sds.ssa.tablet.ItemListActivity;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tablet_activity_app_list);

		// The detail container view will be present only in the
		// large-screen layouts (res/values-large and
		// res/values-sw600dp). If this view is present, then the
		// activity should be in two-pane mode.

		
		Bundle b = getIntent().getExtras();

		String name = b.getString("name");
		String summary = b.getString("summary");
		String desc = b.getString("desc");
		String manual = b.getString("manual");
		String categoryname = b.getString("categoryname");
		String create = b.getString("created");
		String verName = b.getString("verName");
		final String downloadUrl = b.getString("downloadUrl");
		
		
		// In two-pane mode, list items should be given the
		// 'activated' state when touched.
		((AppListFragment) getFragmentManager().findFragmentById(
				R.id.app_list)).setActivateOnItemClick(true);
		
		
		AppDetailFragment fragment = new AppDetailFragment(b.getString("id"));
		getFragmentManager().beginTransaction()
				.replace(R.id.app_detail_container, fragment).commit();
	}

	/**
	 * Callback method from {@link AppListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		// In two-pane mode, show the detail view in this activity by
		// adding or replacing the detail fragment using a
		// fragment transaction.
		AppDetailFragment fragment = new AppDetailFragment(id);
		getFragmentManager().beginTransaction()
				.replace(R.id.app_detail_container, fragment).commit();
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
