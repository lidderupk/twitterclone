package com.codepath.upkar.twitterapp;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
	public static final String REST_URL = "https://api.twitter.com/1.1";
	public static final String REST_CONSUMER_KEY = "SOkJb87Q65bquYsUyK4q1A";
	public static final String REST_CONSUMER_SECRET = "5RrmrddFaMSly0LqvQi8t95rqhXA3R7b8U4CcBYwc";
	public static final String REST_CALLBACK_URL = "oauth://twitterapp";
	private static final int INITIAL_TWEET_COUNT = 20;
	private static final String tag = "Debug - com.codepath.upkar.twitterapp.TwitterClient";
	public static Long maxId;

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY,
				REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	public void getHomeTimeLine(AsyncHttpResponseHandler handler,
			boolean isFirstTimeCall) {
		Log.d(tag, "getHomeTimeLine; isFirstTimeCall: " + isFirstTimeCall);
		String url = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams("count", INITIAL_TWEET_COUNT);
		if (!isFirstTimeCall) {
			Log.d(tag, "calling again");
			params.put("max_id", String.valueOf(maxId));
		}

		client.get(url, params, handler);
	}

	public void postTweet(AsyncHttpResponseHandler handler, String status) {
		String url = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams("status", status);
		client.post(url, params, handler);
	}

	public void getAccountSettings(AsyncHttpResponseHandler handler) {
		String url = "account/settings.json";
		client.get(url, handler);
	}

	// /statuses/update.json

	/*
	 * 1. Define the endpoint URL with getApiUrl and pass a relative path to the
	 * endpoint i.e getApiUrl("statuses/home_timeline.json"); 2. Define the
	 * parameters to pass to the request (query or body) i.e RequestParams
	 * params = new RequestParams("foo", "bar"); 3. Define the request method
	 * and make a call to the client i.e client.get(apiUrl, params, handler);
	 * i.e client.post(apiUrl, params, handler);
	 */
}