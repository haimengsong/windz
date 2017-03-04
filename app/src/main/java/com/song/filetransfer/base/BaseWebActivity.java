package com.song.filetransfer.base;


import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.song.filetransfer.service.WebService;

public abstract class BaseWebActivity extends BaseActivity{
    private WebServiceConnection webServiceConnection;
    private WebService mService;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webServiceConnection = new WebServiceConnection();
        //when this activity is created, bind the service with connection
        bindService(new Intent(this, WebService.class), webServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //When this activity is killed unbind the connection with the service
        unbindService(webServiceConnection);
    }
    protected WebService getService(){
        return mService;
    }
    class WebServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((WebService.LocalBinder) service).getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService=null;
        }
    }
}
