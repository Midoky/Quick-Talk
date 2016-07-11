package com.wxq.userlog;

import com.example.adventuretogether.R;
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
import android.widget.Toast;

public class Register extends Activity implements OnClickListener{
	//获取用户登录信息
	private EditText newUserName,newPsw,checkNewPsw;
	// 按钮
    private Button recbtn,regback;
    // 登录状态
    private boolean statue = false;
    // 提示框
    private ProgressDialog dialog;
    // 检查用户信息
    private String checkedInUser;
    // 子线程传递数据回主线程修改UI
    private static Handler handler = new Handler();
    //退出
	private long mExitTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register);
        //获取控件
        newUserName = (EditText) findViewById(R.id.newuserText);
        newPsw = (EditText) findViewById(R.id.newpswText);
        checkNewPsw = (EditText) findViewById(R.id.checknewpswText);
        recbtn = (Button) findViewById(R.id.sqlregister);
        regback = (Button)findViewById(R.id.regback);
        // 监听按钮
        recbtn.setOnClickListener(this);
        regback.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
   	checkedInUser = newUserName.getText().toString();
        switch (v.getId()) {
        case R.id.sqlregister:
            // 检查网络状态
            if (!checkNetwork()) {
                Toast toast = Toast.makeText(Register.this,"没有网络", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                break;
            }
            if(newUserName.getText().toString().equals("")){//δ�����û���
            	 Toast toast = Toast.makeText(Register.this,"账户不能为空", Toast.LENGTH_SHORT);
                 toast.setGravity(Gravity.CENTER, 0, 0);
                 toast.show();
                 break;
            }
            if(newPsw.getText().toString().equals("")){//δ��������
           	 Toast toast = Toast.makeText(Register.this,"密码不能为空", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                break;
           }
            if(checkNewPsw.getText().toString().equals("")){//δ����ȷ������
              	 Toast toast = Toast.makeText(Register.this,"请再次确认密码", Toast.LENGTH_SHORT);
                   toast.setGravity(Gravity.CENTER, 0, 0);
                   toast.show();
                   break;
              }
            if(!newPsw.getText().toString().equals(checkNewPsw.getText().toString())){
            	Toast toast = Toast.makeText(Register.this,"两次输入的密码不匹配", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                newPsw.setText("");
                checkNewPsw.setText("");
                break;
            }
            if(newUserName.getText().toString().length() < 6){
            	Toast toast = Toast.makeText(Register.this,"账号长度应大于六位", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                break;
            }
            if(newPsw.getText().toString().length() < 6){
            	Toast toast = Toast.makeText(Register.this,"密码长度应大于六位", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                break;
            }
            // 状态提示
            dialog = new ProgressDialog(this);
            dialog.setTitle("请稍后");
            dialog.setMessage("正在登陆...");
            dialog.setCancelable(false);
            dialog.show();
            // 启动子线程进行登录
            new Thread(new MyThread()).start();
            break;
        case R.id.regback:
            Intent back = new Intent(Register.this, MainActivity.class);
            // overridePendingTransition(anim_enter);
            startActivity(back);
            finish();
            break;
        }
    }
    //进行注册，获得注册信息
    public class MyThread implements Runnable {
        @Override
        public void run() {
        	statue =  WebService.executeHttpGetRegister(newUserName.getText().toString(), newPsw.getText().toString());
         //  info = WebServicePost.executeHttpPost(username.getText().toString(), password.getText().toString());
             handler.post(new Runnable() {
                @Override
                public void run() {
                	dialog.dismiss();  
                	//注册成功
                   if(statue){
                       Intent i = new Intent(Register.this,Interface.class);
                       i.putExtra("userLoginName", checkedInUser);
                       startActivity(i);
                       finish();
                   }
                   //注册失败
                   else{
                	   Toast toast = Toast.makeText(Register.this,"注册失败", Toast.LENGTH_SHORT);
                       toast.setGravity(Gravity.CENTER, 0, 0);
                       toast.show();
                	  // username.setText("");
                	   newPsw.setText("");
                	   checkNewPsw.setText("");
                   }

                }
            });
        }
    }
    // 检查网络状态
    private boolean checkNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }
        return false;
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
}
