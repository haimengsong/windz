package com.song.filetransfer.activity;


import android.app.ActionBar;
import android.app.Activity;
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
import com.song.filetransfer.model.Constants;
import com.song.filetransfer.model.PeerModel;
import com.song.filetransfer.service.WebService;

import java.util.List;

public class FriendsActivity extends BaseWebActivity implements ExpandableListView.OnGroupClickListener, ExpandableListView.OnChildClickListener, FriendExpandableListViewAdapter.GroupButtonClickListener {
    public final String TAG = getClass().getName();

    private final static int PICK_FILE_REQUEST = 1;

    private ExpandableListView mExpandableListView;
    private FriendExpandableListViewAdapter mFriendExpandableListViewAdapter;
    private TextView mNoFriendTextView;
    private List<PeerModel> mFriendListData;

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
        mExpandableListView.setOnGroupClickListener(this);
        mExpandableListView.setOnChildClickListener(this);
        if(!getMyApplication().getUserModel().isHasFriend()) showIndicator();
        else {
            mFriendListData = getMyApplication().getUserModel().getFriendList();
            showFriendList();
            Log.i(TAG,mFriendListData.size()+" friends in the data source");
            mFriendExpandableListViewAdapter = new FriendExpandableListViewAdapter(this,mFriendListData);
            mFriendExpandableListViewAdapter.setOnGroupButtonClickListener(this);
            mExpandableListView.setAdapter(mFriendExpandableListViewAdapter);

        }
    }

    private void showIndicator() {
        mNoFriendTextView.setVisibility(View.VISIBLE);
        mExpandableListView.setVisibility(View.GONE);
    }

    private void showFriendList() {
        mNoFriendTextView.setVisibility(View.GONE);
        mExpandableListView.setVisibility(View.VISIBLE);
    }

    private void updateScreen(boolean isFriendChange){
        if(!getMyApplication().getUserModel().isHasFriend()) showIndicator();
        else {
            if(mFriendListData==null || isFriendChange) mFriendListData = getMyApplication().getUserModel().getFriendList();
            if(mFriendExpandableListViewAdapter==null)
            {
                mFriendExpandableListViewAdapter = new FriendExpandableListViewAdapter(this,mFriendListData);
                mFriendExpandableListViewAdapter.setOnGroupButtonClickListener(this);
            }
            showFriendList();
            mFriendExpandableListViewAdapter.setData(mFriendListData);
        }
    }



    @Override
    protected void addFiltersToIntentFilter(IntentFilter mIntentFilter) {
        mIntentFilter.addAction(Constants.ACTION_DISPLAY_CONNECTION_CHANGE);
        mIntentFilter.addAction(Constants.ACTION_DISPLAY_FILE_LIST_CHANGE);
    }

    @Override
    protected void onReceiveIntent(Context context, Intent intent) {
        
        String  action = intent.getAction();
        switch (action){
            case Constants.ACTION_DISPLAY_CONNECTION_CHANGE:
                Log.i(TAG,"receive intent action: ACTION_DISPLAY_CONNECTION_CHANGE");
                updateScreen(true);
                break;
            case Constants.ACTION_DISPLAY_FILE_LIST_CHANGE:
                Log.i(TAG,"receive intent action: ACTION_DISPLAY_FILE_LIST_CHANGE");
                updateScreen(false);
                break;
        }


    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        return false;
    }

    @Override
    public void onGroupButtonClick(int groupPosition) {
        Intent intent = new Intent(FriendsActivity.this,FileChooserActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userIP",mFriendListData.get(groupPosition).getIP());
        intent.putExtras(bundle);
        startActivityForResult(intent,PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "receive request code: "+requestCode+" and result code: "+resultCode);
        if(requestCode == PICK_FILE_REQUEST){
            if(resultCode == Activity.RESULT_OK){
                Log.i(TAG,"Succeed getting file path: "+ data.getExtras().getString("filePath"));
                Bundle bundle = data.getExtras();
                getService().handleMsgFromClients(WebService.SENDFILE,bundle);
            }
        }
    }
}
