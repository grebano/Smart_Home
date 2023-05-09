package Wifi;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Interfaces.WifiScanCompleted;

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

        //conto le reti in 5GHz e in 2.4GHz
        int fiveG = 0;
        int twoG = 0;
        for(int i=0; i<wifiScan.size();i++){
            if(wifiScan.get(i).frequency < 3500){
                twoG ++;
            }
            else{
                fiveG ++;
            }
        }

        String res = "";
        res += "<b>Number of Networks: " + wifiScan.size() + "</b><br><br>";
        res += "<b>   5GHz Networks count: </b>"+ fiveG +"<br>";
        res += "<b> 2.4GHz Networks count:  </b>"+ twoG +"<br><br>";

        for(int i = 0; i < wifiScan.size(); i++){
            res += (i+1) + "<b>) SSID: </b>" + wifiScan.get(i).SSID + "<br>";
            res += "<b> MAC: </b>" + wifiScan.get(i).BSSID + "<br>";
            res += "<b> Frequency: </b>" + wifiScan.get(i).frequency + " MHz <br>";
            res += "<b> Capabilities: </b>" + wifiScan.get(i).capabilities + "<br>";
            res += "<b> RSSI: </b>" + wifiScan.get(i).level + " dBm<br><br>";
            //RSSI-RSS è la potenza del segnale ricevuto
        }
        Log.i(TAG,"res");
        wifiScanCompleted.onWifiScanCompleted(res);
    }
}
