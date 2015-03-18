package com.ositano.listviewfeed.adapter;

import com.ositano.gsoc.R;
import com.ositano.gsoc.app.AppController;
import com.ositano.gsoc.model.cliq;
import com.ositano.listviewfeed.volley.CustomNetworkImageView;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class CustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<cliq> cliqItems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CustomListAdapter(Activity activity, List<cliq> cliqItems) {
		this.activity = activity;
		this.cliqItems = cliqItems;
	}

	@Override
	public int getCount() {
		return cliqItems.size();
	}

	@Override
	public Object getItem(int location) {
		return cliqItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.cliq_item, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		CustomNetworkImageView thumbNail = (CustomNetworkImageView) convertView.findViewById(R.id.profilepic);
		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView story = (TextView) convertView.findViewById(R.id.story);
		TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);
		TextView vision = (TextView) convertView.findViewById(R.id.vision);
		TextView vis = (TextView)convertView.findViewById(R.id.vis);

		// getting movie data for the row
		cliq m = cliqItems.get(position);

		// thumbnail image
		if(m.getThumbnailUrl() == null || m.getThumbnailUrl().equals(""))
			thumbNail.setImageResource(R.drawable.profile);
		else
			thumbNail.setLocalImageBitmap(decodeBase64(m.getThumbnailUrl()));
		
		// title
		title.setText(m.getTitle());
		
		// story
		story.setText(m.getStory());
		
		
		java.sql.Timestamp ts2 = java.sql.Timestamp.valueOf(m.getTimestamp());
	    long time = ts2.getTime();
		CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                time,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
		// timestamp
		timestamp.setText(timeAgo);
		
		// vision
		vision.setText(m.getVision());
		vis.setText(m.getUrl());

		return convertView;
	}
	public Bitmap decodeBase64(String input) 
	{
	    byte[] decodedByte = Base64.decode(input, 0);
	    return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length); 
	}

}