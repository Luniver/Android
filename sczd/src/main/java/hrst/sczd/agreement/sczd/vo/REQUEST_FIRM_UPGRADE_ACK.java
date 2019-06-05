package hrst.sczd.agreement.sczd.vo;

/**
 * Created by Sophimp on 2019/1/9.
 */

public class REQUEST_FIRM_UPGRADE_ACK {
    /**
     * 总帧数
     */
    private short ucTotalFrame_a;

    /**
     * 当前帧数
     */
    private short ucCurFrame_b;

    /**
     * 状态
     * 0: 正常
     * 1: 数据长度错误
     * 2: 帧号错误
     * 3: 写flash错误
     */
    private byte state_c;

    public short getUcTotalFrame_a() {
        return ucTotalFrame_a;
    }

    public void setUcTotalFrame_a(short ucTotalFrame_a) {
        this.ucTotalFrame_a = ucTotalFrame_a;
    }

    public short getUcCurFrame_b() {
        return ucCurFrame_b;
    }

    public void setUcCurFrame_b(short ucCurFrame_b) {
        this.ucCurFrame_b = ucCurFrame_b;
    }

    public byte getState_c() {
        return state_c;
    }

    public void setState_c(byte state_c) {
        this.state_c = state_c;
    }

    @Override
    public String toString() {
        return "REQUEST_FIRM_UPGRADE_ACK{" +
                "ucTotalFrame_a=" + ucTotalFrame_a +
                ", ucCurFrame_b=" + ucCurFrame_b +
                ", state_c=" + state_c +
                '}';
    }
}

