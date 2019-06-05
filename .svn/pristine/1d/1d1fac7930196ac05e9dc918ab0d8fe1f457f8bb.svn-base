package hrst.sczd.ui.activity.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hrst.common.ui.R;
import com.hrst.common.ui.database.dao.OperateRecordDao;
import com.hrst.common.ui.dialog.OperateRecordExportTimeDialog;
import com.hrst.common.ui.pojo.OperateRecordInfo;

import java.util.List;

import hrst.sczd.ui.adapter.OperateRecordAdapter;


/**
 * @author 关玲基
 * @describe 操作记录Activity
 * @date 2016.04.27
 */
public class OperateRecordFragment extends Fragment {
	
	private static final String TAG = "OperateRecordFragment";

	private ListView listview;
	private RelativeLayout rlExport;
	
	private OperateRecordDao operateRecordDao;
	private OperateRecordAdapter adapter;
	private List<OperateRecordInfo> list;
	private ProgressDialog mProgressDialog;
	
	private String path = "hrst/sczd/operateRecord/";
    public OperateRecordFragment(){
    	
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {

    	View view = inflater.inflate(R.layout.activity_operate_record, null);
    	initView(view);
    	initData();
    	return view;
    }

	private void initData() {
		operateRecordDao = new OperateRecordDao(getActivity());
		list = operateRecordDao.queryOperateRecord();
		adapter = new OperateRecordAdapter(getActivity(), list);
		listview.setAdapter(adapter);
	}

	private void initView(View view) {
		listview = view.findViewById(R.id.list_operate_record);
    	rlExport =  view.findViewById(R.id.rl_export);
    	rlExport.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new OperateRecordExportTimeDialog(getActivity(), list).show();
			}
		});
	}
	
	
	/**
	 * 数据拼装
	 */
	private StringBuffer dataList() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++){
			String recordDate = list.get(i).getRecordDate();
			String function = list.get(i).getFunction();
			String standard = list.get(i).getStandard();
			String targetPhoneNumber = list.get(i).getTargetPhoneNumber();
			String inducedPhoneNumber = list.get(i).getInducedPhoneNumber();
			String esn = list.get(i).getESN();
			String imsi = list.get(i).getIMIS();
			String imei = list.get(i).getIMEI();
			if(recordDate!=null){
				sb.append(list.get(i).getRecordDate()+", ");
			}		
			if(function!=null){
				sb.append(list.get(i).getFunction()+", ");
			}
			if(standard!=null){
				sb.append(list.get(i).getStandard()+", ");
			}
			if(targetPhoneNumber!=null){
				sb.append(list.get(i).getTargetPhoneNumber()+", ");
			}
			if(inducedPhoneNumber!=null){
				sb.append(list.get(i).getInducedPhoneNumber()+", ");
			}
			if(esn!=null){
				sb.append(list.get(i).getESN()+", ");
			}
			if(imsi!=null){
				sb.append(list.get(i).getIMIS()+", ");
			}
			if(imei!=null){
				sb.append(list.get(i).getIMEI()+"\r\n");
			}
			sb.append("\r\n");
		}
		return sb;
	}
	
    /**
     * 显示导出窗口
     */
    private void showExportDialog() {
    	mProgressDialog = new ProgressDialog(getActivity());
    	mProgressDialog.setTitle("正在导出文件...");
    	mProgressDialog.show();
    }
    
    /**
     * 关闭导出窗口
     */
    private void dissExportDialog(){
    	if(mProgressDialog!=null){
    		mProgressDialog.dismiss();
    	}
    }
    
    @Override
    public void onDestroy() {

    	operateRecordDao.closeDataBase();
    	dissExportDialog();
    	super.onDestroy();
    }
}
