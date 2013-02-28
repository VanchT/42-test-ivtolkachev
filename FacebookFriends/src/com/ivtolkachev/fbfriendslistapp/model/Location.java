package com.ivtolkachev.fbfriendslistapp.model;

import java.util.Map;

import org.json.JSONObject;

import com.facebook.model.GraphLocation;
import com.facebook.model.GraphObject;

public class Location {
	
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
		this.mCountry = mCountry;
		this.mState = mState;
		this.mCity = mCity;
		this.mStreet = mStreet;
		this.mZip = mZip;
		this.mLatitude = mLatitude;
		this.mLongitude = mLongitude;
		this.mUserId = userId;
	}

	public Location(GraphLocation location, String userId){
		this.mCountry = location.getCountry();
		this.mState = location.getState();
		this.mCity = location.getCity();
		this.mStreet = location.getStreet();
		this.mZip = location.getZip();
		this.mLatitude = location.getLatitude();
		this.mLongitude = location.getLatitude();
		this.mUserId = userId;
	}
	
	public String getStreet() {
		return mStreet;
	}

	public void setStreet(String street) {
		mStreet = street;

	}

	public String getCity() {
		return mCity;
	}

	public void setCity(String city) {
		mCity = city;
	}

	public String getState() {
		return mState;
	}

	public void setState(String state) {
		mState = state;
	}

	public String getCountry() {
		return mCountry;
	}

	public void setCountry(String country) {
		mCountry = country;
	}

	public String getZip() {
		return mZip;
	}

	public void setZip(String zip) {
		mZip = zip;
	}

	public double getLatitude() {
		return mLatitude;
	}

	public void setLatitude(double latitude) {
		mLatitude = latitude;
	}

	public double getLongitude() {
		return mLongitude;
	}

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
