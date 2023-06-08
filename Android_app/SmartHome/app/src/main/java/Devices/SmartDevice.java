package Devices;

import android.util.Log;

import java.util.ArrayList;

import Interfaces.HttpRequestCompleted;
import Miscellaneous.Constants;
import Network.HttpRequests;

/**
 * Classe che rappresenta un dispositivo smart
 */

public class SmartDevice implements HttpRequestCompleted {

    private final String TAG = "SmartDevice";
    private boolean status;
    private boolean isOnline = false;
    private final String nearestRouterMac;
    private final HttpRequests httpRequests;

    /**
     * Costruttore del dispositivo, con associazione alla classe http request (in ingresso ogg. interfaccia)
     * @param ipAddress indirizzo ip del dispositivo
     * @param nearestRouterMac indirizzo mac del router più vicino
     * @param httpRequestCompleted interfaccia per la gestione della risposta http
     */
    public SmartDevice(String ipAddress, String nearestRouterMac, HttpRequestCompleted httpRequestCompleted)
    {
        this.nearestRouterMac = nearestRouterMac;
        ArrayList<HttpRequestCompleted> completedArrayList = new ArrayList<>();
        completedArrayList.add(this);
        completedArrayList.add(httpRequestCompleted);
        this.httpRequests = new HttpRequests(Constants.HTTP  + ipAddress, completedArrayList);
        httpRequests.Request(Constants.PING_PATH);
    }


    /**
     * Costruttore del dispositivo, con associazione alla classe http request (in ingresso ogg. interfaccia)
     * @param ipAddress indirizzo ip del dispositivo
     * @param nearestRouterMac indirizzo mac del router più vicino
     * @param httpRequestsCompleted interfaccia per la gestione della risposta http
     */
    public SmartDevice(String ipAddress, String nearestRouterMac, ArrayList<HttpRequestCompleted> httpRequestsCompleted)
    {
        this.nearestRouterMac = nearestRouterMac;
        httpRequestsCompleted.add(this);
        this.httpRequests = new HttpRequests(Constants.HTTP + ipAddress, httpRequestsCompleted);
        httpRequests.Request(Constants.PING_PATH);
    }


    /**
     * funzione che setterà lo stato del dispositivo
     * @param status boolean che rappresenta lo stato del dispositivo
     */
    public void setStatus(boolean status)
    {
        if(status)
            httpRequests.Request(Constants.ON_PATH);
        else
            httpRequests.Request(Constants.OFF_PATH);
    }

    /**
     * funzione che restituisce lo stato del dispositivo
     * @return boolean
     */
    public boolean getLocalStatus()
    {
        return this.status;
    }

    /**
     * funzione che esegue una richiesta http per ottenere lo stato del dispositivo
     */
    public void getHttpStatus()
    {
        httpRequests.Request(Constants.PING_PATH);
    }


    /**
     * funzione che restituisce l'indirizzo mac del router più vicino
     * @return boolean
     */
    public String getNearestRouterMac() {
        return this.nearestRouterMac;
    }

    /**
     * funzione da implementare per la gestione della risposta http
     * @param response risposta http
     */
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
