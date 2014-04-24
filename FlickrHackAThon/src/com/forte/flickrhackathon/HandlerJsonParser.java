package com.forte.flickrhackathon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.util.JsonReader;
import com.forte.flickrhackathon.FlickrItem;

public class HandlerJsonParser implements FlickrImageJsonParser {

	private OnJsonParserCompleteListener mListener;

	public static final int PARSING_FINISHED = Integer.MAX_VALUE;
	public static final int PROGRESS_UPDATE = Integer.MIN_VALUE;

	private boolean isStarted = false;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case PARSING_FINISHED:
				isStarted = false;		// in case we want to start it again for a refresh
				if (mListener != null) {

					mListener.onJsonParserCompleted((ArrayList<FlickrItem>) msg.obj);
				}
				break;

			case PROGRESS_UPDATE:
				if (mListener != null) {
					mListener.onProgressUpdate(msg.arg1);
				}
				break;

			default:
				super.handleMessage(msg);
				break;
			}

		}

	};

	@Override
	public void setListener(OnJsonParserCompleteListener listener) {
		mListener = listener;
	}

	@Override
	public void start(String url) {

		if (!isStarted) {
			isStarted = true;
			parseAsync(url);
		}

	}

	private void parseAsync(final String url) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					parse(url);
				}
				catch (IOException e){
					e.printStackTrace();
				}
			}
		}).start();
	}

	@SuppressLint("NewApi")
	private void parse(String url) throws IOException {

		URLConnection conn = null;

		try {
			conn = new URL(url).openConnection();
		} catch (MalformedURLException e) {
			conn = null;
			e.printStackTrace();
		} catch (IOException e) {
			conn = null;
			e.printStackTrace();
		}

		if (conn != null) {

			JsonReader reader = new JsonReader(new BufferedReader(
					new InputStreamReader(conn.getInputStream())));
			
			List<FlickrItem> data = null;
			
			try {
			       data = readFlickrImagesArray(reader);
			}
			finally {
				reader.close();
			}
			
			publishProgress(100);
			
			mHandler.obtainMessage(PARSING_FINISHED, data).sendToTarget();
		}
	}
	
	private List<FlickrItem> readFlickrImagesArray(JsonReader reader) throws IOException {
	     
		List<FlickrItem> flickrImages = new ArrayList<FlickrItem>();

		reader.beginObject();

		while (reader.hasNext()) {

			String name = reader.nextName();

			if (name.equals(Contract.FLICKR_KEY_ITEMS)) {
		
			     reader.beginArray();
			     while (reader.hasNext()) {
			    	 
			    	 FlickrItem fi = readFlickrImage(reader);
			    	 flickrImages.add(fi);			    	 
			     }
			     reader.endArray();
			}
			else {
				reader.skipValue();
			}	
		}
		return flickrImages;
	}
	
	private FlickrItem readFlickrImage(JsonReader reader) throws IOException {
	     String title = null;
	     String link = null;
	     String media = null;
	     String desc = null;
	     

	     reader.beginObject();
	     while (reader.hasNext()) {
	       String name = reader.nextName();
	       if (name.equals(Contract.FLICKR_KEY_IMAGE_TITLE)) {
		         title = reader.nextString();
	       } else if (name.equals(Contract.FLICKR_KEY_IMAGE_LINK)) {
	    	   link = reader.nextString();
	       } else if (name.equals(Contract.FLICKR_KEY_IMAGE_MEDIA)) {
	           media = readMedia(reader);
	       } else if (name.equals(Contract.FLICKR_KEY_IMAGE_DESC)) {
		         desc = reader.nextString();
	       } else {
	         reader.skipValue();
	       }
	     }
	     reader.endObject();
	     return new FlickrItem(title, link, media, desc);
	     
	   }
	
	private String readMedia(JsonReader reader) throws IOException {
	     String m = null;
	     
	     reader.beginObject();
	     while (reader.hasNext()) {
	       String name = reader.nextName();
	       if (name.equals("m")) {
	         m = reader.nextString();
	       } else {
	         reader.skipValue();
	       }
	     }
	     reader.endObject();
	     return new String(m);
	}

	private void publishProgress(int progress) {

		mHandler.removeMessages(PROGRESS_UPDATE);
		mHandler.obtainMessage(PROGRESS_UPDATE, progress, -1).sendToTarget();

	}

}
