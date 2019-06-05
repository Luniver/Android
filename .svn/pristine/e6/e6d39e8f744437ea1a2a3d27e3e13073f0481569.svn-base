package hrst.sczd.ui.activity.fragment.fieldAnalysis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hrst.common.annotation.CaptureMethod;
import com.hrst.common.annotation.SimOperator;
import com.hrst.common.ui.InduceConfigure;
import com.hrst.common.ui.database.dao.OperateRecordDao;
import com.hrst.common.ui.view.ListDialog;
import com.hrst.common.util.DualSimUtils;
import com.hrst.common.util.ToastUtils;
import com.hrst.induce.InduceFactory;
import com.hrst.induce.annotation.InduceType;
import com.hrst.induce.annotation.SendMethod;
import com.hrst.induce.annotation.TargetFormat;
import com.hrst.induce.interfaces.InduceCallBack;
import com.hrst.induce.sms.HrstSmsMessage;
import com.hrst.induce.utils.InduceConstants;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;

import de.greenrobot.event.EventBus;
import hrst.sczd.R;
import hrst.sczd.ui.adapter.DropEditTextBaseAdapter;
import hrst.sczd.utils.PhoneUtil;
import hrst.sczd.view.DropEditText;
import hrst.sczd.view.animation.PushDownAnimation;

/**
 * 页面说明:
 * <p>
 * 所有的UI逻辑
 * 由 目标号码 {@link InduceConfigure#targetPhone}, 选择的制式 {@link InduceConfigure#targetInduceFormat},
 * 手机卡状态{@link DualSimUtils#cardState}, 及页面内参数选择来控制
 * 隔绝与 activity 上的UI交互逻辑
 * <p>
 * 页面入口函数
 * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
 * <p>
 * 诱发功能, 使用诱发模块 sms_induce
 * <p>
 * create by Sophimp on 2018/7/12
 */
public class FragmentMSM extends Fragment implements View.OnClickListener {

    private final static String TAG = "goo-FragmentMSM";

    private final static String[] smsContents = {"LTE常用短信", "LTE新捕获", "LTE精准捕获"};
    private final static String[] channels = {"电信卡诱发", "非电信卡诱发"};

    private final String[] msm_timenumber_list = {"1", "2", "5", "10", "20"};
    private final String[] msm_outtime_list = {"30000", "40000", "50000", "60000", "90000"};
    private final String[] msm_interval_list = {"3000", "4000", "5000", "10000", "25000"};
    private final String[] call_interval_list = {"3000", "4000", "5000", "6000", "7000"};
    private final String[] call_hangup_list = {"3000", "4000", "5000", "6000", "7000"};

    private TextView tvMsmContent, tvCdmaAlleyway;

    private TextView tvDsp;
    private LinearLayout ll_msm_content, ll_cdma_alleyway;

    private CheckBox cb_msm_induce, cb_call_induce;
    private LinearLayout ll_setting_incude, ll_result_incude;
    private RelativeLayout rl_setting_incude_more, rl_result_incude_more,
            rl_incude_result_more_all;
    private ImageView iv_incude_more, iv_incude_remove, iv_incude_result_more;
    // 诱发设置 -- 短信
    private TextView tv_msm_dotime, tv_msm_timenumber, tv_msm_outtime,
            tv_msm_time_ms, tv_msm_interval, tv_msm_interval_ms,
            tv_incude_result;
    //	private EditText ed_msm_timenumber, ed_msm_outtime, ed_msm_interval;
    private DropEditText ed_msm_timenumber, ed_msm_outtime, ed_msm_interval;
    private DropEditText ed_call_timenumber, ed_call_interval, ed_call_hangup;
    // 诱发设置 -- 拨打
    private TextView tv_call_dotime, tv_call_timenumber, tv_call_interval,
            tv_call_interval_ms, tv_call_hangup, tv_call_hangup_ms;
//	private EditText ed_call_timenumber, ed_call_interval, ed_call_hangup;

    private ScrollView sv_induce;
    // 本类字符串
    private int msm_outtime = 30000;
    private int msm_interval = 3000;
    private int msm_timenumber = 1;
    private int call_timenumber = 1;
    private int call_hangup = 3000;
    private int call_interval = 7000;

    // 发送设置
    private int sendNoteNuming = 0; // 正在发送的短信次数
    private int sendCallNuming = 0; // 正在拨打的次数

    /***********************************************************************/
    private int sendNoteSusseccSms = 0;    //记录发送成功短信的条数
    private int sendNoteSusseccCall = 0;    //记录成功拨打的条数

