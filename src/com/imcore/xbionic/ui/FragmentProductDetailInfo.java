package com.imcore.xbionic.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

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
import com.imcore.xbionic.model.ProductColor;
import com.imcore.xbionic.model.ProductQuantity;
import com.imcore.xbionic.model.ProductSize;
import com.imcore.xbionic.util.Const;
import com.imcore.xbionic.util.JsonUtil;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class FragmentProductDetailInfo extends Fragment implements
		OnClickListener {
	private View view;
	private Button btnBuy, btnTrolley;
	private TextView mTvTitle, mTvPrice, mAmount;
	private int mImgID;
	private EditText mBuyEt;
	private ArrayList<TextView> mSizeArray;
	private ProductColor mSelectProductColor;
	private ProductSize mSelectProductSize;
	private long commodityDetailId;
	private String sumBuy;

	private String mTitle;
	private String mPrice;
	private ArrayList<ProductColor> mColorArray;
	private ArrayList<ImageView> mColorImgArray;
	private ArrayList<ProductSize> mSize;
	private int mSizeId;

	private ProductQuantity productQuantity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_commodity_detail_info, null);

		commodityDetailId = getArguments().getLong(
				Const.PRODUCT_DETAIL_FRAGMENT_KEY);

		initWidget();

		getProductDetailInfo();// 获取产品详情
		getProductDetailSize();// 获取产品尺寸

		return view;
	}

	// 初始化控件
	private void initWidget() {
		mTvTitle = (TextView) view.findViewById(R.id.tv_com_detail_title);
		mTvPrice = (TextView) view.findViewById(R.id.tv_com_detail_price_num);
		mAmount = (TextView) view.findViewById(R.id.tv_com_detail_amount);
		mBuyEt = (EditText) view.findViewById(R.id.et_com_detail_amount);
		btnBuy = (Button) view.findViewById(R.id.btn_buy);
		btnTrolley = (Button) view.findViewById(R.id.btn_add_trolley);

		btnBuy.setOnClickListener(this);
		btnTrolley.setOnClickListener(this);

	}

	// 获取产品详情
	private void getProductDetailInfo() {
		mColorArray = new ArrayList<ProductColor>();
		mColorImgArray = new ArrayList<ImageView>();

		String url = Constant.HOST + "/product/get.do?id=" + commodityDetailId;
		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						if (response != null) {
							onResponseForProductList(response);
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("sign", error.getMessage());
					}
				});

		RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(
				request);
	}

	private void onResponseForProductList(String response) {
		try {
			JSONObject jo = new JSONObject(response);
			mTitle = jo.getString("name");
			mPrice = jo.getString("price");
			mColorArray = (ArrayList<ProductColor>) JsonUtil.toObjectList(
					jo.getString("sysColorList"), ProductColor.class);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		mTvTitle.setText(mTitle);
		mTvPrice.setText("￥" + mPrice);
		addColor();// 添加颜色图片控件

	}

	// 动态生成颜色控件
	private void addColor() {
		LinearLayout insertLayout = (LinearLayout) view
				.findViewById(R.id.rel_com_detail_color);
		mImgID = 0x11;
		for (ProductColor p : mColorArray) {
			ImageView image = new ImageView(getActivity());
			String url = Constant.IMAGE_ADDRESS + p.colorImage + ".jpg";
			setImag(image, url);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					50, 50);
			params.leftMargin = 20;
			image.setScaleType(ScaleType.FIT_XY);
			image.setId(mImgID++);
			insertLayout.addView(image, params);
			image.setOnClickListener(colorOnClickListener);
			mColorImgArray.add(image);
			if (mColorArray.indexOf(p) == 0) {
				image.setBackgroundResource(R.drawable.product_detail_info_color_select_background);
				mSelectProductColor = p;
			}

		}

	}

	// 控件添加图片
	private void setImag(ImageView image, String url) {
		ImageLoader loader = RequestQueueSingleton.getInstance(
				getActivity().getApplicationContext()).getImageLoader();

		ImageListener listener = ImageLoader.getImageListener(image,
				R.drawable.test1, R.drawable.test1);

		loader.get(url, listener, 400, 400);

	}

	// 获取产品尺寸
	private void getProductDetailSize() {
		mSize = new ArrayList<ProductSize>();
		String url = Constant.HOST + "/product/size/list.do?id="
				+ commodityDetailId;

		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						if (response != null) {
							String jsonSize = JsonUtil.getJsonValueByKey(
									response, "sysSizeList");
							mSize = (ArrayList<ProductSize>) JsonUtil
									.toObjectList(jsonSize, ProductSize.class);
							addSize();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("sign", error.getMessage());

					}
				});

		RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(
				request);

	}

	// 动态生成尺寸控件
	private void addSize() {
		mSizeArray = new ArrayList<TextView>();
		LinearLayout insertLayout = (LinearLayout) view
				.findViewById(R.id.rel_com_detail_size);
		mSizeId = 0x01;
		for (ProductSize p : mSize) {
			TextView tv = new TextView(getActivity());
			int index = p.size.indexOf("（");
			String s;
			if (index != -1) {
				s = p.size.substring(0, index);
			} else {
				s = p.size;
			}
			tv.setText(s);

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = 10;
			tv.setId(mSizeId++);
			insertLayout.addView(tv, layoutParams);
			tv.setOnClickListener(sizeOnClickListener);
			tv.setBackgroundResource(R.drawable.product_detail_info_size_background);
			mSizeArray.add(tv);
		}
	}

	// 监听颜色控件
	private OnClickListener colorOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			for (ImageView image : mColorImgArray) {
				if (v.getId() == image.getId()) {
					image.setBackgroundResource(R.drawable.product_detail_info_color_select_background);
					mSelectProductColor = mColorArray.get(mColorImgArray
							.indexOf(image));
					if (mSelectProductColor != null
							&& mSelectProductSize != null) {
						getProductNum(mSelectProductColor.id,
								mSelectProductSize.id);
					}

				} else {
					image.setBackgroundResource(R.drawable.product_detail_info_color_background);
				}
			}
		}

	};

	// 监听尺寸控件
	private OnClickListener sizeOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			for (TextView tv : mSizeArray) {
				if (v.getId() == tv.getId()) {
					tv.setBackgroundResource(R.drawable.product_detail_info_size_select_background);
					mSelectProductSize = mSize.get(mSizeArray.indexOf(tv));
					if (mSelectProductColor != null
							&& mSelectProductSize != null) {
						getProductNum(mSelectProductColor.id,
								mSelectProductSize.id);
					}
				} else {
					tv.setBackgroundResource(R.drawable.product_detail_info_size_background);
				}
			}
			
		}

	};

	// 获取产品库存
	private void getProductNum(long colorId, long sizeId) {
		String url = Constant.HOST + "/product/quantity/get.do?id="
				+ commodityDetailId + "&colorId=" + colorId + "&sizeId="
				+ sizeId;

		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						if (response != null) {
							productQuantity = JsonUtil.toObject(response,
									ProductQuantity.class);
						}
						if (productQuantity != null) {
							mAmount.setText("(库存" + productQuantity.qty + "件)");
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("sign", error.getMessage());
					}
				});

		RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(
				request);
	}
	
	//购买按钮事件
	private void getBtnBuy() {
		sumBuy = mBuyEt.getText().toString().trim();

		if (mSelectProductSize == null) {
			new AlertDialog.Builder(getActivity()).setMessage("尺寸还没有选....")
					.create().show();
			return;
		} else if (productQuantity.qty == 0 || productQuantity == null) {
			new AlertDialog.Builder(getActivity()).setTitle("太火了，已经卖光了...")
					.create().show();
			return;
		} else if (sumBuy == null || sumBuy.equals("")) {
			new AlertDialog.Builder(getActivity()).setTitle("购买数量还没有选....")
					.create().show();
			return;
		} else if (Integer.parseInt(sumBuy) > productQuantity.qty) {
			new AlertDialog.Builder(getActivity())
					.setTitle("您要的太多，小的没法给了...").create().show();
			return;
		} else {
			submitOrders();
		}
	}
	
	/**
	 * 下单成功对话框
	 * 
	 * @param context
	 * @return
	 */
	private Dialog showDialog(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("下单成功");
		builder.setPositiveButton("马上结算",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				});
		builder.setNegativeButton("再逛逛", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		return builder.create();
	}
	
	private String userId;
	private String token;
	
	private void submitOrders() {
		SharedPreferences sp = getActivity().getSharedPreferences("loginUser",
				Context.MODE_PRIVATE); // 私有数据
		userId = sp.getString("userId", "");
		token = sp.getString("token", "");
		if (userId.equals("") || token.equals("")) {
			new AlertDialog.Builder(getActivity())
					.setTitle("您还未登陆，请先登陆.....")
					.setPositiveButton("登陆",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									Intent intent = new Intent(getActivity(),
											TribeLogin.class);
									intent.putExtra(Const.LOGIN_KEY,
											Const.LOGIN_AT_BUY_VALUE);
									startActivity(intent);
								}
							}).create().show();
			return;
		}

		String url = Constant.HOST + "/shoppingcart/update.do";
		DataRequest request = new DataRequest(Request.Method.POST, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						 if(response != null){
							 showDialog(getActivity()).show();
						 }
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
				params.put("userId", userId);
				params.put("token", token);
				params.put("productQuantityId", productQuantity.id + "");
				params.put("qty", sumBuy);
				// Log.i("sign", productQuantity.id + "----" + sumBuy);
				return params;
			}
		};
		RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(
				request);
	}
	
	//加入购物车按钮监听方法
	private void getAddTrolley() {
		//
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add_trolley:
			getAddTrolley();
			break;

		case R.id.btn_buy:
			getBtnBuy();
			break;
		}

	}

}
