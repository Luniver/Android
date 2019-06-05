package hrst.sczd.ui.manager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hrst.BluetoothCommHelper;
import com.hrst.common.annotation.SimOperator;
import com.hrst.common.ui.InduceConfigure;
import com.hrst.common.ui.database.dao.UsedRecordDao;
import com.hrst.common.ui.interfaces.Constants;
import com.hrst.common.ui.view.ListDialog;
import com.hrst.common.util.DualSimUtils;
import com.hrst.common.util.SharedPreferencesUtils;
import com.hrst.common.util.ToastUtils;
import com.hrst.induce.annotation.SmsPduMode;
import com.hrst.induce.annotation.TargetFormat;

import hrst.sczd.R;

/**
 * 制式选择界面管理类
 *
 * @author glj
 *         2018-01-17
 */
public class InduceConfigManager {

    /**
     * 上次诱发目标缓存key
     */
    private static final String KEY_LAST_TARGET_NUMBER = "last_target_number";

    private Context context;

    /**
     * 目标号码控件
     */
    private TextView phoneNumTxt;

    /**
     * 目标姓名控件
     */
    private TextView nameTxt;

    /**
     * 国家编码控件
     */
    private TextView countryTxt;

    /**
     * 国家名称控件
     */
    private TextView countryNameTxt;

    /**
     * GSM制式控件
     */
    private RadioButton gsmRbtn;

    /**
     * CDMA制式控件
     */
    private RadioButton cdmaRbtn;

    /**
     * TD制式控件
     */
    private RadioButton tdRbtn;

    /**
     * WCDMA制式控件
     */
    private RadioButton wcdmaRbtn;

    /**
     * LTE制式控件
     */
    private RadioButton lteRbtn;

    /**
     * 国家码开关控件
     */
    private CheckBox openCountryCb;


    /**
     * 目标号码
     */
    private String strTargetPhone = "";

    /**
     * 目标名字
     */
    private String strTargetName = "";

    /**
     * 当前短信诱发格式
     */
    private @SmsPduMode
    int smsPduMode = SmsPduMode.PDU_MODE_TWO;

    /**
     * 国家码
     */
    private String countryStr = "";

    /**
     * 当前目标运营商
     */
    private String simOperator;

    /**
     * 构造函数
     */
    public InduceConfigManager(Context context, TextView phoneNumTxt,
                               TextView nameTxt, RadioButton gsmRbtn, RadioButton cdmaRbtn,
                               RadioButton lteRbtn, RadioButton tdRbtn,
                               RadioButton wcdmaRbtn, CheckBox openCountryCb,
                               TextView countryTxt, TextView countryNameTxt) {
        this.context = context;
        this.phoneNumTxt = phoneNumTxt;
        this.nameTxt = nameTxt;
        this.gsmRbtn = gsmRbtn;
        this.cdmaRbtn = cdmaRbtn;
        this.wcdmaRbtn = wcdmaRbtn;
        this.tdRbtn = tdRbtn;
        this.lteRbtn = lteRbtn;
        this.openCountryCb = openCountryCb;
        this.countryTxt = countryTxt;
        this.countryNameTxt = countryNameTxt;
    }


    /**
     * 初始化数据
     */
    public void initData() {
        String targetPhone = SharedPreferencesUtils.getString(Constants.Induce.KEY_TARGET_PHONE);
        String targetName = SharedPreferencesUtils.getString(Constants.Induce.KEY_TARGET_NAME);
        InduceConfigure.targetPhone = targetPhone;
        phoneNumTxt.setText(targetPhone);
        phoneNumTxt.setTextColor(context.getResources().getColor(R.color.light_blue));
        nameTxt.setText(targetName);
        nameTxt.setTextColor(context.getResources().getColor(R.color.light_blue));
        openCountryCb.setChecked(SharedPreferencesUtils.getBoolean(Constants.Induce.KEY_OPEN_COUNTRY));
        // 设置制式初始值
        setFormatLayout();
    }

