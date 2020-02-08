package com.denniskim.kisang.heyllo;

public class ModelReminder {
private String reminderName,reminderTime,reminderButtonText,description;
private int requestCode,id;
public ModelReminder(String reminderName,String reminderTime,String reminderButtonText,int requestCode,String description){
    this.id = id;
    this.reminderName = reminderName;
    this.reminderTime = reminderTime;
    this.reminderButtonText = reminderButtonText;
    this.requestCode = requestCode;
    this.description = description;
}
    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    public void setReminderButtonText(String reminderButtonText) {
        this.reminderButtonText = reminderButtonText;
    }

    public void setReminderName(String reminderName) {
        this.reminderName = reminderName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReminderButtonText() {
        return reminderButtonText;
    }

    public String getReminderName() {
        return reminderName;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
