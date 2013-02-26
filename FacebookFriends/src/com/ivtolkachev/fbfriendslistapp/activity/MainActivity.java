package com.ivtolkachev.fbfriendslistapp.activity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;

import com.facebook.Session;
import com.facebook.SessionState;
import com.ivtolkachev.fbfriendslistapp.R;

public class MainActivity extends Activity {
	
	private static final String TAG = "MainActivityTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
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
    }  
    
    private void onSessionOpened(Session session){
    	Intent intent = new Intent(this, ProfileActivity.class);
    	startActivity(intent);
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
    				buildAlertDialogNoConnection();
    			}
    		}
    	});
    }

    private void buildAlertDialogNoConnection(){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setPositiveButton(R.string.button_retry, new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   authenticate();
    	           }
    	       });
    	builder.setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   finish();
    	           }
    	       });
    	builder.setMessage(R.string.connection_alert);
    	AlertDialog dialog = builder.create();
    	dialog.show();
    			
    }
}
