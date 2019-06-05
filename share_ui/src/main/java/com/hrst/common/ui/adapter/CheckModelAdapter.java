package com.hrst.common.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hrst.common.base.BaseApp;
import com.hrst.common.ui.R;

public class CheckModelAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private View.OnClickListener lisener;
	private String[] datasources = {};

	public CheckModelAdapter(String[] datasources) {
	    if (null != datasources){
	    	this.datasources = datasources;
		}
		inflater = LayoutInflater.from(BaseApp.getCtx());
	}

	@Override
	public int getCount() {
		return datasources.length;
	}

	@Override
	public Object getItem(int position) {
		return datasources[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_list_dialog, null);
			holder = new ViewHolder();
			holder.check = convertView.findViewById(R.id.checkBox);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.check.setText(datasources[position]);
		return convertView;
	}

	public void updateDatasource(String[] datas) {
	    this.datasources = datas;
	    notifyDataSetChanged();
	}

	public class ViewHolder {
		public TextView check;
	}

}
