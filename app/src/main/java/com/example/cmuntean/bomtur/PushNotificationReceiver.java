package com.example.cmuntean.bomtur;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import com.parse.ParsePushBroadcastReceiver;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;

public class PushNotificationReceiver extends ParsePushBroadcastReceiver implements LocationListener {
    private final String TAG = "PUSH_NOTIF";
    public static double currentLat = 0;
    public static double currentLng = 0;
    public static Date currentDate = null;

    @Override
    public void onPushOpen(Context context, Intent intent) {
        Log.i(TAG, "onPushOpen triggered!");
        Intent i = new Intent(context, MainActivity.class);
        i.putExtras(intent.getExtras());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    @Override
    public void onPushReceive(Context context, Intent intent) {
        Log.i(TAG, "onPushReceive triggered!");

        JSONObject pushData;
        String alert = null;
        String title = null;
        try {
            pushData = new JSONObject(intent.getStringExtra(PushNotificationReceiver.KEY_PUSH_DATA));
            alert = pushData.getString("alert");
            title = pushData.getString("title");
        } catch (JSONException e) {}


        Log.i(TAG,"alert is " + alert);
        Log.i(TAG,"title is " + title);

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        getLocation(locationManager);

        Intent cIntent = new Intent(PushNotificationReceiver.ACTION_PUSH_OPEN);
        cIntent.putExtras(intent.getExtras());
        cIntent.setPackage(context.getPackageName());

        // WE SHOULD HANDLE DELETE AS WELL
        // BUT IT'S NOT HERE TO SIMPLIFY THINGS!

        PendingIntent pContentIntent =
                PendingIntent.getBroadcast(context, 0 /*just for testing*/, cIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder
                .setSmallIcon(R.drawable.car190)
                .setContentTitle(alert)
                .setContentText(title)
                .setContentIntent(pContentIntent)
                .setAutoCancel(true);


        NotificationManager myNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        myNotificationManager.notify(1, builder.build());
    }

    private void getLocation(LocationManager locationManager){
        Criteria criteria = new Criteria();
        String provider = LocationManager.GPS_PROVIDER;
        provider = locationManager.getBestProvider(criteria, false);
        // Get last known location
        Location location = locationManager.getLastKnownLocation(provider);
        currentLat = location.getLatitude();
        currentLng = location.getLongitude();
        currentDate = new Date();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
