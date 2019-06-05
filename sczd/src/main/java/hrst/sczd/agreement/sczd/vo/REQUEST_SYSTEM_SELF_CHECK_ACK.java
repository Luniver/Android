package hrst.sczd.agreement.sczd.vo;

/**
 * 系统自检应答
 * Created by Sophimp on 2019/1/9.
 */

public class REQUEST_SYSTEM_SELF_CHECK_ACK {
    /**
     * 0xFF: crc错误
     * 0x00: 正常
     * 0x01: 无线芯片损坏
     */
    private short ucResult_a;

    public short getUcResult_a() {
        return ucResult_a;
    }

    public void setUcResult_a(short ucResult_a) {
        this.ucResult_a = ucResult_a;
    }
}
