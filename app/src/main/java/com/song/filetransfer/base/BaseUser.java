package com.song.filetransfer.base;


public class BaseUser {
    private String name;
    private String mac;
    private String ip;
    private String location;
    public BaseUser(){
        this(null,null,null);
    }
    public BaseUser(String name){
        this(name,null,null);
    }
    public BaseUser(String name,String mac){
        this(name,mac,null);
    }
    public BaseUser(String name,String mac, String ip ){
        this(name,mac,ip,null);
    }
    public BaseUser(String name,String mac, String ip, String location ){

        this.name = name;
        this.mac = mac;
        this.ip = ip;
        this.location = location;
    }

    public void setMac(String mac){this.mac = mac;}

    public String getMac(){ return this.mac;}

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

    public void setLocation(String location){this.location = location;}

    public String getLocation(){return location;}

    public int getDistance() {
        return 0;
    }

    public String getDistanceByString(){
        int res = getDistance();
        if(res/1000==0) return res+" m";
        return res + " km";
    }
}
