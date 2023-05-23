package Devices;

import Devices.SmartDevice;
import Interfaces.HttpRequestCompleted;

public class DeviceList {
    private HttpRequestCompleted httpRequestCompleted = null;
    public SmartDevice shutter1;
    public SmartDevice myList[];
    public DeviceList(HttpRequestCompleted httpRequestCompleted)
    {
        this.httpRequestCompleted = httpRequestCompleted;
        shutter1 = new SmartDevice("192.168.16.176","", this.httpRequestCompleted);
        myList = new SmartDevice[] {shutter1};
    }
}
