package hrst.sczd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.RadioButton;

import java.util.ArrayList;

import hrst.sczd.ExitApplication;
import hrst.sczd.R;
import hrst.sczd.ui.activity.fragment.setting.DeveiceSelfCheckFragment;
import hrst.sczd.ui.activity.fragment.setting.VoicePromptSettingFragment;
import hrst.sczd.ui.adapter.MyFragmentPagerAdapter;
import hrst.sczd.ui.base.NavigationActivity;
import hrst.sczd.ui.manager.SkinSettingManager;

/**
 * @author 李家威
 * @version 1.1.1.3 修改者，修改日期，修改内容
 * @describe 系统设置
 * @date 2014.05.23
 */
public class SystemSettingActivity extends NavigationActivity implements OnPageChangeListener {

    private SkinSettingManager skinManager;

    private RadioButton voicePromptSettingRb;

    private RadioButton deviceSelfCheckingSettingRb;

    private ViewPager settingVpager;

    private ArrayList<Fragment> fragmentsList;

    private Fragment deveiceSelfCheckFragment;

    private Fragment voicePromptSettingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExitApplication.getInstance().addActivity(this);
        skinManager = new SkinSettingManager(this);
        skinManager.initSkins();
    }

    /**
     * 设置布局，标题与视觉模式
     */
    @Override
    public void setContentView() {
        skinManager = new SkinSettingManager(this);
        if (skinManager.getCurrentSkinRes() == 0) {
            this.setTheme(android.R.style.Theme_Black_NoTitleBar);
        } else {
            this.setTheme(android.R.style.Theme_Light_NoTitleBar);
        }
        setContentView(R.layout.activity_setting_sczd);
        setTitle("系统设置");
        initView();
        initListener();
        initViewPager();
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        if (v == leftButton) {
        /*
            if (tvBack.getText().toString().equals("场强分析")) {
                Intent intent2 = new Intent(SystemSettingActivity.this,
                        FieldAnalysisActivity.class);
                intent2.putExtra("tv_back", "蓝牙配对");
                startActivity(intent2);
            } else if (tvBack.getText().toString().equals("首页")) {
                Intent intent4 = new Intent(SystemSettingActivity.this,
                        InduceConfigActivity.class);
                startActivity(intent4);
            }
            */
            SystemSettingActivity.this.finish();
        }
    }

    /**
     * 返回事件
     */
    @Override
    public void onBackPressed() {
        Intent intent;
        /*
        if (tvBack.getText().toString().equals("场强分析")) {
            intent = new Intent(SystemSettingActivity.this,
                    FieldAnalysisActivity.class);
            intent.putExtra("tv_back", "蓝牙配对");
            startActivity(intent);
        } else if (tvBack.getText().toString().equals("首页")) {
            intent = new Intent(SystemSettingActivity.this,
                    InduceConfigActivity.class);
            startActivity(intent);
        }
        */
        SystemSettingActivity.this.finish();
    }

    /**
     * 初始化
     */
    private void initView() {
        voicePromptSettingRb = (RadioButton) findViewById(R.id.rb_voice_prompt_setting);

        deviceSelfCheckingSettingRb = (RadioButton) findViewById(R.id.rb_device_self_checking_setting);

        settingVpager = (ViewPager) findViewById(R.id.vPager_setting);
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        voicePromptSettingRb.setOnClickListener(new MyOnClickListener(0));
        deviceSelfCheckingSettingRb.setOnClickListener(new MyOnClickListener(1));
    }

    private void initViewPager() {
        fragmentsList = new ArrayList<Fragment>();
        deveiceSelfCheckFragment = DeveiceSelfCheckFragment.getInstance();
        voicePromptSettingFragment = VoicePromptSettingFragment.getInstance();

        fragmentsList.add(voicePromptSettingFragment);
        fragmentsList.add(deveiceSelfCheckFragment);
        settingVpager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList));
        settingVpager.setCurrentItem(0);
        settingVpager.setOffscreenPageLimit(1);
        settingVpager.setOnPageChangeListener(this);
    }

    @Override
    public void handleMessage(Message msg) {

    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            if (settingVpager != null) {
                settingVpager.setCurrentItem(index);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        switch (arg0) {
            case 0:
                voicePromptSettingRb.setChecked(true);
                deviceSelfCheckingSettingRb.setChecked(false);
                break;
            case 1:
                voicePromptSettingRb.setChecked(false);
                deviceSelfCheckingSettingRb.setChecked(true);
                break;
        }
    }

}
