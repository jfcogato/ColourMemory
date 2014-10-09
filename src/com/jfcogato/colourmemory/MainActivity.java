package com.jfcogato.colourmemory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.jfcogato.colourmemory.adapters.ImageAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public TextView pointsValue = null;

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
		GridView gridview = (GridView) findViewById(R.id.gridview);// crear el
		// gridview a partir del elemento del xml gridview

		ImageAdapter adapter = new ImageAdapter(this, randomList);
		gridview.setAdapter(adapter);// con setAdapter se llena
		// el gridview con datos. en
		// este caso un nuevo objeto de la clase ImageAdapter,
		// que está definida en otro archivo
		// para que detecte la pulsación se le añade un listener de itemClick
		// que recibe un OniTemClickListener creado con new

		gridview.setOnItemClickListener(adapter);

	}
}
