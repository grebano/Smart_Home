package Devices;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import Interfaces.HttpRequestCompleted;
import Miscellaneous.Constants;
import Miscellaneous.IpAddr_MacAddr;

/**
 * Classe che contiene la lista dei dispositivi
 */
public class DeviceList {
    private final String TAG = "DeviceList";
    private final ArrayList<SmartDevice> shutterList;

    private final ArrayList<SmartDevice> lightsList;

    /**
     * Costruttore della classe DeviceList
     * @param httpRequestCompleted interfaccia per la gestione della risposta http
     */
    public DeviceList(HttpRequestCompleted httpRequestCompleted)
    {
        //--------------------Shutters-----------------------------------
        SmartDevice shutter1 = new SmartDevice(IpAddr_MacAddr.SHUTTER1_IP, IpAddr_MacAddr.SHUTTER1_MAC, httpRequestCompleted);
        shutterList = new ArrayList<>();
        checkOnlineShutters();
        //--------------------Lamps--------------------------------------
        SmartDevice lamp1 = new SmartDevice(IpAddr_MacAddr.LAMP1_IP, IpAddr_MacAddr.LAMP1_MAC, httpRequestCompleted);
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
        handler.postDelayed(() -> {
            for(SmartDevice smartDevice : lightsList)
            {
                if(!smartDevice.checkIfOnline())
                {
                    lightsList.remove(smartDevice);
                    Log.i(TAG,"Lamp removed -> not online");
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
        handler.postDelayed(() -> {
            for(SmartDevice smartDevice : shutterList)
            {
                if(smartDevice.checkIfOnline())
                {
                    shutterList.remove(smartDevice);
                    Log.i(TAG,"Shutter removed -> not online");
                }
            }
        }, Constants.DEVICE_ONLINE_DELAY);
    }

    /**
     * Metodo che controlla il numero di lampade disponibili
     * @param context contesto
     * @param withToast booleano che indica se visualizzare o meno il toast
     * @return count
     */
    public int checkLightsCount(Context context, boolean withToast)
    {
        if(lightsList != null) {
            int count = lightsList.size();
            if(withToast) {
                if (count == 0) {
                    // visualizzazione a schermo
                    Toast.makeText(context, "there are no available lamps", Toast.LENGTH_SHORT).show();
                }
            }
            return count;
        }
        return 0;
    }

    /**
     * Metodo che controlla se ci sono tapparelle disponibili
     * @param withToast booleano che indica se visualizzare o meno il toast
     * @return count
     */
    public int checkShutterCount(Context context, boolean withToast)
    {
        if(shutterList != null) {
            int count = shutterList.size();
            if(withToast) {
                if (count == 0) {
                    // visualizzazione a schermo
                    Toast.makeText(context, "there are no available shutters", Toast.LENGTH_SHORT).show();
                }
            }
            return count;
        }
        return 0;
    }
}