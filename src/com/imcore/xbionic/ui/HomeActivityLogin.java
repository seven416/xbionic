package com.imcore.xbionic.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.imcore.xbionic.R;
import com.imcore.xbionic.util.Const;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class HomeActivityLogin extends FragmentActivity {

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ListView mDrawerList;
	private int[] mNaviItemIcon;
	private String[] mNaviItemText;
	private Fragment mHomeFragment;
	private Fragment mHomeFragmentUser;
	private View mDrawerView;

	private final static String NAVI_ITEM_TEXT = "text";
	private final static String NAVI_ITEM_ICON = "icon";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_login);

		initDrawerLayout();// 侧拉菜单
		initFragment();

		// 注册广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("ACTION_OPEN_DRAWER");
		registerReceiver(openDrawerReceiver, intentFilter);
	}

	private void initFragment() {
		mHomeFragment = new HomeFragment();
		mHomeFragmentUser = new HomeDrawerUser();
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putBoolean(Const.IS_LOGIN_KEY, Const.LOGIN);
		mHomeFragment.setArguments(bundle);
		ft.add(R.id.home_activity_login_fragment, mHomeFragment);
		ft.add(R.id.drawer_fragment_user, mHomeFragmentUser);
		ft.commit();
	}

	private void initDrawerLayout() {
		mNaviItemText = getResources().getStringArray(
				R.array.drawer_item_array_text);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.home_login_layout);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		mDrawerView = findViewById(R.id.left_drawer);
		mNaviItemIcon = new int[] { R.drawable.ic_launcher,
				R.drawable.ic_launcher, R.drawable.ic_launcher,
				R.drawable.ic_launcher, R.drawable.ic_launcher,
				R.drawable.ic_launcher, R.drawable.ic_launcher,
				R.drawable.ic_launcher, R.drawable.ic_launcher };
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < mNaviItemText.length; i++) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put(NAVI_ITEM_TEXT, mNaviItemText[i]);
			item.put(NAVI_ITEM_ICON, mNaviItemIcon[i]);
			data.add(item);
		}

		String[] from = new String[] { NAVI_ITEM_TEXT, NAVI_ITEM_ICON };
		int[] to = new int[] { R.id.tv_navi_item_text, R.id.iv_navi_item_icon };
		mDrawerList = (ListView) findViewById(R.id.lv_drawer_list);
		mDrawerList.setAdapter(new SimpleAdapter(this, data,
				R.layout.view_navi_drawer_item, from, to));
		mDrawerList
				.setOnItemClickListener(new NaviDrawerListItemOnClickListner());

	}
	
	//接收广播
		private BroadcastReceiver openDrawerReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if(intent.getAction().equals("ACTION_OPEN_DRAWER")){
					mDrawerLayout.openDrawer(mDrawerView);
				}
			}
		};

	private class NaviDrawerListItemOnClickListner implements
			OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectNaviItem(position);
			mDrawerLayout.closeDrawers();
		}
	}

	private void selectNaviItem(int position) {

		switch (position) {
		case 0:
			//
			break;
		case 1:
			//
			break;
		case 2:
			//
			break;
		case 3:
			//
			break;
		case 4:
			//
			break;
		case 5:
			//
			break;
		case 6:
			//
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		unregisterReceiver(openDrawerReceiver);
		super.onDestroy();
	}

}
