
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

	private WebView webView;//webView控件，，实现内嵌浏览器的主要控件

	private ProgressDialog mPgrogressDialog;//弹出框，，用作提示

	private Button btn_left;
	private ImageButton btn_right;
	private TextView title_btn;
	private WebChromeClient wvcc; 

	private String url = "http://www.baidu.com";

	public void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		setContentView(R.layout.browser);

		initFindById();//初始化控件
		//获取传递的参数
		Intent it = getIntent();
		String u = it.getStringExtra("url");
		if(!TextUtils.isEmpty(u)){
			url = u;
		}
		init();//加载网页初始化
		addEvents();//添加事件

	}


	/**
	 * 初始化控件
	 */
	public void initFindById(){
		btn_left = (Button) this.findViewById(R.id.nav_left_btn);
		btn_right =  (ImageButton) this.findViewById(R.id.nav_right_btn);
		title_btn =(TextView)this.findViewById(R.id.nav_center_btn);
		webView = (WebView) findViewById(R.id.webView);//实例化控件

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
	 * 添加事件
	 */
	public void addEvents(){
		//返回事件
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
	 * 加载网页
	 */
	private void init(){

		// 显示网页加载中的提示框
		mPgrogressDialog =  new ProgressDialog(this);
		mPgrogressDialog.setTitle("");
		mPgrogressDialog.setMessage("正在加载网页...");
		mPgrogressDialog.show();

		//设置和开启一些webview的权限和监听器
		webView.getSettings().setJavaScriptEnabled(true);//开启与js交互的权限
		webView.getSettings().setDefaultTextEncodingName("gbk");//防止中文乱码
		webView.getSettings().setGeolocationEnabled(true);
		webView.getSettings().setDomStorageEnabled(true);
		webView.setDownloadListener(new MyDownloadListener());//设置webview可以下载网页的下载地址，调用第三发的浏览器
//		webView.setBackgroundResource(R.drawable.brower);//设置webview的背景
//        webView.setBackgroundColor(Color.argb(0, 0, 0, 0));//背景透明

		//WebView加载web资源
		webView.loadUrl(url);
		//刷新页面的事件
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
		
		//webView的监听（对其中的方法进行重写，实现自己的需求）
		webView.setWebViewClient(new WebViewClient(){

			/**
			 * 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
			 */
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {			
				//返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
				view.loadUrl(url);
				return true;
			}

			/**
			 * 页面渲染结束的事件
			 * 关闭提示框
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
			 * 页面开始渲染后的事件监听
			 */
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon)  
			{ 
				super.onPageStarted(view, url, favicon); 
			} 
		});
	}

	/**
	 * 内部类
	 * 
	 * 实现webview的下载功能
	 * 这边是简单的调用内置的浏览器
	 * 实现下载（若要实现webview自身的下载，需要重写和扩展这个方法）
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
	 * 重写系统的返回键
	 * 
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			if(webView.canGoBack())
			{
				webView.goBack();//返回上一页面
				return true;
			}
			else
			{
				System.exit(0);//退出程序
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
