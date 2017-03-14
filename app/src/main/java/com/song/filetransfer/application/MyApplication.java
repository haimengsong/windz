package com.song.filetransfer.application;

import android.app.Application;

import com.song.filetransfer.model.UserModel;

public class MyApplication extends Application{

    private UserModel mySelf;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public UserModel getUserModel(){
        return mySelf;
    }


    public void setUserInfo(UserModel userModel){
        this.mySelf = userModel;
    }
}