    //设置号码制式
    private void setFormatLayout() {
        InduceConfigure.targetInduceFormat = SharedPreferencesUtils.getString(Constants.Induce.KEY_TARGET_FORMAT);
//        Logger.d("shared preference: " + InduceConfigure.targetInduceFormat);
        switch (InduceConfigure.targetInduceFormat) {
            case TargetFormat.TARGET_FORMAT_GSM:
                gsmRbtn.setChecked(true);
                InduceConfigure.targetInduceFormat = TargetFormat.TARGET_FORMAT_GSM;
                break;
            case TargetFormat.TARGET_FORMAT_CDMA:
                cdmaRbtn.setChecked(true);
                InduceConfigure.targetInduceFormat = TargetFormat.TARGET_FORMAT_CDMA;
                break;
            case TargetFormat.TARGET_FORMAT_TDSCDMA:
                InduceConfigure.targetInduceFormat = TargetFormat.TARGET_FORMAT_TDSCDMA;
                tdRbtn.setChecked(true);
                break;
            case TargetFormat.TARGET_FORMAT_WCDMA:
                InduceConfigure.targetInduceFormat = TargetFormat.TARGET_FORMAT_WCDMA;
                wcdmaRbtn.setChecked(true);
                break;
            case TargetFormat.TARGET_FORMAT_LTE:
                InduceConfigure.targetInduceFormat = TargetFormat.TARGET_FORMAT_LTE;
                lteRbtn.setChecked(true);
                break;
            default:
                InduceConfigure.targetInduceFormat = TargetFormat.TARGET_FORMAT_GSM;
                gsmRbtn.setChecked(true);
                break;
        }

    }


    /**
     * 选择短信诱发格式
     *
     * @param tv_msm_type
     */
    public void selectSmsType(TextView tv_msm_type) {
        String[] select_sms_type = {"格式一", "格式二", "格式三"};
        ListDialog listDialog = new ListDialog(context);
        listDialog.setTitle("格式选择");
        listDialog.setDatasources(select_sms_type);
        listDialog.setOnItemClickListener((parent, view, position, id) -> {
            if (position < select_sms_type.length) {
                tv_msm_type.setText(select_sms_type[position]);
                smsPduMode = position;
            }
        });
        listDialog.show();
    }

    /**
     * 设置用户输入信息
     *
     * @param data
     */
    public void userInput(Intent data) {
        strTargetPhone = data.getExtras().getString("phoneNumber");
        strTargetName = data.getExtras().getString("name");
        phoneNumTxt.setText(strTargetPhone);
        phoneNumTxt.setTextColor(context.getResources().getColor(R.color.light_blue));
        nameTxt.setText(strTargetName);
        nameTxt.setTextColor(context.getResources().getColor(R.color.light_blue));
    }

    /**
     * 设置国家码信息
     *
     * @param data
     */
    public void setCountryInfo(Intent data) {
        Bundle bundle = data.getExtras();
        String countryName = bundle.getString(Constants.Induce.KEY_COUNTRY_NAME);
        String countryNumber = bundle.getString(Constants.Induce.KEY_COUNTRY_NUMBER);
        countryTxt.setText(countryNumber);
        countryNameTxt.setText(countryName);
        InduceConfigure.countryName = countryName;
        //国家码
        InduceConfigure.countryNum = countryNumber;
    }


    /**
     * 下一步操作
     *
     * @param usedDao
     */
    public boolean onNext(UsedRecordDao usedDao) {
        // 检查蓝牙是否连接
        if(!BluetoothCommHelper.get().isConnected()){
            ToastUtils.showToast(context.getResources().getString(R.string.disconnect));
            return false;
        }
        //校验目标信息
        if (checkTargetInfo()) {

            //设置目标信息缓存
            saveTargetInfo(usedDao);

            //设置配置信息
            setConfigInfo();

            return true;
        }

        return false;
    }

    /**
     * 设置配置信息
     */
    private void setConfigInfo() {

        // 目标号码
        InduceConfigure.targetPhone = strTargetPhone;

        // 目标号码运营商
        InduceConfigure.targetPhoneOperator = DualSimUtils.getSimOperator(strTargetPhone);

        // 国家码
        InduceConfigure.countryNum = countryStr;

        // 诱发格式
        InduceConfigure.pduMode = smsPduMode;

        //判断是否使用加国家码
        if (!openCountryCb.isChecked()) {
            InduceConfigure.countryNum = "";
        }

        //如果有加号则把加号去掉在前面加00
        if (InduceConfigure.countryNum.contains("+")) {
            InduceConfigure.countryNum = "00" + InduceConfigure.countryNum.substring(1, InduceConfigure.countryNum.length());
        }

        // 电信目标只有一种诱发 格式 CDMA
        if (SimOperator.TELECOM.equals(DualSimUtils.getSimOperator(InduceConfigure.targetPhone))) {
            InduceConfigure.targetInduceFormat = TargetFormat.TARGET_FORMAT_CDMA;
        }
    }

