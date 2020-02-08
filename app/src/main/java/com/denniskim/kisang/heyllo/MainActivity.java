package com.denniskim.kisang.heyllo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView showReminders;
    HeylloDatabaseHelper heylloDatabaseHelper;
    ArrayList<ModelReminder> reminders = new ArrayList<>();
    Cursor data;
    ModelReminder modelReminder;
    ReminderAdapter reminderAdapter;
    CoordinatorLayout coordinatorLayout;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        recyclerView = (RecyclerView) findViewById(R.id.rvReminders);
        reminders = new ArrayList<>();
        //extracting data from sql database
        heylloDatabaseHelper = new HeylloDatabaseHelper(this);
        data = heylloDatabaseHelper.getData();
        while (data.moveToNext()) {
            modelReminder = new ModelReminder(data.getString(0)
                    ,data.getString(1)
                    ,data.getString(2)
                    ,data.getInt(3)
            ,data.getString(4));
            //Add to arraylist
            reminders.add(modelReminder);
        }
        reminderAdapter =  new ReminderAdapter(reminders,getApplicationContext());
        recyclerView.setAdapter(reminderAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(MainActivity.this,CreateReminder.class);
               startActivity(intent);
            }
        });
        enableSwipeToDeleteAndUndo();
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
*/
  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    @Override
    protected void onRestart() {
        super.onRestart();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvReminders);
        reminders = new ArrayList<>();
        //extracting data from sql database
        heylloDatabaseHelper = new HeylloDatabaseHelper(this);
        data = heylloDatabaseHelper.getData();
        while (data.moveToNext()) {
            modelReminder = new ModelReminder(data.getString(0),
                    data.getString(1),
                    data.getString(2),
                    data.getInt(3),
                    data.getString(4));
            //Add to arraylist
            reminders.add(modelReminder);
        }
        reminderAdapter =  new ReminderAdapter(reminders,getApplicationContext());
        recyclerView.setAdapter(reminderAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        }

    @Override
    protected void onStop() {
        super.onStop();
        //clear arraylist
        reminders.clear();
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDelete swipeToDeleteCallback = new SwipeToDelete(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                reminderAdapter.removeItem(position);
                Toast.makeText(getApplicationContext(),"Reminder has been deleted",Toast.LENGTH_SHORT).show();
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

}