    // 时间格式
    private SimpleDateFormat sdfAll = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private ListDialog listDialog;

    private Button btnStartInduce;
    private Button btnEndInduce;

    private @SendMethod
    String curSendMethod;
    /**
     * 使用卡槽的卡id
     */
    private int cardId = DualSimUtils.cardId1;
    private long unitStartInduceTime;

    /**
     * 操作记录存储
     */
    private OperateRecordDao operateRecordDao;

    private InduceFactory induceFactory = InduceFactory.create();

    /**
     * 设置界面布局
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Logger.d("sms fragment on create view");
//        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_app_incude_sczd, container, false);
        initView(view);
        initListener();
        updateState();
        return view;
    }

    @Override
    public void onResume() {
        Logger.d("goo FragmentSms onResume");
        updateState();
        super.onResume();
    }

    /**
     * 初始化控件
     *
     * @param view
     */
    private void initView(View view) {
        ll_msm_content = (LinearLayout) view.findViewById(R.id.ll_msm_content);
        tvMsmContent = (TextView) view.findViewById(R.id.tv_msm_content);
        cb_msm_induce = (CheckBox) view.findViewById(R.id.cb_msm_induce);
        cb_call_induce = (CheckBox) view.findViewById(R.id.cb_call_induce);
        tv_incude_result = (TextView) view.findViewById(R.id.tv_incude_result);
        // 短信诱发收缩动画
        ll_setting_incude = (LinearLayout) view.findViewById(R.id.ll_setting_incude);
        ll_result_incude = (LinearLayout) view.findViewById(R.id.ll_result_incude);
        rl_setting_incude_more = (RelativeLayout) view.findViewById(R.id.rl_setting_incude_more);
        rl_result_incude_more = (RelativeLayout) view.findViewById(R.id.rl_result_incude_more);
        rl_incude_result_more_all = (RelativeLayout) view.findViewById(R.id.rl_result_incude_more_all);

        iv_incude_more = (ImageView) view.findViewById(R.id.iv_incude_more);
        iv_incude_remove = (ImageView) view.findViewById(R.id.iv_incude_remove);
        iv_incude_result_more = (ImageView) view.findViewById(R.id.iv_incude_result_more);
        // 诱发设置 -- 短信
        tv_msm_dotime = (TextView) view.findViewById(R.id.tv_msm_dotime);
        tv_msm_interval_ms = (TextView) view.findViewById(R.id.tv_msm_interval_ms);
        tv_msm_timenumber = (TextView) view.findViewById(R.id.tv_msm_timenumber);
        tv_msm_outtime = (TextView) view.findViewById(R.id.tv_msm_outtime);
        tv_msm_time_ms = (TextView) view.findViewById(R.id.tv_msm_time_ms);
        tv_msm_interval = (TextView) view.findViewById(R.id.tv_msm_interval);
        tv_msm_dotime = (TextView) view.findViewById(R.id.tv_msm_dotime);
        ed_msm_timenumber = (DropEditText) view
                .findViewById(R.id.ed_msm_timenumber);
        ed_msm_outtime = (DropEditText) view.findViewById(R.id.ed_msm_outtime);
        ed_msm_interval = (DropEditText) view.findViewById(R.id.ed_msm_interval);

        tv_call_dotime = (TextView) view.findViewById(R.id.tv_call_dotime);
        tv_call_timenumber = (TextView) view.findViewById(R.id.tv_call_timenumber);
        tv_call_interval = (TextView) view.findViewById(R.id.tv_call_interval);
        tv_call_interval_ms = (TextView) view.findViewById(R.id.tv_call_interval_ms);
        tv_call_hangup = (TextView) view.findViewById(R.id.tv_call_hangup);
        tv_call_hangup_ms = (TextView) view.findViewById(R.id.tv_call_hangup_ms);
        ed_call_timenumber = (DropEditText) view.findViewById(R.id.ed_call_timenumber);
        ed_call_interval = (DropEditText) view.findViewById(R.id.ed_call_interval);
        ed_call_hangup = (DropEditText) view.findViewById(R.id.ed_call_hangup);

        ll_cdma_alleyway = (LinearLayout) view.findViewById(R.id.ll_cdma_alleyway);
        tvCdmaAlleyway = (TextView) view.findViewById(R.id.tv_cdma_alleyway);

        tvDsp = (TextView) view.findViewById(R.id.tv_dsp);
        tvDsp.setVisibility(View.GONE);
        tvDsp.setEnabled(false);

        sv_induce = (ScrollView) view.findViewById(R.id.sv_induce);
        btnStartInduce = view.findViewById(R.id.btn_start_induce);
        btnEndInduce = view.findViewById(R.id.btn_end_induce);

        listDialog = new ListDialog(getContext());
    }

