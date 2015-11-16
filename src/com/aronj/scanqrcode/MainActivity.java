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
	 * ���ɨ����¼�����
	 */
	 public Button.OnClickListener ScanOcl = new Button.OnClickListener() {
		@Override
		public void onClick(View arg0) {

			try {
//
//				long [] pattern = {10,50,10,50};   // ֹͣ ���� ֹͣ ����   
//				vibrator.vibrate(pattern,-1);           //�ظ����������pattern ���ֻ����һ�Σ�index��Ϊ-1   

				Intent intent = new Intent();
				intent.setClass(me, ScanCaptureAct.class);
				startActivityForResult(intent, 30);

			} catch (Exception e) {
				Toast.makeText(me, "�����ʧ��,��������Ƿ������ʹ��", Toast.LENGTH_LONG).show();
			}

		}

	 };

	 /**
	  * ɨ�����Ĵ���
	  * onActivityResult ʵ�ִ�ֵ
	  */
	 public void onActivityResult(int requestCode, int resultCode, Intent intent) {

		 switch (requestCode) {
		 case 30:
			 if (resultCode == RESULT_OK) {

				 long [] pattern = {50,100,50,100};   // ֹͣ ���� ֹͣ ����   
				 vibrator.vibrate(pattern,-1);           //�ظ����������pattern ���ֻ����һ�Σ�index��Ϊ-1   

				 String contents = intent.getStringExtra("SCAN_RESULT");			
				 Intent intent2=new Intent(MainActivity.this, BrowserActivity.class);
				 intent2.putExtra("url", contents);
				 startActivity(intent2);

			 } else if (resultCode == RESULT_CANCELED) {

				 Toast.makeText(me, "ɨ��ʧ��", Toast.LENGTH_SHORT).show();
			 }
			 break;
		 }

	 }

	 @Override  
	 /**
	  * ��д���ؼ��Ķ���
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
