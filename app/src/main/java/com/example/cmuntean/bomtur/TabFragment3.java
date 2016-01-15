package com.example.cmuntean.bomtur;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TabFragment3 extends Fragment {

    private List<Person> personList = new ArrayList<Person>();
    String json;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_3, container, false);

        final EditText editText_Name = (EditText) view.findViewById(R.id.edittext_name);
        final EditText editText_Regnr = (EditText) view.findViewById(R.id.edittext_regnr);
        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.timePicker);
        MainActivity mainActivity = new MainActivity();
        final Tollbooth toolbooth = new Tollbooth();

        Button registrer = (Button) view.findViewById(R.id.register_btn);
        registrer.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                registeredPassing(editText_Name.getText().toString(), toolbooth, editText_Regnr.getText().toString(), new Date(datePicker.getYear() - 1900, datePicker.getMonth(), datePicker.getDayOfMonth()));
                json = new Gson().toJson(personList);
                System.out.println(json);
            }
        });

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.settingsRadiogrp);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch (checkedId) {
                    case R.id.radio_moss:
                        toolbooth.setLat(32.11);
                        toolbooth.setLng(43.54);
                        break;
                    case R.id.radio_oslo:
                        toolbooth.setLat(22.20);
                        toolbooth.setLng(11.76);
                        break;
                    case R.id.radio_drammen:
                        toolbooth.setLat(45.61);
                        toolbooth.setLng(32.84);
                        break;
                }
            }
        });

        return view;
    }

    private void registeredPassing(String name, Tollbooth tollbooth, String regnr, Date date){
        Person person = new Person();
        person.setName(name);
        person.setTollbooth(tollbooth);
        person.setRegnr(regnr);
        person.setTimeStamp(date);

        personList.add(person);
    }
}