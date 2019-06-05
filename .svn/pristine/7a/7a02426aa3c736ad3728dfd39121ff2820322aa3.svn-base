package hrst.sczd.ui.manager;

import android.text.TextUtils;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hrst.sczd.agreement.manager.InteractiveManager;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_PEAK_AVERAGE_RATION;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_PEAK_AVERAGE_RATION_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_SENSIBLE_VALID_STATISTIC_ACK;
import hrst.sczd.ui.activity.SensibilityActivity;
import hrst.sczd.ui.adapter.SensibilityResAdapter;
import hrst.sczd.ui.vo.SensibilityResultVO;
import hrst.sczd.utils.log.IOPath;

/**
 * 灵敏度测试管理类
 * Created by Sophimp on 2018/5/28.
 */
public class SensibilityManager {

    private static final String TAG = "goo-SensibilityManager";
    private SensibilityActivity activity;
    private SensibilityResAdapter adapter;

    private List<SensibilityResultVO> datasource;

    private SimpleDateFormat dateFormat;
    private CsvListWriter csvListWriter;
    private final String[] csvHeaders = new String[]{"时间", "频率(MHz)", "检测门限(dB)", "有效率检测"};
    private String fileName;
    //    private final String[] csvHeaders = new String[]{"time", "frequency", "threshold", "sensibility"};

    public SensibilityManager(SensibilityActivity activity) {
        this.activity = activity;
        init();
    }

    private void init() {
        dateFormat = new SimpleDateFormat("HH:mm:ss");
        datasource = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            SensibilityResultVO vo = new SensibilityResultVO();
//            vo.setTime(dateFormat.format(new Date()));
//            vo.setSensibility("-");
//            vo.setFrequency("-");
//            vo.setThreshold("-");
//            datasource.add(0, vo);
//        }
        adapter = new SensibilityResAdapter(activity, datasource);
        activity.setAdapter(adapter);

        // test, 初始化放在第一次获取数据
//        dateFormat.applyPattern("yyyyMMdd-HHmmss");
//        fileName = dateFormat.format(new Date()) + ".csv";
//        dateFormat.applyPattern("HH:mm:ss");

        // 创建目录
        File desFile = new File(IOPath.SENSIBLE_STATISTIC_DATA);
        if (!desFile.exists()){
            desFile.mkdirs();
        }
    }

    /**
     * 开始门限测试
     */
    public void startThresholdStatistic(PDA_TO_MCU_REQUEST_PEAK_AVERAGE_RATION obj) {
        InteractiveManager.getInstance().sendRequestStartThresholdStatistic(obj);
    }

    /**
     * 开始灵敏度有效率测试
     */
    public void startSensibleStatistic() {
        InteractiveManager.getInstance().sendRequestStartSensibleStatistic();
    }

    /**
     * 门限值进度反馈
     */
    public void handleThresholdAck(PDA_TO_MCU_REQUEST_PEAK_AVERAGE_RATION_ACK obj) {
        // 有数据时记录文件名, 选择保存的时候再创建
        if (null == fileName) {
            dateFormat.applyPattern("yyyyMMdd-HHmmss");
            fileName = dateFormat.format(new Date()) + ".csv";
            dateFormat.applyPattern("HH:mm:ss");
        }
        SensibilityResultVO data = new SensibilityResultVO();
        data.setTime(dateFormat.format(new Date()));
        data.setFrequency(activity.getCurFrequency());
        data.setThreshold(obj.getUcPeakThreshold_b() + "");
        if (obj.getProgress_a() == 100) {
            datasource.add(0, data);
        }
        // 根据进度刷新UI
        handleProgress2RefreshUI(obj.getProgress_a());
    }

    /**
     * 灵敏度进度反馈
     */
    public void handleSensibleAck(PDA_TO_MCU_REQUEST_SENSIBLE_VALID_STATISTIC_ACK obj) {
        SensibilityResultVO data = datasource.get(0);
        if (obj.getProgress_a() == 100) {
            // sensible 已经有值, 另开一条数据
            if (!TextUtils.isEmpty(data.getSensibility())) {
                String lastThreshold = data.getThreshold();
                data = new SensibilityResultVO();
                data.setTime(dateFormat.format(new Date()));
                data.setFrequency(activity.getCurFrequency());
                data.setThreshold(lastThreshold);
                datasource.add(0, data);
            }
            data.setSensibility(obj.getUcsensibleResutl_b() + "%");
        }
        handleProgress2RefreshUI(obj.getProgress_a());
    }

    /**
     * 根据进度刷新UI
     */
    private void handleProgress2RefreshUI(int progress) {
        if (progress == 100) {
            // 隐藏进度
            activity.hideProgress();
            activity.enableThresholdStatistic(true);
            activity.enableSensibleStatistic(true);
            adapter.notifyDataSetChanged();
        } else {
            activity.enableThresholdStatistic(false);
            activity.enableSensibleStatistic(false);
            // 显示进度
            activity.showProgress(progress);
        }
    }

    /**
     * 关闭统计
     */
    public void stopSensibleStatistic() {
        InteractiveManager.getInstance().sendRequestStartThresholdStatistic(new PDA_TO_MCU_REQUEST_PEAK_AVERAGE_RATION());
    }

    public void saveData2CsvFiles() {
        File desFile = new File(IOPath.SENSIBLE_STATISTIC_DATA + fileName);
        try {
            if (!desFile.exists()) {
                desFile.createNewFile();
            }
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(desFile), "gbk");
            csvListWriter = new CsvListWriter(writer, CsvPreference.STANDARD_PREFERENCE);
            csvListWriter.writeHeader(csvHeaders);
            for (SensibilityResultVO vo : datasource) {
                csvListWriter.write(vo.getTime(), vo.getFrequency(), vo.getThreshold(), vo.getSensibility());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (csvListWriter != null) {
                try {
                    csvListWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private CellProcessor[] getProcessors() {
        CellProcessor[] processors = new CellProcessor[]{
                new NotNull(),
                new NotNull(),
                new Optional(),
                new Optional()
        };
        return processors;
    }
}
