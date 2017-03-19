package com.song.filetransfer.model;


import java.io.File;
import java.util.Date;

public class FileModel{

    public final static int FILE_START = 0x0001;
    public final static int FILE_TRANSFERING = 0x0002;
    public final static int FILE_PAUSE = 0x0003;
    public final static int FILE_SUCCESS = 0x0004;
    public final static int FILE_FAIL = 0x0005;

    public final static int FILE_SEND = 0x0006;

    public final static int FILE_RECEIVE = 0x0007;


    private File file;
    private String filePath;
    private int state;
    private long totalSize;
    private long curSize;
    private int transDirection;
    private String date;

    public FileModel(String filePath,int transDirection){
        this.filePath = filePath;
        this.file = new File(filePath);
        this.state = FILE_START;
        this.totalSize = file.getTotalSpace();
        this.curSize = 0;
        this.transDirection = transDirection;
        this.date = android.text.format.DateFormat.format("yyyy-MM-dd kk:mm:ss a", new java.util.Date()).toString();
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


    public int getRate() {
        return 1000;
    }

    public String getDate() {
        return date;
    }

}
