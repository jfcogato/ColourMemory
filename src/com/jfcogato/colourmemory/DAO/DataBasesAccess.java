package com.jfcogato.colourmemory.DAO;

import java.util.ArrayList;

import com.jfcogato.colourmemory.models.UsersObject;

import android.content.Context;

public class DataBasesAccess {

	private static DataBasesAccess classAccess = null;
	private static UsersDataBaseAdapter dbUsers = null;

	private static String createInstance = "";

	// label to only let one instance to access the same database, if we have
	// more databases we could create more label like this, and access to both
	// database at the same time
	private static String accessUsers = "";

	public static DataBasesAccess getInstance(Context ctx) {

		// get instances of the class, to secure the access
		if (classAccess == null) {
			synchronized (createInstance) {
				if (classAccess == null) {
					classAccess = new DataBasesAccess();
					dbUsers = new UsersDataBaseAdapter(
							ctx.getApplicationContext());
					dbUsers.open();
				}
			}
		}
		return classAccess;
	}

	public ArrayList<UsersObject> getUsers() {
		synchronized (accessUsers) {
			return dbUsers.getAllEntriesParsed();
		}
	}

	public void setUser(UsersObject userObject) {
		synchronized (accessUsers) {
			dbUsers.insertEntry(userObject);
		}
	}
}
