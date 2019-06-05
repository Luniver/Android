package hrst.sczd.ui.activity.fragment.fieldAnalysis;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import hrst.sczd.view.bar.BarChartPanel;
import hrst.sczd.agreement.sczd.vo.FPGA_DT_TO_PDA_HEART;
import hrst.sczd.agreement.sczd.vo.MCU_TO_PDA_REPORT_ENERGY;
import hrst.sczd.ui.manager.SkinSettingManager;
import hrst.sczd.ui.manager.TargetManager;
import hrst.sczd.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hrst.common.ui.InduceConfigure;
import com.hrst.induce.annotation.TargetFormat;

/**
 * @author 刘兰
 * @version 1.1.1.3 修改者，修改日期，修改内容
 * @describe 目标信息fragment类
 * @date 2014.05.23
 */
public class TargetFragment extends Fragment {
    private View mView;
    private SkinSettingManager skinManager;

    /**
     * LAC控件
     */
    private TextView lacTxt;

    /**
     * CID控件
     */
    private TextView cidTxt;

    /**
     * 频点控件
     */
    private TextView freqTxt;

    /**
     * 能量控件
     */
    private TextView powerTxt;

    /**
     * 距离控件
     */
    private TextView distanceTxt;

    /**
     * SNR控件
     */
    private TextView snrTxt;

    /**
     * 制式控件
     */
    private TextView standardTxt;

    /**
     * 增益控件
     */
    private TextView gainTxt;

    /**
     * 中标次数控件
     */
    private TextView bidNumTxt;

    /**
     * 能量柱控件
     */
    private BarChartPanel bar_s;

    private TargetManager manager;
    private TextView dtTxt;
    private LinearLayout ll_target_cid;

    /**
     * 构造方法
     */
    public TargetFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_target_sczd,
                (ViewGroup) getActivity().findViewById(R.id.vPager), false);

        ViewGroup group = (ViewGroup) mView.getParent();

        if (group != null) {
            group.removeAllViewsInLayout();
        }
        //初始化控件
        initView();

        //初始化数据
        initData();

        return mView;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        lacTxt = $(R.id.tv_lac_s);
        cidTxt = $(R.id.tv_cid_s);
        freqTxt = $(R.id.tv_pindian_s);
        powerTxt = $(R.id.tv_power_s);
        distanceTxt = $(R.id.tv_targetdistant_s);
        snrTxt = $(R.id.tv_snr_s);
        standardTxt = $(R.id.txt_standard_s);
        gainTxt = $(R.id.tv_gain_type_s);
        bidNumTxt = $(R.id.tv_bid_s);
        bar_s = $(R.id.bar_s);
        dtTxt = $(R.id.tv_dt);
        ll_target_cid = $(R.id.ll_target_cid);
        LinearLayout lacContainer = $(R.id.ll_lac_container);
        LinearLayout snrContainer = $(R.id.ll_snr_container);

        switch (InduceConfigure.targetInduceFormat) {
            case TargetFormat.TARGET_FORMAT_CDMA:
                lacContainer.setVisibility(View.GONE);
                snrContainer.setVisibility(View.GONE);
                break;
            case TargetFormat.TARGET_FORMAT_GSM:
            case TargetFormat.TARGET_FORMAT_TDSCDMA:
            case TargetFormat.TARGET_FORMAT_WCDMA:
                lacContainer.setVisibility(View.GONE);
                snrContainer.setVisibility(View.VISIBLE);
                break;
            case TargetFormat.TARGET_FORMAT_LTE:
                lacContainer.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        EventBus.getDefault().register(this);
        manager = new TargetManager(getActivity());
        manager.initView(lacTxt, cidTxt, freqTxt, powerTxt, distanceTxt, snrTxt,
                standardTxt, gainTxt, bidNumTxt, bar_s, dtTxt);

        ll_target_cid.setOnClickListener(v -> {
            manager.showRadioDialog();
        });
    }

    /**
     * 获取目标定位信息
     *
     * @param obj
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getTargetInfo(MCU_TO_PDA_REPORT_ENERGY obj) {
        manager.setTargetInfo(obj);
    }

    /**
     * 获取Fpga心跳信息
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getFpgaHeart(FPGA_DT_TO_PDA_HEART obj) {
        manager.showFpgaHeart(obj);
    }

    /**
     * 获取数传信号
     */
//    @Subscribe (threadMode = ThreadMode.MainThread)
//    public void getDigitalSignal (MCU_TO_PDA_REPORT_ENERGY obj) {
//    	manager.setDigitalSignal(obj);
//    }
    @Override
    public void onResume() {

        super.onResume();
        //显示增益方式
        manager.showGrowType();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.onDesTory();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 初始化控件工具
     *
     * @param id
     * @return
     */
    private <T> T $(int id) {
        T view = (T) mView.findViewById(id);
        return view;
    }

}
