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
import Network.HttpRequests;

public class SmartDevice implements HttpRequestCompleted {

    private final String TAG = "SmartDevice";
    private boolean status;
    private String nearestRouterMac;
    private HttpRequests httpRequests = null;

    // costruttore del dispositivo, con associazione alla classe http request
    public SmartDevice(String ipAddress, String nearestRouterMac, HttpRequestCompleted httpRequestCompleted)
    {
        this.nearestRouterMac = nearestRouterMac;
        ArrayList<HttpRequestCompleted> completedArrayList = new ArrayList<>();
        completedArrayList.add(this);
        completedArrayList.add(httpRequestCompleted);
        this.httpRequests = new HttpRequests("http://" + ipAddress, completedArrayList);
        httpRequests.Request("/ping");
    }

    // Settaggio a On/Off del dispositivo tramite http
    public void setStatus(boolean status)
    {
        if(status)
            httpRequests.Request("/on");
        else
            httpRequests.Request("/off");
    }

    // get dello stato del dispositivo
    public boolean getStatus()
    {
        return this.status;
    }

    // get del router più vicino
    public String getNearestRouterMac() {
        return nearestRouterMac;
    }

    // Si aggiorna lo stato del dispositivo dopo una richiesta http
    @Override
    public void onHttpRequestCompleted(String response) {
        if(response != null) {
            if (response.equals("on")) {
                this.status = true;
            }
            if (response.equals("off")) {
                this.status = false;
            }
        }
        else
        {
            Log.e(TAG,"Empty http Result");
        }
    }
}
