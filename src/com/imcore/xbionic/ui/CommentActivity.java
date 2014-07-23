package com.imcore.xbionic.ui;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.imcore.xbionic.R;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.util.Const;
import com.imcore.xbionic.util.ToastUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CommentActivity extends Activity implements OnClickListener {

	private Button btnBack, btnSubmit;
	private EditText etTitle, etContent, etStars;
	private long id;// 产品id

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);

		btnBack = (Button) findViewById(R.id.btn_comment_back);
		btnSubmit = (Button) findViewById(R.id.btn_comment_submit);
		etTitle = (EditText) findViewById(R.id.et_comment_title);
		etContent = (EditText) findViewById(R.id.et_comment_content);
		etStars = (EditText) findViewById(R.id.et_comment_stars);
		btnBack.setOnClickListener(this);
		btnSubmit.setOnClickListener(this);
		id = this.getIntent().getLongExtra("id", 0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_comment_back:
			finish();
			break;
		case R.id.btn_comment_submit:
			addComment();
			break;
		}

	}

	private String userId;
	private String token;
	private String title;
	private String comment;
	private String stars;

	private void addComment() {
		SharedPreferences sp = getSharedPreferences("loginUser",
				Context.MODE_PRIVATE); // 私有数据
		userId = sp.getString("userId", "");
		token = sp.getString("token", "");
		if (userId.equals("") || token.equals("")) {
			new AlertDialog.Builder(this)
					.setTitle("您还未登陆，请先登陆.....")
					.setPositiveButton("登陆",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									Intent intent = new Intent(
											CommentActivity.this,
											TribeLogin.class);
									intent.putExtra(Const.LOGIN_KEY,
											Const.LOGIN_AT_BUY_VALUE);
									startActivity(intent);
								}
							}).create().show();
			return;
		}
		title = etTitle.getText().toString();
		comment = etContent.getText().toString();
		stars = etStars.getText().toString();
		if (comment == null || comment.equals("")) {
			ToastUtil.showToast(this, "评论内容不能为空");
			return;
		}

		String url = Constant.HOST + "/product/comments/add.do";
		DataRequest request = new DataRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						if (response != null) {
							// Log.i("Error", response);
							ToastUtil.showToast(CommentActivity.this, "发表成功");
							finish();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("Error", error.getMessage());
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				// 在此方法中设置要提交的请求参数
				Map<String, String> params = new HashMap<String, String>();
				params.put("userId", userId);
				params.put("token", token);
				params.put("id", id + "");
				params.put("comment", comment);
				params.put("star", stars);
				params.put("title", title);
				// Log.i("Error", productQuantity.id + "----" + sumBuy);
				return params;
			}
		};
		RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
	}

}
