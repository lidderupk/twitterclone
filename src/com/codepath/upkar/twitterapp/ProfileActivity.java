package com.codepath.upkar.twitterapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.codepath.upkar.twitterapp.models.UserData;
import com.codepath.upkar.twitterapp.util.Constants;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends SherlockFragmentActivity {

	private static final String tag = "Debug - com.actionbarsherlock.app.SherlockFragmentActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);

		/*
		 * get the user data
		 */

		UserData userData = (UserData) getIntent().getSerializableExtra(
				Constants.USER_DATA);
		if (userData != null) {
			ImageView imageView = (ImageView) findViewById(R.id.ivUserImage);
			ImageLoader.getInstance().displayImage(
					userData.getProfileImageURL(), imageView);

			TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
			tvUserName.setText("@" + userData.getScreenName());
			setTitle("@" + userData.getScreenName());

			TextView tvNumFollowers = (TextView) findViewById(R.id.tvNumFollowers);
			tvNumFollowers.setText("" + userData.getFollowersCount()
					+ " Followers");

			TextView tvNumFollowing = (TextView) findViewById(R.id.tvNumFollowing);
			tvNumFollowing.setText("" + userData.getFollowingCount()
					+ " Following");
		} else
			Log.d(tag,
					"userData is null, unable to get picture, followers and following");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

}
