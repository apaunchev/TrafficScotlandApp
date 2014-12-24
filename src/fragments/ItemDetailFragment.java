package fragments;

/**
 * @author Angel Paunchev (S1105400)
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import models.FeedItem;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.me.trafficscotlandapp.R;

public class ItemDetailFragment extends Fragment {
	
	private FeedItem item;

	public ItemDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey("item_obj")) {
			item = getArguments().getParcelable("item_obj");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

		if (item != null) {
			String title = item.getTitle();
			Date startDate = item.getStartDate();
			Date endDate = item.getEndDate();
			String additionalInfo = item.getAdditionalInfo();
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
			
			long now = new Date().getTime();
			long startTime = startDate.getTime();
			long endTime = endDate.getTime();
			
			long duration = endTime - startTime;
			long remaining = endTime - now;
			String remainingFromNow = String.valueOf(remaining / (1000 * 60 * 60 * 24) + 1);
			
			int progressMax = (int) (duration / (1000 * 60 * 60 * 24));
			int progressCurrent = progressMax - (int) (remaining / (1000 * 60 * 60 * 24));
			
			TextView titleTv = (TextView) rootView.findViewById(R.id.titleTv);
	        titleTv.setText(title);
	        
	        TextView startDateTv = (TextView) rootView.findViewById(R.id.startDateTv);
	        startDateTv.setText(dateFormat.format(startDate).toString());
	        
	        TextView endDateTv = (TextView) rootView.findViewById(R.id.endDateTv);
	        endDateTv.setText(dateFormat.format(endDate).toString());
	        
	        TextView timeRemainingTv = (TextView) rootView.findViewById(R.id.timeRemainingTv);
	        timeRemainingTv.setText(remainingFromNow + " " + getResources()
	        	.getString(R.string.days_from_today));
	        
	        ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
	        progressBar.setMax(progressMax + 1);
	        progressBar.setProgress(progressCurrent);
	        
	        TextView infoTv = (TextView) rootView.findViewById(R.id.infoTv);
	        if (additionalInfo != null && !additionalInfo.isEmpty()) {
		        infoTv.setText(additionalInfo);
	        } else {
	        	infoTv.setText("n/a");
	        }
		}

		return rootView;
	}
	
}
