package com.imcore.xbionic.ui;

import com.imcore.xbionic.R;
import com.imcore.xbionic.util.ToastUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeDrawerUser extends Fragment implements OnClickListener {
	
	private View mFragmentView;
	private ImageView mHeadImg, mModifyImg;
	private TextView mUserName;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mFragmentView = inflater.inflate(R.layout.view_navi_drawer_userdetail,
				null);
		mHeadImg = (ImageView) mFragmentView
				.findViewById(R.id.iv_drawer_user_img);
		mModifyImg = (ImageView) mFragmentView
				.findViewById(R.id.iv_drawer_user_modify);
		mUserName = (TextView) mFragmentView
				.findViewById(R.id.tv_drawer_user_name);
		SharedPreferences sp = getActivity().getSharedPreferences("loginUser",
				Context.MODE_PRIVATE); // 私有数据
		String userName = sp.getString("userName", "用户,你好");
		mUserName.setText(userName + ",你好");

		mHeadImg.setOnClickListener(this);
		mModifyImg.setOnClickListener(this);

		return mFragmentView;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.iv_drawer_user_img
				|| v.getId() == R.id.iv_drawer_user_modify) {
			ToastUtil.showToast(getActivity(), "修改用户信息");
		}
	}

}
