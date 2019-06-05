package hrst.sczd.ui.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

import com.hrst.BluetoothCommHelper;
import com.hrst.common.util.DualSimUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.greenrobot.event.EventBus;
import hrst.sczd.R;
import hrst.sczd.agreement.manager.InteractiveManager;
import hrst.sczd.agreement.sczd.Cmd;
import hrst.sczd.agreement.sczd.McuDataDispatcher;
import hrst.sczd.agreement.sczd.vo.FPGA_DT_TO_PDA_HEART;
import hrst.sczd.agreement.sczd.vo.MCU_TO_PDA_HEART;
import hrst.sczd.agreement.sczd.vo.MCU_TO_PDA_REPORT_ENERGY;
import hrst.sczd.agreement.sczd.vo.MCU_TO_PDA_TEMPERATURE_AND_ELECTRICITY_STATE;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_MODE_SETTING_ACK;

public class HideTestActivity extends ListActivity {

    private static final String TAG = "goo-HideTextActivity";
    String[] titles = {"蓝牙大数据包测试", "蓝牙高频测试", "sim卡读取", "设备温度50度,电量30",
            "mcu心跳", "蓝牙原始数据清零", "语音报数能量值-39, -91, -101", "开始统计数据",
            "结束统计数据", "硬切换测试"};
    private List<Map<String, String>> datasource = new ArrayList<>();
    private int ind = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hide_test);
        initData();
        setListAdapter(
                new SimpleAdapter(this,
                        datasource,
                        android.R.layout.two_line_list_item,
                        new String[]{"title"},
                        new int[]{android.R.id.text1})
        );
        getListView().setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0: // 蓝牙大数据包测试
                    testBigPacket();
                    break;
                case 1: // 蓝牙高频测试
                    testFrequency();
                    break;
                case 2: // sim卡读取
                    readSimId();
                    break;
                case 3: // 设备温度50度
                    MCU_TO_PDA_TEMPERATURE_AND_ELECTRICITY_STATE v1 = new MCU_TO_PDA_TEMPERATURE_AND_ELECTRICITY_STATE();
                    v1.setDigitalTemperature_a((short) 50);
                    v1.setElectricity_c((short) 30);
                    EventBus.getDefault().post(v1);
                    break;
                case 4: // mcu心跳
                    ind++;
                    MCU_TO_PDA_HEART heart = new MCU_TO_PDA_HEART();
                    if (ind % 2 == 0) {
                        heart.setcHeartState_a((byte) -1);
                    } else {
                        heart.setcHeartState_a((byte) 0);
                    }
                    EventBus.getDefault().post(heart);
                    break;
                case 5: // 蓝牙原始数据清零
                    BluetoothCommHelper.get().clearCounter();
                    break;
                case 6: // 语音报数, 能量值 -41
                    ind++;
                    MCU_TO_PDA_REPORT_ENERGY obj = new MCU_TO_PDA_REPORT_ENERGY();
                    if (ind % 3 == 0){
                        obj.setsRxLevInDbm_g((short) -39);
                    }else if(ind % 3 == 1){
                        obj.setsRxLevInDbm_g((short) -91);
                    }else {
                        obj.setsRxLevInDbm_g((short) -101);
                    }
                    byte over = (byte) (new Random(System.currentTimeMillis()).nextInt() % 4);
                    obj.setbIsPowerOverFlow_j(over);
                    EventBus.getDefault().post(obj);
                    break;
                case 7: // 开始统计数据
                    datasource.get(7).put("title", "开始统计(已开始)");
                    datasource.get(8).put("title", "结束统计");
                    ((SimpleAdapter) getListAdapter()).notifyDataSetChanged();
                    McuDataDispatcher.setTesting(true);
                    FPGA_DT_TO_PDA_HEART start = new FPGA_DT_TO_PDA_HEART();
                    start.setOverflow_a((byte) 1);
                    InteractiveManager.getInstance().encapsulateDataAndSend(start, Cmd.DISCARD_TEST_START, McuDataDispatcher.PROPERTY_SC);
                    break;
                case 8: // 结束统计数据
                    datasource.get(7).put("title", "开始统计");
                    datasource.get(8).put("title", "结束统计(已结束)");
                    ((SimpleAdapter) getListAdapter()).notifyDataSetInvalidated();
                    McuDataDispatcher.setTesting(false);
                    FPGA_DT_TO_PDA_HEART end = new FPGA_DT_TO_PDA_HEART();
                    end.setOverflow_a((byte) 0);
                    InteractiveManager.getInstance().encapsulateDataAndSend(end, Cmd.DISCARD_TEST_START, McuDataDispatcher.PROPERTY_SC);
                    break;
                case 9: // 硬切换测试
                    PDA_TO_MCU_REQUEST_MODE_SETTING_ACK modeSetting = new PDA_TO_MCU_REQUEST_MODE_SETTING_ACK();
                    modeSetting.setcResult_a((byte) -6);
                    EventBus.getDefault().post(modeSetting);
                    break;
            }

        });
    }

    private void readSimId() {
        DualSimUtils.loggerShare();
    }

    private void testFrequency() {
        // 高频
        for (int i = 0; i < 100; i++) {
            BluetoothCommHelper.get().write(null, null, fillData(20));
        }
    }

    private void initData() {
        Map<String, String> tmp;
        for (int i = 0; i < titles.length; i++) {
            tmp = new HashMap<>();
            tmp.put("title", titles[i]);
            datasource.add(tmp);
        }
    }

    // 测试大数据
    private void testBigPacket() {
        // 大数据包
        BluetoothCommHelper.get().write(null, null, fillData(88));
    }

    private byte[] fillData(int count) {
        byte[] buf = new byte[count];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = (byte) (i + 1);
        }
        return buf;
    }
}
