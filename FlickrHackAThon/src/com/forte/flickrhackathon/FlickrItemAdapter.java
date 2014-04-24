package com.forte.flickrhackathon;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FlickrItemAdapter extends ArrayAdapter<FlickrItem> {
	private Context context;
	private ThumbnailDownloader<ImageView> thread;
	
    public FlickrItemAdapter(Context context, ArrayList<FlickrItem> items,
    					ThumbnailDownloader<ImageView> thread) {

    	super(context, 0, items);
    	this.context = context;
    	this.thread = thread;
    	
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
        	
        	LayoutInflater inflater = ((Activity)parent.getContext()).getLayoutInflater();  
        	
        	//LayoutInflater inflater = ((Activity)context).getLayoutInflater ();  // same thing
        	convertView = inflater.inflate (R.layout.gridview_item, parent, false);
        	

        }
        
        FlickrItem item = getItem(position);
        ImageView imageView = (ImageView)convertView
                .findViewById(R.id.gridview_item_imageView);
        imageView.setImageResource(R.drawable.belarusflag);
        
        thread.queueThumbnail(imageView, item.getMedia());
        
        
        TextView textView = (TextView)convertView
                .findViewById(R.id.gridview_item_textView);
        
        // need to detect lots of other ways of having no title, like "" or "     " etc.
        
        if(!item.getTitle().equals(" "))
        	textView.setText(item.getTitle());
        else
        	textView.setText("No Title");        
        
        return convertView;
    }
}
