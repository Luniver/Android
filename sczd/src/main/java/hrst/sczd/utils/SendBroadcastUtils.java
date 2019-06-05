package hrst.sczd.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * 广播发送类
 * @author glj
 * 2016-09-09
 */
public class SendBroadcastUtils {
	
	private Context context;
	
	public SendBroadcastUtils (Context context) {
		this.context = context;
	}
	
	
	public void sendBroadcast (String activityName) {

	}
	
	public void sendBroadcast (String activityName, Object data) {
		
	}
	
	public void sendBroadcast (String activityName, Object data1, Object data2) {
		
	}
	
	public void sendBroadcast (String activityName, Object data1, Object data2, Object data3) {
		
	}
	
	Bundle bundle = new Bundle();
	String UIACTION = "cn.hrst.refreshUI";
	Intent intent = new Intent(UIACTION);
	private String ACTIVITYNAME = "activityname";

	public void sendBrodact(String activityname) {
		bundle.putString(ACTIVITYNAME, activityname);
		intent.putExtras(bundle);
		this.context.sendBroadcast(intent);
	}

}
