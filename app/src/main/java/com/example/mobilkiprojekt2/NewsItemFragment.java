package com.example.mobilkiprojekt2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobilkiprojekt2.models.NewsItem;
import com.example.mobilkiprojekt2.models.NewsItems;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class NewsItemFragment extends Fragment {
    private TextView titleTextView;
    private TextView sourceTextView;
    private TextView dateTextView;
    private TextView articleTextView;
    private NewsItem newsItem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int position = (Integer) getArguments().getSerializable("NEWS_ITEM_ID");
        newsItem = NewsItems.getInstance().getNewsItems().get(position);
        DownloadArticle downloadArticle = new DownloadArticle();
        downloadArticle.execute(newsItem.getLink());
    }

    public static NewsItemFragment newInstance(int id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("NEWS_ITEM_ID", id);
        NewsItemFragment newsItemFragment = new NewsItemFragment();
        newsItemFragment.setArguments(bundle);
        return newsItemFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_item, container, false);

        titleTextView = view.findViewById(R.id.titleTextView);
        titleTextView.setText(newsItem.getTitle());
        sourceTextView = view.findViewById(R.id.sourceTextView);
        sourceTextView.setText(newsItem.getSource());
        dateTextView = view.findViewById(R.id.dateTextView);
        dateTextView.setText(newsItem.getPubDate());
        articleTextView = view.findViewById(R.id.articleTextView);
        articleTextView.setText(newsItem.getDescription());

        return view;
    }

    public class DownloadArticle extends AsyncTask<String, Void, String> {

        Element element;
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            newsItem.setDescription(element.select("p").text());
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Document document = Jsoup.connect(strings[0]).get();
                element = document.select("article").first();
                Element figure = document.select("figure > img[src]").first();
                System.out.println(figure);
                System.out.println(element.select("p").text());
                newsItem.setDescription(element.select("p").text());
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
