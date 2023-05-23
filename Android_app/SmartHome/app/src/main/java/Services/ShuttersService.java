package Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.maiot.smarthome.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import Devices.DeviceList;
import Interfaces.HttpRequestCompleted;
import Miscellaneous.Constants;


public class ShuttersService extends Service {

    private final String TAG = "MyShuttersService";
    private final String NOTIFICATION_CHANNEL_ID = "MyShuttersService_ID";
    public static boolean isRunning = false;

    private String currentTime;
    private Timer timer;

    private DeviceList deviceList = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //on Bind per questo tipo di servizio non ci interessa


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"Service - OnCreate");
        // non voglio aggiornamenti dopo la richiesta http
        deviceList = new DeviceList(null);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"Service - onStartCommand");
        isRunning = true;

        addNotification();

        timer = new Timer();
        startCheckingTime();

        return START_STICKY;
        //parametro per decidere come si comporta il sistema
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"Service - onDestroy");
        isRunning = false;
    }

    private void startCheckingTime() {
        // si setta l'azione che il timer deve schedulare
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                currentTime = new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());
                if (Integer.parseInt(currentTime)>= Constants.NIGHT_BEGINNING_TIME &&
                        Integer.parseInt(currentTime) <= Constants.MORNING_BEGINNING_TIME)
                {
                    Log.i(TAG,"close shutters");
                    deviceList.getShutterList()[0].setStatus(false);
                    // close shutters
                }
                else
                {
                    Log.i(TAG,"open shutters");
                    deviceList.getShutterList()[0].setStatus(true);
                    // open shutters
                }
            }
        };
        // si schedula un'azione ogni 2 min senza delay iniziale
        timer.scheduleAtFixedRate(timerTask,Constants.SHUTTER_TIMER_DELAY_IN_MILLIS,Constants.SHUTTER_TIMER_PERIOD_IN_MILLIS);
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
                .setSmallIcon(R.drawable.ic_launcher_foreground);

        startForeground(1001,notification.build());
    }

}
