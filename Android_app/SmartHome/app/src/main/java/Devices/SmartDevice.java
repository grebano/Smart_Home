package Devices;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import Interfaces.HttpRequestCompleted;
import Miscellaneous.Constants;
import Network.HttpRequests;

public class SmartDevice implements HttpRequestCompleted {

    private final String TAG = "SmartDevice";
    private boolean status;
    private boolean isOnline = false;
    private String nearestRouterMac;
    private HttpRequests httpRequests = null;

    // costruttore del dispositivo, con associazione alla classe http request (in ingresso singolo ogg. interfaccia)
    public SmartDevice(String ipAddress, String nearestRouterMac, HttpRequestCompleted httpRequestCompleted)
    {
        this.nearestRouterMac = nearestRouterMac;
        ArrayList<HttpRequestCompleted> completedArrayList = new ArrayList<>();
        completedArrayList.add(this);
        completedArrayList.add(httpRequestCompleted);
        this.httpRequests = new HttpRequests(Constants.HTTP  + ipAddress, completedArrayList);
        httpRequests.Request(Constants.PING_PATH);
    }

    // costruttore del dispositivo, con associazione alla classe http request (in ingresso lista di ogg. interfaccia)
    public SmartDevice(String ipAddress, String nearestRouterMac, ArrayList<HttpRequestCompleted> httpRequestsCompleted)
    {
        this.nearestRouterMac = nearestRouterMac;
        httpRequestsCompleted.add(this);
        this.httpRequests = new HttpRequests(Constants.HTTP + ipAddress, httpRequestsCompleted);
        httpRequests.Request(Constants.PING_PATH);
    }


    // Settaggio a On/Off del dispositivo tramite http
    public void setStatus(boolean status)
    {
        if(status)
            httpRequests.Request(Constants.ON_PATH);
        else
            httpRequests.Request(Constants.OFF_PATH);
    }

    // get dello stato del dispositivo
    public boolean getLocalStatus()
    {
        return this.status;
    }

    public void getHttpStatus()
    {
        httpRequests.Request(Constants.PING_PATH);
    }


    // get del router più vicino
    public String getNearestRouterMac() {
        return this.nearestRouterMac;
    }

    // Si aggiorna lo stato del dispositivo dopo una richiesta http
    @Override
    public void onHttpRequestCompleted(String response) {
        if(response != null && !response.equals("")) {
            this.isOnline = true;
            if (response.equals(Constants.ON_RESPONSE)) {
                this.status = true;
                return;
            }
            if (response.equals(Constants.OFF_RESPONSE)) {
                this.status = false;
                return;
            }
        }
        this.isOnline = false;
        Log.e(TAG,"Empty http Result");
    }

    public boolean checkIfOnline()
    {
        return this.isOnline;
    }
}
