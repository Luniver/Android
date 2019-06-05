package hrst.sczd.agreement.sczd.vo;

/**
 * 频点设置应答
 * Created by Sophimp on 2019/1/9.
 */
public class REQUEST_FREQUENCY_SETTING_ACK {
    /**
     * 频点设置结果
     * 0XFF: crc错误
     * 0x00: 正常
     * 0x01: 频点越界
     */
    private short ucResult_a;

    public short getUcResult_a() {
        return ucResult_a;
    }

    public void setUcResult_a(short ucResult_a) {
        this.ucResult_a = ucResult_a;
    }
}
