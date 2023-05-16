package Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import Miscellaneous.Constants;


public class Shutters extends Service {

    private final String TAG = "MyShuttersService";
    public static boolean isRunning = false;

    private String currentTime;
    private Timer timer;

    //private MusicPlayer musicPlayer

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //on Bind per questo tipo di servizio non ci interessa è un retaggio


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"Service - OnCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"Service - onStartCommand");
        isRunning = true;

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

        // lo si mette a null se lo riutilizzassimo
        timer = null;
        timer = new Timer();
        // si setta l'azione che il timer deve schedulare
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                currentTime = new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());
                if (Integer.parseInt(currentTime)>= Constants.NIGHT_BEGINNING_TIME)
                {
                    // close shutters
                }
                else if (Integer.parseInt(currentTime.split(",")[0])>= Constants.MORNING_BEGINNING_TIME)
                {
                    // open shutters
                }
            }
        };
        // si schedula un'azione ogni 2sec senza delay iniziale
        timer.scheduleAtFixedRate(timerTask,Constants.TIMER_DELAY_IN_MILLIS,Constants.TIMER_PERIOD_IN_MILLIS);
    }
}
