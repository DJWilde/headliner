package com.example.mobilkiprojekt2.tasks;

import android.os.AsyncTask;

import com.example.mobilkiprojekt2.models.NewsItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class DownloadArticle extends AsyncTask<String, Void, String> {
    private NewsItem newsItem;

    public DownloadArticle(NewsItem newsItem) {
        this.newsItem = newsItem;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            Document document = Jsoup.connect(strings[0]).get();
            Element element = document.select("article").first();
            Element figure = document.select("figure").first();
            System.out.println(figure);
            System.out.println(element.select("p").text());
            newsItem.setDescription(element.select("p").text());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
