package com.ivtolkachev.facebookfriends.model;

import java.util.Map;

import org.json.JSONObject;

import com.facebook.model.GraphLocation;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;

public class User implements GraphUser {
	private String mId;
	private String mName;
	private String mFirstName;
	private String mMiddleName;
	private String mLastName;
	private String mLink;
	private String mUsername;
	private String mBirthday;
	private Location mLocation;

	public User(String mId, String mName, String mFirstName,
			String mMiddleName, String mLastName, String mLink,
			String mUsername, String mBirthday) {
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

	/**
	 * Not implemented!
	 * @param graphObjectClass
	 * @return null
	 */
	@Override
	public <T extends GraphObject> T cast(Class<T> graphObjectClass) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Not implemented!
	 * @return null
	 */
	@Override
	public Map<String, Object> asMap() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Not implemented!
	 * @return null
	 */
	@Override
	public JSONObject getInnerJSONObject() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Not implemented!
	 * @param propertyName
	 * @return null
	 */
	@Override
	public Object getProperty(String propertyName) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Not implemented!
	 * @param propertyName
	 * @param propertyValue
	 */
	@Override
	public void setProperty(String propertyName, Object propertyValue) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Not implemented!
	 * @param propertyName
	 */
	@Override
	public void removeProperty(String propertyName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getId() {
		return mId;
	}

	@Override
	public void setId(String id) {
		mId = id;
	}

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public void setName(String name) {
		mName = name;
		
	}

	@Override
	public String getFirstName() {
		return mFirstName;
	}

	@Override
	public void setFirstName(String firstName) {
		mFirstName = firstName;
	}

	@Override
	public String getMiddleName() {
		return mMiddleName;
	}

	@Override
	public void setMiddleName(String middleName) {
		mMiddleName = middleName;
	}

	@Override
	public String getLastName() {
		return mLastName;
	}

	@Override
	public void setLastName(String lastName) {
		mLastName = lastName;
	}

	@Override
	public String getLink() {
		return mLink;
	}

	@Override
	public void setLink(String link) {
		mLink = link;
	}

	@Override
	public String getUsername() {
		return mUsername;
	}

	@Override
	public void setUsername(String username) {
		mUsername = username;
	}

	@Override
	public String getBirthday() {
		return mBirthday;
	}

	@Override
	public void setBirthday(String birthday) {
		mBirthday = birthday;
	}

	@Override
	public GraphLocation getLocation() {
		return mLocation;
	}

	@Override
	public void setLocation(GraphLocation location) {
		mLocation = (Location)location;
	}
	
	
	
}
