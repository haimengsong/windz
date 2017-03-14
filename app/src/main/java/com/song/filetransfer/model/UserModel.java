package com.song.filetransfer.model;


import com.song.filetransfer.base.BaseUser;

public class UserModel {

    private String name;
    private String ip;
    public UserModel(){

    }
    public UserModel(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getIP(){
        return ip;
    }
    public void setIP(String ip){
        this.ip = ip;
    }
}
