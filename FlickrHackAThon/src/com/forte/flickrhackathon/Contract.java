package com.forte.flickrhackathon;

public abstract class Contract {
	
	public static final String FLICKR_BASE_URL = "http://api.flickr.com";
	public static final String FLICKR_JSON_FEED = FLICKR_BASE_URL + "/services/feeds/photos_public.gne?format=json&nojsoncallback=1" ;
	public static final String FLICKR_KEY_VALUE = "value";
	public static final String FLICKR_KEY_ITEMS = "items";
	public static final String FLICKR_KEY_IMAGE_TITLE = "title";
	public static final String FLICKR_KEY_IMAGE_LINK = "link";
	public static final String FLICKR_KEY_IMAGE_MEDIA = "media";
	public static final String FLICKR_KEY_IMAGE_DESC = "description";
	
	public final static String EXTRA_IMAGE_LINK = "com.forte.flickrhackathon.IMAGE_LINK";
	public final static String EXTRA_IMAGE_TITLE = "com.forte.flickrhackathon.IMAGE_TITLE";
	
	
	public static final String COOL_PREFERENCE_KEY = "cool_prefs_for_yanina";
	
	

}
