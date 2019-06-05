package hrst.sczd.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.widget.TextView;

import com.hrst.common.ui.interfaces.Constants;

import hrst.sczd.ExitApplication;
import hrst.sczd.R;
import hrst.sczd.ui.manager.AboutUsManager;

/**
 * @author 沈月美
 * @describe 闪窗
 * @date 2014.05.23
 * @version 1.1.1.3
 * 修改者，修改日期，修改内容
*/
public class SplashActivity extends Activity {
	private TextView tvVersion;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		tvVersion=(TextView) findViewById(R.id.tv_version);
		tvVersion.setText(new AboutUsManager(this).getVersionCode());
		new Handler(Looper.myLooper()).postDelayed(new Runnable() {

			@Override
			public void run() {

				Intent splashIntent = new Intent(SplashActivity.this, BluetoothPairActivity.class);
				splashIntent.putExtra(Constants.Common.KEY_DEFAULT_INDEX_NAME, FieldAnalysisActivity.class.getName());
				startActivity(splashIntent);
				finish();
			}
		}, 3000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}
	@SuppressLint("NewApi")
	@Override
	public void onBackPressed() {

		  ExitApplication.getInstance().exit();
		super.onBackPressed();
	}

}
