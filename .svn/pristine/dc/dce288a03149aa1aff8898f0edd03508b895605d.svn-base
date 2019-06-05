package com.hrst.mcu;

import com.hrst.common.protocol.crc.CrcUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Mcu工具类
 * @author glj
 * 2018-01-16
 */
public class McuUtil {
	
	/**
	 * Crc校验
	 * 
	 * @param data
	 *            源数据（命令字+设备属性+数据）
	 * @param procrc
	 *            crc校验字节
	 * 
	 * @return 是否通过
	 */
	public boolean isCrc(byte[] data, byte[] procrc) {
		// 创建crc处理单元
		CrcUtil crcmethod = new CrcUtil();
		// 去除同步头、报文长度和校验码本身长度
		byte[] bytes = new byte[data.length - 8];
		// 拷贝数据
		System.arraycopy(data, 6, bytes, 0, bytes.length);
		// 获取crc
		char crc = (char) crcmethod.getCrc(bytes);
//		System.out.println("goo crc receive: " + crc + "-- " + ConvertUtil.byteToChar(procrc));
		// 验证crc
		return ConvertUtil.byteToChar(procrc) == crc;
	}
	
	/**
	 * 封装协议
	 * @param cmd    命令字
	 * @param property
     *@param data    数据体  @return 消息返回仅用来存储日志使用
	 */
    public static byte[] packingMcuProtocol(short cmd, short property, byte[] data) {
		// 拼装的集合体
		List<Byte> list = new ArrayList<Byte>();
		// 同步头4字节
		list.add((byte) 0xA5);
		list.add((byte) 0x5A);
		list.add((byte) 0xCD);
		list.add((byte) 0xDC);

		// 长度2字节(Crc字节已算进去+2命令字和设备属性的长度)
		char length = (char) (data.length + 4);
		byte[] lengthBytes = ConvertUtil.charToByte(length);
		list.add(lengthBytes[0]);
		list.add(lengthBytes[1]);

		// 设备属性1字节
		list.add((byte) property);

		// 命令字
		list.add((byte) cmd);

		// 数据体(- 2 不包括00 00的Crc部分)
		for (int i = 0; i < data.length; i++) {
			list.add(data[i]);
		}

		// 计算Crc
		CrcUtil crcmethod = new CrcUtil();
		byte[] crcBytes = new byte[2 + data.length];
		crcBytes[0] = (byte) property;
		crcBytes[1] = (byte) cmd;
		System.arraycopy(data, 0, crcBytes, 2, data.length);
		byte[] crc = ConvertUtil.charToByte((char) crcmethod.getCrc(crcBytes));
		for (int i = 0; i < crc.length; i++) {
			list.add(crc[i]);
//			System.out.println("goo send crc bytes: " + Integer.toHexString(crc[i]));
		}
		
		// 转换byte[]
		byte[] returnBytes = new byte[list.size()];
		for (int i = 0; i < list.size(); i++) {
			returnBytes[i] = list.get(i);
		}
//		System.out.println("goo return send heart:" + Arrays.toString(returnBytes));
		//下发消息
//		sendMessage(returnBytes);

		//返回数据
		return returnBytes;
	}

    /**
     * 发送消息
     * 
     * @param data
     *            数据
     */
    private void sendMessage(byte[] data) {
//        if (BluetoothPairingActivity.bluetoothConnection != null) {
//            BluetoothPairingActivity.bluetoothConnection
//                    .setCommunicationThreadWrite(data);
//        }
    }

}
