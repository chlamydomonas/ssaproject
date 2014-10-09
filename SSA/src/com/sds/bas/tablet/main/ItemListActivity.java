package com.sds.bas.tablet.main;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.sds.ssa.R;

/**
 * An activity representing a list of Items. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link ItemDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ItemListFragment} and the item details (if present) is a
 * {@link ItemDetailFragment}.
 * <p>
 * This activity also implements the required {@link ItemListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class ItemListActivity extends ActionBarActivity implements
		ItemListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tablet_activity_item_list);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		if (findViewById(R.id.item_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((ItemListFragment) getFragmentManager().findFragmentById(
					R.id.item_list)).setActivateOnItemClick(true);
			
			ItemDetailFragment fragment = new ItemDetailFragment("ALL", null);
			getFragmentManager().beginTransaction()
					.replace(R.id.item_detail_container, fragment).commit();
		}
	}

	/**
	 * Callback method from {@link ItemListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id, String searchWord) {
		 if(id.equals("UPDATE")){
			 setTitle(R.string.app_name);
			 ItemUpdateDetailFragment fragment = new ItemUpdateDetailFragment(id);
				getFragmentManager().beginTransaction()
						.replace(R.id.item_detail_container, fragment).commit();
		 }else if(id.equals("SEARCH")){
			 setTitle(searchWord);
			 ItemDetailFragment fragment = new ItemDetailFragment(id, searchWord);
				getFragmentManager().beginTransaction()
						.replace(R.id.item_detail_container, fragment).commit();
		 }else{
			 setTitle(R.string.app_name);
			 ItemDetailFragment fragment = new ItemDetailFragment(id, null);
				getFragmentManager().beginTransaction()
						.replace(R.id.item_detail_container, fragment).commit();
		 }
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case android.R.id.home:
        	finish();
        default:
            return super.onOptionsItemSelected(item);
        }
	}
}
