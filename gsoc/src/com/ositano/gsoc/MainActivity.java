package com.ositano.gsoc;

import com.ositano.gsoc.R;
import com.ositano.gsoc.app.AppConfig;
import com.ositano.gsoc.app.AppController;
import com.ositano.listviewfeed.data.FeedItem;
import com.ositano.gsoc.helper.SQLiteHandler;
import com.ositano.gsoc.helper.SessionManager;
import com.ositano.gsoc.model.cliq;
import com.ositano.gsoc.LoginActivity;
import com.ositano.listviewfeed.adapter.CustomListAdapter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.faizmalkani.floatingactionbutton.FloatingActionButton;

public class MainActivity extends Activity {
	// Log tag
		private static final String TAG = MainActivity.class.getSimpleName();

		// Movies json url
		private static final String url = AppConfig.URL_READJSON;
		private ProgressDialog pDialog;
		private List<cliq> cliqList = new ArrayList<cliq>();
		private ListView listView;
		private CustomListAdapter adapter;
	private SQLiteHandler db;
	private SessionManager session;
	
	FloatingActionButton mFab;
	InputStream is = null;
	String result = "";
	String line = "";
	int code;
	String getEmail;
	SharedPreferences myPrefs;
	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		myPrefs = this.getSharedPreferences("cliq", MODE_PRIVATE);
		getEmail = myPrefs.getString("email", null);
		setTitle(getEmail);
		// SqLite database handler
				db = new SQLiteHandler(getApplicationContext());

				// session manager
				session = new SessionManager(getApplicationContext());
				if (!session.isLoggedIn()) {
					logoutUser();
				}
		mFab = (FloatingActionButton) findViewById(R.id.fabbutton);
		int holoGreen = getResources().getColor(R.color.holo_green);
        mFab.setColor(holoGreen);
        
		
		// These two lines not needed,
		// just to get the look of cliq (changing background color & hiding the icon)
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#827717")));
		getActionBar().setIcon(
				   new ColorDrawable(getResources().getColor(android.R.color.transparent)));

