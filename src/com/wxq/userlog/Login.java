package com.wxq.userlog;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.example.adventuretogether.R;
import com.wxq.model.User;
import com.wxq.web.WebService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener {

    // 登录，返回登录
    private Button logbtn,backbtn;
    // 用于返回用户信息，已弃用
    private TextView infotv;
    // 用户输入框
    private EditText username, password;
    //	返回用户状态
    private boolean statue = false;
    // 提示框
    private ProgressDialog dialog;
    //	检查用户
    private String checkedInUser;
    // 子线程传递数据回主线程修改UI
    private static Handler handler = new Handler();
    //退出状态
	private long mExitTime;
	//用户信息
	private static User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        // 获取所有控件信息
        username = (EditText) findViewById(R.id.userText);
        password = (EditText) findViewById(R.id.pswText);
        logbtn = (Button) findViewById(R.id.sqllogin);
        backbtn = (Button) findViewById(R.id.logback);
        infotv = (TextView) findViewById(R.id.info);
        this.init();
        // 监听按钮信息
        logbtn.setOnClickListener(this);
        backbtn.setOnClickListener(this);

    }
    // 初始化
    private void init() {
		// TODO Auto-generated method stub
    	 Intent intent =getIntent(); 
         String state = intent.getStringExtra("state");
         if(state.equals("Main")){
         	if(readUser()&&user != null){
         		username.setText(user.getUserName());
         		password.setText(user.getUserPsw());
         		checkedInUser = username.getText().toString();
         		dialog = new ProgressDialog(this);
                dialog.setTitle("请稍等");
                dialog.setMessage("正在登陆...");
                dialog.setCancelable(false);
                dialog.show();
         		new Thread(new MyThread()).start();
         	}
         } 
	}

	@Override
    public void onClick(View v) {
    	checkedInUser = username.getText().toString();
    	infotv.setText(""); 
        switch (v.getId()) {
        case R.id.sqllogin:
            // 网络异常
            if (!checkNetwork()) {
                Toast toast = Toast.makeText(Login.this,"没有网络", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                break;
            }
            if(username.getText().toString().equals("")||password.getText().toString().equals("")){//δ����û���������
            	 Toast toast = Toast.makeText(Login.this,"请输入账号或密码", Toast.LENGTH_SHORT);
                 toast.setGravity(Gravity.CENTER, 0, 0);
                 toast.show();
                 break;
            }
            //登录
            dialog = new ProgressDialog(this);
            dialog.setTitle("请稍等");
            dialog.setMessage("正在登陆...");
            dialog.setCancelable(false);
            dialog.show();
            // 启动新线程进行登录
            new Thread(new MyThread()).start();
            break;
        case R.id.logback:
            Intent back = new Intent(Login.this, MainActivity.class);
            startActivity(back);
            finish();
            break;
        }
    }
    // 登录
    public class MyThread implements Runnable {
        @Override
        public void run() {
        	statue =  WebService.executeHttpGet(username.getText().toString(), password.getText().toString());
         //  info = WebServicePost.executeHttpPost(username.getText().toString(), password.getText().toString());
             handler.post(new Runnable() {
                @Override
                public void run() {
                	dialog.dismiss();  
                	//登录成功
                   if(statue){
                	   User newUser = new User();
                	   newUser.setUserName(username.getText().toString());
                	   newUser.setUserPsw(password.getText().toString());
                	   saveUser(newUser);
                       Intent i = new Intent(Login.this,Interface.class);
                       i.putExtra("userLoginName", checkedInUser);
                       startActivity(i);
                       finish();
                   }
                   //登录失败
                   else{
                	   Toast toast = Toast.makeText(Login.this,"登录失败", Toast.LENGTH_SHORT);
                       toast.setGravity(Gravity.CENTER, 0, 0);
                       toast.show();
                	   User nullUser = null;
                	   saveUser(nullUser);
                	   password.setText("");
                   }

                }
            });
        }
    }

    // 检查网络
    private boolean checkNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
    }
    //本地储存用户信息
    public void saveUser(User user)
  	 {
      	try {
  			FileOutputStream fos = this.openFileOutput("UserInfo.dat",Context.MODE_PRIVATE);
  			ObjectOutputStream oos = new ObjectOutputStream(fos);
  			oos.writeObject(user);
  			fos.close();
  			oos.close();
  		} catch (FileNotFoundException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
  	 }
    //本地读取用户信息
	public boolean readUser()
      {
      	try {
  			FileInputStream fis = this.openFileInput("UserInfo.dat");
  			ObjectInputStream ois = new ObjectInputStream(fis);
  			Object object = ois.readObject();
  			user = (User)object;
  			fis.close();
  			ois.close();
  			return true;
  		} catch (FileNotFoundException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  			return false;
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  			return false;
  		} catch (ClassNotFoundException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  			return false;
  		}
    }
    //两次确认退出
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
}