package hrst.sczd.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hrst.sczd.R;
import hrst.sczd.ui.vo.U_DiskKeepNumInfo;

/**
 * TF文件列表适配器
 * @author glj
 * 2018-01-29
 */
public abstract class U_DiskKeepNumAdapter extends BaseAdapter {
	
	private Context context;
	
	private List<U_DiskKeepNumInfo> datasource = new ArrayList<>();
	
	/**
	 * 初始化适配器
	 * @param context
	 */
	public U_DiskKeepNumAdapter (Context context) {
		this.context = context;
	}
	
	/**
	 * 设置展示数据
	 * @param list 
	 */
	public void setDataList(List<U_DiskKeepNumInfo> list){
	    datasource.clear();
		datasource.addAll(list);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {

		return datasource.size();
	}

	@Override
	public Object getItem(int position) {

		return datasource.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		ViewHolder holder = null;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(context).inflate(R.layout.adapter_u_disk_keep_num, null);
			holder.fileDirTxt = (TextView) view.findViewById(R.id.txt_file_dir_name);
			holder.isCheckCb = (CheckBox) view.findViewById(R.id.cb_select_tf_file);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		final String fileName = datasource.get(position).getFileName();
		holder.fileDirTxt.setText(datasource.get(position).getFileName());
		holder.isCheckCb.setChecked(datasource.get(position).isCheck());
		holder.isCheckCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				setSelectFileDir(fileName, isChecked);
			}
		});
		return view;
	}
	
	/**
	 * 设置选择数据
	 * @param fileName
	 * 			文件名
	 * @param isChecked
	 * 			状态
	 */
	public abstract void setSelectFileDir (String fileName, boolean isChecked);
	
	private class ViewHolder {
		TextView fileDirTxt;
		CheckBox isCheckCb;
	}

}
