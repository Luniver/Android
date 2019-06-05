package hrst.sczd.agreement.sczd.vo;

/**
 * 设备上报固件启动异常
 *
 * @author glj
 *         2018-01-15
 */
public class MCU_TO_PDA_FIRMWARE_STARTUP_ANOMALY {

/*	掩码：
    0x1 FPGA固件异常
	0x2 设备树异常
	0x3 内核固件异常
	0x4 Linux APP程序异常 */

    private byte cErrorCode_a;

    public byte getcErrorCode_a() {
        return cErrorCode_a;
    }

    public void setcErrorCode_a(byte cErrorCode_a) {
        this.cErrorCode_a = cErrorCode_a;
    }

    @Override
    public String toString() {
        return "MCU_TO_PDA_FIRMWARE_STARTUP_ANOMALY [cErrorCode_a="
                + cErrorCode_a + "]";
    }

}
