package com.imcore.xbionic.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import com.imcore.xbionic.model.ProductShopping;
import com.imcore.xbionic.util.JsonUtil;
import com.imcore.xbionic.util.ToastUtil;

public class ShoppingCartActivity extends Activity implements OnClickListener {
	
	private Button btnBack, btnPen, btnAccount;
	private EditText etCartInfo;
	private TextView tvMoney;
	private ListView mCartList;
	private boolean isVisible = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping_cart);
		
		initWidget();
		getShoppingInfo();
		ExitApplication.getInstance().addActivity(this);
	}

	

	// 初始化控件
	private void initWidget() {
		btnBack = (Button) findViewById(R.id.btn_account_reset_back);
		btnPen = (Button) findViewById(R.id.btn_account_reset_pen);
		btnAccount = (Button) findViewById(R.id.btn_shopping_cart_account);
		etCartInfo = (EditText) findViewById(R.id.et_shopping_cart_info);
		tvMoney = (TextView) findViewById(R.id.tv_shopping_cart_commodity_money);
		mCartList = (ListView) findViewById(R.id.lv_shopping_cart_list);

		btnBack.setOnClickListener(this);
		btnPen.setOnClickListener(this);
		btnAccount.setOnClickListener(this);
		
	}
	
	private String userId, token;
	
	//获取商品信息
	private void getShoppingInfo() {
		SharedPreferences sp = getSharedPreferences("loginUser",
				Context.MODE_PRIVATE);
		userId = sp.getString("userId", "");
		token = sp.getString("token", "");
		String url = Constant.HOST + "/shoppingcart/list.do";
		
		DataRequest request = new DataRequest(Request.Method.POST, url, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				onResponseForLogin(response);
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.i("Error", error.getMessage());
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				// 在此方法中设置要提交的请求参数
				Map<String, String> params = new HashMap<String, String>();
				params.put("token", token);
				params.put("userId", userId);
				return params;
			}
		};
		RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
	}
	
	private ArrayList<ProductShopping> mShoppingArray;
	private float totalMoney;

	private void onResponseForLogin(String response) {
		mShoppingArray = new ArrayList<ProductShopping>();
		ArrayList<String> arr = (ArrayList<String>)  JsonUtil.toJsonStrList(response);
		
		if (totalMoney != 0) {
			totalMoney = 0;
		}
		for (String json : arr) {
			ProductShopping ps = new ProductShopping();
			String product = JsonUtil.getJsonValueByKey(json, "product");
			ps.imageUrl = JsonUtil.getJsonValueByKey(product, "imageUrl");
			String sysSize = JsonUtil.getJsonValueByKey(product, "sysSize");
			ps.size = JsonUtil.getJsonValueByKey(sysSize, "size");
			String sysColor = JsonUtil.getJsonValueByKey(product, "sysColor");
			ps.color = JsonUtil.getJsonValueByKey(sysColor, "color");
			ps.price = JsonUtil.getJsonValueByKey(product, "price");
			ps.name = JsonUtil.getJsonValueByKey(product, "name");
			ps.id = JsonUtil.getJsonValueByKey(json, "id");
			ps.productQuantityId = JsonUtil.getJsonValueByKey(json,
					"productQuantityId");
			ps.qty = JsonUtil.getJsonValueByKey(json, "qty");
			mShoppingArray.add(ps);
			totalMoney += Float.parseFloat(ps.price) * Float.parseFloat(ps.qty);
		}
		
		tvMoney.setText("" + totalMoney);
		mVhArray = new ArrayList<ViewHolder>();
		mCartList.setAdapter(shoppingAdapter);
	}
	
	private BaseAdapter shoppingAdapter = new BaseAdapter() {

		@Override
		public int getCount() {
			return mShoppingArray.size();
		}

		@Override
		public Object getItem(int position) {
			return mShoppingArray.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewHolder vh;
			if(view == null) {
				vh = new ViewHolder();
				view = getLayoutInflater().inflate(R.layout.view_shopping_cart, null);
				vh.img = (ImageView) view
						.findViewById(R.id.iv_pro_shopping_img);
				vh.title = (TextView) view
						.findViewById(R.id.tv_pro_shopping_title);
				vh.color = (TextView) view
						.findViewById(R.id.tv_pro_shopping_color);
				vh.size = (TextView) view
						.findViewById(R.id.tv_pro_shopping_size);
				vh.price = (TextView) view
						.findViewById(R.id.tv_pro_shopping_total);
				vh.qty = (TextView) view.findViewById(R.id.et_pro_shopping_qty);
				vh.add = (ImageView) view
						.findViewById(R.id.ib_pro_shopping_right_btn);
				vh.del = (ImageView) view
						.findViewById(R.id.ib_pro_shopping_left_btn);
				vh.remove = (ImageView) view
						.findViewById(R.id.iv_pro_shopping_remove);
				view.setTag(vh);
			} else {
				vh = (ViewHolder) view.getTag();
			}
			vh.title.setText(mShoppingArray.get(position).name);
			vh.color.setText(mShoppingArray.get(position).color);
			vh.size.setText(mShoppingArray.get(position).size);
			vh.price.setText(mShoppingArray.get(position).price);
			vh.qty.setText(mShoppingArray.get(position).qty);
			vh.id = mShoppingArray.get(position).id;
			vh.productQuantityId = mShoppingArray.get(position).productQuantityId;
			String url = Constant.IMAGE_ADDRESS
					+ mShoppingArray.get(position).imageUrl + "_L.jpg";
			setImage(vh.img, url);
			if (mVhArray.indexOf(vh) == -1) {
				mVhArray.add(vh);
			}
			return view;
		}
		
	};
	
	private ArrayList<ViewHolder> mVhArray;

	class ViewHolder {
		String id;
		String productQuantityId;
		ImageView img;
		TextView title;
		TextView color;
		TextView size;
		TextView price;
		TextView qty;
		ImageView add;
		ImageView del;
		ImageView remove;
	}

	private void setImage(ImageView image, String url) {
		ImageLoader loader = RequestQueueSingleton.getInstance(
				getApplicationContext()).getImageLoader();
		ImageListener listener = ImageLoader.getImageListener(image,
				R.drawable.test1, R.drawable.test1);
		loader.get(url, listener, 400, 400);
	}
	
	//各个按钮的监听
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_account_reset_back:
			finish();
			break;
		case R.id.btn_account_reset_pen:
			setViewVisible();
			break;
		case R.id.btn_shopping_cart_account:
			Intent inetentSettle = new Intent(ShoppingCartActivity.this,SettleAccountsActivity.class);
			startActivity(inetentSettle);
			break;
		}
	}



	private void setViewVisible() {
		if (mVhArray != null && mVhArray.size() != 0) {
			if (isVisible) {
				for (ViewHolder vh : mVhArray) {
					vh.add.setVisibility(View.GONE);
					vh.del.setVisibility(View.GONE);
					vh.remove.setVisibility(View.GONE);
				}
				isVisible = false;
			} else {
				for (ViewHolder vh : mVhArray) {
					vh.add.setVisibility(View.VISIBLE);
					vh.del.setVisibility(View.VISIBLE);
					vh.remove.setVisibility(View.VISIBLE);
					vh.add.setOnClickListener(new vhImgOnClickListener(vh));
					vh.del.setOnClickListener(new vhImgOnClickListener(vh));
					vh.remove.setOnClickListener(new vhImgOnClickListener(vh));
				}
				isVisible = true;
			}
		}
		
	}
	
	
	private class vhImgOnClickListener implements OnClickListener {

		private ViewHolder vh;

		vhImgOnClickListener(ViewHolder vh) {
			this.vh = vh;
		}

		@Override
		public void onClick(View v) {
			for (ViewHolder vh : mVhArray) {
				if (v.getId() == vh.add.getId()) {
					addProduct(vh);
					return;
				} else if (v.getId() == vh.del.getId()) {
					delProduct(vh);
					return;
				} else if (v.getId() == vh.remove.getId()) {
					removeProduct(vh);
					return;
				}
			}

		}
	}
	
	private void addProduct(ViewHolder vh) {
		int sum = Integer.parseInt(vh.qty.getText().toString());
		update(vh, sum + 1, sum);

	}

	private void delProduct(ViewHolder vh) {
		int sum = Integer.parseInt(vh.qty.getText().toString());
		if (sum == 0) {
			return;
		}
		update(vh, sum - 1, sum);
	}

	private void removeProduct(ViewHolder vh) {
		removeDialog(this, vh).show();

	}
	
	/**
	 * 提示是否删除商品
	 * 
	 * @param context
	 * @return
	 */
	private Dialog removeDialog(Context context, ViewHolder vh) {
		final ViewHolder v = vh;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("确定要删除该商品吗?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				remove(v);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		return builder.create();
	}

	// 删除商品
	private void remove(ViewHolder vh) {
		SharedPreferences sp = getSharedPreferences("loginUser",
				Context.MODE_PRIVATE); // 私有数据
		userId = sp.getString("userId", "");
		token = sp.getString("token", "");
		String url = Constant.HOST + "/shoppingcart/delete.do";
		final ViewHolder v = vh;
		DataRequest request = new DataRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						// 解析用户信息的json，保存userid和token
						ToastUtil.showToast(ShoppingCartActivity.this, "删除成功");
						getShoppingInfo();
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
				params.put("token", token);
				params.put("userId", userId);
				params.put("cartId", v.id);
				return params;
			}
		};
		RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
	}

	// 更新商品数量
	private void update(ViewHolder vh, int newNum, int oldNum) {
		SharedPreferences sp = getSharedPreferences("loginUser",
				Context.MODE_PRIVATE); // 私有数据
		userId = sp.getString("userId", "");
		token = sp.getString("token", "");
		String url = Constant.HOST + "/shoppingcart/update.do";
		final ViewHolder v = vh;
		final int nn = newNum;
		final int on = oldNum;
		DataRequest request = new DataRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						v.qty.setText(nn + "");
						float total = Float.parseFloat(tvMoney.getText()
								.toString());
						if (nn > on) {
							tvMoney.setText((total + Float.parseFloat(v.price
									.getText().toString())) + "");
						} else if (nn < on) {
							tvMoney.setText((total - Float.parseFloat(v.price
									.getText().toString())) + "");
						}
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
				params.put("token", token);
				params.put("userId", userId);
				params.put("id", v.id);
				params.put("productQuantityId", v.productQuantityId);
				params.put("qty", nn + "");
				return params;
			}
		};
		RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
	}
	

}
