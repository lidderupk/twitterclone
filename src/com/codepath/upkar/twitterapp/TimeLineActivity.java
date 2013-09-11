package com.codepath.upkar.twitterapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.upkar.twitterapp.models.Tweet;
import com.codepath.upkar.twitterapp.models.TweetData;
import com.codepath.upkar.twitterapp.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimeLineActivity extends Activity {

	private static final String tag = "Debug - com.codepath.upkar.twitterapp.TimeLineActivity";
	private List<Tweet> tweets = new ArrayList<Tweet>();
	private ListView lvTweets;
	private TweetsActivityAdapter tweetsActivityAdapter;
	private SharedPreferences mPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getUserName();
		setTitle("@8indaas");
		setContentView(R.layout.activity_time_line);
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		tweetsActivityAdapter = new TweetsActivityAdapter(getBaseContext(),
				tweets);
		lvTweets.setAdapter(tweetsActivityAdapter);

		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void loadMore(int page, int totalItemsCount) {
				Log.d(tag, "page: " + page + " totalItemsCount: "
						+ totalItemsCount);
				customLoadMoreFromActivity(page);
			}
		});

		TwitterClient restClient = TwitterApp.getRestClient();
		restClient.getHomeTimeLine(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONArray tweetsAsJSON) {
				getTweets(tweetsAsJSON);
			}

		}, true);

	}

	private void getUserName() {
		Log.d(tag, "");
		TwitterApp.getRestClient().getAccountSettings(
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject userAccountJson) {
						try {

							String userNameFromJson = (String) userAccountJson
									.get("screen_name");
							userNameFromJson = "8indaas";
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

	private void customLoadMoreFromActivity(int page) {
		Log.d(tag, "customLoadMoreFromActivity");
		TwitterApp.getRestClient().getHomeTimeLine(
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray tweetsAsJSON) {
						getTweets(tweetsAsJSON);
					}
				}, false);
		Log.d(tag, "Loading more feeds");
	}

	private void getTweets(JSONArray tweetsAsJSON) {
		ArrayList<Tweet> tweetsFromJson = Tweet.fromJson(tweetsAsJSON);
		// tweetsActivityAdapter.clear();
		tweetsActivityAdapter.addAll(tweetsFromJson);

		if (tweets != null && tweets.size() > 0) {
			Log.d(tag, "#tweets: " + tweets.size());
			Long maxId = tweets.get(tweets.size() - 1).getStrId();
			Log.d(tag, "maxid (id of the last tweet in the list): " + maxId);
			TwitterApp.getRestClient().maxId = maxId;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.time_line, menu);
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
		case R.id.refresh:
			TwitterApp.getRestClient().getHomeTimeLine(
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(JSONArray tweetsAsJSON) {
							tweetsActivityAdapter.clear();
							getTweets(tweetsAsJSON);
						}
					}, true);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

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
				tweetsActivityAdapter.insert(tweet, 0);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
