package com.example.mobilkiprojekt2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mobilkiprojekt2.models.NewsItem;
import com.example.mobilkiprojekt2.models.NewsItems;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class NewsItemActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_news_item_container);

        setTitle("Artykuł");

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_news_item_container);

        if (fragment == null) {
            fragment = NewsItemFragment.newInstance((int) getIntent().getSerializableExtra("NEWS_ITEM_ID"));
            fragmentManager.beginTransaction().add(R.id.fragment_news_item_container, fragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.article_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.browser_article:
                NewsItem newsItem = NewsItems.getInstance().getNewsItems().get((int) getIntent().getSerializableExtra("NEWS_ITEM_ID"));
                String url = newsItem.getLink();
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}