package com.ivtolkachev.fbfriendslistapp.activity;

import com.ivtolkachev.fbfriendslistapp.R;

import android.app.Activity;
import android.os.Bundle;

public class EditProfileActivity extends Activity {
	
	private static final String TAG = "EditProfileActivityTag";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
	}

}
