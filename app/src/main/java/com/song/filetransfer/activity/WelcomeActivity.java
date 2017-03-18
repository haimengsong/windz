package com.song.filetransfer.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.song.filetransfer.R;
import com.song.filetransfer.application.MyApplication;
import com.song.filetransfer.model.UserModel;
import com.song.filetransfer.utilities.NetUtil;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends Activity {

    public final static String TAG = WelcomeActivity.class.getSimpleName();

    private final static int SPLASH_TIME = 1000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //method 1: user handler
        ///*
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startMainActivity();
            }
        },SPLASH_TIME);
        //*/
        //method 2: use thread
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(SPLASH_TIME);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                startMainActivity();
            }
        }).start();
        */
        // method 3: use timer and timerTask  **deprecated
        /*
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                startMainActivity();
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask,SPLASH_TIME);
        */
        Log.i(TAG,"start loading information!");
        //retrieve user's information
        retrieveInfo();

    }

    private void startMainActivity() {
        Log.i(TAG,"start main activity");
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void retrieveInfo(){
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("userName",getString(R.string.user_name));
        String mac = sharedPreferences.getString("mac","");
        if(mac.isEmpty()) {
            mac = NetUtil.getMacAddr();
            Log.i(TAG,"mac address is: "+mac);
        }
        MyApplication mApplication = (MyApplication) getApplicationContext();
        mApplication.setUserInfo(new UserModel(userName,mac));
    }

}
