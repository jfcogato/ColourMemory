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

public class ImageAdapter extends BaseAdapter implements OnItemClickListener {

	private Context mContext;
	ArrayList<Integer> randomList = new ArrayList<Integer>();
	int points = 0;
	int colors = 0;

	boolean onAnimation = false;
	private View lastCardView = null;

	public ImageAdapter(Context c, ArrayList<Integer> list) {
		mContext = c;
		randomList = list;

		// We get the colors of the cards to count the number of matches, if we
		// add more colors, this adapter
		// will works without changes
		colors = list.size() / 2;
	}

	// We set the onAnimation var to synchronized value, to only be access by
	// one process at the same time, with this, we fix problems with the
	// animation and fast clicks on the cards
	public synchronized boolean inOnAnimation() {
		return onAnimation;
	}

	public synchronized void setOnAnimation(boolean isAnimated) {
		onAnimation = isAnimated;
	}

	public int getCount() {
		return randomList.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// There is no need to create complex list with holders in this case, so
		// we made a simple one
		View view;
		if (convertView == null) {
			view = ((Activity) mContext).getLayoutInflater().inflate(
					R.layout.card, null);
		} else {
			view = (View) convertView;
		}

		((ImageView) view.findViewById(R.id.imageView2))
				.setImageResource(randomList.get(position));
		// use the tag to store the position of the view
		view.setTag(position);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		// Only one process could pass
		if (!inOnAnimation()) {
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
				// We animate the card if is not rotated
				if ((lastCardView == null)
						|| ((Integer) lastCardView.getTag()).intValue() != ((Integer) mActualCard
								.getTag()).intValue()) {
					// The true value, set the animation to know that the color
					// must be shown
					sendAnimation(mActualCard, true);
				}
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				try {
					// If there are two cards turned, we wait 1 second to let
					// the user see the match, if not, we wait for the animation be completed
					if ((lastCardView == null)
							|| (((Integer) lastCardView.getTag()).intValue() == ((Integer) mActualCard
									.getTag()).intValue())) {
						Thread.sleep(500);
					} else {
						Thread.sleep(1000);
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return true;
			}

			@Override
			protected void onPostExecute(Boolean data) {

				// If you don't have a card turned or if user click on the same
				// card, you don't need to check the
				// matches
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

				// cards matches?
				if (actualColor == lastColor) {
					// Set the views non cliclables to ensure the user don't
					// click it again
					mActualCard.setClickable(false);
					lastCardView.setClickable(false);

					removeCard(mActualCard);
					removeCard(lastCardView);

					// remove one color, and set the +2 points
					points = points + 2;
					colors = colors - 1;
				} else {
					sendAnimation(mActualCard, false);
					sendAnimation(lastCardView, false);

					points = points - 1;
				}

				// update the points
				((MainActivity) mContext).pointsValue.setText(String
						.valueOf(points));

				// Set the last cart to null to know that the matches process
				// start again
				lastCardView = null;

				// Check if all the cards was finds
				if (colors == 0) {
					((MainActivity) mContext).shotPopup(points);
				}

				// Reset the synchronous access to let another process to be
				// done (another click)
				setOnAnimation(false);
			}

		}.execute();
	}

	public void removeCard(View view) {
		ImageView imageViewFlip = (ImageView) view
				.findViewById(R.id.imageView2);
		ImageView imageViewOriginal = (ImageView) view
				.findViewById(R.id.imageView1);

		imageViewFlip.setVisibility(View.INVISIBLE);
		imageViewOriginal.setVisibility(View.INVISIBLE);

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
