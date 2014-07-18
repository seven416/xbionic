package com.imcore.xbionic.ui;

import java.util.ArrayList;
import java.util.List;

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
	private List<Integer> productCategory;
	private List<ProductDetail> bionicList;
	private List<ProductDetail> sockList;
	private List<List<ProductDetail>> productList;
	private ProgressDialog pd;
	private Fragment mProductMainFragment;
	private ImageButton btnBack, btnSearch;
	
	private String navId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expandable_list_view);
		getLayoutData();

		btnBack = (ImageButton) findViewById(R.id.btn_product_back);
		btnSearch = (ImageButton) findViewById(R.id.btn_product_seek);

		btnBack.setOnClickListener(this);
		btnSearch.setOnClickListener(this);

		expandableListView = (ExpandableListView) findViewById(R.id.expandable_list_view);
		expandableListView.setAdapter(adapter);
		expandableListView.setOnChildClickListener(onChildClickListener);

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
	
	public void getLayoutData() {
		productCategory = new ArrayList<Integer>();
		bionicList = new ArrayList<ProductDetail>();
		sockList = new ArrayList<ProductDetail>();
		productList = new ArrayList<List<ProductDetail>>();
		productCategory.add(R.drawable.upbackground);
		productCategory.add(R.drawable.downbackground);
		
		String urlBionic = Constant.HOST + "/category/list.do?"
				+ "navId=100001";
		String urlSock = Constant.HOST + "/category/list.do?" + "navId=100002";
		pd = new ProgressDialog(ProductBuyActivity.this);
		pd = ProgressDialog.show(ProductBuyActivity.this, "请稍候", "数据加载中...");
		
		DataRequest bionicRequest = new DataRequest(HttpMethod.GET, urlBionic, bionicListener, bionicErrorListener);
		
		DataRequest sockRequest = new DataRequest(HttpMethod.GET, urlSock, sockListener, sockErrorListener);
		
		RequestQueueSingleton.getInstance(ProductBuyActivity.this).addToRequestQueue(bionicRequest);
		
		RequestQueueSingleton.getInstance(ProductBuyActivity.this).addToRequestQueue(sockRequest);
		
		
	}
	
	private Response.Listener<String> bionicListener = new Response.Listener<String>() {

		@Override
		public void onResponse(String response) {
			bionicList = JsonUtil.toObjectList(response, ProductDetail.class);
			productList.add(bionicList);
			pd.dismiss();
			
		}
	};
	
	private Response.ErrorListener bionicErrorListener = new Response.ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			ToastUtil.showToast(ProductBuyActivity.this, error.getMessage());
			pd.dismiss();
		}
	};
	
	private Response.Listener<String> sockListener = new Response.Listener<String>() {

		@Override
		public void onResponse(String response) {
			sockList = JsonUtil.toObjectList(response, ProductDetail.class);
			productList.add(sockList);
		}
	};

	private Response.ErrorListener sockErrorListener = new Response.ErrorListener() {

		@Override
		public void onErrorResponse(VolleyError error) {
			ToastUtil.showToast(ProductBuyActivity.this, error.getMessage());
		}
	};

	ExpandableListAdapter adapter = new ExpandableListAdapter() {

		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub

		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub

		}

		@Override
		public int getGroupCount() {
			return productCategory.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return 1;
		}

		@Override
		public Object getGroup(int groupPosition) {

			return productCategory.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return productList.get(groupPosition).get(0);
		}

		@Override
		public long getGroupId(int groupPosition) {

			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {

			return childPosition;
		}

		@Override
		public boolean hasStableIds() {

			return true;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			View view = getLayoutInflater().inflate(
					R.layout.view_product_proup, parent, false);
			ImageView imageView = (ImageView) view.findViewById(R.id.iv_group_view);
			imageView.setImageResource(productCategory.get(groupPosition));

			return view;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			View view = getLayoutInflater().inflate(R.layout.view_product_grid,
					parent, false);
			GridView gridView = (GridView) view
					.findViewById(R.id.gv_product_detail);
			gridView.setAdapter(new GridAdapter(groupPosition));
			return view;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		@Override
		public boolean areAllItemsEnabled() {
			return true;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public void onGroupExpanded(int groupPosition) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGroupCollapsed(int groupPosition) {
			// TODO Auto-generated method stub

		}

		@Override
		public long getCombinedChildId(long groupId, long childId) {
			return childId;
		}

		@Override
		public long getCombinedGroupId(long groupId) {
			return groupId;
		}

	};

	OnChildClickListener onChildClickListener = new OnChildClickListener() {

		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			Intent intent = new Intent(ProductBuyActivity.this,
					ProductListActivity.class);
			startActivity(intent);
			return true;
		}
	};

	class GridAdapter extends BaseAdapter {
		int groupPosition;

		public GridAdapter(int groupPosition) {
			this.groupPosition = groupPosition;
		}

		@Override
		public int getCount() {
			return productList.get(groupPosition).size();
		}

		@Override
		public Object getItem(int position) {

			return productList.get(groupPosition).get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			ViewHolder vh = null;
			if (view == null) {
				vh = new ViewHolder();
				view = getLayoutInflater().inflate(R.layout.view_product_list,
						null);
				vh.nImageView = (NetworkImageView) view
						.findViewById(R.id.iv_product_network_img);
				vh.textView = (TextView) view
						.findViewById(R.id.tv_product_title);
				view.setTag(vh);
			} else {
				vh = (ViewHolder) view.getTag();
			}

			String url = Constant.IMAGE_HOST
					+ productList.get(groupPosition).get(position).imageUrl
					+ Constant.IMAGE_SERVER_TAIL;
			
			ImageLoader loader = RequestQueueSingleton.getInstance(getApplicationContext()).getImageLoader();
			vh.nImageView.setImageUrl(url, loader);
			vh.textView.setText(productList.get(groupPosition).get(position).name);
			
			final String subNavId = productList.get(groupPosition).get(position).id +"";
			Log.i("subNavId", subNavId+ "");
			if(productCategory.get(groupPosition) == R.drawable.upbackground) {
				navId = "100001";
			} else if (productCategory.get(groupPosition) == R.drawable.downbackground) {
				navId = "100002";
			}
			
			vh.nImageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(ProductBuyActivity.this,
							ProductListActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("navId", navId);
					bundle.putString("subNavId", subNavId);
					intent.putExtras(bundle);
					startActivity(intent);
					
				}
			});

			return view;
		}

		class ViewHolder {
			NetworkImageView nImageView;
			TextView textView;
		}

	}

}
