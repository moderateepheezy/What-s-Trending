package org.simpumind.com.twittertrendsearch.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import org.simpumind.com.twittertrendsearch.R;
import org.simpumind.com.twittertrendsearch.api.TweetList;

/**
 * @author Sergii Zhuk
 *         Date: 23.07.2014
 *         Time: 23:50
 */
public class TweetAdapter extends BaseAdapter {

	private Context mContext;
	private TweetList tweetList;

	public TweetAdapter(Context mContext, TweetList tweetList) {
		this.mContext = mContext;
		this.tweetList = tweetList;
	}

	public void setTweetList(TweetList tweetList) {
		this.tweetList = tweetList;
	}

	@Override
	public int getCount() {
		if (tweetList.tweets != null) {
			return tweetList.tweets.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return null; // we don't need it now
	}

	@Override
	public long getItemId(int position) {
		return 0; // we don't need it now
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		final ViewHolder holder;

		LayoutInflater inflater = (LayoutInflater)
				mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		if (row == null) {
			row = inflater.inflate(R.layout.row_tweet, parent, false);
			holder = new ViewHolder();
			holder.textTweet = (TextView) row.findViewById(R.id.text_tweet);
			holder.textUser = (TextView) row.findViewById(R.id.text_user);
			holder.imageLogo = (ImageView) row.findViewById(R.id.image_user_logo);
			holder.textImage = (ImageView) row.findViewById(R.id.textImage);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}
		holder.textTweet.setText(tweetList.tweets.get(position).text);
		holder.textUser.setText(tweetList.tweets.get(position).user.name);
		Picasso.with(mContext).load(tweetList.tweets.get(position).user.profileImageUrl).into(holder.imageLogo);
		Picasso.with(mContext) .load(tweetList.tweets.get(position).textImage).into(holder.textImage);
		return row;
	}

	static class ViewHolder {
		TextView textTweet;
		TextView textUser;
		ImageView imageLogo;
		ImageView textImage;
	}

}
