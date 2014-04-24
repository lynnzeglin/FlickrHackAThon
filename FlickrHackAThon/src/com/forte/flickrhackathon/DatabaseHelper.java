package com.forte.flickrhackathon;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int VERSION = 1;

	public DatabaseHelper(Context context) {
		super(context, DatabaseContract.DATABASE_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		try {

			db.beginTransaction();

			db.execSQL("CREATE TABLE " + DatabaseContract.TABLE_NAME + " ( "
					+ DatabaseContract.ID_COLUMN_NAME
					+ " INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
					+ DatabaseContract.TITLE_COLUMN_NAME + " TEXT  NULL, "
					+ DatabaseContract.LINK_COLUMN_NAME + " TEXT  NULL, "
					+ DatabaseContract.MEDIA_COLUMN_NAME + " TEXT  NULL, "
					+ DatabaseContract.DESCRIPTION_COLUMN_NAME + " TEXT  NULL)");

			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		throw new RuntimeException("How did you get there????!!!???");

	}

}
