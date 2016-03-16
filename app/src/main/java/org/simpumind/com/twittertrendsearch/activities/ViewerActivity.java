package org.simpumind.com.twittertrendsearch.activities;

/**
 * Created by simpumind on 3/9/16.
 */
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftinc.kit.util.Utils;
import com.ftinc.kit.widget.AspectRatioImageView;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

import org.simpumind.com.twittertrendsearch.R;
import org.simpumind.com.twittertrendsearch.models.FaceBookEventList;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by r0adkll on 1/11/15.
 */
public class ViewerActivity extends AppCompatActivity {

    public static final String EXTRA_OS = "extra_os_version";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.cover)
    AspectRatioImageView mCover;
    @Bind(R.id.title)
    TextView mTitle;
    @Bind(R.id.description)
    TextView mDescription;
    @Bind(R.id.even_date)
    TextView mDate;
    @Bind(R.id.event_location)
    TextView event_location;

    private FaceBookEventList faceBookEventList;
    private SlidrConfig mConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);
        ButterKnife.bind(this);

        // Get the status bar colors to interpolate between
        int primary = getResources().getColor(R.color.colorPrimaryDark);
        int secondary = getResources().getColor(R.color.red_500);

        // Build the slidr config
        int numPositions = SlidrPosition.values().length;
        SlidrPosition position = SlidrPosition.values()[Utils.getRandom().nextInt(numPositions)];
       // mPosition.setText(position.name());

        mConfig = new SlidrConfig.Builder()
                .primaryColor(primary)
                .secondaryColor(secondary)
                .position(position)
                .velocityThreshold(2400)
                .distanceThreshold(.25f)
                .edge(true)
                .touchSize(Utils.dpToPx(this, 32))
                .build();

        mConfig.setColorSecondary(R.id.color5);

        // Attach the Slidr Mechanism to this activity
        Slidr.attach(this, mConfig);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        faceBookEventList = getIntent().getParcelableExtra(EXTRA_OS);
        if(savedInstanceState != null) faceBookEventList = savedInstanceState.getParcelable(EXTRA_OS);

        // Set layout contents
        mTitle.setText(faceBookEventList.getEventName());
        mDescription.setText(faceBookEventList.getDescription());
        mDate.setText(faceBookEventList.getStartTime());
        event_location.setText(faceBookEventList.getPlaceName());

        // Load header image
        Glide.with(this)
                .load(R.id.color3)
                .crossFade()
                .into(mCover);
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
}
