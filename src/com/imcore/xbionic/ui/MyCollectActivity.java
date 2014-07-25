package com.imcore.xbionic.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.imcore.xbionic.R;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.model.ProductCollect;
import com.imcore.xbionic.util.JsonUtil;
import com.imcore.xbionic.util.ToastUtil;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MyCollectActivity extends Activity implements OnClickListener {
	
	private Button btnBack;
	private ListView mCollectList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_collect);
		btnBack = (Button) findViewById(R.id.btn_my_collect_back);
		mCollectList = (ListView) findViewById(R.id.lv_my_collect_list);
		btnBack.setOnClickListener(this);
		getCollectInfo();
		ExitApplication.getInstance().addActivity(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_my_collect_back) {
			finish();
		}
	}

	private String userId, token;

	private void getCollectInfo() {
		SharedPreferences sp = getSharedPreferences("loginUser",
				Context.MODE_PRIVATE);
		userId = sp.getString("userId", "");
		token = sp.getString("token", "");
		String url = Constant.HOST + "/user/favorite/list.do";
		DataRequest request = new DataRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// 解析用户信息的json，保存userid和token
						// String responseData = JsonUtil.getJsonValueByKey(
						// response, "data");
						onResponseForCollect(response);
						Log.i("==============", response);
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
				params.put("token", token);
				params.put("userId", userId);
				params.put("type", 1 + "");
				params.put("offset", 0 + "");
				params.put("fetchSize", 10 + "");
				return params;
			}
		};

		RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
	}

	private ArrayList<ProductCollect> mCollectArray;

	private void onResponseForCollect(String response) {
		mCollectArray = new ArrayList<ProductCollect>();
		ArrayList<String> arr = (ArrayList<String>) JsonUtil
				.toJsonStrList(response);
		for (String json : arr) {
			ProductCollect pc = new ProductCollect();
			String product = JsonUtil.getJsonValueByKey(json, "product");
			pc.imageUrl = JsonUtil.getJsonValueByKey(product, "imageUrl");
			String sysColor = JsonUtil.getJsonValueByKey(product, "sysColor");
			pc.color = JsonUtil.getJsonValueByKey(sysColor, "color");
			pc.price = JsonUtil.getJsonValueByKey(product, "price");
			pc.name = JsonUtil.getJsonValueByKey(product, "name");
			pc.addDate = JsonUtil.getJsonValueByKey(json, "addDate");
			pc.id = JsonUtil.getJsonValueByKey(json, "id");
			mCollectArray.add(pc);
		}
		mVhArray = new ArrayList<ViewHolder>();
		mCollectList.setAdapter(collectAdapter);
	}

	private ArrayList<ViewHolder> mVhArray;

	class ViewHolder {
		String id;
		ImageView img;
		TextView title;
		TextView color;
		TextView price;
		TextView addDate;
		ImageView remove;
	}

	private void setImage(ImageView image, String url) {
		ImageLoader loader = RequestQueueSingleton.getInstance(
				getApplicationContext()).getImageLoader();
		ImageListener listener = ImageLoader.getImageListener(image,
				R.drawable.test1, R.drawable.test1);
		loader.get(url, listener, 400, 400);
	}

	private BaseAdapter collectAdapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewHolder vh = null;
			if (view == null) {
				vh = new ViewHolder();
				view = getLayoutInflater().inflate(R.layout.view_my_collect,
						null);
				vh.img = (ImageView) view.findViewById(R.id.iv_my_collect_img);
				vh.title = (TextView) view
						.findViewById(R.id.tv_my_collect_title);
				vh.color = (TextView) view
						.findViewById(R.id.tv_my_collect_color);
				vh.price = (TextView) view
						.findViewById(R.id.tv_my_collect_total);
				vh.addDate = (TextView) view
						.findViewById(R.id.tv_my_collect_date);
				vh.remove = (ImageView) view
						.findViewById(R.id.iv_my_collect_remove);
				view.setTag(vh);
			} else {
				vh = (ViewHolder) view.getTag();
			}
			// vh.title.setText(mCollectArray.get(position).name);
			// vh.color.setText(mCollectArray.get(position).color);
			// vh.price.setText(mCollectArray.get(position).price);
			vh.addDate.setText(mCollectArray.get(position).addDate);
			vh.id = mCollectArray.get(position).id;
			String url = Constant.IMAGE_ADDRESS
					+ mCollectArray.get(position).imageUrl + "_L.jpg";
			setImage(vh.img, url);
			if (mVhArray.indexOf(vh) == -1) {
				mVhArray.add(vh);
			}
			vh.remove.setOnClickListener(new vhImgOnClickListener(vh));
			return view;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public Object getItem(int position) {
			return mCollectArray.get(position);
		}

		@Override
		public int getCount() {
			return mCollectArray.size();
		}
	};

	private class vhImgOnClickListener implements OnClickListener {

		private ViewHolder vh;

		vhImgOnClickListener(ViewHolder vh) {
			this.vh = vh;
		}

		@Override
		public void onClick(View v) {
			for (ViewHolder vh : mVhArray) {
				if (v.getId() == vh.remove.getId()) {
					removeCollect(vh);
					return;
				}
			}
		}
	}

	// 删除商品
	private void removeCollect(ViewHolder vh) {
		String url = Constant.HOST + "/user/favorite/delete.do";
		final ViewHolder v = vh;
		DataRequest request = new DataRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// 解析用户信息的json，保存userid和token
						ToastUtil.showToast(MyCollectActivity.this, "删除成功");
						getCollectInfo();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				// 在此方法中设置要提交的请求参数
				Map<String, String> params = new HashMap<String, String>();
				params.put("id", v.id + "");
				params.put("type", 1 + "");
				return params;
			}
		};
		RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
	}

}
