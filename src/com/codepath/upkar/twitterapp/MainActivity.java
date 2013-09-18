package com.codepath.upkar.twitterapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.codepath.upkar.twitterapp.models.Tweet;
import com.codepath.upkar.twitterapp.models.TweetData;
import com.codepath.upkar.twitterapp.models.User;
import com.codepath.upkar.twitterapp.models.UserData;
import com.codepath.upkar.twitterapp.util.Constants;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MainActivity extends SherlockFragmentActivity {

	private static final String tag = "Debug - com.codepath.upkar.twitterapp.MainActivity";
	// List<Tweet> tweets = new ArrayList<Tweet>();
	// protected TweetsActivityAdapter timeLineTweetsAdapter;
	// List<Tweet> mentionTweets = new ArrayList<Tweet>();
	// protected TweetsActivityAdapter mentionTweetsAdapter;
	SharedPreferences mPrefs;
	UserData userData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupTabs();
		setupData();
	}

	private void setupData() {
		mPrefs = getSharedPreferences(Constants.PREFS, MODE_PRIVATE);
		getUserName();
		setupUserData();
	}

	private void setupUserData() {
		getCredentials();
	}

	protected void getHomeTweets() {
		TwitterClient restClient = TwitterApp.getRestClient();
		restClient.getHomeTimeLine(true, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONArray tweetsAsJSON) {
				processTweets(tweetsAsJSON);
			}

		});
	}

	// protected void getMentionTweets() {
	// TwitterClient restClient = TwitterApp.getRestClient();
	// restClient.getMentions(true, new JsonHttpResponseHandler() {
	//
	// @Override
	// public void onSuccess(JSONArray tweetsAsJSON) {
	// processMentions(tweetsAsJSON);
	// }
	//
	// });
	// }

	private void setupTabs() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab tabFirst = actionBar
				.newTab()
				.setText("Home")
				.setIcon(R.drawable.ic_menu_home)
				.setTabListener(
						new SherlockTabListener<TimeLineFragment>(
								R.id.flContainer, this, "Home",
								TimeLineFragment.class));

		actionBar.addTab(tabFirst);
		actionBar.selectTab(tabFirst);

		Tab tabSecond = actionBar
				.newTab()
				.setText("@Mentions")
				// .setIcon(R.drawable.ic_launcher)
				.setTabListener(
						new SherlockTabListener<MentionFragment>(
								R.id.flContainer, this, "Mentions",
								MentionFragment.class));

		actionBar.addTab(tabSecond);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.time_line, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.compose:
			Intent in = new Intent(getBaseContext(), ComposeTweetActivity.class);
			startActivityForResult(in, Constants.REQUEST_CODE);
			/*
			 * animation from stackoverflow: http://goo.gl/sYNzZO
			 */
			overridePendingTransition(R.anim.slide_in_from_top,
					R.anim.slide_out_to_top);
			return true;
		case R.id.profile:
			Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
			intent.putExtra(Constants.USER_DATA, userData);
			startActivityForResult(intent, Constants.REQUEST_CODE);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void getUserName() {
		Log.d(tag, "getUserName");
		TwitterApp.getRestClient().getAccountSettings(
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject userAccountJson) {
						try {
							Log.d(tag, "getUserName - onSuccess");
							String userNameFromJson = (String) userAccountJson
									.get("screen_name");
							setTitle("@" + userNameFromJson);
							SharedPreferences.Editor ed = mPrefs.edit();
							ed.putString(Constants.USER_NAME, userNameFromJson);
							ed.commit();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(Throwable arg0, JSONArray arg1) {
						Log.d(tag, "getAccountSettings failed");
					}
				});
	}

	private void getCredentials() {
		Log.d(tag, "getCredentials");
		TwitterApp.getRestClient().getVerifyCredentials(
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject userAccountJson) {
						try {
							userData = new UserData();
							String userNameFromJson = (String) userAccountJson
									.get("screen_name");
							userData.setScreenName(userNameFromJson);

							int followersCount = userAccountJson
									.getInt("followers_count");
							userData.setFollowersCount(followersCount);

							int followingCount = userAccountJson
									.getInt("friends_count");
							userData.setFollowingCount(followingCount);

							String profileImageURL = userAccountJson
									.getString("profile_image_url");
							userData.setProfileImageURL(profileImageURL);

							String profileBackgroundImageURL = userAccountJson
									.getString("profile_background_image_url");
							userData.setProfileBackgroundImageURL(profileBackgroundImageURL);

							SharedPreferences.Editor ed = mPrefs.edit();
							ed.putString(Constants.USER_NAME, userNameFromJson);
							ed.commit();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(Throwable arg0, JSONArray arg1) {
						Log.d(tag, "getAccountSettings failed");
					}
				});
	}

	private void processTweets(JSONArray tweetsAsJSON) {
		// ArrayList<Tweet> tweetsFromJson = Tweet.fromJson(tweetsAsJSON);
		// // tweetsActivityAdapter.clear();
		// timeLineTweetsAdapter.addAll(tweetsFromJson);
		//
		// if (tweets != null && tweets.size() > 0) {
		// Log.d(tag, "#tweets: " + tweets.size());
		// Long maxId = tweets.get(tweets.size() - 1).getStrId();
		// Log.d(tag, "maxid (id of the last tweet in the list): " + maxId);
		// TwitterApp.getRestClient().maxId = maxId;
		// }
	}

	// private void processMentions(JSONArray tweetsAsJSON) {
	// ArrayList<Tweet> tweetsFromJson = Tweet.fromJson(tweetsAsJSON);
	// // tweetsActivityAdapter.clear();
	// mentionTweetsAdapter.addAll(tweetsFromJson);
	//
	// if (mentionTweets != null && mentionTweets.size() > 0) {
	// Log.d(tag, "#tweets: " + mentionTweets.size());
	// Long maxId = mentionTweets.get(mentionTweets.size() - 1).getStrId();
	// Log.d(tag, "maxid (id of the last tweet in the list): " + maxId);
	// TwitterApp.getRestClient().maxId = maxId;
	// }
	// }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == Constants.REQUEST_CODE) {
			TweetData tweetData = (TweetData) data
					.getSerializableExtra(Constants.TWEET_DATA_NAME);
			Toast.makeText(this, "tweet: " + tweetData.getTweet(),
					Toast.LENGTH_SHORT).show();

			Tweet tweet = new Tweet();
			User user = new User();
			JSONObject tweetAsJson;
			JSONObject userAsSjson;
			try {
				tweetAsJson = new JSONObject(tweetData.getJsonString());
				tweet.setJsonObject(tweetAsJson);
				userAsSjson = new JSONObject(tweetData.getUserString());
				user.setJsonObject(userAsSjson);
				tweet.setUser(user);
				// timeLineTweetsAdapter.insert(tweet, 0);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
