package com.song.filetransfer.model;


import com.song.filetransfer.base.BaseUser;

import java.util.HashMap;
import java.util.Map;

public class FriendModel extends BaseUser{

    private Map<String,FileModel> mFileList;
    private boolean mIsConnected;

    public FriendModel(){
        this(null,null,null);
    }

    public FriendModel(String name){
        this(name,null,null);
    }

    public FriendModel(String name,String mac){
        this(name,mac,null);
    }

    public FriendModel(String name,String mac, String ip ){
        super(name,mac,ip);
        mFileList = new HashMap<>();
        mIsConnected = true;
    }

    public boolean isConnected(){return mIsConnected;}

    public void setIsConnected(boolean mIsConnected){this.mIsConnected = mIsConnected;}

    public void addFile(FileModel fileModel){
        mFileList.put(fileModel.getFilePath(),fileModel);
    }

    public void removeFile(FileModel fileModel){
        mFileList.remove(fileModel.getFilePath());
    }
}
