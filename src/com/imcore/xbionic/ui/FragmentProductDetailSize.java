package com.imcore.xbionic.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.imcore.xbionic.R;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.util.Const;
import com.imcore.xbionic.util.DisplayUtil;
import com.imcore.xbionic.util.JsonUtil;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentProductDetailSize extends Fragment {
	View view;
	private GridView gridView;
	private LayoutInflater inflater;
	private List<String> dataList = new ArrayList<String>();
	private List<String> dataColumns = new ArrayList<String>();
	private ArrayList<String> imgUrl;
	private long commodityDetailId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_commodity_detail_size, null);
		commodityDetailId = getArguments().getLong(
				Const.PRODUCT_DETAIL_FRAGMENT_KEY);

		getCommodityDetailSize();
		return view;
	}

	private void getCommodityDetailSize() {
		imgUrl = new ArrayList<String>();
		String url = Constant.HOST + "/product/size/list.do?id="
				+ commodityDetailId;
		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						onResponseForProductList(response);
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("Error", error.getMessage());
					}
				});

		RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(
				request);

	}

	private void onResponseForProductList(String response) {

		String jsonSize = JsonUtil.getJsonValueByKey(response,
				"sizeStandardDetailList");
		ArrayList<String> arrRow = (ArrayList<String>) JsonUtil
				.toJsonStrList(jsonSize);
		if (!dataList.isEmpty()) {
			dataList.clear();
		}

		for (String json : arrRow) {
			try {
				JSONObject jo = new JSONObject(json);
				if (!dataColumns.isEmpty()) {
					dataColumns.clear();
				}

				if (jo.getString("size") != null
						&& !jo.getString("size").equals("")) {
					dataColumns.add(jo.getString("size"));
				}
				if (jo.getString("p1") != null
						&& !jo.getString("p1").equals("")) {
					dataColumns.add(jo.getString("p1"));
				}
				if (jo.getString("p2") != null
						&& !jo.getString("p2").equals("")) {
					dataColumns.add(jo.getString("p2"));
				}
				if (jo.getString("p3") != null
						&& !jo.getString("p3").equals("")) {
					dataColumns.add(jo.getString("p3"));
				}
				if (jo.getString("p4") != null
						&& !jo.getString("p4").equals("")) {
					dataColumns.add(jo.getString("p4"));
				}
				if (jo.getString("p5") != null
						&& !jo.getString("p5").equals("")) {
					dataColumns.add(jo.getString("p5"));
				}
				if (jo.getString("p6") != null
						&& !jo.getString("p6").equals("")) {
					dataColumns.add(jo.getString("p6"));
				}
				if (jo.getString("p7") != null
						&& !jo.getString("p7").equals("")) {
					dataColumns.add(jo.getString("p7"));
				}
				if (jo.getString("p8") != null
						&& !jo.getString("p8").equals("")) {
					dataColumns.add(jo.getString("p8"));
				}

				dataList.addAll(dataColumns);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		initGridView();
	}

	private void initGridView() {
		gridView = (GridView) view.findViewById(R.id.gv_com_detail_size);
		inflater = (LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		gridView.setAdapter(new GridViewAdapter());
		int sums = dataList.size();
		int columns = dataColumns.size();
		int rows = sums / columns;

		int allWidth;
		int allHeight;
		int itemWidth;

		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		float density = dm.density;

		int screenWidth = DisplayUtil.getScreenWidth(getActivity());
		int gridWidth = (int) (80 * columns * density);

		if (screenWidth < gridWidth) {
			allWidth = gridWidth;
			itemWidth = (int) (80 * density);
		} else {
			allWidth = screenWidth;
			itemWidth = screenWidth / columns;
		}

		int h = DisplayUtil.dip2Px(getActivity(), 36);
		allHeight = rows * h;

		LinearLayout.LayoutParams paeams = new LinearLayout.LayoutParams(
				allWidth, allHeight);
		gridView.setLayoutParams(paeams);
		gridView.setColumnWidth(itemWidth);
		gridView.setStretchMode(GridView.NO_STRETCH);
		gridView.setNumColumns(columns);

	}

	class GridViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = LayoutInflater.from(getActivity()).inflate(
					R.layout.view_commodity_detail_size, null);
			TextView textView = (TextView) convertView
					.findViewById(R.id.tv_commodity_detail_size);
			String str = dataList.get(position);
			textView.setText(str);
			return convertView;
		}

	}

}
