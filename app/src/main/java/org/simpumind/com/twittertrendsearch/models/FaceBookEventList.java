package org.simpumind.com.twittertrendsearch.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

/**
 * Created by simpumind on 2/29/16.
 */
public class FaceBookEventList extends SugarRecord implements Parcelable{

    public String placeName;
    public String startTime;
    public String endTime;
    public String eventName;
    public String description;
    public String ids;
    public String city;
    public String country;
    public String place_id;

    public FaceBookEventList(String placeName, String startTime, String endTime, String eventName,
                             String description, String id, String city, String country, String place_id) {
        this.placeName = placeName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventName = eventName;
        this.description = description;
        this.ids = id;
        this.city = city;
        this.country = country;
        this.place_id = place_id;
    }

    protected FaceBookEventList(Parcel in) {
        placeName = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        eventName = in.readString();
        description = in.readString();
        ids = in.readString();
        city = in.readString();
        country = in.readString();
        place_id = in.readString();
    }

    public static final Creator<FaceBookEventList> CREATOR = new Creator<FaceBookEventList>() {
        @Override
        public FaceBookEventList createFromParcel(Parcel in) {
            return new FaceBookEventList(in);
        }

        @Override
        public FaceBookEventList[] newArray(int size) {
            return new FaceBookEventList[size];
        }
    };

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
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

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
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

    public void setId(String id) {
        this.ids = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(placeName);
        parcel.writeString(startTime);
        parcel.writeString(endTime);
        parcel.writeString(eventName);
        parcel.writeString(description);
        parcel.writeString(ids);
        parcel.writeString(city);
        parcel.writeString(country);
        parcel.writeString(place_id);
    }
}
