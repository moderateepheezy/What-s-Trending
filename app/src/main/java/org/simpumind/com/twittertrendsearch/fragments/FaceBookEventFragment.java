package org.simpumind.com.twittertrendsearch.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.ftinc.kit.adapter.BetterRecyclerAdapter;
import com.gelitenight.waveview.library.WaveView;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.ndczz.infinityloading.InfinityLoading;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simpumind.com.twittertrendsearch.R;
import org.simpumind.com.twittertrendsearch.activities.HomeActivity;
import org.simpumind.com.twittertrendsearch.activities.ViewerActivity;
import org.simpumind.com.twittertrendsearch.adapters.EventListAdapter;
import org.simpumind.com.twittertrendsearch.models.FaceBookEventList;
import org.simpumind.com.twittertrendsearch.util.ButteryProgressBar;
import org.simpumind.com.twittertrendsearch.util.RestClient;
import org.simpumind.com.twittertrendsearch.util.WaveHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FaceBookEventFragment extends Fragment {

    public static final String ARG_SCROLL_Y = "ARG_SCROLL_Y";

    public String json;

    public String formatedDate;
    public String country;
    static String dataString;
    /********************************************************************/
    private static final String TAG_DATA = "data";

    private final static String TAG_DESCRIPTION = "description";
    private static final String TAG_END_TIME = "end_time";
    private final static String TAG_EVENT_NAME = "name";
    private final static String TAG_START_TIME = "start_time";
    private final static String TAG_ID = "id";
/******************************************************************/

    /*******************************************************************/

    private final static String TAG_PLACE = "place";
    private final static String TAG_PLACE_NAME = "name";
    private final static String TAG_LOCATION = "location";
    private final static String TAG_CITY = "city";
    private final static String TAG_COUNTRY = "country";
    private final static String TAG_PLACE_ID  = "id";

    /*********************************************************************/



    private InfinityLoading infinityLoading;

    public static List<FaceBookEventList> eventLists;
    RecyclerView recyclerView;
    //public SwipeRefreshLayout swipeLayout;
    public EventListAdapter mAdapter;

    public FaceBookEventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity());
        View view = inflater.inflate(R.layout.fragment_face_book_event, container, false);

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
        infinityLoading = (InfinityLoading) rootView.findViewById(R.id.loading);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        recyclerView.hasFixedSize();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        eventLists = new ArrayList<>();
        Intent intent = getActivity().getIntent();

        String jsondata = intent.getStringExtra("jsondata");

        setJsonString(jsondata);

        SharedPreferences settings = getActivity().getSharedPreferences("KEY_NAME",
                getActivity().MODE_PRIVATE);
        String fbSession = settings.getString("fbsession", "");

        if(fbSession.isEmpty()){
            HomeActivity.checkLogin();
            Intent intentb = new Intent(getActivity(), HomeActivity.class);
            startActivity(intentb);
        }else {
           // List<FaceBookEventList> eventListss = SugarRecord.listAll(FaceBookEventList.class);
           // if(eventListss == null) {
                getEvents();
        }

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), recyclerView, null);
    }


    public void getEvents(){
        new GetEventData().execute();
    }

    public String getJsonString() {
        return json;
    }

    public void setJsonString(String json) {
        this.json = json;
    }


    public class GetEventData extends AsyncTask<Void, Void, Void> {

        private RestClient connect;
        private String text;

        final String[] afterString = {""};  // will contain the next page cursor
        final Boolean[] noData = {false};   // stop when there is no after cursor

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*InfinityLoading loading = new InfinityLoading(getActivity());
            loading.setProgressColor(Color.RED);
            loading.animate();*/
        }

        @Override
        protected Void doInBackground(Void... voids) {

            SharedPreferences settings = getActivity().getSharedPreferences("KEY_NAME",
                    getActivity().MODE_PRIVATE);
            String fbSession = settings.getString("fbsession", "");

            String apiUrl = "https://graph.facebook.com/search?q=Nigeria&type=event&access_token=" + fbSession;
            if(getJsonString() == null){
                dataString = "";
            }
            dataString = getJsonString();
//            Log.d("OBJECT_JASON", dataString);
            connect = new RestClient(apiUrl);
            try {
                connect.Execute(RestClient.RequestMethod.GET);
                text = connect.getResponse();
                JSONObject jsonObject = new JSONObject(text);
                JSONArray jsonArray =   jsonObject.getJSONArray(TAG_DATA);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject data = jsonArray.getJSONObject(i);
                    String endTime = getMessageFromServer(data, TAG_END_TIME);//data.optString(TAG_END_TIME, "");
                    String description = getMessageFromServer(data, TAG_DESCRIPTION);//data.optString(TAG_DESCRIPTION, "");
                    String startTime = getMessageFromServer(data, TAG_START_TIME);//data.optString(TAG_START_TIME, "");
                    String eventName = getMessageFromServer(data, TAG_EVENT_NAME);//data.optString(TAG_EVENT_NAME, "");
                    String dataID = getMessageFromServer(data, TAG_ID); //data.optString(TAG_ID, "");
                    //Looping through place object
                    JSONObject place = data.getJSONObject(TAG_PLACE);
                    String  placeName = getMessageFromServer(place, TAG_PLACE_NAME);//place.optString(TAG_PLACE_NAME, "");
                    String placeId = getMessageFromServer(place, TAG_PLACE_ID);//place.optString(TAG_PLACE_ID, "");

                    //Loop through place location
                    /*if(!place.isNull(TAG_LOCATION)) {
                        JSONObject location = place.getJSONObject(TAG_LOCATION);
                        city = getMessageFromServer(location, TAG_CITY);//location.optString(TAG_CITY, "");
                        country = getMessageFromServer(location, TAG_COUNTRY);//location.optString(TAG_COUNTRY, "");
                    }*/

                    FaceBookEventList faceBookEventList = new FaceBookEventList(placeName, parseDate(startTime),
                            parseDate(endTime), eventName, description,
                            dataID, "", "", placeId);
                    faceBookEventList.save();
                    eventLists.add(faceBookEventList);
                }

            if(!jsonObject.isNull("paging")) {
                JSONObject paging = jsonObject.getJSONObject("paging");
                JSONObject cursors = paging.getJSONObject("cursors");
                if (!cursors.isNull("after"))
                    afterString[0] = cursors.getString("after");
                else
                    noData[0] = true;
            }
            else
                noData[0] = true;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
           // Log.d("EventListTag", eventLists.toString());
            mAdapter = new EventListAdapter();
            mAdapter.addAll(eventLists);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mAdapter.setOnItemClickListener(new BetterRecyclerAdapter.OnItemClickListener<FaceBookEventList>() {
                @Override
                public void onItemClick(View v, FaceBookEventList item, int position) {
                    Intent viewer = new Intent(getActivity(), ViewerActivity.class);
                    viewer.putExtra(ViewerActivity.EXTRA_OS, item);
                    startActivity(viewer);
                }
            });
            infinityLoading.setVisibility(View.GONE);
            //swipeLayout.setRefreshing(false);
        }
    }

    public String getMessageFromServer(JSONObject response, String data) throws JSONException {
        return ((response.has(data) && !response.isNull(data))) ? response.getString(data) : "";
    }

    @Override
    public void onDestroyView() {
       // swipeLayout.removeAllViews();
        super.onDestroyView();
    }

    public String parseDate(String str) {

        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
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
