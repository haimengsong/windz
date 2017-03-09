package com.song.filetransfer.utilities;


import android.os.Environment;

public class StorageUtil {
    // Checks if external storage is available for write

    public static boolean isExternalStorageWritable(){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)) return true;
        return false;
    }

    public static boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
                return true;
            }
        }
        return false;
    }
}
