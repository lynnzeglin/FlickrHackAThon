package com.forte.flickrhackathon;

import java.util.ArrayList;
import java.util.List;


import com.forte.flickrhackathon.R;
import android.app.Activity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnJsonParserCompleteListener {

	private FlickrImageJsonParser flickrParser;
	private FlickrItemAdapter imageAdapter;
	
	private ArrayList<FlickrItem> flickrItems;
	private GridView gridView;
	private ThumbnailDownloader<ImageView> mThumbnailThread;
	
		
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);    
	    
	    flickrItems = null;
	    flickrParser = (FlickrImageJsonParser) getLastNonConfigurationInstance();
	    
		setupFlickrParser();
		
		setContentView(R.layout.main); 
	    
	    
	    //setRetainInstance(true);
		
		      
        mThumbnailThread = new ThumbnailDownloader<ImageView>(new Handler());
        mThumbnailThread.setListener(new ThumbnailDownloader.Listener<ImageView>() {
            public void onThumbnailDownloaded(ImageView imageView, Bitmap thumbnail) {
                
                    imageView.setImageBitmap(thumbnail);             
            }
        });
        mThumbnailThread.start();
        mThumbnailThread.getLooper();
        
        gridView = (GridView) findViewById(R.id.gridview);
        
        if (flickrItems != null) {
        	imageAdapter = new FlickrItemAdapter(this, flickrItems, mThumbnailThread);	   	
        } else {
        	imageAdapter = null;
        }	   
	    gridView.setAdapter(imageAdapter);
	    

	    gridView.setOnItemClickListener(new OnItemClickListener() {
	    	
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	
	        	Intent intent = new Intent(parent.getContext(), FlickrImageActivity.class);
	            String imageUrl = flickrItems.get(position).getMedia();	
	            String imageTitle = flickrItems.get(position).getTitle();
	            intent.putExtra(Contract.EXTRA_IMAGE_LINK, imageUrl);
	            intent.putExtra(Contract.EXTRA_IMAGE_TITLE, imageTitle);
	            
	            // we COULD just use a webpage to show the imageUrl... but that would be too easy!
	            // (and slow) but it does not crash
	            //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(imageUrl));
	            
	            startActivity(intent); 
	        	
	        	
	            //Toast.makeText(MainActivity.this, "" + position, Toast.LENGTH_SHORT).show();
	        }
	    });
	    
	    
	    
	}
	
	private void setupFlickrParser() {
		
		// get new parser if don't have one, set up to listener and start parser			
		if(flickrParser == null) 
			flickrParser = JsonParserFactory.getNewParser();
				
		flickrParser.setListener(this);
		flickrParser.start(Contract.FLICKR_JSON_FEED);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_refresh:
			setupFlickrParser();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onJsonParserCompleted(ArrayList<FlickrItem> data) {
		
		// parsed flickr data is now available!!!!
		flickrItems = data;
		
		// set adapter for gridview
		imageAdapter = new FlickrItemAdapter(this, flickrItems, mThumbnailThread);	   	
	    gridView.setAdapter(imageAdapter);
			
	}

	@Override
	public void onProgressUpdate(int progress) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Object onRetainNonConfigurationInstance() {
		return flickrParser;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (flickrParser != null) {
			flickrParser.setListener(null);
		}
		mThumbnailThread.quit();
	}


}
