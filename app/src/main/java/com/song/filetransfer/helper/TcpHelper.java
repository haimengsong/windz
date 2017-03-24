package com.song.filetransfer.helper;


import android.app.Service;
import android.os.Environment;
import android.util.Log;

import com.song.filetransfer.model.FileModel;
import com.song.filetransfer.service.WebService;

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
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TcpHelper {

    public final static String TAG  = TcpHelper.class.getSimpleName();

    public final static int ServerPort = 5005;

    public final static String CHARSET = "UTF-8";
    private WebService mService;

    private ServerSocket serverSocket;

    private Thread serverThread;

    private ExecutorService threadPool;

    public TcpHelper(Service service){
        this.mService = (WebService) service;
        threadPool = Executors.newCachedThreadPool();

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
                                Socket s = serverSocket.accept();
                                Log.i(TAG,"receive a socket from: "+ s.getInetAddress().getHostAddress());
                                threadPool.execute(new Runnable() {

                                    private Socket mSocket;

                                    public Runnable init(Socket s){
                                        this.mSocket = s;
                                        return this;
                                    }

                                    @Override
                                    public void run() {
                                        BufferedReader br = null;
                                        String fileName = "";
                                        String fileSize = "";
                                        String checkSum = "";
                                        Timer timer = null;
                                        try{

                                            br = new BufferedReader(new InputStreamReader(mSocket.getInputStream(),CHARSET));
                                            String line = null;
                                            int count = 0;
                                            while(count<3){
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
                                                    case 2:
                                                        checkSum = line;
                                                        Log.i(TAG, "file's check sum: " + line);
                                                        break;
                                                }
                                                count++;
                                            }

                                        }catch (IOException e){
                                            e.printStackTrace();
                                        }
                                        final FileModel fileModel = new FileModel(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/"+fileName,FileModel.FILE_RECEIVE);
                                        fileModel.setTotalSize(Long.parseLong(fileSize));
                                        fileModel.setCheckSum(checkSum);
                                        mService.handleMsgFromTCP(WebService.RECEIVE_FILE,mSocket.getInetAddress().getHostAddress(),fileModel);
                                        Log.i(TAG,"start saving file to : "+ fileModel.getFilePath());
                                        BufferedOutputStream bos = null;
                                        BufferedInputStream bis = null;
                                        fileModel.setState(FileModel.FILE_TRANSFERING);
                                        mService.handleMsgFromTCP(WebService.FILE_TRANSFERING,null,null);
                                        try {

                                            bos= new BufferedOutputStream(new FileOutputStream(fileModel.getFile()));
                                            bis = new BufferedInputStream(mSocket.getInputStream());
                                            byte [] buffer = new byte[8192];
                                            int count = 0;
                                            timer = new Timer();
                                            timer.scheduleAtFixedRate(new TimerTask() {
                                                private long preSize = fileModel.getCurSize();
                                                @Override
                                                public void run() {
                                                    long curSize = fileModel.getCurSize();
                                                    fileModel.setRate((long)((curSize-preSize)/0.1));
                                                    mService.handleMsgFromTCP(WebService.FILE_TRANSFERING,null,null);
                                                }
                                            },100,100);

                                            while ((count = bis.read(buffer))!=-1){
                                                Log.i(TAG,"receive "+count+" bytes from input stream.");
                                                bos.write(buffer,0,count);
                                                bos.flush();
                                                fileModel.addCurSize(count);
                                            }
                                            Log.i(TAG,"succeed to receive file: "+ fileModel.getFilePath());
                                            Log.i(TAG,"start verifying");
                                            fileModel.setState(FileModel.FILE_VERIFYING);
                                            mService.handleMsgFromTCP(WebService.FILE_VERIFYING,null,null);
                                            if(fileModel.verify()){
                                                fileModel.setState(FileModel.FILE_SUCCESS);
                                                mService.handleMsgFromTCP(WebService.FILE_SUCCESS,mSocket.getInetAddress().getHostAddress(),fileModel);
                                            }else{
                                                fileModel.setState(FileModel.FILE_FAIL);
                                                mService.handleMsgFromTCP(WebService.FILE_FAIL,null,null);
                                            }

                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e){
                                            fileModel.setState(FileModel.FILE_FAIL);
                                            mService.handleMsgFromTCP(WebService.FILE_FAIL,null,null);
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

                                            if(mSocket!=null) {
                                                try {
                                                    mSocket.close();
                                                } catch (IOException e){
                                                    e.printStackTrace();
                                                }
                                            }

                                            if(timer!=null){
                                                timer.cancel();
                                            }
                                        }


                                    }
                                }.init(s));
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

    public void sendFile(final String ip, final FileModel fileModel) {
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                BufferedOutputStream ops = null;
                BufferedInputStream bis = null;
                BufferedWriter bw = null;
                Timer timer = null;
                String checkSum = fileModel.calculateCheckSum();
                File file = new File(fileModel.getFilePath());
                try{
                    socket = new Socket(ip,ServerPort);

                    bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),CHARSET));
                    for(int count = 0;count<3;count++){
                        switch(count){
                            case 0:
                                bw.write(file.getName());
                                bw.newLine();
                                bw.flush();
                                break;
                            case 1:
                                bw.write(String.valueOf(file.length()));
                                bw.newLine();
                                bw.flush();
                                break;
                            case 2:
                                bw.write(checkSum);
                                bw.newLine();
                                bw.flush();
                                break;
                        }
                    }
                    fileModel.setState(FileModel.FILE_TRANSFERING);
                    mService.handleMsgFromTCP(WebService.FILE_TRANSFERING,null,null);
                    ops = new BufferedOutputStream(socket.getOutputStream());
                    bis = new BufferedInputStream(new FileInputStream(file));
                    byte [] buffer = new byte[8192];
                    int count=0;
                    timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        private long preSize = fileModel.getCurSize();
                        @Override
                        public void run() {
                            long curSize = fileModel.getCurSize();
                            fileModel.setRate((long)((curSize-preSize)/0.1));
                            mService.handleMsgFromTCP(WebService.FILE_TRANSFERING,null,null);
                        }
                    },100,100);
                    while((count = bis.read(buffer))!=-1){
                        ops.write(buffer,0,count);
                        ops.flush();
                        fileModel.addCurSize(count);
                        Log.i(TAG, "write "+count+" bytes to outputstream");
                    }
                    Log.i(TAG,"client: "+socket.getLocalAddress().getHostAddress()+" on port: "+socket.getLocalPort()+" succeed to send file: "+fileModel.getFilePath()+" to server!");
                    fileModel.setState(FileModel.FILE_SUCCESS);
                    mService.handleMsgFromTCP(WebService.FILE_SUCCESS,ip,fileModel);

                }catch (SocketException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                    fileModel.setState(FileModel.FILE_FAIL);
                    mService.handleMsgFromTCP(WebService.FILE_FAIL,null,null);
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
                    if(timer!=null){
                        timer.cancel();
                    }
                }

            }
        });
    }
}
