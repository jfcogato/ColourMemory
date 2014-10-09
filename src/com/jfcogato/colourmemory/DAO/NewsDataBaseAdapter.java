package com.jfcogato.colourmemory.DAO;

import java.util.ArrayList;

import com.example.rsspanda.model.NewsObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class NewsDataBaseAdapter {

	private static final String DATABASE_NAME = "News.db";
	private static final String DATABASE_TABLE = "news";
	private static final int DATABASE_VERSION = 1;

	// the index key column name for use in where clauses.
	public static final String KEY_ID = "_id";

	// the name and column index of each column in your database
	public static final String KEY_TITLE = "title";
	public static final int TITLE_COLUMN = 1;

	public static final String KEY_DESCRIPTION = "description";
	public static final int DESCRIPTION_COLUMN = 2;

	public static final String KEY_LINK = "link";
	public static final int LINK_COLUMN = 3;

	public static final String KEY_CONTENT = "content";
	public static final int CONTENT_COLUMN = 4;

	public static final String KEY_PICTURE = "picture";
	public static final int PICTURE_COLUMN = 5;

	// SQL statemen to create a new dabase.
	private static final String DATABASE_CREATE = "create table "
			+ DATABASE_TABLE + " (" + KEY_ID
			+ " integer privamry key autoincremente, " + KEY_TITLE
			+ " text not null, " + KEY_DESCRIPTION + " text not null, "
			+ KEY_LINK + " text not null, " + KEY_CONTENT + " text not null, "
			+ KEY_PICTURE + " text not null" + ");";

	private SQLiteDatabase db;
	private final Context context;
	public myDbHelper dbHelper;

	public NewsDataBaseAdapter(Context _context) {
		context = _context;
		dbHelper = new myDbHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
	}

	public NewsDataBaseAdapter open() throws SQLException {
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		db.close();
	}

	public int insertEntry(NewsObject newsObject) {
		int index = 0;

		String strFilter = KEY_DESCRIPTION + "=" + "\"" + newsObject.getTitle() + "\"" ;

		ContentValues newValues = new ContentValues();
		newValues.put(KEY_TITLE, newsObject.getTitle());
		newValues.put(KEY_DESCRIPTION, newsObject.getDescription());
		newValues.put(KEY_LINK, newsObject.getLink());
		newValues.put(KEY_CONTENT, newsObject.getContent());
		newValues.put(KEY_PICTURE, newsObject.getPicture());

		//Filt to don't store the same news twices
		int nRowsEffected = db.update(DATABASE_TABLE, newValues, strFilter,
				null);
		if (nRowsEffected == 0) {
			db.insert(DATABASE_TABLE, null, newValues);
		}

		return index;
	}

	public boolean removeAll() {
		return db.delete(DATABASE_TABLE, null, null) > 0;
	}

	public boolean isEmpty() {
		Cursor cursor = getAllEntries();
		if (cursor.moveToFirst()) {
			return false;
		} else {
			return true;
		}
	}

	public Cursor getAllEntries() {
		return db.query(DATABASE_TABLE, new String[] { KEY_ID, KEY_TITLE,
				KEY_DESCRIPTION, KEY_LINK, KEY_CONTENT, KEY_PICTURE }, null,
				null, null, null, null);
	}

	public ArrayList<NewsObject> getAllEntriesParsed() {
		ArrayList<NewsObject> list = new ArrayList<NewsObject>();
		Cursor cursor = getAllEntries();
		if (cursor.moveToFirst())
			do {
				NewsObject news = new NewsObject();
				news.setTitle(cursor.getString(TITLE_COLUMN));
				news.setDescription(cursor.getString(DESCRIPTION_COLUMN));
				news.setLink(cursor.getString(LINK_COLUMN));
				news.setContent(cursor.getString(CONTENT_COLUMN));
				news.setPicture(cursor.getString(PICTURE_COLUMN));

				list.add(news);
			} while (cursor.moveToNext());
		cursor.close();
		return list;
	}

	private static class myDbHelper extends SQLiteOpenHelper {
		public myDbHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}

		// Called when no database exists in disk and the helper class needs to
		// create a new one.
		// this means that you need to take the row of users, so we need to make
		// an insert
		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DATABASE_CREATE);
		}

		// called when there is a dabase vewrsion mismatch meaning that the
		// version of the database on disk need to be upgraded to the curren
		// version
		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion,
				int _newVersion) {
			// Upgrade the existing database to conform to the new version.
			// multiple
			// previous version can be handle by comparing _oldVersion and
			// _newVersion values.
			// the simplest case is to drop the old table and create a new one.
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(_db);
		}
	}
}
