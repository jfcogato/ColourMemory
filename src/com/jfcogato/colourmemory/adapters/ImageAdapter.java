package com.jfcogato.colourmemory.adapters;

import java.util.ArrayList;

import com.jfcogato.colourmemory.MainActivity;
import com.jfcogato.colourmemory.R;
import com.jfcogato.colourmemory.utils.FlipAnimator;

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
	
	boolean onAnimation = false;

	private View lastCardView = null;

	// el constructor necesita el contexto de la actividad donde se utiliza el
	// adapter
	public ImageAdapter(Context c, ArrayList<Integer> list) {
		mContext = c;
		randomList = list;
		colors = list.size() / 2;
	}
	
	public synchronized boolean inOnAnimation() {
		return onAnimation;
	}
	
	public synchronized void setOnAnimation(boolean isAnimated) {
		onAnimation = isAnimated;
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
		View view;
		if (convertView == null) {
			view = ((Activity) mContext).getLayoutInflater().inflate(
					R.layout.card, null);
		} else {
			view = (View) convertView;
		}

		((ImageView) view.findViewById(R.id.imageView2))
				.setImageResource(randomList.get(position));
		view.setTag(position);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {		
		
		if (!inOnAnimation()){
			setOnAnimation(true);
			moveCard(view);
		} else {
			Log.d("Animation on process", "you can not click");
		}

	}

	private void moveCard(final View actualCard) {

		new AsyncTask<Void, Void, Boolean>() {

			View mActualCard = actualCard;

			@Override
			protected void onPreExecute() {				
				if ((lastCardView == null)
						|| ((Integer) lastCardView.getTag()).intValue() != ((Integer) mActualCard
								.getTag()).intValue()) {
					sendAnimation(mActualCard,true);
				}
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					Thread.sleep(500);
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
					setOnAnimation(false);
					return;
				}

				int actualColor = randomList
						.get((Integer) mActualCard.getTag());
				int lastColor = randomList.get((Integer) lastCardView.getTag());

				// match two cards?
				if (actualColor == lastColor) {
					mActualCard.setClickable(false);
					lastCardView.setClickable(false);
					mActualCard.setVisibility(View.GONE);
					lastCardView.setVisibility(View.GONE);

					points = points + 2;
					colors = colors - 1;
				} else {
					sendAnimation(mActualCard, false);
					sendAnimation(lastCardView, false);

					points = points - 1;
				}

				((MainActivity) mContext).pointsValue.setText(String
						.valueOf(points));
				lastCardView = null;

				// Check if all the cards was finds
				if (colors == 0) {
					((MainActivity) mContext).shotPopup(points);
				}
				setOnAnimation(false);
			}

		}.execute();
	}

	public void sendAnimation(View view, boolean visible) {
		ImageView imageViewFlip = (ImageView) view
				.findViewById(R.id.imageView2);
		ImageView imageViewOriginal = (ImageView) view
				.findViewById(R.id.imageView1);

		FlipAnimator animator = new FlipAnimator(imageViewOriginal,
				imageViewFlip, imageViewFlip.getWidth() / 2,
				imageViewFlip.getHeight() / 2);

		if (!visible) {
			animator.reverse();
		}
		view.startAnimation(animator);
	}
}
