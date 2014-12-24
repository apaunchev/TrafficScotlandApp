package adapters;

/**
 * @author Angel Paunchev (S1105400)
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import models.FeedItem;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.me.trafficscotlandapp.R;

public class ItemAdapter extends BaseAdapter {
	
	private static class ViewHolder {
		LinearLayout details;
		ProgressBar progressBar;
		View indicator;
		TextView title;
		TextView description;
	}
	
	private Context context;
	private List<FeedItem> list;
	private String feedType;
	
	public ItemAdapter() {
	}
	
	public ItemAdapter(Context context, List<FeedItem> list, String feedType) {
		this.context = context;
		this.list = list;
		this.feedType = feedType;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public FeedItem getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.adapter_item_row, parent, false);
			holder = new ViewHolder();
			holder.details = (LinearLayout) convertView.findViewById(R.id.item_details);
			holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
			holder.indicator = (View) convertView.findViewById(R.id.item_indicator);
			holder.title = (TextView) convertView.findViewById(R.id.item_title);
			holder.description = (TextView) convertView.findViewById(R.id.item_description);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		// Hide progress bar when in portrait mode.
		if (context.getResources().getConfiguration().orientation
			== Configuration.ORIENTATION_PORTRAIT) {
			holder.progressBar.setVisibility(View.GONE);
		}
		
		// Adjust layout for landscape mode.
		if (context.getResources().getConfiguration().orientation
			== Configuration.ORIENTATION_LANDSCAPE) {
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
					0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
			holder.details.setLayoutParams(p);
		}

        FeedItem item = getItem(position);
		
		long now = new Date().getTime();
		long startTime = item.getStartDate().getTime();
		long endTime = item.getEndDate().getTime();
        
        long duration = endTime - startTime;
		long remaining = endTime - now;
		
		int remainingFromNow = (int) (remaining / (1000 * 60 * 60 * 24)) + 1;
		
		int indicator = 0;
		
		int progressMax = (int) (duration / (1000 * 60 * 60 * 24));
		int progressCurrent = progressMax - (int) (remaining / (1000 * 60 * 60 * 24));
		
		/**
		 * Roadworks that will be completed within:
		 * a week get a green indicator;
		 * a month - an orange one;
		 * otherwise - a red one.
		 */
		if (remainingFromNow <= 7) {
			indicator = R.color.green;
		} else if (remainingFromNow > 7 && remainingFromNow <= 30) {
			indicator = R.color.orange;
		} else {
			indicator = R.color.red;
		}

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        String delayInfo = (item.getAdditionalInfo() != null) ? "Delay information available"
        		: "No reported delay";
        
        GradientDrawable shape = (GradientDrawable) holder.indicator.getBackground();
        shape.setColor(convertView.getResources().getColor(indicator));
        
        holder.title.setText(item.getTitle());
        
        if (feedType.equals("current")) {
        	// For current roadworks, display when they end:
        	holder.description.setText("Ends: " + dateFormat.format(item.getEndDate()).toString()
    			+ " - " + delayInfo);
        } else if (feedType.equals("planned")) {
        	// For planned roadworks, display when they start:
        	holder.description.setText("Starts: " + dateFormat.format(item.getStartDate())
        		.toString());
        }
		
        holder.progressBar.setMax(progressMax + 1);
        holder.progressBar.setProgress(progressCurrent);

        return convertView;
	}
	
	public void changeAdapterList(List<FeedItem> list) {
		this.list = list;
		notifyDataSetChanged();
	}
	
}
