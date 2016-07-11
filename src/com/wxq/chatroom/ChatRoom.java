package com.wxq.chatroom;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.example.adventuretogether.R;
import com.wxq.model.ChatItem;
import com.wxq.web.WebService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChatRoom extends Activity{
    //子线程数据回传给主线程修改UI
    private static Handler handler = new Handler();
    //聊天列表
	static ArrayList<ChatItem> chatList = new ArrayList<ChatItem>();
	//临时聊天列表，用于接收服务端数据
	static ArrayList<ChatItem> chatListTemp = new ArrayList<ChatItem>();
	//房间号，用户
	String roomId,user;
	//内容和状态
	String content;
	boolean state;
	//list列表视图
	static int index,top;
	static Timer timer;
	
	EditText inputText;
	
	Button send;
	
	ListView msgListView;

	private MsgAdapter adapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chatroom);
        adapter = new MsgAdapter(ChatRoom.this,R.layout.item_chat,chatList);
        TextView titleText = (TextView)findViewById(R.id.title);
    	Intent intent =getIntent(); 
    	//从interfaceclass接收属性内容
        roomId = intent.getStringExtra("state");
        user = intent.getStringExtra("user");
        titleText.setText("您已进入房间："+roomId);
        inputText = (EditText) findViewById(R.id.content);
		send = (Button)findViewById(R.id.send);
		msgListView = (ListView)findViewById(R.id.chatlist);
		msgListView.setAdapter(adapter);
		msgListView.setSelection(chatList.size());//返回listView的最后位置
		// 返回上一次列表的位置
		index = msgListView.getFirstVisiblePosition();
		View v = msgListView.getChildAt(0);
		top = (v == null) ? 0 : v.getTop();
		// ...
		//根据Index和top确定位置
		msgListView.setSelectionFromTop(index, top);
		timer = new Timer();
        // 运行子线程，按照时间间隔，每隔一定时间从服务器读一次信息
		timer.schedule(new GetInfoTask(timer),500, 1500);	
		send.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				content =  inputText.getText().toString();
				if(!content.equals("")){
					state = false;
					new Thread(new SendThread()).start();
				}
			}
    });
    }
  class GetInfoTask extends TimerTask {
    private Timer timer;

    public Timer getTimer() {
		return timer;
	}
	public GetInfoTask(Timer timer) {
        this.timer = timer;
    }

    @Override
    public void run() {
    			//根据房间ID和用户名从服务器获取信息
    		chatListTemp= WebService.executeHttpGetMsg(roomId,user);   		
    		handler.post(new Runnable() {
                @Override
                public void run() {     
                	//如果服务器有新消息，并且数据发生变化，就更新列表
                	if(chatListTemp != null&&chatListTemp.size()!=chatList.size()){
                		//如果服务器列表有更新就更新视图列表
                		while(chatListTemp.size()>chatList.size()){
                		chatList.add((chatListTemp.get(chatList.size())));
                		}
            		adapter.notifyDataSetChanged();
            		msgListView.setAdapter(adapter);  
            		msgListView.setSelection(chatList.size());
                	}
                }
		 });
    		
		}
  } 
  //该子线程用于用户发送数据给服务器端
  public class SendThread implements Runnable {
      @Override
      public void run() {
     	  state = WebService.executeHttpSendMsg(user,roomId,content);
           handler.post(new Runnable() {
              @Override
              public void run() {
            	  //发送数据标记
             		if(state){ 
             			inputText.setText("");
	        	       	}else{
	        	       		 Toast.makeText(ChatRoom.this,"信息发送失败..." ,Toast.LENGTH_SHORT).show();
	        	       	}
                 }
          });
      }
  }
  //销毁计时器，改变用户状态
  public void onDestroy(){
	  super.onDestroy();
	  //
	  ChatRoom.timer.cancel();
	  chatList.clear();
	  chatListTemp.clear();
	   WebService.executeHttpCreateChange(user,roomId,"quit");
	   Toast.makeText(ChatRoom.this,"您已退出房间..." ,Toast.LENGTH_SHORT).show();

	  //Toast
  }
}
