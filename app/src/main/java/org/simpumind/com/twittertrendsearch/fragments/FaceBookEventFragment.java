package org.simpumind.com.twittertrendsearch.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
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

import com.ftinc.kit.adapter.BetterRecyclerAdapter;
import com.gelitenight.waveview.library.WaveView;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.ndczz.infinityloading.InfinityLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simpumind.com.twittertrendsearch.R;
import org.simpumind.com.twittertrendsearch.activities.ViewerActivity;
import org.simpumind.com.twittertrendsearch.adapters.EventListAdapter;
import org.simpumind.com.twittertrendsearch.models.FaceBookEventList;
import org.simpumind.com.twittertrendsearch.util.ButteryProgressBar;
import org.simpumind.com.twittertrendsearch.util.WaveHelper;

import java.util.ArrayList;
import java.util.List;


public class FaceBookEventFragment extends Fragment {

    public String json;

    public String city;
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
        return inflater.inflate(R.layout.fragment_face_book_event, container, false);
    }

    @Override
    public void onViewCreated(View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        infinityLoading = (InfinityLoading) rootView.findViewById(R.id.loading);
        eventLists = new ArrayList<>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        recyclerView.hasFixedSize();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getActivity().getIntent();

        String jsondata = intent.getStringExtra("jsondata");

        // setEventData(jsondata);

        setJsonString(jsondata);

        // new Handler().postDelayed(new Runnable() {
        //    @Override
        //    public void run() {
        getEvents();
        //    }
        //  }, SPLASH_TIME_OUT);

        /*swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);*/

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), recyclerView, null);
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_face_book_event, container, false);
        infinityLoading = (InfinityLoading) rootView.findViewById(R.id.loading);
        eventLists = new ArrayList<>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        recyclerView.hasFixedSize();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getActivity().getIntent();

        String jsondata = intent.getStringExtra("jsondata");

        // setEventData(jsondata);

        setJsonString(jsondata);

       // new Handler().postDelayed(new Runnable() {
        //    @Override
        //    public void run() {
                getEvents();
        //    }
      //  }, SPLASH_TIME_OUT);

        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        return rootView;

    }*/

   /* @Override public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                swipeLayout.setRefreshing(false);
            }
        }, 5000);
    }*/



    public void getEvents(){
        new GetEventData().execute();
    }

    public String getJsonString() {
        return json;
    }

    public void setJsonString(String json) {
        this.json = json;
    }

    private void setEventData(String jsondata) {
        Log.d("OBJECT_JASON", jsondata);
        String all = null;


       // Log.d("Fire", eventLists.toString());

    }

    public class GetEventData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*InfinityLoading loading = new InfinityLoading(getActivity());
            loading.setProgressColor(Color.RED);
            loading.animate();*/
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if(getJsonString() == null){
                dataString = "";
            }
            dataString = getJsonString();
//            Log.d("OBJECT_JASON", dataString);
            try {
                JSONObject jsonObject = new JSONObject(dataString);
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
                    if(!place.isNull(TAG_LOCATION)) {
                        JSONObject location = place.getJSONObject(TAG_LOCATION);
                        city = getMessageFromServer(location, TAG_CITY);//location.optString(TAG_CITY, "");
                        country = getMessageFromServer(location, TAG_COUNTRY);//location.optString(TAG_COUNTRY, "");
                    }

                    eventLists.add(new FaceBookEventList(placeName, startTime,
                            endTime, eventName, description,
                            dataID, "", "", placeId));
                }

            /*if(!jsonObject.isNull("paging")) {
                JSONObject paging = jsonObject.getJSONObject("paging");
                JSONObject cursors = paging.getJSONObject("cursors");
                if (!cursors.isNull("after"))
                    afterString[0] = cursors.getString("after");
                else
                    noData[0] = true;
            }
            else
                noData[0] = true;*/
            }catch (JSONException e){
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
}