		reload();
			}
	@Override
	public void onDestroy() {
		super.onDestroy();
		hidePDialog();
	}

	private void hidePDialog() {
		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
	}
	
	public  Drawable getdrawable()
	{
		return getResources().getDrawable(R.drawable.sa);
	}
	
	public void reload()
	{
		// Showing progress dialog before making http request
		try{
			listView = (ListView) findViewById(R.id.list);
			adapter = new CustomListAdapter(this, cliqList);
			listView.setAdapter(adapter);
			pDialog = new ProgressDialog(this);
			mFab.listenTo(listView);
			pDialog.setMessage("Loading...");
			pDialog.show();
			listView.setOnItemClickListener(new  AdapterView.OnItemClickListener() {

				  @Override
				public void onItemClick(AdapterView<?> parent, final View view, int position, long   id) 
				{

				    //View v = getViewByPosition(position, listView);
				    String title = ((TextView) view.findViewById(R.id.title)).getText().toString();
				    String story = ((TextView) view.findViewById(R.id.story)).getText().toString();
				    String vision = ((TextView) view.findViewById(R.id.vis)).getText().toString();
				    Intent in = new Intent(getApplication(), readcliq.class);
				    in.putExtra("title", title);
				    in.putExtra("story", story);
				    in.putExtra("vision", vision);
				    startActivity(in);
				    
				//pass an intent data here to readcliq.java
				}

				    });
		}catch(Exception e){
			Log.d(TAG, e.toString());
		}
// Creating volley request obj
		JsonArrayRequest cliqReq = new JsonArrayRequest(url,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						Log.d(TAG, response.toString());
						hidePDialog();
						 
						// Parsing json
						for (int i = 0; i < response.length(); i++) {
							try {

								JSONObject obj = response.getJSONObject(i);
								cliq clik = new cliq();
								clik.setTitle(obj.getString("title"));
								clik.setThumbnailUrl(obj.getString("photo"));
								clik.setStory(obj.getString("story"));
								clik.setVision(obj.getString("email"));
								clik.setTimestamp(obj.getString("timestamp"));
								clik.setUrl(obj.getString("vision"));

								// adding cliq to cliq array
								cliqList.add(clik);

							} catch (JSONException e) {
								e.printStackTrace();
							}

						}

						// notifying list adapter about data changes
						// so that it renders the list view with updated data
						adapter.notifyDataSetChanged();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						if (error instanceof TimeoutError || error instanceof NoConnectionError) {
					        Toast.makeText(getApplicationContext(),"Network Timeout",
					                Toast.LENGTH_LONG).show();
					    } else if (error instanceof AuthFailureError) {
					    	Toast.makeText(getApplicationContext(),"Authentication Failure",
			                Toast.LENGTH_LONG).show();
					    } else if (error instanceof ServerError) {
					    	Toast.makeText(getApplicationContext(),"Server error",
			                Toast.LENGTH_LONG).show();
					    } else if (error instanceof NetworkError) {
					    	Toast.makeText(getApplicationContext(),"Network error",
			                Toast.LENGTH_LONG).show();
					    } else if (error instanceof ParseError) {
					    	//Log.d(TAG, error);
					    	Toast.makeText(getApplicationContext(),"Parsing error",Toast.LENGTH_LONG).show();
					    }
						hidePDialog();

					}
				});
		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(cliqReq);

	}
	
	public void fabClicked(View view) {
        //addPost();
		showCustomDialog();
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.cliq, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		case R.id.refresh:
			reload();
            return true;
		case R.id.btnLogout:
			logoutUser();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public View getViewByPosition(int pos, ListView listView) {
	    final int firstListItemPosition = listView.getFirstVisiblePosition();
	    final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

	    if (pos < firstListItemPosition || pos > lastListItemPosition ) {
	        return listView.getAdapter().getView(pos, null, listView);
	    } else {
	        final int childIndex = pos - firstListItemPosition;
	        return listView.getChildAt(childIndex);
	    }
	}
	
	public int addPost(String mail, String story, String vision, String title)
	{
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		 
	   	nameValuePairs.add(new BasicNameValuePair("email",mail));
	   	nameValuePairs.add(new BasicNameValuePair("story",story));
	   	nameValuePairs.add(new BasicNameValuePair("vision",vision));
	   	nameValuePairs.add(new BasicNameValuePair("title",title));
	   	StrictMode.setThreadPolicy(policy);
	    	//Toast.makeText(getApplicationContext(), mail +" ** "+story +" ** "+vision, Toast.LENGTH_LONG).show();
	    	try
	    	{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(AppConfig.URL_ADDPOST);
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        HttpResponse response = httpclient.execute(httppost); 
		        Log.d(TAG, "After response statement");
		        HttpEntity entity = response.getEntity();
		        is = entity.getContent();
		        Log.e("pass 1", "connection success ");
		}
	        catch(Exception e)
		{
	        	Log.e("Fail 1", e.toString());
		    	Toast.makeText(getApplicationContext(), "Unable to establish connection",
				Toast.LENGTH_LONG).show();
		}     
	        
	        try
	        {
	            BufferedReader reader = new BufferedReader
				(new InputStreamReader(is,"iso-8859-1"),8);
	            StringBuilder sb = new StringBuilder();
	            while ((line = reader.readLine()) != null)
		    {
	                sb.append(line + "\n");
	            }
	            is.close();
	            result = sb.toString();
		    Log.e("pass 2", "connection success ");
		}
	        catch(Exception e)
		{
	            Log.e("Fail 2", e.toString());
		}     
	        
		try
		{
				Log.d(TAG, result.toString());
	            JSONObject json_data = new JSONObject(result);
	            code=(json_data.getInt("code"));
				
	            if(code==1)
	            {
			Toast.makeText(getBaseContext(), "Post Added",
				Toast.LENGTH_SHORT).show();
	            }
	            else
	            {
			 Toast.makeText(getBaseContext(), "Sorry, Try Again",
				Toast.LENGTH_LONG).show();
	            }
		}
		catch(Exception e)
		{
	            Log.e("Fail 3", e.toString());
		}
		return code;
	}
	private void logoutUser() {
		session.setLogin(false, "");

		db.deleteUsers();

		// Launching the login activity
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}
	
	private void showCustomDialog() {
		// Create custom dialog object
		final Dialog dialog = new Dialog(this);
		// hide to default title for Dialog
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		 
		// inflate the layout dialog_layout.xml and set it as contentView
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog, null, false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(view);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		 
		// Retrieve views from the inflated dialog layout and update their values
		TextView txtTitle = (TextView) dialog.findViewById(R.id.txt_dialog_title);
		txtTitle.setText("New Post");
		 
		final EditText story = (EditText)dialog.findViewById(R.id.story);
		final EditText vision = (EditText)dialog.findViewById(R.id.vision);
		final EditText storyTitle = (EditText)dialog.findViewById(R.id.storyTitle);
		
		 final String storyText = story.getText().toString();
		 final String visionText = vision.getText().toString();
		 
		 
		Button btnpost = (Button) dialog.findViewById(R.id.btn_post);
		btnpost.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(storyTitle.getText().toString().isEmpty() || story.getText().toString().isEmpty() || vision.getText().toString().isEmpty())
					Toast.makeText(getApplicationContext(), "Please Enter Text", Toast.LENGTH_LONG).show();
				else {
					int i = addPost(getEmail, (String)story.getText().toString(), (String)vision.getText().toString(), (String)storyTitle.getText().toString());
					if(i == 1)	
					dialog.dismiss();
				}
					
				
			}
		});
		 
		Button btnCancel = (Button) dialog.findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
		// Close the dialog
		dialog.dismiss();
		}
		});
		 
		// Display the dialog
		dialog.show();
		}
		

}
