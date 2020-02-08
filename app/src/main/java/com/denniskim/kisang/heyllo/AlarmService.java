package com.denniskim.kisang.heyllo;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;

public class AlarmService extends IntentService {
    private NotificationManager alarmNotificationManager;
    String channelName = "Background Service";
    public AlarmService() {
        super("AlarmService");
    }
   @Override
    public void onCreate() {
        super.onCreate();
        Log.i("myservice","service has started");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
         startMyOwnForeground();
        }
        else {
            startForeground(1, new Notification());
        }
    }
//Notification channel
    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
        NotificationChannel chan = new NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);
    }
//runs in the main thread
    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        super .onStartCommand(intent, flags, startId);
        return START_REDELIVER_INTENT;
    }
    @Override
    public void onHandleIntent(Intent intent) {
        String reminderTitle = intent.getStringExtra(Constants.ALARM_EXTRA);
        sendNotification(reminderTitle);
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = Build.VERSION.SDK_INT >= 20 ? pm.isInteractive() : pm.isScreenOn(); // check if screen is on
        if (!isScreenOn) {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "myApp:notificationLock");
            wl.acquire(3000); //set your time in milliseconds
        }
    }
    private void sendNotification(String msg) {
        Log.d("AlarmService", "Preparing to send notification...: " + msg);
        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

         NotificationCompat.Builder alarmNotificationBuilder = new NotificationCompat.Builder(this,Constants.NOTIFICATION_CHANNEL_ID)
                 .setContentTitle("Reminder")
                 .setSmallIcon(R.drawable.heyllo_notification_icon)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(msg))
                .setContentText(msg)
                 .setCategory(NotificationCompat.CATEGORY_REMINDER)
                 .setFullScreenIntent(contentIntent,true)
                 .setContentIntent(contentIntent);
        alarmNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(1, alarmNotificationBuilder.build());
        Log.d("AlarmService", "Notification sent.");
    }
}
