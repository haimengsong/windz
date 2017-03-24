package com.song.filetransfer.utilities;


import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtil {
    private static final String TAG = "FileUtil";
    private static final String OTHERDATATYPE = "*/*";
    private static final String IMAGEDATATYPE = "image/*";
    private static final String PACKAGEDATATYPE = "application/vnd.android.package-archive";
    private static final String VIDEODATATYPE = "video/*";
    private static final String AUDIODATATYPE = "audio/*";
    private static final String HTMLDATATYPE = "text/html";
    private static final String PPTDATATYPE="application/vnd.ms-powerpoint";
    private static final String EXCELDATATYPE="application/vnd.ms-excel";
    private static final String CHMDATATYPE="application/x-chm";
    private static final String WORDDATATYPE="application/msword";
    private static final String TEXTDATATYPE="text/plain";
    private static final String PDFDATATYPE="application/pdf";
    private static final String RARDATATYPE="application/x-rar-compressed";
    private static final String ZIPDATATYPE="application/gzip";

    public static Intent getOpenFileIntent(String filePath){

        File file = new File(filePath);
        Uri uri = Uri.fromFile(new File(filePath));
        if(!file.exists()) return null;
        String fileType = getFileType(file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if(fileType.equals(VIDEODATATYPE))
            intent.setDataAndType(uri,VIDEODATATYPE);
        else if(fileType.equals(AUDIODATATYPE))
            intent.setDataAndType(uri,AUDIODATATYPE);
        else if(fileType.equals(IMAGEDATATYPE))
            intent.setDataAndType(uri,IMAGEDATATYPE);
        else if(fileType.equals(PACKAGEDATATYPE))
            intent.setDataAndType(uri,PACKAGEDATATYPE);
        else if(fileType.equals(PPTDATATYPE))
            intent.setDataAndType(uri,PPTDATATYPE);
        else if(fileType.equals(EXCELDATATYPE))
            intent.setDataAndType(uri,EXCELDATATYPE);
        else if(fileType.equals(WORDDATATYPE))
            intent.setDataAndType(uri,WORDDATATYPE);
        else if(fileType.equals(PDFDATATYPE))
            intent.setDataAndType(uri,PDFDATATYPE);
        else if(fileType.equals(CHMDATATYPE))
            intent.setDataAndType(uri,CHMDATATYPE);
        else if(fileType.equals(EXCELDATATYPE))
            intent.setDataAndType(uri,EXCELDATATYPE);
        else intent.setDataAndType(uri,OTHERDATATYPE);
        return intent;
    }

    public static String getFileExtension(File file){
        // get the extension of this file
        String fileName = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf(".")+1,
                fileName.length());
        return extension;
    }
    public static String getFileType(File file){
        String extension = getFileExtension(file);
        if(isImage(extension)) return IMAGEDATATYPE;
        else if(isVideo(extension)) return VIDEODATATYPE;
        else if(isMusic(extension)) return AUDIODATATYPE;
        else if(isPdf(extension)) return PDFDATATYPE;
        else if(isDoc(extension)) return WORDDATATYPE;
        else if (isExcel(extension)) return EXCELDATATYPE;
        else if(isTxt(extension)) return TEXTDATATYPE;
        else if (isChm(extension)) return CHMDATATYPE;
        else if(isApk(extension)) return PACKAGEDATATYPE;
        else if (isRar(extension)) return RARDATATYPE;
        else if (isZip(extension)) return ZIPDATATYPE;
        else return OTHERDATATYPE;
    }


    public static String getAppropriateSize(long size){
        int level = 0;
        while((size/1024)!=0){
            size /= 1024;
            level++;
        }
        return size +((level==0)?" B":((level==1)?" KB": ((level==2)?" MB":"GB")));
    }


    public static boolean checkMD5(String md5, File updateFile) {
        if (TextUtils.isEmpty(md5) || updateFile == null) {
            Log.e(TAG, "MD5 string empty or updateFile null");
            return false;
        }

        String calculatedDigest = calculateMD5(updateFile);
        if (calculatedDigest == null) {
            Log.e(TAG, "calculatedDigest null");
            return false;
        }

        Log.v(TAG, "Calculated digest: " + calculatedDigest);
        Log.v(TAG, "Provided digest: " + md5);

        return calculatedDigest.equalsIgnoreCase(md5);
    }

    public static String calculateMD5(File updateFile) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Exception while getting digest", e);
            return null;
        }

        InputStream is;
        try {
            is = new FileInputStream(updateFile);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Exception while getting FileInputStream", e);
            return null;
        }

        byte[] buffer = new byte[8192];
        int read;
        try {
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            // Fill to 32 chars
            output = String.format("%32s", output).replace(' ', '0');
            return output;
        } catch (IOException e) {
            throw new RuntimeException("Unable to process file for MD5", e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e(TAG, "Exception on closing MD5 input stream", e);
            }
        }
    }





    public static boolean isImage(String type) {
        if (type != null
                && (type.equals("jpg") || type.equals("gif")
                || type.equals("png") || type.equals("jpeg")
                || type.equals("bmp") || type.equals("wbmp")
                || type.equals("ico") || type.equals("jpe"))) {
            return true;
        }
        return false;
    }
    public static boolean isVideo(String type) {
        if (type != null
                && (type.equals("asx") || type.equals("wmv")
                || type.equals("asf") || type.equals("rm")
                || type.equals("mpg") || type.equals("rmvb")
                || type.equals("mpeg") || type.equals("mpe")
                || type.equals("3gp")|| type.equals("mov")
                || type.equals("m4v")|| type.equals("mp4")
                || type.equals("avi")|| type.equals("mkv")
                || type.equals("flv"))) {
            return true;
        }
        return false;
    }
    public static boolean isMusic(String type){
        if (type != null
                && (type.equals("asx") || type.equals("mp3")
                || type.equals("wma")|| type.equals("waw") || type.equals("m4a")
                || type.equals("mod") || type.equals("ogg")
                || type.equals("ra")|| type.equals("flac")
                || type.equals("cd")|| type.equals("ape")
                || type.equals("asf")|| type.equals("mid")
                || type.equals("aac"))) {
            return true;
        }
        return false;
    }
    public static boolean isPdf(String type) {
        if (type != null
                && (type.equals("pdf"))) {
            return true;
        }
        return false;
    }
    public static boolean isDoc(String type) {
        if (type != null
                && (type.equals("doc")||(type.equals("docx")))) {
            return true;
        }
        return false;
    }
    public static boolean isPpt(String type) {
        if (type != null
                && (type.equals("ppt")||(type.equals("pptx")))) {
            return true;
        }
        return false;
    }
    public static boolean isExcel(String type) {
        if (type != null
                && (type.equals("xls"))) {
            return true;
        }
        return false;
    }
    public static boolean isRar(String type) {
        if (type != null
                && ((type.equals("rar")))) {
            return true;
        }
        return false;
    }
    public static boolean isZip(String type) {
        if (type != null
                && ((type.equals("zip")))) {
            return true;
        }
        return false;
    }
    public static boolean isTxt(String type) {
        if (type != null
                && ((type.equals("txt")))) {
            return true;
        }
        return false;
    }

    public static boolean isChm(String type){
        if(type!=null &&(type.equals("chm"))) return true;
        return false;
    }

    public static boolean isApk(String type){
        if(type!=null && (type.equals("apk"))) return true;
        return false;
    }
}
