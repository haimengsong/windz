package com.song.filetransfer.activity;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.song.filetransfer.R;
import com.song.filetransfer.adapter.FriendExpandableListViewAdapter;
import com.song.filetransfer.base.BaseWebActivity;
import com.song.filetransfer.model.PeerModel;
import com.song.filetransfer.service.WebService;

import java.util.List;

public class FriendsActivity extends BaseWebActivity{
    public final String TAG = getClass().getName();
    private ExpandableListView mExpandableListView;
    private FriendExpandableListViewAdapter mFriendExpandableListViewAdapter;
    private TextView mNoFriendTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        init();
    }
    private void init(){
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorBlack)));
        mExpandableListView = (ExpandableListView) findViewById(R.id.elv_friend_list);
        mNoFriendTextView = (TextView) findViewById(R.id.tv_no_friend);
        mExpandableListView.setGroupIndicator(null);
        setListeners();
        if(!getMyApplication().getUserModel().isHasFriend()) showIndicator();
        else {
            List<PeerModel> mFriendList = getMyApplication().getUserModel().getFriendList();
            showFriendList();
            Log.i(TAG,mFriendList.size()+" friends in the data source");
            mFriendExpandableListViewAdapter = new FriendExpandableListViewAdapter(this,mFriendList);
            mExpandableListView.setAdapter(mFriendExpandableListViewAdapter);

        }
    }
    private void setListeners() {

    }

    private void showIndicator() {
        mNoFriendTextView.setVisibility(View.VISIBLE);
        mExpandableListView.setVisibility(View.GONE);
    }

    private void showFriendList() {
        mNoFriendTextView.setVisibility(View.GONE);
        mExpandableListView.setVisibility(View.VISIBLE);
    }

    private void updateScreen(){
        if(!getMyApplication().getUserModel().isHasFriend()) showIndicator();
        else {
            List<PeerModel> mFriendList = getMyApplication().getUserModel().getFriendList();
            showFriendList();
            mFriendExpandableListViewAdapter.setData(mFriendList);
        }
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
}
