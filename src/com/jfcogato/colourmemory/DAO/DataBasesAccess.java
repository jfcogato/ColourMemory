package com.jfcogato.colourmemory.DAO;

import java.util.ArrayList;

import com.example.rsspanda.model.NewsObject;

import android.content.Context;

public class DataBasesAccess {

	private static DataBasesAccess classAccess = null;
	private static NewsDataBaseAdapter dbNews = null;

	private static String createInstance = "";

	// label to only let one instance to access the same database, if we have
	// more databases we could creat more label like this, and acces to boths
	// database at the same time
	private static String accessNews = "";

	public static DataBasesAccess getInstance(Context ctx) {

		// get instances of the class, to secure the access
		if (classAccess == null) {
			synchronized (createInstance) {
				if (classAccess == null) {
					classAccess = new DataBasesAccess();
					dbNews = new NewsDataBaseAdapter(
							ctx.getApplicationContext());
					dbNews.open();
				}
			}
		}
		return classAccess;
	}

	public ArrayList<NewsObject> getNews() {
		synchronized (accessNews) {
			return dbNews.getAllEntriesParsed();
		}
	}

	public void deleteNewsDataBase() {
		synchronized (accessNews) {
			dbNews.removeAll();
		}
	}

	public void setNews(NewsObject newsObject) {
		synchronized (accessNews) {
			dbNews.insertEntry(newsObject);
		}
	}
}
