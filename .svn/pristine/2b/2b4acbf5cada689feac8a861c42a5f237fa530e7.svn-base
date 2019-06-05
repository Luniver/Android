package com.hrst.common.ui.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hrst.common.ui.R;
import com.hrst.common.ui.database.dao.OperateRecordDao;
import com.hrst.common.ui.pojo.OperateRecordInfo;
import com.hrst.common.util.FileUtils;
import com.hrst.common.util.ToastUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author 关玲基
 * 操作记录数据导出
 * 2016-04-28
 *
 */
public class OperateRecordExportTimeDialog extends Dialog implements View.OnClickListener{

	private TextView tvGroup;
	private Context context;

	List<OperateRecordInfo> list;

    // 时间列表
    private List<Map<String, Object>> dataList;

    // 面板
    private PopupWindow pw;

    // 选择的时间下标
    private int selectTime;
    // 文件路径
    private String path = "hrst/sczd/operateRecord/";

    private OperateRecordDao operateRecordDao;

    private ProgressDialog mProgressDialog;

    private boolean isFinish = false;

	public OperateRecordExportTimeDialog(Context cn, List list){
		super(cn);
		this.context = cn;
//		this.list = list;
		init();
	}

    /**
     * 初始化
     *
     */
    private void init() {
        LayoutInflater inflat = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflat.inflate(R.layout.dialog_movegroup, null);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(layout);

        Button btnCancel = (Button) findViewById(R.id.btn_cancel);
        Button btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setEnabled(true);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        tvGroup = (TextView) findViewById(R.id.tv_group);
        tvGroup.setOnClickListener(this);
        TextView text = (TextView) findViewById(R.id.dialog_title);
        ((TextView) findViewById(R.id.dialog_title)).setText("导出使用记录");
        ((TextView) findViewById(R.id.tv_groupname)).setText("选择导出时间");
        String[] strTime = new String[] { "全部", "当天", "两天", "一周内", "一个月内",
                "一年内" };
        dataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < strTime.length; i++) {
            setData(strTime[i]);
        }
        tvGroup.setText(strTime[0]);
        selectTime = 0;
        exportTimeDialog();
    }


    /**
     * 设置数据
     *
     * @param str
     *            时间列表数据
     */
    private void setData(String str) {
        Map map = new HashMap<String, Object>();
        map.put("time", str);
        dataList.add(map);
    }

    /**
     * 弹出导出时间选择Dialog
     *
     */
    private void exportTimeDialog() {
        LayoutInflater inflat = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup menuView = (ViewGroup) inflat.inflate(
                R.layout.dialog_grouplst, null, true);
        pw = new PopupWindow(menuView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setOutsideTouchable(false);
        pw.setFocusable(true);

        // 分组列表
        SimpleAdapter apater = new SimpleAdapter(context, dataList,
                R.layout.list_group, new String[] { "time" },
                new int[] { R.id.name });
        ListView groupLst = (ListView) menuView.findViewById(R.id.groupLst);
        groupLst.setAdapter(apater);
        // 分组点击
        groupLst.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                Map<String, Object> map = dataList.get(arg2);
                tvGroup.setText(map.get("time").toString());
                selectTime = arg2;
                pw.dismiss();
            }
        });
    }
    

	@Override
	public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_cancel) {
            this.cancel();

        } else if (i == R.id.btn_ok) {
            exportDate();

        } else if (i == R.id.tv_group) {
            showDialog();

        }
		
	}

    /**
     * 导出数据
     * 
     */
    private void exportDate() {
        
        SimpleDateFormat sf;
        operateRecordDao = new OperateRecordDao(context);
        // 选择全部
        if (selectTime == 0) {
//            list = usedRecordDao.getWithinUsedRecord(null, phoneNumber);
              list = operateRecordDao.queryOperateRecord();
        } else {
            // 得到一个Calendar实例
            Calendar ca = Calendar.getInstance();
            // 设置时间为当前时间
            ca.setTime(new Date());
            // 判断要截取的时间
            switch (selectTime) {
            case 1:
                break;
            case 2:
                ca.add(Calendar.DAY_OF_MONTH, -1);
                break;
            case 3:
                ca.add(Calendar.DAY_OF_MONTH, -7);
                break;
            case 4:
                ca.add(Calendar.MONTH, -1);
                break;
            case 5:
                ca.add(Calendar.YEAR, -1);
            }
            // 得到时间
            Date now = ca.getTime();
            sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String startDate = sf.format(now);
            String endDate = sf.format(new Date());
            if(startDate.equals(endDate)){
            	String[] split = startDate.split(" ");
            	startDate = split[0]+" 00:00:00";
            }
            list = operateRecordDao.querySelectOperateRecord(startDate, endDate);
        }
        if (list.size() == 0) {
        	ToastUtils.showToast("你选择的时间段内没有使用记录");
            return;
        }
        showExportDialog();
        StringBuffer sb = dataList();
        SimpleDateFormat sf2 = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String strDate = sf2.format(date);
        String sdPath = Environment.getExternalStorageDirectory() + File.separator;
        boolean res = FileUtils.writeFile(sdPath + path + strDate + ".csv", sb.toString());
        if (res) {
            ToastUtils.showToast("文件已导出至 " + sdPath + path+strDate + ".csv");
        } else {
            ToastUtils.showToast( "导出失败");
        }
        dissExportDialog();
        cancel();
    }

    
    /**
     * 显示导出窗口
     */
    private void showExportDialog() {
    	mProgressDialog = new ProgressDialog(context);
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
    
	/**
	 * 数据拼装
	 * @return
	 */
	private StringBuffer dataList() {
		StringBuffer sb = new StringBuffer();
		sb.append("时间, 功能, 制式, 目标号码, 诱发号码, ESN, IMIS, IMEI\r\n");
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
			}else{
				sb.append(", ");
			}		
			if(function!=null){
				sb.append(list.get(i).getFunction()+", ");
			}else{
				sb.append(", ");
			}
			if(standard!=null){
				sb.append(list.get(i).getStandard()+", ");
			}else{
				sb.append(", ");
			}
			if(targetPhoneNumber!=null){
				sb.append(list.get(i).getTargetPhoneNumber()+", ");
			}else{
				sb.append(", ");
			}
			if(inducedPhoneNumber!=null){
				sb.append(list.get(i).getInducedPhoneNumber()+", ");
			}else{
				sb.append(", ");
			}
			if(esn!=null){
				sb.append(list.get(i).getESN()+", ");
			}else{
				sb.append(", ");
			}
			if(imsi!=null){
				sb.append(list.get(i).getIMIS()+", ");
			}else{
				sb.append(", ");
			}
			if(imei!=null){
				sb.append(list.get(i).getIMEI());
			}else{
				sb.append(", ");
			}
			sb.append("\r\n");
		}
		return sb;
	}
	
	/**
     * 显示DIalog
     * 
     */
    private void showDialog() {
        pw.showAsDropDown(tvGroup);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
