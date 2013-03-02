package com.ivtolkachev.fbfriendslistapp.activity;

import java.net.MalformedURLException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.Facebook;
import com.facebook.internal.Logger;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;
import com.ivtolkachev.fbfriendslistapp.R;
import com.ivtolkachev.fbfriendslistapp.data.DatabaseWorker;
import com.ivtolkachev.fbfriendslistapp.model.User;
import com.ivtolkachev.fbfriendslistapp.net.FacebookLoader;

public class MainActivity extends Activity {
	
	private static final String TAG = "MainActivityTag";
	private static final int EDIT_PROFILE_ACTIVITY_CODE = 200;
	
	private TextView mNoDataView;
	private RelativeLayout mProfileView;
	private ImageView mProfileImage;
	private ProgressBar mImageProgress;
	
	private DatabaseWorker mDatabaseWorker;
	private SharedPreferences mPreferences;
	private User mCurrentUser;
	UiLifecycleHelper mUIHelper;
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(final Session session, final SessionState state, final Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUIHelper = new UiLifecycleHelper(MainActivity.this, callback);
        
        mNoDataView = (TextView)findViewById(R.id.profile_no_data);
        mProfileView = (RelativeLayout)findViewById(R.id.profile_holder);
        mProfileImage = (ImageView)findViewById(R.id.profile_pic);
        mImageProgress = (ProgressBar)findViewById(R.id.profile_pic_progress);
        
        mPreferences = getSharedPreferences(getString(R.string.app_preferences), 0);
        mDatabaseWorker = DatabaseWorker.getDatabaseWorker(this.getApplicationContext());
        mDatabaseWorker.openDatabase();
        Log.d(TAG, "This name = " + this.getLocalClassName());
        authenticate();
    }    
    
    private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
    	Log.d(TAG, "Session state was changed");
        if (session != null && session.isOpened()) {
        	Log.d(TAG, "Session is opened");
        	loadUserData();
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
			case R.id.menu_edit_profile:
				Intent intent = new Intent(this, EditProfileActivity.class);
				startActivityForResult(intent, EDIT_PROFILE_ACTIVITY_CODE);
				return true;
		
			default:
				return super.onOptionsItemSelected(item);
			}
	}
    
    private void handleReauthActivityResult(int requestCode, int resultCode, Intent data){
    	if (resultCode == RESULT_OK) {
			mUIHelper.onActivityResult(requestCode, resultCode, data);
			return;
		} 
    	if (resultCode == RESULT_CANCELED) {
			Log.d(TAG, "Close app");
			Session.getActiveSession().closeAndClearTokenInformation();
			Session.setActiveSession(null);
			finish();
		}
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode = " + String.valueOf(requestCode));
        Log.d(TAG, "onActivityResult: resultCode = " + String.valueOf(resultCode));
        switch (requestCode) {				
			case EDIT_PROFILE_ACTIVITY_CODE:
				if (resultCode == RESULT_OK) loadUserData();
				break;
			
			default:
				handleReauthActivityResult(requestCode, resultCode, data);
		}
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    }  
      
    @Override
    public void onResume() {
        super.onResume();
        mUIHelper.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        mUIHelper.onSaveInstanceState(bundle);
    }

    @Override
    public void onPause() {
        super.onPause();
        mUIHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUIHelper.onDestroy();
        FacebookLoader.cancelAllTasks();
    }
    
    /**
     * Authenticates the user if it need.
     */
    private void authenticate() {   	   	
    	Log.d(TAG, "authenticate");
    	
    	Session.openActiveSession(this, true, new Session.StatusCallback() {
 
    		@Override
    		public void call(Session session, SessionState state, Exception exception) {
    			if (session.isOpened()) {
    				Log.d(TAG, "openActiveSession: session is opened");
    			}
    		}
    	});
    	
    }

    private void loadUserDataFromFacebook(){
    	Session session = Session.getActiveSession();
        if (session != null && session.isOpened()) {
        	Log.d(TAG, "session is opened");
        	//List<String> permissions = new ArrayList<String>();
	        //permissions.add("user_birthday");
	        //session.requestNewReadPermissions(new Session.NewPermissionsRequest(MainActivity.this, permissions)); 
            makeMeRequest(session);
        } else {
        	Log.d(TAG, "session is not opened");
        	authenticate();
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
	    				loadMeProfilePicture();
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
	    	
	    	mNoDataView.setVisibility(View.GONE);
	    	mProfileView.setVisibility(View.VISIBLE);
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
	    	updateUIProfilePicture();
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
    	        	   finish();
    	           }
    	       });
    	builder.setMessage(R.string.connection_alert);
    	AlertDialog dialog = builder.create();
    	dialog.show();
    }

    private FacebookLoader.Callback onLoadProfilePictureCallback = new FacebookLoader.Callback() {
		
		@Override
		public void onError(Exception exception) {
			Log.w(TAG, "The picture does not loaded!");
			Log.e(TAG, "error text = " + exception.getMessage());
			mImageProgress.setVisibility(View.GONE);
			exception.printStackTrace();	
		}
		
		@Override
		public void onComplate(Bitmap bitmap) {
			mCurrentUser.setImage(bitmap);
			mDatabaseWorker.updateUserProfilePicture(bitmap, mCurrentUser.getId());
			updateUIProfilePicture();
		}
		
		@Override
		public void onCancel() {
			Log.w(TAG, "Loadind of profile image was canceled.");
		}
	};
    
    private void loadMeProfilePicture(){
    	mImageProgress.setVisibility(View.VISIBLE);
    	FacebookLoader.loadMeProfilePicture(this, Session.getActiveSession(), new Request.Callback() {
			
			@Override
			public void onCompleted(Response response) {
				Log.d(TAG, "responce = " + response.toString());
				FacebookLoader.loadImage(MainActivity.this, response, onLoadProfilePictureCallback);				
			}
		});
    }
    
    private void updateUIProfilePicture(){
    	mImageProgress.setVisibility(View.GONE);
    	if (mCurrentUser.getImage() != null){
    		Log.d(TAG, "The picture was updated.");
    		mProfileImage.setImageBitmap(mCurrentUser.getImage());
    	}
    }
}