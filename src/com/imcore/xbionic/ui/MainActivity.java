package com.imcore.xbionic.ui;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.imcore.xbionic.R;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;

public class MainActivity extends Activity implements OnClickListener {
	private ImageButton btnTribeLogin;
	private ImageButton btnSinaLogin;
	private ImageButton btnQQLogin;
	private ImageButton btnRegister;
	private ImageButton btnHelp;
	private ImageButton btnServer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initControl();

	}

	private void initControl() {
		btnTribeLogin = (ImageButton) findViewById(R.id.btn_tribe_login);
		btnSinaLogin = (ImageButton) findViewById(R.id.btn_sina_login);
		btnQQLogin = (ImageButton) findViewById(R.id.btn_qq_login);
		btnRegister = (ImageButton) findViewById(R.id.btn_register);
		btnHelp = (ImageButton) findViewById(R.id.btn_help);
		btnServer = (ImageButton) findViewById(R.id.btn_serve);

		btnTribeLogin.setOnClickListener(this);
		btnSinaLogin.setOnClickListener(this);
		btnQQLogin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		btnHelp.setOnClickListener(this);
		btnServer.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sina_login:
			
			Intent intent1 = new Intent(MainActivity.this,SinaLogin.class);
			startActivity(intent1);

			break;
		case R.id.btn_qq_login:
			Intent intent2 = new Intent(MainActivity.this,QQLogin.class);
			startActivity(intent2);
			break;

		case R.id.btn_tribe_login:
			
			Intent intent3 = new Intent(MainActivity.this,TribeLogin.class);
			startActivity(intent3);

			break;

		case R.id.btn_register:
			Intent intent4 = new Intent(MainActivity.this,NewUserEnroll.class);
			startActivity(intent4);
			break;

		case R.id.btn_help:
               //
			break;

		case R.id.btn_serve:
                //
			break;

		}

	}


	@SuppressWarnings("unused")
	private void doLogin() {
		final String userName = "";
		final String password = "";

		String url = Constant.HOST + "/passport/login";
		DataRequest request = new DataRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// 解析用户信息的json，保存userid和token

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub

					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				// 在此方法中设置要提交的请求参数
				Map<String, String> params = new HashMap<String, String>();
				params.put("phoneNumber", userName);
				params.put("password", password);

				return params;
			}
		};

		RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
