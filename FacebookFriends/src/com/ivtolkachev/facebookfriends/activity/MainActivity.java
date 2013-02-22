package com.ivtolkachev.facebookfriends.activity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.ivtolkachev.facebookfriends.R;
import com.ivtolkachev.facebookfriends.data.DatabaseWorker;
import com.ivtolkachev.facebookfriends.model.User;

public class MainActivity extends Activity {
	
	private DatabaseWorker mDatabaseWorker;
	private SharedPreferences mPreferences;
	private GraphUser currentUser;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mPreferences = getSharedPreferences(getString(R.string.app_preferences), 0);
        mDatabaseWorker = DatabaseWorker.getDatabaseWorker(this.getApplicationContext());
        mDatabaseWorker.openDatabase();
        printHashKey();
        authenticate();
    }
    
    public void printHashKey() {
	    try {
	        PackageInfo info = getPackageManager().getPackageInfo("com.ivtolkachev.facebookfriends",
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.d("MainActivityTag",
	                    Base64.encodeToString(md.digest(), Base64.DEFAULT));
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
    
    /**
     * Authenticates the user if it need.
     */
    private void authenticate(){
    	Session.openActiveSession(this, true, new Session.StatusCallback() {

    	      @Override
    	      public void call(Session session, SessionState state, Exception exception) {
    	        if (session.isOpened()) {
    	        	loadUserData(session);    	          
    	        } else {
    	        	//loadUserDataFromDatabase();
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
              if (user != null) {
            	  Log.d("MainActivityTag", "User data was loaded.");
            	  SharedPreferences.Editor editor = mPreferences.edit();
            	  editor.putString(getString(R.string.preference_user_id), user.getId());
            	  editor.commit();
            	  mDatabaseWorker.addUser(user);
            	  showUserData();
              } else {
            	  Log.d("MainActivityTag", "User data was not loaded!");
            	  //TODO: need send some error massege
              }
            }
          });
    }
    
    /**
     * Loads user data from database.
     */
    private void loadUserDataFromDatabase(){  
    	Log.d("MainActivityTag", "Try load from database.");
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
					currentUser = user;
					showUserData();
		    	} else {
		    		//TODO: need send error message
		    	}
			}
			
    	}.execute();    	
    }  
     
    private void showUserData() {
    	Log.d("MainActivityTag", currentUser.toString());
    }
}
