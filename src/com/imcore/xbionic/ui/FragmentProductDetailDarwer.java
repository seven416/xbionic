package com.imcore.xbionic.ui;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.imcore.xbionic.R;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.DataRequest;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.model.ProductAwards;
import com.imcore.xbionic.model.ProductComment;
import com.imcore.xbionic.model.ProductNews;
import com.imcore.xbionic.util.Const;
import com.imcore.xbionic.util.JsonUtil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class FragmentProductDetailDarwer extends Fragment implements
		OnClickListener {
	private View view;
	private Button btnClient, btnNews, btnAwards, btnShare, btnCart, btnBuy,
			btnCollect;
	private Button btnComment;
	private ListView mListView;
	private ArrayList<ProductComment> mCommentList;
	private ArrayList<ProductNews> mNewsList;
	private ArrayList<ProductAwards> mAwardsList;
	private long id;// 产品id

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_product_detail_drawer, null);
		mListView = (ListView) view.findViewById(R.id.lv_drawer_fragment_list);
		mCommentList = new ArrayList<ProductComment>();
		mNewsList = new ArrayList<ProductNews>();
		mAwardsList = new ArrayList<ProductAwards>();
		//id = getArguments().getLong("id");
		id = getActivity().getIntent().getLongExtra(Const.PRODUCT_DETAIL_FRAGMENT_KEY, 0);
		Log.i("sign", id + "");
		getProductComment();
		initWidget();
		return view;
	}

	// 初始化控件
	private void initWidget() {
		btnClient = (Button) view.findViewById(R.id.btn_drawer_fragment_client);
		btnNews = (Button) view.findViewById(R.id.btn_drawer_fragment_news);
		btnAwards = (Button) view.findViewById(R.id.btn_drawer_fragment_awards);
		btnShare = (Button) view.findViewById(R.id.btn_darwer_fragment_share);
		btnCart = (Button) view
				.findViewById(R.id.btn_darwer_fragment_shopping_cart);
		btnBuy = (Button) view.findViewById(R.id.btn_darwer_fragment_buy);
		btnCollect = (Button) view
				.findViewById(R.id.btn_darwer_fragment_collect);
		btnComment = (Button) view.findViewById(R.id.btn_darwer_fragment_comment);

		btnClient.setTextColor(Color.MAGENTA);
		btnClient.setOnClickListener(this);
		btnNews.setOnClickListener(this);
		btnAwards.setOnClickListener(this);
		btnShare.setOnClickListener(this);
		btnCart.setOnClickListener(this);
		btnBuy.setOnClickListener(this);
		btnCollect.setOnClickListener(this);
		btnComment.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_drawer_fragment_client) {// 客户评价
			btnClient.setTextColor(Color.MAGENTA);
			getProductComment();
		} else {
			btnClient.setTextColor(Color.WHITE);
		}
		if (v.getId() == R.id.btn_drawer_fragment_news) {// 产品新闻
			btnNews.setTextColor(Color.MAGENTA);
			getProductNews();
		} else {
			btnNews.setTextColor(Color.WHITE);
		}
		if (v.getId() == R.id.btn_drawer_fragment_awards) {// 所获奖项
			btnAwards.setTextColor(Color.MAGENTA);
			getProductAwards();
		} else {
			btnAwards.setTextColor(Color.WHITE);
		}
		if (v.getId() == R.id.btn_darwer_fragment_share) {// 分享
			btnShare.setTextColor(Color.MAGENTA);
		} else {
			btnShare.setTextColor(Color.WHITE);
		}
		if (v.getId() == R.id.btn_darwer_fragment_shopping_cart) {// 购物车
			btnCart.setTextColor(Color.MAGENTA);
			Intent intentShoppingCart = new Intent(getActivity(),ShoppingCartActivity.class);
		
			startActivity(intentShoppingCart);
		} else {
			btnCart.setTextColor(Color.WHITE);
		}
		if (v.getId() == R.id.btn_darwer_fragment_buy) {// 购买
			btnBuy.setTextColor(Color.MAGENTA);
			Intent intentBuy = new Intent(getActivity(),SettleAccountsActivity.class);
			startActivity(intentBuy);
		} else {
			btnBuy.setTextColor(Color.WHITE);
		}
		if (v.getId() == R.id.btn_darwer_fragment_collect) {// 收藏
			btnCollect.setTextColor(Color.MAGENTA);
			Intent intentCollect = new Intent(getActivity(),MyCollectActivity.class);
			startActivity(intentCollect);
		} else {
			btnCollect.setTextColor(Color.WHITE);
		}
		if (v.getId() == R.id.btn_darwer_fragment_comment) {//评论
			Intent intentComment = new Intent(getActivity(),CommentActivity.class);
			Bundle bundle = new Bundle();
			bundle.putLong("id", id);
			intentComment.putExtras(bundle);
			startActivity(intentComment);
		} 
	}

	// 客户评论------------------------------------------------------------------------
	private void getProductComment() {
		String url = Constant.HOST + "/product/comments/list.do?id=" + id;
		//Log.i("sign", id + "");
		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						if (response != null) {
							// Log.i("sign", response);
							onResponseForProductComment(response);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("sign", error.getMessage());
					}
				});
		request.setTag(ProductDetailsActivity.class);
		RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(
				request);
	}

	private void onResponseForProductComment(String response) {
		mCommentList = (ArrayList<ProductComment>) JsonUtil.toObjectList(
				response, ProductComment.class);
		// Log.i("sign", mCommentArray.toString());
		mListView.setAdapter(new CommentAdapter(getActivity(), mCommentList));
	}

	// 产品新闻------------------------------------------------------------------------
	private void getProductNews() {

		String url = Constant.HOST + "/news/list.do?offset=0&fetchSize=10";
		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						if (response != null) {
							// Log.i("sign", response);
							responseForNews(response);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// Log.i("sign", error.getMessage());
					}
				});
		request.setTag(ProductDetailsActivity.class);
		RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(
				request);
	}

	private void responseForNews(String response) {
		ArrayList<String> data = (ArrayList<String>) JsonUtil
				.toJsonStrList(response);
		for (String s : data) {
			try {
				JSONObject j = new JSONObject(s);
				ProductNews p = new ProductNews();
				p.id = j.getLong("id");
				p.title = j.getString("title");
				p.imageUrl = j.getString("imageUrl");
				p.newsDate = j.getString("newsDate");
				mNewsList.add(p);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		mListView.setAdapter(new NewsAdapter(getActivity(), mNewsList));
	}

	// 所获奖项------------------------------------------------------------------------
	private void getProductAwards() {

		String url = Constant.HOST + "/honor/list.do?offset=0&fetchSize=10";
		DataRequest request = new DataRequest(Request.Method.GET, url,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						if (response != null) {
							// Log.i("sign", response);
							responseForAwards(response);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// Log.i("sign", error.getMessage());
					}
				});
		request.setTag(ProductDetailsActivity.class);
		RequestQueueSingleton.getInstance(getActivity()).addToRequestQueue(
				request);
	}

	private void responseForAwards(String response) {
		ArrayList<String> data = (ArrayList<String>) JsonUtil
				.toJsonStrList(response);
		for (String s : data) {
			try {
				JSONObject j = new JSONObject(s);
				ProductAwards p = new ProductAwards();
				p.id = j.getLong("id");
				p.title = j.getString("title");
				p.imageUrl = j.getString("imageUrl");
				p.createDate = j.getString("createDate");
				mAwardsList.add(p);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		// Log.i("sign", mAwardArray.toString());
		mListView.setAdapter(new AwardsAdapter(getActivity(), mAwardsList));

	}
}
