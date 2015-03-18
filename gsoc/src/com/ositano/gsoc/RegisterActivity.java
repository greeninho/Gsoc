
package com.ositano.gsoc;

import com.ositano.gsoc.app.AppConfig;
import com.ositano.gsoc.app.AppController;
import com.ositano.gsoc.helper.SQLiteHandler;
import com.ositano.gsoc.helper.SessionManager;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
public class RegisterActivity extends Activity {
	private static final String TAG = RegisterActivity.class.getSimpleName();
	private Button btnRegister;
	private Button btnLinkToLogin;
	private EditText inputFullName;
	private EditText inputEmail;
	private EditText inputPassword, rpassword;
	private ProgressDialog pDialog;
	private SessionManager session;
	private SQLiteHandler db;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#827717")));
		inputFullName = (EditText) findViewById(R.id.fname);
		inputEmail = (EditText) findViewById(R.id.email);
		inputPassword = (EditText) findViewById(R.id.password);
		rpassword = (EditText) findViewById(R.id.rpassword);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

		// Progress dialog
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);

		// Session manager
		session = new SessionManager(getApplicationContext());

		// SQLite database handler
		db = new SQLiteHandler(getApplicationContext());

		// Check if user is already logged in or not
		if (session.isLoggedIn()) {
			// User is already logged in. Take him to main activity
			Intent intent = new Intent(RegisterActivity.this,
					MainActivity.class);
			startActivity(intent);
			finish();
		}

		// Register Button Click event
		btnRegister.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String name = inputFullName.getText().toString();
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();
				
				String special = "@.";
				String pattern = ".*[" + Pattern.quote(special) + "].*";
				boolean emailcheck = email.matches(pattern);
				
				if(!rpassword.getText().toString().equals(inputPassword.getText().toString())){
					Toast.makeText(getApplicationContext(), "Passwords don't match!", Toast.LENGTH_SHORT).show();
				}
				else if(!emailcheck)
					Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
				else if(email.equals(password))
					Toast.makeText(getApplicationContext(), "Email is the same as password", Toast.LENGTH_LONG).show();
				else if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
					registerUser(name, email, password);
				
				} 
				else {
				
					Toast.makeText(getApplicationContext(),
							"Please enter your details!", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

		// Link to Login Screen
		btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						LoginActivity.class);
				startActivity(i);
				finish();
			}
		});

	}

	/**
	 * Function to store user in MySQL database will post params(tag, name,
	 * email, password) to register url
	 * */
	private void registerUser(final String name, final String email,
			final String password) {
		// Tag used to cancel the request
		String tag_string_req = "req_register";

		pDialog.setMessage("Registering ...");
		showDialog();

		StringRequest strReq = new StringRequest(Method.POST,
				AppConfig.URL_REGISTER, new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						Log.d(TAG, "Register Response: " + response.toString());
						hideDialog();
						String[] resp = response.split("</table></font>\n\\{");
						StringBuilder stb = new StringBuilder();
						stb.append("{");
						stb.append(resp[1].toString());
						String resps = stb.toString();
						Log.d(TAG, " real register Response: " + resps.toString());
						
						try {
							JSONObject jObj = new JSONObject(resps);
							boolean error = jObj.getBoolean("error");
							if (!error) {
								// User successfully stored in MySQL
								// Now store the user in sqlite
								String uid = jObj.getString("uid");

								JSONObject user = jObj.getJSONObject("user");
								String name = user.getString("name");
								String email = user.getString("email");
								String created_at = user
										.getString("created_at");

								// Inserting row in users table
								db.addUser(name, email, uid, created_at);

								// Launch login activity
								Intent intent = new Intent(
										RegisterActivity.this,
										CameraActivity.class);
								intent.putExtra("Email", email);
								startActivity(intent);
								finish();
							} else {

								// Error occurred in registration. Get the error
								// message
								String errorMsg = jObj.getString("error_msg");
								if(errorMsg.equals("User already existed")){
									Intent intent = new Intent(
											RegisterActivity.this,
											CameraActivity.class);
									intent.putExtra("Email", email);
									startActivity(intent);
									finish();
								}
								Toast.makeText(getApplicationContext(),
										errorMsg, Toast.LENGTH_LONG).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, "Registration Error: " + error.getMessage());
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
				// Posting params to register url
				Map<String, String> params = new HashMap<String, String>();
				params.put("tag", "register");
				params.put("name", name);
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
