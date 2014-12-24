package activities;

/**
 * @author Angel Paunchev (S1105400)
 */

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

import com.me.trafficscotlandapp.R;

import fragments.ItemDetailFragment;

public class ItemDetailActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_detail);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putParcelable("item_obj", getIntent().getParcelableExtra("item_obj"));
			
			ItemDetailFragment f = new ItemDetailFragment();
			f.setArguments(arguments);
			getSupportFragmentManager().beginTransaction().add(R.id.item_detail, f).commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
