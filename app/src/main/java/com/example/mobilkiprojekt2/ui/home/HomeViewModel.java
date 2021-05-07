package com.example.mobilkiprojekt2.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mobilkiprojekt2.models.NewsItem;
import com.example.mobilkiprojekt2.models.NewsItems;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<ArrayList<NewsItem>> newsItems;

    public HomeViewModel() {
        newsItems = new MutableLiveData<>();
        newsItems.setValue(NewsItems.getInstance().getNewsItems());
    }

    public LiveData<ArrayList<NewsItem>> getNewsItems() {
        return newsItems;
    }
}