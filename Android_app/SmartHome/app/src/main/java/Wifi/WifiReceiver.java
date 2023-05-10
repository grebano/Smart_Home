package Wifi;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Interfaces.WifiScanCompleted;
import Miscellaneous.Constants;

public class WifiReceiver extends BroadcastReceiver {

    private final String TAG = "WifiReceiver";
    private WifiManager wifiManager = null;

    private WifiScanCompleted wifiScanCompleted = null;
    //oggetto dell'interfaccia che usiamo per restituire la stringa alla main activity
    public WifiReceiver(WifiManager wifiManager, WifiScanCompleted wifiScanCompleted)
    {
        this.wifiManager = wifiManager;
        this.wifiScanCompleted = wifiScanCompleted;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"onReceive");

        @SuppressLint("MissingPermission")
        List<ScanResult> wifiScan = wifiManager.getScanResults();

        //riordino le reti in base alla potenza del segnale
        Comparator<ScanResult> comparator =  (ScanResult a, ScanResult b) -> {
            if (a.level > b.level){
                return -1;
            }
            else{
                return 1;
            }
        };
        Collections.sort(wifiScan, comparator);

        // lista di stringhe che ritorno come output
        ArrayList<String[]> nets = null;

        // aggiungo alla lista il Mac e la potenza dei router
        for(int i = 0; i < wifiScan.size(); i++){
            if(wifiScan.get(i).SSID == Constants.SSID) {
                String lev = "" + wifiScan.get(i).level;
                String[] net = new String[]{wifiScan.get(i).BSSID, lev};
                nets.add(net);
            }
        }
        Log.i(TAG,"res");
        wifiScanCompleted.onWifiScanCompleted(nets);
    }
}
