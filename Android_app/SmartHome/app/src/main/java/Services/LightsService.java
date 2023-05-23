package Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.maiot.smarthome.R;

import java.util.ArrayList;

import Devices.DeviceList;
import Interfaces.WifiScanCompleted;
import Wifi.WifiReceiver;


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

    @Override
    public void onCreate() {
        super.onCreate();

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiReceiver = new WifiReceiver(wifiManager, this);

        deviceList = new DeviceList(null);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        wifiManager.startScan();
        isRunning = true;
        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }


    private void addNotification(){
        NotificationChannel channel = null;
        channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_ID, NotificationManager.IMPORTANCE_LOW);
        getSystemService(NotificationManager.class).createNotificationChannel(channel);

        Notification.Builder notification = null;
        notification = new Notification.Builder(
                this, NOTIFICATION_CHANNEL_ID)
                .setContentText("MyShuttersService is running")
                .setContentTitle("Service is running")
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_background);

        startForeground(1002,notification.build());
    }

    // si ricercano i router più vicini al telefono
    @Override
    public void onWifiScanCompleted(ArrayList<String[]> networks) {
        for(int i = 0; i < networks.size(); i++)
        {
            if(Integer.parseInt(networks.get(i)[1]) > -50)
            {
                for(int j = 0; j < deviceList.getLightsList().length; j++)
                {
                    // accensione luci nelle prossimità
                    if(deviceList.getLightsList()[i].getNearestRouterMac() == networks.get(i)[0])
                    {
                        deviceList.getLightsList()[i].setStatus(true);
                    }
                    // spegnimento delle altre
                    else
                    {
                        deviceList.getLightsList()[i].setStatus(false);
                    }
                }
            }
        }
        wifiManager.startScan();
    }
}