package models;

/**
 * @author Angel Paunchev (S1105400)
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FeedItemRepository {

	private List<FeedItem> list;
	private String feedType;
	
	public FeedItemRepository() {
		list = new ArrayList<FeedItem>();
	}
	
	public List<FeedItem> getList() {
		return list;
	}
	
	public void setList(List<FeedItem> list) {
		this.list = list; 
	}
	
	public String getFeedType() {
		return feedType;
	}
	
	public void setFeedType(String feedType) {
		this.feedType = feedType;
	}
	
	public FeedItem getItemByTitle(String title) {
		FeedItem result = null;
		
		for (FeedItem item : list) {
			if (item.getTitle().equals(title)) {
				result = item;
			}
		}
		
		return result;
	}

	public List<FeedItem> filterByRoad(String roadName) {
		List<FeedItem> filteredList = new ArrayList<FeedItem>();
		
		for (FeedItem item : list) {
			if (item.getTitle().toLowerCase().contains(roadName.toLowerCase())) {
				filteredList.add(item);
			}
		}
		
		return filteredList;
	}
	
	public List<FeedItem> filterByDate(Date date) {
		List<FeedItem> filteredList = new ArrayList<FeedItem>();
		
		for (FeedItem item : list) {
			if (item.getStartDate().before(date) && item.getEndDate().after(date)) {
				filteredList.add(item);
			}
		}
		
		return filteredList;
	}
	
}
