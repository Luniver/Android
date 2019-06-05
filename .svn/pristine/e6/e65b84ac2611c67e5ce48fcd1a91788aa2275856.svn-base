package hrst.sczd.agreement.sczd.vo;

import java.util.Arrays;

/**
 * 设备主动上报能量
 * @author glj
 * 2018-01-15
 */
public class MCU_TO_PDA_REPORT_ENERGY {
	private int usTargetARFCN_a;						//目标所在频点
	
	private int usTargetTAC_b;							//目标所在TAC
	
	private long uiTargetCID_c;							//目标所在CellId
	
	private float fRxPower_d;			//目标能量值
	
	private float fTargetAmplitude_e ;	//I路
	
	private float fTargetPhase_f;		//Q路
	
	private short sRxLevInDbm_g;		//目标能量值(Dbm)
	
	private int usGpsIdx_h;								//GPS索引
	
	private byte cSNR_i;

	/**
	 * 溢出指示
	 * 0: 无溢出
	 * 1: 干扰溢出
	 * 2: 目标溢出
	 * 3: 总信号(干扰,目标)溢出
	 */
	private byte bIsPowerOverFlow_j;					//溢出指示
	
	private short[] ucRsvd_k = new short[2];			//对其使用,每个字节均填0xFF

	public int getUsTargetARFCN_a() {
		return usTargetARFCN_a;
	}

	public void setUsTargetARFCN_a(int usTargetARFCN_a) {
		this.usTargetARFCN_a = usTargetARFCN_a;
	}

	public int getUsTargetTAC_b() {
		return usTargetTAC_b;
	}

	public void setUsTargetTAC_b(int usTargetTAC_b) {
		this.usTargetTAC_b = usTargetTAC_b;
	}

	public long getUiTargetCID_c() {
		return uiTargetCID_c;
	}

	public void setUiTargetCID_c(long uiTargetCID_c) {
		this.uiTargetCID_c = uiTargetCID_c;
	}

	public float getfRxPower_d() {
		return fRxPower_d;
	}

	public void setfRxPower_d(float fRxPower_d) {
		this.fRxPower_d = fRxPower_d;
	}

	public float getfTargetAmplitude_e() {
		return fTargetAmplitude_e;
	}

	public void setfTargetAmplitude_e(float fTargetAmplitude_e) {
		this.fTargetAmplitude_e = fTargetAmplitude_e;
	}

	public float getfTargetPhase_f() {
		return fTargetPhase_f;
	}

	public void setfTargetPhase_f(float fTargetPhase_f) {
		this.fTargetPhase_f = fTargetPhase_f;
	}

	public short getsRxLevInDbm_g() {
		return sRxLevInDbm_g;
	}

	public void setsRxLevInDbm_g(short sRxLevInDbm_g) {
		this.sRxLevInDbm_g = sRxLevInDbm_g;
	}

	public int getUsGpsIdx_h() {
		return usGpsIdx_h;
	}

	public void setUsGpsIdx_h(int usGpsIdx_h) {
		this.usGpsIdx_h = usGpsIdx_h;
	}

	public byte getcSNR_i() {
		return cSNR_i;
	}

	public void setcSNR_i(byte cSNR_i) {
		this.cSNR_i = cSNR_i;
	}

	public byte getbIsPowerOverFlow_j() {
		return bIsPowerOverFlow_j;
	}

	public void setbIsPowerOverFlow_j(byte bIsPowerOverFlow_j) {
		this.bIsPowerOverFlow_j = bIsPowerOverFlow_j;
	}

	public short[] getUcRsvd_k() {
		return ucRsvd_k;
	}

	public void setUcRsvd_k(short[] ucRsvd_k) {
		this.ucRsvd_k = ucRsvd_k;
	}

	@Override
	public String toString() {
		return "MCU_TO_PDA_REPORT_ENERGY{" +
				"usTargetARFCN_a=" + usTargetARFCN_a +
				", usTargetTAC_b=" + usTargetTAC_b +
				", uiTargetCID_c=" + uiTargetCID_c +
				", fRxPower_d=" + fRxPower_d +
				", fTargetAmplitude_e=" + fTargetAmplitude_e +
				", fTargetPhase_f=" + fTargetPhase_f +
				", sRxLevInDbm_g=" + sRxLevInDbm_g +
				", usGpsIdx_h=" + usGpsIdx_h +
				", cSNR_i=" + cSNR_i +
				", bIsPowerOverFlow_j=" + bIsPowerOverFlow_j +
				", ucRsvd_k=" + Arrays.toString(ucRsvd_k) +
				'}';
	}
}
