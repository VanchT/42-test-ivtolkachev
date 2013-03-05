package com.ivtolkachev.fbfriendslistapp.model;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import com.facebook.model.GraphUser;
import com.ivtolkachev.fbfriendslistapp.data.DatabaseHelper;

public class User implements Parcelable {
	private String mId;
	private String mName;
	private String mFirstName;
	private String mMiddleName;
	private String mLastName;
	private String mLink;
	private String mUsername;
	private String mBirthday;
	private Bitmap mImage;
	
	private boolean mImageChanged;

	public User(String mId, String mName, String mFirstName,
			String mMiddleName, String mLastName,
			String mUsername, String mBirthday, String mLink) {
		super();
		this.mId = mId;
		this.mName = mName;
		this.mFirstName = mFirstName;
		this.mMiddleName = mMiddleName;
		this.mLastName = mLastName;
		this.mLink = mLink;
		this.mUsername = mUsername;
		this.mBirthday = mBirthday;
		this.mImage = null;
		this.mImageChanged = false;
	}
	
	public User(GraphUser graphUser){
		this.mId = graphUser.getId();
		this.mName = graphUser.getName();
		this.mFirstName = graphUser.getFirstName();
		this.mMiddleName = graphUser.getMiddleName();
		this.mLastName = graphUser.getLastName();
		this.mLink = graphUser.getLink();
		this.mUsername = graphUser.getUsername();
		this.mBirthday = graphUser.getBirthday();
		this.mImage = null;
		this.mImageChanged = false;
	}

	private User(Parcel in) {
		this.mId = in.readString();
		this.mName = in.readString();
		this.mFirstName = in.readString();
		this.mMiddleName = in.readString();
		this.mLastName = in.readString();
		this.mLink = in.readString();
		this.mUsername = in.readString();
		this.mBirthday = in.readString();
		byte[] byteArray = {};
		in.readByteArray(byteArray);
		this.mImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
	}
	
	public String getId() {
		return mId;
	}

	public void setId(String id) {
		mId = id;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
		
	}

	public String getFirstName() {
		return mFirstName;
	}

	public void setFirstName(String firstName) {
		mFirstName = firstName;
	}

	public String getMiddleName() {
		return mMiddleName;
	}

	public void setMiddleName(String middleName) {
		mMiddleName = middleName;
	}

	public String getLastName() {
		return mLastName;
	}

	public void setLastName(String lastName) {
		mLastName = lastName;
	}

	public String getLink() {
		return mLink;
	}

	public void setLink(String link) {
		mLink = link;
	}

	public String getUsername() {
		return mUsername;
	}

	public void setUsername(String username) {
		mUsername = username;
	}

	public String getBirthday() {
		return mBirthday;
	}

	public void setBirthday(String birthday) {
		mBirthday = birthday;
	}

	/**
	 * @return the mImage
	 */
	public Bitmap getImage() {
		return mImage;
	}
	
	/**
	 * @param mImage the mImage to set
	 */
	public void setImage(Bitmap mImage) {
		this.mImage = mImage;
		this.mImageChanged = true;
	}
	
	/**
	 * Generates value of field 'name' using values of fields 
	 * 'firstName', 'middleName' and 'lastName'.
	 */
	public void generateName(){
		mName = mFirstName;
		if (mMiddleName == null || "".equals(mMiddleName)){
			mName += " " + mLastName;
		} else {
			mName += " " + mMiddleName + "\n" + mLastName;
		}
	}
	
	public boolean isImageChanged(){
		return mImageChanged;
	}
	
	public void setImageChangedFlag(boolean flag){
		this.mImageChanged = flag;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mId);
		dest.writeString(mName);
		dest.writeString(mFirstName);
		dest.writeString(mMiddleName);
		dest.writeString(mLastName);
		dest.writeString(mLink);
		dest.writeString(mUsername);
		dest.writeString(mBirthday);
		if (mImage == null){
			dest.writeByteArray(null);
		} else {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			mImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
			dest.writeByteArray(out.toByteArray());
		} 
	}
	
	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
		
		public User createFromParcel(Parcel in) {
		    return new User(in);
		}
		
		public User[] newArray(int size) {
		    return new User[size];
		}
	};

}
