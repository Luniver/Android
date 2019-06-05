package hrst.sczd.ui.activity.dialog;

import hrst.sczd.R;
import hrst.sczd.ui.activity.DebugActivity;
import hrst.sczd.utils.WindowUtils;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hrst.common.ui.interfaces.Constants;
import com.hrst.common.util.SharedPreferencesUtils;

/**
 * 调试密码输入窗
 * @author glj
 *
 */
public class DebugPasswdDialog {
	
	private Context context;
	
	private Button btnDebugOK;
	private Button btnDebugCancel;
	
	private EditText edtDebugPasswd;
	
	private Dialog debugPasswdDialog;
	
	private DebugPasswdDialog(){}
	
	public DebugPasswdDialog (Context cn) {
		this.context = cn;
	}
	
	public void initDialog() {
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_debug_mode, null);
		edtDebugPasswd = (EditText) view.findViewById(R.id.edt_debug_passwd);
		btnDebugOK = (Button) view.findViewById(R.id.btn_debug_ok);
		btnDebugCancel = (Button) view.findViewById(R.id.btn_debug_cancel);
		btnDebugOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (passwordMatching()) {
					Intent intent = new Intent(context, DebugActivity.class);
					context.startActivity(intent);
					debugPasswdDialog.cancel();
				}
				
			}
		});
		btnDebugCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				debugPasswdDialog.cancel();
			}
		});
		debugPasswdDialog = WindowUtils.showDiyDialog(context, view);
		debugPasswdDialog.show();
        WindowManager.LayoutParams params = debugPasswdDialog.getWindow().getAttributes(); 
        params.width = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth(); 
//        params.height = 200 ; 
        debugPasswdDialog.getWindow().setAttributes(params);
	}

	/**
	 * 匹配密码
	 * @return
	 */
	protected boolean passwordMatching() {
		String debugPasswd = edtDebugPasswd.getText().toString();
		if ("123456".equals(debugPasswd)) {
			SharedPreferencesUtils.put(Constants.Common.KEY_PASSWORD_VERIFY, true);
			return true;
		}
		Toast.makeText(context, "密码错误!", Toast.LENGTH_SHORT).show();
		return false;
	}
	
}
