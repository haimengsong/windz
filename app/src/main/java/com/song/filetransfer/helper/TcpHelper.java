package com.song.filetransfer.helper;


import android.app.Service;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TcpHelper {

    public final static String TAG  = TcpHelper.class.getSimpleName();

    public final static int ServerPort = 5005;

    private Service service;

    private ServerSocket serverSocket;

    private Thread serverThread;

    public TcpHelper(Service service){
        this.service = service;
    }

    public void startServer(){
        if(serverSocket==null){
            try{
                serverSocket = new ServerSocket(ServerPort);
                Log.i(TAG,"succeed to create server socket bind to port: "+ServerPort);
                serverThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            while(true){
                                final Socket s = serverSocket.accept();
                                Log.i(TAG,"receive a socket from: "+ s.getInetAddress().getHostAddress());
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        /*
                                        try{
                                            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                                            String line = null;
                                            while((line = in.readLine())!=null){
                                                Log.i(TAG,"get message: "+line+" from "+s.getInetAddress().getHostAddress());
                                            }
                                        }catch (IOException e){
                                            e.printStackTrace();
                                        }
                                        */

                                        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"Hello.txt");
                                        Log.i(TAG,"start saving file to : "+ file.getAbsolutePath());
                                        try {

                                            BufferedOutputStream bos= new BufferedOutputStream(new FileOutputStream(file));
                                            BufferedInputStream bis = new BufferedInputStream(s.getInputStream());
                                            byte [] buffer = new byte[8192];
                                            int count = 0;
                                            while ((count = bis.read(buffer))!=-1){
                                                Log.i(TAG,count+"");
                                                bos.write(buffer,0,count);
                                                bos.flush();
                                            }
                                            Log.i("TAG","succeed to receive file ");
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e){
                                            e.printStackTrace();
                                        }


                                    }
                                }).start();
                            }
                        } catch (SocketException e) {
                            e.printStackTrace();
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                });
                serverThread.start();
                Log.i(TAG,"ready to receive connections");
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void stopServer(){
        if(serverSocket!=null){
            try{
                serverSocket.close();
            } catch (IOException e){
                e.printStackTrace();
            }
            serverSocket = null;
        }
    }

    public void sendFile(final String ip, final String filePath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                BufferedOutputStream ops = null;
                BufferedInputStream bis = null;
                try{
                    socket = new Socket("192.168.0.7",ServerPort);
                    ///*
                    ops = new BufferedOutputStream(socket.getOutputStream());
                    bis = new BufferedInputStream(new FileInputStream(new File(filePath)));
                    byte [] buffer = new byte[8192];
                    int count=0;
                    while((count = bis.read(buffer))!=-1){
                        ops.write(buffer,0,count);
                        ops.flush();
                    }
                    //*/
                    /*
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                        out.write("Hello World");
                        out.newLine();
                        out.flush();
                    */
                    Log.i(TAG,"client: "+socket.getLocalAddress().getHostAddress()+" on port: "+socket.getLocalPort()+" succeed to send file: "+filePath+" to server: "+socket.getInetAddress().getHostAddress()+" on port:"+ServerPort+" !");
                }catch (SocketException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }finally {
                    if(ops!=null) {
                        try {
                            ops.close();
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                    if(bis!=null) {
                        try {
                            bis.close();
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    }

                    if(socket!=null) {
                        try {
                            socket.close();
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }

            }
        }).start();
    }
}
