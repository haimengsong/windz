package com.song.filetransfer.adapter;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.song.filetransfer.R;
import com.song.filetransfer.activity.FriendsActivity;
import com.song.filetransfer.model.FileModel;
import com.song.filetransfer.model.PeerModel;
import com.song.filetransfer.utilities.FileUtil;

import org.w3c.dom.Text;

import java.util.List;

public class FriendExpandableListViewAdapter extends BaseExpandableListAdapter  {

    private List<PeerModel> mFriendList;
    private Context context;
    private GroupButtonClickListener groupButtonListener;


    public FriendExpandableListViewAdapter(Context context,List<PeerModel> mFriendList){
        this.context = context;
        this.mFriendList = mFriendList;
    }


    @Override
    public int getGroupCount() {
        return mFriendList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mFriendList.get((int)getGroupId(groupPosition));
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        GroupViewHolder holder = null;
        if(convertView == null){
            holder = new GroupViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.friend_group_layout,parent,false);
            holder.fileSendButton = (Button) convertView.findViewById(R.id.btn_send);
            holder.friendNameTextView = (TextView) convertView.findViewById(R.id.friend_name);
            holder.friendIPTextView = (TextView) convertView.findViewById(R.id.friend_ip);
            holder.friendDistanceTextView = (TextView) convertView.findViewById(R.id.friend_distance);
            convertView.setTag(holder);
        } else{
            holder = (GroupViewHolder) convertView.getTag();
        }

        PeerModel friendData = (PeerModel) getGroup(groupPosition);
        holder.fileSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(groupButtonListener!=null){
                    groupButtonListener.onGroupButtonClick(groupPosition);
                }
            }
        });
        holder.friendNameTextView.setText(friendData.getName());
        holder.friendIPTextView.setText(friendData.getIP());
        holder.friendDistanceTextView.setText(friendData.getDistanceByString());

        return convertView;
    }




    @Override
    public int getChildrenCount(int groupPosition) {
        PeerModel friend = (PeerModel) getGroup(groupPosition);
        return friend.getFileNumber();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        PeerModel friend = (PeerModel) getGroup(groupPosition);
        return friend.getFileById((int)getChildId(groupPosition,childPosition));
    }


    private boolean checkIfRightLayout(View convertView, int state ){

        ChildViewHolder holder = (ChildViewHolder)convertView.getTag();
        if(holder.type==FileModel.FILE_TRANSFERING && state == holder.type) return true;
        if(holder.type==FileModel.FILE_SUCCESS){
            if(state == FileModel.FILE_TRANSFERING) return false;
            return true;
        }
        return false;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        FileModel fileData = (FileModel) getChild(groupPosition,childPosition);

        ChildViewHolder childViewHolder = null;

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        boolean isTransferring = (fileData.getState()==FileModel.FILE_TRANSFERING);

        if(convertView == null || !checkIfRightLayout(convertView,fileData.getState())){
            childViewHolder = new ChildViewHolder();
            if(fileData.getState() == FileModel.FILE_TRANSFERING){

                convertView = layoutInflater.inflate(R.layout.child_transfering_layout,parent,false);
                childViewHolder.type = FileModel.FILE_TRANSFERING;
                childViewHolder.fileNameTextView = (TextView) convertView.findViewById(R.id.file_name);
                childViewHolder.fileRateTextView = (TextView) convertView.findViewById(R.id.file_rate);
                childViewHolder.fileCurrentSizeTextView = (TextView) convertView.findViewById(R.id.file_currentsize);
                childViewHolder.fileStateTextView = (TextView) convertView.findViewById(R.id.file_state);
                childViewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.file_progress);
                convertView.setTag(childViewHolder);
            }
            else{
                convertView = layoutInflater.inflate(R.layout.child_transferred_layout,parent,false);
                childViewHolder.type = FileModel.FILE_SUCCESS;
                childViewHolder.fileNameTextView = (TextView) convertView.findViewById(R.id.file_name);
                childViewHolder.fileDateTextView = (TextView) convertView.findViewById(R.id.file_date);
                childViewHolder.fileTotalSizeTextView = (TextView) convertView.findViewById(R.id.file_size);
                childViewHolder.fileStateTextView = (TextView) convertView.findViewById(R.id.file_state);
                convertView.setTag(childViewHolder);
            }

        }

        childViewHolder = (ChildViewHolder)convertView.getTag();
        if(childViewHolder.type == FileModel.FILE_TRANSFERING){
            childViewHolder.fileNameTextView.setText(fileData.getFileName());
            childViewHolder.fileRateTextView.setText(fileData.getRate());
            childViewHolder.fileCurrentSizeTextView.setText(FileUtil.getAppropriateSize(fileData.getCurSize()));
            childViewHolder.fileStateTextView.setText(fileData.getTransDirection()==FileModel.FILE_SEND?context.getString(R.string.file_sending):context.getString(R.string.file_receiving));
            childViewHolder.progressBar.setMax((int)fileData.getTotalSize());
            childViewHolder.progressBar.setProgress((int)fileData.getCurSize());
        }else{
            childViewHolder.fileNameTextView.setText(fileData.getFileName());
            childViewHolder.fileDateTextView.setText(fileData.getDate());
            childViewHolder.fileTotalSizeTextView.setText(FileUtil.getAppropriateSize(fileData.getTotalSize()));
            String state = null;
            switch (fileData.getState()){
                case FileModel.FILE_SUCCESS:
                    state = context.getString(R.string.file_success);
                    break;
                case FileModel.FILE_FAIL:
                    state = context.getString(R.string.file_fail);
                    break;
                case FileModel.FILE_PAUSE:
                    state = context.getString(R.string.file_pause);
                    break;
            }
            childViewHolder.fileStateTextView.setText(state);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public void setOnGroupButtonClickListener(GroupButtonClickListener groupButtonClickListener) {
        this.groupButtonListener = groupButtonClickListener;
    }


    private class GroupViewHolder{
        public Button fileSendButton;
        public TextView friendNameTextView;
        public TextView friendIPTextView;
        public TextView friendDistanceTextView;
    }
    private class ChildViewHolder{
        public int type;
        public TextView fileNameTextView;
        public TextView fileSizeTextView;
        public TextView fileStateTextView;
        public TextView fileRateTextView;
        public TextView fileCurrentSizeTextView;
        public TextView fileTotalSizeTextView;
        public TextView fileDateTextView;
        public ProgressBar progressBar;
    }


    public void setData(List<PeerModel> mFriendList){
        this.mFriendList = mFriendList;
        notifyDataSetChanged();
    }

    public static interface GroupButtonClickListener {
        void onGroupButtonClick(int groupPosition);
    }
}
