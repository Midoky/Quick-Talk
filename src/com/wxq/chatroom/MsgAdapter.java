package com.wxq.chatroom;

import java.util.ArrayList;

import com.example.adventuretogether.R;
import com.wxq.model.ChatItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MsgAdapter extends ArrayAdapter<ChatItem>{
	
	private int resourceId;

	public MsgAdapter(Context context,int textViewResourceId,ArrayList<ChatItem> objects){
		super(context , textViewResourceId, objects);
		resourceId = textViewResourceId;
	}

	@SuppressWarnings("static-access")
	public View getView(int position,View convertView,ViewGroup parent){
		ChatItem msg = getItem(position);
		View view;
		ViewHolder viewHolder;
		if(convertView == null){
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
			viewHolder.rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
			viewHolder.leftMsg = (TextView) view.findViewById(R.id.left_msg);
			viewHolder.rightMsg = (TextView) view.findViewById(R.id.right_msg);
			viewHolder.lefttime = (TextView) view.findViewById(R.id.sms_time1);
			viewHolder.righttime = (TextView) view.findViewById(R.id.sms_time2);
			view.setTag(viewHolder);
		}else{
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();	
		}
		if(msg.isRes() == false){
			//服务器接受其他用户发送的信息
			viewHolder.leftLayout.setVisibility(view.VISIBLE);
			viewHolder.rightLayout.setVisibility(View.GONE);
			viewHolder.leftMsg.setText(msg.getUser()+"："+msg.getContent());
		}else if(msg.isRes() == true){
			//服务器接收的用户发送的信息
			viewHolder.rightLayout.setVisibility(View.VISIBLE);
			viewHolder.leftLayout.setVisibility(View.GONE);
			viewHolder.rightMsg.setText(msg.getContent());
		}
		return view;
	}
	class ViewHolder{
		LinearLayout leftLayout;
		LinearLayout rightLayout;
		
		TextView leftMsg;
		TextView lefttime;
		TextView rightMsg;
		TextView righttime;
	}

}