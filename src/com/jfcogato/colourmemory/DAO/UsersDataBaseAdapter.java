package com.jfcogato.colourmemory.DAO;

import java.util.ArrayList;

import com.jfcogato.colourmemory.models.UsersObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class UsersDataBaseAdapter {

	private static final String DATABASE_NAME = "Users.db";
	private static final String DATABASE_TABLE = "users";
	private static final int DATABASE_VERSION = 2;

	// the index key column name for use in where clauses.
	public static final String KEY_ID = "_id";

	// the name and column index of each column in your database
	public static final String KEY_NAME = "name";
	public static final int NAME_COLUMN = 1;

	public static final String KEY_POINTS = "points";
	public static final int POINTS_COLUMN = 2;

	// SQL statemen to create a new dabase.
	private static final String DATABASE_CREATE = "create table "
			+ DATABASE_TABLE + " (" + KEY_ID
			+ " integer privamry key autoincremente, " + KEY_NAME
			+ " text not null, " + KEY_POINTS + " real not null " + ");";

	private SQLiteDatabase db;
	private final Context context;
	public myDbHelper dbHelper;

	public UsersDataBaseAdapter(Context _context) {
		context = _context;
		dbHelper = new myDbHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
	}

	public UsersDataBaseAdapter open() throws SQLException {
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		db.close();
	}

	public int insertEntry(UsersObject newsObject) {
		int index = 0;

		ContentValues newValues = new ContentValues();
		newValues.put(KEY_NAME, newsObject.getName());
		newValues.put(KEY_POINTS, newsObject.getPoints());

		db.insert(DATABASE_TABLE, null, newValues);

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
		return db.query(DATABASE_TABLE, new String[] { KEY_ID, KEY_NAME,
				KEY_POINTS }, null,
				null, null, null, KEY_POINTS+" DESC");
	}

	public ArrayList<UsersObject> getAllEntriesParsed() {
		ArrayList<UsersObject> list = new ArrayList<UsersObject>();
		Cursor cursor = getAllEntries();
		if (cursor.moveToFirst())
			do {
				UsersObject news = new UsersObject();
				news.setName(cursor.getString(NAME_COLUMN));
				news.setPoints(cursor.getString(POINTS_COLUMN));
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
