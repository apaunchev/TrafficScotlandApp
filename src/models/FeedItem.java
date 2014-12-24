package models;

/**
 * @author Angel Paunchev (S1105400)
 */

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

// Implement Parcelable so we can pass objects between activities easily.
public class FeedItem implements Parcelable {
    private String title;
    private Date startDate;
    private Date endDate;
    private String additionalInfo;

    public FeedItem(String title, Date startDate, Date endDate, String additionalInfo) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.additionalInfo = additionalInfo;
    }
    
    private FeedItem(Parcel in) {
        title = in.readString();
        startDate = new Date(in.readLong());
        endDate = new Date(in.readLong());
        additionalInfo = in.readString();
    }
    
    public static final Parcelable.Creator<FeedItem> CREATOR = new Parcelable.Creator<FeedItem>() {
        public FeedItem createFromParcel(Parcel in) {
            return new FeedItem(in);
        }

        public FeedItem[] newArray(int size) {
            return new FeedItem[size];
        }
    };
    
    @Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(title);
		out.writeLong(startDate.getTime());
		out.writeLong(endDate.getTime());
		out.writeString(additionalInfo);
	}
    
    @Override
	public int describeContents() {
		return 0;
	}
    
    public String getTitle() {
		return title;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public String toString() {
    	return title;
    }
    
}
