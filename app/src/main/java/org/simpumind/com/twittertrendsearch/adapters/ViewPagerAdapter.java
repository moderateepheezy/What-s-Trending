package org.simpumind.com.twittertrendsearch.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.simpumind.com.twittertrendsearch.fragments.FaceBookEventFragment;
import org.simpumind.com.twittertrendsearch.fragments.TwitterTrendFragment;

/**
 * Created by simpumind on 3/18/16.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                FaceBookEventFragment faceBookEvent = new FaceBookEventFragment();
                return faceBookEvent;
            case 1:
                TwitterTrendFragment twitterTrendFragment = new TwitterTrendFragment();
                return twitterTrendFragment;
            default:
                return new FaceBookEventFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;           // As there are only 3 Tabs
    }

}
