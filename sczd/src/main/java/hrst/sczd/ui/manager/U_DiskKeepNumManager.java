package hrst.sczd.ui.manager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import hrst.sczd.agreement.manager.InteractiveManager;
import hrst.sczd.agreement.manager.vo.QueryTfKeepNumberFile;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_DELETE_THE_SPECIFIED_FILE_FOR_TF;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_DELETE_THE_SPECIFIED_FILE_FOR_TF_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_KEEP_NUMBER_COPY_TO_U_DISK_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_QUERY_TF_KEEP_NUMBER_FILE_LIST;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_CLEAR_TF_KEEP_NUMBER_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_SELECT_KEEP_NUMBER_FILE_COPY_TO_U_DISK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_SELECT_KEEP_NUMBER_FILE_COPY_TO_U_DISK_ACK;
import hrst.sczd.ui.adapter.U_DiskKeepNumAdapter;
import hrst.sczd.ui.vo.U_DiskKeepNumInfo;
import hrst.sczd.utils.ToastUtil;

/**
 * U盘存数管理
 * @author glj
 * 2018-01-29
 */
public class U_DiskKeepNumManager {

	private static final int DISS_KEEP_NUM_DIALOG = 0x001;
	private static final int CHECK_PACKET_IS_LOST = 0x101;
	
	private Activity activity;
	
	private ListView tfList;
	
	private List<U_DiskKeepNumInfo> list = new ArrayList<U_DiskKeepNumInfo>(); 
	
	private HashMap<String, Boolean> map = new HashMap<String, Boolean>();
	
	private U_DiskKeepNumAdapter adaper;
	/** 弹窗提示信息 */
	private String toastMsg;
	/** 进度弹窗 */
//	private KeepNumDialog dialog;
	
	private boolean isChecked;

	/** 第一次查询是否收到应答 */
	private boolean isReceiveAck = false;
	
	/** 记录下发文件位置 */
	private int sendFileIndex = 1;

	/** 由于存在丢包, 所以设置重查机制 */
	private short queryFileNum = 1;

	private short totalFileNum = 0;
	
	/** 0 指定删除 1删除全部 2指定复制 3复制全部*/
	private int operatingState = -1;
	
	private ArrayList<String> clearList = new ArrayList<String>();
	
	private ArrayList<String> copyList = new ArrayList<String>();
	
