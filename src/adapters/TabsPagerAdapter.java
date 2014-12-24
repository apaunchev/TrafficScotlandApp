package adapters;

/**
 * @author Angel Paunchev (S1105400)
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import fragments.FeedFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	private static final int NUM_TABS = 2;
	
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
    	switch (index) {
	    	case 0:
	    		return FeedFragment.newInstance(
    				"current",
    				"http://www.trafficscotland.org/rss/feeds/roadworks.aspx"
	    		);
	    	case 1:
	    		return FeedFragment.newInstance(
    				"planned",
    				"http://www.trafficscotland.org/rss/feeds/plannedroadworks.aspx"
	    		);
    	}
    	
    	return null;
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }
}
