package Wifi;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import Interfaces.WifiScanCompleted;
import Miscellaneous.Constants;
import Network.Net;

/**
 * Classe che rappresenta il receiver per la scansione delle reti wifi
 */
public class WifiReceiver extends BroadcastReceiver {

    private final String TAG = "WifiReceiver";
    private WifiManager wifiManager = null;

    // callback per la scansione delle reti wifi
    private WifiScanCompleted wifiScanCompleted = null;


    /**
     * Costruttore della classe
     * @param wifiManager
     * @param wifiScanCompleted
     */
    public WifiReceiver(WifiManager wifiManager, WifiScanCompleted wifiScanCompleted)
    {
        Log.i(TAG,"wifiReceiver");
        this.wifiManager = wifiManager;
        this.wifiScanCompleted = wifiScanCompleted;
    }

    /**
     * Metodo che viene chiamato quando il receiver riceve un intent
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"onReceive");

        @SuppressLint("MissingPermission")
        List<ScanResult> wifiScan = wifiManager.getScanResults();

        // riordino le reti in base alla potenza del segnale
        Comparator<ScanResult> comparator =  (ScanResult a, ScanResult b) -> {
            if (a.level > b.level){
                return -1;
            }
            else if (a.level < b.level){
                return 1;
            }
            else
                return 0;
        };
        wifiScan.sort(comparator);

        // lista di reti che ritorno come output
        ArrayList<Net> nets = new ArrayList<>();

        // aggiungo alla lista il Mac e la potenza dei router
        if(wifiScan != null) {
            for (int i = 0; i < wifiScan.size(); i++) {
                if (Objects.equals(wifiScan.get(i).SSID, Constants.SSID)) {
                    Net net = new Net(wifiScan.get(i).SSID, wifiScan.get(i).BSSID, wifiScan.get(i).level);
                    nets.add(net);
                    Log.i(TAG, "net: " + i + " " + net.getBssid() + " -> " + net.getLevel());
                }
            }
        }
        wifiScanCompleted.onWifiScanCompleted(nets);
    }
}
