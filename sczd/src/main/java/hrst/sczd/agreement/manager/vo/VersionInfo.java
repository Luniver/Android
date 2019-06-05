package hrst.sczd.agreement.manager.vo;

/**
 * 数传终端版本信息
 * @author glj
 * 2018-01-16
 */
public class VersionInfo {
	
	private String linuxSystemFirmwareVersion = "";
	
	private String fpgaFirmwareVersion = "";
	
	private String linuxAppVersion = "";
	
	private String hardwareVersion = "";

	public String getLinuxSystemFirmwareVersion() {
		return linuxSystemFirmwareVersion;
	}

	public void setLinuxSystemFirmwareVersion(String linuxSystemFirmwareVersion) {
		this.linuxSystemFirmwareVersion = linuxSystemFirmwareVersion;
	}

	public String getFpgaFirmwareVersion() {
		return fpgaFirmwareVersion;
	}

	public void setFpgaFirmwareVersion(String fpgaFirmwareVersion) {
		this.fpgaFirmwareVersion = fpgaFirmwareVersion;
	}

	public String getLinuxAppVersion() {
		return linuxAppVersion;
	}

	public void setLinuxAppVersion(String linuxAppVersion) {
		this.linuxAppVersion = linuxAppVersion;
	}

	public String getHardwareVersion() {
		return hardwareVersion;
	}

	public void setHardwareVersion(String hardwareVersion) {
		this.hardwareVersion = hardwareVersion;
	}

	@Override
	public String toString() {
		return "VersionInfo [linuxSystemFirmwareVersion="
				+ linuxSystemFirmwareVersion + ", fpgaFirmwareVersion="
				+ fpgaFirmwareVersion + ", linuxAppVersion=" + linuxAppVersion
				+ ", hardwareVersion=" + hardwareVersion + "]";
	}
}
