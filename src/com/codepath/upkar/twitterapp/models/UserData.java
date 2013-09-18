package com.codepath.upkar.twitterapp.models;

import java.io.Serializable;

public class UserData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3617065942043752510L;

	private String userName;
	private String screenName;
	private String profileImageURL;
	private String profileBackgroundImageURL;
	private int numTweets;
	private int followersCount;
	private int friendsCount;
	private int followingCount;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getProfileImageURL() {
		return profileImageURL;
	}

	public void setProfileImageURL(String profileImageURL) {
		this.profileImageURL = profileImageURL;
	}

	public String getProfileBackgroundImageURL() {
		return profileBackgroundImageURL;
	}

	public void setProfileBackgroundImageURL(String profileBackgroundImageURL) {
		this.profileBackgroundImageURL = profileBackgroundImageURL;
	}

	public int getNumTweets() {
		return numTweets;
	}

	public void setNumTweets(int numTweets) {
		this.numTweets = numTweets;
	}

	public int getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}

	public int getFriendsCount() {
		return friendsCount;
	}

	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}

	public int getFollowingCount() {
		return followingCount;
	}

	public void setFollowingCount(int followingCount) {
		this.followingCount = followingCount;
	}
}
