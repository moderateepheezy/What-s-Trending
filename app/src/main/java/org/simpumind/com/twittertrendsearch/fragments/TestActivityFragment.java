package org.simpumind.com.twittertrendsearch.fragments;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.TweetUi;

import org.simpumind.com.twittertrendsearch.api.ApiConstants;
import org.simpumind.com.twittertrendsearch.R;

import io.fabric.sdk.android.Fabric;

/**
 * A placeholder fragment containing a simple view.
 */
public class TestActivityFragment extends ListFragment {

     TweetTimelineListAdapter timelineAdapter;
     ProgressDialog pd;

    public TestActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_list_tweets, container, false);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(ApiConstants.CONSUMER_KEY, ApiConstants.CONSUMER_SECRET);
        Fabric.with(getActivity(), new TwitterCore(authConfig), new TweetUi(), new Crashlytics());
         pd = ProgressDialog.show(getActivity(), "", "Loading...", true);
        pd.setCancelable(false);
        setUpPopularList(view);
        return  view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(timelineAdapter);
       // getListView().setAdapter(););
    }

    private void setUpPopularList(View view){
        SearchTimeline searchTimeline=new SearchTimeline.Builder().query("#Harper Lee").maxItemsPerRequest(50).build();
         timelineAdapter=new TweetTimelineListAdapter(getActivity(),searchTimeline);
         pd.dismiss();
    }
}
