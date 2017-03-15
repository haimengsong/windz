package com.song.filetransfer.service;


import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.song.filetransfer.application.MyApplication;
import com.song.filetransfer.helper.TcpHelper;
import com.song.filetransfer.helper.UdpHelper;
import com.song.filetransfer.model.Constants;
import com.song.filetransfer.model.UserModel;
import com.song.filetransfer.utilities.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class WebService extends Service {
    public final static String TAG = "WebService";

    public final static int ONLINE = 0x00000000;

    public final static int OFFLINE = 0x00000001;

    public final static int REQUESTCONNECTION = 0x00000002;

    public final static int REJECTCONNECTION = 0x00000003;

    private TcpHelper tcphelper;

    private UdpHelper udpHelper;

    private MyApplication myApplication;

    private Map<String, UserModel> onlineUserList;

    private Map<String, UserModel> connectedUserList;

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
        onlineUserList = new HashMap<>();
        connectedUserList = new HashMap<>();
    }

    public MyApplication getGlobal(){
        return myApplication;
    }

    public void handleMsgFromClients(int what, Object obj){
        switch (what){
            case ONLINE:
                Log.i(TAG,"ask udpHelper to broadcast online message in LAN");
                udpHelper.broadcastOnline();
                break;
            case OFFLINE:
                Log.i(TAG,"ask udpHelper to broadcast offline message in LAN");
                udpHelper.broadcastOffline();
                onlineUserList.clear();
                break;
        }

    }

    public void handleMsgFromUDP(String ip, String msgStr){
        try {
            JSONObject jsonObject = new JSONObject(msgStr);
            int info = jsonObject.getInt("info");
            Intent intent = null;
            Bundle bundle = null;
            switch (info){
                case ONLINE:
                    String name = jsonObject.getString("name");
                    String mac = jsonObject.getString("mac");
                    if(!onlineUserList.containsKey(ip)) onlineUserList.put(ip,new UserModel(name,mac,ip));
                    ///// just for testing
                    if(!connectedUserList.containsKey(ip)) connectedUserList.put(ip,new UserModel(name,mac,ip));
                    /////
                    intent = new Intent(Constants.ACTION_DISPLAY_USER_IN);
                    bundle = new Bundle();
                    bundle.putString("name",name);
                    bundle.putString("mac",mac);
                    bundle.putString("ip",ip);
                    intent.putExtras(bundle);
                    sendBroadcast(intent);
                    break;
                case OFFLINE:
                    if(onlineUserList.containsKey(ip)) onlineUserList.remove(ip);
                    intent = new Intent(Constants.ACTION_DISPLAY_USER_OFF);
                    bundle = new Bundle();
                    bundle.putString("ip",ip);
                    intent.putExtras(bundle);
                    sendBroadcast(intent);
                    break;
                case REQUESTCONNECTION:

                    break;
                case REJECTCONNECTION:

                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
