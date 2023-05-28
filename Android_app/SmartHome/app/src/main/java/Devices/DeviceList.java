package Devices;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;

import Devices.SmartDevice;
import Interfaces.HttpRequestCompleted;
import Miscellaneous.Constants;
import Miscellaneous.IpAddr_MacAddr;

public class DeviceList {
    private final String TAG = "DeviceList";
    private SmartDevice shutter1;
    private ArrayList<SmartDevice> shutterList;

    private SmartDevice lamp1;
    private ArrayList<SmartDevice> lightsList;
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
    public ArrayList<SmartDevice> getShutterList() {
        return shutterList;
    }
    public ArrayList<SmartDevice> getLightsList() {
        return lightsList;
    }

    private void checkOnlineLamps(){

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                for(SmartDevice smartDevice : lightsList)
                {
                    smartDevice.getHttpStatus();
                }
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


    private void checkOnlineShutters(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                for(SmartDevice smartDevice : shutterList)
                {
                    smartDevice.getHttpStatus();
                }
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