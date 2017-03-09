package com.song.filetransfer.base;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.song.filetransfer.service.WebService;

public abstract class BaseActivity extends AppCompatActivity{

    private static final String TAG = "BaseActivity";
    private MyBroadcastReceiver mReceiver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doRegisterReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mReceiver!=null){
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        }
    }

    //register broadcast receiver
    private void doRegisterReceiver(){
        mReceiver = new MyBroadcastReceiver();
        IntentFilter mIntentFiler = new IntentFilter();
        addFiltersToIntentFilter(mIntentFiler);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,mIntentFiler);
    }
    //subclasses add filters into the IntentFilter
    protected abstract void addFiltersToIntentFilter(IntentFilter mIntentFilter);

    //subclasses handle intents from other components
    protected abstract void onReceiveIntent(Context context, Intent intent);


    class MyBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG,"received");
            onReceiveIntent(context,intent);
        }
    }
}