    private void updateChannelLayout() {
        // 获取默认cardId
        cardId = DualSimUtils.getDefaultCardId(InduceConfigure.targetPhone);
        Logger.d(DualSimUtils.loggerShare());
        // 诱发目标为电信号码, 控制诱发通道选择
        if (SimOperator.TELECOM.equals(DualSimUtils.getSimOperator(InduceConfigure.targetPhone))) {
            ll_cdma_alleyway.setVisibility(View.VISIBLE);
            ll_msm_content.setVisibility(View.GONE);
//            Logger.d("cardState: " + DualSimUtils.cardState);
            switch (DualSimUtils.cardState) {
                case ONLY_CARD_ONE:
                    ll_cdma_alleyway.setEnabled(false);
                    setCdmaCardChannel(DualSimUtils.isTelecom(DualSimUtils.simOperator1));
                    break;
                case ONLY_CARD_TWO:
                    ll_cdma_alleyway.setEnabled(false);
                    setCdmaCardChannel(DualSimUtils.isTelecom(DualSimUtils.simOperator2));
                    break;
                case BOTH_CARD:
                    ll_cdma_alleyway.setEnabled(true);
                    // 默认选择卡一诱发
                    setCdmaCardChannel(DualSimUtils.isTelecom(DualSimUtils.simOperator1));
                    break;
                default:
                    // 无卡
                    ll_cdma_alleyway.setEnabled(false);
                    setCdmaCardChannel(DualSimUtils.isTelecom(DualSimUtils.simOperator1));
                    break;
            }
//            Logger.d("cardId: " + cardId);
        } else { // 诱发制式选择LTE 控制短信捕获方式
            if (TargetFormat.TARGET_FORMAT_LTE.equals(InduceConfigure.targetInduceFormat)) {
                ll_msm_content.setVisibility(View.VISIBLE);
                ll_cdma_alleyway.setVisibility(View.GONE);
                // todo 数传终端目前只支持新捕获, 设置不可以点击
                ll_msm_content.setEnabled(false);
                setMsmContent(CaptureMethod.NEW_CAPTURE);
            } else {
                ll_msm_content.setVisibility(View.GONE);
                ll_cdma_alleyway.setVisibility(View.GONE);
            }
        }

    }

    /**
     * 设置 cdma 诱发通道结果
     *
     * @param isTelecom 是否选择电信卡诱发
     */
    private void setCdmaCardChannel(boolean isTelecom) {
        Logger.d("setCdmaCardChannel: " + isTelecom);
        if (isTelecom) { // 电信卡诱发
            curSendMethod = SendMethod.METHOD_CDMA_SEND;
            tvCdmaAlleyway.setText(getString(R.string.cdma_card_induce));
        } else { // 非电信卡诱发
            curSendMethod = SendMethod.METHOD_NOT_CDMA_SEND;
            tvCdmaAlleyway.setText(getString(R.string.not_cdma_card_induce));
        }
        // 缓存默认诱发方式
        InduceConfigure.sendMethod = curSendMethod;
    }

    private void updateState() {

        Logger.d("sms fragment update state");

        operateRecordDao = new OperateRecordDao(getContext());

        //短信次数设置最多只能输入2位数字
        ed_msm_timenumber.mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
        //设置下拉的数据
        ed_msm_timenumber.setAdapter(new DropEditTextBaseAdapter(getActivity(), msm_timenumber_list));

        //短信超时设置最多只能输入6位数字
        ed_msm_outtime.mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        //设置下拉的数据
        ed_msm_outtime.setAdapter(new DropEditTextBaseAdapter(getActivity(), msm_outtime_list));
        //短信间隔设置最多只能输入6位数字
        ed_msm_interval.mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        //设置下拉的数据
        ed_msm_interval.setAdapter(new DropEditTextBaseAdapter(getActivity(), msm_interval_list));

        //拨打次数设置最多只能输入6位数字
        ed_call_timenumber.mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
        //设置下拉的数据
        ed_call_timenumber.setAdapter(new DropEditTextBaseAdapter(getActivity(), msm_timenumber_list));

        //拨打间隔设置最多只能输入4位数字
        ed_call_interval.mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        //设置下拉的数据
        ed_call_interval.setAdapter(new DropEditTextBaseAdapter(getActivity(), call_interval_list));

        //拨打挂断设置最多只能输入6位数字
        ed_call_hangup.mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        //设置下拉的数据
        ed_call_hangup.setAdapter(new DropEditTextBaseAdapter(getActivity(), call_hangup_list));
        // 默认选中短信
        if (!cb_msm_induce.isChecked() && !cb_call_induce.isChecked()){
            cb_msm_induce.setChecked(true);
        }
        ed_msm_timenumber.mEditText.setText("1");
        checkBoxColor(0, cb_msm_induce);
        checkBoxColor(1, cb_call_induce);
        //ed_msm_interval.setText(Global.msm_interval);

        // 设置默认值
        setText(msm_timenumber + "", msm_outtime + "", msm_interval + "",
                call_timenumber + "", call_hangup + "", call_interval + "");

        // 更新诱发通道选择布局
        updateChannelLayout();

        //初始化流程已结束, 后续流程是click事件互动
    }

