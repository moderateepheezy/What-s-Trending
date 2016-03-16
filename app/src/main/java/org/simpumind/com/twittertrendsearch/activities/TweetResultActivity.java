package org.simpumind.com.twittertrendsearch.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
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

import static org.simpumind.com.twittertrendsearch.util.Util.makeToast;
import  org.simpumind.com.twittertrendsearch.util.TweetAdapter;

public class TweetResultActivity extends AppCompatActivity {

    private static final String TAG = TweetResultActivity.class.getName();
    private Bus mBus;
    private String request;

    private TweetAdapter brandAdapter;
    private ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent.hasExtra("value")) {
            request = intent.getStringExtra("value");
            Toast.makeText(TweetResultActivity.this, request, Toast.LENGTH_SHORT).show();
            //loadTweet();
        }

        loadTweet();
    }

    private void loadTweet() {
        new MyAsyncTask().execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        getBus().register(this);
        if (TextUtils.isEmpty(PrefsController.getAccessToken(getApplicationContext()))) {
            getBus().post(new TwitterGetTokenEvent());
        } else {
            String token = PrefsController.getAccessToken(getApplicationContext());
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
        getBus().post(new SearchTweetsEvent(PrefsController.getAccessToken(getApplicationContext()), request));
    }

    @Subscribe
    public void onTwitterGetTokenFailed(TwitterGetTokenEventFailed event) {
       makeToast(getApplicationContext(), "Failed to get token");
    }


    @Subscribe
    public void onSearchTweetsEventOk(final SearchTweetsEventOk event) {
        brandAdapter.setTweetList(event.tweetsList);
        Toast.makeText(TweetResultActivity.this, "T = " + event.toString(), Toast.LENGTH_SHORT).show();
        brandAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onSearchTweetsEventFailed(SearchTweetsEventFailed event) {
        makeToast(getApplicationContext(), "Search of tweets failed");
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

    @Override
    protected void onResume() {
        super.onResume();
    }


    public class MyAsyncTask extends AsyncTask<Void, Void, Void>{

        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TweetResultActivity.this);
            pDialog.setMessage("Loading... ");
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            brandAdapter = new TweetAdapter(getApplicationContext(), new TweetList());
            ListView listView = (ListView) findViewById(R.id.list);
            listView.setAdapter(brandAdapter);
            pDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }if(id == R.id.action_ui){
            Intent intent = new Intent(this, TestActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
