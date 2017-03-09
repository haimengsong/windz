package com.song.filetransfer.activity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.song.filetransfer.R;
import com.song.filetransfer.base.BaseWebActivity;
import com.song.filetransfer.service.WebService;

public class MainActivity extends BaseWebActivity {
    public static final String TAG = "MainActivity";
    private TextView mTextView;
    private Button mButton1;
    private Button mButton2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.text);
        mButton1 =(Button) findViewById(R.id.button1);
        mButton1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("ACCEPT");
                WebService mService = getService();
                if(mService!=null) mService.processMessages(intent);
            }
        });
        mButton2 =(Button) findViewById(R.id.button2);
        mButton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,FileChooserActivity.class);
                startActivity(intent);
            }
        });
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
                mTextView.setText(intent.getExtras().getString("VALUE"));
                mTextView.append(getBaseContext().getClass().getName().toString());
                break;
        }
    }


}
