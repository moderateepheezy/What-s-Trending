package org.simpumind.com.twittertrendsearch.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.simpumind.com.twittertrendsearch.R;
import org.simpumind.com.twittertrendsearch.adapters.NewsListAdapter;
import org.simpumind.com.twittertrendsearch.models.NewsDataList;
import org.simpumind.com.twittertrendsearch.util.RestClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by SimpuMind on 4/5/16.
 */
public class NewsFragment extends Fragment {

    private ProgressBar infinityLoading;

    public String formatedDate;

    public ArrayList<NewsDataList> newsList;

    RecyclerView recyclerView;

    public static final String ARG_SCROLL_Y = "ARG_SCROLL_Y";

    public NewsListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news_event, container, false);

        final ObservableScrollView scrollView = (ObservableScrollView) view.findViewById(R.id.scroll);
        Activity parentActivity = getActivity();
        if (parentActivity instanceof ObservableScrollViewCallbacks) {
            // Scroll to the specified offset after layout
            Bundle args = getArguments();
            if (args != null && args.containsKey(ARG_SCROLL_Y)) {
                final int scrollY = args.getInt(ARG_SCROLL_Y, 0);
                ScrollUtils.addOnGlobalLayoutListener(scrollView, new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0, scrollY);
                    }
                });
            }

            // TouchInterceptionViewGroup should be a parent view other than ViewPager.
            // This is a workaround for the issue #117:
            // https://github.com/ksoichiro/Android-ObservableScrollView/issues/117
            scrollView.setTouchInterceptionViewGroup((ViewGroup) parentActivity.findViewById(R.id.root));

            scrollView.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentActivity);
        }
        return view;
    }

    @Override
    public void onViewCreated(View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        infinityLoading = (ProgressBar) rootView.findViewById(R.id.loading);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        recyclerView.hasFixedSize();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        newsList = new ArrayList<>();

        getEvents();

    }

    public void getEvents(){
        new GetEventDatas().execute();
    }


    public class GetEventDatas extends AsyncTask<Void, Void, Void> {

        private RestClient connect;
        private String text;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String apiUrl = "http://voice.atp-sevas.com/demo/yql/linda";
            connect = new RestClient(apiUrl);
            try {
                connect.Execute(RestClient.RequestMethod.GET);
                text = connect.getResponse();
                JSONArray jsonArray = new JSONArray(text);
                for(int i = 0; i < jsonArray.length(); i++ ){
                    JSONObject news = jsonArray.getJSONObject(i);
                    String newsName = news.optString("title");
                    String link = news.optString("href");
                    String timeStamp = news.optString("timestamp");
                    String comments = news.optString("comments");

                    newsList.add(new NewsDataList(parseDate(timeStamp),newsName, comments, link, 0, ""));

                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter = new NewsListAdapter();
            mAdapter.addAll(newsList);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            infinityLoading.setVisibility(View.GONE);
        }
    }

    public String parseDate(String str) {

        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sssZ");
            Date date = dateFormat.parse(str);

            dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            formatedDate = dateFormat.format(date);
        }catch (java.text.ParseException e){
            e.printStackTrace();
        }

        Log.d("Date", formatedDate);
        return  formatedDate;
    }

}
