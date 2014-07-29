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

public class CommentRowAdapter extends ArrayAdapter<Comment> {

	private Activity activity;
	private List<Comment> commentList;
	private Comment comment;
	private int row;

	public CommentRowAdapter(Activity act, int resource, List<Comment> commentList) {
		super(act, resource, commentList);
		this.activity = act;
		this.row = resource;
		this.commentList = commentList;
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
		holder.installVerName = (TextView) view.findViewById(R.id.installvername);
		holder.comment = (TextView) view.findViewById(R.id.comment);
		holder.userGrade = (ImageView) view.findViewById(R.id.usergrade);

		if (holder.userName != null && null != comment.getReviewerName()
				&& comment.getReviewerName().trim().length() > 0) {
			holder.userName.setText(Html.fromHtml(comment.getReviewerName()));
		}

		if (holder.installVerName != null && null != comment.getInstalledVerName()
				&& comment.getInstalledVerName().trim().length() > 0) {
			holder.installVerName.setText(Html.fromHtml(comment.getInstalledVerName()));
		}
		
		if (holder.comment != null && null != comment.getComment()
				&& comment.getComment().trim().length() > 0) {
			holder.comment.setText(Html.fromHtml(comment.getComment()));
		}

		return view;
	}

	public class ViewHolder {		
		public TextView userName, installVerName, comment;
		public ImageView userGrade; 
	}

}
