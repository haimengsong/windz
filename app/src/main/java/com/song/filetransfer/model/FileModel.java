package com.song.filetransfer.model;


import java.io.File;
import java.util.Date;

public class FileModel {

    public final static int FILE_START = 0x0001;
    public final static int FILE_TRANSFERING = 0x0002;
    public final static int FILE_PAUSE = 0x0003;
    public final static int FILE_SUCCESS = 0x0004;
    public final static int FILE_FAIL = 0x0005;

    public final static int FILE_SEND = 0x0006;

    public final static int FILE_RECEIVE = 0x0007;



    private String filePath = "";
    private int state = FILE_START;
    private int totalSize = 0;
    private int curSize = 0;
    private int transDirection = FILE_SEND;
    private Date date;

    public FileModel(){}
    public FileModel(String filePath){this.filePath=filePath;}
    public FileModel(String filePath, int totalSize){
        this.filePath = filePath;
        this.totalSize = totalSize;
    }
    public FileModel(String filePath, int totalSize, int transDirection){
        this.filePath = filePath;
        this.totalSize = totalSize;
        this.transDirection = transDirection;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public int getCurSize() {
        return curSize;
    }

    public void setCurSize(int curSize) {
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

    public void setDate(Date date){
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

}
