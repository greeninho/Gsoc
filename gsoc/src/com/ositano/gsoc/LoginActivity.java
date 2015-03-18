/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.ositano.gsoc;

import com.ositano.gsoc.app.AppConfig;
import com.ositano.gsoc.app.AppController;
import com.ositano.gsoc.helper.SessionManager;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

@SuppressLint("NewApi")
public class LoginActivity extends Activity {
	// LogCat tag
	private static final String TAG = RegisterActivity.class.getSimpleName();
	private Button btnLogin;
	private Button btnLinkToRegister;
	private EditText inputEmail;
	private EditText inputPassword;
	private ProgressDialog pDialog;
	private SessionManager session;
	private TextView cliq;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#827717")));
		bar.hide();
		setContentView(R.layout.activity_login);
		Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/cooky.ttf");

		cliq = (TextView)findViewById( R.id.apptitle );
		cliq.setTypeface(typeface);
		inputEmail = (EditText) findViewById(R.id.email);
		inputPassword = (EditText) findViewById(R.id.password);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

		// Progress dialog
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);

		// Session manager
		session = new SessionManager(getApplicationContext());

		// Check if user is already logged in or not
		if (session.isLoggedIn()) {
			// User is already logged in. Take him to main activity
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			intent.putExtra("Email", inputEmail.getText().toString());
			startActivity(intent);
			finish();
		}

		// Login button Click Event
		btnLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();

				// Check for empty data in the form
				if (email.trim().length() > 0 && password.trim().length() > 0) {
					// login user
					checkLogin(email, password);
				} else {
					// Prompt user to enter credentials
					Toast.makeText(getApplicationContext(),
							"Please fill all fields!", Toast.LENGTH_LONG)
							.show();
				}
			}

		});

		// Link to Register Screen
		btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						RegisterActivity.class);
				startActivity(i);
				finish();
			}
		});

	}

	/**
	 * function to verify login details in mysql db
	 * */
	private void checkLogin(final String email, final String password) {
		// Tag used to cancel the request
		String tag_string_req = "req_login";

		pDialog.setMessage("Logging in ...");
		showDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_REGISTER, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG, " **** Login Response: " + response.toString());
						hideDialog();
						String[] resp = response.split("</table></font>\n\\{");
						StringBuilder stb = new StringBuilder();
						stb.append("{");
						stb.append(resp[1].toString());
						String resps = stb.toString();
						Log.d(TAG, " real Login Response: " + resps.toString());
						
						try {
							JSONObject jObj = new JSONObject(resps);
							boolean error = jObj.getBoolean("error");

							// Check for error node in json
							if (!error) {
								// user successfully logged in
								// Create login session
								session.setLogin(true, email);

								// Launch main activity
								Intent intent = new Intent(LoginActivity.this,
										MainActivity.class);
								intent.putExtra("Email", email);
								startActivity(intent);
								finish();
							} else {
								// Error in login. Get the error message
								String errorMsg = jObj.getString("error_msg");
								Toast.makeText(getApplicationContext(),
										errorMsg, Toast.LENGTH_LONG).show();
							}
						} catch (JSONException e) {
							// JSON error
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "Login Error: " + error.getMessage());
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
					    	Toast.makeText(getApplicationContext(),"Parsing error",Toast.LENGTH_LONG).show();
					    }
						hideDialog();
					}
				}) {

			@Override
			protected Map<String, String> getParams() {
				// Posting parameters to login url
				Map<String, String> params = new HashMap<String, String>();
				params.put("tag", "login");
				params.put("email", email);
				params.put("password", password);

				return params;
			}

		};

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
	}

	private void showDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hideDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}
}
