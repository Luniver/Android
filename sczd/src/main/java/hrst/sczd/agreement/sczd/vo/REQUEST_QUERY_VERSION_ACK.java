package hrst.sczd.agreement.sczd.vo;

/**
 * Created by Sophimp on 2019/1/9.
 */

public class REQUEST_QUERY_VERSION_ACK {
    private short ucState_a;
    private short ucPlatform_b;
    private short ucVersion_c;
    private short ucMonth_d;
    private short ucDay_e;
    private short ucSequence_f;

    public short getUcState_a() {
        return ucState_a;
    }

    public void setUcState_a(short ucState_a) {
        this.ucState_a = ucState_a;
    }

    public short getUcPlatform_b() {
        return ucPlatform_b;
    }

    public void setUcPlatform_b(short ucPlatform_b) {
        this.ucPlatform_b = ucPlatform_b;
    }

    public short getUcVersion_c() {
        return ucVersion_c;
    }

    public void setUcVersion_c(short ucVersion_c) {
        this.ucVersion_c = ucVersion_c;
    }

    public short getUcMonth_d() {
        return ucMonth_d;
    }

    public void setUcMonth_d(short ucMonth_d) {
        this.ucMonth_d = ucMonth_d;
    }

    public short getUcDay_e() {
        return ucDay_e;
    }

    public void setUcDay_e(short ucDay_e) {
        this.ucDay_e = ucDay_e;
    }

    public short getUcSequence_f() {
        return ucSequence_f;
    }

    public void setUcSequence_f(short ucSequence_f) {
        this.ucSequence_f = ucSequence_f;
    }

    @Override
    public String toString() {
        return "REQUEST_QUERY_VERSION_ACK{" +
                "ucState_a=" + ucState_a +
                ", ucPlatform_b=" + ucPlatform_b +
                ", ucVersion_c=" + ucVersion_c +
                ", ucMonth_d=" + ucMonth_d +
                ", ucDay_e=" + ucDay_e +
                ", ucSequence_f=" + ucSequence_f +
                '}';
    }
}
