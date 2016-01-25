package com.example.cmuntean.bomtur;

import android.util.Base64;

import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import java.io.UnsupportedEncodingException;

public class Application extends android.app.Application {
    String str;
    @Override
    public void onCreate() {
        super.onCreate();

        //This will only be called once in your app's entire lifecycle.
        Parse.initialize(this,
                getResources().getString(R.string.parse_application_id),
                getResources().getString(R.string.parse_client_key));
        ParseInstallation.getCurrentInstallation().getInstallationId();

        //Gets the ObjectID in Android -> save to User in Database
        String pInstall = ParseInstallation.getCurrentInstallation().getObjectId();

    }
}
