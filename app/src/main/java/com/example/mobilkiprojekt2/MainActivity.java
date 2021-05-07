package com.example.mobilkiprojekt2;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mobilkiprojekt2.models.NewsItem;
import com.example.mobilkiprojekt2.models.NewsItems;
import com.example.mobilkiprojekt2.ui.gallery.GalleryFragment;
import com.example.mobilkiprojekt2.ui.home.HomeFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private AppBarConfiguration mAppBarConfiguration;
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private DrawerLayout drawer;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_news, R.id.nav_weather)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Class fragmentClass;
                switch (item.getItemId()) {
                    case R.id.nav_news:
                        fragmentClass = HomeFragment.class;
                        break;
                    case R.id.nav_weather:
                        fragmentClass = GalleryFragment.class;
                        break;

                    default:
                        fragmentClass = HomeFragment.class;
                }

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        Log.d(TAG, "onCreate: started AsyncTask");
        getUrl("https://news.google.com/rss?hl=pl&gl=PL&ceid=PL:pl");
        refreshFragment();
        Log.d(TAG, "onCreate: wykoaenen");
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void refreshFragment() {
        getSupportFragmentManager().beginTransaction().detach(fragment).commitNowAllowingStateLoss();
        getSupportFragmentManager().beginTransaction().attach(fragment).commitAllowingStateLoss();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuItemId = item.getItemId();
        String newsFeedUrl;

        switch (menuItemId) {
            case R.id.topStoriesMenuItem:
                newsFeedUrl = "https://news.google.com/rss?hl=pl&gl=PL&ceid=PL:pl";
                NewsItems.getInstance().clearNewsItems();
//                refreshFragment();
                break;
            case R.id.countryMenuItem:
                newsFeedUrl = "https://news.google.com/rss/topics/CAAqIQgKIhtDQkFTRGdvSUwyMHZNRFZ4YUhjU0FuQnNLQUFQAQ?hl=pl&gl=PL&ceid=PL%3Apl";
                setTitle("Wiadomości z kraju");
                NewsItems.getInstance().clearNewsItems();
//                refreshFragment();
                break;
            case R.id.worldwideMenuItem:
                newsFeedUrl = "https://news.google.com/rss/topics/CAAqJggKIiBDQkFTRWdvSUwyMHZNRGx1YlY4U0FuQnNHZ0pRVENnQVAB?hl=pl&gl=PL&ceid=PL%3Apl";
                setTitle("Wiadomości ze świata");
                NewsItems.getInstance().clearNewsItems();
//                refreshFragment();
                break;
            case R.id.businessMenuItem:
                newsFeedUrl = "https://news.google.com/rss/topics/CAAqJggKIiBDQkFTRWdvSUwyMHZNRGx6TVdZU0FuQnNHZ0pRVENnQVAB?hl=pl&gl=PL&ceid=PL%3Apl";
                setTitle("Biznes");
                NewsItems.getInstance().clearNewsItems();
//                refreshFragment();
                break;
            case R.id.scitechMenuItem:
                newsFeedUrl = "https://news.google.com/rss/topics/CAAqKAgKIiJDQkFTRXdvSkwyMHZNR1ptZHpWbUVnSndiQm9DVUV3b0FBUAE?hl=pl&gl=PL&ceid=PL%3Apl";
                setTitle("Nauka i technika");
                NewsItems.getInstance().clearNewsItems();
//                refreshFragment();
                break;
            case R.id.entertainmentMenuItem:
                newsFeedUrl = "https://news.google.com/rss/topics/CAAqJggKIiBDQkFTRWdvSUwyMHZNREpxYW5RU0FuQnNHZ0pRVENnQVAB?hl=pl&gl=PL&ceid=PL%3Apl";
                setTitle("Rozrywka");
                NewsItems.getInstance().clearNewsItems();
//                refreshFragment();
                break;
            case R.id.sportsMenuItem:
                newsFeedUrl = "https://news.google.com/rss/topics/CAAqJggKIiBDQkFTRWdvSUwyMHZNRFp1ZEdvU0FuQnNHZ0pRVENnQVAB?hl=pl&gl=PL&ceid=PL%3Apl";
                setTitle("Sport");
                NewsItems.getInstance().clearNewsItems();
//                refreshFragment();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        getUrl(newsFeedUrl);
        refreshFragment();
        return true;
    }

    private void getUrl(String newsFeedUrl) {
        DownloadXML downloadXML = new DownloadXML();
        downloadXML.execute(newsFeedUrl);
//        DownloadImages downloadImages = new DownloadImages();
//        downloadImages.execute(newsFeedUrl);
    }

    private class DownloadXML extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: start");
            NewsItems.getInstance().parseNewsItems(s);

//            NewsAdapter newsAdapter = new NewsAdapter(MainActivity.this,
//                    R.layout.list_record, newsItems.getNewsItems());
//            listNews.setAdapter(newsAdapter);
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: starts with " + strings[0]);
            String googleNewsFeed = downloadNews(strings[0]);
            if (googleNewsFeed == null) {
                Log.e(TAG, "doInBackground: something went wrong with downloading data ");
            }
            return googleNewsFeed;
        }

        private String downloadNews(String urlPath) {
            StringBuilder xml = new StringBuilder();

            try {
                URL url = new URL(urlPath);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                int res = httpURLConnection.getResponseCode();
                Log.d(TAG, "downloadNews: HTTP " + res);
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                int characters;
                char[] inputBuffer = new char[500];
                while (true) {
                    characters = reader.read(inputBuffer);
                    if (characters < 0) {
                        break;
                    }
                    if (characters > 0) {
                        xml.append(String.copyValueOf(inputBuffer, 0, characters));
                    }
                }
                reader.close();

                System.out.println(xml.toString());

                return xml.toString();
            } catch (MalformedURLException e) {
                Log.e(TAG, "downloadNews: Invalid URL: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "downloadNews: IOException during reading data: " + e.getMessage());
            } catch (SecurityException e) {
                Log.e(TAG, "downloadNews: Security Exception. Probably missing permissions. " +
                        e.getMessage());
            }

            return null;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}