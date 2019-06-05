package hrst;

import hrst.sczd.ExitApplication;
import hrst.sczd.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 
 * @author 关玲基
 * 选择应用界面
 * 2016-09-19
 */
public class SelectEntryActivity extends Activity implements OnClickListener{
		
	private Button btnSinglepawn, btnSafesms, btnSczd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_app);
		btnSinglepawn = (Button) findViewById(R.id.btn_singlepawn);
		btnSafesms = (Button) findViewById(R.id.btn_safesms);
		btnSczd = (Button) findViewById(R.id.btn_sczd);
		btnSinglepawn.setOnClickListener(this);
		btnSafesms.setOnClickListener(this);
		btnSczd.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		//检测Xposed状态并写入文件
//		boolean state = checkXposedState(false);
//		FileUtil.writeXposedStateToText(state);
		
//		if(!state)
//			Toast.makeText(SelectEntryActivity.this, "Xposed框架未激活", Toast.LENGTH_SHORT).show();
		
		switch (v.getId()) {
		case R.id.btn_singlepawn:
//			startActivity(new Intent(SelectEntryActivity.this, hrst.singlepawn.main.SplashActivity.class));
//			startActivity(new Intent(SelectEntryActivity.this, hrst.singlepawn.main.SelectEntryActivity.class));
			break;
		case R.id.btn_safesms:
//			startActivity(new Intent(SelectEntryActivity.this, hrst.safesms.activity.SafeSmsMainActivity.class));
			break;
		case R.id.btn_sczd:
			startActivity(new Intent(SelectEntryActivity.this, hrst.sczd.ui.activity.SplashActivity.class));
			break;
		default:
			break;
		}
		finish();
	}
	
	/**
	 * 该方法提供给CheckXposedHook来Hook
	 * @param state
	 * @return
	 */
	public boolean checkXposedState (boolean state) {
		return state;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(SelectEntryActivity.this);
			dialog.setTitle("确定退出单兵程序吗？");
			dialog.setPositiveButton("确定", new  android.content.DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ExitApplication.getInstance().exit();
				}
			});
			dialog.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
				
			});
			dialog.create();
			dialog.show();
			
		}
		return false;
	}
	
	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

}
