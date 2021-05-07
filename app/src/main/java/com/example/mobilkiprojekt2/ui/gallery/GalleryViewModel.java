package com.example.mobilkiprojekt2.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GalleryViewModel extends ViewModel {

//    private MutableLiveData<String> mText;
    private double latitude;
    private double longitude;

    public GalleryViewModel() {
//        mText = new MutableLiveData<>();
//        mText.setValue("This is gallery fragment");
        latitude = 0;
        longitude = 0;
    }

//    public LiveData<String> getText() {
//        return mText;
//    }
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}