package hrst.sczd.ui.vo;

/**
 * 灵敏度测试结果
 * Created by Sophimp on 2018/5/28.
 */

public class SensibilityResultVO {
    private String time;
    private String frequency;
    /**
     * 峰均比门限
     */
    private String threshold;
    /**
     * 0-100
     */
    private String sensibility;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    public String getSensibility() {
        return sensibility;
    }

    public void setSensibility(String sensibility) {
        this.sensibility = sensibility;
    }

    public String toString() {
        return "SensibilityResultVO[" +
                "time='" + time + '\'' +
                ", frequency=" + frequency +
                ", threshold=" + threshold +
                ", sensibility=" + sensibility +
                ']';
    }
}
