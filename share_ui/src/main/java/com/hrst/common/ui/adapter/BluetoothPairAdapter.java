package com.hrst.common.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hrst.common.ui.R;
import com.hrst.common.ui.vo.BlePairItemInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 蓝牙配对界面 ListView adapter
 *
 * by Sophimp
 * on 2018/4/24
 */
public class BluetoothPairAdapter extends BaseAdapter {
	private Context context;
	private List<BlePairItemInfo> list;

	public List<BlePairItemInfo> getList() {
		return list;
	}

	public void setList(List<BlePairItemInfo> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	public BluetoothPairAdapter(Context context) {
		this.context = context;
		list = new ArrayList<>();

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.bluetooth_item, null);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_rssi = (TextView) convertView.findViewById(R.id.tv_rssi);
			holder.tvPaired = (TextView) convertView
					.findViewById(R.id.tv_paired);
			holder.rbChecked = (RadioButton) convertView
					.findViewById(R.id.rb_checked);
			holder.iv_operate = (ImageView) convertView
					.findViewById(R.id.iv_operate);
			holder.rl_onclickimage = (RelativeLayout) convertView
					.findViewById(R.id.rl_onclickimage);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		BlePairItemInfo itemInfo = list.get(position);
		holder.tv_name.setText(itemInfo.getName());
		if (itemInfo.isPaired()) {
			holder.tv_name.setText(itemInfo.getName() + " [已配对]");
		} else {
			holder.tv_name.setText(itemInfo.getName() + " [未配对]");
		}
		holder.tvPaired.setText(itemInfo.getAddress());
		holder.tv_rssi.setText(itemInfo.getRssi());
		holder.rbChecked.setChecked(itemInfo.isChecked());
		if (itemInfo.isChecked()) {
			holder.iv_operate.setVisibility(View.VISIBLE);
			holder.rl_onclickimage.setVisibility(View.VISIBLE);
		} else {
			holder.iv_operate.setVisibility(View.INVISIBLE);
			holder.rl_onclickimage.setVisibility(View.GONE);
		}
		holder.rbChecked.setClickable(false);// 设置不让RadioButton获得焦点
		holder.rbChecked.setFocusable(false);
		holder.rbChecked.setFocusableInTouchMode(false);
		return convertView;
	}

	private class ViewHolder {
		TextView tv_name;
		TextView tvPaired;
		ImageView iv_operate;
		RadioButton rbChecked;
		RelativeLayout rl_onclickimage;
		TextView tv_rssi;
	}

}
