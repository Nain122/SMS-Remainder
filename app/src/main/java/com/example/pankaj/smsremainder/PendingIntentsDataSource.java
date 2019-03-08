package com.example.pankaj.smsremainder;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PendingIntentsDataSource {


    private SQLiteDatabase database;
    private PendingIntentSQLiteHelper dbHelper;
    private String[] allColumns = { PendingIntentSQLiteHelper.COLUMN_ID,PendingIntentSQLiteHelper.COLUMN_HOUR, PendingIntentSQLiteHelper.COLUMN_MINUTES, PendingIntentSQLiteHelper.COLUMN_SECONDS, PendingIntentSQLiteHelper.COLUMN_YEAR, PendingIntentSQLiteHelper.COLUMN_MONTH, PendingIntentSQLiteHelper.COLUMN_DAY, PendingIntentSQLiteHelper.COLUMN_FREQUENCY, PendingIntentSQLiteHelper.COLUMN_NUMBERTOSEND, PendingIntentSQLiteHelper.COLUMN_RECEIVERNAME, PendingIntentSQLiteHelper.COLUMN_MESSAGE };

    public PendingIntentsDataSource(Context context){
        dbHelper = new PendingIntentSQLiteHelper(context, "pendingintents.db",null, 2);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public SMSSchedulerPendingIntent createPendingIntents(int id, int hour, int mins, int secs, int year, int month, int day, String frequency, String number, String name, String message ){
        ContentValues values = new ContentValues();
        values.put(PendingIntentSQLiteHelper.COLUMN_ID, id);
        values.put(PendingIntentSQLiteHelper.COLUMN_HOUR, hour);
        values.put(PendingIntentSQLiteHelper.COLUMN_MINUTES, mins);
        values.put(PendingIntentSQLiteHelper.COLUMN_SECONDS, secs);
        values.put(PendingIntentSQLiteHelper.COLUMN_YEAR, year);
        values.put(PendingIntentSQLiteHelper.COLUMN_MONTH, month);
        values.put(PendingIntentSQLiteHelper.COLUMN_DAY, day);
        values.put(PendingIntentSQLiteHelper.COLUMN_FREQUENCY, frequency);
        values.put(PendingIntentSQLiteHelper.COLUMN_NUMBERTOSEND, number);
        values.put(PendingIntentSQLiteHelper.COLUMN_RECEIVERNAME, name);
        values.put(PendingIntentSQLiteHelper.COLUMN_MESSAGE, message);

        database.insert(PendingIntentSQLiteHelper.TABLE_PENDINGINTENT, null,
                values);

        Cursor cursor = database.query(PendingIntentSQLiteHelper.TABLE_PENDINGINTENT,
                allColumns, PendingIntentSQLiteHelper.COLUMN_ID + " = " + id, null,
                null, null, null);

        cursor.moveToFirst();
        SMSSchedulerPendingIntent newPendingIntent = cursorToPendingIntent(cursor);
        cursor.close();
        return newPendingIntent;
    }

    public List<SMSSchedulerPendingIntent> getAllPendingIntents() {
        List<SMSSchedulerPendingIntent> pendingIntents = new ArrayList<SMSSchedulerPendingIntent>();
        Cursor cursor = database.query(PendingIntentSQLiteHelper.TABLE_PENDINGINTENT,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            SMSSchedulerPendingIntent pendingIntent = cursorToPendingIntent(cursor);
            pendingIntents.add(pendingIntent);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return pendingIntents;
    }

    private SMSSchedulerPendingIntent cursorToPendingIntent(Cursor cursor) {
        SMSSchedulerPendingIntent pendingIntent = new SMSSchedulerPendingIntent();
        pendingIntent.setId(cursor.getLong(0));
        pendingIntent.setHour(cursor.getInt(1));
        pendingIntent.setMinutes(cursor.getInt(2));
        pendingIntent.setSeconds(cursor.getInt(3));
        pendingIntent.setYear(cursor.getInt(4));
        pendingIntent.setMonth(cursor.getInt(5));
        pendingIntent.setDay(cursor.getInt(6));
        pendingIntent.setFrequency(cursor.getString(7));
        pendingIntent.setNumberToSend(cursor.getString(8));
        pendingIntent.setReceiverName(cursor.getString(9));
        pendingIntent.setMessage(cursor.getString(10));

        return pendingIntent;
    }

    public boolean deletePendingIntent(int id)
    {

        return database.delete(PendingIntentSQLiteHelper.TABLE_PENDINGINTENT, PendingIntentSQLiteHelper.COLUMN_ID + "=" + id, null) > 0;
    }
}
