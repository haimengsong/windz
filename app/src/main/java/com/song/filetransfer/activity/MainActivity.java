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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.song.filetransfer.R;
import com.song.filetransfer.base.BaseWebActivity;
import com.song.filetransfer.model.Constants;
import com.song.filetransfer.service.WebService;
import com.song.filetransfer.utilities.NetUtil;
import com.song.filetransfer.view.RadarView;

public class MainActivity extends BaseWebActivity implements View.OnClickListener {
    public final String TAG = getClass().getName();

    private TextView mTVUserName;
    private TextView mTVUserIP;

    private LinearLayout mLinearLayout;

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
        mLinearLayout = (LinearLayout) findViewById(R.id.layout_user_info);
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
        mIntentFilter.addAction(Constants.ACTION_DISPLAY_USER_IN);
        mIntentFilter.addAction(Constants.ACTION_DISPLAY_USER_OFF);
        mIntentFilter.addAction(Constants.ACTION_DISPLAY_USER_FRIEND_REQUEST);
        mIntentFilter.addAction(Constants.ACTION_DISPLAY_USER_FILE_REQUEST);
    }

    @Override
    protected void onReceiveIntent(Context context, Intent intent) {
        Log.d(TAG,"received intent");

        String  action = intent.getAction();
        switch (action){
            case Constants.ACTION_DISPLAY_USER_IN:
                displayUserIn(intent);
                break;
            case Constants.ACTION_DISPLAY_USER_OFF:
                displayUserOff(intent);
                break;
            case Constants.ACTION_DISPLAY_USER_FRIEND_REQUEST:
                displayUserFriendRequest(intent);
                break;
            case Constants.ACTION_DISPLAY_USER_FILE_REQUEST:
                displayUserFileRequest(intent);
                break;
        }
    }

    private void displayUserFileRequest(Intent intent) {
        Bundle bundle = intent.getExtras();
    }

    private void displayUserFriendRequest(Intent intent) {
        Bundle bundle = intent.getExtras();
    }

    private void displayUserOff(Intent intent) {
        Bundle bundle = intent.getExtras();
    }

    private void displayUserIn(Intent intent) {
        Bundle bundle = intent.getExtras();
        String name = bundle.getString("name");
        String ip = bundle.getString("ip");

    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId){
            case R.id.ib_record:
                break;
            case R.id.ib_search:
                boolean isSearching = mRadarView.isSearch();
                if(isSearching) {
                    getService().handleMsgFromClients(WebService.OFFLINE,null);
                    mRadarView.setIsSearching(!isSearching);
                    HideUserInfo();
                }else{
                    if(NetUtil.isWifiConnect(this)) {
                        String ip = NetUtil.getIPAddr(this);
                        Log.i(TAG,"current ip address: "+ ip);
                        getMyApplication().getUserModel().setIP(ip);
                        displayUserInfo(getMyApplication().getUserModel().getName(),ip);
                        mRadarView.setIsSearching(!isSearching);
                        getService().handleMsgFromClients(WebService.ONLINE,null);
                    }
                    else Toast.makeText(this,R.string.wifi_not_work,Toast.LENGTH_SHORT);
                }
                break;
            case R.id.ib_friends:
                Intent intent = new Intent(this,FriendsActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void HideUserInfo() {
        mIBSetting.setVisibility(View.VISIBLE);
        mLinearLayout.setVisibility(View.GONE);
    }

    private void displayUserInfo(String name, String ip) {
        mIBSetting.setVisibility(View.GONE);
        mTVUserName.setText(name);
        mTVUserIP.setText(ip);
        mLinearLayout.setVisibility(View.VISIBLE);
    }
}
