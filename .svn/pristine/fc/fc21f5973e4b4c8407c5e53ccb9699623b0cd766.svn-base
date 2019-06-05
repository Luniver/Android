package hrst.sczd.ui.activity.fragment.setting;

import hrst.sczd.systemsettings.SettingUtils;
import hrst.sczd.ui.activity.dialog.HostDeficiencyDialogActivity;
import hrst.sczd.ui.activity.dialog.TempHighDialogActivity;
import hrst.sczd.ui.activity.dialog.VisualModeDialogActivity;
import hrst.sczd.ui.vo.SharedPreferencesValues;
import hrst.sczd.utils.SaveData_withPreferences;
import hrst.sczd.utils.Utils;
import hrst.sczd.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 语音提示设置
 *
 * @author glj
 *         2018-01-22
 */
public class VoicePromptSettingFragment extends Fragment implements OnCheckedChangeListener, OnClickListener {

    private static VoicePromptSettingFragment instance = null;
    SaveData_withPreferences preferences;

    public VoicePromptSettingFragment() {

    }

    /**
     * 获取实例
     *
     * @return
     */
    public static Fragment getInstance() {
        if (instance == null) {
            instance = new VoicePromptSettingFragment();
        }
        return instance;
    }

    private View view;

    private CheckBox cbVoiceCountoff;
    private CheckBox cbNearTarget;
    private CheckBox cbTempHigh;
    private CheckBox cbHostDeficiency;

    private TextView tvTempHigh;
    private TextView tvHostDeficiency;
    private TextView tvVisualPatterns;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        preferences = SaveData_withPreferences.getInstance(getActivity());
        view = initView(inflater);
        return view;
    }


    /**
     * 初始化控件
     *
     * @param inflater
     * @return
     */
    private View initView(LayoutInflater inflater) {

        View view = inflater.inflate(R.layout.system_settings, null);

        // 视觉模式
        tvVisualPatterns = (TextView) view.findViewById(R.id.tv_visual_patterns);
        boolean visualMode = preferences.getData_Boolean(SettingUtils.VisionMode);
        tvVisualPatterns.setText(visualMode ? "夜间模式" : "白天模式");

        // 语音开关
        cbVoiceCountoff = (CheckBox) view.findViewById(R.id.cb_voice_countoff);

        cbVoiceCountoff.setOnCheckedChangeListener(this);

        // 即将接近目标LAC
        cbNearTarget = (CheckBox) view.findViewById(R.id.cb_near_target);

        cbNearTarget.setOnCheckedChangeListener(this);

        // 过高温度
        cbTempHigh = (CheckBox) view.findViewById(R.id.cb_temp_high);
        cbTempHigh.setOnCheckedChangeListener(this);

        // 过低电量
        cbHostDeficiency = (CheckBox) view.findViewById(R.id.cb_host_deficiency);

        cbHostDeficiency.setOnCheckedChangeListener(this);

        // 设备过高温度值
        tvTempHigh = (TextView) view.findViewById(R.id.tv_temp_high);

        // 主机过低电量值
        tvHostDeficiency = (TextView) view.findViewById(R.id.tv_host_deficiency);


        // 视觉模式RL点击事件
        RelativeLayout rlVisualPatterns = (RelativeLayout) view.findViewById(R.id.rl_visual_patterns);
        rlVisualPatterns.setOnClickListener(this);

        // 过高温度值LL点击事件
        RelativeLayout rlTempHigh = (RelativeLayout) view.findViewById(R.id.rl_temp_high);
        rlTempHigh.setOnClickListener(this);

        // 过低电量值LL点击事件
        RelativeLayout rlHostDeficiency = (RelativeLayout) view.findViewById(R.id.rl_host_deficiency);
        rlHostDeficiency.setOnClickListener(this);

        // 重置所有按钮点击事件
        Button btnResetDefault = (Button) view.findViewById(R.id.btn_reset_default);
        btnResetDefault.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String batteryLow = preferences.getData_String(SettingUtils.HostDeficiencyValue);
        String tempHigh = preferences.getData_String(SettingUtils.TempHighValue);

        // 语音开关
        cbVoiceCountoff.setChecked(preferences.getData_Boolean(SettingUtils.VoiceCountOff));

        // 即将接近目标语音开关
        cbNearTarget.setChecked(preferences.getData_Boolean(SettingUtils.NearTarget));

        //高温开关
        cbTempHigh.setChecked(preferences.getData_Boolean(SettingUtils.TempHigh));

        //低电量开关
        cbHostDeficiency.setChecked(preferences.getData_Boolean(SettingUtils.HostDeficiency));

        tvTempHigh.setText("设备温度过高时提醒(" + tempHigh + "℃)");

        tvHostDeficiency.setText("主机电量不足时提醒(" + batteryLow + "%)");
    }

    @Override
    public void onCheckedChanged(CompoundButton arg0, boolean value) {
        switch (arg0.getId()) {
            case R.id.cb_voice_countoff:
                setParameter(SettingUtils.VoiceCountOff, value);
                break;

            case R.id.cb_near_target:
                setParameter(SettingUtils.NearTarget, value);
                break;

            case R.id.cb_temp_high:
//            setParameter(SharedPreferencesValues.TEMP_HIGH_OFF_SCZD, value);
                setParameter(SettingUtils.TempHigh, value);
                break;

            case R.id.cb_host_deficiency:
//            setParameter(SharedPreferencesValues.HOST_DEFICIENCY_OFF_SCZD, value);
                setParameter(SettingUtils.HostDeficiency, value);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();// 定义跳转对象
        switch (v.getId()) {
            case R.id.rl_temp_high: // 设备温度过高对话框
            {
                //温度开关打开时
                if (cbTempHigh.isChecked()) {
                    intent.setClass(getActivity(), TempHighDialogActivity.class);
                    startActivity(intent);
                }
                break;
            }
            case R.id.rl_host_deficiency:// 主机电量过低
            {
                //电量开关开启时
                if (cbHostDeficiency.isChecked()) {
                    intent.setClass(getActivity(), HostDeficiencyDialogActivity.class);
                    startActivity(intent);
                }
                break;
            }
            case R.id.btn_reset_default: // 恢复默认值
                resetDefault();
                break;
        }
    }

    /**
     * 恢复默认值
     */
    @SuppressLint("NewApi")
    private void resetDefault() {
        // 语音开关
        cbVoiceCountoff.setChecked(true);

        // 即将接近目标LAC
        cbNearTarget.setChecked(true);

        // 过高温度
        cbTempHigh.setChecked(true);

        // 过低电量
        cbHostDeficiency.setChecked(true);

        // 默认温度值
        tvTempHigh.setText("设备温度过高时提醒(65℃)");
        setParameter(SettingUtils.TempHighValue, "65");

        // 默认电量值
        tvHostDeficiency.setText("主机电量不足时提醒(20%)");
        setParameter(SettingUtils.HostDeficiencyValue, "20");
    }

    /**
     * 設置配置，值为Boolean
     *
     * @param paramName 配置名称
     * @param value     值
     * @return boolean
     */
    private void setParameter(String paramName, boolean value) {
        preferences.saveDatas_Boolean(paramName, value);
    }

    /**
     * 設置配置，值为String
     *
     * @param paramName 配置名称
     * @param value     值
     * @return boolean
     */
    private void setParameter(String paramName, String value) {
        preferences.saveDatas_String(paramName, value);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
