package com.song.filetransfer.utilities;


import android.content.Intent;
import android.net.Uri;

import java.io.File;

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
        if(fileType.equals(VIDEODATATYPE))
            return getBaseIntent(filePath).setDataAndType(uri,VIDEODATATYPE);
        else if(fileType.equals(AUDIODATATYPE))
            return getBaseIntent(filePath).setDataAndType(uri,AUDIODATATYPE);
        else if(fileType.equals(IMAGEDATATYPE))
            return getBaseIntent(filePath).setDataAndType(uri,IMAGEDATATYPE);
        else if(fileType.equals(PACKAGEDATATYPE))
            return getBaseIntent(filePath).setDataAndType(uri,PACKAGEDATATYPE);
        else if(fileType.equals(PPTDATATYPE))
            return getBaseIntent(filePath).setDataAndType(uri,PPTDATATYPE);
        else if(fileType.equals(EXCELDATATYPE))
            return getBaseIntent(filePath).setDataAndType(uri,EXCELDATATYPE);
        else if(fileType.equals(WORDDATATYPE))
            return getBaseIntent(filePath).setDataAndType(uri,WORDDATATYPE);
        else if(fileType.equals(PDFDATATYPE))
            return getBaseIntent(filePath).setDataAndType(uri,PDFDATATYPE);
        else if(fileType.equals(CHMDATATYPE))
            return getBaseIntent(filePath).setDataAndType(uri,CHMDATATYPE);
        else if(fileType.equals(EXCELDATATYPE))
            return getBaseIntent(filePath).setDataAndType(uri,EXCELDATATYPE);
        else return getBaseIntent(filePath).setDataAndType(uri,OTHERDATATYPE);
    }

    public static Intent getBaseIntent(String filePath){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
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


    public static String getAppropriateSize(int size){
        int level = 0;
        while((size/1024)!=0){
            size /= 1024;
            level++;
        }
        return size +((level==0)?" B":((level==1)?" KB": ((level==2)?" MB":"GB")));
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
