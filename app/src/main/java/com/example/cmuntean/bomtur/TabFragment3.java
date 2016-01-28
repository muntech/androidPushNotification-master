package com.example.cmuntean.bomtur;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;

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

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabFragment3 extends Fragment {

    public static String userToken = "";
    public static String userEmail = "";
    public static String userResponse = "";
    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tab_fragment_3, container, false);

        Button btn=(Button)view.findViewById(R.id.buttonLogin);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //getUserData(view);
                getUserToken(view);

            }
        });

        return view;
    }

    public void getUserToken(View view){
        final EditText editText_username = (EditText)view.findViewById(R.id.username_name_edittext);
        final EditText editText_password = (EditText)view.findViewById(R.id.password_name_edittext);

        RequestQueue queue2 = Volley.newRequestQueue(getContext());
        String url = "http://bomtur.northeurope.cloudapp.azure.com:80/rest/user/session";

        JSONObject js2 = new JSONObject();

        try {
            js2.put("email", editText_username.getText().toString());
            js2.put("password", editText_password.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest userTokenRequest = new JsonObjectRequest(Request.Method.POST, url,js2,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            userToken = response.getString("session_id");
                            userEmail = response.getString("email");
                            Log.d("UserToken Response", "OK => " + userToken);

                            getUserData();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                //params.put("X-DreamFactory-Session-Token", "fr");

                return params;
            }
            /*@Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", editText_username.getText().toString());
                params.put("password", editText_password.getText().toString());
                return params;
            }*/
        };

        int socketTimeout =10000;//5 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        userTokenRequest.setRetryPolicy(policy);

        queue2.add(userTokenRequest);

    }

    public void navigateToFragment(Fragment fragment){

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.pager, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        mainActivity.viewPager.setCurrentItem(0);
    }

    private void getUserData(){

        final RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://bomtur.northeurope.cloudapp.azure.com:80/rest/bomtur/Driver?ids="+ userEmail + "&related=Passings_by_DriverID";
        final StringRequest userInfoRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        userResponse = response;
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

                return params;
            }
            /*@Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("ids", userEmail);
                //params.put("related", "Passings_by_DriverID");
                return params;
            }*/
        };

        int socketTimeout =15000;//5 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        userInfoRequest.setRetryPolicy(policy);

        queue.add(userInfoRequest);

        TabFragment1 tabFragment1 = new TabFragment1();
        navigateToFragment(tabFragment1);
    }
}