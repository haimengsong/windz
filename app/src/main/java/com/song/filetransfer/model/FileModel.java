package com.song.filetransfer.model;


import com.song.filetransfer.utilities.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class FileModel{

    public final static int FILE_START = 0x0001;
    public final static int FILE_TRANSFERING = 0x0002;
    public final static int FILE_STOP = 0x0003;
    public final static int FILE_CANCEL = 0x0004;
    public final static int FILE_SUCCESS = 0x0005;
    public final static int FILE_FAIL = 0x0006;
    public final static int FILE_VERIFYING = 0x0009;

    public final static int FILE_SEND = 0x0007;
    public final static int FILE_RECEIVE = 0x0008;


    private File file = null;
    private String filePath = null;
    private int state = FILE_START;
    private long totalSize  = file.length();
    private long curSize = 0;
    private long rate = 0;
    private int transDirection = 0;
    private String date = android.text.format.DateFormat.format("MM-dd kk:mm:ss a", new java.util.Date()).toString();
    private String checkSum = null;

    public FileModel(String filePath,int transDirection){
        this.filePath = filePath;
        this.file = new File(filePath);
        this.transDirection = transDirection;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getCurSize() {
        return curSize;
    }

    public void setCurSize(long curSize) {
        this.curSize = curSize;
    }

    public void addCurSize(long addBytes){
        this.curSize += addBytes;
    }

    public int getTransDirection() {
        return transDirection;
    }

    public void setTransDirection(int transDirection) {
        this.transDirection = transDirection;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName(){
        return new File(filePath).getName();
    }

    public void setRate(long rate){
        this.rate = rate;
    }

    public long getRate() {
        return rate;
    }

    public String getDate() {
        return date;
    }

    public void setCheckSum(String checkSum){
        this.checkSum = checkSum;
    }

    public boolean verify(){
        return FileUtil.checkMD5(checkSum,file);
    }

    public String calculateCheckSum(){
        checkSum = FileUtil.calculateMD5(file);
        return checkSum;
    }

    public File getFile(){
        return file;
    }
}
