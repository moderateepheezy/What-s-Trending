package org.simpumind.com.twittertrendsearch.activities;

/**
 * Created by simpumind on 3/9/16.
 */
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.simpumind.com.twittertrendsearch.R;
import org.simpumind.com.twittertrendsearch.models.EventsDataList;


import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * Created by r0adkll on 1/11/15.
 */
public class ViewerActivity extends AppCompatActivity {

    public static final String EXTRA_OS = "extra_os_version";

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    RecyclerView recyclerView;


    public static String placeName;
    public static String starTime;
    public static String description;

    private EventsDataList faceBookEventList;

    CardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);

        collapsingToolbarLayout = (CollapsingToolbarLayout)    findViewById(R.id.collapsing_toolbar);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        toolbar = (Toolbar) findViewById(R.id.toolbars);

        faceBookEventList = getIntent().getParcelableExtra(EXTRA_OS);

        if(savedInstanceState != null) faceBookEventList = savedInstanceState.getParcelable(EXTRA_OS);

        // Set layout contents
        collapsingToolbarLayout.setTitle(faceBookEventList.getEventName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

       /* mDescription.setText(faceBookEventList.getDescription());
        mDate.setText(faceBookEventList.getStartTime());
        event_location.setText(faceBookEventList.getPlaceName());*/

        placeName  = faceBookEventList.getPlaceName();
        starTime = faceBookEventList.getStartTime();
        description = faceBookEventList.getDescription();

        adapter = new CardAdapter();
        recyclerView.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_OS, faceBookEventList);
    }

    public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

        @Override
        public int getItemCount() {
            return 1;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_layout, parent, false);
            return new ViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.mDescription.setText(description);
            holder.mDate.setText(starTime);
            holder.event_location.setText(placeName);
        }
        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView mDescription;
            TextView mDate;
            TextView event_location;
            public ViewHolder(View itemView) {
                super(itemView);
                mDescription = (TextView) itemView.findViewById(R.id.description);
                mDate = (TextView) itemView.findViewById(R.id.even_date);
                event_location = (TextView) itemView.findViewById(R.id.event_location);
            }
        }
    }
}
