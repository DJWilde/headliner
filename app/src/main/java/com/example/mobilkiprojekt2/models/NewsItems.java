package com.example.mobilkiprojekt2.models;

import android.util.Log;

import com.example.mobilkiprojekt2.NewsItemFragment;
import com.example.mobilkiprojekt2.tasks.DownloadArticle;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class NewsItems {
    private static final String TAG = "NewsItems";
    private static final NewsItems instance = new NewsItems();
    private ArrayList<NewsItem> newsItems;

    private NewsItems() {
        this.newsItems = new ArrayList<>();
    }

    public static NewsItems getInstance() {
        return instance;
    }

    public ArrayList<NewsItem> getNewsItems() {
        return newsItems;
    }

    public void clearNewsItems() {
        newsItems.clear();
    }

    public boolean parseNewsItems(String xml) {
        boolean status = true;
        NewsItem currentNewsItem = null;
        boolean inItem = false;
        String textValue = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xml));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        Log.d(TAG, "parseNewsItems: start tag of " + tagName);
                        if ("item".equalsIgnoreCase(tagName)) {
                            inItem = true;
                            currentNewsItem = new NewsItem();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        Log.d(TAG, "parseNewsItems: ending tag of " + tagName);
                        if (inItem) {
                            if ("item".equalsIgnoreCase(tagName)) {
                                newsItems.add(currentNewsItem);
                                inItem = false;
                            } else if ("title".equalsIgnoreCase(tagName)) {
                                currentNewsItem.setTitle(textValue);
                            } else if ("link".equalsIgnoreCase(tagName)) {
                                currentNewsItem.setLink(textValue);
                            } else if ("pubDate".equalsIgnoreCase(tagName)) {
                                currentNewsItem.setPubDate(textValue);
                            } else if ("description".equalsIgnoreCase(tagName)) {
                                currentNewsItem.setDescription("");
//                                DownloadArticle downloadArticle = new DownloadArticle(currentNewsItem);
//                                downloadArticle.execute(currentNewsItem.getLink());
                            } else if ("source".equalsIgnoreCase(tagName)) {
                                currentNewsItem.setSource(textValue);
                            }
                        }

                        break;

                    default:
                        // Nic tu nie ma
                }
                eventType = xpp.next();
            }
            for (NewsItem newsItem : newsItems) {
                Log.d(TAG, "parseNewsItems: *****************");
                Log.d(TAG, newsItem.toString());
            }
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }
}
