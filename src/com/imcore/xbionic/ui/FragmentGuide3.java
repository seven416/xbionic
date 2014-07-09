package com.imcore.xbionic.ui;

import com.imcore.xbionic.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentGuide3 extends Fragment {
	
	private Button btnExperience;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_guide3, null);
		
		btnExperience = (Button) view.findViewById(R.id.btn_experience);
		btnExperience.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),MainActivity.class);
				startActivity(intent);
			}
		});
		
		return view;
	}

}
