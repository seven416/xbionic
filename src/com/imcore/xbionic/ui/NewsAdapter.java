package com.imcore.xbionic.ui;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.imcore.xbionic.R;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.model.ProductNews;

public class NewsAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<ProductNews> mNewsList;
	
	public NewsAdapter(Context context, ArrayList<ProductNews> commentList) {
		mContext = context;
		mNewsList = commentList;
	}

	@Override
	public int getCount() {
		return mNewsList.size();
	}

	@Override
	public Object getItem(int position) {
		return mNewsList.get(position);
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
			view = LayoutInflater.from(mContext).inflate(
					R.layout.view_product_detail_news, null);
			vh.ivNewsImg = (ImageView) view
					.findViewById(R.id.iv_pro_det_news_img);
			vh.tvNewsTitle = (TextView) view
					.findViewById(R.id.tv_pro_det_news_title);
			vh.tvNewsDate = (TextView) view
					.findViewById(R.id.tv_pro_det_news_date);

			view.setTag(vh);

		} else {
			vh = (ViewHolder) view.getTag();
		}

		String url = Constant.IMAGE_ADDRESS + mNewsList.get(position).imageUrl
				+ "_M.jpg";
		setImage(vh.ivNewsImg, url);
		vh.tvNewsTitle.setText(mNewsList.get(position).title);
		vh.tvNewsDate.setText(mNewsList.get(position).newsDate);

		return view;
	}

	private void setImage(ImageView image, String url) {
		ImageLoader loader = RequestQueueSingleton.getInstance(
				mContext.getApplicationContext()).getImageLoader();

		ImageListener listener = ImageLoader.getImageListener(image,
				R.drawable.test1, R.drawable.test1);
		loader.get(url, listener, 400, 400);

	}

	class ViewHolder {
		ImageView ivNewsImg;
		TextView tvNewsTitle;
		TextView tvNewsDate;
	}

}
