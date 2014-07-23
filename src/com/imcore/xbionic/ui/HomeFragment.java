package com.imcore.xbionic.ui;

import com.imcore.xbionic.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

public class HomeFragment extends Fragment implements OnClickListener {
	private ImageButton btnList,btnSeek;
	private ImageView ivProductBuy, ivStory, ivXactivity, ivXintroduce;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, null);
		btnList = (ImageButton) view.findViewById(R.id.btn_listbutton);
		btnSeek = (ImageButton) view.findViewById(R.id.btn_seek);
		ivProductBuy = (ImageView) view.findViewById(R.id.iv_product_buy);
		ivStory = (ImageView) view.findViewById(R.id.iv_expert_story);
		ivXactivity = (ImageView) view.findViewById(R.id.iv_x_activity);
		ivXintroduce = (ImageView) view.findViewById(R.id.iv_x_introduce);
		
		
		btnList.setOnClickListener(this);
		btnSeek.setOnClickListener(this);
		ivProductBuy.setOnClickListener(this);
		ivStory.setOnClickListener(this);
		ivXactivity.setOnClickListener(this);
		ivXintroduce.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_listbutton:
			Intent intent = new Intent("ACTION_OPEN_DRAWER");
			getActivity().sendBroadcast(intent);
			break;
		case R.id.btn_seek:
			//
			break;
		case R.id.iv_product_buy:
			Intent intentBuy = new Intent(getActivity(),ProductBuyActivity.class);
			startActivity(intentBuy);
			break;
		case R.id.iv_expert_story:
			Intent intentStory = new Intent(getActivity(),ExpertStoryActivity.class);
			startActivity(intentStory);
			break;
		case R.id.iv_x_activity:
			//
			break;
		case R.id.iv_x_introduce:
			Intent intentIntroduce = new Intent(getActivity(),XIntroduceActivity.class);
			startActivity(intentIntroduce);
			break;
		}
		
	}

}
