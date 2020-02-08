package com.denniskim.kisang.heyllo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

import static java.lang.Math.random;

public class CreateReminder extends AppCompatActivity {
HeylloDatabaseHelper heylloDatabaseHelper;
SQLiteDatabase db;
private EditText reminder;
private EditText description;
    private static CreateReminder inst;
    private Button buttonToSetTime;
    private int mHour,mMinute;
    Alarm alarm;
    public static CreateReminder instance() {
        return inst;
    }
    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reminder);
        reminder = (EditText) findViewById(R.id.title);
        description = (EditText) findViewById(R.id.description);
        buttonToSetTime = (Button) findViewById(R.id.settime);
        alarm = new Alarm();
        heylloDatabaseHelper = new HeylloDatabaseHelper(this);
        buttonToSetTime.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //Get current time
               final Calendar c = Calendar.getInstance();
               mHour = c.get(Calendar.HOUR_OF_DAY);
               mMinute = c.get(Calendar.MINUTE);

               //Launch timepicker dialog
               TimePickerDialog timePickerDialog = new TimePickerDialog(CreateReminder.this , new TimePickerDialog.OnTimeSetListener() {
                   @Override
                   public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                       Log.i("hourofday",String.valueOf(hourOfDay));
                       Log.i("minute",String.valueOf(minute));
                       alarm.setStringNotation(hourOfDay,minute);
                       String stringNotation = alarm.getStringNotation();
                       Log.i("stringNotation",stringNotation);
                       alarm.setAlarmTime(stringNotation);
                       //get  edit text contents
                       String reminderTitle = reminder.getText().toString();
                       String descriptionContent = description.getText().toString();
                       //get requestcode
                       alarm.scheduleAlarm(getApplicationContext(),reminderTitle);
                       int requestcode = alarm.getRequestCode();
                       Log.i("xoxo",String.valueOf(requestcode));
                       heylloDatabaseHelper.insertAssests(reminderTitle,stringNotation,descriptionContent,"Delete",requestcode);
                       Intent intent = new Intent(CreateReminder.this,MainActivity.class);
                       startActivity(intent);
                   }
               },mHour,mMinute,true);
               timePickerDialog.show();
           }
       });
    }
//check if the service is running
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("status", "Running");
                return true;
            }
        }
        Log.i ("status", "Not running");
        return false;
    }
}
