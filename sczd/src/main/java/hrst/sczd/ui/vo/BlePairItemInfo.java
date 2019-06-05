package hrst.sczd.ui.vo;

/**
 * hrst.sczd.ui.vo
 *  蓝牙配对界面, list view item bean
 *
 * by Sophimp
 * on 2018/4/24
 */

public class BlePairItemInfo {
    private boolean isPaired;
    private boolean isChecked;
    private String name;
    private String rssi;
    private String address;

    public boolean isPaired() {
        return isPaired;
    }

    public void setPaired(boolean paired) {
        isPaired = paired;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "BlePairItemInfo{" +
                "isPaired=" + isPaired +
                ", isChecked=" + isChecked +
                ", name='" + name + '\'' +
                ", rssi='" + rssi + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

}
