package com.song.filetransfer.activity;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.song.filetransfer.R;
import com.song.filetransfer.adapter.RecordRecyclerViewAdapter;
import com.song.filetransfer.base.BaseActivity;
import com.song.filetransfer.model.Constants;
import com.song.filetransfer.model.RecordModel;

import java.util.List;

public class RecordsActivity extends BaseActivity{


    private RecyclerView mRecyclerView;

    private TextView noRecordTextView;

    private List<RecordModel> list;

    private RecordRecyclerViewAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorBlack)));
        setContentView(R.layout.activity_records);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_record);
        noRecordTextView =(TextView) findViewById(R.id.tv_no_record);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = getMyApplication().getUserModel().getRecordList();
        mAdapter = new RecordRecyclerViewAdapter(this,list);
        if(list.isEmpty()){
            showNoRecord();
        }else{
            showRecordList();
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    private void showRecordList(){
        mRecyclerView.setVisibility(View.VISIBLE);
        noRecordTextView.setVisibility(View.GONE);
    }

    private void showNoRecord(){
        mRecyclerView.setVisibility(View.GONE);
        noRecordTextView.setVisibility(View.VISIBLE);
    }

    private void updateRecord(){
        if(mRecyclerView.getAdapter()==null){
            mRecyclerView.setAdapter(mAdapter);
        } else mAdapter.notifyDataSetChanged();
    }
    @Override
    protected void addFiltersToIntentFilter(IntentFilter mIntentFilter) {
        mIntentFilter.addAction(Constants.ACTION_DISPLAY_RECORD_LIST_CHANGE);
    }

    @Override
    protected void onReceiveIntent(Context context, Intent intent) {
        String action = intent.getAction();
        switch(action){
            case Constants.ACTION_DISPLAY_RECORD_LIST_CHANGE:
                updateRecord();
                break;

        }
    }
}
