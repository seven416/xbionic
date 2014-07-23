package com.imcore.xbionic.ui;

import java.util.ArrayList;

import com.imcore.xbionic.R;
import com.imcore.xbionic.model.ProductComment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<ProductComment> mCommentList;

	public CommentAdapter(Context context, ArrayList<ProductComment> commentList) {
		mContext = context;
		mCommentList = commentList;
	}

	@Override
	public int getCount() {
		return mCommentList.size();
	}

	@Override
	public Object getItem(int position) {
		return mCommentList.get(position);
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
			view = LayoutInflater.from(mContext).inflate(
					R.layout.view_product_detail_comment, null);
			vh.tvComment = (TextView) view
					.findViewById(R.id.tv_pro_det_comment);
			vh.tvUserName = (TextView) view
					.findViewById(R.id.tv_pro_det_user_name);
			vh.tvDate = (TextView) view
					.findViewById(R.id.tv_pro_det_client_date);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}
		vh.tvComment.setText(mCommentList.get(position).comment);
		vh.tvUserName.setText(mCommentList.get(position).displayName);
		vh.tvDate.setText(mCommentList.get(position).commentDate);

		return view;
	}

	class ViewHolder {
		TextView tvComment;
		TextView tvUserName;
		TextView tvDate;
	}
}
