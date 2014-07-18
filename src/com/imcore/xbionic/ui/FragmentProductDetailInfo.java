package com.imcore.xbionic.ui;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

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
	private Button btnCollect, btnTrolley;
	private TextView mTvTitle, mTvPrice, mAmount;
	private int mImgID;
	private EditText mBuyEt;
	private ArrayList<TextView> mSizeArray;
	private ProductColor mSelectProductColor;
	private ProductSize mSelectProductSize;
	private long commodityDetailId;

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
		btnCollect = (Button) view.findViewById(R.id.btn_add_collect);
		btnTrolley = (Button) view.findViewById(R.id.btn_add_trolley);

		btnCollect.setOnClickListener(this);
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
					image.setBackgroundResource(R.drawable.product_detail_info_color_select_background);
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

	@Override
	public void onClick(View v) {

	}

}
