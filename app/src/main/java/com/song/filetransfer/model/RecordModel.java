package com.song.filetransfer.model;


import java.io.File;

public class RecordModel {

    public final static int FILE_SEND = 0x0006;

    public final static int FILE_RECEIVE = 0x0007;

    private String filePath = "";
    private int totalSize = 0;
    private int transDirection = FILE_SEND;
    private String friendName = "";

    public RecordModel(){}
    public RecordModel(String filePath){this.filePath=filePath;}
    public RecordModel(String filePath, int totalSize){
        this.filePath = filePath;
        this.totalSize = totalSize;
    }
    public RecordModel(String filePath, int totalSize, int transDirection){
        this.filePath = filePath;
        this.totalSize = totalSize;
        this.transDirection = transDirection;
    }
    public RecordModel(String filePath, int totalSize, int transDirection, String friendName){
        this.filePath = filePath;
        this.totalSize = totalSize;
        this.transDirection = transDirection;
        this.friendName = friendName;
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
    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public int getTransDirection() {
        return transDirection;
    }

    public void setTransDirection(int transDirection) {
        this.transDirection = transDirection;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

}
