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
	
	
	
	public static void main(String args[]){
		System.out.println("Hello World");
	}

}
