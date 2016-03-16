package org.simpumind.com.twittertrendsearch.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftinc.kit.adapter.BetterRecyclerAdapter;


import org.simpumind.com.twittertrendsearch.R;
import org.simpumind.com.twittertrendsearch.models.TrendListItem;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by simpumind on 2/15/16.
 */
public class TweetTagAdapter extends BetterRecyclerAdapter<TrendListItem, TweetTagAdapter.OSViewHolder> {

    @Override
    public OSViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new OSViewHolder(view);
    }


    // @Override
    public void add(TrendListItem object) {
        super.add(object);
    }

    @Override
    public void onBindViewHolder(OSViewHolder viewHolder, int i) {
        super.onBindViewHolder(viewHolder, i);
        viewHolder.tweetName.setText(items.get(i).getNames());
        viewHolder.tweetVolume.setText(String.valueOf(items.get(i).getTweetVolume()));
    }

    public static class OSViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tweet_name)         public TextView tweetName;
        @Bind(R.id.tweet_volume)   public TextView tweetVolume;

        public OSViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

/*
public class TweetTagAdapter extends BaseAdapter {

    private Context mContext;
    private List<TrendList> tweetList;

    public TweetTagAdapter(Context mContext, List<TrendList> tweetList) {
        this.mContext = mContext;
        this.tweetList = tweetList;
    }

    public void setTweetTagList(List<TrendList> tweetList) {
        this.tweetList = tweetList;
    }

    @Override
    public int getCount() {
        if (tweetList != null) {
            return tweetList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return position; // we don't need it now
    }

    @Override
    public long getItemId(int position) {
        return tweetList.size(); // we don't need it now
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.tweetname = (TextView) row.findViewById(R.id.tweet_name);
            holder.tweetUrl = (TextView) row.findViewById(R.id.tweet_url);
            holder.tweetVolume = (TextView) row.findViewById(R.id.tweet_volume);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        holder.tweetname.setText(tweetList.get(position).trends.get(position).name);
        holder.tweetUrl.setText(tweetList.get(position).trends.get(position).url);
        holder.tweetVolume.setText(String.valueOf(tweetList.get(position).trends.get(position).tweetVolume));
        return row;
    }

    static class ViewHolder {
        TextView tweetname;
        TextView tweetUrl;
        TextView tweetVolume;
    }

}
*/
