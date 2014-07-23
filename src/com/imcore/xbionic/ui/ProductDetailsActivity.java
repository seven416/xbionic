package com.imcore.xbionic.ui;

import com.imcore.xbionic.R;
import com.imcore.xbionic.R.id;
import com.imcore.xbionic.util.Const;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class ProductDetailsActivity extends FragmentActivity implements OnClickListener {
	private long commodityId;
	private ImageButton btnBack,btnMenu;
	private DrawerLayout mDrawerLayout;
	private View mDrawerView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commodity_detail);
		
		btnBack = (ImageButton) findViewById(R.id.btn_commodity_detail_back);
		btnMenu = (ImageButton) findViewById(R.id.btn_commodity_detail_menu);
		btnBack.setOnClickListener(this);
		btnMenu.setOnClickListener(this);
		
		Intent intent = getIntent();
		commodityId = intent.getLongExtra(Const.PRODUCT_DETAIL_FRAGMENT_KEY, 0);
		//Log.i("sign", commodityId + "");
		
		initProductDetailFragment();
		initDrawerLayout();
		
	}
	
	// 初始化界面信息
	private void initProductDetailFragment() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment imgFragment = new FragmentProductDetailImg();
		Fragment infoFragment = new FragmentProductDetailInfo();
		Fragment sizeFragment = new FragmentProductDetailSize();
		Fragment techFragment = new FragmentProductDetailTech();
		Fragment DarwerFragment = new FragmentProductDetailDarwer();
		ft.add(R.id.fragment_commodity_detail_img, imgFragment);
		ft.add(R.id.fragment_commodity_detail_info, infoFragment);
		ft.add(R.id.fragment_commodity_detail_size, sizeFragment);
		ft.add(R.id.fragment_commodity_detail_tech, techFragment);
		ft.add(R.id.frg_drawer_product_detail, DarwerFragment);
		
		Bundle bundle = new Bundle();
		bundle.putLong(Const.PRODUCT_DETAIL_FRAGMENT_KEY, commodityId);
		imgFragment.setArguments(bundle);
		infoFragment.setArguments(bundle);
		sizeFragment.setArguments(bundle);
		techFragment.setArguments(bundle);
		DarwerFragment.setArguments(bundle);
		
		ft.addToBackStack(null).commit();

	}
	
	// 初始化侧拉菜单
		public void initDrawerLayout() {

			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_product_detail_layout);
			mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
					GravityCompat.START);
			mDrawerView = findViewById(R.id.rel_drawer_product_detail_drawer);

		}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_commodity_detail_back:
			finish();
			break;

		case R.id.btn_commodity_detail_menu:
			mDrawerLayout.openDrawer(mDrawerView);
			break;
		}
	}

}