	private Handler handler = new Handler (Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			
			case DISS_KEEP_NUM_DIALOG:
				//关闭弹窗时对记录位置进行初始化
				sendFileIndex = 1;
				clearList.clear();
				copyList.clear();
				list.clear();
				adaper.setDataList(list);

				if(null != progressDialog){
					PDA_TO_MCU_QUERY_TF_KEEP_NUMBER_FILE_LIST obj = new PDA_TO_MCU_QUERY_TF_KEEP_NUMBER_FILE_LIST();
					obj.setUcFileName_a((short) 1);
					sendQueryTfKeepFileList(obj);
					progressDialog.dismiss();
					progressDialog = null;
				}
				/*
				switch (operatingState) {
				//指定删除
				case 0:
					clearList.clear();
					list.clear();
					map.clear();
					PDA_TO_MCU_QUERY_TF_KEEP_NUMBER_FILE_LIST  listInfo = new PDA_TO_MCU_QUERY_TF_KEEP_NUMBER_FILE_LIST();
					listInfo.setUcFileName_a((short)1);
					InteractiveManager.getInstance(activity).sendQueryTfKeepNumberFileList(listInfo);
					break;
				//全部删除
				case 1:
					clearList.clear();
					list.clear();
					map.clear();
					adaper.setDataList(list);
					break;
				//指定复制
				case 2:
					
					break;
				//全部删除
				case 3:
					
					break;

				}
				*/
				break;
			case CHECK_PACKET_IS_LOST: // 查询文件是否查询完
				if (isReceiveAck) {
					if(queryFileNum <= totalFileNum){
						sendQueryTfKeepFileList(queryObj);
					}else {
						handler.removeMessages(CHECK_PACKET_IS_LOST);
					}
				}else {
					sendQueryTfKeepFileList(queryObj);
				}
				break;
			default:
				break;
			}
		}
		
	};
	private PDA_TO_MCU_QUERY_TF_KEEP_NUMBER_FILE_LIST queryObj;
	/**
	 * 是否复制存数完毕
	 */
	private boolean hasCopyFinished = true;
	private ProgressDialog progressDialog;
	
	/**
	 * 构造函数
	 * @param activity
	 * @param tfList 
	 */
	public U_DiskKeepNumManager(Activity activity, ListView tfList) {
		this.activity = activity;
		this.tfList = tfList;
		isReceiveAck = false;
		queryObj = new PDA_TO_MCU_QUERY_TF_KEEP_NUMBER_FILE_LIST();
		queryObj.setUcFileName_a(queryFileNum);
	}

	/**
	 * 保存TF_FIleList
	 */
	public void saveTF_FIleList(QueryTfKeepNumberFile obj) {
		isReceiveAck = true;
		totalFileNum = obj.getTotalFile();
		if (totalFileNum == 0){
			Log.d("goo", "total file num:" + totalFileNum);
			list.clear();
		}else{
			U_DiskKeepNumInfo info = new U_DiskKeepNumInfo();
			info.setFileName(obj.getFileName());
			info.setCheck(false);
			list.add(info);
			System.out.println("goo file: " + obj);

			queryFileNum = (short) (obj.getFileNum()+1);
			// 查询成功,先移除上次的检测, 重新延时500秒检测
			handler.removeMessages(CHECK_PACKET_IS_LOST);
			//未获取完列表文件则继续获取
			if (queryFileNum <= obj.getTotalFile()) {
				queryObj.setUcFileName_a(queryFileNum);
				InteractiveManager.getInstance().sendQueryTfKeepNumberFileList(queryObj);
				handler.sendEmptyMessageDelayed(CHECK_PACKET_IS_LOST, 500);
			}
			if(null == adaper){
				adaper = new U_DiskKeepNumAdapter(activity){

					@Override
					public void setSelectFileDir(String fileName, boolean isChecked) {
						map.put(fileName, isChecked);
					}

				};
                tfList.setAdapter(adaper);
			}
		}
		if(adaper != null && tfList != null){
			adaper.setDataList(list);
		}
	}

	/**
	 * 设置选择状态
	 * @param selectStateTxt
	 * @param isChecked
	 * 			
	 */
	public void setSelectState(TextView selectStateTxt, boolean isChecked) {
		this.isChecked = isChecked;
		//根据选择状态进行数据处理
		if (isChecked) {
			selectStateTxt.setText("取消");
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setCheck(true);
			}
		} else {
			selectStateTxt.setText("全选");
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setCheck(false);
			}
		}
		//更新选择状态
		if (null != adaper){
			adaper.setDataList(list);
		}
	}

	/**
	 * 清除TF的数据
	 */
	public void setClearData() {
		// 默认清除所有
		isChecked=true;
		if (isChecked) {
			operatingState = 1;
			if(totalFileNum == 0){
				ToastUtil.showToast(activity,"文件已清除");
			}else {
				//清除全部存数
				InteractiveManager.getInstance().sendRequestClearTfKeepNumber();
			}
		} else {
			operatingState = 0;
			ArrayList<String> clearList = new ArrayList<String>();
			
			//迭代Map
			Set<Entry<String,Boolean>> entrySet = map.entrySet();
			Iterator<Entry<String, Boolean>> it = entrySet.iterator();
			
			while(it.hasNext()){
				//获取每个元素的数据
				Entry<String, Boolean> entry = it.next();
				String key = entry.getKey();
				boolean value = entry.getValue();
				//选中则添加到集合中
				if (value) {
					clearList.add(key);
				}
			}
			
			PDA_TO_MCU_DELETE_THE_SPECIFIED_FILE_FOR_TF obj = new PDA_TO_MCU_DELETE_THE_SPECIFIED_FILE_FOR_TF();
			byte[] bytes = clearList.get(0).getBytes();
			obj.setCfileName_b(bytes);
			//删除单个指定文件
			InteractiveManager.getInstance().sendDeleteTheSpecifiedFileForTf(obj);
		}
		
	}

	/**
	 * 将存数拷贝到U盘
	 * (目前只实现了全部拷贝, 不能选择拷贝)
	 */
	public void setCopyToUdisk() {
		isChecked = true;
		operatingState = 3;
		//拷贝全部存数
		// 默认全部拷贝
		if (isChecked) {
			if (totalFileNum == 0){
				ToastUtil.showToast(activity, "无文件");
			}else {
				operatingState = 3;
				//拷贝全部存数
				InteractiveManager.getInstance().sendKeepNumberCopyToUDisk();
				hasCopyFinished = false;
			}
		} else {
			operatingState = 2;
			//迭代Map
			Set<Entry<String,Boolean>> entrySet = map.entrySet();
			Iterator<Entry<String, Boolean>> it = entrySet.iterator();
			
			while(it.hasNext()){
				//获取每个元素的数据
				Entry<String, Boolean> entry = it.next();
				String key = entry.getKey();
				boolean value = entry.getValue();
				//选中则添加到集合中
				if (value) {
					copyList.add(key);
				}
			}
			if(copyList.size() < 1){
				ToastUtil.showToast(activity, "请选择文件");
				return;
			}
			//发送每个勾选的数据
			PDA_TO_MCU_SELECT_KEEP_NUMBER_FILE_COPY_TO_U_DISK obj = new PDA_TO_MCU_SELECT_KEEP_NUMBER_FILE_COPY_TO_U_DISK();
			byte[] bytes = copyList.get(0).getBytes();
			obj.setCfileName_b(bytes);
			//拷贝单个指定文件
			InteractiveManager.getInstance().sendSelectKeepNumberFileCopyToUDisk(obj);
		}
		
	}

	/**
	 * 复制单个存数到U盘的应答
	 * @param obj
	 */
	public void selectTfCopyToUdiskResult(
			PDA_TO_MCU_SELECT_KEEP_NUMBER_FILE_COPY_TO_U_DISK_ACK obj) {
		//		0xFF　U盘空间不足
		//		0xFE  复制失败
		//		0xFD  TF卡中文件不存在
		//		0xFC  确定接收到请求
		//		0 ~ 100(%)复制进度
		byte result = obj.getcResult_a();
		switch (result) {
		case (byte) 0xFF:
			ToastUtil.showToastLong(activity, "U盘空间不足");
			break;
		case (byte) 0xFE:
			ToastUtil.showToastLong(activity, "复制失败");
			break;
		case (byte) 0xFD:
			ToastUtil.showToastLong(activity, "TF卡中文件不存在");
		break;
		//下发下一个请求
		case (byte) 0xFC:
			// 记录位置不大于集合的长度时发送下一包
			if (copyList.size() < sendFileIndex) {
				//发送每个勾选的数据
				PDA_TO_MCU_SELECT_KEEP_NUMBER_FILE_COPY_TO_U_DISK info = new PDA_TO_MCU_SELECT_KEEP_NUMBER_FILE_COPY_TO_U_DISK();
				byte[] bytes = copyList.get(sendFileIndex).getBytes();
				info.setCfileName_b(bytes);
				//拷贝单个指定文件
				InteractiveManager.getInstance().sendSelectKeepNumberFileCopyToUDisk(info);
			}

		break;
		default:
			showProgress(result, 1);
			break;
		}
		
	}

	/**
	 * 复制全部存数到U盘的应答
	 * @param obj
	 */
	public void keepNumberCopyToUdiskResult(
			PDA_TO_MCU_KEEP_NUMBER_COPY_TO_U_DISK_ACK obj) {
		byte result = obj.getcResult_a();
		switch (result) {
		case (byte) 0xFF:
			ToastUtil.showToastLong(activity, "U盘空间不足");
			break;
		case (byte) 0xFE:
			ToastUtil.showToastLong(activity, "复制失败");
			break;
		case (byte) 0xFD:
			ToastUtil.showToastLong(activity, "TF卡文件名不存在");
			break;
		case (byte) 0xFC:
			ToastUtil.showToastLong(activity, "确定收到请求");
			break;
		case (byte) 0xFB:
			ToastUtil.showToastLong(activity, "复制正在进行");
			break;
		case (byte) 0xFA:
			ToastUtil.showToastLong(activity, "U盘未插入");
			break;
		default:
			showProgress(result, 1);
			break;
		}
		
	}

	/**
	 * 清除指定存数的应答
	 * @param obj
	 */
	public void selectTfDelectFileResult(
			PDA_TO_MCU_DELETE_THE_SPECIFIED_FILE_FOR_TF_ACK obj) {
		//		0xFF　删除失败
		//		0xFE  TF卡中文件不存在
		//		0xFC  确定接收命令 	
		byte result = obj.getcResult_a();
		switch (result) {
		case (byte) 0xFF:
			ToastUtil.showToastLong(activity, "删除失败");
			break;
		case (byte) 0xFE:
			ToastUtil.showToastLong(activity, "TF卡中文件不存在");
			break;
		//下发下一个请求
		case (byte) 0xFC:
			//记录位置不大于集合的长度时发送下一包
			if (clearList.size() < sendFileIndex) {
				PDA_TO_MCU_DELETE_THE_SPECIFIED_FILE_FOR_TF info = new PDA_TO_MCU_DELETE_THE_SPECIFIED_FILE_FOR_TF();
				byte[] bytes = clearList.get(sendFileIndex).getBytes();
				info.setCfileName_b(bytes);
				//删除单个指定文件
				InteractiveManager.getInstance().sendDeleteTheSpecifiedFileForTf(info);
			}
			break;

		default:
			showProgress(result, 0);
			break;
		}
		
	}

	/**
	 * 清除全部存数的应答
	 * @param obj
	 */
	public void clearTfKeepNumberResult(
			PDA_TO_MCU_REQUEST_CLEAR_TF_KEEP_NUMBER_ACK obj) {
		byte result = obj.getcResult_a();
		switch (result) {
		case (byte) 0xFF:
			ToastUtil.showToastLong(activity, "清除失败");
			break;
		default:
			showProgress(result, 0);
			break;
		}
	}
	
	/**
	 * 显示删除/复制进度窗
	 * @param result
	 * 			进度
	 * @param type
	 * 			类型
	 * 			0	删除
	 * 			1	复制
	 */
	private void showProgress (byte result, int type) {
		
		switch (type) {
		case 0:
			//如果弹窗为null时创建弹窗
			if (progressDialog == null) {
				showProgressDialog("正在删除存数...");
			}
			toastMsg = "删除完成";
			break;
			
		case 1:
			//如果弹窗为null时创建弹窗
			if (progressDialog == null) {
				showProgressDialog("正在复制存数...");
			}
			toastMsg = "复制完成";
			break;

		default:
			break;
		}
//		System.out.println("goo copy progress: " + result);
		progressDialog.setProgress(result);
		
		//停留1秒后关闭弹窗
		if (result == 100) {
			handler.sendEmptyMessageDelayed(DISS_KEEP_NUM_DIALOG, 1000);
		}
	}
	/**
	 * 显示进度条
	 */
	private void showProgressDialog(String title) {
	    final int MAX_PROGRESS = 100;
	    if (progressDialog == null){
			progressDialog = new ProgressDialog(activity);
			progressDialog.setProgress(0);
			progressDialog.setTitle(title);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setMax(MAX_PROGRESS);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setOnCancelListener(new OnCancelListener() {


				@Override
				public void onCancel(DialogInterface dialog) {
					ToastUtil.showToast(activity, toastMsg);
					InteractiveManager.getInstance().sendStopKeepNumberFileCopyToUDisk();
					progressDialog = null;
				}
			});
		}
	    progressDialog.show();
	}
	
	/** 查询 tf 文件数 */
	public void sendQueryTfKeepFileList(
			PDA_TO_MCU_QUERY_TF_KEEP_NUMBER_FILE_LIST obj) {
		InteractiveManager.getInstance().sendQueryTfKeepNumberFileList(obj);
		handler.sendEmptyMessageDelayed(CHECK_PACKET_IS_LOST, 500);
	}

	public void removeAllMessages() {

		handler.removeMessages(CHECK_PACKET_IS_LOST);
	}

	public boolean hasCopyFinished() {
		return hasCopyFinished ;
	}

}
