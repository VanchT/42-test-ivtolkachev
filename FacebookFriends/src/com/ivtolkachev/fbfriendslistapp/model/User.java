package com.ivtolkachev.fbfriendslistapp.model;

import java.util.Map;

import org.json.JSONObject;

import com.facebook.model.GraphLocation;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;

public class User {
	private String mId;
	private String mName;
	private String mFirstName;
	private String mMiddleName;
	private String mLastName;
	private String mLink;
	private String mUsername;
	private String mBirthday;

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

}
