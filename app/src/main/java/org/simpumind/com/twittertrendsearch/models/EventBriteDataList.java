package org.simpumind.com.twittertrendsearch.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

/**
 * Created by simpumind on 2/29/16.
 */
public class EventBriteDataList extends SugarRecord implements Parcelable{

    public String startTime;
    public String endTime;
    public String place;
    public String description;
    public String ids;
    public String eventName;
    public String url;

    public EventBriteDataList(String startTime, String endTime, String place, String description, String ids, String eventName, String url) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.place = place;
        this.description = description;
        this.ids = ids;
        this.eventName = eventName;
        this.url = url;
    }


    protected EventBriteDataList(Parcel in) {
        startTime = in.readString();
        endTime = in.readString();
        place = in.readString();
        description = in.readString();
        ids = in.readString();
        eventName = in.readString();
        url = in.readString();
    }

    public static final Creator<EventBriteDataList> CREATOR = new Creator<EventBriteDataList>() {
        @Override
        public EventBriteDataList createFromParcel(Parcel in) {
            return new EventBriteDataList(in);
        }

        @Override
        public EventBriteDataList[] newArray(int size) {
            return new EventBriteDataList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(place);
        dest.writeString(description);
        dest.writeString(ids);
        dest.writeString(eventName);
        dest.writeString(url);
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getName() {
        return eventName;
    }

    public void setName(String name) {
        this.eventName = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static Creator<EventBriteDataList> getCREATOR() {
        return CREATOR;
    }
}
