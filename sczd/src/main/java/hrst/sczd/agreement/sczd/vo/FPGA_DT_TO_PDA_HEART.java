package hrst.sczd.agreement.sczd.vo;

/**
 * FPGA数传对PDA的心跳
 * @author glj
 * 2018-01-15
 */
public class FPGA_DT_TO_PDA_HEART {

    /**
     * 溢出指示标志
     */
    private byte overflow_a;

    public byte getOverflow_a() {
        return overflow_a;
    }

    public void setOverflow_a(byte overflow_a) {
        this.overflow_a = overflow_a;
    }

    @Override
    public String toString() {
        return "FPGA_DT_TO_PDA_HEART{" +
                "overflow_a=" + overflow_a +
                '}';
    }
}
