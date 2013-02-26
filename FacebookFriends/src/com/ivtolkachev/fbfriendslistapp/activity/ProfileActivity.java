package com.ivtolkachev.fbfriendslistapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphLocation;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.ivtolkachev.fbfriendslistapp.R;
import com.ivtolkachev.fbfriendslistapp.data.DatabaseWorker;
import com.ivtolkachev.fbfriendslistapp.model.User;

public class ProfileActivity extends Activity {
	
	private static final String TAG = "ProfileActivityTag";
	private static final int REAUTH_ACTIVITY_CODE = 100;
	
	private UiLifecycleHelper uiHelper;
	private TextView mNoDataView;
	private RelativeLayout mProfileView;
	
	private DatabaseWorker mDatabaseWorker;
	private SharedPreferences mPreferences;
	private GraphUser mCurrentUser;
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(final Session session, final SessionState state, final Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		uiHelper = new UiLifecycleHelper(this, callback);
	    uiHelper.onCreate(savedInstanceState);
	    
	    mNoDataView = (TextView)findViewById(R.id.profile_no_data);
        mProfileView = (RelativeLayout)findViewById(R.id.profile_holder);
        
        mPreferences = getSharedPreferences(getString(R.string.app_preferences), 0);
        mDatabaseWorker = DatabaseWorker.getDatabaseWorker(this.getApplicationContext());
        mDatabaseWorker.openDatabase();
        
        loadUserData();
	}
	
	private void loadUserData(){
		Session session = Session.getActiveSession();
        if (session != null && session.isOpened()) {
            makeMeRequest(session);
        } else {
        	finish();
        }
	}
	
	/**
     * Loads user data from server.
     * @param session the opened active session.
     */
	private void makeMeRequest(final Session session) {
	    Request request = Request.newMeRequest(session, 
	            new Request.GraphUserCallback() {
	        @Override
	        public void onCompleted(GraphUser user, Response response) {
	            if (session == Session.getActiveSession()) {
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
	                }
	            }
	            if (response.getError() != null) {
	            	Log.d(TAG, "User data was not loaded!");
    				buildAlertDialogNoConnection();
	            }
	        }
	    });
	    request.executeAsync();
	} 
	
	private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
	    if (session != null && session.isOpened()) {
	        makeMeRequest(session);
	    }
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
	    		mCurrentUser = user;
				showUserData();
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
	    		linkView.setText(Html.fromHtml(
	    	             "<a href=\""+ mCurrentUser.getLink() + "\"><b>"+ getString(R.string.me_link) + "</b></a> "));
	    		linkView.setMovementMethod(LinkMovementMethod.getInstance());
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
	
    private void buildAlertDialogNoConnection(){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setPositiveButton(R.string.button_retry, new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   loadUserData();
    	           }
    	       });
    	builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   loadUserDataFromDatabase();
    	           }
    	       });
    	builder.setMessage(R.string.connection_alert);
    	AlertDialog dialog = builder.create();
    	dialog.show();
    			
    }
    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    if (requestCode == REAUTH_ACTIVITY_CODE) {
	        uiHelper.onActivityResult(requestCode, resultCode, data);
	    }
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    uiHelper.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle bundle) {
	    super.onSaveInstanceState(bundle);
	    uiHelper.onSaveInstanceState(bundle);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		mDatabaseWorker.closeDatabase();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}
	
}
