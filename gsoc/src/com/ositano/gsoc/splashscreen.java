package com.ositano.gsoc;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

@SuppressLint("NewApi")
public class splashscreen extends Activity{
	private TextView text, text2;
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#827717")));
		bar.hide();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.splashscreen);
		Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/cooky.ttf");

		text = (TextView)findViewById( R.id.text1 );
		text.setTypeface(typeface);
		
		text2 = (TextView)findViewById( R.id.text2 );
		text2.setTypeface(typeface);
		Handler handler = new Handler();
	    handler.postDelayed(new Runnable() {
	        @Override
	        public void run() {
	            Intent openLoginActivity =  new Intent(getApplicationContext(), LoginActivity.class);
	            startActivity(openLoginActivity);
	            finish();

	        }
	    }, 10000); 
		
	}

}
