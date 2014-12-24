package activities;

/**
 * @author Angel Paunchev (S1105400)
 */

import java.util.Calendar;
import java.util.Date;

import models.FeedItem;
import adapters.TabsPagerAdapter;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;

import com.me.trafficscotlandapp.R;

import fragments.AboutDialogFragment;
import fragments.FeedFragment;
import fragments.FeedFragment.FeedFragmentListener;
import fragments.FilterByDatePickerFragment;
import fragments.FilterByRoadDialogFragment;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener,
	FeedFragmentListener,
	FilterByRoadDialogFragment.FilterByRoadDialogListener,
	FilterByDatePickerFragment.FilterByDateDialogListener {

	private TabsPagerAdapter adapter;
    private ViewPager pager;
    private ActionBar bar;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        adapter = new TabsPagerAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(1); // 2 (tabs) - 1 = 1
        
        // Initialise ActionBar and add tabs.
        bar = getActionBar();
        bar.setHomeButtonEnabled(false);
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.addTab(bar.newTab().setText("Current roadworks").setTabListener(this));
        bar.addTab(bar.newTab().setText("Planned roadworks").setTabListener(this));
        
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                bar.setSelectedNavigationItem(position);
            }
         
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
         
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		pager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items.
	    switch (item.getItemId()) {
	        case R.id.action_filter_road:
	        	showFilterByRoadDialog();
	            return true;
	        case R.id.action_filter_date:
	        	showFilterByDatePicker();
	            return true;
	        case R.id.action_update_feed:
	        	updateFeed();
	        case R.id.action_reset_filters:
	        	resetFilters();
	        	return true;
	        case R.id.action_about:
	        	showAboutDialog();
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public void onItemSelected(FeedItem item) {
		// Start a new details activity for the selected item.
		Intent i = new Intent(this, ItemDetailActivity.class);
		i.putExtra("item_obj", (Parcelable) item);
		startActivity(i);
	}
	
	public void showFilterByRoadDialog() {
		DialogFragment dialog = new FilterByRoadDialogFragment();
        dialog.show(getSupportFragmentManager(), "FilterByRoadDialogFragment");
    }
	
	@Override
    public void onRoadDialogPositiveClick(DialogFragment dialog, String roadName) {
		if (roadName != null && !roadName.isEmpty()) {
			// Filter the current fragment's list by road name.
			FeedFragment f = (FeedFragment) getSupportFragmentManager()
				.findFragmentByTag("android:switcher:" + R.id.pager + ":" + pager.getCurrentItem());
			f.filterByRoad(roadName);
		}
    }
	
	public void showFilterByDatePicker() {
	    DialogFragment picker = new FilterByDatePickerFragment();
	    picker.show(getSupportFragmentManager(), "FilterByDatePickerFragment");
	}
	
	@Override
	public void onDateSetCompleted(DatePicker view, int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(year, month, day);
		Date date = new Date(c.getTimeInMillis());
		
		// Filter the current fragment's list by date.
		FeedFragment f = (FeedFragment) getSupportFragmentManager()
				.findFragmentByTag("android:switcher:" + R.id.pager + ":" + pager.getCurrentItem());
		f.filterByDate(date);
	}
	
	public void resetFilters() {
		// Iterate through all fragment instances and call their respective resetFilters() methods.
		for (int i = 0; i < adapter.getCount(); i++) {
			FeedFragment f = (FeedFragment) getSupportFragmentManager()
					.findFragmentByTag("android:switcher:" + R.id.pager + ":" + i);
			f.resetFilters();
		}
	}
	
	public void showAboutDialog() {
		DialogFragment dialog = new AboutDialogFragment();
		dialog.show(getSupportFragmentManager(), "AboutDialogFragment");
	}
	
	public void updateFeed() {
		// Update the current fragment's list by getting and parsing the feed again.
		FeedFragment f = (FeedFragment) getSupportFragmentManager()
				.findFragmentByTag("android:switcher:" + R.id.pager + ":" + pager.getCurrentItem());
		f.updateFeed();
	}
	
}
