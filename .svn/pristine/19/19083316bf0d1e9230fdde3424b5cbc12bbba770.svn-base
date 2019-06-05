package hrst.sczd.agreement.sczd.vo;

import java.util.Arrays;

/**
 * 固件升级指令
 * Created by Sophimp on 2019/1/9.
 */
public class REQUEST_FIRM_UPGRADE {
    /**
     * 总帧数
     */
    private int usTotalFrame_a;

    /**
     * 当前帧数
     */
    private int usCurFrame_b;

    /**
     * 数据内容
     * 每一帧数据长度最大为242, 这里初始化为0, 是为了节省内存, 且不空指针, 后续实例化, 会再设置真实数据
     */
    private byte[] content_c = new byte[0];

    public int getUsTotalFrame_a() {
        return usTotalFrame_a;
    }

    public void setUsTotalFrame_a(int usTotalFrame_a) {
        this.usTotalFrame_a = usTotalFrame_a;
    }

    public int getUsCurFrame_b() {
        return usCurFrame_b;
    }

    public void setUsCurFrame_b(int usCurFrame_b) {
        this.usCurFrame_b = usCurFrame_b;
    }

    public byte[] getContent_c() {
        return content_c;
    }

    public void setContent_c(byte[] content_c) {
        this.content_c = content_c;
    }

    @Override
    public String toString() {
        return "REQUEST_FIRM_UPGRADE{" +
                "usTotalFrame_a=" + usTotalFrame_a +
                ", usCurFrame_b=" + usCurFrame_b +
                ", content_c=" + Arrays.toString(content_c) +
                '}';
    }
}
