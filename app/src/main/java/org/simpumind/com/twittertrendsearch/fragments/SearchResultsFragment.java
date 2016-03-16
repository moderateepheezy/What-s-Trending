package org.simpumind.com.twittertrendsearch.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.simpumind.com.twittertrendsearch.R;
import org.simpumind.com.twittertrendsearch.api.TweetList;
import org.simpumind.com.twittertrendsearch.event.SearchTweetsEvent;
import org.simpumind.com.twittertrendsearch.event.SearchTweetsEventFailed;
import org.simpumind.com.twittertrendsearch.event.SearchTweetsEventOk;
import org.simpumind.com.twittertrendsearch.event.TwitterGetTokenEvent;
import org.simpumind.com.twittertrendsearch.event.TwitterGetTokenEventFailed;
import org.simpumind.com.twittertrendsearch.event.TwitterGetTokenEventOk;
import org.simpumind.com.twittertrendsearch.util.BusProvider;
import org.simpumind.com.twittertrendsearch.util.PrefsController;
import org.simpumind.com.twittertrendsearch.util.TweetAdapter;

/**
 * @author Sergii Zhuk
 *         Date: 24.06.2014
 *         Time: 22:32
 */
public class SearchResultsFragment extends ListFragment {

	private static final String TAG = SearchResultsFragment.class.getName();
	private Bus mBus;
	private String request;

	private TweetAdapter brandAdapter;

	private TextView textResult;

	public static final String ARG_SEARCH_REQUEST = "request";


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_list_tweets, container, false);
		brandAdapter = new TweetAdapter(getActivity(), new TweetList());
		setListAdapter(brandAdapter);
		request = getArguments().getString(ARG_SEARCH_REQUEST);
		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		getBus().register(this);
		if (TextUtils.isEmpty(PrefsController.getAccessToken(getActivity()))) {
			getBus().post(new TwitterGetTokenEvent());
		} else {
			String token = PrefsController.getAccessToken(getActivity());
			getBus().post(new SearchTweetsEvent(token, request));
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		getBus().unregister(this);
	}

	@Subscribe
	public void onTwitterGetTokenOk(TwitterGetTokenEventOk event) {
		getBus().post(new SearchTweetsEvent(PrefsController.getAccessToken(getActivity()), request));
	}

	@Subscribe
	public void onTwitterGetTokenFailed(TwitterGetTokenEventFailed event) {
		makeToast(getActivity(), "Failed to get token");
	}

	private void makeToast(Activity activity, String s) {
		Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
	}

	@Subscribe
	public void onSearchTweetsEventOk(final SearchTweetsEventOk event) {
		brandAdapter.setTweetList(event.tweetsList);
		brandAdapter.notifyDataSetChanged();
	}

	@Subscribe
	public void onSearchTweetsEventFailed(SearchTweetsEventFailed event) {
		makeToast(getActivity(), "Search of tweets failed");
	}


	// TODO migrate to DI
	private Bus getBus() {
		if (mBus == null) {
			mBus = BusProvider.getInstance();
		}
		return mBus;
	}

	public void setBus(Bus bus) {
		mBus = bus;
	}

}
