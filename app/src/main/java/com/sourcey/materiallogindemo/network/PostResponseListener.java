package com.sourcey.materiallogindemo.network;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by geojose1990 on 16/02/16.
 * Interface for sending server response back to UI
 * Used in NetworkManager class to send data back to CustomActionBar activity
 */
public interface PostResponseListener {

    void onRequestStarted();

    void onPOSTRequestCompleted(String result);

    void onPOSTRequestEndedWithError(VolleyError error);

    void onRequestTimeOutError();

}
