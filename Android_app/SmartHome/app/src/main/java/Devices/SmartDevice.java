package Devices;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Interfaces.HttpRequestCompleted;
import Network.HttpRequests;

public class SmartDevice implements HttpRequestCompleted {
    private final String TAG = "SmartDevice";
    private boolean status;
    private String ipAddress;
    private String nearestRouterMac;
    private HttpRequests httpRequests = null;

    public SmartDevice(String ipAddress, String nearestRouterMac)
    {
        this.ipAddress = ipAddress;
        this.nearestRouterMac = nearestRouterMac;
        this.httpRequests = new HttpRequests("http://" + this.ipAddress, this);

    }

    public void setStatus(boolean status)
    {
        if(status)
            httpRequests.Request("/on");
        else
            httpRequests.Request("/off");
       httpRequests.Request("/ping");
    }

    public boolean getStatus()
    {
        return this.status;
    }

    @Override
    public void onHttpRequestCompleted(String response) {
        if(response != null) {
            if (response.equals("on")) {
                this.status = true;
            }
            if (response.equals("off")) {
                this.status = false;
            } else {
                Log.i(TAG, "set command performed");
            }
        }
        else
        {
            Log.e(TAG,"Empty http Result");
        }

    }
}
