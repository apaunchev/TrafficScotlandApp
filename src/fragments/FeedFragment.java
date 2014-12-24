package fragments;

/**
 * @author Angel Paunchev (S1105400)
 */

import helpers.DownloadXmlTask;

import java.util.Date;
import java.util.List;

import models.FeedItem;
import models.FeedItemRepository;
import adapters.ItemAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

public class FeedFragment extends ListFragment implements DownloadXmlTask.DownloadXmlTaskListener {
	
	private FeedItemRepository repository = new FeedItemRepository();
	private ItemAdapter adapter = new ItemAdapter();
	private FeedFragmentListener listener;
	
	public static FeedFragment newInstance(String feedType, String feedUrl) {
		FeedFragment f = new FeedFragment();
		Bundle args = new Bundle();
		args.putString("feedType", feedType);
		args.putString("feedUrl", feedUrl);
        f.setArguments(args);
        return f;
    }
	
	public FeedFragment() {
	}
	
	public interface FeedFragmentListener {
		public void onItemSelected(FeedItem item);
    }
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        try {
            listener = (FeedFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
            	" must implement FeedFragmentListener");
        }
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle args = getArguments();
		repository.setFeedType(args.getString("feedType"));
		
		// Start AsyncTask to download and parse the feed.
		new DownloadXmlTask(this).execute(args.getString("feedUrl"));
	}
	
	@Override
	public void onStart() {
	    super.onStart();
	    // Set empty view for when the list is empty.
	    getListView().setEmptyView(noItems("No roadworks available for the selected filters."));
	}

	@Override
	public void onTaskCompleted(List<FeedItem> list) {
		repository.setList(list);
		Bundle args = getArguments();
		adapter = new ItemAdapter(getActivity(), repository.getList(), args.getString("feedType"));
		setListAdapter(adapter);
	}
	
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);
		listener.onItemSelected(adapter.getItem(position));
	}
	
	private TextView noItems(String text) {
	    TextView emptyView = new TextView(getActivity());
	    emptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
	            LayoutParams.MATCH_PARENT));
	    emptyView.setText(text);
	    emptyView.setTextSize(16);
	    emptyView.setVisibility(View.GONE);
	    emptyView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

	    // Add the empty view to the list view.
	    ((ViewGroup) getListView().getParent()).addView(emptyView);

	    return emptyView;
	}
	
	public void filterByRoad(String roadName) {
		List<FeedItem> newList = repository.filterByRoad(roadName);
		adapter.changeAdapterList(newList);
	}
	
	public void filterByDate(Date date) {
		List<FeedItem> newList = repository.filterByDate(date);
		adapter.changeAdapterList(newList);
	}
	
	public void resetFilters() {
		adapter.changeAdapterList(repository.getList());
	}
	
	public void updateFeed() {
		Bundle args = getArguments();
		new DownloadXmlTask(this).execute(args.getString("feedUrl"));
		getListView().smoothScrollToPositionFromTop(0, 0, 500);
	}
	
}
