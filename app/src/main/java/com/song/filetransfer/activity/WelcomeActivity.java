package com.song.filetransfer.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.song.filetransfer.R;
import com.song.filetransfer.application.MyApplication;
import com.song.filetransfer.model.UserModel;
import com.song.filetransfer.utilities.NetUtil;

public class WelcomeActivity extends Activity {

    public final static String TAG = WelcomeActivity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //retrieve user's information
        retrieveInfo();
        startMainActivity();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
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
