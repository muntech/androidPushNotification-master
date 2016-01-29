package com.example.cmuntean.bomtur;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;


public class TabFragment1 extends Fragment {

    private TabFragment3 tabFragment3;
    private MainActivity mainActivity;
    private boolean contentLoaded;
    private static List<Passing> userPassingsList;
    private View view1;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view1 = inflater.inflate(R.layout.tab_fragment_1, container, false);

        progressBar = (ProgressBar) view1.findViewById(R.id.progressBar_frag1);
        if (tabFragment3.userResponse != ""){
            progressBar.setVisibility(View.VISIBLE);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                }
            }, 2500);
        }

        return view1;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            loadView(view1);
        }
        else {  }
    }

    private void loadView(final View view){

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (tabFragment3.userResponse != "") {
                            if (!contentLoaded) {
                                try {
                                    addViewItem(view, mainActivity.getAppContext());
                                    contentLoaded = true;
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }, 1800);
    }

    private void addViewItem(View view, Context context) throws JSONException {

        userPassingsList = new ArrayList<Passing>();
        addPassingToList(jsonPassingsArray(), userPassingsList);
        final LinearLayout linearLayout = (LinearLayout) view1.findViewById(R.id.frag1);

        for (int i = 0; i < userPassingsList.size(); i++){

            final Passing passing = userPassingsList.get(i);

            final RelativeLayout relativeLayout = new RelativeLayout(context);
            relativeLayout.setLayoutParams
                    (new ViewGroup.MarginLayoutParams
                            (ViewGroup.LayoutParams.MATCH_PARENT, 500));


            final TextView tollboothTextView = new TextView(context);
            tollboothTextView.setText(passing.getTolbooth());
            tollboothTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            tollboothTextView.setWidth(1000);
            tollboothTextView.setHeight(300);
            tollboothTextView.setTextSize(28);
            tollboothTextView.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
            tollboothTextView.setId(R.id.tollboothTextView);

            final TextView priceTextView = new TextView(context);
            priceTextView.setText(passing.getPrice() + " kr");
            priceTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            priceTextView.setWidth(550);
            priceTextView.setHeight(200);
            priceTextView.setTextSize(35);
            priceTextView.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
            priceTextView.setId(R.id.priceTextView);

            SimpleDateFormat timeStampFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            String DateStr = timeStampFormat.format(passing.getTimeStamp());
            DateStr = DateStr.replace("-", ".");

            final TextView timeStampTextView = new TextView(context);
            timeStampTextView.setText(DateStr);
            timeStampTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            timeStampTextView.setWidth(800);
            timeStampTextView.setHeight(90);
            timeStampTextView.setTextSize(16);
            timeStampTextView.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));


            RelativeLayout.LayoutParams paramsTollbooth = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
            paramsTollbooth.setMargins(0, 35, 0, 0);
            tollboothTextView.setLayoutParams(paramsTollbooth);

            RelativeLayout.LayoutParams paramsPrice = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
            paramsPrice.addRule(RelativeLayout.RIGHT_OF, R.id.tollboothTextView);
            paramsPrice.setMargins(0,35,0,0);
            priceTextView.setLayoutParams(paramsPrice);

            RelativeLayout.LayoutParams paramsTimeStamp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            //paramsTimeStamp.addRule(RelativeLayout.BELOW, R.id.tollboothTextView);
            paramsTimeStamp.setMargins(0, 290, 0, 0);
            timeStampTextView.setLayoutParams(paramsTimeStamp);

            relativeLayout.addView(tollboothTextView);
            relativeLayout.addView(priceTextView);
            relativeLayout.addView(timeStampTextView);

            final RelativeLayout line = new RelativeLayout(context);
            line.setLayoutParams
                    (new ViewGroup.MarginLayoutParams
                            (ViewGroup.LayoutParams.MATCH_PARENT, 5));
            line.setBackgroundColor(getResources().getColor(R.color.colorGray));

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    linearLayout.addView(relativeLayout);
                    linearLayout.addView(line);
                }
            }, 300);
        }
        progressBar.setVisibility(View.GONE);
    }

    private JSONArray jsonPassingsArray() throws JSONException {
        JSONObject jsonObject = new JSONObject(tabFragment3.userResponse);
        JSONArray records = jsonObject.getJSONArray("record");
        Log.d("A LENGTH", records.length() + "");
        JSONObject passingsObject = new JSONObject(records.getString(0));
        JSONArray passingsArray = passingsObject.getJSONArray("Passings_by_DriverID");
        Log.d("Passings_by_DriverID", passingsArray.getString(0));

        return passingsArray;
    }

    private void addPassingToList(JSONArray jsonArray, List<Passing> passingList) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject passingObject = new JSONObject(jsonArray.getString(i));

            Passing passing = new Passing();
            passing.setTolbooth(passingObject.getString("Tolbooth"));
            passing.setPrice(passingObject.getString("Price"));
            passing.setEmail(passingObject.getString("DriverID"));

            DateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
            format.setTimeZone(TimeZone.getTimeZone("CET"));
            try {
                String time = passingObject.getString("TimeStamp");
                Date passingDate = format.parse(time);
                passing.setTimeStamp(passingDate);
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
            passingList.add(passing);
            Log.d("PassingsList:", userPassingsList.toString());
        }
    }

    public void navigateToFragment(Fragment fragment){

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.pager, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        mainActivity.viewPager.setCurrentItem(2);
    }
}
