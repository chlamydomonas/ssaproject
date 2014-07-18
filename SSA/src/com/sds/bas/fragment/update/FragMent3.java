package com.sds.bas.fragment.update;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.ssa.bas.R;

@SuppressLint("ValidFragment")
public class FragMent3 extends Fragment {

	Context mContext;
	
	public FragMent3(Context context) {
		mContext = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/*ImageView image = new ImageView(getActivity());
		image.setLayoutParams(new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		image.setBackgroundResource(R.drawable.car);
		return image;*/
		
		View view = inflater.inflate(R.layout.gridview, null);
		//GridView listView = (GridView) view.findViewById(R.id.mainGrid);
		//listView.setAdapter(new Adapter());
		return view;
	}
}
