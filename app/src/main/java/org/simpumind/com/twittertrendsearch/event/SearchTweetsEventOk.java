package org.simpumind.com.twittertrendsearch.event;


import org.simpumind.com.twittertrendsearch.api.TweetList;

/**
 * @author Sergii Zhuk
 *         Date: 23.06.2014
 *         Time: 17:29
 */
public class SearchTweetsEventOk {

	public final TweetList tweetsList;

	public SearchTweetsEventOk(TweetList tweets) {
		this.tweetsList = tweets;
	}

}
