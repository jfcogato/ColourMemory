package com.jfcogato.colourmemory.adapters;

import java.util.ArrayList;

import com.jfcogato.colourmemory.R;
import com.jfcogato.colourmemory.models.UsersObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ScoreListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<UsersObject> mList = new ArrayList<UsersObject>();

	public ScoreListAdapter(Context context, ArrayList<UsersObject> list) {
		this.mList = list;
		this.mContext = context;
	}

	// A real simple adapter with holders to control the list more efficiently
	private class ViewHolder {
		private TextView name;
		private TextView points;
		private TextView ranking;
	}

	public void refreshList(ArrayList<UsersObject> list) {
		mList = list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public UsersObject getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final ViewHolder holder;
		if (convertView == null) {
			view = ((Activity) mContext).getLayoutInflater().inflate(
					R.layout.score_item_list, null);

			holder = new ViewHolder();
			holder.name = (TextView) view.findViewById(R.id.name);
			holder.points = (TextView) view.findViewById(R.id.points);
			holder.ranking = (TextView) view.findViewById(R.id.ranking);

			view.setTag(holder);
		} else
			holder = (ViewHolder) view.getTag();

		UsersObject news = getItem(position);

		holder.name.setText(news.getName());
		holder.points.setText(news.getPoints());
		holder.ranking.setText(String.valueOf(position + 1));

		return view;
	}
}
