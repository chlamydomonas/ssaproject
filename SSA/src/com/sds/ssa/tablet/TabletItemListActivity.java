package com.sds.ssa.tablet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.sds.ssa.R;
import com.sds.ssa.search.SearchActivity;

/**
 * An activity representing a list of Items. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link TabletItemDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link TabletItemListFragment} and the item details (if present) is a
 * {@link TabletItemDetailFragment}.
 * <p>
 * This activity also implements the required {@link TabletItemListFragment.Callbacks}
 * interface to listen for item selections.
 */
public class TabletItemListActivity extends ActionBarActivity implements
		TabletItemListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tablet_activity_item_list);

		if (findViewById(R.id.item_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((TabletItemListFragment) getFragmentManager().findFragmentById(
					R.id.item_list)).setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
	}

	/**
	 * Callback method from {@link TabletItemListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	@Override
	public void onItemSelected(String id) {
		Log.e("bas", "onItemSelected(): " + mTwoPane);
		if (mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			TabletItemDetailFragment fragment = new TabletItemDetailFragment(id);
			getFragmentManager().beginTransaction()
					.replace(R.id.item_detail_container, fragment).commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this, TabletItemDetailActivity.class);
			detailIntent.putExtra(TabletItemDetailFragment.ARG_ITEM_ID, id);
			startActivity(detailIntent);
		}
	}
	
	private String id;

	/*
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		//super.onPrepareOptionsMenu(menu);
		Log.e("bas", "onPrepareOptionsMenu()");

		SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));
	    searchView.setQueryHint(this.getString(R.string.search));

	    ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text))
        .setHintTextColor(getResources().getColor(R.color.white));	    
	    searchView.setOnQueryTextListener(OnQuerySearchView);
					  
	    if(id != null && id.equals("UPDATE")) {
	    	menu.findItem(R.id.menu_update).setVisible(true);		
			menu.findItem(R.id.menu_search).setVisible(true);	
	    } else {
	    	menu.findItem(R.id.menu_update).setVisible(false);	
	    	//menu.findItem(R.id.menu_update).setVisible(true);
			menu.findItem(R.id.menu_search).setVisible(true);	
	    }
  	    
		searchCheck = false;	
		return true;
	}
	
	boolean searchCheck;
	private OnQueryTextListener OnQuerySearchView = new OnQueryTextListener() {
		
		@Override
		public boolean onQueryTextSubmit(String query) {
			
			return false;
		}
		
		@Override
		public boolean onQueryTextChange(String query) {
			if (searchCheck){
				Log.v("bas", "onQueryTextChange");
			}
			return false;
		}
	};
	*/
}
