package com.forte.flickrhackathon;

import android.content.ContentValues;


public class FlickrItem {

	
		private String title;
		private String link;
		private String media;
		private String description;

		public FlickrItem(String title, String link, String media, String description) {
			super();
			this.title = title;
			this.link = link;
			this.media = media;
			this.description = description;
		}

		public String getTitle() {
			return title;
		}

		public String getLink() {
			return link;
		}

		public String getMedia() {
			return media;
		}

		public String getDescription() {
			return description;
		}

		@Override
		public String toString() {
			return title;
		}

		public ContentValues convert() {

			ContentValues values = new ContentValues();

			values.put(DatabaseContract.TITLE_COLUMN_NAME, title);
			values.put(DatabaseContract.LINK_COLUMN_NAME, link);
			values.put(DatabaseContract.MEDIA_COLUMN_NAME, media);
			values.put(DatabaseContract.DESCRIPTION_COLUMN_NAME, description);

			return values;

		}

	

}


