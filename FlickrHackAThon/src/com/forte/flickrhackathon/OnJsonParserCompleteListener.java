package com.forte.flickrhackathon;

import java.util.ArrayList;

public interface OnJsonParserCompleteListener {
	
	public void onJsonParserCompleted(ArrayList<FlickrItem> data);
	
	public void onProgressUpdate(int progress);

}
