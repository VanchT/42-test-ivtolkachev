package com.ivtolkachev.fbfriendslistapp.activity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.ivtolkachev.fbfriendslistapp.R;
import com.ivtolkachev.fbfriendslistapp.data.DatabaseWorker;
import com.ivtolkachev.fbfriendslistapp.model.User;

public class MainActivity extends Activity {
	
	private static final String TAG = "MainActivityTag";
	
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
        
        mPreferences = getSharedPreferences(getString(R.string.app_preferences), 0);
        
        boolean isConnected = mPreferences.getBoolean(getString(R.string.pref_fb_connection), false);
        if (isConnected) {
        	loadUserData();
        } else {
        	checkFacebookConnection();
        }
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
        //getMenuInflater().inflate(R.menu.activity_main, menu);
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
    }  
    
    private void checkFacebookConnection(){
    	Session session = new Session.Builder(this).setApplicationId(getString(R.string.app_id)).build();
		Session.setActiveSession(session);
		if (!session.isOpened()){
			buildAlertDialogConnectionProposal();
		}
    }
    
    /**
     * Authenticates the user if it need.
     */
    private void authenticate() {
    	PackageInfo packageInfo = null;
		try {
			packageInfo = this.getPackageManager().getPackageInfo(
					this.getComponentName().toShortString(), PackageManager.GET_META_DATA);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (packageInfo != null) {
			Bundle metaData = packageInfo.applicationInfo.metaData;
			String appId = metaData.getString(Session.APPLICATION_ID_PROPERTY);
			if (appId == null){
				metaData.putString(Session.APPLICATION_ID_PROPERTY, getString(R.string.app_id));
			}
		}
    	Session.openActiveSession(this, true, new Session.StatusCallback() {

    		@Override
    		public void call(Session session, SessionState state, Exception exception) {
    			if (session.isOpened()) {
    				SharedPreferences.Editor editor = mPreferences.edit();
    				editor.putBoolean(getString(R.string.pref_fb_connection), true);
    				editor.commit();
    				loadUserData();
    				Log.d(TAG, "Session is opened");
    			} 
    		}
    	});
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
		List<String> permissions = new ArrayList<String>();
        permissions.add("user_birthday");
        session.requestNewReadPermissions(new Session.NewPermissionsRequest(this, permissions));
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

    private void buildAlertDialogConnectionProposal(){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   authenticate();
    	           }
    	       });
    	builder.setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   finish();
    	           }
    	       });
    	builder.setMessage(R.string.connection_proposal);
    	AlertDialog dialog = builder.create();
    	dialog.show();
    }
}
