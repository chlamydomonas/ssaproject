package com.sds.bas.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sds.bas.vo.AllType;
import com.sds.ssa.R;

public class AllTypeRowAdapter extends ArrayAdapter<AllType> {

	private Activity activity;
	private List<AllType> allTypeList;
	private AllType allType;
	private int row;

	public AllTypeRowAdapter(Activity act, int resource, List<AllType> allTypeList) {
		super(act, resource, allTypeList);
		this.activity = act;
		this.row = resource;
		this.allTypeList = allTypeList;
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

		if ((allTypeList == null) || ((position + 1) > allTypeList.size()))
			return view;

		allType = allTypeList.get(position);

		holder.allTypeId = (TextView) view.findViewById(R.id.alltypeid);
		holder.allTypeName = (TextView) view.findViewById(R.id.alltypename);
		holder.allTypes = (TextView) view.findViewById(R.id.alltypes);
		
		holder.allTypeId.setVisibility(View.INVISIBLE);
		if(allType.getType().equals("CATEGORY")){
			holder.allTypeName.setPadding(20, 0, 0, 0);
		}
		holder.allTypes.setVisibility(View.INVISIBLE);

		if (holder.allTypeId != null && null != allType.getId()
				&& allType.getId().trim().length() > 0) {
			holder.allTypeId.setText(Html.fromHtml(allType.getId()));
		}
		if (holder.allTypeName != null && null != allType.getName()
				&& allType.getName().trim().length() > 0) {
			holder.allTypeName.setText(Html.fromHtml(allType.getName()));
		}
		if (holder.allTypes != null && null != allType.getType()
				&& allType.getType().trim().length() > 0) {
			holder.allTypes.setText(Html.fromHtml(allType.getType()));
		}
		return view;
	}

	public class ViewHolder {
		//public TextView categoryId, categoryName;
		public TextView allTypeId, allTypeName, allTypes;
	}
}
