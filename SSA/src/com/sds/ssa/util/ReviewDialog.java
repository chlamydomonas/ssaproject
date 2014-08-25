package com.sds.ssa.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sds.ssa.R;

public class ReviewDialog extends Dialog{

	private TextView titleView;
	private ImageView rateStar1, rateStar2, rateStar3, rateStar4, rateStar5;
	private Button cancelBtn, confirmBtn;
	private int star;
	private View.OnClickListener cancelClickListener;
	private View.OnClickListener confirmClickListener;
	
	public ReviewDialog(Context context , int star , 
			View.OnClickListener cancelListener, View.OnClickListener confirmListener) {
		super(context , android.R.style.Theme_Translucent_NoTitleBar);
		this.star = star;
		this.cancelClickListener = cancelListener;
		this.confirmClickListener = confirmListener;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();    
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		lpWindow.dimAmount = 0.8f;
		getWindow().setAttributes(lpWindow);
		
		setContentView(R.layout.review_dialog);
		
		setLayout();
		setTitle();
		setRateStar0();
		setRateStar1();
		setRateStar2();
		setRateStar3();
		setRateStar4();
		setRateStar5();
		setCancelClickListener(cancelClickListener);
		setConfirmClickListener(confirmClickListener);
	}

	private void setLayout(){
		titleView = (TextView) findViewById(R.id.tv_title);
		rateStar1 = (ImageView) findViewById(R.id.ratestar1);
		rateStar2 = (ImageView) findViewById(R.id.ratestar2);
		rateStar3 = (ImageView) findViewById(R.id.ratestar3);
		rateStar4 = (ImageView) findViewById(R.id.ratestar4);
		rateStar5 = (ImageView) findViewById(R.id.ratestar5);
		
		cancelBtn = (Button) findViewById(R.id.cancel_btn);
		confirmBtn = (Button) findViewById(R.id.confirm_btn);
	}
	
	private void setTitle(){
		titleView.setText(R.string.write_review);
	}
	
	private void setRateStar0() {
		reset();
	}
	
	private void setRateStar1() {
		if(star == 1){
			rateStar1.setImageResource(R.drawable.filled_star);
		}
		rateStar1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				reset();
				rateStar1.setImageResource(R.drawable.filled_star);
          	}
		});
	}

	private void setRateStar2() {
		if(star == 2){
			rateStar1.setImageResource(R.drawable.filled_star);
			rateStar2.setImageResource(R.drawable.filled_star);
		}
		rateStar2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				reset();
				rateStar1.setImageResource(R.drawable.filled_star);
				rateStar2.setImageResource(R.drawable.filled_star);
          	}
		});
	}
	
	private void setRateStar3() {
		if(star == 3){
			rateStar1.setImageResource(R.drawable.filled_star);
			rateStar2.setImageResource(R.drawable.filled_star);
			rateStar3.setImageResource(R.drawable.filled_star);
		}
		rateStar3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				reset();
				rateStar1.setImageResource(R.drawable.filled_star);
				rateStar2.setImageResource(R.drawable.filled_star);
				rateStar3.setImageResource(R.drawable.filled_star);
          	}
		});
	}

	private void setRateStar4() {
		if(star == 4){
			rateStar1.setImageResource(R.drawable.filled_star);
			rateStar2.setImageResource(R.drawable.filled_star);
			rateStar3.setImageResource(R.drawable.filled_star);
			rateStar4.setImageResource(R.drawable.filled_star);	
		}
		rateStar4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				reset();
				rateStar1.setImageResource(R.drawable.filled_star);
				rateStar2.setImageResource(R.drawable.filled_star);
				rateStar3.setImageResource(R.drawable.filled_star);
				rateStar4.setImageResource(R.drawable.filled_star);
          	}
		});
	}
	
	private void setRateStar5() {
		if(star == 5){
			rateStar1.setImageResource(R.drawable.filled_star);
			rateStar2.setImageResource(R.drawable.filled_star);
			rateStar3.setImageResource(R.drawable.filled_star);
			rateStar4.setImageResource(R.drawable.filled_star);	
			rateStar5.setImageResource(R.drawable.filled_star);
		}
		rateStar5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				reset();
				rateStar1.setImageResource(R.drawable.filled_star);
				rateStar2.setImageResource(R.drawable.filled_star);
				rateStar3.setImageResource(R.drawable.filled_star);
				rateStar4.setImageResource(R.drawable.filled_star);
				rateStar5.setImageResource(R.drawable.filled_star);
          	}
		});
	}
	
	private void setCancelClickListener(View.OnClickListener cancelListener){
		cancelBtn.setOnClickListener(cancelListener);
	}


	private void setConfirmClickListener(View.OnClickListener confirmListener){
		confirmBtn.setOnClickListener(confirmListener);
	}
	
	private void reset() {
		rateStar1.setImageResource(R.drawable.blank_star);
		rateStar2.setImageResource(R.drawable.blank_star);
		rateStar3.setImageResource(R.drawable.blank_star);
		rateStar4.setImageResource(R.drawable.blank_star);
		rateStar5.setImageResource(R.drawable.blank_star);
	}
	
}