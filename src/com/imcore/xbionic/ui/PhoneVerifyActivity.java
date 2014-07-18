package com.imcore.xbionic.ui;

import com.imcore.xbionic.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class PhoneVerifyActivity extends Activity implements OnClickListener {
	
	private ImageButton btnBack;
	private Button btnAuth;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_verify);
		
		btnBack = (ImageButton) findViewById(R.id.btn_phone_verify_back);
		btnAuth = (Button) findViewById(R.id.btn_gain_auth);
		
		btnAuth.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_phone_verify_back:
			finish();
			break;
		case R.id.btn_gain_auth:
			//
			break;
		}
		
	}

}
