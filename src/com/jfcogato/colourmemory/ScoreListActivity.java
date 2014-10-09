package com.jfcogato.colourmemory;

import java.util.ArrayList;

import com.jfcogato.colourmemory.DAO.DataBasesAccess;
import com.jfcogato.colourmemory.adapters.ScoreListAdapter;
import com.jfcogato.colourmemory.models.UsersObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

public class ScoreListActivity extends Activity {

	private ScoreListAdapter adapter;
	private ArrayList<UsersObject> list;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score);

		// The access to the databases must be done out of the main thread
		LoadDataBase();
	}

	private void LoadDataBase() {

		new AsyncTask<Void, Void, ArrayList<UsersObject>>() {

			@Override
			protected void onPreExecute() {

			}

			@Override
			protected ArrayList<UsersObject> doInBackground(Void... params) {
				ArrayList<UsersObject> list = DataBasesAccess.getInstance(
						getApplicationContext()).getUsers();
				return list;

			}

			@Override
			protected void onPostExecute(ArrayList<UsersObject> result) {
				list = result;
				// When we have all the data, we can set the adapters and lists
				setAdapterData();
			}

		}.execute();
	}

	private void setAdapterData() {
		adapter = new ScoreListAdapter(this, list);
		listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(adapter);
	}
}
