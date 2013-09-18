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

import com.actionbarsherlock.app.SherlockFragment;
import com.codepath.upkar.twitterapp.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MentionFragment extends SherlockFragment {
	private static final String tag = "Debug - com.codepath.upkar.twitterapp.MentionFragment";
	List<Tweet> mentionTweets = new ArrayList<Tweet>();
	protected TweetsActivityAdapter mentionTweetsAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Defines the xml file for the fragment
		View view = inflater.inflate(R.layout.activity_time_line, container,
				false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mentionTweetsAdapter = new TweetsActivityAdapter(getActivity()
				.getBaseContext(), mentionTweets);
		setupViews();
	}

	private void setupViews() {
		getMentionTweets();
		ListView lvTweets = (ListView) getView().findViewById(R.id.lvTweets);
		lvTweets.setAdapter(mentionTweetsAdapter);

		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void loadMore(int page, int totalItemsCount) {
				// whatever code is needed to append new items to your
				// AdapterView
				customLoadMoreFromActivity(page);    
			}
		});
	}

	private void customLoadMoreFromActivity(int page) {
		Log.d(tag, "customLoadMoreFromActivity");
		TwitterApp.getRestClient().getMentions(false,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray tweetsAsJSON) {
						processMentions(tweetsAsJSON);
					}
				});
		Log.d(tag, "Loading more feeds");
	}

	protected void getMentionTweets() {
		TwitterClient restClient = TwitterApp.getRestClient();
		restClient.getMentions(true, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONArray tweetsAsJSON) {
				processMentions(tweetsAsJSON);
			}

		});
	}

	private void processMentions(JSONArray tweetsAsJSON) {
		ArrayList<Tweet> tweetsFromJson = Tweet.fromJson(tweetsAsJSON);
		// tweetsActivityAdapter.clear();
		mentionTweetsAdapter.addAll(tweetsFromJson);

		if (mentionTweets != null && mentionTweets.size() > 0) {
			Log.d(tag, "#tweets: " + mentionTweets.size());
			Long maxId = mentionTweets.get(mentionTweets.size() - 1).getStrId();
			Log.d(tag, "maxid (id of the last tweet in the list): " + maxId);
			TwitterApp.getRestClient().maxId = maxId;
		}
	}
}
