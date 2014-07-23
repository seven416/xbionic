package com.imcore.xbionic.ui;

import com.imcore.xbionic.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

public class XIntroduceActivity extends Activity implements OnClickListener {
	private ImageButton btnBack;
	private ImageView ivHistory,ivAwards,ivPrototype,ivBase,ivDesign;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_x_introduce);
	}

	@Override
	public void onClick(View v) {
		//
	}

}
