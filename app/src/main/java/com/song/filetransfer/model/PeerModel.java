package com.song.filetransfer.model;


import com.song.filetransfer.base.BaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeerModel extends BaseUser{

    public final static int NORMAL = 0x0001;

    public final static int FRIEND = 0x0002;

    private List<FileModel> mFileList;

    private int identity = NORMAL;

    private boolean mIsConnected = false;

    public PeerModel(){
        this(null,null,null);
    }

    public PeerModel(String name){
        this(name,null,null);
    }

    public PeerModel(String name,String mac){
        this(name,mac,null);
    }

    public PeerModel(String name,String mac, String ip ){
        super(name,mac,ip);
        mFileList = new ArrayList<>();
    }
    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public boolean isConnected(){return mIsConnected;}

    public void setIsConnected(boolean mIsConnected){this.mIsConnected = mIsConnected;}

    public int addFile(FileModel fileModel){
        mFileList.add(fileModel);
        return mFileList.size()-1;
    }

    public FileModel getFileById(int id){
        return mFileList.get(id);
    }

    public int getFileNumber(){
        return mFileList.size();
    }


}
