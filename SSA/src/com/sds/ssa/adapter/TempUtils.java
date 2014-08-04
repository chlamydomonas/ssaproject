package com.sds.ssa.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

public class TempUtils {
	public static View getView(Context con) {
		return getImageView(con);
	}

	private static ImageView getImageView(Context con) {
		ImageView iv = new ImageView(con);
		iv.setImageResource(com.sds.ssa.R.drawable.pakistan);
		return iv;
	}
}
