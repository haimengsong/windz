package com.song.filetransfer.activity;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.song.filetransfer.R;
import com.song.filetransfer.base.BaseWebActivity;
import com.song.filetransfer.service.WebService;
import com.song.filetransfer.view.RadarView;

public class MainActivity extends BaseWebActivity implements View.OnClickListener {
    public final String TAG = getClass().getName();

    private TextView mTVUserName;
    private TextView mTVUserIP;

    private RadarView mRadarView;

    private ImageButton mIBSetting;
    private ImageButton mIBRecord;
    private ImageButton mIBSearch;
    private ImageButton mIBFriend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        findViews();
        setListeners();
    }
    private void findViews(){
        mTVUserName = (TextView) findViewById(R.id.tv_user_name);
        mTVUserIP = (TextView) findViewById(R.id.tv_user_ip);
        mRadarView = (RadarView) findViewById(R.id.radar_view);
        mIBSetting = (ImageButton) findViewById(R.id.ib_setting);
        mIBRecord = (ImageButton) findViewById(R.id.ib_record);
        mIBSearch = (ImageButton) findViewById(R.id.ib_search);
        mIBFriend = (ImageButton) findViewById(R.id.ib_friends);
    }

    private void setListeners(){
        mIBRecord.setOnClickListener(this);
        mIBSearch.setOnClickListener(this);
        mIBFriend.setOnClickListener(this);
    }
    @Override
    protected void addFiltersToIntentFilter(IntentFilter mIntentFilter) {
        mIntentFilter.addAction("com.song.transfer.ACTION_DISPLAY_USER_IN");
    }

    @Override
    protected void onReceiveIntent(Context context, Intent intent) {
        Log.d(TAG,"received intent");

        String  action = intent.getAction();
        switch (action){
            case "com.song.transfer.ACTION_DISPLAY_USER_IN":
                break;
        }
    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId){
            case R.id.ib_record:
                break;
            case R.id.ib_search:
                boolean isSearching = mRadarView.isSearch();
                mRadarView.setIsSearching(!isSearching);
                if(isSearching) getService().perform(WebService.OFFLINE,null);
                else getService().perform(WebService.ONLINE,null);
                break;
            case R.id.ib_friends:
                Intent intent = new Intent(this,FriendsActivity.class);
                startActivity(intent);
                break;
        }
    }
}
