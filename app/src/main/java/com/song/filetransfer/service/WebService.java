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
import com.song.filetransfer.model.FileModel;
import com.song.filetransfer.model.PeerModel;
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
    // for user and udp helper
    public final static int ONLINE = 0x00000000;

    public final static int OFFLINE = 0x00000001;

    public final static int REQUESTCONNECTION = 0x00000002;

    public final static int REJECTCONNECTION = 0x00000003;

    public final static int SENDFILE = 0x00000004;

    //for tcp helper
    public final static int RECEIVE_FILE = 0x00000005;

    public final static int FILE_STATE_CHANGE = 0x00000006;


    private TcpHelper tcphelper;

    private UdpHelper udpHelper;

    private MyApplication myApplication;

    private UserModel userModel;

    private Object lockObject = new Object();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = (MyApplication) getApplicationContext();
        userModel = myApplication.getUserModel();
        udpHelper = new UdpHelper(this);
        tcphelper = new TcpHelper(this);
    }

    public void handleMsgFromClients(int what, Bundle bundle){
        switch (what){
            case ONLINE:
                Log.i(TAG,"ask udpHelper to broadcast online message in LAN");
                udpHelper.broadcastOnline(userModel);
                tcphelper.startServer();
                break;
            case OFFLINE:
                Log.i(TAG,"ask udpHelper to broadcast offline message in LAN");
                udpHelper.broadcastOffline();
                break;
            case SENDFILE:
                String ip = bundle.getString("userIP");
                String filePath = bundle.getString("filePath");
                Log.e(TAG,userModel.getPeer(ip)+"");
                FileModel fileModel = new FileModel(filePath,FileModel.FILE_SEND);
                userModel.getPeer(ip).addFile(fileModel);
                sendBroadCast(new Intent(Constants.ACTION_DISPLAY_FILE_LIST_CHANGE));
                Log.i(TAG,"ask tcpHelper to send file: "+filePath+" to: "+ip);
                tcphelper.sendFile(ip,fileModel);
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
                    ///// just for testing
                    PeerModel peerModel = new PeerModel(name,mac,ip);
                    peerModel.setIdentity(PeerModel.FRIEND);
                    userModel.addPeer(peerModel);
                    sendBroadCast(new Intent(Constants.ACTION_DISPLAY_CONNECTION_CHANGE));
                    ////
                    intent = new Intent(Constants.ACTION_DISPLAY_USER_IN);
                    bundle = new Bundle();
                    bundle.putString("name",name);
                    bundle.putString("mac",mac);
                    bundle.putString("ip",ip);
                    intent.putExtras(bundle);
                    sendBroadcast(intent);
                    break;
                case OFFLINE:
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

    public void handleMsgFromTCP(int info,String ip,FileModel fileModel){

        Intent intent = null;
        switch (info){
            case RECEIVE_FILE:
                synchronized (lockObject){
                    userModel.getPeer(ip).addFile(fileModel);
                }
                intent = new Intent(Constants.ACTION_DISPLAY_FILE_LIST_CHANGE);
                sendBroadCast(intent);
                break;
            case FILE_STATE_CHANGE:
                intent = new Intent(Constants.ACTION_DISPLAY_FILE_LIST_CHANGE);
                sendBroadCast(intent);
                break;
        }

    }




    private void sendBroadCast(Intent intent){
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


    public final class LocalBinder extends Binder {
        public WebService getService(){
            return WebService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tcphelper.stopServer();
    }
}
