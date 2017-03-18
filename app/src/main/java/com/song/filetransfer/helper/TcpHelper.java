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

    public final static String CHARSET = "UTF-8";
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
                                        BufferedReader br = null;
                                        String fileName = "";
                                        String fileSize = "";
                                        try{

                                            br = new BufferedReader(new InputStreamReader(s.getInputStream(),CHARSET));
                                            String line = null;
                                            int count = 0;
                                            while(count<2){
                                                line = br.readLine();
                                                switch (count) {
                                                    case 0:
                                                        fileName = line;
                                                        Log.i(TAG, "file's name: " + line);
                                                        break;
                                                    case 1:
                                                        fileSize = line;
                                                        Log.i(TAG, "file's size: " + line + "bytes");
                                                        break;
                                                }
                                                count++;
                                            }

                                        }catch (IOException e){
                                            e.printStackTrace();
                                        }


                                        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),fileName);
                                        Log.i(TAG,"start saving file to : "+ file.getAbsolutePath());
                                        BufferedOutputStream bos = null;
                                        BufferedInputStream bis = null;
                                        try {

                                            bos= new BufferedOutputStream(new FileOutputStream(file));
                                            bis = new BufferedInputStream(s.getInputStream());
                                            byte [] buffer = new byte[8192];
                                            int count = 0;
                                            Log.i(TAG,"available: "+bis.available());
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
                                        } finally {
                                            if(bos!=null) {
                                                try {
                                                    bos.close();
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
                                            if(br!=null) {
                                                try {
                                                    br.close();
                                                } catch (IOException e){
                                                    e.printStackTrace();
                                                }
                                            }

                                            if(s!=null) {
                                                try {
                                                    s.close();
                                                } catch (IOException e){
                                                    e.printStackTrace();
                                                }
                                            }
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
                BufferedWriter bw = null;
                File file = new File(filePath);
                try{
                    socket = new Socket("192.168.0.7",ServerPort);

                    bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),CHARSET));
                    for(int count = 0;count<2;count++){
                        switch(count){
                            case 0:
                                bw.write(file.getName());
                                bw.newLine();
                                bw.flush();
                                break;
                            case 1:
                                bw.write(String.valueOf(file.getTotalSpace()));
                                bw.newLine();
                                bw.flush();
                                break;

                        }
                    }
                    ops = new BufferedOutputStream(socket.getOutputStream());
                    bis = new BufferedInputStream(new FileInputStream(file));
                    byte [] buffer = new byte[8192];
                    int count=0;
                    while((count = bis.read(buffer))!=-1){
                        ops.write(buffer,0,count);
                        ops.flush();
                        Log.i(TAG, "write "+count+" bytes to outputstream");
                    }
                    Log.i(TAG,"client: "+socket.getLocalAddress().getHostAddress()+" on port: "+socket.getLocalPort()+" succeed to send file: "+filePath+" to server!");
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
                    if(bw!=null) {
                        try {
                            bw.close();
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
