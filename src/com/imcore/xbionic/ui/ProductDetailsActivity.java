package com.imcore.xbionic.ui;

import com.imcore.xbionic.R;
import com.imcore.xbionic.util.Const;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class ProductDetailsActivity extends FragmentActivity implements OnClickListener {
	private long commodityId;
	private ImageButton btnBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commodity_detail);
		
		btnBack = (ImageButton) findViewById(R.id.btn_commodity_detail_back);
		btnBack.setOnClickListener(this);
		
		Intent intent = getIntent();
		commodityId = intent.getLongExtra(Const.PRODUCT_DETAIL_FRAGMENT_KEY, 0);
		
		initProductDetailFragment();
		
	}
	
	// 初始化界面信息
	private void initProductDetailFragment() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment imgFragment = new FragmentProductDetailImg();
		Fragment infoFragment = new FragmentProductDetailInfo();
		Fragment sizeFragment = new FragmentProductDetailSize();
		Fragment techFragment = new FragmentProductDetailTech();
		ft.add(R.id.fragment_commodity_detail_img, imgFragment);
		ft.add(R.id.fragment_commodity_detail_info, infoFragment);
		ft.add(R.id.fragment_commodity_detail_size, sizeFragment);
		ft.add(R.id.fragment_commodity_detail_tech, techFragment);
		
		Bundle bundle = new Bundle();
		bundle.putLong(Const.PRODUCT_DETAIL_FRAGMENT_KEY, commodityId);
		imgFragment.setArguments(bundle);
		infoFragment.setArguments(bundle);
		sizeFragment.setArguments(bundle);
		techFragment.setArguments(bundle);
		
		ft.addToBackStack(null).commit();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_commodity_detail_back:
			finish();
			break;

		default:
			break;
		}
	}

}
