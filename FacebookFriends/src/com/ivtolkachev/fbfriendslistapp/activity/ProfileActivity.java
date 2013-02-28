/**
 * This code is need for 4th ticket!
 */

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
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.ivtolkachev.fbfriendslistapp.R;
import com.ivtolkachev.fbfriendslistapp.data.DatabaseWorker;
import com.ivtolkachev.fbfriendslistapp.model.User;

public class ProfileActivity extends Activity {
	
	private static final String TAG = "ProfileActivityTag";
	
	private TextView mNoDataView;
	private RelativeLayout mProfileView;
	
	private DatabaseWorker mDatabaseWorker;
	private SharedPreferences mPreferences;
	private User mCurrentUser;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	    
	    mNoDataView = (TextView)findViewById(R.id.profile_no_data);
        mProfileView = (RelativeLayout)findViewById(R.id.profile_holder);
        
        mPreferences = getSharedPreferences(getString(R.string.app_preferences), 0);
        mDatabaseWorker = DatabaseWorker.getDatabaseWorker(this.getApplicationContext());
        mDatabaseWorker.openDatabase();
        
        loadUserData();
	}
	
	//TODO: The method added for testing.
    public void create(){
    	onCreate(null);
    }
	
	private void loadUserDataFromFacebook(){
    	Session session = Session.getActiveSession();
        if (session != null && session.isOpened()) {
        	Log.d(TAG, "session is opened");
            makeMeRequest(session);
        } else {
        	Log.d(TAG, "session is not opened");
        	showUserData();
        }

	}
	
	/**
     * Loads user data from server.
     * @param session the opened active session.
     */
	private void makeMeRequest(final Session session) {
	    Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
	    	
	        @Override
	        public void onCompleted(GraphUser user, Response response) {
	            if (session == Session.getActiveSession()) {
	                if (user != null) {
	                	Log.d(TAG, "User data was loaded.");
	    				SharedPreferences.Editor editor = mPreferences.edit();
	    				editor.putString(getString(R.string.pref_user_id), user.getId());
	    				editor.commit();
	    				mCurrentUser = new User(user);
	    				mDatabaseWorker.addUser(mCurrentUser);
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
	
    
    /**
     * Loads user data from database.
     */
    private void loadUserData(){   	
    	new AsyncTask<Void, Void, User>(){

			@Override
			protected User doInBackground(Void... params) {
				String userId = mPreferences.getString(getString(R.string.pref_user_id), null);
				User user = null;
				if (userId != null) {
					Log.d(TAG, "Try load from database");
					user = mDatabaseWorker.getUser(userId);
				} 
				return user;
			}
    		
			protected void onPostExecute(User user) {
				if (user == null){
					Log.d(TAG, "Try load from Facebook");
					loadUserDataFromFacebook();
				} else {
					mCurrentUser = user;
					showUserData();
				}
			}
			
    	}.execute();    	
    }  
     
    /**
     * Shows data about user on screen.
     */
    private void showUserData() {
    	if (mCurrentUser == null) {
    		Log.d(TAG, "Show massege no data");
    		mProfileView.setVisibility(View.GONE);
    		mNoDataView.setVisibility(View.VISIBLE);
    	} else {
    		Log.d(TAG, "Show user data");
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
    	}
    }
    	
    private void buildAlertDialogNoConnection(){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setPositiveButton(R.string.button_retry, new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   loadUserDataFromFacebook();
    	           }
    	       });
    	builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   showUserData();
    	           }
    	       });
    	builder.setMessage(R.string.connection_alert);
    	AlertDialog dialog = builder.create();
    	dialog.show();
    }
    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle bundle) {
	    super.onSaveInstanceState(bundle);
	}

	@Override
	public void onPause() {
	    super.onPause();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		mDatabaseWorker.closeDatabase();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	}
	
}
