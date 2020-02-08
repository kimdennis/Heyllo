package com.denniskim.kisang.heyllo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

public class Alarm implements Parcelable {
    private String stringNotation;
    private Boolean alarmActive = true;
    private Day[] days = { Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY, Day.THURSDAY, Day.FRIDAY, Day.SATURDAY, Day.SUNDAY };
    private String alarmTonePath = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString();
    private Boolean vibrate = true;
    private String alarmName = "AlarmClock";
    private int ID,requestCode;
    private Calendar alarmTime;

    public enum Day {
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY;

        @Override
        public String toString() {
            switch (this.ordinal()) {
                case 0:
                    return "Sunday";
                case 1:
                    return "Monday";
                case 2:
                    return "Tuesday";
                case 3:
                    return "Wednesday";
                case 4:
                    return "Thursday";
                case 5:
                    return "Friday";
                case 6:
                    return "Saturday";
            }
            return super.toString();
        }
    }

    //constructor
    public void Alarm(String stringNotation,String alarmTonePath,String alarmName,int id){
      this.stringNotation = stringNotation;
      this.alarmTonePath = alarmTonePath;
      this.alarmName = alarmName;
      this.ID = id;
    }
    //Parcelling part
    public Alarm(Parcel in){
        this.stringNotation = in.readString();
        this.alarmTonePath = in.readString();
        this.alarmName = in.readString();
        this.ID = in.readInt();
    }

    //Implemented the Creator method myself
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Alarm>(){
        //returns in form of object or array
        @Override
        public Alarm createFromParcel(Parcel in) {
            return new Alarm(in);
        }

        @Override
        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.stringNotation);
        dest.writeString(this.alarmTonePath);
        dest.writeString(this.alarmName);
        dest.writeInt(this.ID);
    }

    @Override
    public int describeContents() {
        return 0;
    }
//Empty constructor.
    public Alarm() {
        alarmTime = Calendar.getInstance();
        alarmTime.setTimeInMillis(alarmTime.getTimeInMillis() + 5000);
        setStringNotation(alarmTime.get(Calendar.HOUR_OF_DAY), alarmTime.get(Calendar.MINUTE));
    }
    public void setStringNotation(int hour,int minutes){
        StringBuilder stringBuilder = new StringBuilder();
        if (hour / 9 == 0 || hour == 9) {
            stringBuilder.append("0");
        }
        stringBuilder.append(hour).append(":");
        if (minutes / 9 == 0 || minutes == 9) {
            stringBuilder.append("0");
        }
        stringBuilder.append(minutes);
        this.stringNotation = stringBuilder.toString();
    }
    public void setAlarmTime(String alarmTime) {

        this.stringNotation = alarmTime;
        String[] timePieces = alarmTime.split(":");

        Calendar newAlarmTime = Calendar.getInstance();
        newAlarmTime.set(Calendar.HOUR_OF_DAY,
                Integer.parseInt(timePieces[0]));
        newAlarmTime.set(Calendar.MINUTE, Integer.parseInt(timePieces[1]));
        newAlarmTime.set(Calendar.SECOND, 0);
        setAlarmTime(newAlarmTime);
    }
    public void setAlarmTime(Calendar alarmTime) {
        this.alarmTime = alarmTime;
    }
    public Calendar getAlarmTime() {
        if (alarmTime.before(Calendar.getInstance())) {
            alarmTime.add(Calendar.DAY_OF_MONTH, 1);
        }
        while (!Arrays.asList(getDays()).contains(Day.values()[alarmTime.get(Calendar.DAY_OF_WEEK) - 1])) {
            alarmTime.add(Calendar.DAY_OF_MONTH, 1);
        }
        return alarmTime;
    }
    public Day[] getDays() {
        return days;
    }

    private void setAlarmTonePath(String alarmTonePath){
        this.alarmTonePath = alarmTonePath;
    }
    private void setID(int id){
        this.ID = id;
    }
    private void setAlarmName(String alarmName){
        this.alarmName = alarmName;
    }
    public void setDays(Day[] days){
        this.days = days;
    }
    public void setVibrate(Boolean vibrate) {
        this.vibrate = vibrate;
    }
    public void setAlarmActive(Boolean alarmActive) {
        this.alarmActive = alarmActive;
    }
    public String getStringNotation(){
        return stringNotation;
    }
    public String getAlarmTonePath(){
        return alarmTonePath;
    }
    public int getID(){
        return ID;
    }
    public Boolean isAlarmActive() {
        return alarmActive;
    }
    public String getAlarmName(){
        return alarmName;
    }
    public String getTimeUntilNextAlarmMessage() {
        long timeDifference = getAlarmTime().getTimeInMillis() - System.currentTimeMillis();
        long days = timeDifference / (1000 * 60 * 60 * 24);
        long hours = timeDifference / (1000 * 60 * 60) - (days * 24);
        long minutes = timeDifference / (1000 * 60) - (days * 24 * 60) - (hours * 60);
        long seconds = timeDifference / (1000) - (days * 24 * 60 * 60) - (hours * 60 * 60) - (minutes * 60);
        String alert = "Reminder in ";
        if (days > 0) {
            alert += String.format(
                    "%d days, %d hours, %d minutes and %d seconds", days,
                    hours,  minutes, seconds);
        } else {
            if (hours > 0) {
                alert += String.format("%d hours, %d minutes and %d seconds",
                        hours, minutes, seconds);
            } else {
                if (minutes > 0) {
                    alert += String.format("%d minutes, %d seconds", minutes,
                            seconds);
                } else {
                    alert += String.format("%d seconds", seconds);
                }
            }
        }
        return alert;
     }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void scheduleAlarm(Context context,String whatToDo) {
        Random random = new Random();
        int code = random.nextInt(10000);
        setRequestCode(code);
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        myIntent.putExtra(Constants.ALARM_EXTRA,whatToDo);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,getRequestCode(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,getAlarmTime().getTimeInMillis(), pendingIntent);
        Toast.makeText(context,getTimeUntilNextAlarmMessage(),Toast.LENGTH_LONG).show();
        Log.i("getAlarmTime",String.valueOf(getAlarmTime().getTimeInMillis()));
        Log.i("getTimeUntilNext",getTimeUntilNextAlarmMessage());
        Log.i("requestCode",String.valueOf(getRequestCode()));
    }
}
