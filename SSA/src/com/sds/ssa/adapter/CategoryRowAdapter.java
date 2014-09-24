package com.sds.ssa.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sds.ssa.R;
import com.sds.ssa.vo.Category;

public class CategoryRowAdapter extends ArrayAdapter<Category> {

	private Activity activity;
	private List<Category> categoryList;
	private Category category;
	private int row;

	public CategoryRowAdapter(Activity act, int resource, List<Category> categoryList) {
		super(act, resource, categoryList);
		this.activity = act;
		this.row = resource;
		this.categoryList = categoryList;
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

		if ((categoryList == null) || ((position + 1) > categoryList.size()))
			return view;

		category = categoryList.get(position);

		holder.categoryId = (TextView) view.findViewById(R.id.categoryid);
		holder.categoryName = (TextView) view.findViewById(R.id.categoryname);
		holder.categoryId.setVisibility(View.INVISIBLE);

		if (holder.categoryId != null && null != category.getId()
				&& category.getId().trim().length() > 0) {
			holder.categoryId.setText(Html.fromHtml(category.getId()));
		}
		if (holder.categoryName != null && null != category.getName()
				&& category.getName().trim().length() > 0) {
			holder.categoryName.setText(Html.fromHtml(category.getName()));
		}

		return view;
	}

	public class ViewHolder {
		public TextView categoryId, categoryName;
	}
}
