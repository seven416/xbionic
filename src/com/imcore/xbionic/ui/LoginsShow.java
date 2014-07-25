package com.imcore.xbionic.ui;

import com.imcore.xbionic.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LoginsShow extends Activity {
	private ImageView ImgBackground;
	
	private Handler h = new Handler();

	private SharedPreferences preferences;
	private Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logins_show);
		
		ImgBackground = (ImageView) findViewById(R.id.iv_bg);
		transition();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		preferences = getSharedPreferences("count", Context.MODE_PRIVATE);

		if (hasFocus) {
			h.postDelayed(new Runnable() {

				int count = preferences.getInt("count", 0);

				@Override
				public void run() {
					if (count == 0) {
						Intent intent = new Intent(LoginsShow.this,
								FirstGuide.class);
						finish();
						startActivity(intent);
						
						
						editor = preferences.edit();
						// 存入数据
						editor.putInt("count", ++count);
						// 提交修改
						editor.commit();
					} else {
						Intent intent = new Intent(LoginsShow.this,
								MainActivity.class);
						
//						Intent intent = new Intent(LoginsShow.this,
//								TribeLogin.class);
						finish();
						startActivity(intent);
						
					}
					

				}

			}, 2000);
		}
	}
	
	private void transition() {
		Animation animation = AnimationUtils.loadAnimation(this,
				R.anim.shanping_animation);
		ImgBackground.startAnimation(animation);
	}

}
