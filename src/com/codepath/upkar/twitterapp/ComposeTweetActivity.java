package com.codepath.upkar.twitterapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.upkar.twitterapp.models.Tweet;
import com.codepath.upkar.twitterapp.models.TweetData;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ComposeTweetActivity extends Activity {

	private EditText etTweetBody;
	private TextView tvCountlabel;
	private final String tag = "Debug - com.codepath.upkar.twitterapp.ComposeTweetActivity";
	private final int tweetMaxCount = 120;
	private Button btCancelTweet;
	private Button btTweet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_tweet);
		setupViews();
	}

	private void setupViews() {
		btTweet = (Button) findViewById(R.id.btTweet);
		btTweet.setOnClickListener(getTweetButtonClickListener());
		btCancelTweet = (Button) findViewById(R.id.btCancelTweet);
		btCancelTweet.setOnClickListener(getBtCancelTweetListener());
		tvCountlabel = (TextView) findViewById(R.id.tvCountLabel);
		etTweetBody = (EditText) findViewById(R.id.etTweetBody);
		etTweetBody.addTextChangedListener(getTweetTextChangeListener());

	}

	private OnClickListener getTweetButtonClickListener() {
		return new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(tag, "Tweet button clicked");
				String status = etTweetBody.getText().toString();
				TwitterApp.getRestClient().postTweet(
						new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(JSONObject returnObject) {
								Log.d(tag, "onSuccess");
								try {

									Tweet fromJson = Tweet.fromJson(returnObject);
									TweetData data = new TweetData();
									JSONObject user = returnObject
											.getJSONObject("user");
									data.setUserString(user.toString());
									data.setId(fromJson.getId());
									data.setTweet(fromJson.getBody());
									data.setUserName(fromJson.getUser()
											.getName());
									data.setUserProfileImageURL(fromJson
											.getUser().getProfileImageUrl());
									data.setUserBackGroundImageURL(fromJson
											.getUser()
											.getProfileBackgroundImageUrl());
									data.setJsonString(returnObject.toString());
									Intent in = new Intent();
									in.putExtra(Constants.TWEET_DATA_NAME, data);
									setResult(RESULT_OK, in);
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								finish();
							}

							@Override
							public void onFailure(Throwable arg0, JSONArray arg1) {
								Log.d(tag, "onFailure");
							}

							@Override
							public void onFailure(Throwable arg0, String arg1) {
								Log.d(tag, "onFailure");
							}
						}, status);
			}
		};
	}

	private TextWatcher getTweetTextChangeListener() {
		return new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String tweetString = s.toString();
				int tweetStringLength = tweetString.length();

				int countLeft = (tweetString == null || tweetStringLength < 1) ? tweetMaxCount
						: tweetMaxCount - tweetStringLength;
				tvCountlabel.setText(countLeft + " Left");

				if (5 < countLeft && countLeft < tweetMaxCount / 2)
					tvCountlabel.setTextColor(getResources().getColor(
							R.color.orange));
				else if (0 <= countLeft && countLeft <= 5)
					tvCountlabel.setTextColor(getResources().getColor(
							R.color.red));
				else
					tvCountlabel.setTextColor(getResources().getColor(
							R.color.green));

				if (countLeft == 0)
					Toast.makeText(getBaseContext(), R.string.no_chars_left,
							Toast.LENGTH_SHORT).show();
			}
		};
	}

	private OnClickListener getBtCancelTweetListener() {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				overridePendingTransition(R.anim.slide_in_from_top,
						R.anim.slide_out_to_top);
			}
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose_tweet, menu);
		return true;
	}
}
