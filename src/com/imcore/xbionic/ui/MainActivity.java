package com.imcore.xbionic.ui;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
	private Button btnTribeLogin;
	private Button btnSinaLogin;
	private Button btnQQLogin;
	private Button btnRegister;
	private ImageButton btnHelp;
	private ImageButton btnServer;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initWidget();

	}

	private void initWidget() {
		btnTribeLogin = (Button) findViewById(R.id.btn_tribe_login);
		btnSinaLogin = (Button) findViewById(R.id.btn_sina_login);
		btnQQLogin = (Button) findViewById(R.id.btn_qq_login);
		btnRegister = (Button) findViewById(R.id.btn_register);
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


	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
