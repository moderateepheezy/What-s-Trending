package org.simpumind.com.twittertrendsearch.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simpumind.com.twittertrendsearch.adapters.EventListAdapter;
import org.simpumind.com.twittertrendsearch.models.FaceBookEventList;
import org.simpumind.com.twittertrendsearch.R;

import java.util.ArrayList;
import java.util.List;

public class FacebookEventActivity extends AppCompatActivity {

    public ProgressDialog progressDialog;

    public String json;

    public String city;
    public String country;
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

    private static int SPLASH_TIME_OUT = 3000;

    public static List<FaceBookEventList> eventLists;
    EventListAdapter tweetTagAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        eventLists = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.hasFixedSize();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();

        String jsondata = intent.getStringExtra("jsondata");

       // setEventData(jsondata);

        setJsonString(jsondata);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               getEvents();
            }
        }, SPLASH_TIME_OUT);


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

    private void setEventData(String jsondata) {
        Log.d("OBJECT_JASON", jsondata);
        String all = null;


        Log.d("Fire", eventLists.toString());

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
            Log.d("ArrayValue", eventLists.toString());
        }

        return super.onOptionsItemSelected(item);
    }

    public class GetEventData extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(FacebookEventActivity.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String dataString = getJsonString();
            Log.d("OBJECT_JASON", dataString);
            try {
                JSONObject jsonObject = new JSONObject(dataString);
                JSONArray jsonArray =   jsonObject.getJSONArray(TAG_DATA);
                Log.d("JAsonArray", jsonArray.toString());
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
            Log.d("EventListTag", eventLists.toString());
            /*recyclerView.setAdapter(new EventListAdapter(eventLists, new EventListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(FaceBookEventList item) {
                    Toast.makeText(getApplicationContext(), "Item Clicked", Toast.LENGTH_LONG).show();
                }
            }));*/

            progressDialog.dismiss();
        }
    }

    public String getMessageFromServer(JSONObject response, String data) throws JSONException {
        return ((response.has(data) && !response.isNull(data))) ? response.getString(data) : "hel";
    }

}
