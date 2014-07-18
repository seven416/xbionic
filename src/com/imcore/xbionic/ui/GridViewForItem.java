package com.imcore.xbionic.ui;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.GridView;


public class GridViewForItem extends GridView {

	public GridViewForItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void  onMeasure(int widthMeasureSpec,int heightMeasureSpec) {
		int expendSpec=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expendSpec);
	}

}
