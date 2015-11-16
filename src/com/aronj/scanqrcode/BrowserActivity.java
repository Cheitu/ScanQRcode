
package com.aronj.scanqrcode;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


@SuppressLint("SetJavaScriptEnabled")
public class BrowserActivity extends Activity{

	private WebView webView;//webView�ؼ�����ʵ����Ƕ���������Ҫ�ؼ�

	private ProgressDialog mPgrogressDialog;//�����򣬣�������ʾ

	private Button btn_left;
	private ImageButton btn_right;
	private TextView title_btn;
	private WebChromeClient wvcc; 

	private String url = "http://www.baidu.com";

	public void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		setContentView(R.layout.browser);

		initFindById();//��ʼ���ؼ�
		//��ȡ���ݵĲ���
		Intent it = getIntent();
		String u = it.getStringExtra("url");
		if(!TextUtils.isEmpty(u)){
			url = u;
		}
		init();//������ҳ��ʼ��
		addEvents();//����¼�

	}


	/**
	 * ��ʼ���ؼ�
	 */
	public void initFindById(){
		btn_left = (Button) this.findViewById(R.id.nav_left_btn);
		btn_right =  (ImageButton) this.findViewById(R.id.nav_right_btn);
		title_btn =(TextView)this.findViewById(R.id.nav_center_btn);
		webView = (WebView) findViewById(R.id.webView);//ʵ�����ؼ�

		btn_left.setVisibility(View.VISIBLE);
		btn_right.setVisibility(View.VISIBLE);
		
		wvcc = new WebChromeClient(){
            @Override  
            public void onReceivedTitle(WebView view, String title) {  
                super.onReceivedTitle(view, title);  
                title_btn.setText(title);  
            }
		};
	}

	/**
	 * ����¼�
	 */
	public void addEvents(){
		//�����¼�
		btn_left.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				Intent intent=new Intent(BrowserActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		
		btn_left.setOnTouchListener(new ImageButton.OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){   
					v.setBackgroundResource(R.drawable.close_brower);   

				}   
				else if(event.getAction() == MotionEvent.ACTION_UP){   
					v.setBackgroundResource(R.drawable.close_brower_an1); 
					
				} 
				return false;
			}
		});
		
		
	}

	/**
	 * ������ҳ
	 */
	private void init(){

		// ��ʾ��ҳ�����е���ʾ��
		mPgrogressDialog =  new ProgressDialog(this);
		mPgrogressDialog.setTitle("");
		mPgrogressDialog.setMessage("���ڼ�����ҳ...");
		mPgrogressDialog.show();

		//���úͿ���һЩwebview��Ȩ�޺ͼ�����
		webView.getSettings().setJavaScriptEnabled(true);//������js������Ȩ��
		webView.getSettings().setDefaultTextEncodingName("gbk");//��ֹ��������
		webView.getSettings().setGeolocationEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.setDownloadListener(new MyDownloadListener());//����webview����������ҳ�����ص�ַ�����õ������������
//		webView.setBackgroundResource(R.drawable.brower);//����webview�ı���
//        webView.setBackgroundColor(Color.argb(0, 0, 0, 0));//����͸��

		//WebView����web��Դ
		webView.loadUrl(url);
		//ˢ��ҳ����¼�
//		btn_right.setOnClickListener(new OnClickListener()
//		{  
//			@Override
//			public void onClick(View v)
//			{
//				mPgrogressDialog.show();
//				webView.loadUrl(url);
//			}
//		});

		webView.setWebChromeClient(wvcc); 
		
		//webView�ļ����������еķ���������д��ʵ���Լ�������
		webView.setWebViewClient(new WebViewClient(){

			/**
			 * ����WebViewĬ��ʹ�õ�������ϵͳĬ�����������ҳ����Ϊ��ʹ��ҳ��WebView��
			 */
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {			
				//����ֵ��true��ʱ�����ȥWebView�򿪣�Ϊfalse����ϵͳ�����������������
				view.loadUrl(url);
				return true;
			}

			/**
			 * ҳ����Ⱦ�������¼�
			 * �ر���ʾ��
			 */
			@Override
			public void onPageFinished(WebView view, String url){ 
				super.onPageFinished(view, url); 
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						mPgrogressDialog.dismiss();
					}
				});

			} 

			/**
			 * ҳ�濪ʼ��Ⱦ����¼�����
			 */
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon)  
			{ 
				super.onPageStarted(view, url, favicon); 
			} 
		});
	}

	/**
	 * �ڲ���
	 * 
	 * ʵ��webview�����ع���
	 * ����Ǽ򵥵ĵ������õ������
	 * ʵ�����أ���Ҫʵ��webview��������أ���Ҫ��д����չ���������
	 * @author dengjd
	 *
	 */
	public class MyDownloadListener implements DownloadListener{

		@Override
		public void onDownloadStart(String url, String userAgent,
				String contentDisposition, String mimetype, long contentLength) {

			Uri uri = Uri.parse(url);  
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
			startActivity(intent);  
		}

	}

	/**
	 * ��дϵͳ�ķ��ؼ�
	 * 
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			if(webView.canGoBack())
			{
				webView.goBack();//������һҳ��
				return true;
			}
			else
			{
				System.exit(0);//�˳�����
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
