package com.ivtolkachev.fbfriendslistapp.activity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphLocation;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.ivtolkachev.fbfriendslistapp.R;
import com.ivtolkachev.fbfriendslistapp.data.DatabaseWorker;
import com.ivtolkachev.fbfriendslistapp.model.Location;
import com.ivtolkachev.fbfriendslistapp.model.User;

public class MainActivity extends Activity {
	
	private static final String TAG = "MainActivityTag";
	//private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	
	private TextView mNoDataView;
	private RelativeLayout mProfileView;
	
	private DatabaseWorker mDatabaseWorker;
	private SharedPreferences mPreferences;
	private GraphUser mCurrentUser;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mNoDataView = (TextView)findViewById(R.id.profile_no_data);
        mProfileView = (RelativeLayout)findViewById(R.id.profile_holder);
        
        mPreferences = getSharedPreferences(getString(R.string.app_preferences), 0);
        mDatabaseWorker = DatabaseWorker.getDatabaseWorker(this.getApplicationContext());
        mDatabaseWorker.openDatabase();
        authenticate();
    }
    
    /**
     * Prints Hash Key of the application.
     */
    public void printHashKey() {
	    try {
	        PackageInfo info = getPackageManager().getPackageInfo("com.ivtolkachev.fbfriendslistapp",
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.d(TAG, Base64.encodeToString(md.digest(), Base64.DEFAULT));
	        }
	    } catch (NameNotFoundException e) {
	
	    } catch (NoSuchAlgorithmException e) {

    }

}
    
    //TODO: The method added for testing.
    public void create(){
    	onCreate(null);
    	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	mDatabaseWorker.openDatabase();
    }  
    
    private void onSessionOpened(Session session){
    	loadUserData(session);
    }
    
    /**
     * Authenticates the user if it need.
     */
    private void authenticate(){    	
    	Session.openActiveSession(this, true, new Session.StatusCallback() {

    		@Override
    		public void call(Session session, SessionState state, Exception exception) {
    			if (session.isOpened()) {
    				Log.d(TAG, "Session is opened");
    				onSessionOpened(session);
    			} else {
    				Log.d(TAG, "Session is not opened");
    				loadUserDataFromDatabase();
    			}
    		}
    	});
    }
    
    /**
     * Loads user data from server.
     * @param session the opened active session.
     */
    private void loadUserData(Session session){
    	Log.d("MainActivityTag", "Try load from server.");
    	Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

    		@Override
    		public void onCompleted(GraphUser user, Response response) {
    			if (response.getError() != null) {
    				Log.e(TAG, response.getError().getErrorMessage());
    			}
    			Log.d(TAG, "Request = " + response.getRequest().toString());
    			Log.d(TAG, "Responce = " + response.toString());
    			
    			
    			if (user != null) {
    				Log.d("MainActivityTag", "User data was loaded.");
    				SharedPreferences.Editor editor = mPreferences.edit();
    				editor.putString(getString(R.string.preference_user_id), user.getId());
    				editor.commit();
    				mDatabaseWorker.addUser(user);
    				mCurrentUser = user;
    				Log.d(TAG, "Current user = " + user.toString());
    				Log.d(TAG, "JSON = " + user.getInnerJSONObject().toString());
    				showUserData();
    			} else {
    				Log.d(TAG, "User data was not loaded!");
    				loadUserDataFromDatabase();
    				//TODO: need send some error massege
    			}
    		}
    		
    	});
    }
    
    /**
     * Loads user data from database.
     */
    private void loadUserDataFromDatabase(){  
    	Log.d(TAG, "Try load from database.");
    	new AsyncTask<Void, Void, User>(){

			@Override
			protected User doInBackground(Void... params) {
				String userId = mPreferences.getString(getString(R.string.preference_user_id), null);
				User user = null;
				if (userId != null) {
					user = mDatabaseWorker.getUser(userId);
				}
				return user;
			}
    		
			protected void onPostExecute(User user) {
				if (user != null) {
					mCurrentUser = user;
					showUserData();
		    	} else {
		    		//TODO: need send error message
		    	}
			}
			
    	}.execute();    	
    }  
     
    /**
     * Shows data about user on screen.
     */
    private void showUserData() {
    	//TODO: This implementation may be changed in the future.
    	if (mCurrentUser == null) {
    		mProfileView.setVisibility(View.GONE);
    		mNoDataView.setVisibility(View.VISIBLE);
    	} else {
	    	TextView nameView = (TextView)findViewById(R.id.name_me);
	    	TextView usernameView = (TextView)findViewById(R.id.username_me);
	    	TextView birthdayView = (TextView)findViewById(R.id.birthday_me);
	    	TextView linkView = (TextView)findViewById(R.id.link_me);
	    	ProfilePictureView profilePicture = (ProfilePictureView)findViewById(R.id.profile_pic);
	    	
	    	profilePicture.setProfileId(mCurrentUser.getId());
	    	nameView.setText(mCurrentUser.getName());
	    	usernameView.setText(mCurrentUser.getUsername());
	    	if (mCurrentUser.getBirthday() == null){
	    		birthdayView.setVisibility(View.GONE);
	    	} else {
	    		((TextView)findViewById(R.id.birthday_me_lable)).setVisibility(View.VISIBLE);
	    		birthdayView.setText(mCurrentUser.getBirthday());
	    	}
	    	if (mCurrentUser.getLink() == null){
	    		linkView.setVisibility(View.GONE);
	    	} else {
	    		linkView.setText(getString(R.string.me_link) + ": " + mCurrentUser.getLink());
	    	}	    	
	    	GraphLocation location = mCurrentUser.getLocation();
	    	if (location != null) {
	    		((TextView)findViewById(R.id.location_me_lable)).setVisibility(View.VISIBLE);
	    		showLocation(location);
	    	}
    	}
    }
    
    /**
     * Shows data about user location on screen.
     * @param location object of GraphLocation type.
     */
    private void showLocation(GraphLocation location){
    	//TODO: This implementation may be changed in the future.
    	if (location.getCountry() != null) {
    		((TextView)findViewById(R.id.location_country))
    		.setText(getString(R.string.me_country) + ": " + location.getCountry());
    	}
    	if (location.getState() != null) {
    		((TextView)findViewById(R.id.location_state))
    		.setText(getString(R.string.me_state) + ": " + location.getState());
    	}
    	if (location.getCity() != null) {
    		((TextView)findViewById(R.id.location_city))
    		.setText(getString(R.string.me_city) + ": " + location.getCity());
    	}
    	if (location.getStreet() != null) {
    		((TextView)findViewById(R.id.location_street))
    		.setText(getString(R.string.me_street) + ": " + location.getStreet());
    	}
    	if (location.getZip() != null) {
    		((TextView)findViewById(R.id.location_zip))
    		.setText(getString(R.string.me_zip) + ": " + location.getZip());
    	}
    	if (location.getLatitude() != -1) {
    		((TextView)findViewById(R.id.location_latitude))
    		.setText(getString(R.string.me_latitude) + ": " + location.getLatitude());
    	}
    	if (location.getLongitude() != -1) {
    		((TextView)findViewById(R.id.location_longitede))
    		.setText(getString(R.string.me_longitude) + ": " + location.getLongitude());
    	}
    }

}
