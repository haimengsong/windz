package com.song.filetransfer.model;


import com.song.filetransfer.base.BaseUser;

import java.util.HashMap;
import java.util.Map;

public class UserModel extends BaseUser{

    private Map<String,RecordModel> mRecordList;
    private Map<String,FriendModel> mFriendList;

    public UserModel(){
        this(null,null,null);
    }
    public UserModel(String name){
        this(name,null,null);
    }
    public UserModel(String name,String mac){
        this(name,mac,null);
    }
    public UserModel(String name,String mac, String ip ){
        super(name,mac,ip);
        mRecordList = new HashMap<>();
        mFriendList = new HashMap<>();
    }

    public void addFriend(FriendModel friendModel){
        mFriendList.put(friendModel.getMac(),friendModel);
    }

    public void removeFriend(FriendModel friendModel){
        mFriendList.remove(friendModel.getMac());
    }

    public void addRecord(RecordModel recordModel){
        mRecordList.put(recordModel.getFilePath(),recordModel);
    }

    public void removeRecord(RecordModel recordModel){
        mRecordList.remove(recordModel.getFilePath());
    }
}
