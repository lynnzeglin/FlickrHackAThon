package com.forte.flickrhackathon;

import android.os.Build;

public class JsonParserFactory {
	
	private JsonParserFactory (){
		
	}

	public static FlickrImageJsonParser getNewParser(){
		
		FlickrImageJsonParser result;
		
		if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1){
			
			result = new JsonParserTask();
			
		}else{
			
			result = new HandlerJsonParser();
			
		}
		
		return result;
		
	}
	
}
