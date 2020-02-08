package com.denniskim.kisang.heyllo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HeylloDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME="HeylloDB";
    private static final int DB_VERSION=1;
    SQLiteDatabase db;
    HeylloDatabaseHelper(Context context){
    super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS CREDENTIALS");
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      databaseStructure(db,0,DB_VERSION);
    }
    //create database table
    private void databaseStructure(SQLiteDatabase db,int oldVersion,int newVersion){
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE CREDENTIALS (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "REMINDER TEXT, "
                    + "TIMETORING TEXT, "
                    + "DELETETEXT TEXT, "
                    + "REQUESTCODE INTEGER, "
                    + "DESCRIPTION TEXT);");
                          }
               }
    //insert assests
    public boolean insertAssests(String reminder,String timeToRing,String description,String deleteText,int requestCode){
        db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("REMINDER",reminder);
        contentValues.put("TIMETORING",timeToRing);
        contentValues.put("DESCRIPTION",description);
        contentValues.put("DELETETEXT",deleteText);
        contentValues.put("REQUESTCODE",requestCode);
        long returnId=db.insert("CREDENTIALS",null,contentValues);
        if(returnId == -1){
            Log.d("datasaved","failed to save");
            return false;
        }else{
            Log.d("datasaved","Sucessfully saved");
            return true;
        }
    }
    //delete assets
    public void deleteAsset(String argument){
        db=this.getReadableDatabase();
         db.delete("CREDENTIALS","REQUESTCODE=?",new String[]{argument});
    }
    //query Database
    public  Cursor getData(){
        db=this.getReadableDatabase();
        Cursor data=db.query("CREDENTIALS",
                new String[] {"REMINDER","TIMETORING","DELETETEXT","REQUESTCODE","DESCRIPTION"},
                null,
                null,
                null,
                null,
                null);
        return  data;
    }
}
