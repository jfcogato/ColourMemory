package com.jfcogato.colourmemory.adapters;

import java.util.ArrayList;

import com.jfcogato.colourmemory.MainActivity;
import com.jfcogato.colourmemory.R;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

//un BaseAdapter puede usarse para un Adapter en un listview o gridview
// hay que implementar algunos métodos heredados de la clase Adapter,
//porque BaseAdapter es una subclase de Adapter
// estos métodos en este ejemplo son:  getCount(), getItem(), getItemId(), getView()
public class ImageAdapter extends BaseAdapter implements OnItemClickListener {

	private Context mContext;
	ArrayList<Integer> randomList = new ArrayList<Integer>();
	int points = 0;
	int colors = 0;

	private View lastCardView = null;

	// el constructor necesita el contexto de la actividad donde se utiliza el
	// adapter
	public ImageAdapter(Context c, ArrayList<Integer> list) {
		mContext = c;
		randomList = list;
		colors = list.size() / 2;
	}

	public int getCount() {// devuelve el número de elementos que se introducen
		// en el adapter
		return randomList.size();
	}

	public Object getItem(int position) {
		// este método debería devolver el objeto que esta en esa posición del
		// adapter. No es necesario en este caso más que devolver un objeto
		// null.
		return null;
	}

	public long getItemId(int position) {
		// este método debería devolver el id de fila del item que esta en esa
		// posición del adapter. No es necesario en este caso más que devolver
		// 0.
		return 0;
	}

	// crear un nuevo ImageView para cada item referenciado por el Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		// este método crea una nueva View para cada elemento añadido al
		// ImageAdapter. Se le pasa el View en el que se ha pulsado, converview
		// si convertview es null, se instancia y configura un ImageView con las
		// propiedades deseadas para la presentación de la imagen
		// si converview no es null, el ImageView local es inicializado con este
		// objeto View
		ImageView imageView;
		if (convertView == null) {

			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(200, 200));// ancho
			// y alto
			imageView.setPadding(8, 0, 8, 0);
		} else {
			imageView = (ImageView) convertView;
		}

		// imageView.setImageResource(randomList.get(position));
		imageView.setImageResource(R.drawable.card_bg);
		imageView.setTag(position);
		return imageView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		// if (!onCheck) {
		// cardView = view;
		// // ((ImageView)view).setImageResource(randomList.get(position));
		// onCheck = true;
		// } else {
		// // ((ImageView)view).setImageResource(R.drawable.card_bg);
		// // ((ImageView)cardView).setImageResource(R.drawable.card_bg);
		// onCheck = false;
		// }

		moveCard(view);

	}

	private void moveCard(final View actualCard) {

		new AsyncTask<Void, Void, Boolean>() {

			View mActualCard = actualCard;

			@Override
			protected void onPreExecute() {
				((ImageView) mActualCard).setImageResource(randomList
						.get((Integer) mActualCard.getTag()));
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return true;
			}

			@Override
			protected void onPostExecute(Boolean data) {

				if ((lastCardView == null)
						|| (((Integer) lastCardView.getTag()).intValue() == ((Integer) mActualCard
								.getTag()).intValue())) {
					lastCardView = mActualCard;
					return;
				}

				int actualColor = randomList
						.get((Integer) mActualCard.getTag());
				int lastColor = randomList.get((Integer) lastCardView.getTag());

				// match two cards?
				if (actualColor == lastColor) {
					((ImageView) mActualCard).setVisibility(View.INVISIBLE);
					((ImageView) lastCardView).setVisibility(View.INVISIBLE);
					points = points + 2;
					colors = colors -1;
				} else {
					((ImageView) mActualCard)
							.setImageResource(R.drawable.card_bg);
					((ImageView) lastCardView)
							.setImageResource(R.drawable.card_bg);
					points = points - 1;
				}
				
				((MainActivity)mContext).pointsValue.setText(String.valueOf(points));
				lastCardView = null;
				
				//Check if all the cards was finds
				if (colors == 0){
					Log.d("hello", "you finish");
				}
			}

		}.execute();
	}
}
