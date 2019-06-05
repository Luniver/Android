package hrst.sczd.ui.adapter;

import hrst.sczd.R;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * EditText可下拉选择适配器
 * @author glj
 * 2017-08-18
 *
 */
public class DropEditTextBaseAdapter extends BaseAdapter {
	
	private Activity mActivity;
	private String[] datasource;
	
	public DropEditTextBaseAdapter (Activity activity, String[] datasource) {
		mActivity = activity;
		this.datasource = datasource;
	}

	@Override
	public int getCount() {
		return datasource.length;
	}

	@Override
	public Object getItem(int position) {

		return datasource[position];
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView tv = new TextView(mActivity);
		tv.setTextSize(19);
		tv.setTextColor(mActivity.getResources().getColor(R.color.light_blue));
		if (position < datasource.length){
			tv.setText(datasource[position]);
		}
		return tv;
	}

}
