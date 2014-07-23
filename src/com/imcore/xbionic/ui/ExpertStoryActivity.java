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
import com.imcore.xbionic.model.ExpertStoryDetail;
import com.imcore.xbionic.util.JsonUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ExpertStoryActivity extends Activity implements OnClickListener {
	private ListView mListView;
	private ArrayList<ExpertStoryDetail> mList;
	private ProgressDialog mDialog;
	private ImageButton btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expert_story);

		mListView = (ListView) findViewById(R.id.lv_expert_view);
		mList = new ArrayList<ExpertStoryDetail>();
		btnBack = (ImageButton) findViewById(R.id.btn_expert_story_back);
		btnBack.setOnClickListener(this);
		
		getStoryDetail();
		mListView.setOnItemClickListener(new ItemOnClickListener());

	}

	private void getStoryDetail() {
		String url = Constant.HOST
				+ "/testteam/list.do?subNavId=4&offset=0&fetchSize=10&navId=100001";
		mDialog = ProgressDialog.show(this, "请稍候！", "数据正在加载中...", true);
		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {

					@Override
					public void onResponse(String response) {
						onResponseForStoryDetail(response);
						mDialog.cancel();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("Error", error.getMessage());
						mDialog.cancel();
					}
				});
		RequestQueueSingleton.getInstance(this).addToRequestQueue(request);
	}

	private void onResponseForStoryDetail(String response) {
		ArrayList<String> arrJsonStr = (ArrayList<String>) JsonUtil
				.toJsonStrList(response);
		for (String json : arrJsonStr) {
			try {
				JSONObject jo = new JSONObject(json);
				ExpertStoryDetail stortDetail = new ExpertStoryDetail();
				stortDetail.id = jo.getLong("id");
				stortDetail.title = jo.getString("title");
				stortDetail.updateDate = jo.getString("updateDate");
				stortDetail.phoneUrl = jo.getString("phoneUrl");
				mList.add(stortDetail);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		mListView.setAdapter(new ListViewAdapter());
	}

	class ListViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
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
				vh = new ViewHolder();
				view = getLayoutInflater().inflate(
						R.layout.view_expert_story_list, null);
				vh.ivStoryImg = (ImageView) view
						.findViewById(R.id.iv_expert_story_img);
				vh.tvTitle = (TextView) view
						.findViewById(R.id.tv_expert_story_title);
				vh.tvUpdate = (TextView) view
						.findViewById(R.id.tv_expert_story_date);
				view.setTag(vh);
			} else {
				vh = (ViewHolder) view.getTag();
			}
			vh.tvTitle.setText(mList.get(position).title);
			vh.tvUpdate.setText(mList.get(position).updateDate);
			// String url = Constant.IMAGE_ADDRESS +
			// mList.get(position).phoneUrl + "_XL.jpg";
			// setImage(vh.ivStoryImg, url);

			return view;
		}

		class ViewHolder {
			ImageView ivStoryImg;
			TextView tvTitle;
			TextView tvUpdate;
		}

		private void setImage(ImageView image, String url) {
			ImageLoader loader = RequestQueueSingleton.getInstance(
					getApplicationContext()).getImageLoader();
			ImageListener listener = ImageLoader.getImageListener(image,
					R.drawable.xlogo, R.drawable.xlogo);
			loader.get(url, listener, 400, 400);
		}

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_expert_story_back) {
			finish();
		}

	}

	private class ItemOnClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//
		}

	}

}
