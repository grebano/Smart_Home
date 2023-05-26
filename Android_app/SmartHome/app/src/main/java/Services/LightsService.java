package Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.maiot.smarthome.R;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import Devices.DeviceList;
import Devices.SmartDevice;
import Interfaces.WifiScanCompleted;
import Miscellaneous.Constants;
import Network.Net;
import Wifi.WifiReceiver;


public class LightsService extends Service implements WifiScanCompleted {

    private final String TAG = "MyLightsService";
    private final String NOTIFICATION_CHANNEL_ID = "MyLightsService_ID";
    public static boolean isRunning = false;

    private WifiReceiver wifiReceiver = null;
    private WifiManager wifiManager = null;

    private DeviceList deviceList = null;

    private Timer timer;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"Service - OnCreate");

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiReceiver = new WifiReceiver(wifiManager, this);

        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        //quando la scansione è completata viene lanciato l'intento a cui agganciamo il BR

        deviceList = new DeviceList(null);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"Service - onStartCommand");

        isRunning = true;

        timer = new Timer();
        startCheckingTime();

        addNotification();
        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiReceiver);
        isRunning = false;
    }


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

    // si ricercano i router più vicini al telefono
    @Override
    public void onWifiScanCompleted(ArrayList<Net> networks) {
        if(networks != null) {
            for (Net net : networks) {
                if(net != null) {
                    if(deviceList.getLightsList() != null) {
                        for (SmartDevice smartDevice : deviceList.getLightsList()) {
                            if(smartDevice != null) {
                                if (Objects.equals(smartDevice.getNearestRouterMac(), net.getBssid())) {
                                    if (net.getLevel() > Constants.WIFI_NEAR_THRESHOLD && !smartDevice.getLocalStatus()) {
                                        smartDevice.setStatus(true);
                                    }
                                    else if (net.getLevel() < Constants.WIFI_FAR_THRESHOLD && smartDevice.getLocalStatus()) {
                                        smartDevice.setStatus(false);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return;
        }
        Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
    }

    private void startCheckingTime() {
        // si setta l'azione che il timer deve schedulare
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                wifiManager.startScan();
            }
        };
        // si schedula un'azione ogni 3s senza delay iniziale
        timer.scheduleAtFixedRate(timerTask,Constants.LIGHTS_TIMER_DELAY_IN_MILLIS,Constants.LIGHTS_TIMER_PERIOD_IN_MILLIS);
    }
}