    /**
     * 检测用户输入信息
     *
     * @return
     */
    private boolean checkTargetInfo() {
        //获取目标号码,目标姓名,国家码文本信息
        strTargetPhone = phoneNumTxt.getText().toString();
        strTargetName = nameTxt.getText().toString();
        countryStr = countryTxt.getText().toString();

        //检验目标号码
        if (strTargetPhone.equals("点击输入号码")) {
            ToastUtils.showToast("请输入目标信息！");
            return false;
        }

        if (strTargetPhone.equals("")) {
            ToastUtils.showToast("请输入目标信息！");
            return false;
        }

        if (strTargetPhone.trim().length() < 4) {
            ToastUtils.showToast("目标号码位数不能少于4位");
            return false;
        }

        //校验目标姓名
        if (strTargetName.equals("备注姓名")) {
            ToastUtils.showToast("请输入目标信息！");
            return false;
        }

        if (strTargetName.equals("")) {
            ToastUtils.showToast("请输入目标信息！");
            return false;
        }

        //校验目标制式
        if (TextUtils.isEmpty(InduceConfigure.targetInduceFormat)) {
            ToastUtils.showToast("请选择目标制式！");
            return false;
        }

        //获取目标号码的运营商
        simOperator = DualSimUtils.getSimOperator(strTargetPhone);

        //判断目标号码为电信,但选择制式不为CDMA或者LTE则不允许进入
        if (SimOperator.MOBILE.equals(simOperator)) {
            if (cdmaRbtn.isChecked() || wcdmaRbtn.isChecked()) {
                ToastUtils.showToast("目标号码是移动号码,请选择GSM/TD-SCDMA/LTE制式!");
                return false;
            }
        } else if (SimOperator.UNICOM.equals(simOperator)) {
            if (cdmaRbtn.isChecked() || tdRbtn.isChecked()) {
                ToastUtils.showToast("目标号码是联通号码,请选择GSM/WCDMA/LTE制式!");
                return false;
            }
        } else if (SimOperator.TELECOM.equals(simOperator)) {
            if (gsmRbtn.isChecked() || wcdmaRbtn.isChecked() || tdRbtn.isChecked()) {
                ToastUtils.showToast("目标号码是电信号码,请选择CDMA/LTE制式!");
                return false;
            }
        }

        //提醒用户禁用移动卡通过格式一诱发移动联通目标
        if (smsPduMode == SmsPduMode.PDU_MODE_ONE && SimOperator.UNICOM.equals(simOperator)
                && (DualSimUtils.isMobile(DualSimUtils.simOperator1) || DualSimUtils.isMobile(DualSimUtils.simOperator2))) {
            ToastUtils.showToast("禁止移动卡使用格式一诱发移动联通目标号码");
            return false;
        }

        return true;
    }

    /**
     * 通过SharedPreferences保存数据到targetInfo文件中
     *
     * @param usedDao
     */
    private void saveTargetInfo(UsedRecordDao usedDao) {
        SharedPreferencesUtils.put(Constants.Induce.KEY_TARGET_PHONE, strTargetPhone);
        SharedPreferencesUtils.put(Constants.Induce.KEY_TARGET_NAME, strTargetName);
        SharedPreferencesUtils.put(Constants.Induce.KEY_TARGET_FORMAT, InduceConfigure.targetInduceFormat);
        SharedPreferencesUtils.put(Constants.Induce.KEY_OPEN_COUNTRY, openCountryCb.isChecked());

        //获取上次使用的目标号码
        String lastTargetNumber = SharedPreferencesUtils.getString(KEY_LAST_TARGET_NUMBER);

        //如果和上次的目标号码不一样
        if (!lastTargetNumber.contains(strTargetPhone)) {
            //开启记录当前目标号码的使用时间
            usedDao.saveusedRecord(strTargetPhone);

            //保存当次目标号码
            SharedPreferencesUtils.put(KEY_LAST_TARGET_NUMBER, strTargetPhone);
        }
    }
}
