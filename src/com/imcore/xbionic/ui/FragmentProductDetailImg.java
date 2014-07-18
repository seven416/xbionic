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
import com.imcore.xbionic.util.Const;
import com.imcore.xbionic.util.JsonUtil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class FragmentProductDetailImg extends Fragment {
	private Gallery gal;
	ArrayList<String> imgUrl;
	private View view;
	private long commodityId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_commodity_detail_img, null);
		gal = (Gallery) view.findViewById(R.id.gal_commodity_detail_img);

		commodityId = getArguments().getLong(Const.PRODUCT_DETAIL_FRAGMENT_KEY);
		getProductDetailImg();

		return view;
	}

	private BaseAdapter ImgAdapter = new BaseAdapter() {

		@Override
		public int getCount() {
			return imgUrl.size();
		}

		@Override
		public Object getItem(int position) {
			return imgUrl.get(position);
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
				view = LayoutInflater.from(getActivity()).inflate(
						R.layout.view_commodity_detail_img, null);
				vh = new ViewHolder();
				vh.img = (ImageView) view
						.findViewById(R.id.iv_commodity_detail_img);
				view.setTag(vh);
			} else {
				vh = (ViewHolder) view.getTag();
			}

			String url = Constant.IMAGE_ADDRESS + imgUrl.get(position)
					+ "_L.jpg";
			setImage(vh.img, url);

			return view;
		}

		class ViewHolder {
			ImageView img;
		}

	};

	private void setImage(ImageView image, String url) {
		ImageLoader loader = RequestQueueSingleton.getInstance(
				getActivity().getApplicationContext()).getImageLoader();

		ImageListener listener = ImageLoader.getImageListener(image,
				R.drawable.test1, R.drawable.test1);

		loader.get(url, listener, 400, 400);
	}

	private void getProductDetailImg() {
		imgUrl = new ArrayList<String>();
		String url = Constant.HOST + "/product/images/list.do?id="
				+ commodityId;

		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						onResponseForProductList(response);
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
		ArrayList<String> arrJsonStr = (ArrayList<String>) JsonUtil
				.toJsonStrList(response);
		for (String json : arrJsonStr) {
			try {
				JSONObject jsonObject = new JSONObject(json);
				imgUrl.add(jsonObject.get("image").toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		gal.setAdapter(ImgAdapter);
	}

}
