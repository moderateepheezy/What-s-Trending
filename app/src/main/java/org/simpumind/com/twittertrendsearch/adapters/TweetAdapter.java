package org.simpumind.com.twittertrendsearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.simpumind.com.twittertrendsearch.R;
import org.simpumind.com.twittertrendsearch.models.TrendListItem;
import org.simpumind.com.twittertrendsearch.util.RoundedLetterView;

import java.util.List;

/**
 * Created by simpumind on 2/18/16.
 */
public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<TrendListItem> items;
    int itemLayout;
    public OnItemClickListener listener;

    Context c;
   /* public TweetAdapter(List<TrendListItem> items, int itemLayout){
        this.items = items;
        this.itemLayout = itemLayout;
    }*/

    public TweetAdapter(List<TrendListItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public TweetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        c = parent.getContext();
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
        if (items.size() == 0){
            holder.mRoundedLetterView.setTitleText("A");
        }else{
            holder.mRoundedLetterView.setTitleText(items.get(position).getNames().substring(0, 1).toUpperCase());
        }
        if(position%2 == 0){
            holder. mRoundedLetterView.setBackgroundColor(c.getResources().getColor(R.color.green));
        }else{
            holder.mRoundedLetterView.setBackgroundColor(c.getResources().getColor(R.color.red));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class  ViewHolder extends RecyclerView.ViewHolder{
        public TextView tweetName;
        public RoundedLetterView mRoundedLetterView;
        public TextView twwetVolume;

        public ViewHolder(View itemView){
            super(itemView);
            tweetName = (TextView) itemView.findViewById(R.id.tweet_name);
            twwetVolume = (TextView) itemView.findViewById(R.id.tweet_volume);
            mRoundedLetterView = (RoundedLetterView) itemView.findViewById(R.id.rlv_name_view);
        }

        public void bind(final TrendListItem item, final OnItemClickListener listener){
            tweetName.setText(item.getNames());
            twwetVolume.setText(String.valueOf(item.getTweetVolume()));
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(TrendListItem item);
    }
}
