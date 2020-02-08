package com.denniskim.kisang.heyllo;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
public class ViewHolder extends RecyclerView.ViewHolder{
    public TextView reminderName,reminderTime,description;
    public Button deleteReminder;
    public ViewHolder(View viewItem){
        super(viewItem);
        reminderName = (TextView)viewItem.findViewById(R.id.reminder_name);
        reminderTime = (TextView)viewItem.findViewById(R.id.reminder_time);
        deleteReminder = (Button)viewItem.findViewById(R.id.delete_reminder);
        description = (TextView) viewItem.findViewById(R.id.about_field);
    }
}
public ArrayList<ModelReminder> reminders;
Context context;

    public ReminderAdapter(ArrayList<ModelReminder> reminders,Context context){
        this.reminders = reminders;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View reminderView = inflater.inflate(R.layout.reminderview,parent,false);
        ViewHolder viewHolder = new ViewHolder(reminderView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position){
        TextView textView = holder.reminderName;
        textView.setText(reminders.get(position).getReminderName());
        TextView textView1 = holder.reminderTime;
        textView1.setText(reminders.get(position).getReminderTime());
        TextView textView2 = holder.description;
        textView2.setText(reminders.get(position).getDescription());
        final Button button = holder.deleteReminder;
        button.setText(reminders.get(position).getReminderButtonText());
        final int requestCode = reminders.get(position).getRequestCode();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("requestCode",String.valueOf(requestCode));
                //cancel the pending intent and delete from database
                Intent myIntent = new Intent(button.getContext(),AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(button.getContext(),requestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                pendingIntent.cancel();
                Log.i("pendingIntent","Reminder has been cancelled");
                HeylloDatabaseHelper heylloDatabaseHelper = new HeylloDatabaseHelper(button.getContext());
                heylloDatabaseHelper.deleteAsset(String.valueOf(reminders.get(position).getRequestCode()));
                reminders.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,reminders.size());
                Toast.makeText(button.getContext(),"Reminder has been deleted",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return reminders.size();
    }
    public ArrayList<ModelReminder> getData() {
        return reminders;
    }
    public void removeItem(int position) {
        int requestCode = reminders.get(position).getRequestCode();
        Log.d("requestCode",String.valueOf(requestCode));
        //cancel the pending intent and delete from database
        Intent myIntent = new Intent(context,AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,requestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();
        Log.i("pendingIntent","Reminder has been cancelled");
        HeylloDatabaseHelper heylloDatabaseHelper = new HeylloDatabaseHelper(context);
        heylloDatabaseHelper.deleteAsset(String.valueOf(requestCode));
        reminders.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,reminders.size());
    }
}
 