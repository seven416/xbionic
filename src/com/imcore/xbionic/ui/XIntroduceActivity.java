package com.imcore.xbionic.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.imcore.xbionic.R;

public class XIntroduceActivity extends Activity implements OnClickListener {
	private ImageButton btnBack;
	private ImageView ivHistory,ivAwards,ivPrototype,ivBase,ivDesign;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_x_introduce);
		
		initWidget();
	}
	
	private void initWidget() {
		btnBack = (ImageButton) findViewById(R.id.btn_x_introduce_back);
		ivHistory = (ImageView) findViewById(R.id.iv_history_img);
		ivAwards = (ImageView) findViewById(R.id.iv_awards_img);
		ivPrototype = (ImageView) findViewById(R.id.iv_prototype_img);
		ivBase = (ImageView) findViewById(R.id.iv_base_img);
		ivDesign = (ImageView) findViewById(R.id.iv_design_img);
		
		btnBack.setOnClickListener(this);
		ivHistory.setOnClickListener(this);
		ivAwards.setOnClickListener(this);
		ivPrototype.setOnClickListener(this);
		ivBase.setOnClickListener(this);
		ivDesign.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_x_introduce_back:
			finish();
			break;

		case R.id.iv_history_img:
			//
			break;
		case R.id.iv_awards_img:
			Intent intentAwards = new Intent(XIntroduceActivity.this,XIntroduceAwardsActivity.class);
			startActivity(intentAwards);
			break;
			
		case R.id.iv_prototype_img:
			Intent intentPrototype = new Intent(XIntroduceActivity.this,XIntroducePrototypeActivity.class);
			startActivity(intentPrototype);
			break;
		case R.id.iv_base_img:
			//
			break;
		case R.id.iv_design_img:
			//
			break;
		}
	}

}
