package com.imcore.xbionic.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.imcore.xbionic.R;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.HttpMethod;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.model.ProductDetail;
import com.imcore.xbionic.model.ProductItem;
import com.imcore.xbionic.util.JsonUtil;
import com.imcore.xbionic.util.ToastUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Paint.Join;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;

public class ProductBuyActivity extends Activity implements OnClickListener {
	private ExpandableListView expandableListView;
	private ArrayList<ProductDetail> mProductGroups;
	private ProgressDialog pd;
	private ImageButton btnBack, btnSearch;
	private HashMap<String, ArrayList<ProductItem>> mProductItems;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expandable_list_view);
		
//		pd = new ProgressDialog(ProductBuyActivity.this);
//		pd = ProgressDialog.show(ProductBuyActivity.this, "请稍候", "数据加载中...");
		btnBack = (ImageButton) findViewById(R.id.btn_product_back);
		btnSearch = (ImageButton) findViewById(R.id.btn_product_seek);

		btnBack.setOnClickListener(this);
		btnSearch.setOnClickListener(this);

		expandableListView = (ExpandableListView) findViewById(R.id.expandable_list_view);
		mProductItems = new HashMap<String, ArrayList<ProductItem>>();
		
		getGroupsInfo();

	}
	
	private void getGroupsInfo() {
		String url = Constant.HOST + "/category/list.do";
		pd = new ProgressDialog(ProductBuyActivity.this);
		pd = ProgressDialog.show(ProductBuyActivity.this, "请稍候", "数据加载中...");
		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						onResponseForLogin(response);
						pd.dismiss();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("sign", error.getMessage());
						pd.dismiss();
					}
				});

		RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
	}

	private void onResponseForLogin(String response) {

		mProductGroups = (ArrayList<ProductDetail>) JsonUtil
				.toObjectList(response, ProductDetail.class);
		for(ProductDetail p : mProductGroups){
			getItemsInfo(p);
		}
	}
	
	private void getItemsInfo(ProductDetail p) {
		String url = Constant.HOST + "/category/list.do?navId=" + p.code;
		final ProductDetail pro = p;
		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						ArrayList<ProductItem> a = (ArrayList<ProductItem>) JsonUtil.toObjectList(response, ProductItem.class);
						mProductItems.put(mProductGroups.indexOf(pro) + "", a);
						if(mProductItems.size() == mProductGroups.size()){
							initAdapter();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("sign", error.getMessage());
					}
				});

		RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
	}
	
	private void initAdapter(){
		expandableListView.setAdapter(new ExpandableAdapter(this, mProductGroups, mProductItems));
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_product_back:
			finish();
			break;
		case R.id.btn_product_seek:
			//
			break;
		}

	}
	

}
