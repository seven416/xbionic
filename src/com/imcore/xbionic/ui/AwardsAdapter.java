package com.imcore.xbionic.ui;

import java.util.ArrayList;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.imcore.xbionic.R;
import com.imcore.xbionic.http.Constant;
import com.imcore.xbionic.http.RequestQueueSingleton;
import com.imcore.xbionic.model.ProductAwards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AwardsAdapter extends BaseAdapter {
	
	private Context mContext;
	private ArrayList<ProductAwards> mAwardsList;

	public AwardsAdapter(Context context, ArrayList<ProductAwards> commentList) {
		mContext = context;
		mAwardsList = commentList;
	}

	@Override
	public int getCount() {
		return mAwardsList.size();
	}

	@Override
	public Object getItem(int position) {
		return mAwardsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder vh;
		if(view == null) {
			vh = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.view_product_detail_awards, null);
			vh.ivAwardsImg = (ImageView) view.findViewById(R.id.iv_pro_det_awards_img);
			vh.tvAwardsTitle = (TextView) view.findViewById(R.id.tv_pro_det_awards_title);
			vh.tvAwardsDate = (TextView) view.findViewById(R.id.tv_pro_det_awards_date);
			view.setTag(vh);
			
		} else {
			vh = (ViewHolder) view.getTag();
		}
		
		String url = Constant.IMAGE_ADDRESS + mAwardsList.get(position).imageUrl + "_S.jpg";
		setImage(vh.ivAwardsImg, url);
		vh.tvAwardsTitle.setText(mAwardsList.get(position).title);
		vh.tvAwardsDate.setText(mAwardsList.get(position).createDate);
		return view;
	}
	
	private void setImage(ImageView image, String url) {
		ImageLoader loader = RequestQueueSingleton.getInstance(mContext.getApplicationContext()).getImageLoader();
		
		ImageListener listener = ImageLoader.getImageListener(image, R.drawable.test1, R.drawable.test1);
		
		loader.get(url, listener, 400, 400);
		
	}

	class ViewHolder {
		ImageView ivAwardsImg;
		TextView tvAwardsTitle;
		TextView tvAwardsDate;
	}

}
