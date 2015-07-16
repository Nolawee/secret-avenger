package com.ravenpack.feed;

import com.ravenpack.data.DataGatewayClient;
import com.ravenpack.data.DataGatewayClient.Resolution;
import com.ravenpack.data.DataGatewayClient.ConnectionStatus;
import com.ravenpack.data.DataGatewayException;
import com.ravenpack.data.NewsEvent;
import com.ravenpack.data.TimeSeriesRange;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.io.File;
import java.lang.Math;

public class RavenPackFeed 
{
	private String application = "rpna-api";
	private String provider    = "RavenPack";
	// LinkedHashMap so that we have determined order
	private LinkedHashMap<String, DataFeedSymbol> symbols = null;
	
	private int recentUuidLimit = 100; // per Symbol
	private LinkedHashMap<DataFeedSymbol,LinkedList<String>> recentUuids
		= new LinkedHashMap<DataFeedSymbol,LinkedList<String>>();
	private Calendar cal = null;
	protected DataGatewayClient dg = null;
	protected DataFeedTool parent = null;
	protected String realtimeFilename = null;
	
	private long fileStartDate = 0;
	private long fileEndDate = 0;
	
	//Retry a TimeSeriesRange request this number of times
	public int rangeAttempts = 1;
	
	//Use the default package-pribate access level for this variable.
	//It controls whether or not we switch files when we get to a new month
	boolean switchFilenames = true;
	
	public RavenPackFeed ( DataFeedTool tool, DataGatewayClient dg, Calendar cal, 
			LinkedHashMap<String, DataFeedSymbol> symbols, int rangeAttempts){
		{
			this.dg = dg;
			this.cal = cal;
			this.parent = tool;
			this.symbols = symbols;
			this.rangeAttempts = rangeAttempts;
		}
		
		
		private void updateFileDates( long refdate )
		{
			// Set the calendar with the refdate
			cal.setTimeInMillis(refdate);
			// Get the start of that month
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			// Set the month start of this file
			fileStartDate = cal.getTimeInMillis();
			// Set the month end of this file
			cal.add(Calendar.MONTH, 1);
			fileEndDate = cal.getTimeInMillis();
		}
		
		/**
	     * <p>When we're appending a TimeSeriesRange to an existing file,
	     *    we must request the data including the last entry because timestamps
	     *    are not unique in the data.   When writing back out, we want to make
	     *    sure we start writing after the last known id.</p>
	     */
		private int findStartId( TimeSeriesRange range, String last_id )
		{
			// Index from -1 so that if we don't find anthing, the next line will
			// be zero
			int index = -1;
			if( range.size() > 0) {
				//We only need to search the first second.
				long  firstSec = (long)Math.floor(range.getTimeAt(0)/1000);
				for( int i = 0; i < range.size(); i++) {
					// Once we've gone past the first second we're done.
					if( Math.floor(range.getTimeAt(i)/1000) > firstSec ) {
						break;
					}
					// If we found our id then save the index
					// but continue since itcould occur multiple times
					String current_id = 
							range.getPropertyAt(i, DataFeedSymbol.storyIdField);
					if( last_id.equals(current_id)) {
						index = i;
					}
				}
			}
			return index;
		}
	}

}
