package Interfaces;

import java.util.ArrayList;
import Network.Net;

/**
 * Interfaccia che rappresenta la callback per una scansione wifi
 */
public interface WifiScanCompleted {
    void onWifiScanCompleted (ArrayList<Net> networks);
}
