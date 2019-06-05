package hrst.sczd.ui.activity.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hrst.common.util.DualSimUtils;
import com.hrst.common.util.ToastUtils;
import com.hrst.induce.InduceFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hrst.sczd.R;
import hrst.sczd.utils.ToastUtil;
import hrst.sczd.utils.WindowUtils;

/**
 * 短信测试弹窗
 * @author glj
 *
 */
public class SmsTestDialog {
	
	private Context context;
	
	private Button btnSend;
	private Button btnCancel;
	
	private EditText edtSmsPhoneNum;
	
	private Dialog debugPasswdDialog;
	private InduceFactory induceFactory;

	private SmsTestDialog(){}
	
	public SmsTestDialog (Context cn) {
		this.context = cn;
		registerReceiver();
		induceFactory = InduceFactory.create();
	}
	
	public void initDialog() {
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_sms_test, null);
		edtSmsPhoneNum = (EditText) view.findViewById(R.id.edt_sms_phone_num);
		btnSend = (Button) view.findViewById(R.id.btn_send_ok);
		btnCancel = (Button) view.findViewById(R.id.btn_send_cancel);
		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//电话号码不能少于11位
				if (edtSmsPhoneNum.getText().toString().trim().length() < 11) {
					Toast.makeText(context, "电话号码长度不正确!", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!isMobileNO(edtSmsPhoneNum.getText().toString().trim())) {
					Toast.makeText(context, "电话号码格式不正确!", Toast.LENGTH_SHORT).show();
					return;
				}
				if (DualSimUtils.isTelecom(DualSimUtils.simOperator1) || DualSimUtils.isTelecom(DualSimUtils.simOperator2)){
					ToastUtil.showToast(context, "暂不支持电信卡进行短信功能测试!");
					return;
				}

//                String msgContent = new HrstSmsMessage.Builder()
//                        .setTargetFormat(InduceConfigure.targetInduceFormat)
//                        .build()
//                        .getSafeContents().get(0);
                String msgContent = "短信测试";
				SmsManager.getDefault().sendTextMessage(edtSmsPhoneNum.getText().toString(), null, msgContent, sentPI, deliverPI);
				btnSend.setEnabled(false);
				btnCancel.setEnabled(false);
				debugPasswdDialog.cancel();
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				unRegisterReceiver();
				debugPasswdDialog.cancel();
			}
		});
		debugPasswdDialog = WindowUtils.showDiyDialog(context, view);
		debugPasswdDialog.setCanceledOnTouchOutside(false);  
		debugPasswdDialog.setCancelable(false);  
		debugPasswdDialog.show();
        WindowManager.LayoutParams params = debugPasswdDialog.getWindow().getAttributes(); 
        params.width = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth(); 
        debugPasswdDialog.getWindow().setAttributes(params);
	}
	
	private boolean isMobileNO(String mobiles) {
	    Pattern p = Pattern.compile("^(13[0-9]|14[57]|15[0-35-9]|17[0-9]|18[0-9])[0-9]{8}$");
	    Matcher m = p.matcher(mobiles);
	    return m.matches();
	}
	
	private SentReceiver sentReceiver = new SentReceiver();
	private DeliverReceiver deliverReceiver = new DeliverReceiver();
	PendingIntent sentPI;
	PendingIntent deliverPI;
	
	/**
	 * 注册短信广播
	 */
	private void registerReceiver() {
		
		//处理返回的发送状态 
		String SENT_SMS_ACTION = "SENT_SMS_ACTION";
		Intent sentIntent = new Intent(SENT_SMS_ACTION);
		sentPI = PendingIntent.getBroadcast(context, 1, sentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		//处理返回的接收状态 
		String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
		// create the deilverIntent parameter
		Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
		deliverPI = PendingIntent.getBroadcast(context, 2, deliverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		context.registerReceiver(sentReceiver, new IntentFilter(SENT_SMS_ACTION));
		
		context.registerReceiver(deliverReceiver, new IntentFilter(DELIVERED_SMS_ACTION));
		
	}
	
	private void unRegisterReceiver () {
		context.unregisterReceiver(sentReceiver);
		context.unregisterReceiver(deliverReceiver);
	}
	
	class SentReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
	        switch (getResultCode()) {
	        case Activity.RESULT_OK:
				btnSend.setEnabled(true);
				btnCancel.setEnabled(true);
                ToastUtils.showToast("短信发送成功");
	        break;
	        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
	        case SmsManager.RESULT_ERROR_RADIO_OFF:
	        case SmsManager.RESULT_ERROR_NULL_PDU:
				btnSend.setEnabled(true);
				btnCancel.setEnabled(true);
                ToastUtils.showToast("短信发送失败");
	        	break;
	        }
			
		}
		
	}
	
	class DeliverReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
            ToastUtils.showToast("收信人已经成功接收");
		}
	}
}
