package com.sourcey.materiallogindemo.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by geojose1990 on 28/12/15.
 * Singleton class to manage network connection
 */
public class NetworkManager {
    private static NetworkManager instance = null;

    //for Volley API
    public RequestQueue requestQueue;

    private NetworkManager(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized NetworkManager getInstance(Context context) {
        if (null == instance)
            instance = new NetworkManager(context);
        return instance;
    }

    //this is so you don't need to pass context each time
    public static synchronized NetworkManager getInstance() {
        if (null == instance) {
            throw new IllegalStateException(NetworkManager.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    /**
     * This function will execute network request and send the result back to UI
     * method is either GET or POST
     * url is the url to call
     * jsonString is the data to send as string. This will convert into Json object
     * listener is the instance of 'PostResponseListener' interface
     **/
    public void makeGETRequest(final String url, final String username, final String password, final PostResponseListener listener) {
        // callback for starting request
        listener.onRequestStarted();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Send the response back to UI
                        listener.onPOSTRequestCompleted(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Network Error", error.toString());
                // Send error back to the UI if error happens
                if (error instanceof TimeoutError) {
                    listener.onRequestTimeOutError();
                } else {
                    listener.onPOSTRequestEndedWithError(error);
                }
            }
        });
        stringRequest.setTag("volleyRequest");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, 0, 0));
        requestQueue.add(stringRequest);
    }


    public void makePOSTRequest(final String url, final String username, final String password, final PostResponseListener listener) {
        // callback for starting request
        listener.onRequestStarted();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Send the response back to UI
                        listener.onPOSTRequestCompleted(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Network Error", error.toString());
                        // Send error back to the UI if error happens
                        if (error instanceof TimeoutError) {
                            listener.onRequestTimeOutError();
                        } else {
                            listener.onPOSTRequestEndedWithError(error);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", username);
                params.put("pswd", password);
                return params;
            }

        };
        stringRequest.setTag("volleyRequest");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000, 0, 0));
        requestQueue.add(stringRequest);
    }

    /**
     * Function to cancel all the ongoing network requests
     **/
    public void cancelNetworkRequests() {
        if (requestQueue != null) {
            requestQueue.cancelAll("volleyRequest");
        }
    }
}