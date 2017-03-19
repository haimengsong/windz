package com.song.filetransfer.model;


import com.song.filetransfer.base.BaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserModel extends BaseUser{

    private Map<String,RecordModel> mRecordList;
    private Map<String,PeerModel> mPeerList;

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
        mPeerList = new HashMap<>();
    }

    public PeerModel getPeer(String ip){
        return mPeerList.get(ip);
    }

    public void addPeer(PeerModel peerModel){
        mPeerList.put(peerModel.getIP(),peerModel);
    }

    public void removePeer(PeerModel peerModel){
        mPeerList.remove(peerModel.getMac());
    }

    public void addRecord(RecordModel recordModel){
        mRecordList.put(recordModel.getFilePath(),recordModel);
    }

    public void removeRecord(RecordModel recordModel){
        mRecordList.remove(recordModel.getFilePath());
    }

    public boolean isHasFriend(){
        List<PeerModel> friendList = getFriendList();
        if(friendList.isEmpty()) return false;
        return true;
    }
    public List<PeerModel> getFriendList(){
        List<PeerModel> friendList = new ArrayList<>();
        for(String ip:mPeerList.keySet()){
            if(mPeerList.get(ip).getIdentity()==PeerModel.FRIEND){
                friendList.add(mPeerList.get(ip));
            }
        }
        return friendList;
    }
}
