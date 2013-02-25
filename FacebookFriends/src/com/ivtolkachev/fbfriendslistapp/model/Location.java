package com.ivtolkachev.fbfriendslistapp.model;

import java.util.Map;

import org.json.JSONObject;

import com.facebook.model.GraphLocation;
import com.facebook.model.GraphObject;

public class Location implements GraphLocation {
	
	private String mCountry;
	private String mState;
	private String mCity;
	private String mStreet;
	private String mZip;
	private double mLatitude;
	private double mLongitude;
	private String mUserId;
	
	public Location(String mCountry, String mState, String mCity,
			String mStreet, String mZip, double mLatitude, double mLongitude, String userId) {
		super();
		this.mCountry = mCountry;
		this.mState = mState;
		this.mCity = mCity;
		this.mStreet = mStreet;
		this.mZip = mZip;
		this.mLatitude = mLatitude;
		this.mLongitude = mLongitude;
		this.mUserId = userId;
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
	public String getStreet() {
		return mStreet;
	}

	@Override
	public void setStreet(String street) {
		mStreet = street;

	}

	@Override
	public String getCity() {
		return mCity;
	}

	@Override
	public void setCity(String city) {
		mCity = city;
	}

	@Override
	public String getState() {
		return mState;
	}

	@Override
	public void setState(String state) {
		mState = state;
	}

	@Override
	public String getCountry() {
		return mCountry;
	}

	@Override
	public void setCountry(String country) {
		mCountry = country;
	}

	@Override
	public String getZip() {
		return mZip;
	}

	@Override
	public void setZip(String zip) {
		mZip = zip;
	}

	@Override
	public double getLatitude() {
		return mLatitude;
	}

	@Override
	public void setLatitude(double latitude) {
		mLatitude = latitude;
	}

	@Override
	public double getLongitude() {
		return mLongitude;
	}

	@Override
	public void setLongitude(double longitude) {
		mLongitude = longitude;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return mUserId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.mUserId = userId;
	}

}
