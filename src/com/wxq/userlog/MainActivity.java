package com.wxq.userlog;

import com.example.adventuretogether.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	private long mExitTime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);		
		Button login = (Button)findViewById(R.id.mainlogin);
		Button register = (Button)findViewById(R.id.mainregister);
        login.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,Login.class);
				intent.putExtra("state", "Main");
				startActivity(intent);
				finish();
			}
		} );
        register.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,Register.class);
				startActivity(intent);
				finish();
			}
		} );
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
