package com.denniskim.kisang.heyllo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Broadcast-Listened", "Broadcast Started");
        Intent serviceIntent = new Intent(context,AlarmService.class);

        Bundle bundle = intent.getExtras();
        final Alarm alarm = bundle.getParcelable(Constants.ALARM_EXTRA);
        Log.i("parcelAlarm",String.valueOf(alarm));
        String reminderTitle = intent.getStringExtra(Constants.ALARM_EXTRA);
        Log.i("medennis",reminderTitle);
        serviceIntent.putExtra(Constants.ALARM_EXTRA,reminderTitle);
        context.startService(serviceIntent);
    }
}
