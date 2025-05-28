package com.example.lab6;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherFragment extends Fragment {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView locationText, tempText, descriptionText;
    private ImageView weatherImage;
    private final String apiKey = "0c996cd3bb80eaf8f54a8c6fe5033536";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        locationText = view.findViewById(R.id.text_location);
        tempText = view.findViewById(R.id.text_temp);
        descriptionText = view.findViewById(R.id.text_description);
        weatherImage = view.findViewById(R.id.image_weather);
        getLastLocation();
        return view;
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double lat = location.getLatitude();
                            double lon = location.getLongitude();
                            locationText.setText("Lat: " + lat + ", Lon: " + lon);
                            getWeatherByCoords(lat, lon);
                        } else {
                            locationText.setText("Location not available");
                        }
                    }
                });
    }

    private void getWeatherByCoords(double lat, double lon) {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat +
                "&lon=" + lon + "&appid=" + apiKey + "&units=metric";
        new GetWeatherTask().execute(url);
    }

    private class GetWeatherTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder json = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }

                JSONObject data = new JSONObject(json.toString());
                String weather = data.getJSONArray("weather").getJSONObject(0).getString("description");
                String temp = data.getJSONObject("main").getString("temp");
                String iconCode = data.getJSONArray("weather").getJSONObject(0).getString("icon");
                String cityName = data.getString("name");

                return new String[]{cityName, weather, temp, iconCode};

            } catch (Exception e) {
                e.printStackTrace();
                return new String[]{"Unknown", "Error", "?", ""};
            }
        }

        @Override
        protected void onPostExecute(String[] result) {
            locationText.setText(result[0]);
            tempText.setText(result[2] + "°C");
            descriptionText.setText(result[1]);


            String iconCode = result[3];
            int resId = getResources().getIdentifier("ic_" + iconCode, "drawable", getActivity().getPackageName());

            if (resId != 0) {
                weatherImage.setImageResource(resId);
            } else {
                weatherImage.setImageResource(R.drawable.ic_default_weather);
            }
        }
    }

}
