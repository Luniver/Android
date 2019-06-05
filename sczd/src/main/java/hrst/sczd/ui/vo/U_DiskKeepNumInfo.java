package hrst.sczd.ui.vo;

/**
 * TF存数文件列表item信息
 * @author glj
 * 2018-01-29
 */
public class U_DiskKeepNumInfo {
	
	/** 文件名 */
	private String fileName;
	
	/** 选择状态 */
	private boolean isCheck;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	@Override
	public String toString() {
		return "U_DiskKeepNumInfo [fileName=" + fileName + ", isCheck="
				+ isCheck + "]";
	}
	
	
}
