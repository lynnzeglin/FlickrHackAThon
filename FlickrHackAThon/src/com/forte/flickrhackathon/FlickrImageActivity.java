package com.forte.flickrhackathon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class FlickrImageActivity extends Activity {

	private ThumbnailDownloader<ImageView> bigImageThread;
	
	public FlickrImageActivity() {

		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);	
		
		setContentView(R.layout.flickrimage_activity);	
		
		bigImageThread = new ThumbnailDownloader<ImageView>(new Handler());
		bigImageThread.setListener(new ThumbnailDownloader.Listener<ImageView>() {
            public void onThumbnailDownloaded(ImageView imageView, Bitmap thumbnail) {
                    imageView.setImageBitmap(thumbnail);
                
            }
        });
		bigImageThread.start();
		bigImageThread.getLooper();
		
		
		// Get the url from the intent
	    Intent intent = getIntent();
	    String imageUrl = intent.getStringExtra(Contract.EXTRA_IMAGE_LINK);
	    String imageTitle = intent.getStringExtra(Contract.EXTRA_IMAGE_TITLE);
	    
		ImageView imageView = (ImageView) findViewById(R.id.flickr_imageView);
		TextView textView = (TextView) findViewById(R.id.flickr_textView);
		textView.setText("Title: " + imageTitle + "\n" + "\n" + "Click here to visit site where image is: " + "\n" + imageUrl);
	
		bigImageThread.queueThumbnail(imageView, imageUrl);
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_refresh:
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		bigImageThread.quit();
	}
}
