package com.codepath.upkar.twitterapp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockFragment;
import com.codepath.upkar.twitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ProfileTimeLineFragment extends SherlockFragment {
	private static final String tag = "Debug - com.codepath.upkar.twitterapp.ProfileTimeLineFragment";
	ProgressBar myProgressBar;
	List<Tweet> tweets = new ArrayList<Tweet>();
	protected TweetsActivityAdapter timeLineTweetsAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_time_line, container,
				false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		timeLineTweetsAdapter = new TweetsActivityAdapter(getActivity()
				.getBaseContext(), tweets);
		setupViews();

	}

	private void setupViews() {
		myProgressBar = (ProgressBar) getView().findViewById(R.id.pbLoading);
		myProgressBar.setVisibility(ProgressBar.VISIBLE);
		getUserTweets();
		ListView lvTweets = (ListView) getView().findViewById(R.id.lvTweets);
		lvTweets.setAdapter(timeLineTweetsAdapter);
		myProgressBar.setVisibility(ProgressBar.INVISIBLE);
	}

	protected void getUserTweets() {
		TwitterClient restClient = TwitterApp.getRestClient();
		restClient.getUserTimeline(true, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONArray tweetsAsJSON) {
				processTweets(tweetsAsJSON);
			}

		});
	}

	private void processTweets(JSONArray tweetsAsJSON) {
		ArrayList<Tweet> tweetsFromJson = Tweet.fromJson(tweetsAsJSON);
		timeLineTweetsAdapter.addAll(tweetsFromJson);

		if (tweets != null && tweets.size() > 0) {
			Log.d(tag, "#tweets: " + tweets.size());
			Long maxId = tweets.get(tweets.size() - 1).getStrId();
			Log.d(tag, "maxid (id of the last tweet in the list): " + maxId);
			TwitterApp.getRestClient().maxId = maxId;
		}
	}
}
