package hrst.sczd.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.RadioButton;

import hrst.sczd.ui.adapter.MyFragmentPagerAdapter;
import hrst.sczd.ui.activity.fragment.OperateRecordFragment;
import hrst.sczd.ui.activity.fragment.UsedRecordFragment;

import java.util.ArrayList;

import hrst.sczd.R;
import hrst.sczd.ui.base.NavigationActivity;

/**
 * @author 刘兰
 * @describe 使用记录界面
 * @date 2014.05.23
 * @version 1.1.1.3 修改者，修改日期，修改内容
 */

public class HistoryRecordActivity extends NavigationActivity implements OnPageChangeListener {
    Fragment usedreFragment = null;
    Fragment operateRecord = null;
    private ArrayList<Fragment> fragmentsList;
    private ViewPager viewPager;
    private RadioButton rbUserHistory;
    private RadioButton rbOperateRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView() {
        this.setTheme(android.R.style.Theme_Black_NoTitleBar);
        setContentView(R.layout.activity_history_record_sczd);
        setTitle("历史记录");
        initView();
        initData();
    }

	private void initView() {
        rbUserHistory = (RadioButton) findViewById(R.id.rb_user_history_s);
        rbOperateRecord = (RadioButton) findViewById(R.id.rb_operate_record_s);
        rbUserHistory.setChecked(true);
        rbUserHistory.setOnClickListener(new MyOnClickListener(0));
        rbOperateRecord.setOnClickListener(new MyOnClickListener(1));
        initViewPager();
    }
	
    private void initData() {
    	//来源:0号码管理/语音监听界面      1更多界面
    	int source = getIntent().getIntExtra("source", 1);
    	if (source == 0) {
        	String phoneNum = getIntent().getStringExtra("phoneNumber");
    		if (!(null == phoneNum || "".equals(phoneNum))) {
    			Bundle bundle = new Bundle();
    	        bundle.putString("phoneNumber",phoneNum);
    	        usedreFragment.setArguments(bundle);
    		}
    	} else if (source == 1) {
   			Bundle bundle = new Bundle();
	        bundle.putString("phoneNumber",null);
	        usedreFragment.setArguments(bundle);
    	}
	}
    
    /**
     * 初始化viewpager
     * 
     */
    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.vPager_history_record_s);
        fragmentsList = new ArrayList<Fragment>();
        usedreFragment = UsedRecordFragment.getInstance();
        operateRecord = new OperateRecordFragment();
        fragmentsList.add(usedreFragment);
        fragmentsList.add(operateRecord);
        viewPager.setAdapter(new MyFragmentPagerAdapter(
                getSupportFragmentManager(), fragmentsList));
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void handleMessage(Message msg) {

    }

//    @Override
//    public void setAnimation() {
//        this.overridePendingTransition(R.anim.alpha2, R.anim.alpha1);
//    }

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
        	rbOperateRecord.setChecked(false);
            rbUserHistory.setChecked(true);
            break;
        case 1:
        	rbOperateRecord.setChecked(true);
            rbUserHistory.setChecked(false);
            break;
        default:
            break;
        }

    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            viewPager.setCurrentItem(index);
        }
    }
    
    @Override
    protected void onDestroy() {
        usedreFragment = null;
        operateRecord = null;
    	super.onDestroy();
    }
}
