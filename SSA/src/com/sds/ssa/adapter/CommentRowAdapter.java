package com.sds.ssa.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sds.ssa.R;
import com.sds.ssa.vo.Comment;
import com.sds.ssa.vo.UserInfo;

public class CommentRowAdapter extends ArrayAdapter<Comment> {

	private Activity activity;
	private List<Comment> commentList;
	private Comment comment;
	private UserInfo userInfo;
	private int row;
	
	public CommentRowAdapter(Activity act, int resource, List<Comment> commentList, 
			UserInfo loginUserInfo) {
		super(act, resource, commentList);
		this.activity = act;
		this.row = resource;
		this.commentList = commentList;
		this.userInfo = loginUserInfo;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(row, null);

			holder = new ViewHolder();
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}


		if ((commentList == null) || ((position + 1) > commentList.size()))
			return view;

		comment = commentList.get(position);

		holder.userName = (TextView) view.findViewById(R.id.username);
		//holder.userDept = (TextView) view.findViewById(R.id.);
		holder.installVerName = (TextView) view.findViewById(R.id.installvername);
		holder.comment = (TextView) view.findViewById(R.id.comment);
		holder.usergrade = (ImageView) view.findViewById(R.id.usergrade);
		//holder.pbar = (ProgressBar) view.findViewById(R.id.pbar);
		
		if(comment.getReviewerName().equals(userInfo.getUserId())){ //ID일치 여부를 확인해야 함. 이름이 아니라...
			//holder.mygrade.setBackgroundResource(R.drawable.star....);
		}
		
		
		if ((holder.userName != null && null != comment.getReviewerName()
				&& comment.getReviewerName().trim().length() > 0) && (holder.userName != null && null != comment.getDeptName()
						&& comment.getDeptName().trim().length() > 0)){
			holder.userName.setText(Html.fromHtml(comment.getReviewerName() 
					+ "("+comment.getDeptName()+")"));
		}
		if (holder.installVerName != null && null != comment.getInstalledVerName()
				&& comment.getInstalledVerName().trim().length() > 0) {
			holder.installVerName.setText(Html.fromHtml(activity.getString(R.string.version)+ " "+ comment.getInstalledVerName()));
		}
		if (holder.comment != null && null != comment.getComment()
				&& comment.getComment().trim().length() > 0) {
			holder.comment.setText(Html.fromHtml(comment.getComment()));
		}
		if (holder.usergrade != null && null != comment.getAppGrade()
				&& comment.getAppGrade().trim().length() > 0) {
			float stargrade = Float.parseFloat(comment.getAppGrade());
			
			if(0 <= stargrade && stargrade < 0.25){
				holder.usergrade.setBackgroundResource(R.drawable.star_0);
			}else if(0.25 <= stargrade && stargrade < 0.75){
				holder.usergrade.setBackgroundResource(R.drawable.star_05);
			}else if(0.75 <= stargrade && stargrade < 1.25){
				holder.usergrade.setBackgroundResource(R.drawable.star_1);
			}else if(1.25 <= stargrade && stargrade < 1.75){
				holder.usergrade.setBackgroundResource(R.drawable.star_15);
			}else if(1.75 <= stargrade && stargrade < 2.25){
				holder.usergrade.setBackgroundResource(R.drawable.star_2);
			}else if(2.25 <= stargrade && stargrade < 2.75){
				holder.usergrade.setBackgroundResource(R.drawable.star_25);
			}else if(2.75 <= stargrade && stargrade < 3.25){
				holder.usergrade.setBackgroundResource(R.drawable.star_3);
			}else if(3.25 <= stargrade && stargrade < 3.75){
				holder.usergrade.setBackgroundResource(R.drawable.star_35);
			}else if(3.75 <= stargrade && stargrade < 4.25){
				holder.usergrade.setBackgroundResource(R.drawable.star_4);
			}else if(4.25 <= stargrade && stargrade < 4.75){
				holder.usergrade.setBackgroundResource(R.drawable.star_45);
			}else if(4.75 <= stargrade){
				holder.usergrade.setBackgroundResource(R.drawable.star_5);
			}
		}

		return view;
	}

	public class ViewHolder {
		public TextView userName, userDept, installVerName, comment;
		public ImageView usergrade;
		//private ProgressBar pbar;
	}

}
