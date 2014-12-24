package helpers;

/**
 * @author Angel Paunchev (S1105400)
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.List;

import models.FeedItem;

import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;

public class DownloadXmlTask extends AsyncTask<String, Object, List<FeedItem>> {
	
	public interface DownloadXmlTaskListener {
		void onTaskCompleted(List<FeedItem> result);
	}
	
	private DownloadXmlTaskListener listener;
	private List<FeedItem> list;
	
	public DownloadXmlTask(DownloadXmlTaskListener listener) {
		this.listener = listener;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
    protected List<FeedItem> doInBackground(String... urls) {
		try {
			list = loadXmlFromNetwork(urls[0]);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return list;
    }

    @Override
    protected void onPostExecute(List<FeedItem> result) {
    	super.onPostExecute(result);
    	listener.onTaskCompleted(result);
    }
    
    public List<FeedItem> loadXmlFromNetwork(String urlString) throws XmlPullParserException,
    	IOException, ParseException {
	    InputStream stream = null;
	    List<FeedItem> list = null;
	    TSXmlParser parser = new TSXmlParser();

	    try {
	         stream = downloadUrl(urlString);
	         list = parser.parse(stream);
	    } finally {
	        if (stream != null) {
	            stream.close();
	        }
	    }
	    
	    return list;
	}

	private InputStream downloadUrl(String urlString) throws IOException {
	    URL url = new URL(urlString);
	    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    
	    conn.setReadTimeout(10000); // milliseconds
	    conn.setConnectTimeout(15000); // milliseconds
	    conn.setRequestMethod("GET");
	    conn.setDoInput(true);
	    conn.connect();
	    
	    InputStream stream = conn.getInputStream();
	    
	    return stream;
	}
    
}
