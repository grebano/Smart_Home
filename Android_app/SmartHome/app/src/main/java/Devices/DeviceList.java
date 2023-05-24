package Devices;

import Devices.SmartDevice;
import Interfaces.HttpRequestCompleted;
import Miscellaneous.Constants;
import Miscellaneous.IpAddr_MacAddr;

public class DeviceList {
    private SmartDevice shutter1;
    private SmartDevice shutterList[];

    private SmartDevice lamp1;
    private SmartDevice lightsList[];
    public DeviceList(HttpRequestCompleted httpRequestCompleted)
    {
        //--------------------Shutters-----------------------------------
        shutter1 = new SmartDevice(IpAddr_MacAddr.SHUTTER1_IP, IpAddr_MacAddr.SHUTTER1_MAC, httpRequestCompleted);
        shutterList = new SmartDevice[] {shutter1};

        //--------------------Lamps--------------------------------------
//        lamp1 = new SmartDevice(IpAddr_MacAddr.LAMP1_IP, IpAddr_MacAddr.LAMP1_MAC, httpRequestCompleted);
        lightsList = new SmartDevice[] {};
    }
    public SmartDevice[] getShutterList() {
        return shutterList;
    }
    public SmartDevice[] getLightsList() {
        return lightsList;
    }
}
