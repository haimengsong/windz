package com.song.filetransfer.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.song.filetransfer.R;

public class RadarView extends View {

    public final String TAG = getClass().getName();
    private Bitmap mBitmapSearch;
    private Bitmap mBitmapSweep;
    private Rect rMoon = new Rect();
    private boolean isSearching = false;
    private float accelerate = 2.0f;
    private float offsetArgs =0.0f;

    private int windowWidth;
    private int windowHeight;
    public RadarView(Context context){
        super(context);
        init();
    }


    public RadarView(Context context, AttributeSet attrs){
        super(context,attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.RadarView,0,0);

        try{
            isSearching = a.getBoolean(R.styleable.RadarView_isSearching,false);
            accelerate = a.getFloat(R.styleable.RadarView_accelerate,2.0f);
        } finally{
            a.recycle();
        }
        Log.d(TAG,"Constructor 1: "+isSearching);
        init();
    }
    private void init() {
        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        windowWidth = display.getWidth();
        windowHeight = display.getHeight();
        initBitmap();
    }

    private void initBitmap() {
        if(mBitmapSearch==null){
            mBitmapSearch = Bitmap.createBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.gplus_search_bg));
        }
        if(mBitmapSweep==null){
            mBitmapSweep= Bitmap.createBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.sweep));
        }
        Log.d(TAG,"size of search_bg: "+mBitmapSearch.getWidth()+"*"+mBitmapSearch.getHeight());
        Log.d(TAG,"size of sweep: "+mBitmapSweep.getWidth()+"*"+mBitmapSweep.getHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //Log.d(TAG,"Measure(WH) "+getWidth()+"*"+getHeight());
        //Log.d(TAG,"Measure(MW*MH) "+getMeasuredWidth()+"*"+getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //Log.d(TAG,"Layout(WH) "+getWidth()+"*"+getHeight());
        //Log.d(TAG,"Layout(MW*MH) "+getMeasuredWidth()+"*"+getMeasuredHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Log.d(TAG,"canvas "+canvas.getWidth()+"*"+canvas.getHeight());
        super.onDraw(canvas);
        int width = getWidth()/2;
        int height = getHeight()/2;
        canvas.drawBitmap(mBitmapSearch, width-mBitmapSearch.getWidth()/2,height-mBitmapSearch.getHeight()/2,null);
        canvas.rotate(offsetArgs,width,height);
        rMoon.set(width-mBitmapSweep.getWidth(),height,width,height+mBitmapSweep.getHeight());
        canvas.drawBitmap(mBitmapSweep,null,rMoon,null);
        if(isSearching){
            offsetArgs = offsetArgs + accelerate;
            invalidate();
        }
    }

    public boolean isSearch(){
        return isSearching;
    }
    public void setIsSearching(boolean isSearch){
        isSearching = isSearch;
        invalidate();
    }

}
