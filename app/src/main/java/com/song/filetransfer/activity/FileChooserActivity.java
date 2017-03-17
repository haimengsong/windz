package com.song.filetransfer.activity;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.song.filetransfer.R;
import com.song.filetransfer.base.BaseActivity;
import com.song.filetransfer.base.BaseWebActivity;
import com.song.filetransfer.service.WebService;
import com.song.filetransfer.utilities.FileUtil;
import com.song.filetransfer.utilities.StorageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileChooserActivity extends BaseWebActivity implements AdapterView.OnItemClickListener{

    private final String TAG = getClass().getName();
    private final static int MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 0;
    private String rootDirPath;
    private ListView mListView;
    private SimpleAdapter listAdapter;
    private List<Map<String, Object>> fileListData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_files);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setRootDirPath();
    }

    private void setRootDirPath() {
        if(!StorageUtil.isExternalStorageWritable()){
            Toast.makeText(this,"ExternalStorage is not available",Toast.LENGTH_SHORT);
        }else{
            checkExternalStoragePermisson();
        }
    }
    private void checkExternalStoragePermisson(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }else{
            rootDirPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            showRootPage(rootDirPath);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    rootDirPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                    showRootPage(rootDirPath);
                }else{

                }
                return;
            }
        }
    }

    private void showRootPage(String path){

        mListView = (ListView)findViewById(R.id.lv_file_choose);
        fileListData = new ArrayList<>();
        setFileListData(path);
        listAdapter = new SimpleAdapter(this,fileListData, R.layout.activity_file_chooser_item,
                new String[]{"name","img"},
                new int[]{R.id.file_name,R.id.file_img});
        mListView.setAdapter(listAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setSelection(0);
    }
    private void setFileListData(String path){
        File mFile = new File(path);
        Log.d(TAG,"Path of Current Directory: "+path);
        Map<String, Object> rootItemData = new HashMap<>();
        rootItemData.put("name", "root");
        rootItemData.put("img", R.drawable.img_dictionary2);
        rootItemData.put("path",path);
        fileListData.add(rootItemData);
        if(!mFile.isDirectory()){
            return;
        }
        File[] files = mFile.listFiles();
        if(files==null) Log.d(TAG,"files is null");
        if (files!=null) {
            for(File childFile:files){
                Map<String, Object> map = new HashMap<>();
                map.put("name",childFile.getName());
                map.put("path",childFile.getPath());
                if(childFile.isDirectory()){
                    map.put("img",R.drawable.img_dictionary3);
                }else {
                    String type = FileUtil.getFileExtension(childFile);
                    if(FileUtil.isMusic(type)){
                        map.put("img", R.drawable.img_music);
                    }else if(FileUtil.isVideo(type)){
                        map.put("img", R.drawable.img_video);
                    }else if(FileUtil.isDoc(type)){
                        map.put("img", R.drawable.img_doc);
                    }else if(FileUtil.isPpt(type)){
                        map.put("img", R.drawable.img_ppt);
                    }else if(FileUtil.isExcel(type)){
                        map.put("img", R.drawable.img_xls);
                    }else if(FileUtil.isImage(type)){
                        map.put("img", R.drawable.img_image);
                    }else if(FileUtil.isPdf(type)){
                        map.put("img", R.drawable.img_pdf);
                    }else if(FileUtil.isRar(type)){
                        map.put("img", R.drawable.img_rar);
                    }else if(FileUtil.isZip(type)){
                        map.put("img", R.drawable.img_zip);
                    }else if(FileUtil.isTxt(type)){
                        map.put("img", R.drawable.img_txt);
                    }else{
                        map.put("img", R.drawable.img_other);
                    }
                }
                fileListData.add(map);
            }
        }
        return;
    }

    @Override
    protected void addFiltersToIntentFilter(IntentFilter mIntentFilter) {

    }

    @Override
    protected void onReceiveIntent(Context context, Intent intent) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch(itemId){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position ==0 ){
            fileListData.clear();
            setFileListData(rootDirPath);
            listAdapter.notifyDataSetChanged();
        }else{
            String mPath = (String) fileListData.get(position).get("path");
            File file = new File(mPath);
            if(!file.isDirectory()){
                showDialog(mPath);
                return;
            }
            Log.d(TAG,"Get into directory "+mPath);
            fileListData.clear();
            setFileListData(mPath);
            listAdapter.notifyDataSetChanged();
        }
    }

    private void showDialog(final String fileName) {
        new AlertDialog.Builder(this)
                .setMessage("Send \""+fileName+"\"?")
                .setTitle(R.string.dialog_confirm)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /////just for testing
                        Bundle bundle = new Bundle();
                        bundle.putString("ip","localhost");
                        bundle.putString("filePath",fileName);
                        getService().handleMsgFromClients(WebService.SENDFILE,bundle);
                        /////
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            backToParent();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void backToParent() {
        String currentPath = (String) fileListData.get(0).get("path");
        File file = new File(currentPath);
        File pFile = file.getParentFile();
        if(pFile == null){
            Toast.makeText(this,"This is root directory",Toast.LENGTH_SHORT);
        }else{
            String pPath = pFile.getAbsolutePath();
            fileListData.clear();
            setFileListData(pPath);
            listAdapter.notifyDataSetChanged();
        }
    }
}
