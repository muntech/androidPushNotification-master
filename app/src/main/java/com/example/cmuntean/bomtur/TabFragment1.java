package com.example.cmuntean.bomtur;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class TabFragment1 extends Fragment {

    private PopupWindow popupWindow;
    private View popupView;
    public static String usernameResponse = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_1, container, false);

        showPopup(view);

        return view;
    }
    public void showPopup(View view){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        popupView = inflater.inflate(R.layout.login, null);
        popupWindow = new PopupWindow(popupView,
                ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        popupWindow.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);

        popupWindow.setFocusable(true);
        popupWindow.update();

        Button btn=(Button)popupView.findViewById(R.id.button_login);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                hidePopup();
            }
        });


    }
    public void hidePopup(){
        popupWindow.dismiss();

        EditText editText = (EditText)popupView.findViewById(R.id.editText_email);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://df-bomtur.enterprise.dreamfactory.com:443/api/v2/bomtur/_table/Driver/" + editText.getText().toString();
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        usernameResponse = response;
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
                params.put("X-DreamFactory-Api-Key", "760f7c2f81c40d37c5b619215e39a66e98c62b50024c146bc77a46e5e081236f");
                //params.put("Accept-Language", "fr");

                return params;
            }
        };
        queue.add(postRequest);

    }




}
