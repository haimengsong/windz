package com.song.filetransfer.service;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class WebService extends Service {
    public final static String TAG = "WebService";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public final class LocalBinder extends Binder {
        public WebService getService(){
            return WebService.this;
        }
    }

    public void processMessages(Intent intent){
        String what = intent.getAction();
        Log.d(TAG,"get message: "+ what);
        if(what.equals("ACCEPT")){
            Intent mIntent = new Intent("com.song.transfer.ACTION_DISPLAY_USER_IN");
            mIntent.putExtra("VALUE","HAIMENG");
            LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent);
            Log.d(TAG,"sendback message: "+ what);
        }

    }
}
