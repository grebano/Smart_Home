package Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.maiot.smarthome.R;

import java.util.ArrayList;

import Devices.DeviceList;
import Devices.SmartDevice;
import Interfaces.WifiScanCompleted;
import Miscellaneous.Constants;
import Network.Net;
import Wifi.WifiReceiver;


/**
 * Classe che rappresenta il servizio che controlla lo stato delle luci
 */
public class LightsService extends Service implements WifiScanCompleted {

    private final String TAG = "MyLightsService";
    private final String NOTIFICATION_CHANNEL_ID = "MyLightsService_ID";
    public static boolean isRunning = false;

    private WifiReceiver wifiReceiver = null;
    private WifiManager wifiManager = null;

    private DeviceList deviceList = null;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * Metodo che viene chiamato quando il servizio viene creato
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"Service - OnCreate");

        // inizializzazione del wifi manager e del wifi receiver
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiReceiver = new WifiReceiver(wifiManager, this);

        // registrazione del wifi receiver per ricevere gli intent
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        // inizializzazione della lista dei dispositivi
        deviceList = new DeviceList(null);
    }

    /**
     * Metodo che viene chiamato quando il servizio viene avviato
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"Service - onStartCommand");

        // imposto il servizio come running
        isRunning = true;

        // avvio la scansione del wifi
        wifiManager.startScan();

        // notifico l'utente che il servizio è attivo
        addNotification();
        return START_STICKY;

    }


    /**
     * Metodo che viene chiamato quando il servizio viene distrutto
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"Service - onDestroy");

        // annullo la registrazione del wifi receiver
        unregisterReceiver(wifiReceiver);

        // imposto il servizio come non running
        isRunning = false;
    }


    /**
     * funzione che notifica l'utente che il servizio è attivo
     */
    private void addNotification(){
        NotificationChannel channel;
        channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_ID, NotificationManager.IMPORTANCE_LOW);
        getSystemService(NotificationManager.class).createNotificationChannel(channel);

        Notification.Builder notification = null;
        notification = new Notification.Builder(
                this, NOTIFICATION_CHANNEL_ID)
                .setContentText("MyLightsService is running")
                .setContentTitle("Service is running")
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground);

        startForeground(1002,notification.build());
    }

    /**
     * funzione che viene chiamata quando la scansione del wifi è completata
     * @param networks
     */
    @Override
    public void onWifiScanCompleted(ArrayList<Net> networks) {
        // controllo se la lista dei dispositivi è vuota
        if(networks != null) {
            for (Net net : networks) {
                if(net != null) {
                    // controllo se la rete è presente nella lista dei dispositivi
                    if(deviceList.getLightsList() != null) {
                        for (SmartDevice smartDevice : deviceList.getLightsList()) {
                            if(smartDevice != null) {
                                if (smartDevice.getNearestRouterMac().equals(net.getBssid())) {
                                    Log.i(TAG,"Found a device for mac : " + net.getBssid());
                                    if (net.getLevel() > Constants.WIFI_NEAR_THRESHOLD && !smartDevice.getLocalStatus()) {
                                        // accendo la luce
                                        smartDevice.setStatus(true);
                                    }
                                    else if (net.getLevel() < Constants.WIFI_FAR_THRESHOLD && smartDevice.getLocalStatus()) {
                                        // spengo la luce
                                        smartDevice.setStatus(false);
                                    }
                                }
                                else
                                    Log.i(TAG,"No device found for mac : " + net.getBssid());
                            }
                        }
                    }
                }
            }
            delayedScan();
            return;
        }
        delayedScan();
        Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
    }

    /**
     * funzione che ritarda la scansione del wifi
     */
    private void delayedScan() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                wifiManager.startScan();
            }

        }, Constants.LIGHTS_DELAY_IN_MILLIS);
    }
}
