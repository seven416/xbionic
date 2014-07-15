package com.imcore.xbionic.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.imcore.xbionic.R;

public class NewUserEnroll extends Activity implements OnClickListener {
	
	private ImageButton btnBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_user_enroll);
		
		btnBack = (ImageButton) findViewById(R.id.btn_backtrack);
		btnBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_backtrack:
			finish();
			break;

		}
		
	}

}
