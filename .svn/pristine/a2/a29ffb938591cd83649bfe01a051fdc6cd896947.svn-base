package hrst.sczd.ui.activity;

import android.os.CountDownTimer;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import hrst.sczd.ExitApplication;
import hrst.sczd.R;
import hrst.sczd.agreement.manager.vo.QueryTfKeepNumberFile;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_DELETE_THE_SPECIFIED_FILE_FOR_TF_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_KEEP_NUMBER_COPY_TO_U_DISK_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_QUERY_TF_KEEP_NUMBER_FILE_LIST;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_CLEAR_TF_KEEP_NUMBER_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_STOP_COPY_TO_UDISK_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_SELECT_KEEP_NUMBER_FILE_COPY_TO_U_DISK_ACK;
import hrst.sczd.ui.base.NavigationActivity;
import hrst.sczd.ui.manager.SkinSettingManager;
import hrst.sczd.ui.manager.U_DiskKeepNumManager;

/**
 * TF存数到U盘界面
 *
 * @author glj 2018-01-29
 */
public class U_DiskKeepNumActivity extends NavigationActivity implements
        OnCheckedChangeListener {

    private U_DiskKeepNumManager manager;

    private TextView selectStateTxt;
    private CheckBox allSelectCb;
    private ListView tfList;
    private Button copyToUdiskBtn;
    private Button tfClearDataBtn;
    private short count;

    @Override
    public void setContentView() {
        ExitApplication.getInstance().addActivity(this);
        SkinSettingManager skinManager = new SkinSettingManager(this);
        if (skinManager.getCurrentSkinRes() == 0) {
            this.setTheme(android.R.style.Theme_Black_NoTitleBar);
        } else {
            this.setTheme(android.R.style.Theme_Light_NoTitleBar);
        }
        setContentView(R.layout.activity_u_disk_keep_num);
        setTitle("TF存数");
        initView();
        initData();
        initListener();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        selectStateTxt = $(R.id.txt_select_state);
        allSelectCb = $(R.id.cb_all_select);
        tfList = $(R.id.list_tf);
        copyToUdiskBtn = $(R.id.btn_tf_copy_to_u_disk);
        tfClearDataBtn = $(R.id.btn_tf_clear_data);
    }

    /**
     * 初始化数据
     */
    private void initData() {
//		EventBus.getDefault().register(this);
        manager = new U_DiskKeepNumManager(this, tfList);

        // 获取TF存数文件
        PDA_TO_MCU_QUERY_TF_KEEP_NUMBER_FILE_LIST obj = new PDA_TO_MCU_QUERY_TF_KEEP_NUMBER_FILE_LIST();
        obj.setUcFileName_a((short) 1);
        manager.sendQueryTfKeepFileList(obj);
    }

    /**
     * 初始化控件监听
     */
    private void initListener() {
        tfClearDataBtn.setOnClickListener(this);
        copyToUdiskBtn.setOnClickListener(this);
        allSelectCb.setOnCheckedChangeListener(this);
        // todo test
//        startTest();
    }

    private void startTest() {
        new CountDownTimer(1000000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                QueryTfKeepNumberFile obj = new QueryTfKeepNumberFile();
                obj.setFileName("file_" + (count + 1));
                obj.setTotalFile((short) 20);
                obj.setFileNum(count);
                manager.saveTF_FIleList(obj);
                count++;
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    /**
     * 获取TF中的文件信息
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getTF_FileList(QueryTfKeepNumberFile obj) {
        manager.saveTF_FIleList(obj);
    }

    /**
     * 复制单个存数到U盘的应答
     *
     * @param obj
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getSelectTfCopyToUdiskResult(
            PDA_TO_MCU_SELECT_KEEP_NUMBER_FILE_COPY_TO_U_DISK_ACK obj) {
        manager.selectTfCopyToUdiskResult(obj);
    }

    /**
     * 复制全部存数到U盘的应答
     *
     * @param obj
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getKeepNumberCopyToUdiskResult(
            PDA_TO_MCU_KEEP_NUMBER_COPY_TO_U_DISK_ACK obj) {
        manager.keepNumberCopyToUdiskResult(obj);
    }

    /**
     * 停止复制存数到U盘应答
     *
     * @param obj
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void stopKeepNumberCopyToUdiskResult(
            PDA_TO_MCU_REQUEST_STOP_COPY_TO_UDISK_ACK obj) {

    }

    /**
     * 清除全部存数的应答
     *
     * @param obj
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getClearTfKeepNumberResult(
            PDA_TO_MCU_REQUEST_CLEAR_TF_KEEP_NUMBER_ACK obj) {
        manager.clearTfKeepNumberResult(obj);
    }

    /**
     * 清除指定存数的应答
     *
     * @param obj
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getSelectTfDelectFileResult(
            PDA_TO_MCU_DELETE_THE_SPECIFIED_FILE_FOR_TF_ACK obj) {
        manager.selectTfDelectFileResult(obj);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            // 清除TF数据
            case R.id.btn_tf_clear_data:
                manager.setClearData();
                break;

            // 复制到U盘
            case R.id.btn_tf_copy_to_u_disk:
                manager.setCopyToUdisk();
                break;
        }
    }

    @Override
    public void handleMessage(Message msg) {


    }

    /**
     * 初始化View工具
     *
     * @param id 绑定的Id
     * @return
     */
    private <T> T $(int id) {
        T view = (T) findViewById(id);
        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == allSelectCb) {
            manager.setSelectState(selectStateTxt, isChecked);
        }
    }

    @Override
    protected void onDestroy() {

//		EventBus.getDefault().unregister(this);
        manager.removeAllMessages();
        super.onDestroy();
    }
}
