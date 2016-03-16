package org.simpumind.com.twittertrendsearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftinc.kit.adapter.BetterRecyclerAdapter;

import org.simpumind.com.twittertrendsearch.R;
import org.simpumind.com.twittertrendsearch.models.FaceBookEventList;
import org.simpumind.com.twittertrendsearch.util.RoundedLetterView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by simpumind on 3/4/16.
 */
public class EventListAdapter extends BetterRecyclerAdapter<FaceBookEventList, EventListAdapter.ViewHolder> {


    Context c;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        c = parent.getContext();
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        FaceBookEventList faceBookEventList = getItem(position);
        holder.eventName.setText(faceBookEventList.getEventName());
        holder.eventDescription.setText(faceBookEventList.getStartTime());
        if (items.size() == 0){
            holder.mRoundedLetterView.setTitleText("A");
        }else{
            holder.mRoundedLetterView.setTitleText(items.get(position).getEventName().substring(0, 1).toUpperCase());
        }
        if(position%2 == 0){
            holder.mRoundedLetterView.setBackgroundColor(c.getResources().getColor(R.color.green));
        }else{
            holder.mRoundedLetterView.setBackgroundColor(c.getResources().getColor(R.color.red));
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.event_name)         public TextView eventName;
        @Bind(R.id.event_description)   public TextView eventDescription;
        @Bind(R.id.rlv_name_view)      public RoundedLetterView mRoundedLetterView;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

