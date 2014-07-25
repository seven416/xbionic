package com.imcore.xbionic.ui;

import com.imcore.xbionic.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class SettleAccountsActivity extends Activity implements OnClickListener {
	
	private Button btnBack;
	private ImageButton btnBuy,btnSubmit;
	private TextView tvIndentMoney,tvPayMoney;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orderconfirmation);
		initWidget();
		ExitApplication.getInstance().addActivity(this);
	}
	private void initWidget(){
		btnBack=(Button) findViewById(R.id.btn_settle_accounts_back);
		btnBuy=(ImageButton) findViewById(R.id.btn_continue_shopping);
		btnSubmit=(ImageButton) findViewById(R.id.btn_submit_indent);
		tvIndentMoney=(TextView) findViewById(R.id.tv_indent_money);
		tvPayMoney=(TextView) findViewById(R.id.tv_pay_money);
		btnBack.setOnClickListener(this);
		btnBuy.setOnClickListener(this);
		btnSubmit.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_settle_accounts_back:
			finish();
			break;
		case R.id.btn_continue_shopping:
			Intent intent = new Intent(SettleAccountsActivity.this,ProductBuyActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_submit_indent:
			new AlertDialog.Builder(SettleAccountsActivity.this)
			.setMessage("提交成功")
			.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							dialog.dismiss();
						}
					}).create().show();
			break;
		}
	}
}
