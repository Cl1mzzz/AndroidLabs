package com.example.lab6;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class WeatherWidget extends AppWidgetProvider {

    private static final String UPDATE_ACTION = "com.example.lab6.UPDATE_WIDGET";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
        scheduleNextUpdate(context);
    }


    @SuppressLint("MissingPermission")
    public static void updateWidget(Context context, AppWidgetManager manager, int widgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
        Intent launchIntent = new Intent(context, MainActivity.class);
        PendingIntent launchPendingIntent = PendingIntent.getActivity(
                context,
                0,
                launchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        views.setOnClickPendingIntent(R.id.center_container, launchPendingIntent);

        views.setTextViewText(R.id.text_city, "Loading...");
        views.setTextViewText(R.id.text_temp, "");
        views.setInt(R.id.widget_layout, "setBackgroundResource", R.drawable.bg_default);

        Intent intent = new Intent(context, WeatherWidget.class);
        intent.setAction("com.example.lab6.UPDATE_WIDGET");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        manager.updateAppWidget(widgetId, views);

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        new Thread(() -> {
                            try {
                                String apiKey = "0c996cd3bb80eaf8f54a8c6fe5033536";
                                URL url = new URL("https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + apiKey + "&units=metric");

                                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                                StringBuilder response = new StringBuilder();
                                String inputLine;

                                while ((inputLine = in.readLine()) != null) {
                                    response.append(inputLine);
                                }
                                in.close();

                                JSONObject json = new JSONObject(response.toString());
                                String temperature = json.getJSONObject("main").getString("temp");
                                String city = json.getString("name");
                                JSONObject weatherObject = json.getJSONArray("weather").getJSONObject(0);
                                String weatherMain = weatherObject.getString("description");
                                String icon = weatherObject.getString("icon");

                                int backgroundRes;
                                switch (weatherMain.toLowerCase()) {
                                    case "clear sky":
                                        backgroundRes = R.drawable.bg_clear;
                                        break;
                                    case "clouds":
                                        backgroundRes = R.drawable.bg_clouds;
                                        break;
                                    case "rain":
                                    case "drizzle":
                                    case "thunderstorm":
                                        backgroundRes = R.drawable.bg_rain;
                                        break;
                                    case "snow":
                                        backgroundRes = R.drawable.bg_snow;
                                        break;
                                    default:
                                        backgroundRes = R.drawable.bg_default;
                                        break;
                                }

                                views.setTextViewText(R.id.text_city, city);
                                views.setTextViewText(R.id.text_weather, weatherMain);
                                views.setTextViewText(R.id.text_temp, temperature + "°C");
                                views.setInt(R.id.widget_layout, "setBackgroundResource", backgroundRes);


                                int iconResId = context.getResources().getIdentifier("ic_" + icon, "drawable", context.getPackageName());
                                if (iconResId != 0) {
                                    views.setImageViewResource(R.id.image_weather, iconResId);
                                } else {
                                    views.setImageViewResource(R.id.image_weather, R.drawable.ic_default_weather);
                                }


                                manager.updateAppWidget(widgetId, views);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }).start();
                    } else {
                        views.setTextViewText(R.id.text_city, "Location not available");
                        views.setTextViewText(R.id.text_temp, "");
                        views.setInt(R.id.widget_layout, "setBackgroundResource", R.drawable.bg_default);
                        manager.updateAppWidget(widgetId, views);
                    }
                })
                .addOnFailureListener(e -> {
                    views.setTextViewText(R.id.text_city, "Failed to get location");
                    views.setTextViewText(R.id.text_temp, "");
                    views.setInt(R.id.widget_layout, "setBackgroundResource", R.drawable.bg_default);
                    manager.updateAppWidget(widgetId, views);
                });
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (UPDATE_ACTION.equals(intent.getAction())) {
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            ComponentName widget = new ComponentName(context, WeatherWidget.class);
            int[] ids = manager.getAppWidgetIds(widget);
            for (int id : ids) {
                updateWidget(context, manager, id);
            }
        }
    }

    @Override
    public void onEnabled(Context context) {
        scheduleNextUpdate(context);
    }

    @Override
    public void onDisabled(Context context) {
        cancelUpdates(context);
    }

    private void scheduleNextUpdate(Context context) {
        Intent intent = new Intent(context, WeatherWidget.class);
        intent.setAction(UPDATE_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long interval = 10 * 1000;

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, interval, pendingIntent);
    }

    private void cancelUpdates(Context context) {
        Intent intent = new Intent(context, WeatherWidget.class);
        intent.setAction(UPDATE_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
