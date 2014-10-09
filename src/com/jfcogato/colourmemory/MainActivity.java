package com.jfcogato.colourmemory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.jfcogato.colourmemory.DAO.DataBasesAccess;
import com.jfcogato.colourmemory.adapters.ImageAdapter;
import com.jfcogato.colourmemory.models.UsersObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public TextView pointsValue = null;
	public float density = 1.0f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		pointsValue = (TextView) findViewById(R.id.points_value);

		// First we create an aux list with the object repeated
		ArrayList<Integer> listObject = new ArrayList<Integer>();

		for (int i = 0; i < 2; i++) {
			listObject.add(R.drawable.colour1);
			listObject.add(R.drawable.colour2);
			listObject.add(R.drawable.colour3);
			listObject.add(R.drawable.colour4);
			listObject.add(R.drawable.colour5);
			listObject.add(R.drawable.colour6);
			listObject.add(R.drawable.colour7);
			listObject.add(R.drawable.colour8);
		}

		int size = listObject.size();
		Random random = new Random();

		// Now we create a random list with the list of object
		ArrayList<Integer> randomList = new ArrayList<Integer>();

		for (int i = 0; i < size; i++) {
			int index = random.nextInt(listObject.size());
			randomList.add(listObject.remove(index));
		}

		// On randomList we have the order to paint the cards
		GridView gridview = (GridView) findViewById(R.id.gridview);

		ImageAdapter adapter = new ImageAdapter(this, randomList);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(adapter);
		
		Button scoreButton = (Button) this.findViewById(R.id.score_button);
		scoreButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				startScoreList();
			}
			
		});
	}
	
	public void startScoreList(){
		Intent i = new Intent(getApplicationContext(), ScoreListActivity.class);				
		startActivity(i);
	}

	public void shotPopup(final int points) {

		final EditText input = new EditText(this);
		int padding = Math.round(16 * density);
		input.setPadding(padding, padding, padding, padding);

		new AlertDialog.Builder(this)
				.setTitle(getString(R.string.popup_header))
				.setMessage(getString(R.string.popup_text)).setView(input)
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						if (input.getText().length() == 0) {
							shotPopup(points);
						} else {
							UsersObject score = new UsersObject();
							score.setName(String.valueOf(input.getText()));
							score.setPoints(String.valueOf(points));

							DataBasesAccess
									.getInstance(getApplicationContext())
									.setUser(score);
							
							startScoreList();
						}

					}
				}).show();
	}
}
