package com.imcore.xbionic.ui;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.imcore.xbionic.R;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.util.JsonUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class TribeLogin extends Activity implements OnClickListener {

	private EditText mUser, mPsw;
	private ImageButton btnGetBack, btnLoginTribe, btnForgetPsw;
	ProgressDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tribe_login);

		initWidget();
	}

	private void initWidget() {
		mUser = (EditText) findViewById(R.id.et_user);
		mPsw = (EditText) findViewById(R.id.et_password);
		btnGetBack = (ImageButton) findViewById(R.id.btn_getback);
		btnLoginTribe = (ImageButton) findViewById(R.id.btn_login_tribe);
		btnForgetPsw = (ImageButton) findViewById(R.id.btn_forget_password);

		mPsw.setOnClickListener(this);
		mUser.setOnClickListener(this);
		btnGetBack.setOnClickListener(this);
		btnLoginTribe.setOnClickListener(this);
		btnForgetPsw.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_getback:
			finish();
			break;
		case R.id.btn_login_tribe:
			doLogin();
			break;
		case R.id.btn_forget_password:

			break;

		}

	}

	@SuppressWarnings("unused")
	private void doLogin() {
		final String userName = mUser.getText().toString().trim();
		final String password = mPsw.getText().toString().trim();

		mDialog = ProgressDialog.show(TribeLogin.this, "请稍候", "正在连接服务器...",
				true);

		String url = Constant.HOST + "/passport/login.do";
		DataRequest request = new DataRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// 解析用户信息的json，保存userid和token
						onResponseForLogin(response);

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("sign", error.getMessage());
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				// 在此方法中设置要提交的请求参数
				Map<String, String> params = new HashMap<String, String>();
				params.put("phoneNumber", userName);
				params.put("password", password);
				
				Log.i("msg", userName + "----" + password);
				
				params.put("client", "android");

				return params;
			}
		};

		RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
	}

	private void onResponseForLogin(String response) {
		String userAdd = JsonUtil.getJsonValueByKey(response, "userAddress");
		String userId = null;
		JSONObject jo;

		try {
			jo = new JSONObject(userAdd);
			userId = jo.getString("userId");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		
		String token = JsonUtil.getJsonValueByKey(response, "token");
		String firstName = JsonUtil.getJsonValueByKey(response, "firstName");
		String lastName = JsonUtil.getJsonValueByKey(response, "lastName");
		String userName = lastName + firstName;

		SharedPreferences preferences = getSharedPreferences("loginUser",
				Context.MODE_PRIVATE);// 私有数据
		
		Editor editor = preferences.edit();// 获取编辑器
		editor.putString("userId", userId);
		editor.putString("token", token);
		editor.putString("userName", userName);
		editor.putBoolean("isLogin", true);
		
		editor.commit();//提交修改
		
		mDialog.cancel();
		
		Intent intent = new Intent(this, HomeActivityLogin.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);// 清除栈内其他activity
		startActivity(intent);
	}

}
