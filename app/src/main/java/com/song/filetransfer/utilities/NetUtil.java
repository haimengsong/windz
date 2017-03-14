package com.song.filetransfer.utilities;


import android.util.Log;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public class NetUtil {
    public static final String TAG = "NetUtil";
    public static void getLocalIpAddress() throws UnknownHostException, SocketException {
        InetAddress localHost = Inet4Address.getLocalHost();
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);

        for (InterfaceAddress address : networkInterface.getInterfaceAddresses()) {
            Log.i(TAG,localHost.getHostAddress()+" "+address.getAddress()+" "+address.getNetworkPrefixLength()+"");
        }
    }
    }



