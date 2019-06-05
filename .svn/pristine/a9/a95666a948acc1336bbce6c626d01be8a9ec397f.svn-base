package hrst.sczd.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hrst.sczd.ExitApplication;
import hrst.sczd.R;
import hrst.sczd.agreement.manager.InteractiveManager;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_DT_GAIN_SETTING;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_GAIN_SETTING;
import hrst.sczd.ui.base.NavigationActivity;
import hrst.sczd.ui.manager.SkinSettingManager;
import hrst.sczd.utils.SaveData_withPreferences;

/**
 * @author 陈洁
 * @version 1.1.1.3
 *          修改者，修改日期，修改内容
 * @describe 增益方式类
 * @date 2014.05.23
 * <p>
 * modify sophimp on 2018.4.26
 */
public class GainWayActivity extends NavigationActivity implements OnCheckedChangeListener {

    public static final String KEY_DES_GAIN = "des_gain";
    public static final String KEY_DT_GAIN = "dt_gain";
    private Button btnSure;// 按钮：确定
    private RadioButton rbLowGain, rbDesLow, rbDesMiddle, rbDesHigh;// 高中低增益方式
    private RelativeLayout rlGainway;// 控制高中低增益方式布局隐藏与显示
    private SaveData_withPreferences sPreferences;// 保存数据
    private RadioButton rbDtLow;
    private RadioButton rbDtMiddle;
    private RadioButton rbDtHigh;
    /**
     * 当前的目标增益
     */
    private byte desGain = 1;
    /**
     * 当前的数传增益
     */
    private byte dtGain = 1;
    private RadioGroup rgDesGain;
    private RadioGroup rgDtGain;
    private CheckBox auxLevel;
    private int sharedDesGain;
    private int sharedDtGain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExitApplication.getInstance().addActivity(this);
        init();
        // 获取上次保存的增益方式
        // 1 - low,  2 - middle, 3 - high
        sharedDesGain = sPreferences.getData_int(KEY_DES_GAIN);
        sharedDtGain = sPreferences.getData_int(KEY_DT_GAIN);
        // 辅助档位的值在第7位
        int aux = (sharedDesGain & 0x80) >> 7;
        auxLevel.setChecked(aux == 1);
        // 取出普通档位的值
        sharedDesGain = sharedDesGain & 0x7f;
        switch (sharedDesGain) {
            case 1:
                rbDesLow.setChecked(true);
                break;
            case 2:
                rbDesMiddle.setChecked(true);
                break;
            case 3:
                rbDesHigh.setChecked(true);
                break;
            case 4:
                rbLowGain.setChecked(true);
                break;
            default:
                rbDesMiddle.setChecked(true);
                sPreferences.saveDatas(KEY_DES_GAIN, 1);
                break;
        }
        switch (sharedDtGain) {
            case 1:
                rbDtLow.setChecked(true);
                break;
            case 2:
                rbDtMiddle.setChecked(true);
                break;
            case 3:
                rbDtHigh.setChecked(true);
                break;
            default:
                rbDtMiddle.setChecked(true);
                sPreferences.saveDatas(KEY_DT_GAIN, 1);
                break;
        }

        this.desGain = (byte) sharedDesGain;
        this.dtGain = (byte) sharedDtGain;
    }

    /**
     * 初始化
     */
    public void init() {
        rgDesGain = (RadioGroup) findViewById(R.id.rg_gainway);
        rgDtGain = findViewById(R.id.rg_dt_gainway);
        rlGainway = (RelativeLayout) findViewById(R.id.rl_gainway);
        rbLowGain = (RadioButton) findViewById(R.id.rb_lowgain);
        rbDesLow = (RadioButton) findViewById(R.id.rb_gain_low);
        rbDesMiddle = (RadioButton) findViewById(R.id.rb_gain_middle);
        rbDesHigh = (RadioButton) findViewById(R.id.rb_gain_high);
        rbDtLow = findViewById(R.id.rb_dt_low);
        rbDtMiddle = findViewById(R.id.rb_dt_middle);
        rbDtHigh = findViewById(R.id.rb_dt_high);
        btnSure = (Button) findViewById(R.id.btnSure);
        auxLevel = findViewById(R.id.cb_aux_level);
        TextView tvDtTitle = findViewById(R.id.tv_dt_gain_title);
        tvDtTitle.setVisibility(View.VISIBLE);
        RelativeLayout rlDtContainer = findViewById(R.id.rl_dt_gainway);
        rlDtContainer.setVisibility(View.VISIBLE);
        btnSure.setOnClickListener(this);
        rgDesGain.setOnCheckedChangeListener(this);
        rgDtGain.setOnCheckedChangeListener(this);
        sPreferences = SaveData_withPreferences.getInstance(this);
    }

    /**
     * 设置布局，标题与视觉模式
     */
    @Override
    public void setContentView() {
        SkinSettingManager skinManager = new SkinSettingManager(this);
        if (skinManager.getCurrentSkinRes() == 0) {
            this.setTheme(android.R.style.Theme_Black_NoTitleBar);
        } else {
            this.setTheme(android.R.style.Theme_Light_NoTitleBar);
        }
        setContentView(R.layout.activity_gainway);
        setTitle("增益方式");
    }

    /**
     * 确定按钮的监听函数
     */
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == btnSure) {
            if (auxLevel.isChecked()) {
                // 从0开始,将第7位置1, 代表开
                desGain |= 0x80;
            }
//            if (sharedDesGain != desGain){
            // 设置目标增益
            PDA_TO_MCU_REQUEST_GAIN_SETTING obj = new PDA_TO_MCU_REQUEST_GAIN_SETTING();
            obj.setcGain_a(desGain);
            InteractiveManager.getInstance().sendRequestGainSetting(obj);
            sPreferences.saveDatas(KEY_DES_GAIN, desGain);
//            }

//            if (sharedDtGain != dtGain){
            // 设置数传增益
            PDA_TO_MCU_REQUEST_DT_GAIN_SETTING dtObj = new PDA_TO_MCU_REQUEST_DT_GAIN_SETTING();
            dtObj.setcGain_a(dtGain);
            InteractiveManager.getInstance().sendRequestDtGainSetting(dtObj);
            sPreferences.saveDatas(KEY_DT_GAIN, dtGain);
//            }
            finish();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_gain_low:
                desGain = 1;
                break;
            case R.id.rb_gain_middle:
                desGain = 2;
                break;
            case R.id.rb_gain_high:
                desGain = 3;
                break;
            case R.id.rb_dt_low:
                dtGain = 1;
                break;
            case R.id.rb_dt_middle:
                dtGain = 2;
                break;
            case R.id.rb_dt_high:
                dtGain = 3;
                break;
            case R.id.rb_lowgain:
                desGain = 4;
                break;
            default:
                break;
        }

        // todo test code will delete
//        if (group == rgDesGain){
//            PDA_TO_MCU_REQUEST_GAIN_SETTING obj = new PDA_TO_MCU_REQUEST_GAIN_SETTING();
//            obj.setcGain_a(desGain);
//            InteractiveManager.getInstance(context).sendRequestGainSetting(obj);
//        }else {
//
//            PDA_TO_MCU_REQUEST_DT_GAIN_SETTING dtObj = new PDA_TO_MCU_REQUEST_DT_GAIN_SETTING();
//            dtObj.setcGain_a(dtGain);
//            InteractiveManager.getInstance(context).sendRequestDtGainSetting(dtObj);
//        }

    }

    @Override
    public void handleMessage(Message msg) {

    }

}
