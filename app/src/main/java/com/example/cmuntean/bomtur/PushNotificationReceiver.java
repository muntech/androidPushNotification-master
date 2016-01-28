package com.example.cmuntean.bomtur;

import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.parse.ParsePushBroadcastReceiver;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PushNotificationReceiver extends ParsePushBroadcastReceiver implements  LocationListener{
    private final String TAG = "PUSH_NOTIF";
    public String currentLat = "";
    public String currentLng = "";
    public static Date currentDate = null;
    private TabFragment3 tabFragment3;
    private String username;
    private Location mLastLocation;
    private LocationManager locationManager;
    // Google client to interact with Google API
    private static GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;
    private com.google.android.gms.location.LocationListener locationListener;
    private FusedLocationProviderApi fusedLocationProviderApi;
    private MainActivity mainActivity;
    private PowerManager pm;
    private PowerManager.WakeLock wakeLock;

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

        pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock((pm.SCREEN_BRIGHT_WAKE_LOCK | pm.FULL_WAKE_LOCK | pm.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();

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

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        getLocation(context, locationManager);

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

    private void getLocation(final Context context, final LocationManager mlocationManager){

        mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        mlocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

    }

    private void postLocation(Context context, String lat, String lng, String date, String driverID){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://bomtur.northeurope.cloudapp.azure.com:80/rest/bomtur/Driver/";
        JSONObject js = new JSONObject();

        try {
            js.put("id", driverID);
            js.put("Lat", lat);
            js.put("Lng", lng);
            js.put("TimeStamp",date);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PATCH, url,js,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR","error => "+error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("X-DreamFactory-Application-Name", "bomtur");
                //params.put("X-DreamFactory-Session-Token", tabFragment3.userToken);

                return params;
            }
            /*@Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ID", "test");
                params.put("Name", "test");
                return params;
            }*/
        };

        /*int socketTimeout =10000;//5 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);*/

        queue.add(jsonObjReq);
    }


    @Override
    public void onLocationChanged(Location location) {

        currentLat = location.getLatitude() + "";
        currentLng = location.getLongitude() + "";
        currentDate = new Date();
        username = tabFragment3.userEmail;

        postLocation(mainActivity.getAppContext(), currentLat, currentLng, currentDate.toString(), username);
        locationManager.removeUpdates(PushNotificationReceiver.this);
        wakeLock.release();
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
