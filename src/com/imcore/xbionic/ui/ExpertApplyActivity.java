package com.imcore.xbionic.ui;

import com.imcore.xbionic.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpertApplyActivity extends Activity implements OnClickListener {
	
	private ImageView mBack;
	private TextView mSignOut;
	private TextView mLastName, mFirstName, mSex, mEmail, mProvince, mAddress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expert_apply);mBack = (ImageView) findViewById(R.id.iv_account_reset_back);
		mSignOut = (TextView) findViewById(R.id.tv_account_reset_signout);
		mLastName = (TextView) findViewById(R.id.tv_acc_reset_info_lastname);
		mFirstName = (TextView) findViewById(R.id.tv_acc_reset_info_firstname);
		mSex = (TextView) findViewById(R.id.tv_acc_reset_info_sex);
		mEmail = (TextView) findViewById(R.id.tv_acc_reset_info_email);
		mProvince = (TextView) findViewById(R.id.tv_acc_reset_address_province);
		mAddress = (TextView) findViewById(R.id.tv_acc_reset_address);
		
		SharedPreferences sp = getSharedPreferences("userInfo",
				Context.MODE_PRIVATE); // 私有数据
		initWidget(sp);
		
		mBack.setOnClickListener(this);
	}
	
	private void initWidget(SharedPreferences sp) {
		mLastName.setText(sp.getString("lastName", ""));
		mFirstName.setText(sp.getString("firstName", ""));
		mEmail.setText(sp.getString("email", ""));
		mProvince.setText(sp.getString("address", ""));
		mAddress.setText(sp.getString("address", ""));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_account_reset_back:// 回退
			finish();
			break;

		case R.id.tv_account_reset_signout:// 注销账户
			//
			break;

		case R.id.rel_account_reset_baseinfromation:// 基本资料

			break;

		case R.id.rel_account_reset_address:// 地址信息

			break;

		}
		
	}

}
