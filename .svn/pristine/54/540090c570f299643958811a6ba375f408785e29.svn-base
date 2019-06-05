package hrst.sczd.ui.activity.dialog;

import hrst.sczd.R;
import hrst.sczd.ui.vo.BluetoothGlobal;
import hrst.sczd.utils.UserConfig;
import hrst.sczd.utils.Utils;

import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author 赵耿忠
 * @describe 修改蓝牙名称Dialog
 * @date 2014.05.23
 * @version 1.1.1.3
 * 修改者，修改日期，修改内容
*/
public class UpdateBluetoothNameDialog extends Dialog implements
		android.view.View.OnClickListener, TextWatcher {
	private final Context context;
	// 输入框
	private EditText etGroup;
	private TextView tvGroupname;
	private TextView tvBluetoothName;
	private Button btnOk;
	private Button btnCancel;
	private Handler handler;
	private Map<String, Object> map;
	private String editStr = "蓝牙名称";

	public UpdateBluetoothNameDialog(final Context context,
			Map<String, Object> map, TextView bluetoothName) {
		super(context);
		this.context = context;
		this.handler = handler;
		this.map = map;
		this.tvBluetoothName = bluetoothName;
		this.setCanceledOnTouchOutside(false);
		try {
			registerBoradcastReceiver();
		} catch (Exception e) {
			e.printStackTrace();
		}
		init();
	}

	/** 
	 * 注册广播 
	 */
	public void registerBoradcastReceiver() throws Exception {
		// 注册广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("cn.hrst.bluetooth");
		context.registerReceiver(broadcastReceiver, intentFilter);

		// 注册更新界面广播
		IntentFilter intentFilter1 = new IntentFilter();
		intentFilter1.addAction("cn.hrst.refreshUI");
		context.registerReceiver(UIrefreshbroadcastReceiver, intentFilter1);
	}

	private void init() {
		LayoutInflater inflat = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflat.inflate(R.layout.dialog_addgroup, null);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(layout);

		((TextView) layout.findViewById(R.id.tv_title)).setText("修改蓝牙名称");
		((TextView) layout.findViewById(R.id.tv_groupname)).setText("蓝牙名称");

		btnCancel = (Button) layout.findViewById(R.id.btn_cancel);
		btnOk = (Button) layout.findViewById(R.id.btn_ok);
		btnCancel = (Button) layout.findViewById(R.id.btn_cancel);
		etGroup = (EditText) layout.findViewById(R.id.et_group);
		tvGroupname = (TextView) layout.findViewById(R.id.tv_groupname);
		InputFilter[] filter = { new LengthFilter(15) };
		etGroup.setFilters(filter);
		etGroup.setText(map.get("name").toString());
		etGroup.addTextChangedListener(this);
		btnCancel.setOnClickListener(this);
		btnOk.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:
			this.cancel();
			break;
		case R.id.btn_ok:
			updateBluetoothName();
			break;
		}
	}

	@Override
	public void cancel() {
		try {
			context.unregisterReceiver(broadcastReceiver);
			context.unregisterReceiver(UIrefreshbroadcastReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.cancel();
	}

	/**
	 * 修改蓝牙名称
	 * 
	 */
	private void updateBluetoothName() {
//		String groupName = etGroup.getText().toString();
//		McuBluetoothAck blue = new McuBluetoothAck();
//		blue.setAsName_a(groupName);
//		//McuSender.getInstance(context).bluetoothName(blue);
//		btnCancel.setEnabled(false);
//		UserConfig.ShowToast(context, "正在修改蓝牙名称,请勿关闭窗口！");
	}
	
	/**
	 * 用户界面活动上下文
	 */
	private BroadcastReceiver UIrefreshbroadcastReceiver = new BroadcastReceiver() {
		@SuppressLint("NewApi")
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("cn.hrst.refreshUI")) {
				String stractivity = intent.getStringExtra("activityname");
				if (stractivity != null && !stractivity.equals("")
						&& stractivity.equals("McuBluetooth")) {
					UserConfig.ShowToast(context, "修改失败!");
					cancel();
				}
			}
		}
	};
	
	/**
	 * 获取蓝牙连接状态的广播
	 */
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("cn.hrst.bluetooth")) {
				int author = Integer.parseInt(intent
						.getStringExtra("bluetooth"));
				switch (author) {
				case BluetoothGlobal.SFIUI_DISCONNECT:
					String groupName = etGroup.getText().toString();
					String blueName = tvBluetoothName.getText().toString();
					tvBluetoothName.setText(groupName
							+ blueName.substring(blueName.length() - 5,
									blueName.length()));
					map.put("name", groupName);
					new Thread() {
						@Override
						public void run() {
							try {
								Thread.sleep(500);
								// todo 蓝牙改名,连接重构
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}.start();
					break;
				case BluetoothGlobal.SFIUI_CONNECT_SUCCESS:
					UserConfig.ShowToast(context, "已修改完毕");
					cancel();
					break;
				}
			}
		}

	};

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	/**
	 * 监听文本框改变
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String groupName = etGroup.getText().toString();
		if (Utils.verifySymbol(groupName)) {
			btnOk.setEnabled(false);
			tvGroupname.setText(editStr + "(" + "不能输入特殊符号！" + ")");
		} else if (groupName.getBytes().length > 15) {
			btnOk.setEnabled(false);
			tvGroupname.setText(editStr + "(" + "不能超过15个字节，1个汉字3个字节)");
		} else if (groupName.equals(map.get("name").toString())) {
			tvGroupname.setText(editStr);
			btnOk.setEnabled(false);
		} else if (!groupName.equals("")) {
			tvGroupname.setText(editStr);
			btnOk.setEnabled(true);
		} else {
			tvGroupname.setText(editStr);
			btnOk.setEnabled(false);
		}
	}

}
