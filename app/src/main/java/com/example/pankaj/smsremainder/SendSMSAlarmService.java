package com.example.pankaj.smsremainder;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

 public class SendSMSAlarmService extends Service {
    private String mNumberToSend;
    private String mSMSMessage;
    private String mFrequency;
    private int mId;
    private CancelAnAlarmActivity mCancelAnAlarmActivity;
    private PendingIntentsDataSource mDatasource;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mCancelAnAlarmActivity = CancelAnAlarmActivity.getCancelAlarmActivity();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub

        return null;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mDatasource.close();
    }

    @Override
    public int onStartCommand(Intent intent, int startId, int arg) {
        // TODO Auto-generated method stub
        super.onStartCommand(intent, startId, arg);

        // Bundle bundle = intent.getExtras();
        mNumberToSend = intent.getStringExtra("com.somitsolutions.android.smsscheduler.number");

        mSMSMessage = intent.getStringExtra("com.somitsolutions.android.smsscheduler.message");
        mFrequency = intent.getStringExtra("com.somitsolutions.android.smsscheduler.frequency");
        mId = intent.getIntExtra("com.somitsolutions.android.smsscheduler.id", 0);
        SmsManager sms = SmsManager.getDefault();

        if(!mSMSMessage.equals(null)){

            ArrayList<String> msgStringArray = sms.divideMessage(mSMSMessage);
            sms.sendMultipartTextMessage(mNumberToSend, null, msgStringArray, null, null);
        }

        if(mFrequency.equalsIgnoreCase("Once")){
            mDatasource = new PendingIntentsDataSource(getApplicationContext());
            mDatasource.open();
            mDatasource.deletePendingIntent(mId);
            //mDatasource.close();*/
            //mCancelAnAlarmActivity.DeleteAnForOnceAlarmEntryFromDatabase(mId);
        }

        return START_STICKY_COMPATIBILITY;

        //sms.sendTextMessage(mNumberToSend, null, mSMSMessage, null, null);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub

        return super.onUnbind(intent);

    }

}
