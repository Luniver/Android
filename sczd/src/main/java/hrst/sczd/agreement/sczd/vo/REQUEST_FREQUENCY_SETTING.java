package hrst.sczd.agreement.sczd.vo;

/**
 * 433 频点设置
 * Created by Sophimp on 2019/1/9.
 */

public class REQUEST_FREQUENCY_SETTING {

    /**
     * 频点范围 420 - 435 MHZ
     */
    private int freq_a;

    public REQUEST_FREQUENCY_SETTING(int freq) {
        freq_a = freq;
    }

    public int getFreq_a() {
        return freq_a;
    }

    public void setFreq_a(int freq_a) {
        this.freq_a = freq_a;
    }

    @Override
    public String toString() {
        return "REQUEST_FREQUENCY_SETTING{" +
                "freq_a=" + freq_a +
                '}';
    }
}
