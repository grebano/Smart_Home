package Interfaces;

import java.util.ArrayList;
import java.util.List;

public interface WifiScanCompleted {
    public void onWifiScanCompleted (ArrayList<String[]> networks);
}
