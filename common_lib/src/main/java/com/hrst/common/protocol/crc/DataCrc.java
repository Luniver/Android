package com.hrst.common.protocol.crc;

import android.util.Log;

/**
 * @Author ljw
 * @Date 2015-7-2上午10:08:10
 */
public class DataCrc {

	PathMeasureManagerCallBack callBack;

	static {
		System.loadLibrary("DataCrc");
	}

	public DataCrc(PathMeasureManagerCallBack callBack) {
		this.callBack = callBack;
	}

	/**
	 * @param length
	 * @param data
	 * @return
	 */
	public native int crc(int length, byte[] data);

	/**
	 * @param txDataI
	 * @param txDataQ
	 * @param rxDataI
	 * @param rxDataQ
	 * @param tscLen
	 * @param dataLenPerPath
	 * @param pathNum
	 * @param overSampleRate
	 * @param mode
	 * @param systemSel
	 * @param hardBoardSel
	 */
	public native void pathMeasure(double[][] txDataI, double[][] txDataQ,
			double[][] rxDataI, double[][] rxDataQ, int[] record,
			int tscLen, int dataLenPerPath,
			int pathNum, int overSampleRate, int mode, int systemSel,
			int hardBoardSel, int method);

	/**
	 * 校准回调函数
	 * 
	 * @param calibratDelayArr
	 * @param calibratGainIArr
	 * @param calibratGainQArr
	 * @param calibratGainIndBArr
	 * @param calibratAngleArr
	 */
	public void getMeasureResult(double[] calibratDelayArr,
			double[] calibratGainIArr, double[] calibratGainQArr,
			double[] calibratGainIndBArr, double[] calibratAngleArr, 
			int[] delayRecord) {
		Log.e("ccd1", "校准回调函数Ok");

		// 回调
		callBack.getMeasureResult(calibratDelayArr, calibratGainIArr,
				calibratGainQArr, calibratGainIndBArr, calibratAngleArr, delayRecord);
	}
}