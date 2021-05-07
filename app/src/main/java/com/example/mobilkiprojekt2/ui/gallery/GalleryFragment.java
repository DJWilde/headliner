package com.example.mobilkiprojekt2.ui.gallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.mobilkiprojekt2.MainActivity;
import com.example.mobilkiprojekt2.R;
import com.example.mobilkiprojekt2.weather.FetchWeatherTask;
import com.example.mobilkiprojekt2.weather.Weather;
import com.example.mobilkiprojekt2.weather.WeatherParser;
import com.example.mobilkiprojekt2.weather.WeatherTaskListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class GalleryFragment extends Fragment implements WeatherTaskListener, LocationListener {
    private final String API_KEY = "60da09af60520d7e26ad70ae4ea28d8f";
    private static final String TAG = "GalleryFragment";
    private GalleryViewModel galleryViewModel;
    private TextView cityTextView;
    private TextView conditionTextView;
    private TextView temperatureTextView;
    private TextView feelsLikeTemperatureTextView;
    private ImageView weatherImageView;
    private LocationManager locationManager;

    private FusedLocationProviderClient client;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_weather, container, false);

        cityTextView = root.findViewById(R.id.cityTextView);
        conditionTextView = root.findViewById(R.id.conditionTextView);
        temperatureTextView = root.findViewById(R.id.temperatureTextView);
        feelsLikeTemperatureTextView = root.findViewById(R.id.feelsLikeTempTextView);
        weatherImageView = root.findViewById(R.id.weatherImageView);

        client = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ContextCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(getActivity(), ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, 100);
        }

        getLatLong();

        String weatherUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" + galleryViewModel.getLatitude()
                + "&lon=" + galleryViewModel.getLongitude() + "&appid=" + API_KEY;

        new FetchWeatherTask(this).execute(weatherUrl);
        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && (grantResults.length > 0) && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            getLocation();
        } else {
            Toast.makeText(getActivity(), "Brak uprawnień", Toast.LENGTH_LONG).show();
        }
    }

    private void getLatLong() {
        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
            galleryViewModel.setLatitude(location.getLatitude());
            galleryViewModel.setLongitude(location.getLongitude());
            String city = getCityName(location.getLatitude(), location.getLongitude());
            cityTextView.setText(city);
        } catch (SecurityException e) {
            e.getMessage();
        }
    }

    private String getCityName(double latitude, double longitude) {
        String city = "";

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocation(latitude, longitude, 10);
            if (addressList.size() > 0) {
                for (Address address : addressList) {
                    if (address.getLocality() != null && address.getLocality().length() > 0) {
                        city = address.getLocality();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return city;
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
//                        latitude.setText(String.valueOf(location.getLatitude()));
                        galleryViewModel.setLatitude(location.getLatitude());
//                        longitude.setText(String.valueOf(location.getLongitude()));
                        galleryViewModel.setLongitude(location.getLongitude());
                    } else {
                        LocationRequest locationRequest = new LocationRequest().
                                setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY).setInterval(10000).
                                setFastestInterval(1000).setNumUpdates(1);

                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();
//                                latitude.setText(String.valueOf(location1.getLatitude()));
                                galleryViewModel.setLatitude(location1.getLatitude());
//                                longitude.setText(String.valueOf(location1.getLongitude()));
                                galleryViewModel.setLongitude(location1.getLongitude());
                            }
                        };

                        client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                }
            });
        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    @Override
    public void onWeatherPreExecute() {

    }

    @Override
    public void onWeatherPostExecute(Weather weather) {
        if (weather != null) {
            conditionTextView.setText(weather.getCondition());
            int temperature = Math.round(weather.getTemperature() - 273.15f);
            temperatureTextView.setText(String.valueOf(temperature));
            int feelsLikeTemperature = Math.round(weather.getFeelsLikeTemperature() - 273.15f);
            feelsLikeTemperatureTextView.setText(String.valueOf(feelsLikeTemperature));
            String imgUrl = "https://openweathermap.org/img/wn/" + weather.getIcon() + "@2x.png";
            Glide.with(getActivity()).asBitmap().load(imgUrl).placeholder(R.mipmap.ic_launcher)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).into(weatherImageView);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.weather_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        galleryViewModel.setLatitude(location.getLatitude());
        galleryViewModel.setLongitude(location.getLongitude());
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}