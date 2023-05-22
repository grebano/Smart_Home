package Devices;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ShutterDevice {
    private boolean status;
    private String ipAddress;
    private String nearestRouterMac;

    public ShutterDevice(boolean status, String ipAddress, String nearestRouterMac)
    {
        this.status = status;
        this.ipAddress = ipAddress;
        this.nearestRouterMac = nearestRouterMac;
    }

    public void setShutterStatus(boolean status)
    {
        setStatus(status, this.status, "http://" + this.ipAddress);
        this.status = status;
    }

    public boolean getStatus()
    {
        return this.status;
    }

    private void setStatus(boolean status, boolean actualState, String baseUrl)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //doInBackground
                URL url = null;
                if(status != actualState) {
                    if (status) {
                        try {
                            url = new URL(baseUrl + "/on");
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            url = new URL(baseUrl + "/off");
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    HttpURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpURLConnection) url.openConnection();
                        try {
                            try {
                                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                                in.close();
                            } catch (IOException e) {
                                //throw new RuntimeException(e);
                            }
                        } finally {
                            urlConnection.disconnect();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    urlConnection.disconnect();
                }
            }
        }).start();
    }

}
