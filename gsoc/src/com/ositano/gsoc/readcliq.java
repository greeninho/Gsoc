package com.ositano.gsoc;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class readcliq extends Activity{
	TextView title, story, vision;
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.readcliq);
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#827717")));
		title = (TextView)findViewById(R.id.readTitle);
		story = (TextView)findViewById(R.id.stor);
		vision = (TextView)findViewById(R.id.visio);
		Intent in = getIntent();
		String titl = in.getStringExtra("title");
		String stor = in.getStringExtra("story");
		String vis = in.getStringExtra("vision");
		title.setText(titl);
		story.setText(stor);
		vision.setText(vis);
		
	}
	

}
