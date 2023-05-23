package Devices;

import Devices.SmartDevice;
import Interfaces.HttpRequestCompleted;

public class DeviceList {
    private SmartDevice shutter1;
    private SmartDevice shutterList[];
    private SmartDevice lightsList[];
    public DeviceList(HttpRequestCompleted httpRequestCompleted)
    {
        //--------------------Shutters-----------------------------------
        shutter1 = new SmartDevice("192.168.16.176","", httpRequestCompleted);
        shutterList = new SmartDevice[] {shutter1};

        //--------------------Lamps--------------------------------------
        lightsList = new SmartDevice[] {};
    }
    public SmartDevice[] getShutterList() {
        return shutterList;
    }
    public SmartDevice[] getLightsList() {
        return lightsList;
    }
}
