package com.wxq.userlog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.adventuretogether.R;
import com.wxq.chatroom.ChatRoom;
import com.wxq.web.WebService;

public class Interface extends Activity implements OnClickListener{
	//用于显示用户状态的文本框
	private TextView usernameview,text1,text2,text3;
	//按钮
	private Button joinChat,createRoom,cloudNotes,chguser;
	//获取的用户名ֵ
	public static String userLoginName;
	//退出状态
	private long mExitTime;
	//用户是否已在房间标识
	private static boolean cRoom = true;
	//记录文本框当前位置
	public static int textIndex = 0;
	//第一次启动标识
	public boolean firstStart = true;
	//dialog 编辑框
	private EditText t;
	// 建立房间标识
	private boolean createState;
    // 子线程传递数据回主线程修改UI
    private static Handler handler = new Handler();
    //房间号获取
    private static String roomId = null;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gminterface);
		createRoom = (Button)findViewById(R.id.create);
		chguser = (Button)findViewById(R.id.changeuser);
		joinChat = (Button)findViewById(R.id.join);
		cloudNotes = (Button)findViewById(R.id.note);
		text1 = (TextView)findViewById(R.id.commontext1);
		text2 = (TextView)findViewById(R.id.commontext2);
		text3 = (TextView)findViewById(R.id.commontext3);
		usernameview = (TextView)findViewById(R.id.usernameview);
		Intent i =getIntent(); 
		userLoginName = i.getStringExtra("userLoginName");
		usernameview.setText("欢迎"+userLoginName+"使用Quick Talk");       		
		createRoom.setOnClickListener(this);
		joinChat.setOnClickListener(this);
		cloudNotes.setOnClickListener(this);
		chguser.setOnClickListener(this);
	}
	 public void onClick(View v) {
		 
	       switch (v.getId()) {
	        //改变登录用户
	        case R.id.changeuser:
	        	Intent i = new Intent(Interface.this,Login.class);
	        	i.putExtra("state", "GmInterface");
	        	startActivity(i);
	        	finish();
	        	break;
	        //加入房间
	        case R.id.join:
	        	this.dialog();
	        	break;
	        //建立房间
	        case R.id.create:
	        		if(cRoom == true){
	        			//建立房间，生成随机房间号
	        			cRoom = false;	
	        			roomId = (int)(Math.random()*8999999)+1000000+"";
	        			new Thread(new MyThread()).start();
	        		}else{
	        			Toast.makeText(Interface.this,"您已进入"+roomId+"..." ,Toast.LENGTH_SHORT).show();
	        		}
				
	        	break;
	        //云笔记
	        case R.id.note:
        		text1.setText("功能尚未实现");
        	
				text2.setText("功能尚未实现");
			
				text3.setText("功能尚未实现"); 		
        	break;
	        }
	    }
     @SuppressLint("InflateParams")
     //进入房间的dialog
	protected void dialog() {           
    	LayoutInflater inflater = getLayoutInflater();
    	final View DialogView =inflater.inflate ( R.layout.load, null);     
     	AlertDialog.Builder builder = new Builder(Interface.this)
     	.setTitle("请输入房间号")
     	.setIcon(android.R.drawable.ic_dialog_info)
     	.setView(DialogView)
     	.setPositiveButton("进入", new DialogInterface.OnClickListener() {  
             public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();  
              	t =(EditText) DialogView.findViewById(R.id.inputromm);
              	if(t.getText().toString().equals("")){
              		new Thread(new MyThread2()).start();
              	}else{
              		roomId = t.getText().toString();
              		new Thread(new MyThread2()).start();
              	}
             }  
         });  
     	builder.setNegativeButton("取消",  
                new android.content.DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int which) {  
                        dialog.dismiss();  
                    }  
                });  
     	  builder.create().show();
     }
     //子线程创建房间
     public class MyThread implements Runnable {
         @Override
         public void run() {
        	 createState = WebService.executeHttpCreateChange(userLoginName,roomId,"create");
              handler.post(new Runnable() {
                 @Override
                 public void run() {
                		if(createState){ //�����ɹ�
                            cRoom = false;
	         	        	Intent i2 = new Intent(Interface.this,ChatRoom.class);
	        	        	i2.putExtra("state", roomId);
	        	        	i2.putExtra("user", userLoginName);
	         	        	startActivity(i2);
	         	        	text1.setText("你已建立房间："+roomId);
	        	       	}else{
	        	       		cRoom = true;
	        	       		 Toast.makeText(Interface.this,"创建房间失败..." ,Toast.LENGTH_SHORT).show();
	        	       	}
                    }
             });
         }
     }
     //子线程加入房间
     public class MyThread2 implements Runnable {
         @Override
         public void run() {
        	 createState = WebService.executeHttpCreateChange(userLoginName,roomId,"join");
              handler.post(new Runnable() {
                 @Override
                 public void run() {
                		if(createState){ //����ɹ�
                            cRoom = false;
	         	        	Intent i2 = new Intent(Interface.this,ChatRoom.class);
	        	        	i2.putExtra("state", roomId);
	        	        	i2.putExtra("user", userLoginName);
	         	        	startActivity(i2);
	         	        	text1.setText("您加入房间："+roomId);
	        	       	}else{
	        	       		 Toast.makeText(Interface.this,"加入房间失败..." ,Toast.LENGTH_SHORT).show();
	        	       	}
                    }
             });
         }
     }

	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	        if (keyCode == KeyEvent.KEYCODE_BACK) {
				if ((System.currentTimeMillis() - mExitTime) > 2000) {

	                Toast.makeText(this,"再按一次退出" ,Toast.LENGTH_SHORT).show();
	                mExitTime = System.currentTimeMillis();

	            } else {
	                finish();
	            }
	            return true;
	        }
	        return super.onKeyDown(keyCode, event);
	 }
		protected void onDestroy(){
			super.onDestroy();
		}
}