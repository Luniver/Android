package hrst.sczd.ui.activity.fragment.setting;

import hrst.sczd.systemsettings.SettingUtils;
import hrst.sczd.ui.vo.SharedPreferencesValues;
import hrst.sczd.utils.SaveData_withPreferences;
import hrst.sczd.utils.Utils;
import hrst.sczd.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * 设备自检设置
 * @author glj
 * 2018-01-22
 */
public class DeveiceSelfCheckFragment extends Fragment implements OnCheckedChangeListener{
	
	private static DeveiceSelfCheckFragment instance = null;
	
	public DeveiceSelfCheckFragment () {
		
	}
	
	public static Fragment getInstance() {
		if (instance == null) {
			instance = new DeveiceSelfCheckFragment();
		}
		return instance;
	}
	
	private View view;
	
	private CheckBox deviceSelfCheckingCb;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = initView(inflater);
		return view;
	}

	private View initView(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.fragment_device_self_check, null);
		deviceSelfCheckingCb = (CheckBox) view.findViewById(R.id.cb_device_self_checking);
		deviceSelfCheckingCb.setOnCheckedChangeListener(this);
		return view;
	}

	@Override
	public void onCheckedChanged(CompoundButton v, boolean value) {
		switch (v.getId()) {
		
		//设备功能自检开关
		case R.id.cb_device_self_checking:
			setParameter(SharedPreferencesValues.DEVICE_SELF_CHECKING_OFF_SCZD, value);
			break;
		}
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		deviceSelfCheckingCb.setChecked(
		getParameter(SharedPreferencesValues.DEVICE_SELF_CHECKING_OFF_SCZD, false));
	}
	
    /**
     * 設置配置，值为Boolean
     * 
     * @param paramName
     *            配置名称
     * @param value
     *            值
     * @return boolean
     */
	private boolean getParameter(String paramName, boolean value) {
		 return Utils.getSetting(getActivity(), paramName, value);
	}
	
    /**
     * 設置配置，值为Boolean
     * 
     * @param paramName
     *            配置名称
     * @param value
     *            值
     * @return boolean
     */
	private boolean setParameter(String paramName, boolean value) {
		 return Utils.setSetting(getActivity(), paramName, value);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
}
