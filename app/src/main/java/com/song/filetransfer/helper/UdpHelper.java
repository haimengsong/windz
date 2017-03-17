package com.song.filetransfer.helper;


import android.app.Service;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.song.filetransfer.model.Constants;
import com.song.filetransfer.service.WebService;
import com.song.filetransfer.utilities.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class UdpHelper {

    public static final String TAG = UdpHelper.class.getSimpleName();
    private final static int SENDPORT = 5000;
    private final static int RECEIVEPORT = 5001;
    private final static int BUFFERLENGTH = 1024;
    private final static String BROADCASTADDRESS = "255.255.255.255";

    private WebService service;

    private DatagramSocket udpSendSocket;
    private DatagramSocket udpReceiveSocket;


    private MyRunnableFactory mRunnableFactory;

    private Thread onLineThread;
    private ExecutorService threadPool;

    private Object mon;

    public UdpHelper(WebService service){
        this.service = service;
        this.mRunnableFactory = new MyRunnableFactory();
        ThreadFactory threadFactory = new CustomThreadFactoryBuilder()
                .setNamePrefix("UDPSendThreadPool")
                .setDaemon(false)
                .setPriority(Thread.MAX_PRIORITY).build();
        threadPool = Executors.newCachedThreadPool(threadFactory);
    }

    public void broadcastOnline() {
        try {
            udpSendSocket = new DatagramSocket(SENDPORT);
            udpSendSocket.setReuseAddress(true);
            udpSendSocket.setBroadcast(true);
            Log.i(TAG,"udp send socket is bind to "+udpSendSocket.getLocalAddress());
        } catch(SocketException e){
            e.printStackTrace();
            Log.e(TAG,"Fail to create udpSendSocket");
        }
        try {
            udpReceiveSocket = new DatagramSocket(RECEIVEPORT);
            udpReceiveSocket.setReuseAddress(true);
            Log.i(TAG,"udp receive socket is bind to "+udpReceiveSocket.getLocalAddress());
            onLineThread = new Thread(mRunnableFactory.getRunnable(WebService.ONLINE));
            onLineThread.start();
        } catch(SocketException e){
            e.printStackTrace();
            Log.e(TAG,"Fail to create udpReceiveSocket");
        }
    }

    public void broadcastOffline() {
        Log.i(TAG,"Stop the thread waiting for messages from LAN");
        //if(onLineThread!=null) onLineThread.interrupt();
        new Thread(mRunnableFactory.getRunnable(WebService.OFFLINE)).start();
        threadPool.shutdown();
    }

    public void closeSocket(){
        if(udpSendSocket!=null) {
            udpSendSocket.close();
            udpSendSocket = null;
        }
        if(udpReceiveSocket!=null){
            udpReceiveSocket.close();
            udpReceiveSocket = null;
        }
    }


    public synchronized void sendUdpData(String sendStr, InetAddress sendTo,
                                         int sendPort){

        DatagramPacket udpSendPacket=null;
        try {
            byte[] sendBuffer = sendStr.getBytes("UTF-8");

            udpSendPacket = new DatagramPacket(sendBuffer,sendBuffer.length,sendTo,sendPort);

            udpSendSocket.send(udpSendPacket);

            Log.i(TAG,"Data: "+sendStr+ " has been sent to "+sendTo.getHostAddress());
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private class MyRunnableFactory{
        class OnlineRunnable implements Runnable{
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("info",WebService.ONLINE);
                    jsonObject.put("mac",service.getGlobal().getUserModel().getMac());
                    jsonObject.put("name",service.getGlobal().getUserModel().getName());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //send three packets indicating online
                for(int i=0;i < 3; i++){
                    try{
                        InetAddress broadcastAddr = InetAddress.getByName(BROADCASTADDRESS);
                        sendUdpData(jsonObject.toString(),broadcastAddr,RECEIVEPORT);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
                byte[] receiveBuffer = new byte[BUFFERLENGTH];

                try{
                    while(true){
                        DatagramPacket udpReceivePacket = new DatagramPacket(receiveBuffer,receiveBuffer.length);
                        udpReceiveSocket.receive(udpReceivePacket);
                        if(udpReceivePacket.getLength()==0){
                            Log.i(TAG,"receive empty packet from "+udpReceivePacket.getAddress().getHostAddress());
                            continue;
                        }
                        String msgStr = "";
                        try{
                            msgStr = new String(receiveBuffer,0,udpReceivePacket.getLength(),"UTF-8");
                            Log.i(TAG,"receive message: "+msgStr+" from "+udpReceivePacket.getAddress().getHostAddress());
                            service.handleMsgFromUDP(udpReceivePacket.getAddress().getHostAddress(),msgStr);
                        }catch(UnsupportedEncodingException e){
                            e.printStackTrace();
                        }
                    }
                } catch(SocketException e){
                    Log.i(TAG,"offline thread closed udp socket!");
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        class OfflineRunnable implements Runnable{
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("info",WebService.OFFLINE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //send three packets indicating offline
                for(int i=0;i < 3; i++){
                    try{
                        InetAddress broadcastAddr = InetAddress.getByName(BROADCASTADDRESS);
                        sendUdpData(jsonObject.toString(),broadcastAddr,RECEIVEPORT);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
                onLineThread = null;
                closeSocket();
            }
        }
        class ConnectionRequestRunnable implements Runnable{
            @Override
            public void run() {

            }
        }
        class ConnectionRejectRunnable implements Runnable{
            @Override
            public void run() {

            }
        }
        class ConnectionAcceptRunnable implements Runnable{
            @Override
            public void run() {

            }
        }

        public Runnable getRunnable(int runnableType){
            switch (runnableType){
                case WebService.ONLINE:
                    return new OnlineRunnable();
                case WebService.OFFLINE:
                    return new OfflineRunnable();
                default:
                    break;
            }
            return null;
        }
    }



     private class CustomThreadFactoryBuilder{

        private String namePrefix = null;
        private boolean daemon = false;
        private int priority = Thread.NORM_PRIORITY;

        public CustomThreadFactoryBuilder setNamePrefix(String namePrefix){
            if(namePrefix == null){
                throw new NullPointerException();
            }
            this.namePrefix = namePrefix;
            return this;
        }

        public CustomThreadFactoryBuilder setDaemon(boolean daemon){
            this.daemon = daemon;
            return this;
        }

        public CustomThreadFactoryBuilder setPriority(int priority){
            if(priority<Thread.MIN_PRIORITY){
                throw new IllegalArgumentException(String.format("Thread priority (%s) must be >= (%s)",priority,Thread.MIN_PRIORITY));
            }
            if(priority>Thread.MAX_PRIORITY){
                throw new IllegalArgumentException(String.format("Thread priority (%s) must be <= (%s)",priority,Thread.MAX_PRIORITY));
            }
            this.priority = priority;
            return this;
        }

        public ThreadFactory build(){
            return build(this);
        }

        public ThreadFactory build(CustomThreadFactoryBuilder builder){

            final String namePrefix = builder.namePrefix;
            final Boolean daemon = builder.daemon;
            final Integer priority = builder.priority;

            final AtomicLong count = new AtomicLong(0);

            return new ThreadFactory(){
                @Override
                public Thread newThread(@NonNull Runnable r) {
                    Thread thread = new Thread(r);
                    if(namePrefix != null){
                        thread.setName(namePrefix+"-"+count.getAndIncrement());
                    }
                    if(daemon != null){
                        thread.setDaemon(daemon);
                    }
                    if(priority != null){
                        thread.setPriority(priority);
                    }
                    return thread;
                }
            };
        }
    }

}