    /**
     * 设置诱发参数
     *
     * @param smsNum       短信条数
     * @param msmOutTime   短信超时
     * @param msmInterval  短信间隔
     * @param callNum      拨打次数
     * @param callHangup   拨打挂断时间
     * @param callInterval 拨打间隔时间
     */
    private void setText(String smsNum, String msmOutTime, String msmInterval,
                         String callNum, String callHangup, String callInterval) {
        ed_msm_timenumber.mEditText.setText(smsNum);
        ed_msm_outtime.mEditText.setText(msmOutTime);
        ed_msm_interval.mEditText.setText(msmInterval);
        ed_call_timenumber.mEditText.setText(callNum);
        ed_call_hangup.mEditText.setText(callHangup);
        ed_call_interval.mEditText.setText(callInterval);
    }

    private void checkBoxColor(int type, CheckBox checkBox) {
        if (checkBox.isChecked()) {
            setTextColor(type, Color.parseColor("#FFFFFF"),
                    Color.parseColor("#52a4d9"), true);
        } else {
            setTextColor(type, Color.parseColor("#6d6d6d"),
                    Color.parseColor("#6d6d6d"), false);
        }
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        rl_setting_incude_more.setOnClickListener(this);
        rl_result_incude_more.setOnClickListener(this);
        iv_incude_remove.setOnClickListener(this);
        ll_msm_content.setOnClickListener(this);
        cb_msm_induce.setOnClickListener(this);
        cb_call_induce.setOnClickListener(this);
        ll_cdma_alleyway.setOnClickListener(this);
        btnStartInduce.setOnClickListener(this);
        btnEndInduce.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_induce: // 开始诱发
                startInduce();
                break;
            case R.id.btn_end_induce:  // 结束诱发
                endInduce();
                break;
            case R.id.ll_msm_content: // 选择短信内容
                listDialog.setTitle(getString(R.string.select_sms_type));
                listDialog.setDatasources(smsContents);
                listDialog.setOnItemClickListener((parent, view, position, id) -> {
                    if (position < smsContents.length) {
                        setMsmContent(position);
                    }
                });
                listDialog.show();
                break;
            case R.id.ll_cdma_alleyway: // 选择诱发通道
                listDialog.setTitle(getString(R.string.check_com));
                listDialog.setDatasources(channels);
                listDialog.setOnItemClickListener((parent, view, position, id) -> {
                    // 选择cardId, 可选通道, 说明必定有两张不一样的卡, 且有一张卡为电信卡,
                    // 因为在 DualSimUtils 初始化过程中, 都将两张卡为同一个运营商的情况归结为 ONLY_CARD_ONE
                    cardId = DualSimUtils.isTelecom(DualSimUtils.simOperator1) ? DualSimUtils.cardId1 : DualSimUtils.cardId2;
                    if (position < channels.length) {
                        setCdmaCardChannel(position == 0);
                    }
                });
                listDialog.show();
                break;
            case R.id.cb_msm_induce:
                // 选择短信诱发
                checkBoxColor(0, cb_msm_induce);
                break;
            case R.id.cb_call_induce:
                // 选择电话诱发
                checkBoxColor(1, cb_call_induce);
                break;
            case R.id.rl_setting_incude_more:
                // 诱发设置栏收缩动画
                animateInduceSetting();
                break;
            case R.id.rl_result_incude_more:
                // 诱发结果收缩动画
                animateResultInduce();
                break;
            case R.id.iv_incude_remove:
                // 清除诱发内容
                clearInduceResult();
                break;
        }
    }

    /**
     * 清除诱发结果
     */
    private void clearInduceResult() {
        // 清除内容
        if (!tv_incude_result.getText().toString().isEmpty()) {
            new AlertDialog.Builder(getActivity())
                    .setMessage("是否清除诱发结果数据!")
                    .setNegativeButton("取消", new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("确定", new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tv_incude_result.setText("");
                        }
                    })
                    .show();
        }
    }

    /**
     * 诱发结果收缩动画
     */
    private void animateResultInduce() {
        // 收缩诱发结果
        if (ll_result_incude.getVisibility() == View.GONE) {
            PushDownAnimation.putDownAnimation(ll_result_incude, null, 500, ll_result_incude.getMeasuredHeight());
            iv_incude_result_more.setBackgroundResource(R.drawable.up);
            PushDownAnimation.startAnimation(getActivity(), iv_incude_result_more);
        } else {
            PushDownAnimation.putUpAnimation(ll_result_incude, null, 500);
            iv_incude_result_more.setBackgroundResource(R.drawable.down);
            PushDownAnimation.startAnimation(getActivity(), iv_incude_result_more);
        }
    }

    /**
     * 诱发设置框动画
     */
    private void animateInduceSetting() {
        if (ll_setting_incude.getVisibility() == View.GONE) {
            PushDownAnimation.putDownAnimation(ll_setting_incude, rl_incude_result_more_all, 500, ll_setting_incude.getMeasuredHeight());
            iv_incude_more.setBackgroundResource(R.drawable.up);
            PushDownAnimation.startAnimation(getActivity(), iv_incude_more);
        } else {
            PushDownAnimation.putUpAnimation(ll_setting_incude, rl_incude_result_more_all, 500);
            iv_incude_more.setBackgroundResource(R.drawable.down);
            PushDownAnimation.startAnimation(getActivity(), iv_incude_more);
        }
    }

    /**
     * 改变编辑状态
     *
     * @param i     0为短信设置 1为拨打设置
     * @param gray  文本颜色值
     * @param blue  输入框颜色值
     * @param fouce 输入框编辑状态
     */
    private void setTextColor(int i, int gray, int blue, boolean fouce) {
        if (i == 0) {
            // 更改字体颜色，灰色是不可编辑
            tv_msm_dotime.setTextColor(gray);
            tv_msm_timenumber.setTextColor(gray);
            tv_msm_outtime.setTextColor(gray);
            tv_msm_time_ms.setTextColor(gray);
            tv_msm_interval.setTextColor(gray);
            tv_msm_dotime.setTextColor(gray);
//			ed_msm_timenumber.setTextColor(blue);
//			ed_msm_outtime.setTextColor(blue);
//			ed_msm_interval.setTextColor(blue);
            ed_msm_timenumber.mEditText.setTextColor(blue);
            ed_msm_outtime.mEditText.setTextColor(blue);
            ed_msm_interval.mEditText.setTextColor(blue);
            tv_msm_interval_ms.setTextColor(gray);
            // 输入框焦点设置
            ed_msm_timenumber.setEnabled(fouce);
            ed_msm_outtime.setEnabled(fouce);
            ed_msm_interval.setEnabled(fouce);
        } else {
            // 更改字体颜色，灰色是不可编辑
            tv_call_dotime.setTextColor(gray);
            tv_call_timenumber.setTextColor(gray);
            tv_call_interval.setTextColor(gray);
            tv_call_interval_ms.setTextColor(gray);
            tv_call_hangup.setTextColor(gray);
            tv_call_hangup_ms.setTextColor(gray);
//			ed_call_timenumber.setTextColor(blue);
//			ed_call_interval.setTextColor(blue);
//			ed_call_hangup.setTextColor(blue);
            ed_call_timenumber.mEditText.setTextColor(blue);
            ed_call_interval.mEditText.setTextColor(blue);
            ed_call_hangup.mEditText.setTextColor(blue);
            // 输入框焦点设置
            ed_call_timenumber.setEnabled(fouce);
            ed_call_interval.setEnabled(fouce);
            ed_call_hangup.setEnabled(fouce);
        }

        //选中拨打诱发的效果
        if (cb_call_induce.isChecked()) {
            ed_call_timenumber.mEditText.setEnabled(true);
            ed_call_interval.mEditText.setEnabled(true);
            ed_call_hangup.mEditText.setEnabled(true);
            ed_call_timenumber.mDropImage.setEnabled(true);
            ed_call_interval.mDropImage.setEnabled(true);
            ed_call_hangup.mDropImage.setEnabled(true);
        } else {
            ed_call_timenumber.mEditText.setEnabled(false);
            ed_call_interval.mEditText.setEnabled(false);
            ed_call_hangup.mEditText.setEnabled(false);
            ed_call_timenumber.mDropImage.setEnabled(false);
            ed_call_interval.mDropImage.setEnabled(false);
            ed_call_hangup.mDropImage.setEnabled(false);
        }

        //选中短信诱发的效果
        if (cb_msm_induce.isChecked()) {
            ed_msm_timenumber.mEditText.setEnabled(true);
            ed_msm_outtime.mEditText.setEnabled(true);
            ed_msm_interval.mEditText.setEnabled(true);
            ed_msm_timenumber.mDropImage.setEnabled(true);
            ed_msm_outtime.mDropImage.setEnabled(true);
            ed_msm_interval.mDropImage.setEnabled(true);
        } else {
            ed_msm_timenumber.mEditText.setEnabled(false);
            ed_msm_outtime.mEditText.setEnabled(false);
            ed_msm_interval.mEditText.setEnabled(false);
            ed_msm_timenumber.mDropImage.setEnabled(false);
            ed_msm_outtime.mDropImage.setEnabled(false);
            ed_msm_interval.mDropImage.setEnabled(false);
        }

    }

    /**
     * 检查短信诱发设置
     */
    private boolean checkMsmSetting() {
        String msm_outtimeString = ed_msm_outtime.getText();
        String msm_intervalString = ed_msm_interval.getText();
        String msm_timenumberString = ed_msm_timenumber.getText();
        // 判空
        if (TextUtils.isEmpty(msm_outtimeString)) {
            ToastUtils.showToast("请设置短信诱发超时");
            return false;
        }
        if (TextUtils.isEmpty(msm_intervalString)) {
            ToastUtils.showToast("请设置短信诱发间隔");
            return false;
        }
        if (TextUtils.isEmpty(msm_timenumberString)) {
            ToastUtils.showToast("请设置短信诱发次数");
            return false;
        }
        // 短信间隔为 4位数到6位数之间
        if (msm_intervalString.length() > 5 || msm_intervalString.length() < 4) {
            ToastUtils.showToast(getString(R.string.interval_tip));
            return false;
        }
        // 超时时间为 4位数到6位数之间
        if (msm_outtimeString.length() > 5 || msm_outtimeString.length() < 4) {
            ToastUtils.showToast(getString(R.string.outtime_tip));
            return false;
        }
        // 短信次数为1到99次
        if (msm_timenumberString.length() > 2 || msm_timenumberString.equals("0")) {
            ToastUtils.showToast(getString(R.string.note_time_number_tip));
            return false;
        }
        // 输入框不能有负数
        if (msm_timenumberString.contains("-")
                || msm_outtimeString.contains("-")
                || msm_intervalString.contains("-")) {
            ToastUtils.showToast(getString(R.string.negative_tip));
            return false;
        }
        msm_timenumber = Integer.parseInt(msm_timenumberString);
        msm_interval = Integer.parseInt(msm_intervalString);
        msm_outtime = Integer.parseInt(msm_outtimeString);

        if (msm_timenumber < 1 || msm_timenumber > 99) {
            ToastUtils.showToast(getString(R.string.note_time_number_tip));
            return false;
        }

        if (msm_interval < 1000 || msm_interval > 99999) {
            ToastUtils.showToast(getString(R.string.interval_tip));
            return false;
        }

        if (msm_outtime < 1000 || msm_outtime > 99999) {
            ToastUtils.showToast(getString(R.string.outtime_tip));
            return false;
        }

        return true;
    }

    /**
     * 根据现有参数, 构造短信内容
     */
    private HrstSmsMessage buildSendMessage() {
        // 获取需要诱发的短信内容
        HrstSmsMessage.Builder builder = new HrstSmsMessage.Builder()
                .setCountryCode(InduceConfigure.countryNum)
                .setSmsPduMode(InduceConfigure.pduMode)
                .setTargetFormat(InduceConfigure.targetInduceFormat)
                .setSendMethod(InduceConfigure.sendMethod);
        Logger.d("send message: " + builder.toString());
        // 常规短信需要回执
        builder.setHasReceive(SendMethod.METHOD_LTE_NORMAL.equals(curSendMethod));
        return builder.build();
    }

    /**
     * 检查拨打设置
     */
    private boolean checkCallSetting() {
        // 判空
        String call_hangupString = ed_call_hangup.getText();
        String call_intervalString = ed_call_interval.getText();
        String call_timenumberString = ed_call_timenumber.getText();
        if (TextUtils.isEmpty(call_hangupString)) {
            ToastUtils.showToast("请设置拨打诱发挂断");
            return false;
        }
        if (TextUtils.isEmpty(call_intervalString)) {
            ToastUtils.showToast("请设置拨打诱发间隔");
            return false;
        }
        if (TextUtils.isEmpty(call_timenumberString)) {
            ToastUtils.showToast("请设置拨打诱发次数");
            return false;
        }
        // 拨打间隔为 4位数到6位数之间
        if (call_intervalString.length() > 5
                || call_intervalString.length() < 4) {
            ToastUtils.showToast(getString(R.string.interval_tip));
            return false;
        }
        // 拨打挂断为 4位数到6位数之间
        if (call_hangupString.length() > 5 || call_hangupString.length() < 4) {
            ToastUtils.showToast(getString(R.string.hangup_tip));
            return false;
        }
        // 拨打次数为1到99次
        if (call_timenumberString.length() > 2 || call_timenumberString.equals("0")) {
            ToastUtils.showToast(getString(R.string.call_time_number_tip));
            return false;
        }
        // 拨打设置中不能有负数
        if (call_hangupString.contains("-")
                || call_intervalString.contains("-")
                || call_timenumberString.contains("-")) {
            ToastUtils.showToast(getString(R.string.negative_tip));
            return false;
        }
        call_hangup = Integer.parseInt(call_hangupString);
        call_interval = Integer.parseInt(call_intervalString);
        call_timenumber = Integer.parseInt(call_timenumberString);

        if (call_timenumber < 1 || call_timenumber > 99) {
            ToastUtils.showToast(getString(R.string.call_time_number_tip));
            return false;
        }

        if (call_interval < 1000 || call_interval > 99999) {
            ToastUtils.showToast(getString(R.string.interval_tip));
            return false;
        }

        if (call_hangup < 1000 || call_hangup > 99999) {
            ToastUtils.showToast(getString(R.string.hangup_tip));
            return false;
        }

        return true;
    }

    /**
     * 设置短信内容
     */
    public void setMsmContent(@CaptureMethod int method) {
        switch (method) {
            case CaptureMethod.NORMAL_CAPTURE:
                curSendMethod = SendMethod.METHOD_LTE_NORMAL;
                ed_msm_interval.mEditText.setText("25000");
                tvMsmContent.setText(getString(R.string.lte_normal_capture));
                break;
            case CaptureMethod.NEW_CAPTURE:
                curSendMethod = SendMethod.METHOD_LTE_NEW;
                tvMsmContent.setText(getString(R.string.lte_new_capture));
                ed_msm_interval.mEditText.setText("3000");
                break;
            case CaptureMethod.PRECISION_CAPTURE:
                curSendMethod = SendMethod.METHOD_LTE_PRECISE;
                tvMsmContent.setText(getString(R.string.lte_precision_capture));
                ed_msm_interval.mEditText.setText("3000");
                break;
        }
        // 缓存默认诱发方式
        InduceConfigure.sendMethod = curSendMethod;
    }

    public void startInduce() {
        operateRecordDao.insertOperateRecord("诱发",
                InduceConfigure.targetInduceFormat,
                InduceConfigure.targetPhone,
                "", "", "", "");
        boolean canInduce = false;
        // 短信诱发
        if (cb_msm_induce.isChecked() && checkMsmSetting() && canInduce()) {
            Logger.d(InduceConfigure.loggerConfigure() + "\n cardId: " + cardId);
            canInduce = true;
            induceFactory.addSmsInduceTask(InduceConfigure.targetPhone, buildSendMessage(), msm_timenumber, msm_outtime, msm_interval, cardId);
        }

        // 电话诱发
        if (cb_call_induce.isChecked() && checkCallSetting() && canInduce()) {
            canInduce = true;
            induceFactory.addCallInduceTask(InduceConfigure.targetPhone, call_timenumber, call_interval, call_hangup);
        }

        if (!canInduce) return;

        // 注册事件监听, 这里肯定是要监听短信广播的的, 没必要再搞一个register
        induceFactory.setInduceCallBack(new InduceCallBack() {
            @Override
            public void onInduceStart(int type) {
                unitStartInduceTime = System.currentTimeMillis();
                switch (type) {
                    case InduceType.SMS_INDUCE:
                        sendNoteNuming++;
                        break;
                    case InduceType.CALL_INDUCE:
                        sendCallNuming++;
                        break;
                }
            }

            @Override
            public void onSend(String resutlCode) {

            }

            @Override
            public void onDelivered() {
                showText("接收到一条信息发送成功！\n");
            }

            @Override
            public void onInduceResult(int type, boolean isSuccess, String resultCode) {
                Logger.d("onInduceResult: " + "type: " + type + " success: " + isSuccess + " resultCode: " + resultCode);
                switch (type) {
                    case InduceType.SMS_INDUCE:
                        showSmsInduceResult(resultCode);
                        break;
                    case InduceType.CALL_INDUCE:
                        showCallInduceResult(isSuccess);
                        break;
                }
            }

            @Override
            public void onInduceFinish() {
//                Logger.d("onInduceFinish");
                showText("发送完毕！共已发送短信：" + sendNoteSusseccSms + "条，拨打电话：" + sendNoteSusseccCall + "次\n");
                ToastUtils.showToast("本轮诱发已完成!");
                btnStartInduce.setEnabled(true);
                btnEndInduce.setEnabled(false);
            }
        });

        // 开始诱发
        induceFactory.startInduce();
        showText("开始诱发 :\n");

        btnStartInduce.setEnabled(false);
        btnEndInduce.setEnabled(true);
        // 后续交互是在上面回调中
    }

    /**
     * 显示电话诱发结果
     *
     * @param success 是否拨打成功
     */
    private void showCallInduceResult(boolean success) {
        if (success) {
            sendNoteSusseccCall++;
            showText("第" + sendCallNuming + "次拨打成功" + "\n");
        } else {
            showText("第" + sendCallNuming + "次拨打失几" + "\n");
        }
    }

    /**
     * 显示短信诱发结果
     */
    private void showSmsInduceResult(String resultCode) {
        switch (resultCode) {
            case InduceConstants.SMS_SENDDING_OK:
                sendNoteSusseccSms++;
                showText("第" + sendNoteNuming + "条信息发送成功	" + (System.currentTimeMillis() - unitStartInduceTime) / 1000 + "秒\n");
                break;
            case InduceConstants.SMS_SENDDING_TIME_OUT:
                showText("第" + sendNoteNuming + "条信息发送超时" + "\n");
                break;
            default:
                // todo 错误情况未处理
                showText("第" + sendNoteNuming + "条信息发送失败	" + (System.currentTimeMillis() - unitStartInduceTime) / 1000 + "秒\n");
                break;
        }
    }

    /**
     * 检测是否达到诱发要求
     */
    private boolean canInduce() {
        if (induceFactory.isInducing()) {
            ToastUtils.showToast("正在开关机检测, 请先停止检测");
            return false;
        }
        switch (DualSimUtils.cardState) {
            case NO_CARD:
                ToastUtils.showToast("请装手机卡");
                return false;
            case ONLY_CARD_ONE:
                if (DualSimUtils.isTelecom(1)) {
                    if (!SimOperator.TELECOM.equals(InduceConfigure.targetPhoneOperator)) {
                        ToastUtils.showToast("电信卡只能诱发电信目标");
                        return false;
                    }
                }
                break;
            case ONLY_CARD_TWO:
                if (DualSimUtils.isTelecom(2)) {
                    if (!SimOperator.TELECOM.equals(InduceConfigure.targetPhoneOperator)) {
                        ToastUtils.showToast("电信卡只能诱发电信目标");
                        return false;
                    }
                }
                break;
        }

        if (!PhoneUtil.isMsgInduce()) {
            ToastUtils.showToast("该手机型号暂不支持诱发功能!");
            return false;
        }

        if (!cb_msm_induce.isChecked() && !cb_call_induce.isChecked()) {
            ToastUtils.showToast(getString(R.string.check_tip));
            return false;
        }
        return true;
    }

    /**
     * 停止诱发
     */
    public void endInduce() {
        showText("停止诱发 ！\n");
        induceFactory.stopInduce();
        msm_timenumber = 1;
        call_timenumber = 1;
    }

    /**
     * 显示文本内容
     *
     * @param str
     */
    private void showText(String str) {
        tv_incude_result.append(str);
        sv_induce.fullScroll(ScrollView.FOCUS_DOWN);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        operateRecordDao.closeDataBase();
        endInduce();
        super.onDestroy();
    }
}
