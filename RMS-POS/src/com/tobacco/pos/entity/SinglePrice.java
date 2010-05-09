package com.tobacco.pos.entity;

import java.util.HashMap;

import android.os.Parcel;
import android.os.Parcelable;
 
public class SinglePrice implements Parcelable {
    private HashMap map;
 
    public SinglePrice() {
        map = new HashMap();
    }
 
    public SinglePrice(Parcel in) {
        map = new HashMap();
        readFromParcel(in);
    }
 
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public SinglePrice createFromParcel(Parcel in) {
            return new SinglePrice(in);
        }
 
        public SinglePrice[] newArray(int size) {
            return new SinglePrice[size];
        }
    };
 
    public int describeContents() {
        return 0;
    }
 
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(map.size());
        for (Object s: map.keySet()) {
            dest.writeString(s.toString());
            dest.writeString((String) map.get(s));
        }
    }
 
    public void readFromParcel(Parcel in) {
        int count = in.readInt();
        for (int i = 0; i < count; i++) {
            map.put(in.readString(), in.readString());
        }
    }
 
    public String get(String key) {
        return (String) map.get(key);
    }
 
    public void put(String key, String value) {
        map.put(key, value);
    }
}