package com.ivtolkachev.facebookfriends.model;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Application;


import com.ivtolkachev.facebookfriends.data.DatabaseHelper;

public class User {
	private long mUserId;
	private String mUserName;
	private String mUserSurname;
	private String mUserBirthday;
	private String mUserBio;
	private String[] mUserContacts;
	
	public User(long uId, String uName, String uSurname, long uBirthday, String uBio, String[] uContacts){
		this.mUserId = uId;
		this.mUserName = uName;
		this.mUserSurname = uSurname;
		this.setUserBirthday(uBirthday);
		this.mUserBio = uBio;
		this.mUserContacts = uContacts;
	}
	
	/**
	 * @return the userId
	 */
 	public long getUserId() {
		return mUserId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.mUserId = userId;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return mUserName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.mUserName = userName;
	}
	/**
	 * @return the userSurname
	 */
	public String getUserSurname() {
		return mUserSurname;
	}
	/**
	 * @param userSurname the userSurname to set
	 */
	public void setUserSurname(String userSurname) {
		this.mUserSurname = userSurname;
	}
	/**
	 * @return the userBirthday
	 */
	public String getUserBirthday() {
		return mUserBirthday;
	}
	/**
	 * @param userBirthday the userBirthday to set
	 */
	public void setUserBirthday(long userBirthday) {
		Date date = new Date(userBirthday);
		DateFormat formater = DateFormat.getDateInstance(DateFormat.DEFAULT);		
		this.mUserBirthday = formater.format(date);
	}
	/**
	 * @return the userBio
	 */
	public String getUserBio() {
		return mUserBio;
	}
	/**
	 * @param userBio the userBio to set
	 */
	public void setUserBio(String userBio) {
		this.mUserBio = userBio;
	}
	/**
	 * @return the userContacts
	 */
	public String[] getUserContacts() {
		return mUserContacts;
	}
	/**
	 * @param userContacts the userContacts to set
	 */
	public void setUserContacts(String[] userContacts) {
		this.mUserContacts = userContacts;
	}	
	
}
