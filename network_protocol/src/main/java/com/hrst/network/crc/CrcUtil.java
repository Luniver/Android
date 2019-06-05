package com.hrst.network.crc;


/**
 * @author zgz
 * @describe Crc验证
 * @createdate 2014.05.23
 * @version 1.1.1.3
 */
public class CrcUtil {
	private short[] crcTable = new short[256];

	private DataCrc dataCrc = new DataCrc(null);

	/** 生成多项式 */
	private int gPloy = 0x1021;

	/**
	 * 构造方法
	 * 
	 */
	public CrcUtil() {
		computeCrcTable();
	}

	/**
	 * 将字节数组转为short
	 * 
	 * @param data
	 *            数据
	 * @return short 返回
	 */
	public short getCrc(byte[] data) {
		
		// int crc = dataCrc.crc(data.length, data);
		int crc = 0;
		int length = data.length;
		for (int i = 0; i < length; i++) {
			crc = ((crc & 0xFF) << 8)
					^ crcTable[(((crc & 0xFF00) >> 8) ^ data[i]) & 0xFF];
		}
		crc = crc & 0xFFFF;
		return (short) crc;
	}
	
	public short getCrc(int length, byte[] data){
		return (short) dataCrc.crc(length, data);
	}

	/**
	 * 将数字转为short
	 * 
	 * @param aByte
	 *            数据
	 * @return short
	 */
	private short getCrcOfByte(int aByte) {
		int value = aByte << 8;
		for (int count = 7; count >= 0; count--) {
			if ((value & 0x8000) != 0) { // 高第16位为1，可以按位异或
				value = (value << 1) ^ gPloy;
			} else {
				value = value << 1; // 首位为0，左移
			}
		}
		value = value & 0xFFFF; // 取低16位的值
		return (short) value;
	}

	/**
	 * 生成0 - 255对应的CRC16校验码
	 * 
	 */
	private void computeCrcTable() {
		for (int i = 0; i < 256; i++) {
			crcTable[i] = getCrcOfByte(i);
		}
	}
}
