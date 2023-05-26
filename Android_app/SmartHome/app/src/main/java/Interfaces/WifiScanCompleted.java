package Interfaces;

import java.util.ArrayList;
import Network.Net;

public interface WifiScanCompleted {
    public void onWifiScanCompleted (ArrayList<Net> networks);
}
