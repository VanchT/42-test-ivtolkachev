package com.ivtolkachev.fbfriendslistapp.activity;

import java.io.FileInputStream;
import java.io.InputStream;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ivtolkachev.fbfriendslistapp.FFApplication;
import com.ivtolkachev.fbfriendslistapp.R;
import com.ivtolkachev.fbfriendslistapp.data.DatabaseWorker;
import com.ivtolkachev.fbfriendslistapp.model.User;
import com.ivtolkachev.fbfriendslistapp.widget.CalendarView;

public class EditProfileActivity extends Activity {
	
	private static final String TAG = "EditProfileActivityTag";
	private static final int REQUEST_CODE_SELECT_PICTURE = 201;
	
	private Button mSaveButton;
	private Button mImageEditButton;
	private Button mDateEditButton;
	private ImageView mProfileImage;
	private EditText mFirstNameField;
	private EditText mMiddleNameField;
	private EditText mLastNameField;
	private EditText mUsernameField;
	private TextView mBirthdayText;
	private CalendarView mEditDateCalendar;
	
	private DatabaseWorker mDatabaseWorker;
	private Bitmap mSelectedImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		
		mDatabaseWorker = FFApplication.getOpenedDatabaseWorker();
		
		initUI();
		fillForm();
	}
	
	//TODO: The method added for testing.
    public void create(){
    	onCreate(null);
    }
    
    private void initUI(){
    	mSaveButton = (Button)findViewById(R.id.profile_save_btn);
		mImageEditButton = (Button)findViewById(R.id.image_edit_btn);
		mDateEditButton = (Button)findViewById(R.id.birthday_edit_btn);
		mProfileImage = (ImageView)findViewById(R.id.profile_image_edit);
		mFirstNameField = (EditText)findViewById(R.id.first_name_edit);
		mMiddleNameField = (EditText)findViewById(R.id.middle_name_edit);
		mLastNameField = (EditText)findViewById(R.id.last_name_edit);
		mUsernameField = (EditText)findViewById(R.id.username_edit);
		mBirthdayText = (TextView)findViewById(R.id.birthday_date);
		mEditDateCalendar = (CalendarView)findViewById(R.id.edit_date_calendsr);
		mEditDateCalendar.setDate(FFApplication.getCurrentUser().getBirthday());
		initListeners();
    }
    
    private void initListeners(){
    	mSaveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				saveData();				
			}
		});
    	
    	mImageEditButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(
					      Context.INPUT_METHOD_SERVICE);
				inputMethodManager.hideSoftInputFromWindow(mImageEditButton.getWindowToken(), 0);
				Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                		REQUEST_CODE_SELECT_PICTURE);
			}
		});
    	
    	mDateEditButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mEditDateCalendar.setVisibility(View.VISIBLE);
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			}
		});
    	
    	mEditDateCalendar.setOnCloseListener(new CalendarView.CalendarOnCloseListener() {
			
			@Override
			public void onClose(String date) {
				mBirthdayText.setText(date);
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
			}
		});
    }

    private void fillForm(){
    	User currentUser = FFApplication.getCurrentUser();
    	if (currentUser.getImage() == null){
    		mProfileImage.setImageDrawable(getResources().getDrawable(R.drawable.com_facebook_profile_picture_blank_square));
    	} else {
    		mProfileImage.setImageBitmap(currentUser.getImage());
    	}
    	mFirstNameField.setText(currentUser.getFirstName());
    	mMiddleNameField.setText(currentUser.getMiddleName());
    	mLastNameField.setText(currentUser.getLastName());
    	mUsernameField.setText(currentUser.getUsername());
    	mBirthdayText.setText(currentUser.getBirthday());
    }

    private boolean isDataValid(){
    	boolean result = true;
    	if ("".equals(mFirstNameField.getText().toString())){
    		mFirstNameField.setError(getString(R.string.message_no_empty));
    		result = false;
    	}
    	if ("".equals(mLastNameField.getText().toString())){
    		mLastNameField.setError(getString(R.string.message_no_empty));
    		result = false;
    	}
    	if ("".equals(mUsernameField.getText().toString())){
    		mUsernameField.setError(getString(R.string.message_no_empty));
    		result = false;
    	}
    	return result;
    }
    
    private void saveData(){
    	if(isDataValid()){
    		User currentUser = FFApplication.getCurrentUser();
    		currentUser.setFirstName(mFirstNameField.getText().toString());
    		currentUser.setMiddleName(mMiddleNameField.getText().toString());
    		currentUser.setLastName(mLastNameField.getText().toString());
    		currentUser.setUsername(mUsernameField.getText().toString());
    		currentUser.generateName();
    		currentUser.setBirthday(mBirthdayText.getText().toString());
    		currentUser.setImage(mSelectedImage);
    		String result = mDatabaseWorker.updateUser(FFApplication.getCurrentUser());
    		if (result != null){
    			finish();
    		}     		
    	}
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                String selectedImagePath = obtainPath(selectedImageUri);
                mSelectedImage = obtainResizedImage(selectedImagePath);
                mProfileImage.setImageBitmap(mSelectedImage);
            }
        }
    }

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public String obtainPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor;
        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.GINGERBREAD_MR1){
        	CursorLoader loader = new CursorLoader(this, uri, projection, null, null, null);
            cursor = loader.loadInBackground();
        } else {
        	cursor = managedQuery(uri, projection, null, null, null);
        }
        
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

	public Bitmap obtainResizedImage(String path){
		Bitmap resizedBitmap = null;
		try
		{
		    int inWidth = 0;
		    int inHeight = 0;

		    InputStream in = new FileInputStream(path);

		    BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeStream(in, null, options);
		    in.close();
		    in = null;

		    inWidth = options.outWidth;
		    inHeight = options.outHeight;

		    int dstSize = getResources().getDimensionPixelSize(R.dimen.com_facebook_profilepictureview_preset_size_large);
		    in = new FileInputStream(path);
		    options = new BitmapFactory.Options();
		    options.inSampleSize = Math.max(inWidth/dstSize, inHeight/dstSize);
		    Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);

		   Matrix matrix = new Matrix();
		    RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
		    RectF outRect = new RectF(0, 0, dstSize, dstSize);
		    matrix.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
		    float[] values = new float[9];
		    matrix.getValues(values);

		    resizedBitmap = Bitmap.createScaledBitmap(roughBitmap, 
		    		(int) (roughBitmap.getWidth() * values[0]), (int) (roughBitmap.getHeight() * values[4]), true);

		}
		catch (Exception e)
		{
		    e.printStackTrace();
		}
		return resizedBitmap;
	}
}
