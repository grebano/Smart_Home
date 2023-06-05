package Devices;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;

import Devices.SmartDevice;
import Interfaces.HttpRequestCompleted;
import Miscellaneous.Constants;
import Miscellaneous.IpAddr_MacAddr;

/**
 * Classe che contiene la lista dei dispositivi
 */
public class DeviceList {
    private final String TAG = "DeviceList";
    private SmartDevice shutter1;
    private ArrayList<SmartDevice> shutterList;

    private SmartDevice lamp1;
    private ArrayList<SmartDevice> lightsList;

    /**
     * Costruttore della classe DeviceList
     * @param httpRequestCompleted
     */
    public DeviceList(HttpRequestCompleted httpRequestCompleted)
    {
        //--------------------Shutters-----------------------------------
        shutter1 = new SmartDevice(IpAddr_MacAddr.SHUTTER1_IP, IpAddr_MacAddr.SHUTTER1_MAC, httpRequestCompleted);
        shutterList = new ArrayList<>();
        checkOnlineShutters();
        //--------------------Lamps--------------------------------------
        lamp1 = new SmartDevice(IpAddr_MacAddr.LAMP1_IP, IpAddr_MacAddr.LAMP1_MAC, httpRequestCompleted);
        lightsList = new ArrayList<>();
        lightsList.add(lamp1);
        checkOnlineLamps();

    }

    /**
     * funzione che restituisce la lista delle tapparelle
     * @return ArrayList<SmartDevice>
     */
    public ArrayList<SmartDevice> getShutterList() {
        return shutterList;
    }
    /**
     * funzione che restituisce la lista delle lampade
     * @return ArrayList<SmartDevice>
     */
    public ArrayList<SmartDevice> getLightsList() {
        return lightsList;
    }


    /**
     * funzione che controlla se le lampade sono online
     */
    private void checkOnlineLamps(){

        Handler handler = new Handler();
        for(SmartDevice smartDevice : lightsList)
        {
            smartDevice.getHttpStatus();
        }
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                for(SmartDevice smartDevice : lightsList)
                {
                    if(!smartDevice.checkIfOnline())
                    {
                        lightsList.remove(smartDevice);
                        Log.i(TAG,"Lamp removed -> not online");
                    }
                }
            }
        }, Constants.DEVICE_ONLINE_DELAY);
    }

    /**
     * funzione che controlla se le tapparelle sono online
     */
    private void checkOnlineShutters(){

        Handler handler = new Handler();
        for(SmartDevice smartDevice : shutterList)
        {
            smartDevice.getHttpStatus();
        }
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                for(SmartDevice smartDevice : shutterList)
                {
                    if(!smartDevice.checkIfOnline())
                    {
                        shutterList.remove(smartDevice);
                        Log.i(TAG,"Shutter removed -> not online");
                    }
                }
            }
        }, Constants.DEVICE_ONLINE_DELAY);
    }
}