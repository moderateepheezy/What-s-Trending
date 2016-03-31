package org.simpumind.com.twittertrendsearch.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ParseException;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ftinc.kit.adapter.BetterRecyclerAdapter;
import com.kd.dynamic.calendar.generator.ImageGenerator;

import org.simpumind.com.twittertrendsearch.R;
import org.simpumind.com.twittertrendsearch.models.FaceBookEventList;
import org.simpumind.com.twittertrendsearch.util.RoundedLetterView;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by simpumind on 3/4/16.
 */
public class EventListAdapter extends BetterRecyclerAdapter<FaceBookEventList, EventListAdapter.ViewHolder> {

    java.sql.Timestamp timeStampDate;

    Context c;
    ImageGenerator mImageGenerator;
    Bitmap mGeneratedDateIcon;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        c = parent.getContext();
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        mImageGenerator = new ImageGenerator(c);
        FaceBookEventList faceBookEventList = getItem(position);
        holder.eventName.setText(faceBookEventList.getEventName());
        holder.startEvent.setText( getDays(faceBookEventList.getStartTime()));
        holder.eventDescription.setText(faceBookEventList.getDescription());
        /*if (items.size() == 0){
            holder.mRoundedLetterView.setTitleText("A");
        }else{
            holder.mRoundedLetterView.setTitleText(items.get(position).getEventName().substring(0, 1).toUpperCase());
        }
        if(position%2 == 0){
            holder.mRoundedLetterView.setBackgroundColor(c.getResources().getColor(R.color.green));
        }else{
            holder.mRoundedLetterView.setBackgroundColor(c.getResources().getColor(R.color.red));
        }*/

        getCalender(faceBookEventList);
        holder.mDisplayGeneratedImage.setImageBitmap(mGeneratedDateIcon);



    }

    private void getCalender(FaceBookEventList faceBookEventList) {
        Calendar cal = Calendar.getInstance();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            cal.setTime(sdf.parse(faceBookEventList.getStartTime()));
            mGeneratedDateIcon = mImageGenerator.generateDateImage(cal, R.drawable.empty_calendar);
            mImageGenerator.setIconSize(50, 50);
            mImageGenerator.setDateSize(30);
            mImageGenerator.setMonthSize(10);

            mImageGenerator.setDatePosition(42);
            mImageGenerator.setMonthPosition(14);

            mImageGenerator.setDateColor(Color.parseColor("#3c6eaf"));
            mImageGenerator.setMonthColor(Color.WHITE);

            mImageGenerator.setStorageToSDCard(true);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.event_start)    public TextView startEvent;
        @Bind(R.id.event_name)         public TextView eventName;
        @Bind(R.id.event_description)   public TextView eventDescription;
        @Bind(R.id.rlv_name_view)      public ImageView mDisplayGeneratedImage;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public String getDays(String dates){
        SimpleDateFormat dfDate  = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date d = null;
        java.util.Date d1 = null;
        Calendar cal = Calendar.getInstance();
        try {
            d = dfDate.parse(dates);
            d1 = dfDate.parse(dfDate.format(cal.getTime()));//Returns 15/10/2012
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        int diffInDays = (int) ((d.getTime() - d1.getTime())/ (1000 * 60 * 60 * 24));

        return diffInDays + "d";
    }


}

