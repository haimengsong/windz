package com.song.filetransfer.model;


import java.io.File;

public class RecordModel {


    private String filePath = null;
    private long totalSize = 0;
    private int transDirection = 0;
    private String friendName = null;

    public RecordModel(String friendName, FileModel fileModel){

        this.friendName = friendName;
        filePath = fileModel.getFilePath();
        totalSize = fileModel.getTotalSize();
        transDirection = fileModel.getTransDirection();
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

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
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
