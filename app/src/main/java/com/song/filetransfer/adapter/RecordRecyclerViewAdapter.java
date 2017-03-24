package com.song.filetransfer.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.song.filetransfer.R;
import com.song.filetransfer.model.RecordModel;

import java.util.List;

public class RecordRecyclerViewAdapter  extends RecyclerView.Adapter<RecordRecyclerViewAdapter.MyViewHolder>{


    private Context context;
    private List<RecordModel> list;
    public RecordRecyclerViewAdapter(Context context, List<RecordModel> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v  = LayoutInflater.from(context).inflate(R.layout.record_item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RecordModel recordModel = list.get(position);
        holder.fileNameTextView.setText(recordModel.getFileName());
        holder.fileSizeTextView.setText(recordModel.getTotalSize()+"");
        holder.fileFriendTextView.setText(recordModel.getFriendName());
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView fileNameTextView;
        public TextView fileSizeTextView;
        public TextView fileFriendTextView;

        public MyViewHolder(View view){
            super(view);
            fileNameTextView = (TextView) view.findViewById(R.id.rc_file_name);
            fileSizeTextView = (TextView) view.findViewById(R.id.rc_file_size);
            fileFriendTextView = (TextView) view.findViewById(R.id.rc_file_friend);
        }
    }
}
