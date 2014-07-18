package com.imcore.xbionic.ui;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.imcore.xbionic.R;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.HttpMethod;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.model.ProductList;
import com.imcore.xbionic.util.Const;
import com.imcore.xbionic.util.JsonUtil;

import android.app.Activity;
import android.app.DownloadManager.Request;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ProductListActivity extends Activity implements OnClickListener {
	private GridView mGridView;
	private ArrayList<ProductList> mProductListGroup;
	private String navId;
	private String subNavId;
	private ImageButton btnBack, btnSearch;
	private ProgressDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commodity_list);

		mGridView = (GridView) findViewById(R.id.gv_commodity_grid_view);
		btnBack = (ImageButton) findViewById(R.id.btn_commodity_back);

		btnBack.setOnClickListener(this);

		Bundle bundle = this.getIntent().getExtras();
		navId = bundle.getString("navId");
		Log.i("id", navId +"");
		subNavId = bundle.getString("subNavId");
		Log.i("dfdf", subNavId +"");
		mProductListGroup = new ArrayList<ProductList>();
//		mGridView.setOnItemClickListener(new ItemOnClickListener());
		getProductListInfo();

	}

	private void getProductListInfo() {
		String url = Constant.HOST + "/category/products.do?navId=" + navId
				+ "&subNavId=" + subNavId + "&offset=0&fetchSize=10";

		mDialog = ProgressDialog.show(this, "请稍候", "正在加载数据...", true);
		DataRequest request = new DataRequest(com.android.volley.Request.Method.GET, url,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						onResponseForProductList(response);
						mDialog.cancel();

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("sign", error.getMessage());
						mDialog.cancel();

					}
				});

		RequestQueueSingleton.getInstance(this).addToRequestQueue(request);

	}

	private void onResponseForProductList(String response) {

		ArrayList<String> arrJsonStr = (ArrayList<String>) JsonUtil
				.toJsonStrList(response);
		for (String json : arrJsonStr) {
			try {
				JSONObject jsonObject = new JSONObject(json);
				ProductList productList = new ProductList();
				productList.id = jsonObject.getLong("id");
				productList.name = jsonObject.getString("name");
				productList.price = jsonObject.getString("price");
				productList.imageUrl = jsonObject.getString("imageUrl");
				mProductListGroup.add(productList);

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		mGridView.setAdapter(new ProductListAdapter());

	}

	private class ProductListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mProductListGroup.size();
		}

		@Override
		public Object getItem(int position) {
			return mProductListGroup.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewHolder vh;

			if (view == null) {
				view = getLayoutInflater().inflate(R.layout.product_list_view,
						null);
				vh = new ViewHolder();
				vh.img = (ImageView) view
						.findViewById(R.id.iv_product_list_img);
				vh.tvName = (TextView) view
						.findViewById(R.id.tv_product_list_dec);
				vh.tvPrice = (TextView) view
						.findViewById(R.id.tv_product_list_price);
				view.setTag(vh);
			} else {
				vh = (ViewHolder) view.getTag();
			}

			vh.tvName.setText(mProductListGroup.get(position).name);
			vh.tvPrice.setText("￥" + mProductListGroup.get(position).price);
			String url = Constant.IMAGE_ADDRESS
					+ mProductListGroup.get(position).imageUrl
					+ "_L.jpg";
			setImage(vh.img, url);
			
			view.setOnClickListener(new ItemOnClickListener(mProductListGroup
					.get(position)));

			return view;
		}

		class ViewHolder {
			ImageView img;
			TextView tvName;
			TextView tvPrice;
		}

		private void setImage(ImageView image, String url) {
			ImageLoader loader = RequestQueueSingleton.getInstance(
					getApplicationContext()).getImageLoader();
			ImageListener listener = ImageLoader.getImageListener(image, R.drawable.test1, R.drawable.test1);
			
			loader.get(url, listener, 400, 400);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_commodity_back:
			finish();
			break;

		}

	}
	
	private class ItemOnClickListener implements OnClickListener {
		private ProductList p;
		
		ItemOnClickListener(ProductList p){
			this.p = p;
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(ProductListActivity.this,ProductDetailsActivity.class);
			intent.putExtra(Const.PRODUCT_DETAIL_FRAGMENT_KEY, p.id);
			startActivity(intent);
		}

		

	}

}
