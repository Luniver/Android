package hrst.sczd.agreement.sczd.vo;

/**
 * 下发峰均比门限测试指令
 * Created by Sophimp on 2018/6/6.
 */
public class PDA_TO_MCU_REQUEST_PEAK_AVERAGE_RATION {
    /** 频率 khz */
    private int freqKhz_a;

    /**
     * 开关
     */
    private byte openOrclose_b;

    /**
     * 制式:
     * 1 - LTE
     */
    private byte standard_c;

    /**
     * 统计方式
     * 1 - 自动统计, 不带参数
     * 2 - 手动统计, 带参数(门限值)
     */
    private byte statisticMode_d;

    /**
     * 峰均比门限值
     */
    private short ucPeakAvgRatioThreshold_e;

    /**
     * 统计次数
     */
    private int usStatisticTimes_f;

    public int getFreqKhz_a() {
        return freqKhz_a;
    }

    public void setFreqKhz_a(int freqKhz_a) {
        this.freqKhz_a = freqKhz_a;
    }

    public byte getOpenOrclose_b() {
        return openOrclose_b;
    }

    public void setOpenOrclose_b(byte openOrclose_b) {
        this.openOrclose_b = openOrclose_b;
    }

    public byte getStandard_c() {
        return standard_c;
    }

    public void setStandard_c(byte standard_c) {
        this.standard_c = standard_c;
    }

    public byte getStatisticMode_d() {
        return statisticMode_d;
    }

    public void setStatisticMode_d(byte statisticMode_d) {
        this.statisticMode_d = statisticMode_d;
    }

    public short getUcPeakAvgRatioThreshold_e() {
        return ucPeakAvgRatioThreshold_e;
    }

    public void setUcPeakAvgRatioThreshold_e(short ucPeakAvgRatioThreshold_e) {
        this.ucPeakAvgRatioThreshold_e = ucPeakAvgRatioThreshold_e;
    }

    public int getUsStatisticTimes_f() {
        return usStatisticTimes_f;
    }

    public void setUsStatisticTimes_f(int usStatisticTimes_f) {
        this.usStatisticTimes_f = usStatisticTimes_f;
    }

    @Override
    public String toString() {
        return "PDA_TO_MCU_REQUEST_PEAK_AVERAGE_RATION{" +
                "freqKhz_a=" + freqKhz_a +
                ", openOrclose_b=" + openOrclose_b +
                ", standard_c=" + standard_c +
                ", statisticMode_d=" + statisticMode_d +
                ", ucPeakAvgRatioThreshold_e=" + ucPeakAvgRatioThreshold_e +
                ", usStatisticTimes_f=" + usStatisticTimes_f +
                '}';
    }
}
