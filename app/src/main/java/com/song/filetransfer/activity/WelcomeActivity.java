package com.song.filetransfer.activity;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.song.filetransfer.R;
import com.song.filetransfer.application.MyApplication;
import com.song.filetransfer.model.UserModel;

public class WelcomeActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //retrieve user's information
        retrieveInfo();

    }

    private void retrieveInfo(){
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName",getString(R.string.user_name));
        MyApplication mApplication = (MyApplication) getApplicationContext();
        mApplication.setUserInfo(new UserModel(userName));
    }

}
