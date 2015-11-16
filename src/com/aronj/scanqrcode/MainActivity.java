package com.aronj.scanqrcode;

import com.zbar.scan.ScanCaptureAct;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity{

	Context me = MainActivity.this;
	ImageButton btnScan;	
	private Vibrator vibrator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnScan = (ImageButton) findViewById(R.id.btnScan);
		vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);  


		btnScan.setOnClickListener(ScanOcl);
		btnScan.setOnTouchListener(new ImageButton.OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){   
					v.setBackgroundResource(R.drawable.two_btn);   

				}   
				else if(event.getAction() == MotionEvent.ACTION_UP){   
					v.setBackgroundResource(R.drawable.two_btn_an); 
					
				} 
				return false;
			}
		});

	}

	/**
	 * 点击扫描的事件监听
	 */
	 public Button.OnClickListener ScanOcl = new Button.OnClickListener() {
		@Override
		public void onClick(View arg0) {

			try {
//
//				long [] pattern = {10,50,10,50};   // 停止 开启 停止 开启   
//				vibrator.vibrate(pattern,-1);           //重复两次上面的pattern 如果只想震动一次，index设为-1   

				Intent intent = new Intent();
				intent.setClass(me, ScanCaptureAct.class);
				startActivityForResult(intent, 30);

			} catch (Exception e) {
				Toast.makeText(me, "相机打开失败,请检查相机是否可正常使用", Toast.LENGTH_LONG).show();
			}

		}

	 };

	 /**
	  * 扫描结果的处理
	  * onActivityResult 实现传值
	  */
	 public void onActivityResult(int requestCode, int resultCode, Intent intent) {

		 switch (requestCode) {
		 case 30:
			 if (resultCode == RESULT_OK) {

				 long [] pattern = {50,100,50,100};   // 停止 开启 停止 开启   
				 vibrator.vibrate(pattern,-1);           //重复两次上面的pattern 如果只想震动一次，index设为-1   

				 String contents = intent.getStringExtra("SCAN_RESULT");			
				 Intent intent2=new Intent(MainActivity.this, BrowserActivity.class);
				 intent2.putExtra("url", contents);
				 startActivity(intent2);

			 } else if (resultCode == RESULT_CANCELED) {

				 Toast.makeText(me, "扫描失败", Toast.LENGTH_SHORT).show();
			 }
			 break;
		 }

	 }

	 @Override  
	 /**
	  * 重写返回键的动作
	  */
	 public boolean onKeyDown(int keyCode, KeyEvent event){  
		 if (keyCode == KeyEvent.KEYCODE_BACK) {  
			 Intent MyIntent = new Intent(Intent.ACTION_MAIN); 
			 MyIntent.addCategory(Intent.CATEGORY_HOME); 
			 startActivity(MyIntent); 
			 finish();
			 return true;  
		 }
		 else  
			 return super.onKeyDown(keyCode, event);
	 };  
}
