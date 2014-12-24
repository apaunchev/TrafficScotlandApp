package helpers;

/**
 * @author Angel Paunchev (S1105400)
 */

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import models.FeedItem;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.text.Html;
import android.util.Xml;

public class TSXmlParser {

	private static final String ns = null;

    public List<FeedItem> parse(InputStream in) throws XmlPullParserException, IOException,
    	ParseException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            
            // Traffic Scotland's feeds are in the following format:
            // <rss>
            //   <channel></channel>
            // </rss>
            //
            // Skip the <rss> tag and go to <channel> so we can begin parsing from there.
            parser.nextTag();
            parser.nextTag();
            
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List<FeedItem> readFeed(XmlPullParser parser) throws XmlPullParserException,
    	IOException, ParseException {
        List<FeedItem> list = new ArrayList<FeedItem>();

        parser.require(XmlPullParser.START_TAG, ns, "channel");
        
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            
            String name = parser.getName();
            
            if (name.equals("item")) {
            	list.add(readItem(parser));
            } else {
                skip(parser);
            }
        }
        
        return list;
    }

    // Parses the contents of an item. If it encounters a title or description tag, hands them
    // off to their respective "read" methods for processing. Otherwise, skips the tag.
    private FeedItem readItem(XmlPullParser parser) throws XmlPullParserException, IOException,
    	ParseException {
    	String title = null;
        String description = null;
        Date startDate = null;
        Date endDate = null;
        String additionalInfo = null;
        String datePattern = "EEEE, dd MMMM yyyy";
    	
        parser.require(XmlPullParser.START_TAG, ns, "item");
        
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            
            String name = parser.getName();
            
            if (name.equals("title")) {
                title = readTitle(parser);
            } else if (name.equals("description")) {
                description = readDescription(parser);
            } else {
                skip(parser);
            }
        }
        
        // Split the description on each <br> tag.
        String[] splitDescr = description.split("<br />");
        
        // Replace unnecessary information and get just the dates.
        startDate = new SimpleDateFormat(datePattern, Locale.ENGLISH)
        	.parse(splitDescr[0].replace("Start Date: ", "").replace(" - 00:00", ""));
        endDate = new SimpleDateFormat(datePattern, Locale.ENGLISH)
        	.parse(splitDescr[1].replace("End Date: ", "").replace(" - 00:00", ""));
        
        // If there is anything after the dates, store it in the additionalInfo variable.
        if (splitDescr.length > 2) {
        	additionalInfo = Html.fromHtml(splitDescr[2]).toString();
        }
        
        return new FeedItem(title, startDate, endDate, additionalInfo);
    }

    // Processes title tags in the feed.
    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        
        String title = readText(parser);
        
        parser.require(XmlPullParser.END_TAG, ns, "title");
        
        return title;
    }

    // Processes description tags in the feed.
    private String readDescription(XmlPullParser parser) throws IOException,
    	XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        
        String description = readText(parser);
        
        parser.require(XmlPullParser.END_TAG, ns, "description");
        
        return description;
    }

    // For the tags title and description, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        
        return result;
    }

    // Skips tags the parser isn't interested in. Uses depth to handle nested tags, i.e.
    // if the next tag after a START_TAG isn't a matching END_TAG, it keeps going until it
    // finds the matching END_TAG (as indicated by the value of "depth" being 0).
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
	            case XmlPullParser.END_TAG:
	                    depth--;
	                    break;
	            case XmlPullParser.START_TAG:
	                    depth++;
	                    break;
            }
        }
    }
	
}
