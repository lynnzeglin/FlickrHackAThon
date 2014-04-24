package com.forte.flickrhackathon;

public interface FlickrImageJsonParser {
	
		
		public void start(String url);
		
		public void setListener(OnJsonParserCompleteListener listener);

}
