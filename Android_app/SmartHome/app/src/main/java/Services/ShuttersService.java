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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import Devices.DeviceList;
import Miscellaneous.Constants;

/**
 * Classe che rappresenta il servizio che controlla lo stato delle tapparelle
 */

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


    /**
     * Metodo che viene chiamato quando il servizio viene creato
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"Service - OnCreate");

        // creazione di un oggetto di tipo DeviceList
        deviceList = new DeviceList(null);
    }

    /**
     * Metodo che viene chiamato quando il servizio viene avviato
     * @param intent
     * @param flags
     * @param startId
     * @return START_STICKY
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"Service - onStartCommand");
        isRunning = true;

        // si notifica che il servizio è in esecuzione
        addNotification();

        // creazione di un oggetto di tipo Timer
        timer = new Timer();

        // si avvia il controllo delle tapparelle
        startCheckingTime();

        return START_STICKY;
        //parametro per decidere come si comporta il sistema
    }


    /**
     * Metodo che viene chiamato quando il servizio viene distrutto
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"Service - onDestroy");

        // si ferma il timer
        timer.cancel();

        // si imposta il servizio come non in esecuzione
        isRunning = false;
    }


    /**
     * Metodo che avvia il controllo delle tapparelle
     */
    private void startCheckingTime() {
        // si setta l'azione che il timer deve schedulare
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                currentTime = new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());
                if(deviceList.getShutterList() != null) {
                    // se l'ora corrente è compresa nell'intervallo specificato
                    if (Integer.parseInt(currentTime) >= Constants.NIGHT_BEGINNING_TIME ||
                            Integer.parseInt(currentTime) <= Constants.MORNING_BEGINNING_TIME) {
                        Log.i(TAG, "close shutters");
                        // chiusura tapparelle
                        deviceList.getShutterList().get(0).setStatus(false);
                    } else {
                        Log.i(TAG, "open shutters");
                        // apertura tapparelle
                        deviceList.getShutterList().get(0).setStatus(true);
                    }
                }
                else
                {
                    Log.e(TAG, getResources().getString(R.string.NULL_OBJECT));
                }
            }
        };
        // si schedula un'azione ogni x min senza delay iniziale
        timer.scheduleAtFixedRate(timerTask,Constants.SHUTTER_TIMER_DELAY_IN_MILLIS,Constants.SHUTTER_TIMER_PERIOD_IN_MILLIS);
    }

    /**
     * Metodo che crea una notifica per notificare che il servizio è in esecuzione
     */
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
