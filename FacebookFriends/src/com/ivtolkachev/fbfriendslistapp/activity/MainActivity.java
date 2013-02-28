package com.ivtolkachev.fbfriendslistapp.activity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.facebook.Session;
import com.facebook.SessionState;
import com.ivtolkachev.fbfriendslistapp.R;

public class MainActivity extends Activity {
	
	private static final String TAG = "MainActivityTag";
	
	private Button mProfileButton;
	private Button mFriendsButton;
	private SharedPreferences mPreferences;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mProfileButton = (Button)findViewById(R.id.profile_button);
        mFriendsButton = (Button)findViewById(R.id.friends_button);
        
        mPreferences = getSharedPreferences(getString(R.string.app_preferences), 0);
        
        setListeners();   
        boolean isConnected = mPreferences.getBoolean(getString(R.string.pref_fb_connection), false);
        if (!isConnected) {
        	checkFacebookConnection();
        }
        
    }    
    
    /**
     * Sets the listeners for views which were created in onCreate method.
     */
    private void setListeners() {
    	mProfileButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
		    	startActivity(intent);
			}
		});	
    	
    	mFriendsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Not implemented yet
			}
		});
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
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch (item.getItemId()) {
			case R.id.menu_fb_connection:
				authenticate();
				return true;
	
			default:
				return super.onOptionsItemSelected(item);
		}
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
    				Log.d(TAG, "Session is opened");
    			} 
    		}
    	});
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
    	        	   dialog.dismiss();
    	           }
    	       });
    	builder.setMessage(R.string.connection_proposal);
    	AlertDialog dialog = builder.create();
    	dialog.show();
    }
}
