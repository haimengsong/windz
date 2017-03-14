package com.song.filetransfer.service;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.song.filetransfer.application.MyApplication;
import com.song.filetransfer.helper.TcpHelper;
import com.song.filetransfer.helper.UdpHelper;
import com.song.filetransfer.utilities.NetUtil;

import java.net.SocketException;
import java.net.UnknownHostException;

public class WebService extends Service {
    public final static String TAG = "WebService";

    public final static int ONLINE = 0x00000000;

    public final static int OFFLINE = 0x00000001;

    private TcpHelper tcphelper;

    private UdpHelper udpHelper;

    private MyApplication myApplication;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = (MyApplication) getApplicationContext();
        udpHelper = new UdpHelper(this);
        tcphelper = new TcpHelper(this);
    }

    public void perform(int what, Object obj){
        switch (what){
            case ONLINE:
                Log.i(TAG,"ask udpHelper to broadcast online message in LAN");
                udpHelper.broadcastOnline();
                break;
            case OFFLINE:
                Log.i(TAG,"ask udpHelper to broadcast offline message in LAN");
                udpHelper.broadcastOffline();
                break;
        }

    }
    private void sentBroadcast(Intent intent){
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    public final class LocalBinder extends Binder {
        public WebService getService(){
            return WebService.this;
        }
    }
}
